set client_min_messages=WARNING;

create table department_options (
    department_id   bigint not null references departments(id),
    option          varchar(255) not null,
  primary key (department_id, option)
);

create table mft_scores (
    id              bigint primary key,
    department_id   bigint references departments(id),
    date            date not null,
    user_id         bigint references users(id),
    value           integer not null,
  unique (department_id, user_id, date)
);

insert into mft_scores select nextval('hibernate_sequence'), 200, mft_date, id, mft_score 
    from users where mft_score is not null;
update mft_scores set date = '2007-12-05' where date = '2007-12-16';
update mft_scores set date = '2009-05-28' where date = '2009-06-01';
update mft_scores set date = '2011-06-06' where date = '2011-06-01';
alter table users drop column mft_score;
alter table users drop column mft_date;

alter table mft_assessment_summaries rename to mft_assessment_indicators;
alter table mft_assessment_indicators alter column id type bigint;
alter table mft_assessment_indicators add column department_id bigint references departments(id);
update mft_assessment_indicators set department_id = 200;
alter table mft_assessment_indicators alter column ai1 set not null;
alter table mft_assessment_indicators alter column ai2 set not null;
alter table mft_assessment_indicators alter column ai3 set not null;

alter table mft_assessment_indicators rename to mft_indicators;
alter table mft_indicators alter column department_id set not null;
alter table mft_indicators drop constraint mft_assessment_summaries_date_key;
alter table mft_indicators add constraint mft_indicators_department_date_key unique (department_id, date);

alter table mft_distribution_types alter column id type bigint;
alter table mft_distribution_types drop constraint mft_distribution_types_alias_key;
alter table mft_distribution_types add column department_id bigint references departments(id);
update mft_distribution_types set department_id = 200;
alter table mft_distribution_types alter column department_id set not null;
alter table mft_distribution_types add constraint mft_distribution_types_department_alias_key unique (department_id, alias);

alter table mft_distributions alter column id type bigint;
alter table mft_distributions alter column type_id type bigint;
alter table mft_distributions add column department_id bigint references departments(id);
update mft_distributions set department_id = 200;
alter table mft_distributions add column year integer;
update mft_distributions set year = extract(year from to_date);
alter table mft_distributions alter column year set not null;

alter table mft_distributions alter column department_id set not null;
alter table mft_distributions alter column type_id set not null;
alter table mft_distributions add constraint mft_distributions_department_year_type_key
    unique (department_id, year, type_id);

alter table mft_distributions drop column p5;
alter table mft_distributions drop column p10;
alter table mft_distributions drop column p15;
alter table mft_distributions drop column p20;
alter table mft_distributions drop column p25;
alter table mft_distributions drop column p30;
alter table mft_distributions drop column p35;
alter table mft_distributions drop column p40;
alter table mft_distributions drop column p45;
alter table mft_distributions drop column p50;
alter table mft_distributions drop column p55;
alter table mft_distributions drop column p60;
alter table mft_distributions drop column p65;
alter table mft_distributions drop column p70;
alter table mft_distributions drop column p75;
alter table mft_distributions drop column p80;
alter table mft_distributions drop column p85;
alter table mft_distributions drop column p90;
alter table mft_distributions drop column p95;

create table mft_distribution_entries (
    distribution_id bigint not null references mft_distributions(id),
    percentile      integer not null,
    value           integer not null
);
