-- This script creates all the tables/indexes/procedures/triggers and some
-- data required for an initial CSNS setup.

set client_min_messages=WARNING;

create sequence hibernate_sequence minvalue 2000000;

-- This sequence is used to generate peudo-id's for query results.
create sequence result_sequence cycle;

------------------------------
-- users, roles, and groups --
------------------------------

create table users (
    id                      bigint primary key,
    cin                     varchar(255) not null unique,
    username                varchar(255) not null unique,
    password                varchar(255) not null,
    last_name               varchar(255) not null,
    first_name              varchar(255) not null,
    middle_name             varchar(255),
    street                  varchar(255),
    city                    varchar(255),
    state                   varchar(255),
    zip                     varchar(255),
    primary_email           varchar(255) not null unique,
    secondary_email         varchar(255),
    cell_phone              varchar(255),
    home_phone              varchar(255),
    work_phone              varchar(255),
    original_picture_id     bigint,
    profile_picture_id      bigint,
    profile_thumbnail_id    bigint,
    enabled                 boolean not null default 't',
    temporary               boolean not null default 'f',
    major_id                bigint,
    personal_program_id     bigint,
    num_of_forum_posts      integer not null default 0,
    disk_quota              integer not null default 200,
    access_key              varchar(255) unique
);

create unique index user_cin_index on users (cin varchar_pattern_ops);
create unique index user_username_index
    on users (lower(username) varchar_pattern_ops);
create unique index user_primary_email_index
    on users (lower(primary_email) varchar_pattern_ops);

create index user_firstname_index
    on users (lower(first_name) varchar_pattern_ops);
create index user_lastname_index
    on users (lower(last_name) varchar_pattern_ops);
create index user_fullname_index
    on users (lower(first_name || ' ' || last_name) varchar_pattern_ops);

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

create table groups (
    id              bigint primary key,
    department_id   bigint,
    name            varchar(255) not null,
    description     varchar(8000),
    date            timestamp not null default current_timestamp
);

create table members (
    id          bigint primary key,
    group_id    bigint references groups(id),
    user_id     bigint references users(id),
    date        timestamp not null default current_timestamp,
  unique (group_id, user_id)
);

-------------------------
-- files and resources --
-------------------------

create table files (
    id              bigint primary key,
    name            varchar(255) not null,
    type            varchar(255),
    size            bigint,
    date            timestamp not null default current_timestamp,
    owner_id        bigint not null references users(id),
    parent_id       bigint references files(id),
    reference_id    bigint references files(id),
    folder          boolean not null default 'f',
    public          boolean not null default 'f',
    submission_id   bigint,
    regular         boolean not null default 'f',
    deleted         boolean not null default 'f'
);

alter table users add constraint original_picture_id_fkey
    foreign key (original_picture_id) references files(id);
alter table users add constraint profile_picture_id_fkey
    foreign key (profile_picture_id) references files(id);
alter table users add constraint profile_thumbnail_id_fkey
    foreign key (profile_thumbnail_id) references files(id);

create table resources (
    id      bigint primary key,
    name    varchar(255),
    type    integer not null,
    text    text,
    file_id bigint references files(id),
    url     varchar(2000),
    private boolean not null default 'f',
    deleted boolean not null default 'f'
);

alter table resources add column tsv tsvector;

create function resources_ts_trigger_function() returns trigger as $$
declare
    l_filename  varchar;
begin
	if new.type = 1 then
        new.tsv := setweight(to_tsvector(new.text), 'D');
    elsif new.type = 2 then
        select name into l_filename from files where id = new.file_id;
        new.tsv := setweight(to_tsvector(l_filename), 'D');
    else
        new.tsv := setweight(to_tsvector(new.url), 'D');
    end if;
    return new;
end
$$ language plpgsql;

create trigger resources_ts_trigger
    before insert or update on resources
    for each row execute procedure resources_ts_trigger_function();

create index resources_ts_index on resources using gin(tsv);

-------------------
-- subscriptions --
-------------------

