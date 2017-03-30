alter table personal_programs
    add constraint personal_programs_sid_pid_unique
    unique (student_id, program_id);
alter table personal_program_entries
    add column prereq_met boolean not null default 'f';
alter table groups alter description drop not null;
alter table members add constraint members_gid_uid_unique unique (group_id, user_id);
