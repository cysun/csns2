create table sites (
    id          bigint primary key,
    section_id  bigint unique references sections(id),
    shared      boolean not null default 'f'
);

create table site_announcements (
    id      bigint primary key,
    date    timestamp not null default current_timestamp,
    content varchar(4000),
    site_id bigint references sites(id)
);

create table site_blocks (
    id          bigint primary key,
    name        varchar(255),
    placeholder boolean not null default 'f',
    hidden      boolean not null default 'f',
    site_id     bigint references sites(id),
    block_index integer
);

create table site_items (
    id          bigint primary key,
    resource_id bigint references resources(id),
    hidden      boolean not null default 'f',
    block_id    bigint references site_blocks(id),
    item_index  integer
);

create table site_item_additional_resources (
    item_id     bigint not null references site_items(id),
    resource_id bigint not null references resources(id),
  primary key (item_id, resource_id)
);
