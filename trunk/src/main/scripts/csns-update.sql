alter table users add column major_id bigint references departments(id);

alter table course_mappings_group1 rename to course_mapping_group1;
alter table course_mappings_group2 rename to course_mapping_group2;

create table programs (
    id              bigint primary key,
    department_id   bigint references departments(id),
    name            varchar(255) not null,
    description     varchar(8000)
);

create table program_required_courses (
    program_id  bigint not null references programs(id),
    course_id   bigint not null references courses(id),
  unique (program_id, course_id)
);

create table program_elective_courses (
    program_id  bigint not null references programs(id),
    course_id   bigint not null references courses(id),
  unique (program_id, course_id)
);

alter table users add column program_id bigint references programs(id);
