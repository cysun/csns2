
    create table WikiPageSearchResult (
        id int8 not null,
        content varchar(255),
        path varchar(255),
        subject varchar(255),
        primary key (id)
    );

    create table answer_sections (
        id int8 not null,
        section_index int4 not null,
        answer_sheet_id int8 not null,
        primary key (id)
    );

    create table answer_selections (
        answer_id int8 not null,
        selection int4
    );

    create table answer_sheets (
        id int8 not null,
        date timestamp,
        author_id int8,
        question_sheet_id int8 not null,
        primary key (id)
    );

    create table answers (
        answer_type varchar(31) not null,
        id int8 not null,
        answer_index int4,
        rating int4,
        text varchar(255),
        question_id int8,
        answer_section_id int8 not null,
        attachment_id int8,
        primary key (id)
    );

    create table assignments (
        assignment_type varchar(31) not null,
        id int8 not null,
        alias varchar(255) not null,
        available_after_due_date boolean not null,
        due_date timestamp,
        file_extensions varchar(255),
        max_file_size int8,
        name varchar(255) not null,
        publish_date timestamp,
        total_points varchar(255),
        section_id int8,
        question_sheet_id int8 unique,
        primary key (id)
    );

    create table authorities (
        user_id int8 not null,
        role varchar(255)
    );

    create table courses (
        id int8 not null,
        code varchar(255) not null unique,
        max_units int4 not null,
        min_units int4 not null,
        name varchar(255) not null,
        obsolete boolean not null,
        coordinator_id int8,
        syllabus_id int8,
        primary key (id)
    );

    create table department_additional_graduate_courses (
        department_id int8 not null,
        course_id int8 not null
    );

    create table department_additional_undergraduate_courses (
        department_id int8 not null,
        course_id int8 not null
    );

    create table department_administrators (
        department_id int8 not null,
        user_id int8 not null
    );

    create table department_faculty (
        department_id int8 not null,
        user_id int8 not null
    );

    create table department_graduate_courses (
        department_id int8 not null,
        course_id int8 not null
    );

    create table department_instructors (
        department_id int8 not null,
        user_id int8 not null
    );

    create table department_reviewers (
        department_id int8 not null,
        user_id int8 not null
    );

    create table department_undergraduate_courses (
        department_id int8 not null,
        course_id int8 not null
    );

    create table departments (
        id int8 not null,
        abbreviation varchar(255) not null unique,
        name varchar(255) not null unique,
        welcome_message varchar(255),
        primary key (id)
    );

    create table enrollments (
        id int8 not null,
        comments varchar(255),
        grade_mailed boolean not null,
        grade_id int8,
        section_id int8 not null,
        student_id int8 not null,
        primary key (id),
        unique (section_id, student_id)
    );

    create table files (
        id int8 not null,
        date timestamp not null,
        deleted boolean not null,
        folder boolean not null,
        public boolean not null,
        name varchar(255) not null,
        regular boolean not null,
        size int8,
        type varchar(255),
        owner_id int8 not null,
        parent_id int8,
        submission_id int8,
        primary key (id)
    );

    create table forum_moderators (
        forum_id int8 not null,
        user_id int8 not null,
        primary key (forum_id, user_id)
    );

    create table forum_post_attachments (
        post_id int8 not null,
        file_id int8 not null
    );

    create table forum_posts (
        id int8 not null,
        content varchar(255) not null,
        date timestamp,
        subject varchar(255) not null,
        edit_date timestamp,
        author_id int8,
        edited_by int8,
        topic_id int8,
        primary key (id)
    );

    create table forum_topics (
        id int8 not null,
        deleted boolean not null,
        num_of_views int4 not null,
        pinned boolean not null,
        first_post_id int8,
        forum_id int8,
        last_post_id int8,
        primary key (id)
    );

    create table forums (
        id int8 not null,
        date timestamp,
        description varchar(255),
        hidden boolean not null,
        name varchar(255) not null,
        num_of_posts int4 not null,
        num_of_topics int4 not null,
        course_id int8 unique,
        department_id int8,
        last_post_id int8 unique,
        primary key (id)
    );

    create table grades (
        id int8 not null,
        description varchar(255),
        symbol varchar(255) not null unique,
        value float8,
        primary key (id)
    );

    create table project_advisors (
        project_id int8 not null,
        advisor_id int8 not null,
        advisor_order int4 not null,
        primary key (project_id, advisor_order)
    );

    create table project_members (
        project_id int8 not null,
        member_id int8 not null
    );

    create table project_resources (
        project_id int8 not null,
        resource_id int8 not null,
        resource_order int4 not null,
        primary key (project_id, resource_order)
    );

    create table projects (
        id int8 not null,
        description varchar(255),
        name varchar(255) not null,
        published boolean not null,
        year int4 not null,
        department_id int8,
        primary key (id)
    );

    create table question_choices (
        question_id int8 not null,
        choice varchar(255),
        choice_index int4 not null,
        primary key (question_id, choice_index)
    );

    create table question_correct_selections (
        question_id int8 not null,
        selection int4
    );

    create table question_sections (
        id int8 not null,
        description varchar(255),
        question_sheet_id int8,
        section_index int4,
        primary key (id)
    );

    create table question_sheets (
        id int8 not null,
        description varchar(255),
        primary key (id)
    );

    create table questions (
        question_type varchar(31) not null,
        id int8 not null,
        description varchar(255),
        point_value int4 not null,
        max_selections int4,
        min_selections int4,
        attachment_allowed boolean not null,
        correct_answer varchar(255),
        text_length int4,
        max_rating int4,
        min_rating int4,
        question_section_id int8,
        question_index int4,
        primary key (id)
    );

    create table resources (
        id int8 not null,
        name varchar(255),
        text varchar(255),
        type int4 not null,
        url varchar(255),
        file_id int8,
        primary key (id)
    );

    create table section_instructors (
        section_id int8 not null,
        instructor_id int8 not null,
        instructor_order int4 not null,
        primary key (section_id, instructor_order)
    );

    create table sections (
        id int8 not null,
        number int4 not null,
        quarter int4 not null,
        course_id int8 not null,
        primary key (id),
        unique (quarter, course_id, number)
    );

    create table submissions (
        submission_type varchar(31) not null,
        id int8 not null,
        comments varchar(255),
        due_date timestamp,
        grade varchar(255),
        grade_mailed boolean not null,
        finished boolean not null,
        saved boolean not null,
        assignment_id int8 not null,
        student_id int8 not null,
        answer_sheet_id int8 unique,
        primary key (id),
        unique (student_id, assignment_id)
    );

    create table subscriptions (
        id int8 not null,
        auto_subscribed boolean not null,
        date timestamp not null,
        notification_sent boolean not null,
        subscribable_type varchar(255),
        subscribable_id int8,
        subscriber_id int8 not null,
        primary key (id)
    );

    create table survey_responses (
        id int8 not null,
        answer_sheet_id int8 not null unique,
        survey_id int8 not null,
        primary key (id)
    );

    create table surveys (
        id int8 not null,
        close_date timestamp,
        date timestamp not null,
        deleted boolean not null,
        name varchar(255) not null,
        publish_date timestamp,
        type varchar(255),
        author_id int8 not null,
        department_id int8,
        question_sheet_id int8 not null unique,
        primary key (id)
    );

    create table surveys_taken (
        user_id int8 not null,
        survey_id int8 not null,
        primary key (user_id, survey_id)
    );

    create table users (
        id int8 not null,
        birthday date,
        cell_phone varchar(255),
        cin varchar(255) not null unique,
        city varchar(255),
        enabled boolean not null,
        first_name varchar(255) not null,
        gender varchar(255),
        home_phone varchar(255),
        last_name varchar(255) not null,
        middle_name varchar(255),
        num_of_forum_posts int4 not null,
        office_phone varchar(255),
        password varchar(255) not null,
        primary_email varchar(255) not null unique,
        secondary_email varchar(255),
        state varchar(255),
        street varchar(255),
        temporary boolean not null,
        username varchar(255) not null unique,
        zip varchar(255),
        primary key (id)
    );

    create table wiki_discussions (
        page_id int8 not null,
        topic_id int8 not null
    );

    create table wiki_pages (
        id int8 not null,
        locked boolean not null,
        password varchar(255),
        path varchar(255) not null unique,
        views int4 not null,
        owner_id int8 not null,
        primary key (id)
    );

    create table wiki_revision_attachments (
        revision_id int8 not null,
        file_id int8 not null
    );

    create table wiki_revisions (
        id int8 not null,
        content varchar(255) not null,
        date timestamp,
        subject varchar(255) not null,
        include_sidebar boolean not null,
        author_id int8,
        page_id int8 not null,
        primary key (id)
    );

    alter table answer_sections 
        add constraint FK96B4258F9AA31C1D 
        foreign key (answer_sheet_id) 
        references answer_sheets;

    alter table answer_selections 
        add constraint FK3DB533885DC9F17B 
        foreign key (answer_id) 
        references answers;

    alter table answer_sheets 
        add constraint FK4BB67A95447A76EB 
        foreign key (author_id) 
        references users;

    alter table answer_sheets 
        add constraint FK4BB67A95810289CD 
        foreign key (question_sheet_id) 
        references question_sheets;

    alter table answers 
        add constraint FKCD7DB87592AF0204 
        foreign key (attachment_id) 
        references files;

    alter table answers 
        add constraint FKCD7DB875F424C19D 
        foreign key (answer_section_id) 
        references answer_sections;

    alter table answers 
        add constraint FKCD7DB8752E9C937A 
        foreign key (question_id) 
        references questions;

    alter table assignments 
        add constraint FK68455346DA3A2B9A 
        foreign key (section_id) 
        references sections;

    alter table assignments 
        add constraint FK68455346810289CD 
        foreign key (question_sheet_id) 
        references question_sheets;

    alter table authorities 
        add constraint FK2B0F1321E3C184AB 
        foreign key (user_id) 
        references users;

    alter table courses 
        add constraint FK391923B8AFCBF26 
        foreign key (coordinator_id) 
        references users;

    alter table courses 
        add constraint FK391923B85EEDFE02 
        foreign key (syllabus_id) 
        references files;

    alter table department_additional_graduate_courses 
        add constraint FKE15B3A6FF7F6787A 
        foreign key (department_id) 
        references departments;

    alter table department_additional_graduate_courses 
        add constraint FKE15B3A6F90C57DA 
        foreign key (course_id) 
        references courses;

    alter table department_additional_undergraduate_courses 
        add constraint FKDCE084D1F7F6787A 
        foreign key (department_id) 
        references departments;

    alter table department_additional_undergraduate_courses 
        add constraint FKDCE084D190C57DA 
        foreign key (course_id) 
        references courses;

    alter table department_administrators 
        add constraint FKB0C173D3F7F6787A 
        foreign key (department_id) 
        references departments;

    alter table department_administrators 
        add constraint FKB0C173D3E3C184AB 
        foreign key (user_id) 
        references users;

    alter table department_faculty 
        add constraint FK77021277F7F6787A 
        foreign key (department_id) 
        references departments;

    alter table department_faculty 
        add constraint FK77021277E3C184AB 
        foreign key (user_id) 
        references users;

    alter table department_graduate_courses 
        add constraint FKF9899A11F7F6787A 
        foreign key (department_id) 
        references departments;

    alter table department_graduate_courses 
        add constraint FKF9899A1190C57DA 
        foreign key (course_id) 
        references courses;

    alter table department_instructors 
        add constraint FK6B1F3A09F7F6787A 
        foreign key (department_id) 
        references departments;

    alter table department_instructors 
        add constraint FK6B1F3A09E3C184AB 
        foreign key (user_id) 
        references users;

    alter table department_reviewers 
        add constraint FK29BBF261F7F6787A 
        foreign key (department_id) 
        references departments;

    alter table department_reviewers 
        add constraint FK29BBF261E3C184AB 
        foreign key (user_id) 
        references users;

    alter table department_undergraduate_courses 
        add constraint FKE2469A6FF7F6787A 
        foreign key (department_id) 
        references departments;

    alter table department_undergraduate_courses 
        add constraint FKE2469A6F90C57DA 
        foreign key (course_id) 
        references courses;

    alter table enrollments 
        add constraint FKD680FDEFC0F82E5A 
        foreign key (grade_id) 
        references grades;

    alter table enrollments 
        add constraint FKD680FDEFAEFD183B 
        foreign key (student_id) 
        references users;

    alter table enrollments 
        add constraint FKD680FDEFDA3A2B9A 
        foreign key (section_id) 
        references sections;

    alter table files 
        add constraint FK5CEBA7767E6511D 
        foreign key (parent_id) 
        references files;

    alter table files 
        add constraint FK5CEBA774FA834C3 
        foreign key (owner_id) 
        references users;

    alter table files 
        add constraint FK5CEBA77B350BE3A 
        foreign key (submission_id) 
        references submissions;

    alter table forum_moderators 
        add constraint FK7C3AF5ECE3C184AB 
        foreign key (user_id) 
        references users;

    alter table forum_moderators 
        add constraint FK7C3AF5ECFE5A182F 
        foreign key (forum_id) 
        references forums;

    alter table forum_post_attachments 
        add constraint FKB4866DEFB9895B0B 
        foreign key (file_id) 
        references files;

    alter table forum_post_attachments 
        add constraint FKB4866DEF2186D105 
        foreign key (post_id) 
        references forum_posts;

    alter table forum_posts 
        add constraint FKEDDC4F35447A76EB 
        foreign key (author_id) 
        references users;

    alter table forum_posts 
        add constraint FKEDDC4F35A958756F 
        foreign key (topic_id) 
        references forum_topics;

    alter table forum_posts 
        add constraint FKEDDC4F357D9A8AC9 
        foreign key (edited_by) 
        references users;

    alter table forum_topics 
        add constraint FKD47F7202FE5A182F 
        foreign key (forum_id) 
        references forums;

    alter table forum_topics 
        add constraint FKD47F720217FF9A76 
        foreign key (first_post_id) 
        references forum_posts;

    alter table forum_topics 
        add constraint FKD47F720243B0145C 
        foreign key (last_post_id) 
        references forum_posts;

    alter table forums 
        add constraint FKB4601772F7F6787A 
        foreign key (department_id) 
        references departments;

    alter table forums 
        add constraint FKB460177290C57DA 
        foreign key (course_id) 
        references courses;

    alter table forums 
        add constraint FKB460177243B0145C 
        foreign key (last_post_id) 
        references forum_posts;

    alter table project_advisors 
        add constraint FK8EBFEB19C30E4F1A 
        foreign key (project_id) 
        references projects;

    alter table project_advisors 
        add constraint FK8EBFEB19C5DD9096 
        foreign key (advisor_id) 
        references users;

    alter table project_members 
        add constraint FKA9E291F33C873F7C 
        foreign key (member_id) 
        references users;

    alter table project_members 
        add constraint FKA9E291F3C30E4F1A 
        foreign key (project_id) 
        references projects;

    alter table project_resources 
        add constraint FKF551B87FC30E4F1A 
        foreign key (project_id) 
        references projects;

    alter table project_resources 
        add constraint FKF551B87FC92D294B 
        foreign key (resource_id) 
        references resources;

    alter table projects 
        add constraint FKC479187AF7F6787A 
        foreign key (department_id) 
        references departments;

    alter table question_choices 
        add constraint FKCCF0F399376C843B 
        foreign key (question_id) 
        references questions;

    alter table question_correct_selections 
        add constraint FKC4E1AC55376C843B 
        foreign key (question_id) 
        references questions;

    alter table question_sections 
        add constraint FK9CB15667810289CD 
        foreign key (question_sheet_id) 
        references question_sheets;

    alter table questions 
        add constraint FK95C5414DC05F834D 
        foreign key (question_section_id) 
        references question_sections;

    alter table resources 
        add constraint FK89CCBE25B9895B0B 
        foreign key (file_id) 
        references files;

    alter table section_instructors 
        add constraint FK8C3CB11C644C5699 
        foreign key (instructor_id) 
        references users;

    alter table section_instructors 
        add constraint FK8C3CB11CDA3A2B9A 
        foreign key (section_id) 
        references sections;

    alter table sections 
        add constraint FK38805E2E90C57DA 
        foreign key (course_id) 
        references courses;

    alter table submissions 
        add constraint FK2912EA73583829A 
        foreign key (assignment_id) 
        references assignments;

    alter table submissions 
        add constraint FK2912EA7AEFD183B 
        foreign key (student_id) 
        references users;

    alter table submissions 
        add constraint FK2912EA79AA31C1D 
        foreign key (answer_sheet_id) 
        references answer_sheets;

    alter table subscriptions 
        add constraint FK7674CAF64A4802EE 
        foreign key (subscriber_id) 
        references users;

    alter table survey_responses 
        add constraint FK86922DAD5B66DD70 
        foreign key (survey_id) 
        references surveys;

    alter table survey_responses 
        add constraint FK86922DAD9AA31C1D 
        foreign key (answer_sheet_id) 
        references answer_sheets;

    alter table surveys 
        add constraint FK91914459F7F6787A 
        foreign key (department_id) 
        references departments;

    alter table surveys 
        add constraint FK91914459447A76EB 
        foreign key (author_id) 
        references users;

    alter table surveys 
        add constraint FK91914459810289CD 
        foreign key (question_sheet_id) 
        references question_sheets;

    alter table surveys_taken 
        add constraint FK95459D61E3C184AB 
        foreign key (user_id) 
        references users;

    alter table surveys_taken 
        add constraint FK95459D615B66DD70 
        foreign key (survey_id) 
        references surveys;

    alter table wiki_discussions 
        add constraint FKE66DD33CA958756F 
        foreign key (topic_id) 
        references forum_topics;

    alter table wiki_discussions 
        add constraint FKE66DD33C773840BA 
        foreign key (page_id) 
        references wiki_pages;

    alter table wiki_pages 
        add constraint FK24357E754FA834C3 
        foreign key (owner_id) 
        references users;

    alter table wiki_revision_attachments 
        add constraint FKF0800A7BB9895B0B 
        foreign key (file_id) 
        references files;

    alter table wiki_revision_attachments 
        add constraint FKF0800A7BD5F8FD3A 
        foreign key (revision_id) 
        references wiki_revisions;

    alter table wiki_revisions 
        add constraint FKE0471229447A76EB 
        foreign key (author_id) 
        references users;

    alter table wiki_revisions 
        add constraint FKE0471229773840BA 
        foreign key (page_id) 
        references wiki_pages;

    create sequence hibernate_sequence;
