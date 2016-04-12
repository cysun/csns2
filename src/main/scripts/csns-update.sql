update resources set type = 0, name = 'Program Measure Resource'
    where name = 'Program Measure Description';

alter table assessment_program_measures rename description_id to resource_id;
alter table assessment_program_measures
    add column description varchar(80000) not null default '';
