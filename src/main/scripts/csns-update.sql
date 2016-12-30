alter table users drop column program_id;

drop trigger programs_ts_trigger on programs;
drop function programs_ts_trigger_function();
drop table program_elective_courses;
drop table program_required_courses;
drop table programs;

create table programs (
    id              bigint primary key,
    department_id   bigint references departments(id),
    name            varchar(255) not null,
    description     varchar(8000),
    publish_date    timestamp,
    last_edited_by  bigint references users(id),
    obsolete        boolean not null default 'f'
);

create table program_blocks (
    id              bigint primary key,
    program_id      bigint references programs(id),
    block_index     integer,
    name            varchar(255),
    description     varchar(8000),
    units_required  integer not null default 0
);

create table program_block_courses (
    block_id    bigint not null references program_blocks(id),
    course_id   bigint not null references courses(id)
);

create table personal_programs (
    id              bigint primary key,
    program_id      bigint references programs(id),
    approve_date    timestamp,
    approved_by     bigint references users(id)
);

create table personal_program_blocks (
    id          bigint primary key,
    program_id  bigint references personal_programs(id),
    block_index integer,
    name        varchar(255),
    description varchar(8000)
);

create table personal_program_entries (
    id              bigint primary key,
    block_id        bigint references personal_program_blocks(id),
    course_id       bigint references courses(id),
    enrollment_id   bigint references enrollments(id)
);

alter table users add column personal_program_id bigint
    references personal_programs(id);

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
