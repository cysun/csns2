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
    name            varchar(255),
    stat_type       varchar(255),
    chart_id        bigint references survey_charts(id),
    series_index    integer
);

create table survey_chart_points (
    id              bigint primary key,
    survey_id       bigint references surveys(id),
    section_index   integer not null default 0,
    question_index  integer not null default 0,
    series_id       bigint references survey_chart_series(id),
    point_index     integer
);
