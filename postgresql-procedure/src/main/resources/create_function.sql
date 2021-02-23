drop table person;

create table Person
(
    id         bigint not null,
    first_Name varchar(100),
    last_Name  varchar(100),
    constraint PK_PERSON primary key (id)
);

drop table Person_Hist;

create table Person_Hist
(
    id         bigint,
    first_Name varchar(100),
    last_Name  varchar(100)
);

drop table ilog;

create table ilog
(
    state    TEXT,
    msg      TEXT,
    detail   TEXT,
    hint     TEXT,
    context  TEXT,
    log_time timestamp not null default current_timestamp
);



create or REPLACE procedure sp_save_person(p_id bigint, p_first_name varchar, p_last_name varchar)
    language plpgsql
as
$$
declare
    v_state   TEXT;
    v_msg     TEXT;
    v_detail  TEXT;
    v_hint    TEXT;
    v_context TEXT;
begin
    BEGIN
        insert into Person_Hist
        select p_id, p_first_name, p_last_name;
        insert into Person
        select p_id, p_first_name, p_last_name;
    EXCEPTION
        WHEN OTHERS THEN
            get stacked diagnostics
                v_state = returned_sqlstate,
                v_msg = message_text,
                v_detail = pg_exception_detail,
                v_hint = pg_exception_hint,
                v_context = pg_exception_context;

            raise notice E'Got exception:
                state  : %
                message: %
                detail : %
                hint   : %
                context: %', v_state, v_msg, v_detail, v_hint, v_context;

            insert into ilog (state, msg, detail, hint, context) values (v_state, v_msg, v_detail, v_hint, v_context);
    END;
end;
$$;

create or REPLACE function fnc_save_person(p_id bigint, p_first_name varchar, p_last_name varchar)
    returns integer
    language plpgsql
as
$$
declare
    v_state   TEXT;
    v_msg     TEXT;
    v_detail  TEXT;
    v_hint    TEXT;
    v_context TEXT;

BEGIN
    insert into Person_Hist
    select p_id, p_first_name, p_last_name;
    insert into Person
    select p_id, p_first_name, p_last_name;
    return 0;
EXCEPTION
    WHEN OTHERS THEN
        get stacked diagnostics
            v_state = returned_sqlstate,
            v_msg = message_text,
            v_detail = pg_exception_detail,
            v_hint = pg_exception_hint,
            v_context = pg_exception_context;

        raise notice E'Got exception:
                  state  : %
                  message: %
                  detail : %
                  hint   : %
                  context: %', v_state, v_msg, v_detail, v_hint, v_context;

        insert into ilog (state, msg, detail, hint, context) values (v_state, v_msg, v_detail, v_hint, v_context);
        return -1;
END;
$$;



call sp_save_person(12, 'F', 'L');

select *
from ilog;


select *
from Person;

select *
from person_hist;

delete
from person
where id < 100;

delete
from person_hist
where id < 100;

truncate table ilog;
