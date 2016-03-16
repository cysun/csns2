alter table subscriptions rename quarter to term;
alter table sections rename quarter to term;

create or replace function sections_ts_trigger_function() returns trigger as $$
declare
    l_course    courses%rowtype;
begin
    select * into l_course from courses where id = new.course_id;
    new.tsv := setweight(to_tsvector(l_course.code), 'A') ||
               setweight(to_tsvector(l_course.name), 'B') ||
               setweight(to_tsvector(term(new.term)), 'A');
    return new;
end
$$ language plpgsql;

create or replace function assignments_ts_trigger_function() returns trigger as $$
declare
    l_term          varchar;
    l_course_code   varchar;
begin
    if new.section_id is not null then
        select term(term) into l_term from sections
            where id = new.section_id;
        select c.code into l_course_code from sections s, courses c
            where s.id = new.section_id and c.id = s.course_id;
    end if;
    new.tsv := setweight(to_tsvector(l_term), 'A') ||
               setweight(to_tsvector(l_course_code), 'A') ||
               setweight(to_tsvector(new.name), 'A');
    return new;
end
$$ language plpgsql;

create or replace function subscribe_to_course_forum( p_user_id bigint, p_section_id bigint)
    returns void as $$
declare
    l_current_term   integer default term();
    l_section_term   integer;
    l_course_id         courses.id%type;
    l_forum_id          forums.id%type;
begin
    select course_id into l_course_id from sections where id = p_section_id;
    select id into l_forum_id from forums where course_id = l_course_id;
    if not exists (select * from subscriptions where subscribable_type = 'FM'
            and subscribable_id = l_forum_id and subscriber_id = p_user_id) then
        select term into l_section_term from sections where id = p_section_id;
        insert into subscriptions (id, subscribable_type, subscribable_id,
            subscriber_id, term, auto_subscribed) values
            (nextval('hibernate_sequence'), 'FM', l_forum_id, p_user_id,
            l_section_term, 't');
    end if;
end;
$$ language plpgsql;

create or replace function unsubscribe_from_course_forum(p_user_id bigint, p_section_id bigint)
    returns void as $$
declare
    l_current_term  integer default term();
    l_section_term  integer;
    l_course_id     courses.id%type;
    l_forum_id      forums.id%type;
begin
    select course_id into l_course_id from sections where id = p_section_id;
    select id into l_forum_id from forums where course_id = l_course_id;
    delete from subscriptions where subscribable_type = 'FM'
        and subscribable_id = l_forum_id
        and subscriber_id = p_user_id
        and auto_subscribed = 't';
end;
$$ language plpgsql;

alter table academic_standings rename quarter to term;

create or replace function term( p_date date ) returns integer as $$
declare
    l_code integer := (extract(year from p_date) - 1900) * 10;
    l_week integer := extract(week from p_date);
begin
    if l_week < 13 then
        l_code := l_code + 1;
    elsif l_week < 25 then
        l_code := l_code + 3;
    elsif l_week < 38 then
        l_code := l_code + 6;
    else
        l_code := l_code + 9;
    end if;
    return l_code;
end;
$$ language plpgsql;

create or replace function term() returns integer as $$
begin
    return term(current_date);
end;
$$ language plpgsql;

create or replace function term( p_code integer ) returns varchar as $$
declare
    l_year      varchar;
    l_term   varchar;
begin
    l_year := cast( p_code/10+1900 as varchar );

    case p_code % 10
        when 1 then
            l_term = 'Winter';
        when 3 then
            l_term = 'Spring';
        when 6 then
            l_term = 'Summer';
        else
            l_term = 'Fall';
    end case;

    return l_term || ' ' || l_year;
end;
$$ language plpgsql;

drop function quarter(integer);
drop function quarter();
drop function quarter(date);

create table groups (
    id              bigint primary key,
    department_id   bigint,
    name            varchar(255) not null,
    description     varchar(8000) not null,
    date            timestamp not null default current_timestamp
);

create table members (
    id          bigint primary key,
    group_id    bigint references groups(id),
    user_id     bigint references users(id),
    date        timestamp not null default current_timestamp
);

alter table groups add constraint groups_department_fk
    foreign key (department_id) references departments(id);
