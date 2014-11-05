alter table forums add column members_only boolean not null default 'f';

create table forum_members (
    forum_id    bigint not null references forums(id),
    user_id     bigint not null references users(id),
  primary key (forum_id, user_id)
);
