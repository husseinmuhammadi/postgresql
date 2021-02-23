create table T1
(
    id    integer not null primary key,
    value integer
);

create or replace function test21() returns integer as
$$
declare
    v_state   TEXT;
    v_msg     TEXT;
    v_detail  TEXT;
    v_hint    TEXT;
    v_context TEXT;
begin

    insert into T1(id, value) values (1, 1);
    insert into T1(id, value) values (1, 1);
    return 0;

exception
    when others then
        get stacked diagnostics
            v_state = returned_sqlstate,
            v_msg = message_text,
            v_detail = pg_exception_detail,
            v_hint = pg_exception_hint,
            v_context = pg_exception_context;

        raise exception E'Got exception:
    state  : %
    message: %
    detail : %
    hint   : %
    context: %' , v_state, v_msg, v_detail, v_hint, v_context;
        return 1;
end;
$$ language plpgsql;