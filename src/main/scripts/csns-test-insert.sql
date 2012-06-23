-- This script inserts some test data.

insert into users (id, cin, username, password, last_name, first_name, primary_email) values
    (1001, '1001', 'cysun', md5('abcd'), 'Sun', 'Chengyu', 'cysun@localhost.localdomain');

insert into authorities values (1001, 'DEPT_ROLE_ADMIN_cs');
insert into authorities values (1001, 'DEPT_ROLE_FACULTY_cs');

insert into departments (id, name, abbreviation) values (1100, 'Computer Science', 'cs');
insert into departments (id, name, abbreviation) values (1101, 'Technology', 'tech');

insert into department_administrators values (1100, 1001);
insert into department_faculty values (1100, 1001);

insert into courses (id, code, name, coordinator_id) values
    (1200, 'CS101', 'Introduction to Computer Science', null);
insert into courses (id, code, name, coordinator_id) values
    (1201, 'CS320', 'Web and Internet Programming', 1001);
insert into courses (id, code, name, coordinator_id) values
    (1202, 'CS520', 'Web Programming', null);
insert into courses (id, code, name, coordinator_id) values
    (1203, 'TECH250', 'Impact of Technology on Individuals and Society', null);

insert into department_undergraduate_courses values (1100, 1200);
insert into department_undergraduate_courses values (1100, 1201);
insert into department_undergraduate_courses values (1101, 1203);
insert into department_additional_undergraduate_courses values (1100, 1203);
insert into department_graduate_courses values (1100, 1202);
