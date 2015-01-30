create table course_mappings (
    id              bigint primary key,
    department_id   bigint not null references departments(id),
    deleted         boolean not null default 'f'
);

create table course_mappings_group1 (
    mapping_id  bigint not null references course_mappings(id),
    course_id   bigint not null references courses(id),
  unique (mapping_id, course_id)
);

create table course_mappings_group2 (
    mapping_id  bigint not null references course_mappings(id),
    course_id   bigint not null references courses(id),
  unique (mapping_id, course_id)
);
