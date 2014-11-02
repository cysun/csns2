alter table courses rename syllabus_id to description_id;
alter table courses alter journal_id type bigint;

alter table sections add column journal_id bigint unique;
alter table sections add constraint sections_journal_id_fkey
    foreign key (journal_id) references course_journals(id);

update sections s set journal_id = (select id from course_journals
    where section_id = s.id);

alter table course_journals drop column section_id;
