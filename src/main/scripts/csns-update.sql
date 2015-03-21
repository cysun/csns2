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

update users set first_name = btrim(first_name)
    where first_name like ' %' or first_name like '% ';

update users set last_name = btrim(last_name)
    where last_name like ' %' or last_name like '% ';

create table attendance_events (
    id      bigint primary key,
    name    varchar(255)
);

create table attendance_records (
    id          bigint primary key,
    event_id    bigint not null references attendance_events(id),
    user_id     bigint not null references users(id),
    attended    boolean,
  unique (event_id, user_id)
);

create table section_attendance_events (
    section_id  bigint not null references sections(id),
    event_id    bigint not null references attendance_events(id),
    event_order integer not null,
  primary key (section_id, event_order)
);
