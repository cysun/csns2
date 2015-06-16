drop table site_item_additional_resources;

create table site_item_additional_resources (
    item_id         bigint not null references site_items(id),
    resource_id     bigint not null references resources(id),
    resource_order  integer not null,
  primary key (item_id, resource_order)
);
