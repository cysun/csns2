-- This script removes everything in a CSNS database.

set client_min_messages=WARNING;

drop sequence hibernate_sequence;

drop function quarter();
drop function quarter(date);

drop table department_additional_graduate_courses;
drop table department_graduate_courses;
drop table department_additional_undergraduate_courses;
drop table department_undergraduate_courses;
drop table department_reviewers;
drop table department_instructors;
drop table department_faculty;
drop table department_administrators;
drop table departments;

drop table enrollments;
drop table section_instructors;
drop table sections;
drop trigger courses_ts_trigger on courses;
drop function courses_ts_trigger_function();
drop table courses;

drop table grades;

drop table files;

drop table persistent_logins;
drop table authorities;
drop table users;
