create or replace function term( p_date date ) returns integer as $$
declare
    l_code integer := (extract(year from p_date) - 1900) * 10;
    l_week integer := extract(week from p_date);
begin
    if l_week < 4 then
        l_code := l_code + 1;
    elsif l_week < 22 then
        l_code := l_code + 3;
    elsif l_week < 34 then
        l_code := l_code + 6;
    else
        l_code := l_code + 9;
    end if;
    return l_code;
end;
$$ language plpgsql;
