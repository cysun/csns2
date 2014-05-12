create table department_evaluators (
    department_id   bigint not null references departments(id),
    user_id         bigint not null references users(id)
);

drop table section_rubrics;
drop table assignment_rubrics;
drop table rubric_evaluation_ratings;
drop table rubric_evaluations;

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
    deleted         boolean not null default 'f'
);

create table rubric_evaluation_ratings (
    evaluation_id   bigint not null references rubric_evaluations(id),
    rating_order    integer not null,
    rating          integer,
  primary key (evaluation_id, rating_order)
);
