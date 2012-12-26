set client_min_messages=WARNING;

drop sequence log_sequence;
drop table generated_cins;

alter table users alter id type bigint;
alter table users alter enabled set default 't';
alter table users drop column date_created;
alter table users rename email to primary_email;
alter table users rename email2 to secondary_email;
update users set address1 = address1 || ' ' || address2
    where char_length(address2) > 0;
alter table users drop column address2;
alter table users rename address1 to street;
alter table users rename phone to home_phone;
alter table users rename phone2 to work_phone;
alter table users add column cell_phone varchar(255);
alter table users drop column cin_encrypted;
alter table users drop column primary_role;
alter table users drop column show_favorite_forums;
alter table users alter column disk_quota set default 200;
alter table users add column temporary boolean not null default 'f';
update users set temporary = 't' where id in (select user_id
    from authorities where role_id = 16);

-- At this point users has three additional columns:
--  mft_score, mft_date, and current_standing_id

drop table authorities;
drop table roles;

create table authorities (
    user_id bigint not null references users(id),
    role    varchar(255)
);

insert into users (id, cin, username, password, last_name, first_name,
    primary_email) values
    (100, '100', 'sysadmin', md5('abcd'), 'System', 'Administrator',
    'csnsadmin@localhost.localdomain');

insert into authorities (user_id, role) values (100, 'ROLE_ADMIN');
insert into authorities (user_id, role) values (1000, 'DEPT_ROLE_ADMIN_cs');

create table persistent_logins (
    series      varchar(64) primary key,
    username    varchar(64) not null,
    token       varchar(64) not null,
    last_used   timestamp not null
);

alter table files alter id type bigint;
alter table files drop column version;
alter table files alter date set default current_timestamp;
alter table files alter owner_id type bigint;
alter table files alter parent_id type bigint;
alter table files add column submission_id bigint references submissions(id);
update files f set submission_id = s.submission_id
    from submission_files s
    where f.id = s.file_id;
alter table files drop column section_id;
drop table submission_files;

-- XXX There are some inconsistencies wrt the section_id
-- field in files. It only involve a few files so
-- ultimately it probably doesn't matter. Just in
-- case we need to manaully fix them, the records are
-- in file_section_ids.txt.

create table resources (
    id      bigint primary key,
    name    varchar(255),
    type    integer not null,
    text    text,
    file_id bigint references files(id),
    url     varchar(2000)
);

alter table subscriptions alter id type bigint;
alter table subscriptions drop column subscription_type;
alter table subscriptions add column subscribable_type char(2);
update subscriptions s set subscribable_type = 'FM'
    where exists (select id from forums where id = s.subscribable_id);
update subscriptions s set subscribable_type = 'FT'
    where exists (select id from forum_topics where id = s.subscribable_id);
update subscriptions s set subscribable_type = 'ML'
    where exists (select id from mailinglists where id = s.subscribable_id);
update subscriptions s set subscribable_type = 'WP'
    where exists (select id from wiki_pages where id = s.subscribable_id);
alter table subscriptions alter subscribable_type set not null;
alter table subscriptions alter subscribable_id type bigint;
alter table subscriptions alter subscriber_id type bigint;
alter table subscriptions add column quarter integer;
alter table subscriptions add column notification_sent boolean not null default 'f';
alter table subscriptions add column auto_subscribed boolean not null default 'f';
alter table subscriptions drop column status;
alter table subscriptions drop constraint subscriptions_subscribable_id_key;
alter table subscriptions add unique(subscribable_type, subscribable_id, subscriber_id);
delete from subscriptions where subscribable_type = 'FM'
    and subscribable_id in (select id from forums where course_id is not null);

alter table question_sections alter id type bigint;
alter table question_sections alter question_sheet_id type bigint;
alter table question_sections alter description type varchar(8000);
alter table questions alter id type bigint;
alter table questions alter description type varchar(8000);
alter table questions alter point_value set default 1;
update questions set point_value = 1 where point_value is null;
alter table questions alter point_value set not null;
alter table questions alter correct_answer type varchar(8000);
alter table questions drop column container_id;
alter table question_choices alter question_id type bigint;
alter table question_choices alter choice type varchar(4000);
alter table question_correct_selections alter question_id type bigint;

