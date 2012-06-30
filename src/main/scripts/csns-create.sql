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
    cin_encrypted   boolean not null default 'f',
    username        varchar(255) not null unique,
    password        varchar(255) not null,
    enabled         boolean not null default 't',
    last_name       varchar(255) not null,
    first_name      varchar(255) not null,
    middle_name     varchar(255),
    gender          char(1) check( gender = 'M' or gender = 'F' ),
    birthday        date,
    address1        varchar(255),
    address2        varchar(255),
    city            varchar(255),
    state           varchar(255),
    zip             varchar(255),
    primary_email   varchar(255) not null unique,
    secondary_email varchar(255) unique,
    cell_phone      varchar(255),
    home_phone      varchar(255),
    office_phone    varchar(255)
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
    id          bigint primary key,
    name        varchar(255) not null,
    type        varchar(255),
    size        bigint,
    date        timestamp not null default current_timestamp,
    owner_id    bigint not null references users(id),
    parent_id   bigint references files(id),
    folder      boolean not null default 'f',
    public      boolean not null default 'f',
    regular     boolean not null default 'f',
    deleted     boolean not null default 'f'
);

-------------
-- courses --
-------------

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
