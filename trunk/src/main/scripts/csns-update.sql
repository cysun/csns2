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
    deleted         boolean not null default 'f'
);

create table rubric_indicators (
    id              bigint primary key,
    name            varchar(255) not null,
    rubric_id       bigint references rubrics(id),
    indicator_order integer not null,
  unique (rubric_id, indicator_order)
);

create table rubric_indicator_criteria (
    indicator_id    bigint not null references rubric_indicators(id),
    criterion_order integer not null,
    criterion       varchar(8000),
  primary key (indicator_id, criterion_order)
);

create table rubric_evaluations (
    id              bigint primary key,
    type            integer, -- instructor, peer, or external
    rubric_id       bigint references rubrics(id),
    evaluator_id    bigint references users(id),
    evaluatee_id    bigint references users(id),
    comments        varchar(80000),
    rubricable_id   bigint,
    rubricable_type varchar(255),
    date            timestamp default current_timestamp,
    deleted         boolean not null default 'f'
);

create table rubric_evaluation_ratings (
    evaluation_id   bigint not null references rubric_evaluations(id),
    rating_order    integer not null,
    rating          integer,
  primary key (evaluation_id, rating_order)
);

create table section_rubrics (
    section_id  bigint not null references sections(id),
    rubric_id   bigint not null references rubrics(id),
  primary key (section_id, rubric_id)
);

create table assignment_rubrics (
    assignment_id   bigint not null references assignments(id),
    rubric_id       bigint not null references rubrics(id),
  primary key( assignment_id, rubric_id)
);
