
    create table RubricEvaluationStats (
        id int8 not null,
        count int4,
        indicator int4,
        max int4,
        mean float8,
        median float8,
        min int4,
        type varchar(255),
        year int4,
        primary key (id)
    );

    create table WikiSearchResult (
        id int8 not null,
        content varchar(255),
        path varchar(255),
        subject varchar(255),
        primary key (id)
    );

    create table academic_standings (
        id int8 not null,
        quarter int4,
        department_id int8,
        standing_id int8,
        student_id int8,
        primary key (id)
    );

    create table advisement_record_attachments (
        record_id int8 not null,
        file_id int8 not null
    );

    create table advisement_records (
        id int8 not null,
        comment varchar(255),
        date timestamp,
        deleted boolean not null,
        editable boolean not null,
        emailed_to_student boolean not null,
        for_advisors_only boolean not null,
        advisor_id int8 not null,
        student_id int8 not null,
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
        deleted boolean not null,
        due_date timestamp,
        file_extensions varchar(255),
        max_file_size int8,
        name varchar(255) not null,
        publish_date timestamp,
        total_points varchar(255),
        resource_id int8,
        section_id int8 not null,
        question_sheet_id int8,
        primary key (id)
    );

    create table authorities (
        user_id int8 not null,
        role varchar(255)
    );

    create table course_journal_assignments (
        course_journal_id int8 not null,
        assignment_id int8 not null,
        assignment_order int4 not null,
        primary key (course_journal_id, assignment_order)
    );

    create table course_journal_handouts (
        course_journal_id int8 not null,
        resource_id int8 not null,
        handout_order int4 not null,
        primary key (course_journal_id, handout_order)
    );

    create table course_journal_student_samples (
        course_journal_id int8 not null,
        enrollment_id int8 not null
    );

    create table course_journals (
        id int8 not null,
        approve_date timestamp,
        submit_date timestamp,
        primary key (id)
    );

    create table course_mapping_group1 (
        mapping_id int8 not null,
        course_id int8 not null
    );

    create table course_mapping_group2 (
        mapping_id int8 not null,
        course_id int8 not null
    );

    create table course_mappings (
        id int8 not null,
        deleted boolean not null,
        department_id int8 not null,
        primary key (id)
    );

    create table course_substitutions (
        id int8 not null,
        advisement_record_id int8,
        original_course_id int8,
        substitute_course_id int8,
        primary key (id)
    );

    create table course_transfers (
        id int8 not null,
        advisement_record_id int8,
        course_id int8,
        primary key (id)
    );

    create table course_waivers (
        id int8 not null,
        advisement_record_id int8,
        course_id int8,
        primary key (id)
    );

    create table courses (
        id int8 not null,
        code varchar(255) not null,
        max_units int4 not null,
        min_units int4 not null,
        name varchar(255) not null,
        obsolete boolean not null,
        coordinator_id int8,
        department_id int8,
        description_id int8,
        journal_id int8,
        primary key (id)
    );

    create table current_standings (
        student_id int8 not null,
        academic_standing_id int8 not null,
        department_id int8 not null,
        primary key (student_id, department_id)
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

    create table department_evaluators (
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

    create table department_options (
        department_id int8 not null,
        option varchar(255) not null,
        primary key (department_id, option)
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
        abbreviation varchar(255) not null,
        full_name varchar(255) not null,
        name varchar(255) not null,
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
        primary key (id)
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
        reference_id int8,
        submission_id int8,
        primary key (id)
    );

    create table forum_members (
        forum_id int8 not null,
        user_id int8 not null,
        primary key (forum_id, user_id)
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
        last_post_date timestamp,
        num_of_posts int4 not null,
        num_of_views int4 not null,
        pinned boolean not null,
        first_post_id int8,
        forum_id int8 not null,
        last_post_id int8,
        primary key (id)
    );

    create table forums (
        id int8 not null,
        date timestamp,
        description varchar(255),
        hidden boolean not null,
        members_only boolean not null,
        name varchar(255) not null,
        num_of_posts int4 not null,
        num_of_topics int4 not null,
        course_id int8,
        department_id int8,
        last_post_id int8,
        primary key (id)
    );

    create table grades (
        id int8 not null,
        description varchar(255),
        symbol varchar(255) not null,
        value float8,
        primary key (id)
    );

    create table mailinglist_message_attachments (
        message_id int8 not null,
        file_id int8 not null
    );

    create table mailinglist_messages (
        id int8 not null,
        content varchar(255) not null,
        date timestamp,
        subject varchar(255) not null,
        author_id int8,
        mailinglist_id int8,
        primary key (id)
    );

    create table mailinglists (
        id int8 not null,
        description varchar(255),
        name varchar(255) not null,
        department_id int8,
        primary key (id)
    );

    create table mft_distribution_entries (
        distribution_id int8 not null,
        percentile int4 not null,
        value int4 not null
    );

    create table mft_distribution_types (
        id int8 not null,
        alias varchar(255) not null,
        max int4 not null,
        min int4 not null,
        name varchar(255) not null,
        value_label varchar(255),
        department_id int8 not null,
        primary key (id)
    );

    create table mft_distributions (
        id int8 not null,
        deleted boolean not null,
        from_date timestamp,
        mean float8,
        median float8,
        num_of_samples int4,
        stdev float8,
        to_date timestamp,
        year int4 not null,
        type_id int8 not null,
        primary key (id)
    );

    create table mft_indicators (
        id int8 not null,
        ai1 int4 not null,
        ai2 int4 not null,
        ai3 int4 not null,
        date timestamp not null,
        deleted boolean not null,
        num_of_students int4 not null,
        department_id int8 not null,
        primary key (id)
    );

    create table mft_scores (
        id int8 not null,
        date timestamp not null,
        value int4 not null,
        department_id int8 not null,
        user_id int8 not null,
        primary key (id)
    );

    create table news (
        id int8 not null,
        expire_date timestamp,
        department_id int8,
        topic_id int8 not null,
        primary key (id)
    );

    create table program_elective_courses (
        program_id int8 not null,
        course_id int8 not null
    );

    create table program_required_courses (
        program_id int8 not null,
        course_id int8 not null
    );

    create table programs (
        id int8 not null,
        description varchar(255),
        name varchar(255) not null,
        department_id int8,
        primary key (id)
    );

    create table project_advisors (
        project_id int8 not null,
        advisor_id int8 not null
    );

    create table project_liaisons (
        project_id int8 not null,
        liaison_id int8 not null
    );

    create table project_resources (
        project_id int8 not null,
        resource_id int8 not null,
        resource_order int4 not null,
        primary key (project_id, resource_order)
    );

    create table project_students (
        project_id int8 not null,
        student_id int8 not null
    );

    create table projects (
        id int8 not null,
        deleted boolean not null,
        description varchar(255),
        published boolean not null,
        sponsor varchar(255),
        title varchar(255) not null,
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
        attachment_allowed boolean not null,
        correct_answer varchar(255),
        text_length int4,
        max_selections int4,
        min_selections int4,
        max_rating int4,
        min_rating int4,
        question_section_id int8,
        question_index int4,
        primary key (id)
    );

    create table resources (
        id int8 not null,
        private boolean not null,
        name varchar(255),
        text varchar(255),
        type int4,
        url varchar(255),
        file_id int8,
        primary key (id)
    );

    create table rubric_assignments (
        id int8 not null,
        deleted boolean not null,
        due_date timestamp,
        evaluated_by_instructors boolean not null,
        evaluated_by_students boolean not null,
        name varchar(255) not null,
        publish_date timestamp,
        rubric_id int8 not null,
        section_id int8 not null,
        primary key (id)
    );

    create table rubric_evaluation_ratings (
        evaluation_id int8 not null,
        rating int4,
        rating_order int4 not null,
        primary key (evaluation_id, rating_order)
    );

    create table rubric_evaluations (
        id int8 not null,
        comments varchar(255),
        completed boolean not null,
        date timestamp,
        deleted boolean not null,
        type varchar(255) not null,
        evaluator_id int8,
        submission_id int8,
        primary key (id)
    );

    create table rubric_external_evaluators (
        rubric_assignment_id int8 not null,
        evaluator_id int8 not null
    );

    create table rubric_indicator_criteria (
        indicator_id int8 not null,
        criterion varchar(255),
        criterion_index int4 not null,
        primary key (indicator_id, criterion_index)
    );

    create table rubric_indicators (
        id int8 not null,
        name varchar(255) not null,
        rubric_id int8,
        indicator_index int4,
        primary key (id)
    );

    create table rubric_submissions (
        id int8 not null,
        external_evaluation_count int4 not null,
        instructor_evaluation_count int4 not null,
        peer_evaluation_count int4 not null,
        assignment_id int8 not null,
        student_id int8 not null,
        primary key (id)
    );

    create table rubrics (
        id int8 not null,
        deleted boolean not null,
        description varchar(255),
        public boolean not null,
        name varchar(255) not null,
        publish_date timestamp,
        scale int4 not null,
        creator_id int8,
        department_id int8,
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
        deleted boolean not null,
        number int4 not null,
        quarter int4 not null,
        course_id int8 not null,
        journal_id int8,
        syllabus_id int8,
        primary key (id)
    );

    create table site_announcements (
        id int8 not null,
        content varchar(255),
        date timestamp not null,
        site_id int8,
        primary key (id)
    );

    create table site_blocks (
        id int8 not null,
        hidden boolean not null,
        name varchar(255),
        type varchar(255) not null,
        site_id int8,
        block_index int4,
        primary key (id)
    );

    create table site_class_info (
        site_id int8 not null,
        name varchar(255),
        value varchar(255),
        entry_index int4 not null,
        primary key (site_id, entry_index)
    );

    create table site_item_additional_resources (
        item_id int8 not null,
        resource_id int8 not null
    );

    create table site_items (
        id int8 not null,
        hidden boolean not null,
        resource_id int8,
        block_id int8,
        item_index int4,
        primary key (id)
    );

    create table sites (
        id int8 not null,
        limited boolean not null,
        restricted boolean not null,
        shared boolean not null,
        folder_id int8,
        section_id int8,
        primary key (id)
    );

    create table standing_mailinglists (
        standing_id int8 not null,
        mailinglist varchar(255)
    );

    create table standings (
        id int8 not null,
        description varchar(255),
        name varchar(255),
        symbol varchar(255) not null,
        primary key (id)
    );

    create table submissions (
        submission_type varchar(31) not null,
        id int8 not null,
        comments varchar(255),
        due_date timestamp,
        file_count int4 not null,
        grade varchar(255),
        grade_mailed boolean not null,
        finished boolean not null,
        saved boolean not null,
        assignment_id int8 not null,
        student_id int8 not null,
        answer_sheet_id int8,
        primary key (id)
    );

    create table subscriptions (
        id int8 not null,
        auto_subscribed boolean not null,
        date timestamp not null,
        notification_sent boolean not null,
        quarter int4,
        subscribable_type varchar(255),
        subscribable_id int8,
        subscriber_id int8 not null,
        primary key (id)
    );

    create table survey_chart_points (
        id int8 not null,
        average float8,
        max float8,
        median float8,
        min float8,
        question_index int4 not null,
        section_index int4 not null,
        values_set boolean not null,
        survey_id int8,
        series_id int8,
        point_index int4,
        primary key (id)
    );

    create table survey_chart_series (
        id int8 not null,
        name varchar(255),
        stat_type varchar(255),
        chart_id int8,
        primary key (id)
    );

    create table survey_chart_xcoordinates (
        chart_id int8 not null,
        coordinate varchar(255),
        coordinate_order int4 not null,
        primary key (chart_id, coordinate_order)
    );

    create table survey_charts (
        id int8 not null,
        date timestamp,
        deleted boolean not null,
        name varchar(255) not null,
        x_label varchar(255),
        y_label varchar(255),
        y_max int4,
        y_min int4,
        author_id int8,
        department_id int8,
        primary key (id)
    );

    create table survey_responses (
        id int8 not null,
        answer_sheet_id int8 not null,
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
        type int4 not null,
        author_id int8 not null,
        department_id int8,
        question_sheet_id int8 not null,
        primary key (id)
    );

    create table surveys_taken (
        user_id int8 not null,
        survey_id int8 not null,
        primary key (user_id, survey_id)
    );

    create table users (
        id int8 not null,
        access_key varchar(255),
        birthday date,
        cell_phone varchar(255),
        cin varchar(255) not null,
        city varchar(255),
        disk_quota int4 not null,
        enabled boolean not null,
        first_name varchar(255) not null,
        gender varchar(255),
        home_phone varchar(255),
        last_name varchar(255) not null,
        middle_name varchar(255),
        num_of_forum_posts int4 not null,
        password varchar(255) not null,
        primary_email varchar(255) not null,
        secondary_email varchar(255),
        state varchar(255),
        street varchar(255),
        temporary boolean not null,
        username varchar(255) not null,
        work_phone varchar(255),
        zip varchar(255),
        major_id int8,
        original_picture_id int8,
        profile_picture_id int8,
        profile_thumbnail_id int8,
        program_id int8,
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
        path varchar(255) not null,
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

    alter table academic_standings 
        add constraint UK_7hsf5t3wd3nfdl4ckq4tgtjx unique (student_id, department_id, standing_id);

    alter table assignments 
        add constraint UK_7ed6gywfn7fll8aklgvqwxnlr unique (question_sheet_id);

    alter table course_journal_student_samples 
        add constraint UK_p314skj0hft837qpu21d77dgj unique (course_journal_id, enrollment_id);

    alter table course_mapping_group1 
        add constraint UK_iedqsk6j6wo8mmuv8u9vjld8 unique (mapping_id, course_id);

    alter table course_mapping_group2 
        add constraint UK_gtwgoyu288y5wwjs8f1ofl1jl unique (mapping_id, course_id);

    alter table courses 
        add constraint UK_61og8rbqdd2y28rx2et5fdnxd unique (code);

    alter table courses 
        add constraint UK_mncv6or01lin4xetkt2srxwhg unique (journal_id);

    alter table current_standings 
        add constraint UK_4a9ef8cucw6u69q55ovpnoxck unique (academic_standing_id);

    alter table departments 
        add constraint UK_q6v6nnrch3oi9l7t9on9ik3l8 unique (abbreviation);

    alter table departments 
        add constraint UK_tjdrq9iqk4unsx9c7od2ayrmv unique (full_name);

    alter table departments 
        add constraint UK_j6cwks7xecs5jov19ro8ge3qk unique (name);

    alter table enrollments 
        add constraint UK_j1fpm330vse7gvfghxpg2n5tp unique (section_id, student_id);

    alter table forums 
        add constraint UK_hq8kwpokg7x9sbmsr89lx4y6v unique (course_id);

    alter table forums 
        add constraint UK_cnb8dm13flqpo2v206p3jj5qd unique (last_post_id);

    alter table grades 
        add constraint UK_2ljyc433n941undoa4gnv7ony unique (symbol);

    alter table mft_distribution_types 
        add constraint UK_bkgmth8ayk24wvyx04uyxcfhh unique (department_id, alias);

    alter table mft_distributions 
        add constraint UK_jugi4cmmvdf3ygjfwhrp4o8cn unique (year, type_id);

    alter table mft_indicators 
        add constraint UK_s4t9k14a550c0f27x1cfwgq46 unique (department_id, date);

    alter table mft_scores 
        add constraint UK_cxmltfnit7bi608roobfafund unique (department_id, user_id, date);

    alter table program_elective_courses 
        add constraint UK_akda9dtkf03rx2p1iql5m38n3 unique (program_id, course_id);

    alter table program_required_courses 
        add constraint UK_9oju6q704367bp22axidu8ek8 unique (program_id, course_id);

    alter table sections 
        add constraint UK_i3480gt0sgeo3myuiwmipxnah unique (quarter, course_id, number);

    alter table sections 
        add constraint UK_2ei8itlc2p5gvjggaooay98tu unique (journal_id);

    alter table standings 
        add constraint UK_frgiry5vvnksrumybvsdws5l9 unique (symbol);

    alter table submissions 
        add constraint UK_eiqoen8c565i0gq79ritryilw unique (student_id, assignment_id);

    alter table submissions 
        add constraint UK_hgewyd3vq7gvuflex6b20l7mx unique (answer_sheet_id);

    alter table survey_responses 
        add constraint UK_5yuaiow1kfy6r5p1ri01ls6mt unique (answer_sheet_id);

    alter table surveys 
        add constraint UK_a0d19kmc1nmh63eku46mqkmx9 unique (question_sheet_id);

    alter table users 
        add constraint UK_ipai46s7a8x10kyobfh05usmu unique (access_key);

    alter table users 
        add constraint UK_ka6m8ghsr7vna1ti6lftwww8o unique (cin);

    alter table users 
        add constraint UK_rf5m47pvq2fh48mh8wrbsrbns unique (primary_email);

    alter table users 
        add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);

    alter table wiki_pages 
        add constraint UK_dktfi88v5q4k48ngvq8fkpmsf unique (path);

    alter table academic_standings 
        add constraint FK_gdwxyd7l658e60ftb5efiblw3 
        foreign key (department_id) 
        references departments;

    alter table academic_standings 
        add constraint FK_b4efy1mapbnrhsficy5pd1jvs 
        foreign key (standing_id) 
        references standings;

    alter table academic_standings 
        add constraint FK_2mqngflh0tlfytmuh631ndfro 
        foreign key (student_id) 
        references users;

    alter table advisement_record_attachments 
        add constraint FK_pdhlg3do8r03448s8nppx50p4 
        foreign key (file_id) 
        references files;

    alter table advisement_record_attachments 
        add constraint FK_dsqilmt9xqt87fqrva6u88jms 
        foreign key (record_id) 
        references advisement_records;

    alter table advisement_records 
        add constraint FK_spfviubr6oegk0fne17kknp66 
        foreign key (advisor_id) 
        references users;

    alter table advisement_records 
        add constraint FK_lwpf8vq3ix2cw87k8snmbpbvj 
        foreign key (student_id) 
        references users;

    alter table answer_sections 
        add constraint FK_bp132bx914jv028eioexs8osp 
        foreign key (answer_sheet_id) 
        references answer_sheets;

    alter table answer_selections 
        add constraint FK_64jvw0qh9qoxps0oruq5c7ij1 
        foreign key (answer_id) 
        references answers;

    alter table answer_sheets 
        add constraint FK_mvn9boi1pfpkxwi4er0n7gbob 
        foreign key (author_id) 
        references users;

    alter table answer_sheets 
        add constraint FK_2a1atrd6dlc5w004spfdbw52l 
        foreign key (question_sheet_id) 
        references question_sheets;

    alter table answers 
        add constraint FK_a8hukb7yjph7wnrlnn8x0fcja 
        foreign key (question_id) 
        references questions;

    alter table answers 
        add constraint FK_d5u4r233tg8mpi1enp2gugx9s 
        foreign key (answer_section_id) 
        references answer_sections;

    alter table answers 
        add constraint FK_i8agbaeksrmm44c8l31gqdtnp 
        foreign key (attachment_id) 
        references files;

    alter table assignments 
        add constraint FK_3npt5rig34lv4oy0gystus2ed 
        foreign key (resource_id) 
        references resources;

    alter table assignments 
        add constraint FK_cyk799lr6903k9wpf33rwrxwi 
        foreign key (section_id) 
        references sections;

    alter table assignments 
        add constraint FK_7ed6gywfn7fll8aklgvqwxnlr 
        foreign key (question_sheet_id) 
        references question_sheets;

    alter table authorities 
        add constraint FK_s21btj9mlob1djhm3amivbe5e 
        foreign key (user_id) 
        references users;

    alter table course_journal_assignments 
        add constraint FK_eivqiu4y1n2ijqe4jqpgwcrua 
        foreign key (assignment_id) 
        references assignments;

    alter table course_journal_assignments 
        add constraint FK_7rciorn2b1tqbinh0qndw8rv1 
        foreign key (course_journal_id) 
        references course_journals;

    alter table course_journal_handouts 
        add constraint FK_i6x8y4el6rb082k6hsgjriuu2 
        foreign key (resource_id) 
        references resources;

    alter table course_journal_handouts 
        add constraint FK_6u818e0hvsqmd68kp3494ap6s 
        foreign key (course_journal_id) 
        references course_journals;

    alter table course_journal_student_samples 
        add constraint FK_c5jfng49990md8krgmybfinrh 
        foreign key (enrollment_id) 
        references enrollments;

    alter table course_journal_student_samples 
        add constraint FK_dofrwq6gyt6if5g22a0vyop7m 
        foreign key (course_journal_id) 
        references course_journals;

    alter table course_mapping_group1 
        add constraint FK_ieewf50yggd1sbmkxs09lvb4a 
        foreign key (course_id) 
        references courses;

    alter table course_mapping_group1 
        add constraint FK_p8gpo50bohrdmrw0dl65nd0h9 
        foreign key (mapping_id) 
        references course_mappings;

    alter table course_mapping_group2 
        add constraint FK_b1nwrxkdpr7d9u06vup6qnilk 
        foreign key (course_id) 
        references courses;

    alter table course_mapping_group2 
        add constraint FK_gcsxlaue65dlkbaib4dga3h7q 
        foreign key (mapping_id) 
        references course_mappings;

    alter table course_mappings 
        add constraint FK_5pd9d8eacjdm0ti185rem73fi 
        foreign key (department_id) 
        references departments;

    alter table course_substitutions 
        add constraint FK_4xv8mf3jidbdi508wufg15vv3 
        foreign key (advisement_record_id) 
        references advisement_records;

    alter table course_substitutions 
        add constraint FK_h4t8oqw9cnw62m8gnrk4t6qaj 
        foreign key (original_course_id) 
        references courses;

    alter table course_substitutions 
        add constraint FK_dj74ijbdy0o9lky297prr87co 
        foreign key (substitute_course_id) 
        references courses;

    alter table course_transfers 
        add constraint FK_qcduonuwdcffp1xdg5u4t8om 
        foreign key (advisement_record_id) 
        references advisement_records;

    alter table course_transfers 
        add constraint FK_h623do91lmlhqhdh13k5eu4kl 
        foreign key (course_id) 
        references courses;

    alter table course_waivers 
        add constraint FK_965lr47ip8vhailtkrukhvdbe 
        foreign key (advisement_record_id) 
        references advisement_records;

    alter table course_waivers 
        add constraint FK_cgb85a94p4eut3bofv5n3x1sw 
        foreign key (course_id) 
        references courses;

    alter table courses 
        add constraint FK_rpniainf0bnl5d9b1s5e03p1c 
        foreign key (coordinator_id) 
        references users;

    alter table courses 
        add constraint FK_i4cr87kpfjdhpko2ahmpcdv51 
        foreign key (department_id) 
        references departments;

    alter table courses 
        add constraint FK_4d959m04dfro4ed100rro7q0b 
        foreign key (description_id) 
        references files;

    alter table courses 
        add constraint FK_mncv6or01lin4xetkt2srxwhg 
        foreign key (journal_id) 
        references course_journals;

    alter table current_standings 
        add constraint FK_4a9ef8cucw6u69q55ovpnoxck 
        foreign key (academic_standing_id) 
        references academic_standings;

    alter table current_standings 
        add constraint FK_m35n4fufdi570uo8m7f4jahg3 
        foreign key (department_id) 
        references departments;

    alter table current_standings 
        add constraint FK_maww42e8vw3bnjxvtyc40ykf3 
        foreign key (student_id) 
        references users;

    alter table department_additional_graduate_courses 
        add constraint FK_l4glpwic362dn06uep6v0jmi1 
        foreign key (course_id) 
        references courses;

    alter table department_additional_graduate_courses 
        add constraint FK_qsobw9rio8lird5y527w5jred 
        foreign key (department_id) 
        references departments;

    alter table department_additional_undergraduate_courses 
        add constraint FK_eirvjog89lg0pmdlshyys2by4 
        foreign key (course_id) 
        references courses;

    alter table department_additional_undergraduate_courses 
        add constraint FK_663rvu3umig3mgqe9pw7ay30d 
        foreign key (department_id) 
        references departments;

    alter table department_administrators 
        add constraint FK_ltl2qejs6cjhrosfti55gn4gk 
        foreign key (user_id) 
        references users;

    alter table department_administrators 
        add constraint FK_rkx6fhm4yxlcbkdhgwgs4vh0d 
        foreign key (department_id) 
        references departments;

    alter table department_evaluators 
        add constraint FK_kphtmq4rhw14e68gmwkct4d94 
        foreign key (user_id) 
        references users;

    alter table department_evaluators 
        add constraint FK_qsn32vxy3bvasb2cdoodj7cjp 
        foreign key (department_id) 
        references departments;

    alter table department_faculty 
        add constraint FK_eki8cc049b0jx59uy1tqjwg4p 
        foreign key (user_id) 
        references users;

    alter table department_faculty 
        add constraint FK_bt7mbo0ikl88c9761dxxog4q1 
        foreign key (department_id) 
        references departments;

    alter table department_graduate_courses 
        add constraint FK_vfhirhe0keow4l5p5c2rmx03 
        foreign key (course_id) 
        references courses;

    alter table department_graduate_courses 
        add constraint FK_odj2pvq31jbcs9wv92wiflbgt 
        foreign key (department_id) 
        references departments;

    alter table department_instructors 
        add constraint FK_7d0xi3mi9cpamommq7uevak1r 
        foreign key (user_id) 
        references users;

    alter table department_instructors 
        add constraint FK_b5p67vw37665g60lavrnnxoig 
        foreign key (department_id) 
        references departments;

    alter table department_options 
        add constraint FK_lrqtbs9px96ynf5hk5p7jvljc 
        foreign key (department_id) 
        references departments;

    alter table department_reviewers 
        add constraint FK_3nhl201t5dxfwwvavo17ngajt 
        foreign key (user_id) 
        references users;

    alter table department_reviewers 
        add constraint FK_1q9bvp0tfl98ittof6c7g21y3 
        foreign key (department_id) 
        references departments;

    alter table department_undergraduate_courses 
        add constraint FK_17jsitihwbuwks72k25j7hyq7 
        foreign key (course_id) 
        references courses;

    alter table department_undergraduate_courses 
        add constraint FK_johybgn2xmnofelshl21rs34t 
        foreign key (department_id) 
        references departments;

    alter table enrollments 
        add constraint FK_gkiw9pcj7x5fbrevfc144ecnw 
        foreign key (grade_id) 
        references grades;

    alter table enrollments 
        add constraint FK_j3itp303earfuf3d1akees0xr 
        foreign key (section_id) 
        references sections;

    alter table enrollments 
        add constraint FK_qo458bnq40h7b9920ikhlfn0o 
        foreign key (student_id) 
        references users;

    alter table files 
        add constraint FK_5ok1awgnfwcf01537ylbycyq1 
        foreign key (owner_id) 
        references users;

    alter table files 
        add constraint FK_aretmtw1sqqw1pfkqdew9f7y7 
        foreign key (parent_id) 
        references files;

    alter table files 
        add constraint FK_74qy8ulabtqbbsbqq1iyq2la7 
        foreign key (reference_id) 
        references files;

    alter table files 
        add constraint FK_r3ilikiw4pt9owf91wedbh472 
        foreign key (submission_id) 
        references submissions;

    alter table forum_members 
        add constraint FK_pxnh2fvvk4hl7feh80hf9wj4g 
        foreign key (user_id) 
        references users;

    alter table forum_members 
        add constraint FK_k7pfx0npdsrweyedme6ta67h6 
        foreign key (forum_id) 
        references forums;

    alter table forum_moderators 
        add constraint FK_nqdympblfjedsdgvbbkkvirgs 
        foreign key (user_id) 
        references users;

    alter table forum_moderators 
        add constraint FK_o0oj0hi884lhla3dy4lg0ktg7 
        foreign key (forum_id) 
        references forums;

    alter table forum_post_attachments 
        add constraint FK_k39tpv1k9cv0npjqeu4d9b1lq 
        foreign key (file_id) 
        references files;

    alter table forum_post_attachments 
        add constraint FK_lj3a0cs8xvqiitwupsqg4tp8q 
        foreign key (post_id) 
        references forum_posts;

    alter table forum_posts 
        add constraint FK_k19s1k9dbmfvli8milvls4py0 
        foreign key (author_id) 
        references users;

    alter table forum_posts 
        add constraint FK_njr1la3q9de1ydgdicgrica6s 
        foreign key (edited_by) 
        references users;

    alter table forum_posts 
        add constraint FK_ddw9kw8owb7o3vnnsilye286w 
        foreign key (topic_id) 
        references forum_topics;

    alter table forum_topics 
        add constraint FK_ll68mar7cp0k7edic8nqjoy9f 
        foreign key (first_post_id) 
        references forum_posts;

    alter table forum_topics 
        add constraint FK_n26u7275hxneotd0dcj3siesd 
        foreign key (forum_id) 
        references forums;

    alter table forum_topics 
        add constraint FK_fhotqdk3icwdtfuf497y1jgj1 
        foreign key (last_post_id) 
        references forum_posts;

    alter table forums 
        add constraint FK_hq8kwpokg7x9sbmsr89lx4y6v 
        foreign key (course_id) 
        references courses;

    alter table forums 
        add constraint FK_fik5pbj65bh1x1rhdw010i4yl 
        foreign key (department_id) 
        references departments;

    alter table forums 
        add constraint FK_cnb8dm13flqpo2v206p3jj5qd 
        foreign key (last_post_id) 
        references forum_posts;

    alter table mailinglist_message_attachments 
        add constraint FK_526nr5va9rgd3lrc30psvxpao 
        foreign key (file_id) 
        references files;

    alter table mailinglist_message_attachments 
        add constraint FK_4pjsd9ta54umbvdr4uiwam2hf 
        foreign key (message_id) 
        references mailinglist_messages;

    alter table mailinglist_messages 
        add constraint FK_fubpkq16gtk4x038d2epoolv0 
        foreign key (author_id) 
        references users;

    alter table mailinglist_messages 
        add constraint FK_luvrjraoa9kj6jo0fu6i82a7d 
        foreign key (mailinglist_id) 
        references mailinglists;

    alter table mailinglists 
        add constraint FK_ht8ig93y9g103erqx1j0m2bpv 
        foreign key (department_id) 
        references departments;

    alter table mft_distribution_entries 
        add constraint FK_2p1l3s24er8uqyk797uxj6ivm 
        foreign key (distribution_id) 
        references mft_distributions;

    alter table mft_distribution_types 
        add constraint FK_cv2w5gaq2bawhkt4wdqeks02l 
        foreign key (department_id) 
        references departments;

    alter table mft_distributions 
        add constraint FK_spad6dusuiyup6bj450nw54hm 
        foreign key (type_id) 
        references mft_distribution_types;

    alter table mft_indicators 
        add constraint FK_nmqrjrlfkwchxodqvxd8xth0o 
        foreign key (department_id) 
        references departments;

    alter table mft_scores 
        add constraint FK_6pqva08sn8i7nrwhdqycml800 
        foreign key (department_id) 
        references departments;

    alter table mft_scores 
        add constraint FK_oqbfom3u0qmb8fl3t2778fgex 
        foreign key (user_id) 
        references users;

    alter table news 
        add constraint FK_5vey85hx7pjb3xkss6luve707 
        foreign key (department_id) 
        references departments;

    alter table news 
        add constraint FK_nc0r2fhq6ur10q2rl7u98pltu 
        foreign key (topic_id) 
        references forum_topics;

    alter table program_elective_courses 
        add constraint FK_lwbjswib5grlmipbyv6fpkvb0 
        foreign key (course_id) 
        references courses;

    alter table program_elective_courses 
        add constraint FK_bim7nogd19wvopkubelxbgaou 
        foreign key (program_id) 
        references programs;

    alter table program_required_courses 
        add constraint FK_iwqhq7lxoyofyq9kyfam8yjw9 
        foreign key (course_id) 
        references courses;

    alter table program_required_courses 
        add constraint FK_brn2eaxo1l4toidbcrh7sc9gx 
        foreign key (program_id) 
        references programs;

    alter table programs 
        add constraint FK_t38cee5jtiwtw07papp2rjlca 
        foreign key (department_id) 
        references departments;

    alter table project_advisors 
        add constraint FK_n0ig0bjo0vq6lv5r5kgvyiwsg 
        foreign key (advisor_id) 
        references users;

    alter table project_advisors 
        add constraint FK_hp6rohtsc7nlsh6t5wxhyrpur 
        foreign key (project_id) 
        references projects;

    alter table project_liaisons 
        add constraint FK_sxe2bc8jnmy4ckjb1t3pfkdj4 
        foreign key (liaison_id) 
        references users;

    alter table project_liaisons 
        add constraint FK_lxjadsx4jvo3rq2r3ip0c6fy 
        foreign key (project_id) 
        references projects;

    alter table project_resources 
        add constraint FK_n46lhiwx8a63b1986ktpqbny 
        foreign key (resource_id) 
        references resources;

    alter table project_resources 
        add constraint FK_9rntv4exxpu29v0uqyb3tljgr 
        foreign key (project_id) 
        references projects;

    alter table project_students 
        add constraint FK_ccwn8kyftaetasdut23vdrhm 
        foreign key (student_id) 
        references users;

    alter table project_students 
        add constraint FK_iqqlnv6t57rfws37o7t4b39f2 
        foreign key (project_id) 
        references projects;

    alter table projects 
        add constraint FK_tbu64x3h2rnwlju3cjsrji41k 
        foreign key (department_id) 
        references departments;

    alter table question_choices 
        add constraint FK_c8dvin7vnie1dpmintbvd9qlv 
        foreign key (question_id) 
        references questions;

    alter table question_correct_selections 
        add constraint FK_eh4055m11na1d63kid5e41g7d 
        foreign key (question_id) 
        references questions;

    alter table question_sections 
        add constraint FK_h8rhrmj2vsmob8yetgksocb5d 
        foreign key (question_sheet_id) 
        references question_sheets;

    alter table questions 
        add constraint FK_ff20ixg4nnw7wtwlxxfd0oa79 
        foreign key (question_section_id) 
        references question_sections;

    alter table resources 
        add constraint FK_tlofym1k7a9cqjjfhk5je1xy1 
        foreign key (file_id) 
        references files;

    alter table rubric_assignments 
        add constraint FK_be1rwpp5ioihw303osd5vui3h 
        foreign key (rubric_id) 
        references rubrics;

    alter table rubric_assignments 
        add constraint FK_5ejomot3box7435gah9aeilqc 
        foreign key (section_id) 
        references sections;

    alter table rubric_evaluation_ratings 
        add constraint FK_na7lb1h97n1rimokl7d1l0h3t 
        foreign key (evaluation_id) 
        references rubric_evaluations;

    alter table rubric_evaluations 
        add constraint FK_hdwl4qnes3hrqcqy79esc6dsk 
        foreign key (evaluator_id) 
        references users;

    alter table rubric_evaluations 
        add constraint FK_19rurndnkiq8yjw6iwm0e6e06 
        foreign key (submission_id) 
        references rubric_submissions;

    alter table rubric_external_evaluators 
        add constraint FK_2alg6e3do16tr4726j214f1vv 
        foreign key (evaluator_id) 
        references users;

    alter table rubric_external_evaluators 
        add constraint FK_rls3ttxbv8j6jmeuxlt2af96 
        foreign key (rubric_assignment_id) 
        references rubric_assignments;

    alter table rubric_indicator_criteria 
        add constraint FK_tdmi3a3igfd1cf8jeafu6j71s 
        foreign key (indicator_id) 
        references rubric_indicators;

    alter table rubric_indicators 
        add constraint FK_l3yyluj99ypim90j9b0tt4wt8 
        foreign key (rubric_id) 
        references rubrics;

    alter table rubric_submissions 
        add constraint FK_okqarri0252dqseefrsyanyly 
        foreign key (assignment_id) 
        references rubric_assignments;

    alter table rubric_submissions 
        add constraint FK_ivogp4ism8sw19yq3nakrak5l 
        foreign key (student_id) 
        references users;

    alter table rubrics 
        add constraint FK_64k2ai3eb5s5vky5ffj9b27bd 
        foreign key (creator_id) 
        references users;

    alter table rubrics 
        add constraint FK_9hma4j4gpkpjluxyq40rrvb48 
        foreign key (department_id) 
        references departments;

    alter table section_instructors 
        add constraint FK_bdnw2i9gn4dyut8n2m2o8is14 
        foreign key (instructor_id) 
        references users;

    alter table section_instructors 
        add constraint FK_6sy04nv0t0w18o9ar1shf4299 
        foreign key (section_id) 
        references sections;

    alter table sections 
        add constraint FK_j52kea1adexdgmvwldq0uluko 
        foreign key (course_id) 
        references courses;

    alter table sections 
        add constraint FK_2ei8itlc2p5gvjggaooay98tu 
        foreign key (journal_id) 
        references course_journals;

    alter table sections 
        add constraint FK_tb2sois1r297sjjopec8sntu4 
        foreign key (syllabus_id) 
        references resources;

    alter table site_announcements 
        add constraint FK_oj2vrsf4hwfjx943c14gvxpr6 
        foreign key (site_id) 
        references sites;

    alter table site_blocks 
        add constraint FK_8dena1stthxg1npvrqrgwf2pu 
        foreign key (site_id) 
        references sites;

    alter table site_class_info 
        add constraint FK_mwdxpihsngivuk1j751ttgo1m 
        foreign key (site_id) 
        references sites;

    alter table site_item_additional_resources 
        add constraint FK_2hsbsaek40kolelovkvfpx7br 
        foreign key (resource_id) 
        references resources;

    alter table site_item_additional_resources 
        add constraint FK_d47ahbwgrniftplj0ii3mmwss 
        foreign key (item_id) 
        references site_items;

    alter table site_items 
        add constraint FK_fu2v34mff8ajn5jjo9xfooym7 
        foreign key (resource_id) 
        references resources;

    alter table site_items 
        add constraint FK_ntpgyvgtuwsb5ja1g794ucy7j 
        foreign key (block_id) 
        references site_blocks;

    alter table sites 
        add constraint FK_cg1njlb2shxy3liugycy4onos 
        foreign key (folder_id) 
        references files;

    alter table sites 
        add constraint FK_lbvw5a0cnlm760o1e0ilsffys 
        foreign key (section_id) 
        references sections;

    alter table standing_mailinglists 
        add constraint FK_futm4ar6w96g0yxx6ml4v48tp 
        foreign key (standing_id) 
        references standings;

    alter table submissions 
        add constraint FK_tmp9uq9lu4lneyua21wexw0pt 
        foreign key (assignment_id) 
        references assignments;

    alter table submissions 
        add constraint FK_6l5xeon1g3op6nlntrr6d03ba 
        foreign key (student_id) 
        references users;

    alter table submissions 
        add constraint FK_hgewyd3vq7gvuflex6b20l7mx 
        foreign key (answer_sheet_id) 
        references answer_sheets;

    alter table subscriptions 
        add constraint FK_qhna8fdv16ljlwghm6vebsxdr 
        foreign key (subscriber_id) 
        references users;

    alter table survey_chart_points 
        add constraint FK_161bmbgp5qv8a2s3rn80ilp0q 
        foreign key (survey_id) 
        references surveys;

    alter table survey_chart_points 
        add constraint FK_80fj5bmp6qi7g0s7k29a1cxm2 
        foreign key (series_id) 
        references survey_chart_series;

    alter table survey_chart_series 
        add constraint FK_448xbia6kua6pfjxq383i0bf3 
        foreign key (chart_id) 
        references survey_charts;

    alter table survey_chart_xcoordinates 
        add constraint FK_ayhauh5bn5lc1duh2mmkkrj1x 
        foreign key (chart_id) 
        references survey_charts;

    alter table survey_charts 
        add constraint FK_bfpixd0u5re4rwu5p8cqe2kk9 
        foreign key (author_id) 
        references users;

    alter table survey_charts 
        add constraint FK_906egie9e2592l6hmj15nkbua 
        foreign key (department_id) 
        references departments;

    alter table survey_responses 
        add constraint FK_5yuaiow1kfy6r5p1ri01ls6mt 
        foreign key (answer_sheet_id) 
        references answer_sheets;

    alter table survey_responses 
        add constraint FK_rp3ncfv50xjtn41ur0lcuwhf3 
        foreign key (survey_id) 
        references surveys;

    alter table surveys 
        add constraint FK_pjyh94nbrlb6vfov5ubu9srrs 
        foreign key (author_id) 
        references users;

    alter table surveys 
        add constraint FK_fwfsqutlamulla3it2uwyvvcd 
        foreign key (department_id) 
        references departments;

    alter table surveys 
        add constraint FK_a0d19kmc1nmh63eku46mqkmx9 
        foreign key (question_sheet_id) 
        references question_sheets;

    alter table surveys_taken 
        add constraint FK_3j8vewipv34plcrivu17d7sox 
        foreign key (survey_id) 
        references surveys;

    alter table surveys_taken 
        add constraint FK_bu5cyb5by7ge6h0m8pergtx83 
        foreign key (user_id) 
        references users;

    alter table users 
        add constraint FK_q37jte7r1ptl16arimkk23y1h 
        foreign key (major_id) 
        references departments;

    alter table users 
        add constraint FK_c5mga1l7ury0q5lit8j53d43r 
        foreign key (original_picture_id) 
        references files;

    alter table users 
        add constraint FK_k0y369dffniq4d2myb2kbkw2h 
        foreign key (profile_picture_id) 
        references files;

    alter table users 
        add constraint FK_fx0l629tx63f2r40mn9qvxp3q 
        foreign key (profile_thumbnail_id) 
        references files;

    alter table users 
        add constraint FK_nup624f9nrfktc27evwuo1t0i 
        foreign key (program_id) 
        references programs;

    alter table wiki_discussions 
        add constraint FK_6yk6dpleuknebuqmajkjupxec 
        foreign key (topic_id) 
        references forum_topics;

    alter table wiki_discussions 
        add constraint FK_q3il29hs5gs3s7bl30yxmiaul 
        foreign key (page_id) 
        references wiki_pages;

    alter table wiki_pages 
        add constraint FK_t12rjajyw53wee2qno2iraicu 
        foreign key (owner_id) 
        references users;

    alter table wiki_revision_attachments 
        add constraint FK_dljtw6ugmm1n0ff0mwsams3 
        foreign key (file_id) 
        references files;

    alter table wiki_revision_attachments 
        add constraint FK_dwb8jvhu4b9btssbawsnd2npc 
        foreign key (revision_id) 
        references wiki_revisions;

    alter table wiki_revisions 
        add constraint FK_rth6cql0ncfo7rguoyrcpstbp 
        foreign key (author_id) 
        references users;

    alter table wiki_revisions 
        add constraint FK_a4xr0nwecmy9xy4nquvb2bitk 
        foreign key (page_id) 
        references wiki_pages;

    create sequence hibernate_sequence;
