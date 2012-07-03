
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
        primary key (id)
    );

    create table users (
        id int8 not null,
        birthday date,
        cell_phone varchar(255),
        cin varchar(255) not null unique,
        cin_encrypted boolean not null,
        city varchar(255),
        enabled boolean not null,
        expired boolean not null,
        first_name varchar(255) not null,
        gender varchar(255),
        home_phone varchar(255),
        last_name varchar(255) not null,
        middle_name varchar(255),
        office_phone varchar(255),
        password varchar(255) not null,
        primary_email varchar(255) not null unique,
        secondary_email varchar(255),
        state varchar(255),
        street varchar(255),
        username varchar(255) not null unique,
        zip varchar(255),
        primary key (id)
    );

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

    alter table files 
        add constraint FK5CEBA7767E6511D 
        foreign key (parent_id) 
        references files;

    alter table files 
        add constraint FK5CEBA774FA834C3 
        foreign key (owner_id) 
        references users;

    create sequence hibernate_sequence;
