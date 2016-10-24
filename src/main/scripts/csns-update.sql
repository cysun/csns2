alter table resources add column deleted boolean not null default 'f';
alter table site_items add column deleted boolean not null default 'f';
alter table assessment_program_objectives add column description text;
alter table assessment_program_outcomes add column description text;
drop table assessment_program_outcome_measures;
drop table assessment_program_measures;
