alter table projects add column private boolean not null default 'f';

create table assessment_programs (
    id              bigint primary key,
    department_id   bigint not null references departments(id),
    vision          varchar(8000),
    mission         varchar(8000),
    deleted         boolean not null default 'f'
);

create table assessment_objectives (
    id              bigint primary key,
    program_id      bigint references assessment_programs(id),
    objective_index integer not null,
    text            varchar(8000)
);

create table assessment_outcomes (
    id              bigint primary key,
    program_id      bigint references assessment_programs(id),
    outcome_index   integer not null,
    text            varchar(8000)
);

create table assessment_objective_outcomes (
    objective_id    bigint not null references assessment_objectives(id),
    outcome_id      bigint not null references assessment_outcomes(id),
  unique (objective_id, outcome_id)
);

create table assessment_outcome_courses (
    outcome_id      bigint not null references assessment_outcomes(id),
    course_id       bigint not null references courses(id),
  unique (outcome_id, course_id)
);