create table subscriptions (
    id                  bigint primary key,
    subscribable_type   char(2) not null,
    subscribable_id     bigint not null,
    subscriber_id       bigint not null references users(id),
    date                timestamp not null default current_timestamp,
    term                integer,
    notification_sent   boolean not null default 'f',
    auto_subscribed     boolean not null default 'f',
  unique (subscribable_type, subscribable_id, subscriber_id)
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
    description         varchar(80000),
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
    text                text,
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

----------------------------------------------------
-- courses, sections, enrollments, and attendance --
----------------------------------------------------

create table courses (
    id                  bigint primary key,
    department_id       bigint,
    code                varchar(255) not null unique,
    name                varchar(255) not null,
    units               integer not null default 3,
    unit_factor         double precision not null default 1.0,
    coordinator_id      bigint references users(id),
    description_id      bigint references files(id),
    catalog_description varchar(8000),
    journal_id          bigint unique,
    obsolete            boolean not null default 'f'
);

alter table courses add column tsv tsvector;

create function courses_ts_trigger_function() returns trigger as $$
begin
    new.tsv := setweight(to_tsvector(new.code), 'A') ||
               setweight(to_tsvector(new.name), 'B');
    return new;
end
$$ language plpgsql;

create trigger courses_ts_trigger
    before insert or update on courses
    for each row execute procedure courses_ts_trigger_function();

create index courses_ts_index on courses using gin(tsv);

create table course_prerequisites (
    course_id       bigint not null references courses(id),
    prerequisite_id bigint not null references courses(id),
  unique (course_id, prerequisite_id)
);

create table sections (
    id          bigint primary key,
    term        integer not null,
    course_id   bigint not null references courses(id),
    number      integer not null default 1,
    syllabus_id bigint references resources(id),
    journal_id  bigint unique,
    deleted     boolean not null default 'f',
  unique (term, course_id, number)
);

alter table sections add column tsv tsvector;

create function sections_ts_trigger_function() returns trigger as $$
declare
    l_user      users%rowtype;
    l_course    courses%rowtype;
begin
	select * into l_course from courses where id = new.course_id;
    new.tsv := setweight(to_tsvector(l_course.code), 'A') ||
               setweight(to_tsvector(l_course.name), 'B') ||
               setweight(to_tsvector(term(new.term)), 'A');
    for l_user in select u.* from users u, section_instructors i
        where i.section_id = new.id and i.instructor_id = u.id loop
            new.tsv := new.tsv ||
                setweight(to_tsvector(l_user.first_name), 'B') ||
                setweight(to_tsvector(l_user.last_name), 'B') ||
                setweight(to_tsvector(l_user.username), 'B');
    end loop;
    return new;
end
$$ language plpgsql;

create trigger sections_ts_trigger
    before insert or update on sections
    for each row execute procedure sections_ts_trigger_function();

create index sections_ts_index on sections using gin(tsv);

create table section_instructors (
    section_id          bigint not null references sections(id),
    instructor_id       bigint not null references users(id),
    instructor_order    integer not null,
  primary key (section_id, instructor_order)
);

create function section_instructors_ts_trigger_function() returns trigger as $$
begin
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        update sections set tsv = '' where id = new.section_id;
    end if;
    if tg_op = 'DELETE' or tg_op = 'UPDATE' then
        update sections set tsv = '' where id = old.section_id;
    end if;
    return null;
end
$$ language plpgsql;

create trigger section_instructors_ts_trigger
    after insert or delete or update on section_instructors
    for each row execute procedure section_instructors_ts_trigger_function();

create table enrollments (
    id              bigint primary key,
    section_id      bigint not null references sections(id),
    student_id      bigint not null references users(id),
    grade_id        bigint references grades(id),
    comments        text,
    grade_mailed    boolean not null default 'f',
  unique (section_id, student_id)
);

create table attendance_events (
    id      bigint primary key,
    name    varchar(255)
);

create table attendance_records (
    id          bigint primary key,
    event_id    bigint not null references attendance_events(id),
    user_id     bigint not null references users(id),
    attended    boolean,
  unique (event_id, user_id)
);

create table section_attendance_events (
    section_id  bigint not null references sections(id),
    event_id    bigint not null references attendance_events(id)
);

--------------------------------
-- assignments and submission --
--------------------------------

create table assignments (
    id                          bigint primary key,
    assignment_type             varchar(255) not null default 'REGULAR',
    name                        varchar(255) not null,
    resource_id                 bigint references resources(id),
    alias                       varchar(255) not null,
    total_points                varchar(255),
    section_id                  bigint not null references sections(id),
    publish_date                timestamp,
    due_date                    timestamp,
    max_file_size               bigint,
    file_extensions             varchar(255),
    available_after_due_date    boolean not null default 't',
    question_sheet_id           bigint unique references question_sheets(id),
    deleted                     boolean not null default 'f'
);

alter table assignments add column tsv tsvector;

create function assignments_ts_trigger_function() returns trigger as $$
declare
    l_term          varchar;
    l_course_code   varchar;
begin
    if new.section_id is not null then
        select term(term) into l_term from sections
            where id = new.section_id;
        select c.code into l_course_code from sections s, courses c
            where s.id = new.section_id and c.id = s.course_id;
    end if;
    new.tsv := setweight(to_tsvector(l_term), 'A') ||
               setweight(to_tsvector(l_course_code), 'A') ||
               setweight(to_tsvector(new.name), 'A');
    return new;
end
$$ language plpgsql;

create trigger assignments_ts_trigger
    before insert or update on assignments
    for each row execute procedure assignments_ts_trigger_function();

create index assignments_ts_index on assignments using gin(tsv);

create table submissions (
    id              bigint primary key,
    submission_type varchar(255) not null default 'REGULAR',
    student_id      bigint not null references users(id),
    assignment_id   bigint not null references assignments(id),
    due_date        timestamp,
    grade           varchar(255),
    comments        text,
    grade_mailed    boolean not null default 'f',
    file_count      integer not null default 0,
    answer_sheet_id bigint unique references answer_sheets(id),
    saved           boolean not null default 'f',
    finished        boolean not null default 'f',
  unique (student_id, assignment_id)
);

alter table files add constraint files_submission_id_fkey
    foreign key (submission_id) references submissions(id);

---------------------
-- course journals --
---------------------

create table course_journals (
    id              bigint primary key,
    section_id      bigint references sections(id),
    submit_date     timestamp,
    approve_date    timestamp
);

create table course_journal_handouts (
    course_journal_id   bigint not null references course_journals(id),
    resource_id         bigint not null references resources(id),
    handout_order       integer not null,
  primary key (course_journal_id, handout_order)
);

create table course_journal_assignments (
    course_journal_id   bigint not null references course_journals(id),
    assignment_id       bigint not null references assignments(id),
    assignment_order    integer not null,
  primary key (course_journal_id, assignment_order)
);

create table course_journal_rubric_assignments (
    course_journal_id   bigint not null references course_journals(id),
    assignment_id       bigint not null,
    assignment_order    integer not null,
  primary key (course_journal_id, assignment_order)
);

create table course_journal_student_samples (
    course_journal_id   bigint not null references course_journals(id),
    enrollment_id       bigint not null references enrollments(id),
  unique (course_journal_id, enrollment_id)
);

alter table courses add constraint courses_journal_id_fkey
    foreign key (journal_id) references course_journals(id);
alter table sections add constraint sections_journal_id_fkey
    foreign key (journal_id) references course_journals(id);

-----------------
-- departments --
-----------------

create table departments (
    id              bigint primary key,
    name            varchar(255) not null unique,
    full_name       varchar(255) not null unique,
    abbreviation    varchar(255) not null unique,
    welcome_message text
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

create table department_evaluators (
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

create table department_graduate_courses (
    department_id   bigint not null references departments(id),
    course_id       bigint not null references courses(id)
);

create table department_options (
    department_id   bigint not null references departments(id),
    option          varchar(255) not null,
  primary key (department_id, option)
);

alter table users add constraint users_major_fk
    foreign key (major_id) references departments(id);
alter table groups add constraint groups_department_fk
    foreign key (department_id) references departments(id);
alter table courses add constraint courses_department_fk
    foreign key (department_id) references departments(id);

--------------
-- programs --
--------------

create table programs (
    id              bigint primary key,
    department_id   bigint references departments(id),
    name            varchar(255) not null,
    description     varchar(8000),
    publish_date    timestamp,
    published_by    bigint references users(id),
    obsolete        boolean not null default 'f'
);

create table program_blocks (
    id              bigint primary key,
    program_id      bigint references programs(id),
    block_index     integer,
    name            varchar(255),
    description     varchar(8000),
    units_required  integer,
    require_all     boolean not null default 't'
);

create table program_block_courses (
    block_id    bigint not null references program_blocks(id),
    course_id   bigint not null references courses(id)
);

create table personal_programs (
    id              bigint primary key,
    student_id      bigint references users(id),
    program_id      bigint references programs(id),
    date            timestamp not null default current_timestamp,
    approve_date    timestamp,
    approved_by     bigint references users(id),
  unique (student_id, program_id)
);

create table personal_program_blocks (
    id                  bigint primary key,
    program_block_id    bigint references program_blocks(id),
    program_id          bigint references personal_programs(id),
    block_index         integer
);

create table personal_program_entries (
    id              bigint primary key,
    block_id        bigint references personal_program_blocks(id),
    course_id       bigint references courses(id),
    enrollment_id   bigint references enrollments(id),
    prereq_met      boolean not null default 'f',
    requirement_met boolean not null default 'f'
);

alter table users add constraint users_personal_program_id_fkey
    foreign key (personal_program_id) references personal_programs(id);

alter table programs add column tsv tsvector;

create function programs_ts_trigger_function() returns trigger as $$
declare
    l_department    departments%rowtype;
begin
    if new.department_id is not null then
        select * into l_department from departments where id = new.department_id;
        new.tsv := setweight(to_tsvector(l_department.name), 'A') ||
                   setweight(to_tsvector(l_department.abbreviation), 'A') ||
                   setweight(to_tsvector(new.name), 'A') ||
                   setweight(to_tsvector(new.description), 'D');
    end if;
    return new;
end
$$ language plpgsql;

create trigger programs_ts_trigger
    before insert or update on programs
    for each row execute procedure programs_ts_trigger_function();

create index programs_ts_index on programs using gin(tsv);

---------------------
-- course mappings --
---------------------

create table course_mappings (
    id              bigint primary key,
    department_id   bigint not null references departments(id),
    deleted         boolean not null default 'f'
);

create table course_mapping_group1 (
    mapping_id  bigint not null references course_mappings(id),
    course_id   bigint not null references courses(id),
  unique (mapping_id, course_id)
);

create table course_mapping_group2 (
    mapping_id  bigint not null references course_mappings(id),
    course_id   bigint not null references courses(id),
  unique (mapping_id, course_id)
);

-----------
-- sites --
-----------

create table sites (
    id          bigint primary key,
    section_id  bigint unique references sections(id),
    folder_id   bigint references files(id),
    restricted  boolean not null default 'f',
    limited     boolean not null default 'f',
    shared      boolean not null default 'f'
);

create table site_class_info (
    site_id     bigint not null references sites(id),
    entry_index integer not null,
    name        varchar(255),
    value       varchar(8000),
  primary key (site_id, entry_index)
);

create table site_announcements (
    id      bigint primary key,
    date    timestamp not null default current_timestamp,
    content varchar(4000),
    site_id bigint references sites(id)
);

create table site_blocks (
    id          bigint primary key,
    name        varchar(255),
    type        varchar(255) not null default 'REGULAR',
    hidden      boolean not null default 'f',
    site_id     bigint references sites(id),
    block_index integer
);

create table site_items (
    id          bigint primary key,
    resource_id bigint references resources(id),
    hidden      boolean not null default 'f',
    deleted     boolean not null default 'f',
    block_id    bigint references site_blocks(id),
    item_index  integer
);

create table site_item_additional_resources (
    item_id         bigint not null references site_items(id),
    resource_id     bigint not null references resources(id),
    resource_order  integer not null,
  primary key (item_id, resource_order)
);

-------------
-- surveys --
-------------

create table surveys (
    id                  bigint primary key,
    name                varchar(255) not null,
    type                integer not null default 0,
    question_sheet_id   bigint not null unique references question_sheets(id),
    publish_date        timestamp,
    close_date          timestamp,
    department_id       bigint references departments(id),
    author_id           bigint not null references users(id),
    date                timestamp not null default current_timestamp,
    deleted             boolean not null default 'f'
);

create index survey_name_index on surveys (lower(name) varchar_pattern_ops);

alter table surveys add column tsv tsvector;

create function surveys_ts_trigger_function() returns trigger as $$
declare
    l_author    users%rowtype;
begin
	select * into l_author from users where id = new.author_id;
    new.tsv := setweight(to_tsvector(new.name), 'A') ||
               setweight(to_tsvector(l_author.first_name), 'A') ||
               setweight(to_tsvector(l_author.last_name), 'A') ||
               setweight(to_tsvector(l_author.username), 'A');
    return new;
end
$$ language plpgsql;

create trigger surveys_ts_trigger
    before insert or update on surveys
    for each row execute procedure surveys_ts_trigger_function();

create index surveys_ts_index on surveys using gin(tsv);

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

create table survey_charts (
    id              bigint primary key,
    name            varchar(255) not null,
    x_label         varchar(255),
    y_label         varchar(255),
    y_min           integer,
    y_max           integer,
    author_id       bigint references users(id),
    department_id   bigint references departments(id),
    date            timestamp default current_timestamp,
    deleted         boolean not null default 'f'
);

create table survey_chart_xcoordinates (
    chart_id            bigint not null references survey_charts(id),
    coordinate          varchar(255),
    coordinate_order    integer not null,
  primary key (chart_id, coordinate_order)
);

create table survey_chart_series (
    id              bigint primary key,
    chart_id        bigint references survey_charts(id),
    name            varchar(255),
    stat_type       varchar(255)
);

create table survey_chart_points (
    id              bigint primary key,
    survey_id       bigint references surveys(id),
    section_index   integer not null default 0,
    question_index  integer not null default 0,
    min             double precision,
    max             double precision,
    average         double precision,
    median          double precision,
    values_set      boolean not null default 'f',
    series_id       bigint references survey_chart_series(id),
    point_index     integer
);

------------
-- forums --
------------

create table forums (
    id              bigint primary key,
    name            varchar(255) not null,
    description     varchar(255),
    date            timestamp default current_timestamp,
    num_of_topics   integer not null default 0,
    num_of_posts    integer not null default 0,
    last_post_id    bigint unique,
    department_id   bigint references departments(id),
    course_id       bigint unique references courses(id),
    members_only    boolean not null default 'f',
    hidden          boolean not null default 'f',
  check ( department_id is null or course_id is null )
);

alter table forums add column tsv tsvector;

create function forums_ts_trigger_function() returns trigger as $$
declare
    l_course        courses;
    l_department    departments;
begin
    if new.course_id is not null then
        select * into l_course from courses where id = new.course_id;
        new.tsv := setweight(to_tsvector(l_course.code), 'A') ||
                   setweight(to_tsvector(l_course.name), 'A');
    elsif new.department_id is not null then
        select * into l_department from departments where id = new.department_id;
        new.tsv := setweight(to_tsvector(new.name), 'A') ||
                   setweight(to_tsvector(l_department.name), 'B') ||
                   setweight(to_tsvector(l_department.abbreviation), 'B');
    else
        new.tsv := setweight(to_tsvector(new.name), 'A');
    end if;
    return new;
end
$$ language plpgsql;

create trigger forums_ts_trigger
    before insert or update on forums
    for each row execute procedure forums_ts_trigger_function();

create index forums_ts_index on forums using gin(tsv);

insert into forums (id, name, description, hidden) values
    (3000, 'CSNS', 'All things related to CSNS.', 'f');
insert into forums (id, name, description, hidden) values
    (3001, 'Wiki Discussion', 'Discussion of wiki pages.', 't');

create table forum_moderators (
    forum_id    bigint not null references forums(id),
    user_id     bigint not null references users(id),
  primary key (forum_id, user_id)
);

create table forum_members (
    forum_id    bigint not null references forums(id),
    user_id     bigint not null references users(id),
  primary key (forum_id, user_id)
);

create table forum_topics (
    id              bigint primary key,
    pinned          boolean not null default 'f',
    num_of_views    integer not null default 0,
    first_post_id   bigint,
    last_post_id    bigint,
    last_post_date  timestamp,
    num_of_posts    integer not null default 0,
    forum_id        bigint not null references forums(id),
    deleted         boolean not null default 'f'
);

create table forum_posts (
    id          bigint primary key,
    subject     varchar(255) not null,
    content     text not null,
    author_id   bigint not null references users(id),
    date        timestamp default current_timestamp,
    topic_id    bigint references forum_topics(id),
    edited_by   bigint references users(id),
    edit_date   timestamp
);

create table forum_post_attachments (
    post_id bigint not null references forum_posts(id),
    file_id bigint not null references files(id),
  primary key (post_id, file_id)
);

alter table forums add constraint fk_forum_last_post
    foreign key (last_post_id) references forum_posts(id);
alter table forum_topics add constraint fk_forum_topic_first_post
    foreign key (first_post_id) references forum_posts(id);
alter table forum_topics add constraint fk_forum_topic_last_post 
    foreign key (last_post_id) references forum_posts(id);

alter table forum_posts add column tsv tsvector;

create function forum_posts_ts_trigger_function() returns trigger as $$
begin
    new.tsv := setweight(to_tsvector(new.subject), 'A') ||
               setweight(to_tsvector(new.content), 'D');
    return new;
end
$$ language plpgsql;

create trigger forum_posts_ts_trigger
    before insert or update on forum_posts
    for each row execute procedure forum_posts_ts_trigger_function();

create index forum_posts_ts_index on forum_posts using gin(tsv);

create function subscribe_to_course_forum( p_user_id bigint, p_section_id bigint)
    returns void as $$
declare
    l_current_term  integer default term();
    l_section_term  integer;
    l_course_id     courses.id%type;
    l_forum_id      forums.id%type;
begin
    select course_id into l_course_id from sections where id = p_section_id;
    select id into l_forum_id from forums where course_id = l_course_id;
    if not exists (select * from subscriptions where subscribable_type = 'FM'
            and subscribable_id = l_forum_id and subscriber_id = p_user_id) then
        select term into l_section_term from sections where id = p_section_id;
        insert into subscriptions (id, subscribable_type, subscribable_id,
            subscriber_id, term, auto_subscribed) values
            (nextval('hibernate_sequence'), 'FM', l_forum_id, p_user_id,
            l_section_term, 't');
    end if;
end;
$$ language plpgsql;

create function unsubscribe_from_course_forum(p_user_id bigint, p_section_id bigint)
    returns void as $$
declare
    l_current_term  integer default term();
    l_section_term  integer;
    l_course_id     courses.id%type;
    l_forum_id      forums.id%type;
begin
    select course_id into l_course_id from sections where id = p_section_id;
    select id into l_forum_id from forums where course_id = l_course_id;
    delete from subscriptions where subscribable_type = 'FM'
        and subscribable_id = l_forum_id
        and subscriber_id = p_user_id
        and auto_subscribed = 't';
end;
$$ language plpgsql;

create function section_instructors_subscription_trigger_function()
    returns trigger as $$
begin
    if tg_op = 'DELETE' or tg_op = 'UPDATE' then
        perform unsubscribe_from_course_forum(old.instructor_id, old.section_id);
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        perform subscribe_to_course_forum(new.instructor_id, new.section_id);
    end if;
    return null;
end
$$ language plpgsql;

create trigger section_instructors_subscription_trigger
    after insert or update or delete on section_instructors
    for each row execute procedure section_instructors_subscription_trigger_function();

create function enrollment_subscription_trigger_function() returns trigger as $$
begin
    if tg_op = 'DELETE' or tg_op = 'UPDATE' then
        perform unsubscribe_from_course_forum(old.student_id, old.section_id);
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        perform subscribe_to_course_forum(new.student_id, new.section_id);
    end if;
    return null;
end
$$ language plpgsql;

create trigger enrollment_subscription_trigger
    after insert or update or delete on enrollments
    for each row execute procedure enrollment_subscription_trigger_function();

----------
-- wiki --
----------

create table wiki_pages (
    id          bigint primary key,
    path        varchar(1000) not null unique,
    owner_id    bigint not null references users(id),
    views       integer not null default 0,
    password    varchar(255),
    locked      boolean not null default 'f'
);

create index wiki_pages_path_pattern_index on wiki_pages (path varchar_pattern_ops);

create table wiki_revisions (
    id              bigint primary key,
    subject         varchar(1000) not null,
    content         text not null,
    date            timestamp default current_timestamp,
    author_id       bigint not null references users(id),
    page_id         bigint not null references wiki_pages(id),
    include_sidebar boolean not null default 'f'
);

create table wiki_revision_attachments (
    revision_id bigint not null references wiki_revisions(id),
    file_id     bigint not null references files(id),
  primary key (revision_id, file_id)
);

create table wiki_discussions (
    page_id     bigint not null references wiki_pages(id),
    topic_id    bigint not null references forum_topics(id),
  primary key (page_id, topic_id)
);

alter table wiki_pages add column tsv tsvector;
alter table wiki_pages add column ts_subject varchar(1000);
alter table wiki_pages add column ts_content text;

create function wiki_revisions_ts_trigger_function() returns trigger as $$
begin
    update wiki_pages
        set tsv = setweight(to_tsvector(new.subject), 'A') ||
                  setweight(to_tsvector(new.content), 'D'),
            ts_subject = new.subject,
            ts_content = new.content
        where id = new.page_id;
    return new;
end
$$ language plpgsql;

create trigger wiki_revisions_ts_trigger
    before insert on wiki_revisions
    for each row execute procedure wiki_revisions_ts_trigger_function();

create index wiki_pages_ts_index on wiki_pages using gin(tsv);

----------
-- news --
----------

create table news (
    id              bigint primary key,
    department_id   bigint references departments(id),
    topic_id        bigint not null references forum_topics(id),
    expire_date     timestamp
);

------------------
-- mailinglists --
------------------

create table mailinglists (
    id              bigint primary key,
    name            varchar(255) not null unique,
    description     varchar(4092),
    department_id   bigint references departments(id)
);

create table mailinglist_messages (
    id              bigint primary key,
    subject         varchar(255) not null,
    content         varchar(8092) not null,
    date            timestamp default current_timestamp,
    author_id       bigint not null references users(id),
    mailinglist_id  bigint references mailinglists(id)
);

create table mailinglist_message_attachments (
    message_id  bigint not null references mailinglist_messages(id),
    file_id     bigint not null references files(id),
  primary key (message_id, file_id)
);

alter table mailinglist_messages add column tsv tsvector;

create function mailinglist_messages_ts_trigger_function() returns trigger as $$
declare
    author users%rowtype;
begin
	select * into author from users where id = new.author_id;
    new.tsv := setweight(to_tsvector(author.first_name), 'A')
        || setweight(to_tsvector(author.first_name), 'A')
        || setweight(to_tsvector(new.subject), 'A')
        || setweight(to_tsvector(new.content), 'D');
    return new;
end
$$ language plpgsql;

create trigger mailinglist_messages_ts_trigger
    before insert or update on mailinglist_messages
    for each row execute procedure mailinglist_messages_ts_trigger_function();

create index mailinglist_messages_ts_index on mailinglist_messages using gin(tsv);

---------------
-- standings --
---------------

create table standings (
    id          bigint primary key,
    symbol      varchar(255) not null unique,
    name        varchar(255),
    description varchar(8000)
);

insert into standings (id, symbol, name, description) values 
    (4000, 'B', 'Undergraduate',
      'Undergraduate student.');
insert into standings (id, symbol, name, description) values 
    (4002, 'BC','Grad Check for a BS/BA Degree',
      'Undergradudate students who have requested grad check.');
insert into standings (id, symbol, name, description) values 
    (4004, 'BG','Graduated with BS/BA Degree',
      'Graduated with a Bachelor''s Degree.');
insert into standings (id, symbol, name, description) values 
    (4010, 'G0', 'Incoming Graduate Student',
      'Incoming graduate student.');
insert into standings (id, symbol, name, description) values 
    (4012, 'G1', 'Conditionaly Classified Graduate',
      'Graduate students who have not completed all prerequisite courses.');
insert into standings (id, symbol, name, description) values 
    (4014, 'G2', 'Classified Graduate',
      'Graduate students who have completed all prerequiste courses but ' ||
      'have not yet fullfilled the requirements for Candidacy.');
insert into standings (id, symbol, name, description) values 
    (4016, 'G3', 'Candidacy Graduate',
      'Graduate students who have met the Candidacy requirements, which ' ||
      'include completion of all core courses, completion of at least half ' ||
      ' of the remaining course requirements, passing WPE, and declaring a ' ||
      ' thesis/project or comprehensive exam option.');
insert into standings (id, symbol, name, description) values 
    (4018, 'GC', 'Grad Check for an MS Degree',
      'Gradudate students who have requested grad check.');
insert into standings (id, symbol, name, description) values 
    (4020, 'GG', 'Graduated with MS Degree',
      'Graduated with a Master''s Degree.');
insert into standings (id, symbol, name, description) values
    (4030, 'HG', 'Graduated from Blended BS/MS Program',
      'Graduated from the Blended BS/MS program.');
insert into standings (id, symbol, name, description) values 
    (4040, 'NG', 'Not Graduated',
      'The student did not graduate for some reason. For example, ' ||
      'the student was disqualified from the program, or dropped out of ' ||
      'the program, or simply stopped taking classes.');

create table standing_mailinglists (
    standing_id     bigint not null references standings(id),
    mailinglist     varchar(255)
);

-- mailing list membership for B Standing
insert into standing_mailinglists values (4000, 'students');
insert into standing_mailinglists values (4000, 'undergrads');

-- mailing list membership for BG Standing
insert into standing_mailinglists values (4004, 'alumni');
insert into standing_mailinglists values (4004, 'alumni-undergrad');

-- mailing list membership for G0 Standing
insert into standing_mailinglists values (4010, 'students');
insert into standing_mailinglists values (4010, 'grads');
insert into standing_mailinglists values (4010, 'grads-g0');

-- mailing list membership for G1 Standing
insert into standing_mailinglists values (4012, 'students');
insert into standing_mailinglists values (4012, 'grads');
insert into standing_mailinglists values (4012, 'grads-g1');

-- mailing list membership for G2 Standing
insert into standing_mailinglists values (4014, 'students');
insert into standing_mailinglists values (4014, 'grads');
insert into standing_mailinglists values (4014, 'grads-g2');

-- Mailing list membership for G3 Standing
insert into standing_mailinglists values (4016, 'students');
insert into standing_mailinglists values (4016, 'grads');
insert into standing_mailinglists values (4016, 'grads-g3');

-- mailing list membership for GG Standing
insert into standing_mailinglists values (4020, 'alumni');
insert into standing_mailinglists values (4020, 'alumni-grad');

-- mailing list membership for HG Standing
insert into standing_mailinglists values (4030, 'alumni');
insert into standing_mailinglists values (4030, 'alumni-undergrad');
insert into standing_mailinglists values (4030, 'alumni-grad');

create table academic_standings (
    id              bigint primary key,
    student_id      bigint references users(id),
    department_id   bigint references departments(id),
    standing_id     bigint references standings(id),
    term            integer,
  unique (student_id, department_id, standing_id)
);

create table current_standings (
    student_id              bigint not null references users(id),
    department_id           bigint not null references departments(id),
    academic_standing_id    bigint unique not null references academic_standings(id),
  primary key (student_id, department_id)
);

create or replace function mailinglist_subscription_on_standing_change_trigger_function()
    returns trigger as $$
declare
    l_dept              varchar;
    l_mailinglist       varchar;
    l_standing_id       standings.id%type;
    l_mailinglist_id    mailinglists.id%type;
    l_subscription_id   subscriptions.id%type;
begin
    if tg_op = 'DELETE' or tg_op = 'UPDATE' then
        select abbreviation into l_dept from departments
            where id = old.department_id;
        select standing_id into l_standing_id from academic_standings
            where id = old.academic_standing_id;
        for l_mailinglist in select mailinglist from standing_mailinglists
            where standing_id = l_standing_id and mailinglist not like 'alumni%' loop
                delete from subscriptions where subscriber_id = old.student_id
                    and subscribable_type = 'ML' and auto_subscribed = 't'
                    and subscribable_id = (select id from mailinglists
                        where name = l_dept || '-' || l_mailinglist);
        end loop;
	end if;
	
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        select abbreviation into l_dept from departments
            where id = new.department_id;
        select standing_id into l_standing_id from academic_standings
            where id = new.academic_standing_id;
        for l_mailinglist in select mailinglist from standing_mailinglists
            where standing_id = l_standing_id loop
            select id into l_mailinglist_id from mailinglists
                where name = l_dept || '-' || l_mailinglist;
            select id into l_subscription_id from subscriptions
                where subscribable_id = l_mailinglist_id
                and subscriber_id = new.student_id
                and subscribable_type = 'ML';
            if not found then
                insert into subscriptions (id, subscribable_type, subscribable_id,
                    subscriber_id, auto_subscribed) values (
                    nextval('hibernate_sequence'), 'ML', l_mailinglist_id,
                    new.student_id, 't');
            end if;
        end loop;
    end if;

    return null; 
end
$$ language plpgsql;

create trigger mailinglist_subscription_on_standing_change_trigger
    after insert or update or delete on current_standings
    for each row execute procedure mailinglist_subscription_on_standing_change_trigger_function();

----------------
-- advisement --
----------------

create table advisement_records (
    id                  bigint primary key,
    student_id          bigint not null references users(id),
    advisor_id          bigint not null references users(id),
    comment             varchar(8000),
    date                timestamp default current_timestamp,
    editable            boolean not null default 't',
    for_advisors_only   boolean not null default 'f',
    emailed_to_student  boolean not null default 'f',
    deleted             boolean not null default 'f'
);

create table advisement_record_attachments (
    record_id   bigint not null references advisement_records(id),
    file_id     bigint not null references files(id),
  primary key (record_id, file_id)
);

create table course_substitutions (
    id                      bigint primary key,
    original_course_id      bigint references courses(id),
    substitute_course_id    bigint references courses(id),
    advisement_record_id    bigint references advisement_records(id)
);

create table course_transfers (
    id                      bigint primary key,
    course_id               bigint references courses(id),
    advisement_record_id    bigint references advisement_records(id)
);

create table course_waivers (
    id                      bigint primary key,
    course_id               bigint references courses(id),
    advisement_record_id    bigint references advisement_records(id)
);

--------------
-- projects --
--------------

create table projects (
    id              bigint primary key,
    title           varchar(255) not null,
    description     varchar(8000),
    sponsor         varchar(255),
    department_id   bigint references departments(id),
    year            integer not null,
    published       boolean not null default 'f',
    private         boolean not null default 'f',
    deleted         boolean not null default 'f'
);

alter table projects add column tsv tsvector;

create function projects_ts_trigger_function() returns trigger as $$
declare
    l_user          users%rowtype;
    l_department    departments%rowtype;
begin
    new.tsv := setweight(to_tsvector(new.title), 'A') ||
               setweight(to_tsvector(coalesce(new.sponsor, '')), 'B') ||
               setweight(to_tsvector(coalesce(new.description, '')), 'D');
    select * into l_department from departments where id = new.department_id;
    new.tsv := new.tsv ||
            setweight(to_tsvector(l_department.name), 'B') ||
            setweight(to_tsvector(l_department.abbreviation), 'B');
    for l_user in select u.* from users u, project_advisors a
        where a.project_id = new.id and a.advisor_id = u.id loop
            new.tsv := new.tsv ||
                setweight(to_tsvector(l_user.first_name), 'B') ||
                setweight(to_tsvector(l_user.last_name), 'B') ||
                setweight(to_tsvector(l_user.username), 'B');
    end loop;
    for l_user in select u.* from users u, project_students s
        where s.project_id = new.id and s.student_id = u.id loop
            new.tsv := new.tsv ||
                setweight(to_tsvector(l_user.first_name), 'B') ||
                setweight(to_tsvector(l_user.last_name), 'B') ||
                setweight(to_tsvector(l_user.username), 'B');
    end loop;
    for l_user in select u.* from users u, project_liaisons l
        where l.project_id = new.id and l.liaison_id = u.id loop
            new.tsv := new.tsv ||
                setweight(to_tsvector(l_user.first_name), 'B') ||
                setweight(to_tsvector(l_user.last_name), 'B') ||
                setweight(to_tsvector(l_user.username), 'B');
    end loop;
    return new;
end
$$ language plpgsql;

create trigger projects_ts_trigger
    before insert or update on projects
    for each row execute procedure projects_ts_trigger_function();

create index projects_ts_index on projects using gin(tsv);

create table project_advisors (
    project_id      bigint not null references projects(id),
    advisor_id      bigint not null references users(id),
  primary key (project_id, advisor_id)
);

create function project_advisors_ts_trigger_function() returns trigger as $$
begin
	if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        update projects set tsv = '' where id = new.project_id;
    end if;
    if tg_op = 'DELETE' or tg_op = 'UPDATE' then
        update projects set tsv = '' where id = old.project_id;
    end if;
    return null;
end
$$ language plpgsql;

create trigger project_advisors_ts_trigger
    after insert or delete or update on project_advisors
    for each row execute procedure project_advisors_ts_trigger_function();

create table project_students (
    project_id  bigint not null references projects(id),
    student_id  bigint not null references users(id),
  primary key (project_id, student_id)
);

create function project_students_ts_trigger_function() returns trigger as $$
begin
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        update projects set tsv = '' where id = new.project_id;
    end if;
    if tg_op = 'DELETE' or tg_op = 'UPDATE' then
        update projects set tsv = '' where id = old.project_id;
    end if;
    return null;
end
$$ language plpgsql;

create trigger project_students_ts_trigger
    after insert or delete or update on project_students
    for each row execute procedure project_students_ts_trigger_function();

create table project_liaisons (
    project_id  bigint not null references projects(id),
    liaison_id  bigint not null references users(id),
  primary key (project_id, liaison_id)
);

create function project_liaisons_ts_trigger_function() returns trigger as $$
begin
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        update projects set tsv = '' where id = new.project_id;
    end if;
    if tg_op = 'DELETE' or tg_op = 'UPDATE' then
        update projects set tsv = '' where id = old.project_id;
    end if;
    return null;
end
$$ language plpgsql;

create trigger project_liaisons_ts_trigger
    after insert or delete or update on project_liaisons
    for each row execute procedure project_liaisons_ts_trigger_function();

create table project_resources (
    project_id      bigint not null references projects(id),
    resource_id     bigint not null references resources(id),
    resource_order  bigint not null,
  primary key (project_id, resource_order)
);

---------
-- mft --
---------

create table mft_scores (
    id              bigint primary key,
    department_id   bigint not null references departments(id),
    date            date not null,
    user_id         bigint not null references users(id),
    value           integer not null,
  unique (department_id, date, user_id)
);

create table mft_indicators (
    id              bigint primary key,
    department_id   bigint not null references departments(id),
    ai1             integer not null,
    ai2             integer not null,
    ai3             integer not null,
    date            date not null,
    num_of_students integer not null,
    deleted         boolean not null default 'f',
  unique (department_id, date)
);

create table mft_distribution_types (
    id              bigint primary key,
    department_id   bigint not null references departments(id),
    name            varchar(255) not null,
    alias           varchar(255) not null,
    min             integer not null,
    max             integer not null,
    value_label     varchar(255),
  unique (department_id, alias)
);

create table mft_distributions (
    id              bigint primary key,
    year            integer not null,
    type_id         bigint not null references mft_distribution_types(id),
    from_date       date,
    to_date         date,
    num_of_samples  integer,
    mean            double precision,
    median          double precision,
    stdev           double precision,
    deleted         boolean not null default 'f',
  unique (year, type_id)
);

create table mft_distribution_entries (
    distribution_id bigint not null references mft_distributions(id),
    percentile      integer not null,
    value           integer not null
);

-------------
-- rubrics --
-------------

create table rubrics (
    id              bigint primary key,
    name            varchar(255) not null,
    description     varchar(8000),
    scale           integer not null default 5,
    department_id   bigint references departments(id),
    creator_id      bigint references users(id),
    publish_date    timestamp,
    public          boolean not null default 'f',
    obsolete        boolean not null default 'f',
    deleted         boolean not null default 'f'
);

alter table rubrics add column tsv tsvector;

create function rubrics_ts_trigger_function() returns trigger as $$
begin
    new.tsv := setweight(to_tsvector(new.name), 'A') ||
               setweight(to_tsvector(new.description), 'D');
    return new;
end
$$ language plpgsql;

create trigger rubrics_ts_trigger
    before insert or update on rubrics
    for each row execute procedure rubrics_ts_trigger_function();

create index rubrics_ts_index on rubrics using gin(tsv);

create table rubric_indicators (
    id              bigint primary key,
    name            varchar(255) not null,
    rubric_id       bigint references rubrics(id),
    indicator_index integer
);

create table rubric_indicator_criteria (
    indicator_id    bigint not null references rubric_indicators(id),
    criterion_index integer not null,
    criterion       varchar(8000),
  primary key (indicator_id, criterion_index)
);

create table rubric_assignments (
    id                          bigint primary key,
    name                        varchar(255) not null,
    rubric_id                   bigint not null references rubrics(id),
    section_id                  bigint not null references sections(id),
    evaluated_by_instructors    boolean not null default 't',
    evaluated_by_students       boolean not null default 'f',
    publish_date                timestamp,
    due_date                    timestamp,
    deleted                     boolean not null default 'f'
);

alter table course_journal_rubric_assignments
    add constraint course_journal_rubric_assignments_assignment_id_fkey
    foreign key (assignment_id) references rubric_assignments(id);

create table rubric_external_evaluators (
    rubric_assignment_id    bigint not null references rubric_assignments(id),
    evaluator_id            bigint not null references users(id),
  primary key (rubric_assignment_id, evaluator_id)
);

create table rubric_submissions (
    id                          bigint primary key,
    assignment_id               bigint not null references rubric_assignments(id),
    student_id                  bigint not null references users(id),
    instructor_evaluation_count integer not null default 0,
    peer_evaluation_count       integer not null default 0,
    external_evaluation_count   integer not null default 0
);

create table rubric_evaluations (
    id              bigint primary key,
    type            varchar(255) not null, -- INSTRUCOTR, PEER, or EXTERNAL
    submission_id   bigint references rubric_submissions(id),
    evaluator_id    bigint references users(id),
    comments        varchar(8000),
    date            timestamp default current_timestamp,
    completed       boolean not null default 'f',
    deleted         boolean not null default 'f'
);

create table rubric_evaluation_ratings (
    evaluation_id   bigint not null references rubric_evaluations(id),
    rating_order    integer not null,
    rating          integer,
  primary key (evaluation_id, rating_order)
);

----------------
-- Assessment --
----------------

create table assessment_programs (
    id              bigint primary key,
    department_id   bigint not null references departments(id),
    name            varchar(255) not null,
    vision          varchar(8000),
    mission         varchar(8000),
    deleted         boolean not null default 'f'
);

create table assessment_program_objectives (
    id              bigint primary key,
    program_id      bigint references assessment_programs(id),
    objective_index integer not null,
    text            varchar(8000),
    description     text
);

create table assessment_program_outcomes (
    id              bigint primary key,
    program_id      bigint references assessment_programs(id),
    outcome_index   integer not null,
    text            varchar(8000),
    description     text
);

create table assessment_program_sections (
    id              bigint primary key,
    program_id      bigint references assessment_programs(id),
    section_index   integer,
    name            varchar(255) not null,
    hidden          boolean not null default 'f'
);

create table assessment_program_resources (
    section_id      bigint not null references assessment_program_sections(id),
    resource_id     bigint not null references resources(id),
    resource_index  integer not null,
  primary key (section_id, resource_index)
);

----------------------
-- Pre-registration --
----------------------

create table prereg_schedules (
    id                          bigint primary key,
    department_id               bigint not null references departments(id),
    term                        integer not null,
    description                 varchar(80000),
    prereg_start                timestamp,
    prereg_end                  timestamp,
    default_section_capacity    integer not null default 30,
    default_undergrad_reg_limit integer not null default 5,
    default_grad_reg_limit      integer not null default 3,
    deleted                     boolean not null default 'f'
);

create table prereg_sections (
    id              bigint primary key,
    schedule_id     bigint references prereg_schedules(id),
    course_id       bigint references courses(id),
    section_number  integer not null default 1,
    type            varchar(255),
    class_number    varchar(255),
    days            varchar(255),
    start_time      varchar(255),
    end_time        varchar(255),
    location        varchar(255),
    notes           varchar(255),
    capacity        integer not null default 30,
    linked_by       bigint references prereg_sections(id)
);

create table prereg_schedule_registrations (
    id          bigint primary key,
    student_id  bigint not null references users(id),
    schedule_id bigint not null references prereg_schedules(id),
    reg_limit   integer not null default 5,
    comments    varchar(8000),
    date        timestamp not null default current_timestamp,
  unique (student_id, schedule_id)
);

create table prereg_section_registrations (
    id                          bigint primary key,
    schedule_registration_id    bigint references prereg_schedule_registrations(id),
    student_id                  bigint not null references users(id),
    section_id                  bigint not null references prereg_sections(id),
    date                        timestamp not null default current_timestamp,
    prereq_met                  boolean,
  unique (student_id, section_id)
);

------------------------------
-- functions and procedures --
------------------------------

--
-- Given a date, returns the term.
--
create or replace function term( p_date date ) returns integer as $$
declare
    l_code integer := (extract(year from p_date) - 1900) * 10;
    l_week integer := extract(week from p_date);
begin
    if l_week < 4 then
        l_code := l_code + 1;
    elsif l_week < 22 then
        l_code := l_code + 3;
    elsif l_week < 34 then
        l_code := l_code + 6;
    else
        l_code := l_code + 9;
    end if;
    return l_code;
end;
$$ language plpgsql;

--
-- Returns the current term.
--
create or replace function term() returns integer as $$
begin
    return term(current_date);
end;
$$ language plpgsql;

--
-- Given a term code, returns the term name (e.g. Fall 2012).
--
create or replace function term( p_code integer ) returns varchar as $$
declare
    l_year  varchar;
    l_term  varchar;
begin
    l_year := cast( p_code/10+1900 as varchar );

    case p_code % 10
        when 1 then
            l_term = 'Winter';
        when 3 then
            l_term = 'Spring';
        when 6 then
            l_term = 'Summer';
        else
            l_term = 'Fall';
    end case;

    return l_term || ' ' || l_year;
end;
$$ language plpgsql;

--
-- Given a term code, returns the year of the term.
--
create or replace function year( p_code integer ) returns integer as $$
begin
    return p_code / 10 + 1900;                                                  
end;
$$ language plpgsql;

--
-- Median Aggregation Function for PostgreSQL 8.4+
--
-- Written by Scott Bailey 'Artacus'
-- http://wiki.postgresql.org/wiki/Aggregate_Median
--
CREATE OR REPLACE FUNCTION _final_median(numeric[])
   RETURNS numeric AS
$$
   SELECT AVG(val)
   FROM (
     SELECT val
     FROM unnest($1) val
     ORDER BY 1
     LIMIT  2 - MOD(array_upper($1, 1), 2)
     OFFSET CEIL(array_upper($1, 1) / 2.0) - 1
   ) sub;
$$
LANGUAGE 'sql' IMMUTABLE;
 
CREATE AGGREGATE median(numeric) (
  SFUNC=array_append,
  STYPE=numeric[],
  FINALFUNC=_final_median,
  INITCOND='{}'
);
