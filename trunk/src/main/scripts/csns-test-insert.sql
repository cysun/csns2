-- This script inserts some test data.

-- The id's of the test data should be between 1,000,000 and 2,000,000 to
-- avoid id conflicts with the data inserted in csns-create.sql (id < 1,000,000)
-- and the data inserted later (id > 2,000,000).

insert into users (id, cin, username, password, last_name, first_name, primary_email) values
    (1000001, '1000001', 'cysun', md5('abcd'), 'Sun', 'Chengyu', 'cysun@localhost.localdomain');
insert into users (id, cin, username, password, last_name, first_name, primary_email) values
    (1000002, '1000002', 'jdoe1', md5('abcd'), 'Doe', 'John', 'jdoe1@localhost.localdomain');
insert into users (id, cin, username, password, last_name, first_name, primary_email) values
    (1000003, '1000003', 'jdoe2', md5('abcd'), 'Doe', 'Jane', 'jdoe2@localhost.localdomain');
insert into users (id, cin, username, password, last_name, first_name, primary_email) values
    (1000004, '1000004', 'rpamula', md5('abcd'), 'Pamula', 'Raj', 'rpamula@localhost.localdomain');
insert into users (id, cin, username, password, last_name, first_name, primary_email) values
    (1000005, '1000005', 'ttrejo', md5('abcd'), 'Trejo', 'Tricia', 'ttrejo@localhost.localdomain');

insert into authorities values (1000005, 'DEPT_ROLE_ADMIN_cs');
insert into authorities values (1000004, 'DEPT_ROLE_ADMIN_cs');
insert into authorities values (1000004, 'DEPT_ROLE_FACULTY_cs');
insert into authorities values (1000001, 'DEPT_ROLE_FACULTY_cs');
insert into authorities values (1000002, 'DEPT_ROLE_INSTRUCTOR_cs');

insert into departments (id, name, abbreviation) values (1000100, 'Computer Science', 'cs');
insert into departments (id, name, abbreviation) values (1000101, 'Mathematics', 'math');

insert into department_administrators values (1000100, 1000005);
insert into department_administrators values (1000100, 1000004);
insert into department_faculty values (1000100, 1000004);
insert into department_faculty values (1000100, 1000001);
insert into department_instructors values (1000100, 1000002);

insert into courses (id, code, name, coordinator_id) values
    (1000200, 'CS101', 'Introduction to Computer Science', null);
insert into courses (id, code, name, coordinator_id) values
    (1000201, 'CS320', 'Web and Internet Programming', 1000001);
insert into courses (id, code, name, coordinator_id) values
    (1000202, 'CS520', 'Web Programming', null);
insert into courses (id, code, name, coordinator_id) values
    (1000203, 'TECH250', 'Impact of Technology on Individuals and Society', null);
insert into courses (id, code, name, coordinator_id) values
    (1000204, 'MATH206', 'Calculus I: Differentiation', null);
insert into courses (id, code, name, coordinator_id) values
    (1000205, 'MATH207', 'Calculus II: Integration', null);

insert into department_undergraduate_courses values (1000100, 1000200);
insert into department_undergraduate_courses values (1000100, 1000201);
insert into department_additional_undergraduate_courses values (1000100, 1000203);
insert into department_graduate_courses values (1000100, 1000202);
insert into department_undergraduate_courses values (1000101, 1000204);
insert into department_undergraduate_courses values (1000101, 1000205);

insert into sections values (1000300, 1109, 1000204, 1);
insert into sections values (1000301, quarter(), 1000201, 1);
insert into sections values (1000302, quarter(), 1000202, 1);

insert into section_instructors values (1000300, 1000001, 0);
insert into section_instructors values (1000301, 1000001, 0);
insert into section_instructors values (1000302, 1000001, 0);

insert into enrollments (id, section_id, student_id) values (1000400, 1000300, 1000002);
insert into enrollments (id, section_id, student_id) values (1000401, 1000300, 1000003);
insert into enrollments (id, section_id, student_id) values (1000402, 1000301, 1000002);
insert into enrollments (id, section_id, student_id) values (1000403, 1000302, 1000003);
