-- This script removes everything in a CSNS database.

set client_min_messages=WARNING;

drop sequence result_sequence;
drop sequence hibernate_sequence;

drop aggregate median( numeric );
drop function _final_median( numeric[] );

drop function year(integer);
drop function term(integer);
drop function term();
drop function term(date);

drop table prereg_section_registrations;
drop table prereg_schedule_registrations;
drop table prereg_sections;
drop table prereg_schedules;

drop table assessment_program_resources;
drop table assessment_program_sections;
drop table assessment_program_outcomes;
drop table assessment_program_objectives;
drop table assessment_programs;

alter table course_journal_rubric_assignments drop constraint course_journal_rubric_assignments_assignment_id_fkey;
drop table rubric_evaluation_ratings;
drop table rubric_evaluations;
drop table rubric_external_evaluators;
drop table rubric_submissions;
drop table rubric_assignments;
drop table rubric_indicator_criteria;
drop table rubric_indicators;
drop trigger rubrics_ts_trigger on rubrics;
drop function rubrics_ts_trigger_function();
drop table rubrics;

drop table mft_distribution_entries;
drop table mft_distributions;
drop table mft_distribution_types;
drop table mft_indicators;
drop table mft_scores;

drop table project_resources;
drop trigger project_liaisons_ts_trigger on project_liaisons;
drop function project_liaisons_ts_trigger_function();
drop table project_liaisons;
drop trigger project_students_ts_trigger on project_students;
drop function project_students_ts_trigger_function();
drop table project_students;
drop trigger project_advisors_ts_trigger on project_advisors;
drop function project_advisors_ts_trigger_function();
drop table project_advisors;
drop trigger projects_ts_trigger on projects;
drop function projects_ts_trigger_function();
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
drop table forum_members;
drop table forum_moderators;
drop trigger forums_ts_trigger on forums;
drop function forums_ts_trigger_function();
drop table forums;

drop table survey_chart_points;
drop table survey_chart_series;
drop table survey_chart_xcoordinates;
drop table survey_charts;
drop table surveys_taken;
drop table survey_responses;
drop trigger surveys_ts_trigger on surveys;
drop function surveys_ts_trigger_function();
drop table surveys;

drop table site_item_additional_resources;
drop table site_items;
drop table site_blocks;
drop table site_announcements;
drop table site_class_info;
drop table sites;

drop table course_mapping_group2;
drop table course_mapping_group1;
drop table course_mappings;

drop trigger programs_ts_trigger on programs;
drop function programs_ts_trigger_function();
alter table users drop constraint users_personal_program_id_fkey;
drop table personal_program_entries;
drop table personal_program_blocks;
drop table personal_programs;
drop table program_block_courses;
drop table program_blocks;
drop table programs;

alter table courses drop constraint courses_department_fk;
alter table groups drop constraint groups_department_fk;
alter table users drop constraint users_major_fk;
drop table department_options;
drop table department_graduate_courses;
drop table department_undergraduate_courses;
drop table department_reviewers;
drop table department_evaluators;
drop table department_instructors;
drop table department_faculty;
drop table department_administrators;
drop table departments;

alter table sections drop constraint sections_journal_id_fkey;
alter table courses drop constraint courses_journal_id_fkey;
drop table course_journal_student_samples;
drop table course_journal_rubric_assignments;
drop table course_journal_assignments;
drop table course_journal_handouts;
drop table course_journals;

alter table files drop constraint files_submission_id_fkey;
drop table submissions;
drop trigger assignments_ts_trigger on assignments;
drop function assignments_ts_trigger_function();
drop table assignments;

drop table section_attendance_events;
drop table attendance_records;
drop table attendance_events;
drop table enrollments;
drop trigger section_instructors_ts_trigger on section_instructors;
drop function section_instructors_ts_trigger_function();
drop table section_instructors;
drop trigger sections_ts_trigger on sections;
drop function  sections_ts_trigger_function();
drop table sections;
drop table course_prerequisites;
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

drop trigger resources_ts_trigger on resources;
drop function resources_ts_trigger_function();
drop table resources;

alter table users drop constraint original_picture_id_fkey;
alter table users drop constraint profile_picture_id_fkey;
alter table users drop constraint profile_thumbnail_id_fkey;
drop table files;

drop table members;
drop table groups;
drop table persistent_logins;
drop table authorities;
drop table users;
