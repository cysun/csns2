-- This script creates all the tables/indexes/procedures/triggers and some
-- data required for an initial CSNS setup.

set client_min_messages=WARNING;

create sequence hibernate_sequence minvalue 2000000;

---------------------
-- users and roles --
---------------------

create table users (
    id              bigint primary key,
    cin             varchar(255) not null unique,
    username        varchar(255) not null unique,
    password        varchar(255) not null,
    last_name       varchar(255) not null,
    first_name      varchar(255) not null,
    middle_name     varchar(255),
    gender          char(1) check( gender = 'M' or gender = 'F' ),
    birthday        date,
    street          varchar(255),
    city            varchar(255),
    state           varchar(255),
    zip             varchar(255),
    primary_email   varchar(255) not null unique,
    secondary_email varchar(255),
    cell_phone      varchar(255),
    home_phone      varchar(255),
    office_phone    varchar(255),
    enabled         boolean not null default 't',
    temporary       boolean not null default 'f'
);

create table authorities (
    user_id bigint not null references users(id),
    role    varchar(255)
);

insert into users (id, cin, username, password, last_name, first_name, primary_email) values
    (1000, '1000', 'sysadmin', md5('abcd'), 'System', 'Administrator', 'csnsadmin@localhost.localdomain');

insert into authorities (user_id, role) values (1000, 'ROLE_ADMIN');

-- for remember-me service
create table persistent_logins (
    series      varchar(64) primary key,
    username    varchar(64) not null,
    token       varchar(64) not null,
    last_used   timestamp not null
);

-----------
-- files --
-----------

create table files (
    id              bigint primary key,
    name            varchar(255) not null,
    type            varchar(255),
    size            bigint,
    date            timestamp not null default current_timestamp,
    owner_id        bigint not null references users(id),
    parent_id       bigint references files(id),
    folder          boolean not null default 'f',
    public          boolean not null default 'f',
    submission_id   bigint,
    regular         boolean not null default 'f',
    deleted         boolean not null default 'f'
);

--------------------------
-- question and answers --
--------------------------

create table question_sheets (
    id          bigint primary key,
    description varchar(8000)
);

create table question_sections (
    id                  bigint primary key,
    description         varchar(8000),
    question_sheet_id   bigint references question_sheets(id),
    section_index       integer,
  unique (question_sheet_id, section_index)
);

create table questions (
    id                  bigint primary key,
    question_type       varchar(255) not null,
    description         varchar(8000),
    point_value         integer not null default 1,
    min_selections      integer,
    max_selections      integer,
    min_rating          integer,
    max_rating          integer,
    text_length         integer,
    attachment_allowed  boolean not null default 'f',
    correct_answer      varchar(8000),
    question_section_id bigint references question_sections(id),
    question_index      integer,
  unique (question_section_id, question_index)
);

create table question_choices (
    question_id     bigint not null references questions(id),
    choice          varchar(4000),
    choice_index    integer not null,
  primary key (question_id, choice_index)
);

create table question_correct_selections (
    question_id bigint not null references questions(id),
    selection   integer,
  primary key (question_id, selection)
);

create table answer_sheets (
    id                  bigint primary key,
    question_sheet_id   bigint not null references question_sheets(id),
    author_id           bigint references users(id),
    date                timestamp
);

create table answer_sections (
    id              bigint primary key,
    answer_sheet_id bigint not null references answer_sheets(id),
    section_index   integer not null,
  unique (answer_sheet_id, section_index)
);

create table answers (
    id                  bigint primary key,
    answer_type         varchar(255) not null,
    answer_section_id   bigint not null references answer_sections(id),
    answer_index        integer not null,
    question_id         bigint references questions(id),
    rating              integer,
    text                varchar(8000),
    attachment_id       bigint unique references files(id),
  unique (answer_section_id, answer_index)
);

create table answer_selections (
    answer_id   bigint not null references answers(id),
    selection   integer
);

------------
-- grades --
------------

create table grades (
    id          bigint primary key,
    symbol      varchar(255) not null unique,
    value       double precision,
    description varchar(1000)
);

