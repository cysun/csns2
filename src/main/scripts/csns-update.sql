alter table sites add column folder_id bigint references files(id);