alter table answer_sheets alter id type bigint;
alter table answer_sheets alter question_sheet_id type bigint;
alter table answer_sheets alter respondent_id type bigint;
alter table answer_sheets rename respondent_id to author_id;
alter table answer_sheets rename timestamp to date;
alter table answer_sections alter id type bigint;
alter table answer_sections alter answer_sheet_id type bigint;
alter table answers alter id type bigint;
alter table answers alter answer_section_id type bigint;
alter table answers alter question_id type bigint;
alter table answers alter text type text;
alter table answers drop column container_id;
alter table answers alter attachment type bigint;
alter table answers rename attachment to attachment_id;
alter table answer_selections alter answer_id type bigint;

alter table grades alter id type bigint;
alter table grades alter value type double precision;
alter table grades alter description type varchar(1000);

alter table courses alter id type bigint;
alter table courses add unique(code);
alter table courses rename units to min_units;
alter table courses add column max_units integer not null default 4;
update courses set max_units = min_units;
alter table courses alter coordinator_id type bigint;
alter table courses rename description to syllabus_id;
alter table courses alter syllabus_id type bigint;
alter table courses drop column is_graduate_course;

-- XXX leave auto_create_assignments and journal_id there for now

alter table courses add column tsv tsvector;

create function courses_ts_trigger_function() returns trigger as $$
begin
    new.tsv := setweight(to_tsvector(new.code), 'A') ||
               setweight(to_tsvector(new.name), 'B');
    return new;
end
$$ language plpgsql;

create trigger courses_ts_trigger
    before insert or update on courses
    for each row execute procedure courses_ts_trigger_function();

create index courses_ts_index on courses using gin(tsv);

alter table sections alter id type bigint;
alter table sections alter course_id type bigint;
alter table instructors rename to section_instructors;
alter table section_instructors alter section_id type bigint;
alter table section_instructors alter instructor_id type bigint;
alter table enrollments alter id type bigint;
alter table enrollments alter section_id type bigint;
alter table enrollments alter student_id type bigint;
alter table enrollments rename grade to grade_id;
alter table enrollments alter grade_id type bigint;

alter table assignments alter id type bigint;
alter table assignments add column resource_id bigint references resources(id);
alter table assignments rename short_name to alias;
alter table assignments alter section_id type bigint;
alter table assignments add column publish_date timestamp;
update assignments a set publish_date = q.publish_date from question_sheets q
    where a.question_sheet_id = q.id;
update assignments a set publish_date = due_date - interval '1 week'
    where publish_date is null and due_date is not null
    and due_date < current_timestamp;
alter table assignments rename allowed_max_file_size to max_file_size;
alter table assignments rename allowed_file_extensions to file_extensions;
alter table assignments rename viewable_after_close to available_after_due_date;
alter table assignments alter question_sheet_id type bigint;
alter table assignments add unique(question_sheet_id);

alter table assignments add column tsv tsvector;

create function assignments_ts_trigger_function() returns trigger as $$
declare
    l_quarter       varchar;
    l_course_code   varchar;
begin
    if new.section_id is not null then
        select quarter(quarter) into l_quarter from sections
            where id = new.section_id;
        select c.code into l_course_code from sections s, courses c
            where s.id = new.section_id and c.id = s.course_id;
    end if;
    new.tsv := setweight(to_tsvector(l_quarter), 'A') ||
               setweight(to_tsvector(l_course_code), 'A') ||
               setweight(to_tsvector(new.name), 'A');
    return new;
end
$$ language plpgsql;

create trigger assignments_ts_trigger
    before insert or update on assignments
    for each row execute procedure assignments_ts_trigger_function();

create index assignments_ts_index on assignments using gin(tsv);

alter table submissions alter id type bigint;
alter table submissions alter student_id type bigint;
alter table submissions alter assignment_id type bigint;
alter table submissions alter answer_sheet_id type bigint;
alter table submissions add column saved boolean not null default 'f';
alter table submissions add column finished boolean not null default 'f';
update submissions set saved = 't' where submission_type = 'ONLINE'
    and due_date < current_timestamp;
update submissions set finished = 't' where submission_type = 'ONLINE'
    and due_date < current_timestamp;

