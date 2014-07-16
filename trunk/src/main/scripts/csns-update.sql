alter table rubric_evaluations add column completed boolean not null default 'f';

update rubric_evaluations e set completed = 't' where 5 = (
    select count(*) from rubric_evaluation_ratings
        where evaluation_id = e.id and rating > -1
    );

create sequence result_sequence cycle;
