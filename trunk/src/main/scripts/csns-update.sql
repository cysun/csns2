alter table survey_chart_points add column min double precision;
alter table survey_chart_points add column max double precision;
alter table survey_chart_points add column average double precision;
alter table survey_chart_points add column median double precision;
alter table survey_chart_points add column values_set boolean not null default 'f';