-- The following grades are defined in the Faculty Handbook
-- (http://www.calstatela.edu/academic/senate/handbook/) under the
-- chpater Instructional Policies.
  
insert into grades (id, symbol, value, description) values
    (2000, 'A', 4.0, 'Superior Attainment of Course Objectives');
insert into grades (id, symbol, value, description) values
    (2002, 'A-', 3.7, 'Outstanding Attainment of Course Objectives');
insert into grades (id, symbol, value, description) values
    (2004, 'B+', 3.3, 'Very Good Attainment of Course Objectives');
insert into grades (id, symbol, value, description) values
    (2006, 'B', 3.0, 'Good Attainment of Course Objectives');
insert into grades (id, symbol, value, description) values
    (2008, 'B-', 2.7, 'Fairly Good Attainment of Course  Objectives');
insert into grades (id, symbol, value, description) values
    (2010, 'C+', 2.3, 'Above Average Attainment of Course Objectives');
insert into grades (id, symbol, value, description) values
    (2012, 'C', 2.0, 'Average Attainment of Course Objectives');
insert into grades (id, symbol, value, description) values
    (2014, 'C-', 1.7, 'Below Average Attainment of Course Objectives');
insert into grades (id, symbol, value, description) values
    (2016, 'D+', 1.3, 'Weak Attainment of Course Objectives');
insert into grades (id, symbol, value, description) values
    (2018, 'D', 1.0, 'Poor Attainment of Course Objectives');
insert into grades (id, symbol, value, description) values
    (2020, 'D-', 0.7, 'Barely Passing Attainment of Course Objectives');
insert into grades (id, symbol, value, description) values
    (2022, 'F', 0.0, 'Non-Attainment of Course Objectives');
insert into grades (id, symbol, description) values
    (2024, 'CR', 'Credit');
insert into grades (id, symbol, description) values
    (2026, 'NC', 'No Credit');
insert into grades (id, symbol, description) values
    (2028, 'RP', 'Report in Progress');
insert into grades (id, symbol, description) values
    (2030, 'RD', 'Report Delayed');
insert into grades (id, symbol, description) values
    (2032, 'I', 'Incomplete  Authorized');
insert into grades (id, symbol, value, description) values
    (2034, 'IC', 0.0, 'Incompleted Charged');
insert into grades (id, symbol, description) values
    (2036, 'W', 'Withdraw');
insert into grades (id, symbol, value, description) values
    (2038, 'WU', 0.0, 'Withdraw Unauthorized');

----------------------------------------
-- courses, sections, and enrollments --
----------------------------------------

create table courses (
    id              bigint primary key,
    code            varchar(255) not null unique,
    name            varchar(255) not null,
    min_units       integer not null default 4,
    max_units       integer not null default 4,
    coordinator_id  bigint references users(id),
    syllabus_id     bigint references files(id),
    obsolete        boolean not null default 'f'
);

alter table courses add column tsv tsvector;

create function courses_ts_trigger_function() returns trigger as $$
begin
    new.tsv :=
        setweight( to_tsvector(coalesce(new.code,'')), 'A') ||
        setweight( to_tsvector(coalesce(new.name,'')), 'B' );
    return new;
end
$$ language plpgsql;

create trigger courses_ts_trigger
    before insert or update
    on courses
    for each row
    execute procedure courses_ts_trigger_function();

create index courses_ts_index
    on courses
    using gin(tsv);

create table sections (
    id              bigint primary key,
    quarter         integer not null,
    course_id       bigint not null references courses(id),
    number          integer not null default 1,
  unique (quarter, course_id, number)
);

create table section_instructors (
    section_id          bigint not null references sections(id),
    instructor_id       bigint not null references users(id),
    instructor_order    integer not null,
  primary key (section_id, instructor_order)
);

create table enrollments (
    id              bigint primary key,
    section_id      bigint not null references sections(id),
    student_id      bigint not null references users(id),
    grade_id        bigint references grades(id),
    comments        text,
    grade_mailed    boolean not null default 'f',
  unique (section_id, student_id)
);

--------------------------------
-- assignments and submission --
--------------------------------

create table assignments (
    id                          bigint primary key,
    assignment_type             varchar(255) not null default 'REGULAR',
    name                        varchar(255) not null,
    alias                       varchar(255) not null,
    total_points                varchar(255),
    section_id                  bigint references sections(id),
    publish_date                timestamp,
    due_date                    timestamp,
    max_file_size               bigint,
    file_extensions             varchar(255),
    available_after_due_date    boolean not null default 't',
    question_sheet_id           bigint unique references question_sheets(id)
);

alter table assignments add column tsv tsvector;

create function assignments_ts_trigger_function() returns trigger as $$
declare
    l_quarter       varchar;
    l_course_code   varchar;
begin
    if new.section_id is not null then
        select quarter(quarter) into l_quarter from sections
            where id = new.section_id;
        select c.code into l_course_code from sections s, courses c
            where s.id = new.section_id and c.id = s.course_id;
    end if;
    new.tsv :=
        setweight( to_tsvector(coalesce(l_quarter,'')), 'A') ||
        setweight( to_tsvector(coalesce(l_course_code,'')), 'A') ||
        setweight( to_tsvector(coalesce(new.name,'')), 'A' );
    return new;
end
$$ language plpgsql;

create trigger assignments_ts_trigger
    before insert or update
    on assignments
    for each row
    execute procedure assignments_ts_trigger_function();

create index assignments_ts_index
    on assignments
    using gin(tsv);

create table submissions (
    id              bigint primary key,
    submission_type varchar(255) not null default 'REGULAR',
    student_id      bigint not null references users(id),
    assignment_id   bigint not null references assignments(id),
    due_date        timestamp,
    grade           varchar(255),
    comments        text,
    grade_mailed    boolean not null default 'f',
    answer_sheet_id bigint unique references answer_sheets(id),
    saved           boolean not null default 'f',
    finished        boolean not null default 'f',
  unique (student_id, assignment_id)
);

alter table files add constraint files_submission_id_fkey
    foreign key (submission_id) references submissions(id);

-----------------
-- departments --
-----------------

create table departments (
    id              bigint primary key,
    name            varchar(255) not null unique,
    abbreviation    varchar(255) not null unique,
    welcome_message varchar(8000)
);

create table department_administrators (
    department_id   bigint not null references departments(id),
    user_id         bigint not null references users(id)
);

create table department_faculty (
    department_id   bigint not null references departments(id),
    user_id         bigint not null references users(id)
);

create table department_instructors (
    department_id   bigint not null references departments(id),
    user_id         bigint not null references users(id)
);

create table department_reviewers (
    department_id   bigint not null references departments(id),
    user_id         bigint not null references users(id)
);

create table department_undergraduate_courses (
    department_id   bigint not null references departments(id),
    course_id       bigint not null references courses(id)
);
    
create table department_additional_undergraduate_courses (
    department_id   bigint not null references departments(id),
    course_id       bigint not null references courses(id)
);

create table department_graduate_courses (
    department_id   bigint not null references departments(id),
    course_id       bigint not null references courses(id)
);

create table department_additional_graduate_courses (
    department_id   bigint not null references departments(id),
    course_id       bigint not null references courses(id)
);

-------------
-- surveys --
-------------

create table surveys (
    id                  bigint primary key,
    name                varchar(255) not null,
    type                varchar(255) default 'ANONYMOUS',
    question_sheet_id   bigint not null unique references question_sheets(id),
    publish_date        timestamp,
    close_date          timestamp,
    department_id       bigint references departments(id),
    author_id           bigint not null references users(id),
    date                timestamp not null default current_timestamp,
    deleted             boolean not null default 'f'
);

create table survey_responses (
    id              bigint primary key,
    survey_id       bigint not null references surveys(id),
    answer_sheet_id bigint not null unique references answer_sheets(id)
);

create table surveys_taken (
    user_id     bigint not null references users(id),
    survey_id   bigint not null references surveys(id),
  primary key (user_id, survey_id)
);

------------------------------
-- functions and procedures --
------------------------------

--
-- Given a date, returns the quarter.
--
create or replace function quarter( p_date date ) returns integer as $$
declare
    l_code integer := (extract(year from p_date) - 1900) * 10;
    l_week integer := extract(week from p_date);
begin
    if l_week < 13 then
        l_code := l_code + 1;
    elsif l_week < 25 then
        l_code := l_code + 3;
    elsif l_week < 38 then
        l_code := l_code + 6;
    else
        l_code := l_code + 9;
    end if;
    return l_code;
end;
$$ language plpgsql;

--
-- Returns the current quarter.
--
create or replace function quarter() returns integer as $$
begin
    return quarter(current_date);
end;
$$ language plpgsql;

--
-- Given a quarter code, returns the quarter name (e.g. Fall 2012).
--
create or replace function quarter( p_code integer ) returns varchar as $$
declare
    l_year      varchar;
    l_quarter   varchar;
begin
    l_year := cast( p_code/10+1900 as varchar );

    case p_code % 10
        when 1 then
            l_quarter = 'Winter';
        when 3 then
            l_quarter = 'Spring';
        when 6 then
            l_quarter = 'Summer';
        else
            l_quarter = 'Fall';
    end case;

    return l_quarter || ' ' || l_year;
end;
$$ language plpgsql;