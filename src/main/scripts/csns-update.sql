drop table assessment_outcome_courses;
drop table assessment_objective_outcomes;

alter table assessment_objectives rename to assessment_program_objectives;
alter table assessment_outcomes rename to assessment_program_outcomes;

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
