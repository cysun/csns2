alter table course_journal_assignments alter column assignment_order type integer;

create table course_journal_rubric_assignments (
    course_journal_id   bigint not null references course_journals(id),
    assignment_id       bigint not null references rubric_assignments(id),
    assignment_order    integer not null,
  primary key (course_journal_id, assignment_order)
);
