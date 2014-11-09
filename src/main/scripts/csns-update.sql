alter table departments add column full_name varchar(255);
update departments set full_name = 'Computer Science Department' where abbreviation = 'cs';
update departments set full_name = 'Technology Department' where abbreviation = 'tech';
alter table departments alter column full_name set not null;
alter table departments add constraint departments_full_name_key unique (full_name);

alter table forums add column members_only boolean not null default 'f';

create table forum_members (
    forum_id    bigint not null references forums(id),
    user_id     bigint not null references users(id),
  primary key (forum_id, user_id)
);
