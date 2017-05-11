create sequence hibernate_sequence start 1 increment 1;

    create table academic_standings (
       id int8 not null,
        term int4,
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

    create table assessment_program_objectives (
       id int8 not null,
        description varchar(255),
        objective_index int4 not null,
        text varchar(255),
        program_id int8,
        primary key (id)
    );

    create table assessment_program_outcomes (
       id int8 not null,
        description varchar(255),
        outcome_index int4 not null,
        text varchar(255),
        program_id int8,
        primary key (id)
    );

    create table assessment_program_resources (
       section_id int8 not null,
        resource_id int8 not null,
        resource_index int4 not null,
        primary key (section_id, resource_index)
    );

    create table assessment_program_sections (
       id int8 not null,
        hidden boolean not null,
        name varchar(255) not null,
        program_id int8,
        section_index int4,
        primary key (id)
    );

    create table assessment_programs (
       id int8 not null,
        deleted boolean not null,
        mission varchar(255),
        name varchar(255) not null,
        vision varchar(255),
        department_id int8 not null,
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

    create table attendance_events (
       id int8 not null,
        name varchar(255),
        primary key (id)
    );

    create table attendance_records (
       id int8 not null,
        attended boolean,
        event_id int8 not null,
        user_id int8 not null,
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

    create table course_journal_rubric_assignments (
       course_journal_id int8 not null,
        assignment_id int8 not null,
        assignment_order int4 not null,
        primary key (course_journal_id, assignment_order)
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

    create table course_prerequisites (
       course_id int8 not null,
        prerequisite_id int8 not null
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
        catalog_description varchar(255),
        code varchar(255) not null,
        name varchar(255) not null,
        obsolete boolean not null,
        unit_factor float8 not null,
        units int4 not null,
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

    create table groups (
       id int8 not null,
        date timestamp not null,
        description varchar(255),
        name varchar(255) not null,
        department_id int8,
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

    create table members (
       id int8 not null,
        date timestamp not null,
        group_id int8,
        user_id int8,
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

    create table personal_program_blocks (
       id int8 not null,
        program_block_id int8,
        program_id int8,
        block_index int4,
        primary key (id)
    );

    create table personal_program_entries (
       id int8 not null,
        prereq_met boolean not null,
        requirement_met boolean not null,
        course_id int8,
        enrollment_id int8,
        block_id int8,
        primary key (id)
    );

    create table personal_programs (
       id int8 not null,
        approve_date timestamp,
        date timestamp not null,
        approved_by int8,
        program_id int8,
        student_id int8,
        primary key (id)
    );

    create table prereg_schedule_registrations (
       id int8 not null,
        comments varchar(255),
        date timestamp not null,
        reg_limit int4 not null,
        schedule_id int8 not null,
        student_id int8 not null,
        primary key (id)
    );

    create table prereg_schedules (
       id int8 not null,
        default_grad_reg_limit int4 not null,
        default_section_capacity int4 not null,
        default_undergrad_reg_limit int4 not null,
        deleted boolean not null,
        description varchar(255),
        prereg_end timestamp,
        prereg_start timestamp,
        term int4 not null,
        department_id int8 not null,
        primary key (id)
    );

    create table prereg_section_registrations (
       id int8 not null,
        date timestamp not null,
        prereq_met boolean,
        schedule_registration_id int8,
        section_id int8 not null,
        student_id int8 not null,
        primary key (id)
    );

    create table prereg_sections (
       id int8 not null,
        capacity int4 not null,
        class_number varchar(255),
        days varchar(255),
        end_time varchar(255),
        location varchar(255),
        notes varchar(255),
        section_number int4 not null,
        start_time varchar(255),
        type varchar(255),
        course_id int8,
        linked_by int8,
        schedule_id int8,
        primary key (id)
    );

    create table program_block_courses (
       block_id int8 not null,
        course_id int8 not null
    );

    create table program_blocks (
       id int8 not null,
        description varchar(255),
        name varchar(255),
        require_all boolean not null,
        units_required int4,
        program_id int8,
        block_index int4,
        primary key (id)
    );

    create table programs (
       id int8 not null,
        description varchar(255),
        name varchar(255) not null,
        obsolete boolean not null,
        publish_date timestamp,
        department_id int8,
        published_by int8,
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
        private boolean not null,
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
        max_rating int4,
        min_rating int4,
        max_selections int4,
        min_selections int4,
        attachment_allowed boolean not null,
        correct_answer varchar(255),
        text_length int4,
        question_section_id int8,
        question_index int4,
        primary key (id)
    );

    create table resources (
       id int8 not null,
        deleted boolean not null,
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

    create table RubricEvaluationStats (
       id int8 not null,
        count int4,
        indicator int4,
        max float8,
        mean float8,
        median float8,
        min float8,
        type varchar(255),
        year int4,
        primary key (id)
    );

    create table rubrics (
       id int8 not null,
        deleted boolean not null,
        description varchar(255),
        public boolean not null,
        name varchar(255) not null,
        obsolete boolean not null,
        publish_date timestamp,
        scale int4 not null,
        creator_id int8,
        department_id int8,
        primary key (id)
    );

    create table section_attendance_events (
       section_id int8 not null,
        event_id int8 not null
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
        term int4 not null,
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
        resource_id int8 not null,
        resource_order int4 not null,
        primary key (item_id, resource_order)
    );

    create table site_items (
       id int8 not null,
        deleted boolean not null,
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
        subscribable_type varchar(255),
        subscribable_id int8,
        term int4,
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
        cell_phone varchar(255),
        cin varchar(255) not null,
        city varchar(255),
        disk_quota int4 not null,
        enabled boolean not null,
        first_name varchar(255) not null,
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
        personal_program_id int8,
        profile_picture_id int8,
        profile_thumbnail_id int8,
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

    create table WikiSearchResult (
       id int8 not null,
        content varchar(255),
        path varchar(255),
        subject varchar(255),
        primary key (id)
    );

    alter table academic_standings 
       add constraint UK7hsf5t3wd3nfdl4ckq4tgtjx unique (student_id, department_id, standing_id);

    alter table assignments 
       add constraint UK_7ed6gywfn7fll8aklgvqwxnlr unique (question_sheet_id);

    alter table attendance_records 
       add constraint UKd9cnf76j2ah0h6ww2pgi86391 unique (event_id, user_id);

    alter table course_journal_student_samples 
       add constraint UKp314skj0hft837qpu21d77dgj unique (course_journal_id, enrollment_id);

    alter table course_mapping_group1 
       add constraint UKiedqsk6j6wo8mmuv8u9vjld8 unique (mapping_id, course_id);

    alter table course_mapping_group2 
       add constraint UKgtwgoyu288y5wwjs8f1ofl1jl unique (mapping_id, course_id);

    alter table course_prerequisites 
       add constraint UKaookxavd3vufctfuway1cumrb unique (course_id, prerequisite_id);

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
       add constraint UKj1fpm330vse7gvfghxpg2n5tp unique (section_id, student_id);

    alter table forums 
       add constraint UK_hq8kwpokg7x9sbmsr89lx4y6v unique (course_id);

    alter table forums 
       add constraint UK_cnb8dm13flqpo2v206p3jj5qd unique (last_post_id);

    alter table grades 
       add constraint UK_2ljyc433n941undoa4gnv7ony unique (symbol);

    alter table members 
       add constraint UKc2tlx6det7nhu66mv4ktta8c3 unique (group_id, user_id);

    alter table mft_distribution_types 
       add constraint UKbkgmth8ayk24wvyx04uyxcfhh unique (department_id, alias);

    alter table mft_distributions 
       add constraint UKjugi4cmmvdf3ygjfwhrp4o8cn unique (year, type_id);

    alter table mft_indicators 
       add constraint UKs4t9k14a550c0f27x1cfwgq46 unique (department_id, date);

    alter table mft_scores 
       add constraint UKcxmltfnit7bi608roobfafund unique (department_id, user_id, date);

    alter table personal_programs 
       add constraint UK2nl0m35f8enfecae6h06cvvm4 unique (program_id, student_id);

    alter table prereg_schedule_registrations 
       add constraint UKe9guak95o4kyg9xvp7l1391yx unique (student_id, schedule_id);

    alter table prereg_section_registrations 
       add constraint UKlkjceiv9gr6675jc0xxegw56k unique (student_id, section_id);

    alter table sections 
       add constraint UK8lwvm8unsny9kwm5dtk81ssot unique (term, course_id, number);

    alter table sections 
       add constraint UK_2ei8itlc2p5gvjggaooay98tu unique (journal_id);

    alter table standings 
       add constraint UK_frgiry5vvnksrumybvsdws5l9 unique (symbol);

    alter table submissions 
       add constraint UKeiqoen8c565i0gq79ritryilw unique (student_id, assignment_id);

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
       add constraint FK69ga1opibasm8yit5275ucpge 
       foreign key (department_id) 
       references departments;

    alter table academic_standings 
       add constraint FKtc4jaj2fuj68qlxhkae057ix2 
       foreign key (standing_id) 
       references standings;

    alter table academic_standings 
       add constraint FK1g6yjrbeo0q38ew7xt7y7b00s 
       foreign key (student_id) 
       references users;

    alter table advisement_record_attachments 
       add constraint FKftg6oecxviruh653eijskud32 
       foreign key (file_id) 
       references files;

    alter table advisement_record_attachments 
       add constraint FKph9wo7c390lh3wbe6jv4c5u2v 
       foreign key (record_id) 
       references advisement_records;

    alter table advisement_records 
       add constraint FKbbrj2kbvbeu14s0arn927g48h 
       foreign key (advisor_id) 
       references users;

    alter table advisement_records 
       add constraint FKhkq0v6d2tiiirti67n3a6lsui 
       foreign key (student_id) 
       references users;

    alter table answer_sections 
       add constraint FKcqg5upj2xqktmw29kqkxk3tcv 
       foreign key (answer_sheet_id) 
       references answer_sheets;

    alter table answer_selections 
       add constraint FK4t112ykel3ldxv5j2j55q00wc 
       foreign key (answer_id) 
       references answers;

    alter table answer_sheets 
       add constraint FK7b5gqq7v00iqpxad4a6qm4jbw 
       foreign key (author_id) 
       references users;

    alter table answer_sheets 
       add constraint FK35vakscy40i68wd58jljowt2c 
       foreign key (question_sheet_id) 
       references question_sheets;

    alter table answers 
       add constraint FK3erw1a3t0r78st8ty27x6v3g1 
       foreign key (question_id) 
       references questions;

    alter table answers 
       add constraint FKioe3coe0r5jp1rij2nua3nrm0 
       foreign key (answer_section_id) 
       references answer_sections;

    alter table answers 
       add constraint FKli7oj7i60y1mgrforbq2otfgd 
       foreign key (attachment_id) 
       references files;

    alter table assessment_program_objectives 
       add constraint FKv4jcplnohiry495lxm7kaptk 
       foreign key (program_id) 
       references assessment_programs;

    alter table assessment_program_outcomes 
       add constraint FK64xismhokysu1lnth0hfbiacu 
       foreign key (program_id) 
       references assessment_programs;

    alter table assessment_program_resources 
       add constraint FKdaq0bgj299s0letlufqbtnksg 
       foreign key (resource_id) 
       references resources;

    alter table assessment_program_resources 
       add constraint FKdqv5v8lsf9gk4rqbnxf2b9f3v 
       foreign key (section_id) 
       references assessment_program_sections;

    alter table assessment_program_sections 
       add constraint FKshpgkm1vsh9jo7anwsfn6okw8 
       foreign key (program_id) 
       references assessment_programs;

    alter table assessment_programs 
       add constraint FKpsoppg730xcgc2qr3h8wvt9yv 
       foreign key (department_id) 
       references departments;

    alter table assignments 
       add constraint FKnfb3wln32j4w6ffmpd7pwh9uc 
       foreign key (resource_id) 
       references resources;

    alter table assignments 
       add constraint FKpwko74vv09l7mxyp13qm0ghwr 
       foreign key (section_id) 
       references sections;

    alter table assignments 
       add constraint FK57cnevoo47ifsyll3jyrdfs0f 
       foreign key (question_sheet_id) 
       references question_sheets;

    alter table attendance_records 
       add constraint FKkjyjsyqkmtcjuq691gok0jfmp 
       foreign key (event_id) 
       references attendance_events;

    alter table attendance_records 
       add constraint FK2yka8cp9l26e4kkyab5iyf3ef 
       foreign key (user_id) 
       references users;

    alter table authorities 
       add constraint FKk91upmbueyim93v469wj7b2qh 
       foreign key (user_id) 
       references users;

    alter table course_journal_assignments 
       add constraint FKgpsy1u8s5lppyao42vs2dap4y 
       foreign key (assignment_id) 
       references assignments;

    alter table course_journal_assignments 
       add constraint FK1sbaxgy58spkhei2d9sr20lv0 
       foreign key (course_journal_id) 
       references course_journals;

    alter table course_journal_handouts 
       add constraint FKf2iatyff7f9f45nm5vptdb0l1 
       foreign key (resource_id) 
       references resources;

    alter table course_journal_handouts 
       add constraint FKocvp0b07el7lwqranlk4dkqyn 
       foreign key (course_journal_id) 
       references course_journals;

    alter table course_journal_rubric_assignments 
       add constraint FK2xpjgpqolrpsbvslysq7c0k2s 
       foreign key (assignment_id) 
       references rubric_assignments;

    alter table course_journal_rubric_assignments 
       add constraint FKlnjmj5up2r1dnn047xjbk73mw 
       foreign key (course_journal_id) 
       references course_journals;

    alter table course_journal_student_samples 
       add constraint FK6toqqmi9wfxgt7v883nv6u24q 
       foreign key (enrollment_id) 
       references enrollments;

    alter table course_journal_student_samples 
       add constraint FK222brmqsndt6c9jhutr840bb0 
       foreign key (course_journal_id) 
       references course_journals;

    alter table course_mapping_group1 
       add constraint FKjt47ohl17wol46a54fskddf3q 
       foreign key (course_id) 
       references courses;

    alter table course_mapping_group1 
       add constraint FK28e5g0t9gmyck6g6gwwygd7x 
       foreign key (mapping_id) 
       references course_mappings;

    alter table course_mapping_group2 
       add constraint FKfvi4930mgevy5l40gfbffkmpq 
       foreign key (course_id) 
       references courses;

    alter table course_mapping_group2 
       add constraint FKhmiibwuoe3wcn93xycj44k9hp 
       foreign key (mapping_id) 
       references course_mappings;

    alter table course_mappings 
       add constraint FKjau4hbqwm7ox8qduhlsito1wf 
       foreign key (department_id) 
       references departments;

    alter table course_prerequisites 
       add constraint FK2w3n61668a1jqt1y4w7we9pn0 
       foreign key (prerequisite_id) 
       references courses;

    alter table course_prerequisites 
       add constraint FKhh4f1avebuvlv54m3j3l3pp36 
       foreign key (course_id) 
       references courses;

    alter table course_substitutions 
       add constraint FK7yrk1gr49b09s2bmihf6l7fe9 
       foreign key (advisement_record_id) 
       references advisement_records;

    alter table course_substitutions 
       add constraint FKj7kt69te4q34xtu8stcjt4sqb 
       foreign key (original_course_id) 
       references courses;

    alter table course_substitutions 
       add constraint FKcusw73bsc9leop00w17cv28qj 
       foreign key (substitute_course_id) 
       references courses;

    alter table course_transfers 
       add constraint FKqpdpfc2hg1phlmwh5pqd7el1j 
       foreign key (advisement_record_id) 
       references advisement_records;

    alter table course_transfers 
       add constraint FKsrwpg711nysdfnj49r90a0kc 
       foreign key (course_id) 
       references courses;

    alter table course_waivers 
       add constraint FKpq2h48buy2ky3miu2t1htoes6 
       foreign key (advisement_record_id) 
       references advisement_records;

    alter table course_waivers 
       add constraint FKbtus76vw7djcvfy7jphkk5gjg 
       foreign key (course_id) 
       references courses;

    alter table courses 
       add constraint FKfcn83cj7u481yf88ypnl5sdl5 
       foreign key (coordinator_id) 
       references users;

    alter table courses 
       add constraint FKsv2mdywju86wq12x4did4xd78 
       foreign key (department_id) 
       references departments;

    alter table courses 
       add constraint FK26kjysrjv6nr061yrx5yomlt8 
       foreign key (description_id) 
       references files;

    alter table courses 
       add constraint FK7v6d6b9eabblfotcrdwxbpt9s 
       foreign key (journal_id) 
       references course_journals;

    alter table current_standings 
       add constraint FKdg48wv9pxngma32i2odrg0wjh 
       foreign key (academic_standing_id) 
       references academic_standings;

    alter table current_standings 
       add constraint FK6q3yldacaqmjopiumt8ifamo7 
       foreign key (department_id) 
       references departments;

    alter table current_standings 
       add constraint FK5l1x1lo6pyw1xk1j22kyo2oox 
       foreign key (student_id) 
       references users;

    alter table department_administrators 
       add constraint FKtlytkm4dgi77g0n85thhh0cyk 
       foreign key (user_id) 
       references users;

    alter table department_administrators 
       add constraint FKr0obk6biqkqwml5gr4xu9jej1 
       foreign key (department_id) 
       references departments;

    alter table department_evaluators 
       add constraint FKmmg0ooqp3u2ek1xnq5jiyhdep 
       foreign key (user_id) 
       references users;

    alter table department_evaluators 
       add constraint FKh82vhruycblxgpl7lqbvbqe9k 
       foreign key (department_id) 
       references departments;

    alter table department_faculty 
       add constraint FKabgyfebnhnfxs49uyabwx38dl 
       foreign key (user_id) 
       references users;

    alter table department_faculty 
       add constraint FKbqyapnin5nea7epiqc2pq6ww3 
       foreign key (department_id) 
       references departments;

    alter table department_graduate_courses 
       add constraint FKrlisl25agd4jk7kceybac9x87 
       foreign key (course_id) 
       references courses;

    alter table department_graduate_courses 
       add constraint FK6r4gg8lyf5mnuw4265pltffjl 
       foreign key (department_id) 
       references departments;

    alter table department_instructors 
       add constraint FKikwj7ql1e9aw53tc621pp6ui0 
       foreign key (user_id) 
       references users;

    alter table department_instructors 
       add constraint FK6bcahb6o1lt0fmy6xncvll7ab 
       foreign key (department_id) 
       references departments;

    alter table department_options 
       add constraint FKtrtoj0d4pp7ivjrbxeae3u3yx 
       foreign key (department_id) 
       references departments;

    alter table department_reviewers 
       add constraint FKc3dwcvwoi7c06156fumv84sm1 
       foreign key (user_id) 
       references users;

    alter table department_reviewers 
       add constraint FKomnxju96t6p00b6bvdtq3hgcp 
       foreign key (department_id) 
       references departments;

    alter table department_undergraduate_courses 
       add constraint FKlaxexmx0rab3n3yvhgmuk8sqx 
       foreign key (course_id) 
       references courses;

    alter table department_undergraduate_courses 
       add constraint FKkg73b5ifb43jknjyyh46qf0fn 
       foreign key (department_id) 
       references departments;

    alter table enrollments 
       add constraint FKube0t8hvfshln29fsjjhvs0v 
       foreign key (grade_id) 
       references grades;

    alter table enrollments 
       add constraint FKh00337cnw2p14fuan7x3rwye6 
       foreign key (section_id) 
       references sections;

    alter table enrollments 
       add constraint FK2lha5vwilci2yi3vu5akusx4a 
       foreign key (student_id) 
       references users;

    alter table files 
       add constraint FKndbd0r86rsovslrthjrgl960x 
       foreign key (owner_id) 
       references users;

    alter table files 
       add constraint FKod0go0ye9ao1yql0nj9hs0k78 
       foreign key (parent_id) 
       references files;

    alter table files 
       add constraint FK1swftf2ytn4p2sorofxkrq0ym 
       foreign key (reference_id) 
       references files;

    alter table files 
       add constraint FKi18cabl1gtnopj7jkaq016xg2 
       foreign key (submission_id) 
       references submissions;

    alter table forum_members 
       add constraint FKv2vmsnb03rv96n7nmxuyrja4 
       foreign key (user_id) 
       references users;

    alter table forum_members 
       add constraint FKb6uoklgxr7hcnvdf2emvmsi2i 
       foreign key (forum_id) 
       references forums;

    alter table forum_moderators 
       add constraint FK7nukbmkpglvxtsu1xlwc8e7y2 
       foreign key (user_id) 
       references users;

    alter table forum_moderators 
       add constraint FK982wd5y74b0wvdlrptbhtohsa 
       foreign key (forum_id) 
       references forums;

    alter table forum_post_attachments 
       add constraint FKat2q6fykr1m3o874a96e85ihc 
       foreign key (file_id) 
       references files;

    alter table forum_post_attachments 
       add constraint FKnnjviktyv823q8uegb4t8ijyk 
       foreign key (post_id) 
       references forum_posts;

    alter table forum_posts 
       add constraint FKpsovhjndrj3obmt1d0p9nhr0u 
       foreign key (author_id) 
       references users;

    alter table forum_posts 
       add constraint FKqwd2ih7xecdl7rh7sqkb10gc8 
       foreign key (edited_by) 
       references users;

    alter table forum_posts 
       add constraint FKhqu825o4c26ejsdbl1cwrsgxs 
       foreign key (topic_id) 
       references forum_topics;

    alter table forum_topics 
       add constraint FK38xd3jr9halhedj4xbt8tj115 
       foreign key (first_post_id) 
       references forum_posts;

    alter table forum_topics 
       add constraint FKcjwe9mtahunrybw03swdcxd61 
       foreign key (forum_id) 
       references forums;

    alter table forum_topics 
       add constraint FK4hpcj9symcugjwlt9atvaxosm 
       foreign key (last_post_id) 
       references forum_posts;

    alter table forums 
       add constraint FK272tq03u1urnl7ildohwcv6cx 
       foreign key (course_id) 
       references courses;

    alter table forums 
       add constraint FK7440kx415fwlpqu50k3juxxdv 
       foreign key (department_id) 
       references departments;

    alter table forums 
       add constraint FKn7gchlgbj91rpl5gvfuy5t9y2 
       foreign key (last_post_id) 
       references forum_posts;

    alter table groups 
       add constraint FKqh3pnyt97wuwjuqmu010d96ma 
       foreign key (department_id) 
       references departments;

    alter table mailinglist_message_attachments 
       add constraint FKkuyhly7fkj24swagbqq7fhelh 
       foreign key (file_id) 
       references files;

    alter table mailinglist_message_attachments 
       add constraint FKrai0spy3qyry9dv722i0vvy02 
       foreign key (message_id) 
       references mailinglist_messages;

    alter table mailinglist_messages 
       add constraint FKln4ia8mtcohiy4sw92nql365i 
       foreign key (author_id) 
       references users;

    alter table mailinglist_messages 
       add constraint FKjctkvlph90qjcujhipla8xf6l 
       foreign key (mailinglist_id) 
       references mailinglists;

    alter table mailinglists 
       add constraint FKpippeslrcwctd4hoqrdd2aef 
       foreign key (department_id) 
       references departments;

    alter table members 
       add constraint FK1jmeir47b7qcn2sd5m4txgfuw 
       foreign key (group_id) 
       references groups;

    alter table members 
       add constraint FKpj3n6wh5muoeakc485whgs3x5 
       foreign key (user_id) 
       references users;

    alter table mft_distribution_entries 
       add constraint FK5nwtiuc3f46jspl1dueh5d8ld 
       foreign key (distribution_id) 
       references mft_distributions;

    alter table mft_distribution_types 
       add constraint FK6gbt5rxxyli0mdmjcxf2man7r 
       foreign key (department_id) 
       references departments;

    alter table mft_distributions 
       add constraint FK45rkv56riwgjxfhr4r8lp7nin 
       foreign key (type_id) 
       references mft_distribution_types;

    alter table mft_indicators 
       add constraint FK33eskdem5xm6enh0qo1nlxgvd 
       foreign key (department_id) 
       references departments;

    alter table mft_scores 
       add constraint FKbidvqxukuo1vdck3b3g37v494 
       foreign key (department_id) 
       references departments;

    alter table mft_scores 
       add constraint FK4htayrydxb0ntvmxxfy1ndk9t 
       foreign key (user_id) 
       references users;

    alter table news 
       add constraint FKhmgc1xll6fgnddaxe2njm44j9 
       foreign key (department_id) 
       references departments;

    alter table news 
       add constraint FKkgtj0ajt1hio2iivq7ci2sakl 
       foreign key (topic_id) 
       references forum_topics;

    alter table personal_program_blocks 
       add constraint FKbygxgxylutgc5jwnikn8xfuyl 
       foreign key (program_block_id) 
       references program_blocks;

    alter table personal_program_blocks 
       add constraint FK30q9qyn8wkolgd7yej6dwwghs 
       foreign key (program_id) 
       references personal_programs;

    alter table personal_program_entries 
       add constraint FKsysrrkugmyjve6nussbql35bc 
       foreign key (course_id) 
       references courses;

    alter table personal_program_entries 
       add constraint FKhgdby36jf3mfdflm5qb6k3x2d 
       foreign key (enrollment_id) 
       references enrollments;

    alter table personal_program_entries 
       add constraint FKm9rs4748jb4qxjdx6xmpqe6jr 
       foreign key (block_id) 
       references personal_program_blocks;

    alter table personal_programs 
       add constraint FKk03jhtcdp7utd8jub1xjmpacc 
       foreign key (approved_by) 
       references users;

    alter table personal_programs 
       add constraint FKh8em09oudpn1vtmfcm9xbnvwc 
       foreign key (program_id) 
       references programs;

    alter table personal_programs 
       add constraint FK78sw9n22if9iibuyx7kqudjip 
       foreign key (student_id) 
       references users;

    alter table prereg_schedule_registrations 
       add constraint FKllx059j11jemmap6dlhvoq6wy 
       foreign key (schedule_id) 
       references prereg_schedules;

    alter table prereg_schedule_registrations 
       add constraint FKrn1c5jej2js2fganwbqq1599k 
       foreign key (student_id) 
       references users;

    alter table prereg_schedules 
       add constraint FK4dkuyqnia305pqga0vievxr9b 
       foreign key (department_id) 
       references departments;

    alter table prereg_section_registrations 
       add constraint FKsp22af2lujkh6et5g38a1smxp 
       foreign key (schedule_registration_id) 
       references prereg_schedule_registrations;

    alter table prereg_section_registrations 
       add constraint FK7bkjqbi60htxt04mvq26esmyn 
       foreign key (section_id) 
       references prereg_sections;

    alter table prereg_section_registrations 
       add constraint FKcg42frwf4xfp88jvbpvd6cg6q 
       foreign key (student_id) 
       references users;

    alter table prereg_sections 
       add constraint FKi988e2dgfpfk45yubpywh3jmi 
       foreign key (course_id) 
       references courses;

    alter table prereg_sections 
       add constraint FKt9h4k0ndaudqtvl93k36vqcko 
       foreign key (linked_by) 
       references prereg_sections;

    alter table prereg_sections 
       add constraint FKmawmgenb1ua7u6kyxucjfinxs 
       foreign key (schedule_id) 
       references prereg_schedules;

    alter table program_block_courses 
       add constraint FK9695vxyaeejmel1y3vv7bpydj 
       foreign key (course_id) 
       references courses;

    alter table program_block_courses 
       add constraint FKagkbpnfr2g9qt31jm5snqg11l 
       foreign key (block_id) 
       references program_blocks;

    alter table program_blocks 
       add constraint FK6ca55f8kguyejb7lgdwrakg4y 
       foreign key (program_id) 
       references programs;

    alter table programs 
       add constraint FK7xrusj91mbbujeaxtrrdowj7e 
       foreign key (department_id) 
       references departments;

    alter table programs 
       add constraint FK507rq74eqb0jmn6i9ugsu6uy2 
       foreign key (published_by) 
       references users;

    alter table project_advisors 
       add constraint FK9ga66lqyxwk3n0yu9pe6n7dm0 
       foreign key (advisor_id) 
       references users;

    alter table project_advisors 
       add constraint FKi10r9ulwhy6cwvua3o7fn0ccb 
       foreign key (project_id) 
       references projects;

    alter table project_liaisons 
       add constraint FKrwl1237ng07fagb5krxb2vsak 
       foreign key (liaison_id) 
       references users;

    alter table project_liaisons 
       add constraint FKmpw28v2an7t4dhtjlm68w7s4l 
       foreign key (project_id) 
       references projects;

    alter table project_resources 
       add constraint FKc9wwd3eeywoj9yi0kif5d6gy6 
       foreign key (resource_id) 
       references resources;

    alter table project_resources 
       add constraint FK1ah885txq30jh1uy20akeytps 
       foreign key (project_id) 
       references projects;

    alter table project_students 
       add constraint FKtbm133nxu8hsnom18kfnys9yv 
       foreign key (student_id) 
       references users;

    alter table project_students 
       add constraint FKafjigtoxvs089c1u1dxnn4jmw 
       foreign key (project_id) 
       references projects;

    alter table projects 
       add constraint FKk7yeevqj2y4etn45lq8ilgxdh 
       foreign key (department_id) 
       references departments;

    alter table question_choices 
       add constraint FK77biojwg2xd8kc8a2odnx3ld4 
       foreign key (question_id) 
       references questions;

    alter table question_correct_selections 
       add constraint FKd2tcrfc6ialvmvr44iu9727cd 
       foreign key (question_id) 
       references questions;

    alter table question_sections 
       add constraint FK2rlftenqtbxyqhcv29m4ni9c0 
       foreign key (question_sheet_id) 
       references question_sheets;

    alter table questions 
       add constraint FKj18rt6hryo675u78v2dwe89ir 
       foreign key (question_section_id) 
       references question_sections;

    alter table resources 
       add constraint FKi1ojeclg79ha0fqng72d0ql4i 
       foreign key (file_id) 
       references files;

    alter table rubric_assignments 
       add constraint FKi1pdm5ajau8x5ts6bph5n0yge 
       foreign key (rubric_id) 
       references rubrics;

    alter table rubric_assignments 
       add constraint FKisoeydldsqx3kj208sed3heft 
       foreign key (section_id) 
       references sections;

    alter table rubric_evaluation_ratings 
       add constraint FKrq7757pel1j60wsybip93ue65 
       foreign key (evaluation_id) 
       references rubric_evaluations;

    alter table rubric_evaluations 
       add constraint FK9giivdwhme78nsi10jsgen5dt 
       foreign key (evaluator_id) 
       references users;

    alter table rubric_evaluations 
       add constraint FKcxlrpkjqjwv3i3g3hmvisq5sa 
       foreign key (submission_id) 
       references rubric_submissions;

    alter table rubric_external_evaluators 
       add constraint FKtqyrcwaodlheaa2nols9x7ljv 
       foreign key (evaluator_id) 
       references users;

    alter table rubric_external_evaluators 
       add constraint FKh0dwcirgh3mcegenpj5sjqhh0 
       foreign key (rubric_assignment_id) 
       references rubric_assignments;

    alter table rubric_indicator_criteria 
       add constraint FK4795d1dum6fw6p6xknov440wn 
       foreign key (indicator_id) 
       references rubric_indicators;

    alter table rubric_indicators 
       add constraint FKd7s959h5ytu65u7h1iq84l65i 
       foreign key (rubric_id) 
       references rubrics;

    alter table rubric_submissions 
       add constraint FKhou4jjkrtstqhtlehmr4hm5yw 
       foreign key (assignment_id) 
       references rubric_assignments;

    alter table rubric_submissions 
       add constraint FK6xlypr4gf91159bsho1conkuy 
       foreign key (student_id) 
       references users;

    alter table rubrics 
       add constraint FK2w3xneoptjuj9tdmjbmt09rr6 
       foreign key (creator_id) 
       references users;

    alter table rubrics 
       add constraint FK8heestm650nky9sopuvjah4d2 
       foreign key (department_id) 
       references departments;

    alter table section_attendance_events 
       add constraint FK8t96g47168ldhb2fp4teb3093 
       foreign key (event_id) 
       references attendance_events;

    alter table section_attendance_events 
       add constraint FK77mi94h7wdwv652n3saxs1s96 
       foreign key (section_id) 
       references sections;

    alter table section_instructors 
       add constraint FKoc121kt6hryx70211g9pkskry 
       foreign key (instructor_id) 
       references users;

    alter table section_instructors 
       add constraint FK6owpb5rosx5k2rd44vduqot5l 
       foreign key (section_id) 
       references sections;

    alter table sections 
       add constraint FK7ty9cevpq04d90ohtso1q8312 
       foreign key (course_id) 
       references courses;

    alter table sections 
       add constraint FKqsn967am88wfuwpr062vyw64j 
       foreign key (journal_id) 
       references course_journals;

    alter table sections 
       add constraint FK4swbkgwachrsb4784nddjfc3w 
       foreign key (syllabus_id) 
       references resources;

    alter table site_announcements 
       add constraint FKon4qtdgo2sss5f834xe0pk605 
       foreign key (site_id) 
       references sites;

    alter table site_blocks 
       add constraint FKsltsqb5mp6hvucd7giylibmh1 
       foreign key (site_id) 
       references sites;

    alter table site_class_info 
       add constraint FKqwbtxravky88c2p4cfg9e1osf 
       foreign key (site_id) 
       references sites;

    alter table site_item_additional_resources 
       add constraint FKlkl1eqs62m6cy34od7awfwfqx 
       foreign key (resource_id) 
       references resources;

    alter table site_item_additional_resources 
       add constraint FKt5er83c2pnko75j5qrt79vjtt 
       foreign key (item_id) 
       references site_items;

    alter table site_items 
       add constraint FKj8bs0rxrc14tehuc0wen3pn6b 
       foreign key (resource_id) 
       references resources;

    alter table site_items 
       add constraint FKkctob4gigbb6l1jk3i24300e8 
       foreign key (block_id) 
       references site_blocks;

    alter table sites 
       add constraint FKbl1nhmjs2t4pil7u88l879shj 
       foreign key (folder_id) 
       references files;

    alter table sites 
       add constraint FK6jyuvih8sd0n2ecxc68qtfx24 
       foreign key (section_id) 
       references sections;

    alter table standing_mailinglists 
       add constraint FK70p8q6e70k7k2eol4mkhm1tfh 
       foreign key (standing_id) 
       references standings;

    alter table submissions 
       add constraint FKrirbb44savy2g7nws0hoxs949 
       foreign key (assignment_id) 
       references assignments;

    alter table submissions 
       add constraint FK3p6y8mnhpwusdgqrdl4hcl72m 
       foreign key (student_id) 
       references users;

    alter table submissions 
       add constraint FKaj0192ybly40w6pinovfswkdp 
       foreign key (answer_sheet_id) 
       references answer_sheets;

    alter table subscriptions 
       add constraint FKoodc4352epkjrvxx79odlxbji 
       foreign key (subscriber_id) 
       references users;

    alter table survey_chart_points 
       add constraint FKd5rgyfvaqm7k334rklvi6m97x 
       foreign key (survey_id) 
       references surveys;

    alter table survey_chart_points 
       add constraint FKgndbf4hsamvr6jew9hcd9tjbw 
       foreign key (series_id) 
       references survey_chart_series;

    alter table survey_chart_series 
       add constraint FKq8h4ebpnicvry7sowovadisri 
       foreign key (chart_id) 
       references survey_charts;

    alter table survey_chart_xcoordinates 
       add constraint FK2c7k4iwep6eqi5t8kglo4v6qw 
       foreign key (chart_id) 
       references survey_charts;

    alter table survey_charts 
       add constraint FKhspgmrftp9x7g439iux8y24c8 
       foreign key (author_id) 
       references users;

    alter table survey_charts 
       add constraint FKdubdtt8vi0k991srt5clpiawv 
       foreign key (department_id) 
       references departments;

    alter table survey_responses 
       add constraint FKkw619ewjh9gnxw01rman175fg 
       foreign key (answer_sheet_id) 
       references answer_sheets;

    alter table survey_responses 
       add constraint FKg5uul15qxrqgdet2fa3wvdhj8 
       foreign key (survey_id) 
       references surveys;

    alter table surveys 
       add constraint FKbp1og7ce6n9kud0tp5skxktil 
       foreign key (author_id) 
       references users;

    alter table surveys 
       add constraint FKhsam66mhp6ateuiywyljs3fcb 
       foreign key (department_id) 
       references departments;

    alter table surveys 
       add constraint FK3gsj9bh6cnj5b0xp352hue0dg 
       foreign key (question_sheet_id) 
       references question_sheets;

    alter table surveys_taken 
       add constraint FKrc6t3t53x5vp8yiim36ge9fsj 
       foreign key (survey_id) 
       references surveys;

    alter table surveys_taken 
       add constraint FK2jxphnvw5mlt6h6oemt444k6k 
       foreign key (user_id) 
       references users;

    alter table users 
       add constraint FKi76eki451lvygmph9k4uif3f1 
       foreign key (major_id) 
       references departments;

    alter table users 
       add constraint FK12d3pvm70ccdvjfmtgd3e4dly 
       foreign key (original_picture_id) 
       references files;

    alter table users 
       add constraint FKpb5rrytf665yfcum2mhy4jk3q 
       foreign key (personal_program_id) 
       references personal_programs;

    alter table users 
       add constraint FKnxpsvtwa6bes7prvq0ka1wad3 
       foreign key (profile_picture_id) 
       references files;

    alter table users 
       add constraint FKjahtku3dhehbor3ftkvakjx69 
       foreign key (profile_thumbnail_id) 
       references files;

    alter table wiki_discussions 
       add constraint FKg2p0ylrxp768owh4gw07sblpq 
       foreign key (topic_id) 
       references forum_topics;

    alter table wiki_discussions 
       add constraint FKir7l1fnjbqbajgyrqddyv4bx8 
       foreign key (page_id) 
       references wiki_pages;

    alter table wiki_pages 
       add constraint FK6p0mss8c6n51h3fnwq32d7lsc 
       foreign key (owner_id) 
       references users;

    alter table wiki_revision_attachments 
       add constraint FKbkonbh3djg9ge8va2rd7v7sh9 
       foreign key (file_id) 
       references files;

    alter table wiki_revision_attachments 
       add constraint FK87fqi360hpbeh6f0bh1mr8whd 
       foreign key (revision_id) 
       references wiki_revisions;

    alter table wiki_revisions 
       add constraint FKfvse1f6451jcwhn8u314wlgs8 
       foreign key (author_id) 
       references users;

    alter table wiki_revisions 
       add constraint FKkvsayk9makwu6qtxk4xptdp0s 
       foreign key (page_id) 
       references wiki_pages;
