alter table resources add column private boolean not null default 'f';

alter table projects add column sponsor varchar(255);

create table project_liaisons (
    project_id  bigint not null references projects(id),
    liaison_id  bigint not null references users(id),
  primary key (project_id, liaison_id)
);

alter table users add column original_picture_id bigint references files(id);
alter table users add column profile_picture_id bigint references files(id);
alter table users add column profile_thumbnail_id bigint references files(id);