create table departments (
    id              bigint primary key,
    name            varchar(255) not null unique,
    abbreviation    varchar(255) not null unique,
    welcome_message text
);

create table department_administrators (
    department_id   bigint not null references departments(id),
    user_id         bigint not null references users(id)
);

create table department_faculty (
    department_id   bigint not null references departments(id),
    user_id         bigint not null references users(id)
);

create table department_instructors (
    department_id   bigint not null references departments(id),
    user_id         bigint not null references users(id)
);

create table department_reviewers (
    department_id   bigint not null references departments(id),
    user_id         bigint not null references users(id)
);

create table department_undergraduate_courses (
    department_id   bigint not null references departments(id),
    course_id       bigint not null references courses(id)
);
    
create table department_additional_undergraduate_courses (
    department_id   bigint not null references departments(id),
    course_id       bigint not null references courses(id)
);

create table department_graduate_courses (
    department_id   bigint not null references departments(id),
    course_id       bigint not null references courses(id)
);

create table department_additional_graduate_courses (
    department_id   bigint not null references departments(id),
    course_id       bigint not null references courses(id)
);

-- XXX Are we using additional courses??

insert into departments (id, name, abbreviation) values (200, 'Computer Science', 'cs');
insert into department_administrators values (200, 1000);

alter table surveys alter id type bigint;
alter table surveys drop constraint surveys_name_key;
alter table surveys drop constraint surveys_type_check;
alter table surveys rename survey_type to type;
alter table surveys alter question_sheet_id type bigint;
alter table surveys add column publish_date timestamp;
alter table surveys add column close_date timestamp;
alter table surveys add column author_id bigint references users(id);
alter table surveys add column date timestamp not null default current_timestamp;
update surveys s set publish_date = q.publish_date, close_date = q.close_date,
    author_id = q.creator_id, date = q.create_date
    from question_sheets q where s.question_sheet_id = q.id;
alter table surveys alter author_id set not null;
alter table surveys add column department_id bigint references departments(id);
update surveys set department_id = 200;
alter table surveys add column deleted boolean not null default 'f';
update surveys set deleted = not enabled;
alter table surveys drop column enabled;

alter table question_sheets alter id type bigint;
alter table question_sheets alter description type varchar(8000);
alter table question_sheets drop name;
alter table question_sheets drop creator_id;
alter table question_sheets drop create_date;
alter table question_sheets drop publish_date;
alter table question_sheets drop close_date;

alter table survey_responses alter id type bigint;
alter table survey_responses alter survey_id type bigint;
alter table survey_responses alter answer_sheet_id type bigint;
alter table surveys_taken alter user_id type bigint;
alter table surveys_taken alter survey_id type bigint;

alter table forums alter id type bigint;
alter table forums drop column owner_id;
alter table forums alter last_post_id type bigint;
alter table forums add column department_id bigint references departments(id);
alter table forums alter course_id type bigint;
alter table forums drop column notification_enabled;
alter table forums add column hidden boolean not null default 'f';
alter table forums add check ( department_id is null or course_id is null );
alter table forums drop constraint forums_name_key;
update forums set hidden = 't' where name = 'Wiki Discussion';
update forums set department_id = 200 where name = 'Advisement';
update forums set department_id = 200 where name = 'General Discussion';
update forums set department_id = 200 where name = 'Announcements';
update forums set department_id = 200 where name = 'Job Opportunities';

alter table forums add column tsv tsvector;

create function forums_ts_trigger_function() returns trigger as $$
declare
    l_course        courses;
    l_department    departments;
begin
    if new.course_id is not null then
        select * into l_course from courses where id = new.course_id;
        new.tsv := setweight(to_tsvector(l_course.code), 'A') ||
                   setweight(to_tsvector(l_course.name), 'A');
    elsif new.department_id is not null then
        select * into l_department from departments where id = new.department_id;
        new.tsv := setweight(to_tsvector(new.name), 'A') ||
                   setweight(to_tsvector(l_department.name), 'B') ||
                   setweight(to_tsvector(l_department.abbreviation), 'B');
    else
        new.tsv := setweight(to_tsvector(new.name), 'A');
    end if;
    return new;
end
$$ language plpgsql;

create trigger forums_ts_trigger
    before insert or update on forums
    for each row execute procedure forums_ts_trigger_function();

