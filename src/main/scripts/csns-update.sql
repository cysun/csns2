alter table users drop column gender;
alter table users drop column birthday;

update forum_topics t set last_post_date = (
    select date from forum_posts where id = t.last_post_id
) where t.last_post_date is null;
