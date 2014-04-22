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

create table rubric_external_evaluators (
    rubric_assignment_id    bigint not null references rubric_assignments(id),
    evaluator_id            bigint not null references users(id),
  primary key (rubric_assignment_id, evaluator_id)
);
