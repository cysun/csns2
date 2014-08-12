update users set username = 'lulu2' where cin = '217427806';
update users set username = 'Lusine2' where cin = '220290185';
update users set username = 'javi2' where cin = '220387542';
update users set primary_email = 'other@csns.calstatela.edu' where cin = 'G137402219';

drop index user_firstname_index;
drop index user_lastname_index;
drop index user_fullname_index;
drop index user_names_index;
drop index user_username_index;

create unique index user_cin_index on users (cin varchar_pattern_ops);
create unique index user_username_index
    on users (lower(username) varchar_pattern_ops);
create unique index user_primary_email_index
    on users (lower(primary_email) varchar_pattern_ops);

create index user_firstname_index
    on users (lower(first_name) varchar_pattern_ops);
create index user_lastname_index
    on users (lower(last_name) varchar_pattern_ops);
create index user_fullname_index
    on users (lower(first_name || ' ' || last_name) varchar_pattern_ops);

create index survey_name_index on surveys (lower(name) varchar_pattern_ops);

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
    chart_id        bigint references survey_charts(id),
    name            varchar(255),
    stat_type       varchar(255)
);

create table survey_chart_points (
    id              bigint primary key,
    survey_id       bigint references surveys(id),
    section_index   integer not null default 0,
    question_index  integer not null default 0,
    series_id       bigint references survey_chart_series(id),
    point_index     integer
);