create index forums_ts_index on forums using gin(tsv);

alter table forum_moderators alter forum_id type bigint;
alter table forum_moderators alter user_id type bigint;

alter table forum_topics alter id type bigint;
alter table forum_topics alter first_post_id type bigint;
alter table forum_topics alter last_post_id type bigint;
alter table forum_topics alter forum_id type bigint;
alter table forum_topics drop column notification_enabled;

alter table forum_posts alter id type bigint;
alter table forum_posts alter subject set not null;
alter table forum_posts alter content set not null;
alter table forum_posts alter author_id type bigint;
alter table forum_posts alter topic_id type bigint;
alter table forum_posts alter edited_by type bigint;

alter table forum_post_attachments rename forum_post_id to post_id;
alter table forum_post_attachments alter post_id type bigint;
alter table forum_post_attachments alter file_id type bigint;

drop trigger forum_posts_ts_trigger on forum_posts;
drop function forum_posts_ts_trigger_function();

create function forum_posts_ts_trigger_function() returns trigger as $$
begin
    new.tsv := setweight(to_tsvector(new.subject), 'A') ||
               setweight(to_tsvector(new.content), 'D');
    return new;
end
$$ language plpgsql;

create trigger forum_posts_ts_trigger
    before insert or update on forum_posts
    for each row execute procedure forum_posts_ts_trigger_function();

create function subscribe_to_course_forum( p_user_id bigint, p_section_id bigint)
    returns void as $$
declare
    l_current_quarter   integer default quarter();
    l_section_quarter   integer;
    l_course_id         courses.id%type;
    l_forum_id          forums.id%type;
begin
    select course_id into l_course_id from sections where id = p_section_id;
    select id into l_forum_id from forums where course_id = l_course_id;
    if not exists (select * from subscriptions where subscribable_type = 'FM'
            and subscribable_id = l_forum_id and subscriber_id = p_user_id) then
        select quarter into l_section_quarter from sections where id = p_section_id;
        insert into subscriptions (id, subscribable_type, subscribable_id,
            subscriber_id, quarter, auto_subscribed) values
            (nextval('hibernate_sequence'), 'FM', l_forum_id, p_user_id,
            l_section_quarter, 't');
    end if;
end;
$$ language plpgsql;

create function unsubscribe_from_course_forum(p_user_id bigint, p_section_id bigint)
    returns void as $$
declare
    l_current_quarter   integer default quarter();
    l_section_quarter   integer;
    l_course_id         courses.id%type;
    l_forum_id          forums.id%type;
begin
    select course_id into l_course_id from sections where id = p_section_id;
    select id into l_forum_id from forums where course_id = l_course_id;
    delete from subscriptions where subscribable_type = 'FM'
        and subscribable_id = l_forum_id
        and subscriber_id = p_user_id
        and auto_subscribed = 't';
end;
$$ language plpgsql;

create function section_instructors_subscription_trigger_function()
    returns trigger as $$
begin
    if tg_op = 'DELETE' or tg_op = 'UPDATE' then
        perform unsubscribe_from_course_forum(old.instructor_id, old.section_id);
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        perform subscribe_to_course_forum(new.instructor_id, new.section_id);
    end if;
    return null;
end
$$ language plpgsql;

create trigger section_instructors_subscription_trigger
    after insert or update or delete on section_instructors
    for each row execute procedure section_instructors_subscription_trigger_function();

create function enrollment_subscription_trigger_function() returns trigger as $$
begin
    if tg_op = 'DELETE' or tg_op = 'UPDATE' then
        perform unsubscribe_from_course_forum(old.student_id, old.section_id);
    end if;
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        perform subscribe_to_course_forum(new.student_id, new.section_id);
    end if;
    return null;
end
$$ language plpgsql;

create trigger enrollment_subscription_trigger
    after insert or update or delete on enrollments
    for each row execute procedure enrollment_subscription_trigger_function();

alter table wiki_pages alter id type bigint;
alter table wiki_pages alter owner_id type bigint;
alter table wiki_pages add column views integer not null default 0;
create index wiki_pages_path_pattern_index on wiki_pages (path varchar_pattern_ops);

