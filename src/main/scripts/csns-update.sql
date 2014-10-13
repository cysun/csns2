alter table files add column reference_id bigint references files(id);
alter table sites add column restricted boolean not null default 'f';
alter table sites add column limited boolean not null default 'f';

-- course_journals

alter table course_journals alter id type bigint;
alter table course_journals alter section_id type bigint;

insert into resources
    select nextval('hibernate_sequence'), f.name, 2, null, c.syllabus,
        null, null, not f.public
    from course_journals c, files f
    where c.syllabus is not null and c.syllabus = f.id;

update sections s set syllabus_id = r.id from resources r, course_journals c
    where s.id = c.section_id and r.file_id = c.syllabus
    and s.syllabus_id is null
    and c.syllabus is not null;

alter table course_journals drop column syllabus;

-- course_journal_handouts

alter table course_journal_handouts alter course_journal_id type bigint;

insert into resources
    select nextval('hibernate_sequence'), f.name, 2, null, c.file_id,
        null, null, not f.public
    from course_journal_handouts c, files f
    where c.file_id = f.id;

alter table course_journal_handouts add column resource_id bigint 
    references resources(id);

update course_journal_handouts c set resource_id = (select id from resources
    where type = 2 and file_id = c.file_id 
);

alter table course_journal_handouts alter resource_id set not null;
alter table course_journal_handouts drop file_id;

-- course_journal_assignments

alter table course_journal_assignments rename to course_journal_assignment_files;

create table course_journal_assignments (
    course_journal_id   bigint not null references course_journals(id),
    assignment_id       bigint not null references assignments(id),
    assignment_order    bigint not null,
  primary key (course_journal_id, assignment_order)
);

-- course_journal_student_samples

alter table course_journal_enrollment_samples rename to course_journal_student_samples;

alter table course_journal_student_samples alter course_journal_id type bigint;
alter table course_journal_student_samples alter enrollment_id type bigint;

