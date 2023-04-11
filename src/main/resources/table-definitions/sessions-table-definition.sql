-- TODO: Finish defining the table
CREATE TABLE public.sessions
(
    s_id serial primary key,
    username character varying NOT NULL,
    time_started timestamp,
    time_finished timestamp
);

ALTER TABLE IF EXISTS public.sessions
    OWNER to postgres;

COMMENT ON COLUMN public.sessions.s_id
    IS 'Session id';

COMMENT ON COLUMN public.sessions.username
    IS 'Session user name that owns it';

COMMENT ON COLUMN public.sessions.time_started
    IS 'Time, when user started the approach';

COMMENT ON COLUMN public.sessions.time_finished
    IS 'Time, when user finished the approach';