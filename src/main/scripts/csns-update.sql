alter table resources add column private boolean not null default 'f';

alter table projects add column sponsor varchar(255);

create table project_liaisons (
    project_id  bigint not null references projects(id),
    liaison_id  bigint not null references users(id),
  primary key (project_id, liaison_id)
);
