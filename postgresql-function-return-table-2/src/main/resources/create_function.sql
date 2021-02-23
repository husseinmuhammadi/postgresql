create or replace function cms.inc(val integer) returns integer as
$$
begin
    return val + 1;
end;
$$
    LANGUAGE PLPGSQL