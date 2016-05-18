create table course_prerequisites (
    course_id       bigint not null references courses(id),
    prerequisite_id bigint not null references courses(id),
  unique (course_id, prerequisite_id)
);

alter table prereg_registrations rename to prereg_schedule_registrations;

create table prereg_section_registrations (
    id                          bigint primary key,
    schedule_registration_id    bigint references prereg_schedule_registrations(id),
    student_id                  bigint not null references users(id),
    section_id                  bigint not null references prereg_sections(id),
    date                        timestamp not null default current_timestamp,
    prereq_met                  boolean,
  unique (student_id, section_id)
);

insert into prereg_section_registrations (id, schedule_registration_id, student_id, section_id, date )
    select nextval('hibernate_sequence'), r.id, r.student_id, s.section_id, r.date
        from prereg_schedule_registrations r, prereg_registration_sections s
        where r.id = s.registration_id;

delete from prereg_section_registrations where id in (
    select p2.id from prereg_section_registrations p1, prereg_section_registrations p2, prereg_sections s
        where p1.student_id = p2.student_id
        and p1.section_id = s.id and p2.section_id = s.linked_by);

drop table prereg_registration_sections;
