drop trigger section_instructors_ts_trigger on section_instructors;
drop function section_instructors_ts_trigger_function();
drop trigger sections_ts_trigger on sections;
drop function  sections_ts_trigger_function();

create function sections_ts_trigger_function() returns trigger as $$
declare
    l_user      users%rowtype;
    l_course    courses%rowtype;
begin
    select * into l_course from courses where id = new.course_id;
    new.tsv := setweight(to_tsvector(l_course.code), 'A') ||
               setweight(to_tsvector(l_course.name), 'B') ||
               setweight(to_tsvector(term(new.term)), 'A');
    for l_user in select u.* from users u, section_instructors i
        where i.section_id = new.id and i.instructor_id = u.id loop
            new.tsv := new.tsv ||
                setweight(to_tsvector(l_user.first_name), 'B') ||
                setweight(to_tsvector(l_user.last_name), 'B') ||
                setweight(to_tsvector(l_user.username), 'B');
    end loop;
    return new;
end
$$ language plpgsql;

create trigger sections_ts_trigger
    before insert or update on sections
    for each row execute procedure sections_ts_trigger_function();

create function section_instructors_ts_trigger_function() returns trigger as $$
begin
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        update sections set tsv = '' where id = new.section_id;
    end if;
    if tg_op = 'DELETE' or tg_op = 'UPDATE' then
        update sections set tsv = '' where id = old.section_id;
    end if;
    return null;
end
$$ language plpgsql;

create trigger section_instructors_ts_trigger
    after insert or delete or update on section_instructors
    for each row execute procedure section_instructors_ts_trigger_function();

drop trigger projects_ts_trigger on projects;
drop function projects_ts_trigger_function();

create function projects_ts_trigger_function() returns trigger as $$
declare
    l_user          users%rowtype;
    l_department    departments%rowtype;
begin
    new.tsv := setweight(to_tsvector(new.title), 'A') ||
               setweight(to_tsvector(coalesce(new.sponsor, '')), 'B') ||
               setweight(to_tsvector(coalesce(new.description, '')), 'D');
    select * into l_department from departments where id = new.department_id;
    new.tsv := new.tsv ||
            setweight(to_tsvector(l_department.name), 'B') ||
            setweight(to_tsvector(l_department.abbreviation), 'B');
    for l_user in select u.* from users u, project_advisors a
        where a.project_id = new.id and a.advisor_id = u.id loop
            new.tsv := new.tsv ||
                setweight(to_tsvector(l_user.first_name), 'B') ||
                setweight(to_tsvector(l_user.last_name), 'B') ||
                setweight(to_tsvector(l_user.username), 'B');
    end loop;
    for l_user in select u.* from users u, project_students s
        where s.project_id = new.id and s.student_id = u.id loop
            new.tsv := new.tsv ||
                setweight(to_tsvector(l_user.first_name), 'B') ||
                setweight(to_tsvector(l_user.last_name), 'B') ||
                setweight(to_tsvector(l_user.username), 'B');
    end loop;
    for l_user in select u.* from users u, project_liaisons l
        where l.project_id = new.id and l.liaison_id = u.id loop
            new.tsv := new.tsv ||
                setweight(to_tsvector(l_user.first_name), 'B') ||
                setweight(to_tsvector(l_user.last_name), 'B') ||
                setweight(to_tsvector(l_user.username), 'B');
    end loop;
    return new;
end
$$ language plpgsql;

create trigger projects_ts_trigger
    before insert or update on projects
    for each row execute procedure projects_ts_trigger_function();

create function project_advisors_ts_trigger_function() returns trigger as $$
begin
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        update projects set tsv = '' where id = new.project_id;
    end if;
    if tg_op = 'DELETE' or tg_op = 'UPDATE' then
        update projects set tsv = '' where id = old.project_id;
    end if;
    return null;
end
$$ language plpgsql;

create trigger project_advisors_ts_trigger
    after insert or delete or update on project_advisors
    for each row execute procedure project_advisors_ts_trigger_function();

create function project_students_ts_trigger_function() returns trigger as $$
begin
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        update projects set tsv = '' where id = new.project_id;
    end if;
    if tg_op = 'DELETE' or tg_op = 'UPDATE' then
        update projects set tsv = '' where id = old.project_id;
    end if;
    return null;
end
$$ language plpgsql;

create trigger project_students_ts_trigger
    after insert or delete or update on project_students
    for each row execute procedure project_students_ts_trigger_function();

create function project_liaisons_ts_trigger_function() returns trigger as $$
begin
    if tg_op = 'INSERT' or tg_op = 'UPDATE' then
        update projects set tsv = '' where id = new.project_id;
    end if;
    if tg_op = 'DELETE' or tg_op = 'UPDATE' then
        update projects set tsv = '' where id = old.project_id;
    end if;
    return null;
end
$$ language plpgsql;

create trigger project_liaisons_ts_trigger
    after insert or delete or update on project_liaisons
    for each row execute procedure project_liaisons_ts_trigger_function();

update sections set tsv = '';
update projects set tsv = '';

alter table personal_program_entries
    add column requirement_met boolean not null default 'f';
