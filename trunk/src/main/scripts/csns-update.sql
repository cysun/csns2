alter table programs alter column description type varchar(80000);

alter table programs add column tsv tsvector;

create function programs_ts_trigger_function() returns trigger as $$
declare
    l_department    departments%rowtype;
begin
    if new.department_id is not null then
        select * into l_department from departments where id = new.department_id;
        new.tsv := setweight(to_tsvector(l_department.name), 'A') ||
                   setweight(to_tsvector(l_department.abbreviation), 'A') ||
                   setweight(to_tsvector(new.name), 'A') ||
                   setweight(to_tsvector(new.description), 'D');
    end if;
    return new;
end
$$ language plpgsql;

create trigger programs_ts_trigger
    before insert or update on programs
    for each row execute procedure programs_ts_trigger_function();

create index programs_ts_index on programs using gin(tsv);

update programs set tsv = to_tsvector('');
