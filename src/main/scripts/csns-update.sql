alter table prereg_schedules add column description varchar(80000);
alter table prereg_sections add column notes varchar(255);

update prereg_registrations set reg_limit = 4 where reg_limit = 3;