alter table wiki_revisions alter id type bigint;
alter table wiki_revisions alter subject set not null;
alter table wiki_revisions alter content set not null;
alter table wiki_revisions alter date set default current_timestamp;
alter table wiki_revisions alter author_id type bigint;
alter table wiki_revisions alter page_id type bigint;

create table wiki_revision_attachments (
    revision_id bigint not null references wiki_revisions(id),
    file_id     bigint not null references files(id),
  primary key (revision_id, file_id)
);

alter table wiki_discussions alter page_id type bigint;
alter table wiki_discussions alter topic_id type bigint;

drop trigger wiki_revisions_ts_trigger on wiki_revisions;
drop function wiki_revisions_ts_trigger_function();

create function wiki_revisions_ts_trigger_function() returns trigger as $$
begin
    update wiki_pages
        set tsv = setweight(to_tsvector(new.subject), 'A') ||
                  setweight(to_tsvector(new.content), 'D'),
            ts_subject = new.subject,
            ts_content = new.content
        where id = new.page_id;
    return new;
end
$$ language plpgsql;

create trigger wiki_revisions_ts_trigger
    before insert on wiki_revisions
    for each row execute procedure wiki_revisions_ts_trigger_function();

alter table news alter id type bigint;
alter table news alter topic_id type bigint;
alter table news add column department_id bigint references departments(id);
update news set department_id = 200;

alter table mailinglists alter id type bigint;
alter table mailinglists drop column date;
alter table mailinglists drop column owner_id;
alter table mailinglists add column department_id bigint references departments(id);
update mailinglists set department_id = 200 where name like 'cs-%';

alter table mailinglist_messages alter id type bigint;
alter table mailinglist_messages alter subject set not null;
alter table mailinglist_messages alter content set not null;
alter table mailinglist_messages alter date set default current_timestamp;
alter table mailinglist_messages alter author_id type bigint;
alter table mailinglist_messages alter mailinglist_id type bigint;

alter table mailinglist_message_attachments rename mailinglist_message_id to message_id;
alter table mailinglist_message_attachments alter message_id type bigint;
alter table mailinglist_message_attachments alter file_id type bigint;

drop trigger mailinglist_messages_ts_trigger on mailinglist_messages;
drop function mailinglist_messages_ts_trigger_function();

create function mailinglist_messages_ts_trigger_function() returns trigger as $$
declare
    author users%rowtype;
begin
	select * into author from users where id = new.author_id;
    new.tsv := setweight(to_tsvector(author.first_name), 'A')
        || setweight(to_tsvector(author.first_name), 'A')
        || setweight(to_tsvector(new.subject), 'A')
        || setweight(to_tsvector(new.content), 'D');
    return new;
end
$$ language plpgsql;

create trigger mailinglist_messages_ts_trigger
    before insert or update on mailinglist_messages
    for each row execute procedure mailinglist_messages_ts_trigger_function();

delete from mailinglist_message_attachments where message_id in
    (select id from mailinglist_messages where mailinglist_id in
        (select id from mailinglists where department_id is null));
delete from mailinglist_messages where mailinglist_id in
    (select id from mailinglists where department_id is null);
delete from mailinglists where department_id is null;

drop table standing_mailinglists_to_subscribe;
drop table standing_mailinglists_to_unsubscribe;

alter table mailinglist_messages drop constraint mailinglist_messages_mailinglist_id_fkey;

update mailinglists set id = 20016 where id = 20003;
update mailinglist_messages set mailinglist_id = 20016 where mailinglist_id = 20003;
update mailinglists set id = 20017 where id = 20010;
update mailinglist_messages set mailinglist_id = 20017 where mailinglist_id = 20010;
update mailinglists set id = 20018 where id = 20011;
update mailinglist_messages set mailinglist_id = 20018 where mailinglist_id = 20011;
update mailinglists set id = 20003 where id = 20001;
update mailinglist_messages set mailinglist_id = 20003 where mailinglist_id = 20001;

alter table mailinglist_messages add foreign key (mailinglist_id) references mailinglists(id);

alter table standings alter id type bigint;
insert into standings (id, symbol, name, description) values
    (23025, 'HG', 'Graduated from Blended BS/MS Program',
      'Graduated from the Blended BS/MS program.');

create table standing_mailinglists (
    standing_id     bigint not null references standings(id),
    mailinglist     varchar(255)
);

