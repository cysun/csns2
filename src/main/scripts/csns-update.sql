alter table mft_indicators add column num_of_students integer;
update mft_indicators i set num_of_students = (select count(id) from mft_scores where date = i.date);
alter table mft_indicators alter column num_of_students set not null;
