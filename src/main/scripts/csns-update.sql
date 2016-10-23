alter table resources add column deleted boolean not null default 'f';
alter table site_items add column deleted boolean not null default 'f';
