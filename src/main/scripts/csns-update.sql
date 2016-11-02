update grades set value = 3.7 where symbol = 'A-';
update grades set value = 3.3 where symbol = 'B+';
update grades set value = 2.7 where symbol = 'B-';
update grades set value = 2.3 where symbol = 'C+';
update grades set value = 1.7 where symbol = 'C-';
update grades set value = 1.3 where symbol = 'D+';
update grades set value = 0.7 where symbol = 'D-';

alter table courses drop column min_units;
alter table courses rename column max_units to units;
