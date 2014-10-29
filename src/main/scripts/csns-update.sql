alter table courses rename journal_id to course_journal_id;
alter table courses rename syllabus_id to description_id;
alter table courses alter course_journal_id type bigint;

alter table sections add column course_journal_id bigint unique;
alter table sections add constraint sections_course_journal_id_fkey
    foreign key (course_journal_id) references course_journals(id);

update sections s set course_journal_id = (select id from course_journals
    where section_id = s.id);

alter table course_journals drop column section_id;
