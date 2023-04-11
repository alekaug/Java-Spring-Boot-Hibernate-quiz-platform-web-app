-- TODO: Finish defining table
CREATE TABLE public.tries
(
    t_id serial primary key,
    q_id integer,
    s_id integer,
    try_content json,
    CONSTRAINT fk_question
        FOREIGN KEY(q_id)
            REFERENCES public.questions(q_id),
    CONSTRAINT fk_session
        FOREIGN KEY(s_id)
            REFERENCES public.sessions(s_id)
);

ALTER TABLE IF EXISTS public.tries
    OWNER to postgres;

COMMENT ON COLUMN public.tries.t_id
    IS 'Try id';

COMMENT ON COLUMN public.tries.q_id
    IS 'Try question''s id the answer relates to';

COMMENT ON COLUMN public.tries.s_id
    IS 'Try session''s id the answer relates to';

COMMENT ON COLUMN public.tries.try_content
    IS 'Try''s content aka how user answered to the particular question';