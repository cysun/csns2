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

insert into assignments (id, name, alias, section_id, due_date) values
    (1000500, 'Homework 1', 'HW1', 1000300, '2010-10-01');
insert into assignments (id, name, alias, section_id, due_date) values
    (1000501, 'Homework 2', 'HW2', 1000300, '2010-10-07');
insert into assignments (id, name, alias, section_id, due_date) values
    (1000502, 'Homework 1', 'HW1', 1000301, current_timestamp + interval '2 weeks');

insert into submissions (id, student_id, assignment_id) values
    (1000600, 1000002, 1000500);
insert into submissions (id, student_id, assignment_id) values
    (1000601, 1000003, 1000500);
insert into submissions (id, student_id, assignment_id) values
    (1000602, 1000003, 1000501);
insert into submissions (id, student_id, assignment_id) values
    (1000603, 1000002, 1000502);

insert into forums (id, name, course_id) values
    (1000700, 'CS101 Introduction to Computer Science', 1000200);
insert into forums (id, name, course_id) values
    (1000701, 'CS320 Web and Internet Programming', 1000201);
insert into forums (id, name, course_id) values
    (1000702, 'CS520 Web Programming', 1000202);
insert into forums (id, name, course_id) values
    (1000703, 'TECH250 Impact of Technology on Individuals and Society', 1000203);
insert into forums (id, name, course_id) values
    (1000704, 'MATH206 Calculus I: Differentiation', 1000204);
insert into forums (id, name, course_id) values
    (1000705, 'MATH207 Calculus II: Integration', 1000205);

insert into forums (id, name, department_id) values (1000720, 'Announcements', 1000100);
insert into forums (id, name, department_id) values (1000721, 'Advisement', 1000100);
insert into forums (id, name, department_id) values (1000722, 'Job Opportunities', 1000100);
insert into forums (id, name, department_id) values (1000723, 'General Discussion', 1000100);
insert into forums (id, name, department_id) values (1000724, 'Announcements', 1000101);
insert into forums (id, name, department_id) values (1000725, 'Advisement', 1000101);
insert into forums (id, name, department_id) values (1000726, 'Job Opportunities', 1000101);
insert into forums (id, name, department_id) values (1000727, 'General Discussion', 1000101);

insert into forum_topics (id, forum_id) values (1000800, 3000);
insert into forum_posts (id, subject, content, author_id, topic_id) values
    (1000900, 'Welcome', 'Welcome to CSNS', 1000001, 1000800);
update forum_topics set first_post_id=1000900, last_post_id=1000900 where id = 1000800;
update forums set num_of_topics = 1, num_of_posts = 1, last_post_id = 1000900 where id = 3000;
update users set num_of_forum_posts = 1 where id = 1000001;

insert into subscriptions (id, subscribable_type, subscribable_id, subscriber_id) values
    (1001000, 'FM', 3000, 1000001);
insert into subscriptions (id, subscribable_type, subscribable_id, subscriber_id) values
    (1001001, 'FM', 1000702, 1000001);
insert into subscriptions (id, subscribable_type, subscribable_id, subscriber_id) values
    (1001002, 'FM', 1000720, 1000001);
insert into subscriptions (id, subscribable_type, subscribable_id, subscriber_id) values
    (1001003, 'FT', 1000800, 1000001);

insert into resources (id, name, type) values (1001100, 'Project Documentation', 1);
insert into resources (id, name, type) values (1001101, 'Project Presentation', 1);

insert into projects (id, name, department_id, year) values
    (1001200, 'Modernize Curriculum Review Workflow', 1000100, 2013);
insert into project_advisors (project_id, advisor_id, advisor_order) values (1001200, 1000001, 0);
insert into project_advisors (project_id, advisor_id, advisor_order) values (1001200, 1000004, 1);
insert into project_members (project_id, member_id) values (1001200, 1000002);
insert into project_members (project_id, member_id) values (1001200, 1000003);
insert into project_resources (project_id, resource_id, resource_order) values (1001200, 1001100, 0);
insert into project_resources (project_id, resource_id, resource_order) values (1001200, 1001101, 1);
