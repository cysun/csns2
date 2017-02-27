drop table department_additional_graduate_courses;
drop table department_additional_undergraduate_courses;

alter table courses add column unit_factor double precision not null default 1.0;

update courses set unit_factor = 0.667
    where substring(code from '[0-9]{4}') is null;

update courses set units = 4 where unit_factor < 1;
update courses set units = 3 where unit_factor = 1;

alter table program_blocks alter column units_required drop not null;
alter table program_blocks alter column units_required drop default;
alter table program_blocks add column require_all boolean not null default 't';

alter table personal_programs add column student_id bigint references users(id);
alter table personal_programs add column date timestamp not null default current_timestamp;

alter table personal_program_blocks drop column name;
alter table personal_program_blocks drop column description;
alter table personal_program_blocks add column
    program_block_id bigint references program_blocks(id);
