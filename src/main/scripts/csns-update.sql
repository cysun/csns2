alter table users drop column program_id;

drop trigger programs_ts_trigger on programs;
drop function programs_ts_trigger_function();
drop table program_elective_courses;
drop table program_required_courses;
drop table programs;

create table advisement_programs (
    id              bigint primary key,
    department_id   bigint references departments(id),
    name            varchar(255) not null,
    description     varchar(8000),
    publish_date    timestamp,
    last_edited_by  bigint references users(id),
    obsolete        boolean not null default 'f'
);

create table advisement_program_blocks (
    id              bigint primary key,
    program_id      bigint references advisement_programs(id),
    block_index     integer,
    name            varchar(255),
    description     varchar(8000),
    units_required  integer not null default 0
);

create table advisement_program_block_courses (
    block_id    bigint not null references advisement_program_blocks(id),
    course_id   bigint not null references courses(id)
);

create table advisement_personal_programs (
    id              bigint primary key,
    program_id      bigint references advisement_programs(id),
    approve_date    timestamp,
    approved_by     bigint references users(id)
);

create table advisement_personal_program_blocks (
    id          bigint primary key,
    program_id  bigint references advisement_personal_programs(id),
    block_index integer,
    name        varchar(255),
    description varchar(8000)
);

create table advisement_personal_program_entries (
    id              bigint primary key,
    block_id        bigint references advisement_personal_program_blocks(id),
    course_id       bigint references courses(id),
    enrollment_id   bigint references enrollments(id)
);

alter table users add column personal_program_id bigint
    references advisement_personal_programs(id);
