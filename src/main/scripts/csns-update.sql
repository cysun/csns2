drop table department_additional_graduate_courses;
drop table department_additional_undergraduate_courses;

alter table courses add column unit_factor double precision not null default 1.0;

update courses set unit_factor = 0.667
    where substring(code from '[0-9]{4}') is null;

update courses set units = 4 where unit_factor < 1;
update courses set units = 3 where unit_factor = 1;
