alter table rubric_evaluations add column completed boolean not null default 'f';

update rubric_evaluations e set completed = 't' where 5 = (
    select count(*) from rubric_evaluation_ratings
        where evaluation_id = e.id and rating > -1
    );

create sequence result_sequence cycle;

create or replace function year( p_code integer ) returns integer as $$
begin
    return p_code / 10 + 1900;                                                  
end;
$$ language plpgsql;