-- mailing list membership for B Standing
insert into standing_mailinglists values (23000, 'students');
insert into standing_mailinglists values (23000, 'undergrads');

-- mailing list membership for BG Standing
insert into standing_mailinglists values (23009, 'alumni');
insert into standing_mailinglists values (23009, 'alumni-undergrad');

-- mailing list membership for G0 Standing
insert into standing_mailinglists values (23010, 'students');
insert into standing_mailinglists values (23010, 'grads');
insert into standing_mailinglists values (23010, 'grads-g0');

-- mailing list membership for G1 Standing
insert into standing_mailinglists values (23011, 'students');
insert into standing_mailinglists values (23011, 'grads');
insert into standing_mailinglists values (23011, 'grads-g1');

-- mailing list membership for G2 Standing
insert into standing_mailinglists values (23012, 'students');
insert into standing_mailinglists values (23012, 'grads');
insert into standing_mailinglists values (23012, 'grads-g2');

-- Mailing list membership for G3 Standing
insert into standing_mailinglists values (23013, 'students');
insert into standing_mailinglists values (23013, 'grads');
insert into standing_mailinglists values (23013, 'grads-g3');

-- mailing list membership for GG Standing
insert into standing_mailinglists values (23019, 'alumni');
insert into standing_mailinglists values (23019, 'alumni-grad');

-- mailing list membership for HG Standing
insert into standing_mailinglists values (23025, 'alumni');
insert into standing_mailinglists values (23025, 'alumni-undergrad');
insert into standing_mailinglists values (23025, 'alumni-grad');

alter table academic_standings alter id type bigint;
alter table academic_standings alter student_id type bigint;
alter table academic_standings alter standing_id type bigint;
alter table academic_standings add column department_id bigint references departments(id);
update academic_standings set department_id = 200;
alter table academic_standings drop constraint academic_standings_student_id_key;
alter table academic_standings add unique(student_id, department_id, standing_id);

create table current_standings (
    student_id              bigint not null references users(id),
    department_id           bigint not null references departments(id),
    academic_standing_id    bigint unique not null references academic_standings(id),
  primary key (student_id, department_id)
);

insert into current_standings select id, 200, current_standing_id from users
    where current_standing_id is not null;

alter table users drop column current_standing_id;
drop trigger mailinglist_subscription_on_standing_change_trigger on users;
drop function mailinglist_subscription_on_standing_change_trigger_function();

create or replace function mailinglist_subscription_on_standing_change_trigger_function()
    returns trigger as $$
declare
    l_dept              varchar;
    l_mailinglist       varchar;
    l_standing_id       standings.id%type;
    l_mailinglist_id    mailinglists.id%type;
    l_subscription_id   subscriptions.id%type;
begin
    if tg_op = 'DELETE' or tg_op = 'UPDATE' then
        select abbreviation into l_dept from departments
            where id = old.department_id;
        select standing_id into l_standing_id from academic_standings
            where id = old.academic_standing_id;
        for l_mailinglist in select mailinglist from standing_mailinglists
            where standing_id = l_standing_id and mailinglist not like 'alumni%' loop
                delete from subscriptions where subscriber_id = old.student_id
                    and subscribable_type = 'ML' and auto_subscribed = 't'
                    and subscribable_id = (select id from mailinglists
                        where name = l_dept || '-' || l_mailinglist);
        end loop;
	end if;
	
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        select abbreviation into l_dept from departments
            where id = new.department_id;
        select standing_id into l_standing_id from academic_standings
            where id = new.academic_standing_id;
        for l_mailinglist in select mailinglist from standing_mailinglists
            where standing_id = l_standing_id loop
            select id into l_mailinglist_id from mailinglists
                where name = l_dept || '-' || l_mailinglist;
            select id into l_subscription_id from subscriptions
                where subscribable_id = l_mailinglist_id
                and subscriber_id = new.student_id
                and subscribable_type = 'ML';
            if not found then
                insert into subscriptions (id, subscribable_type, subscribable_id,
                    subscriber_id, auto_subscribed) values (
                    nextval('hibernate_sequence'), 'ML', l_mailinglist_id,
                    new.student_id, 't');
            end if;
        end loop;
    end if;

    return null; 
end
$$ language plpgsql;

