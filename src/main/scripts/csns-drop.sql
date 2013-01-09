-- This script removes everything in a CSNS database.

set client_min_messages=WARNING;

drop sequence hibernate_sequence;

drop function quarter(integer);
drop function quarter();
drop function quarter(date);

drop table project_resources;
drop table project_students;
drop table project_advisors;
drop table projects;

drop table course_waivers;
drop table course_transfers;
drop table course_substitutions;
drop table advisement_record_attachments;
drop table advisement_records;

drop trigger mailinglist_subscription_on_standing_change_trigger on current_standings;
drop function mailinglist_subscription_on_standing_change_trigger_function();
drop table current_standings;
drop table academic_standings;
drop table standing_mailinglists;
drop table standings;

drop trigger mailinglist_messages_ts_trigger on mailinglist_messages;
drop function mailinglist_messages_ts_trigger_function();
drop table mailinglist_message_attachments;
drop table mailinglist_messages;
drop table mailinglists;

drop table news;

drop trigger wiki_revisions_ts_trigger on wiki_revisions;
drop function wiki_revisions_ts_trigger_function();
drop table wiki_discussions;
drop table wiki_revision_attachments;
drop table wiki_revisions;
drop table wiki_pages;

drop trigger enrollment_subscription_trigger on enrollments;
drop function enrollment_subscription_trigger_function();
drop trigger section_instructors_subscription_trigger on section_instructors;
drop function section_instructors_subscription_trigger_function();
drop function unsubscribe_from_course_forum(bigint,bigint);
drop function subscribe_to_course_forum(bigint,bigint);

drop trigger forum_posts_ts_trigger on forum_posts;
drop function forum_posts_ts_trigger_function();
alter table forum_topics drop constraint fk_forum_topic_last_post;
alter table forum_topics drop constraint fk_forum_topic_first_post;
alter table forums drop constraint fk_forum_last_post;
drop table forum_post_attachments;
drop table forum_posts;
drop table forum_topics;
drop table forum_moderators;
drop trigger forums_ts_trigger on forums;
drop function forums_ts_trigger_function();
drop table forums;

drop table surveys_taken;
drop table survey_responses;
drop trigger surveys_ts_trigger on surveys;
drop function surveys_ts_trigger_function();
drop table surveys;

drop table department_additional_graduate_courses;
drop table department_graduate_courses;
drop table department_additional_undergraduate_courses;
drop table department_undergraduate_courses;
drop table department_reviewers;
drop table department_instructors;
drop table department_faculty;
drop table department_administrators;
drop table departments;

alter table files drop constraint files_submission_id_fkey;
drop table submissions;
drop trigger assignments_ts_trigger on assignments;
drop function assignments_ts_trigger_function();
drop table assignments;

drop table enrollments;
drop trigger section_instructors_ts_trigger on section_instructors;
drop function section_instructors_ts_trigger_function();
drop table section_instructors;
drop trigger sections_ts_trigger on sections;
drop function  sections_ts_trigger_function();
drop table sections;
drop trigger courses_ts_trigger on courses;
drop function courses_ts_trigger_function();
drop table courses;

drop table grades;

drop table answer_selections;
drop table answers;
drop table answer_sections;
drop table answer_sheets;
drop table question_correct_selections;
drop table question_choices;
drop table questions;
drop table question_sections;
drop table question_sheets;

drop table subscriptions;

drop table resources;
drop table files;

drop table persistent_logins;
drop table authorities;
drop table users;
