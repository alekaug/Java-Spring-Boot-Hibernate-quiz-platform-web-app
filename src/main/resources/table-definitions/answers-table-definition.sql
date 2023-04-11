CREATE TABLE public.answers
(
    a_id serial primary key,
    q_id integer,
    answer_content character varying NOT NULL,
    is_correct boolean NOT NULL,
    CONSTRAINT fk_question
        FOREIGN KEY(q_id)
            REFERENCES public.questions(q_id)
);

ALTER TABLE IF EXISTS public.answers
    OWNER to postgres;

COMMENT ON COLUMN public.answers.a_id
    IS 'Answer''s id';

COMMENT ON COLUMN public.answers.q_id
    IS 'Question''s id the answer relates to';

COMMENT ON COLUMN public.answers.answer_content
    IS 'Answer''s content';

COMMENT ON COLUMN public.answers.is_correct
    IS 'Is the answer correct value';