create trigger mailinglist_subscription_on_standing_change_trigger
    after insert or update or delete on current_standings
    for each row
    execute procedure mailinglist_subscription_on_standing_change_trigger_function();

alter table advisements rename to advisement_records;
alter table advisement_records alter id type bigint;
alter table advisement_records alter student_id type bigint;
alter table advisement_records alter advisor_id type bigint;
alter table advisement_records drop column edit_date;
alter table advisement_records drop column edited_by;
alter table advisement_records rename for_advisor_only to for_advisors_only;
alter table advisement_records alter for_advisors_only set not null;
alter table advisement_records alter emailed_to_student set not null;
alter table advisement_records add column deleted boolean not null default 'f';
update advisement_records set comment = '<p>' || comment || '</p>'
    where comment is not null;

create table advisement_record_attachments (
    record_id   bigint not null references advisement_records(id),
    file_id     bigint not null references files(id),
  primary key (record_id, file_id)
);

alter table course_substitutions alter id type bigint;
alter table course_substitutions rename orginal_course_id to original_course_id;
alter table course_substitutions alter original_course_id type bigint;
alter table course_substitutions rename substitue_course_id to substitute_course_id;
alter table course_substitutions alter substitute_course_id type bigint;
alter table course_substitutions rename advisement_id to advisement_record_id;
alter table course_substitutions alter advisement_record_id type bigint;

alter table course_transfers alter id type bigint;
alter table course_transfers alter course_id type bigint;
alter table course_transfers rename advisement_id to advisement_record_id;
alter table course_transfers alter advisement_record_id type bigint;

alter table course_waivers alter id type bigint;
alter table course_waivers alter course_id type bigint;
alter table course_waivers rename advisement_id to advisement_record_id;
alter table course_waivers alter advisement_record_id type bigint;

create table projects (
    id              bigint primary key,
    name            varchar(255) not null,
    description     varchar(8000),
    department_id   bigint references departments(id),
    year            integer not null,
    published       boolean not null default 'f'
);

create table project_advisors (
    project_id      bigint not null references projects(id),
    advisor_id      bigint not null references users(id),
    advisor_order   bigint not null,
  primary key (project_id, advisor_order)
);

create table project_members (
    project_id  bigint not null,
    member_id   bigint not null,
  primary key (project_id, member_id)
);

create table project_resources (
    project_id      bigint not null references projects(id),
    resource_id     bigint not null references resources(id),
    resource_order  bigint not null,
  primary key (project_id, resource_order)
);

drop function quarter(integer);
drop function quarter(timestamp);

--
-- Given a date, returns the quarter.
--
create or replace function quarter( p_date date ) returns integer as $$
declare
    l_code integer := (extract(year from p_date) - 1900) * 10;
    l_week integer := extract(week from p_date);
begin
    if l_week < 13 then
        l_code := l_code + 1;
    elsif l_week < 25 then
        l_code := l_code + 3;
    elsif l_week < 38 then
        l_code := l_code + 6;
    else
        l_code := l_code + 9;
    end if;
    return l_code;
end;
$$ language plpgsql;

--
-- Returns the current quarter.
--
create or replace function quarter() returns integer as $$
begin
    return quarter(current_date);
end;
$$ language plpgsql;

--
-- Given a quarter code, returns the quarter name (e.g. Fall 2012).
--
create or replace function quarter( p_code integer ) returns varchar as $$
declare
    l_year      varchar;
    l_quarter   varchar;
begin
    l_year := cast( p_code/10+1900 as varchar );

    case p_code % 10
        when 1 then
            l_quarter = 'Winter';
        when 3 then
            l_quarter = 'Spring';
        when 6 then
            l_quarter = 'Summer';
        else
            l_quarter = 'Fall';
    end case;

    return l_quarter || ' ' || l_year;
end;
$$ language plpgsql;

drop table program_core_courses;
drop table program_course_substitutions;
drop table program_elective_courses;
drop table program_limited_elective_courses;
drop table program_other_courses;
drop table program_prereq_courses;
drop table concentration_courses;
drop table concentrations;
drop table programs;
drop table forum_favorites;
drop table log_events;
drop table log_request_parameters;
drop table log_requests;
drop table prerequisite_courses;
drop table course_prerequisites;
drop table prerequisites;
