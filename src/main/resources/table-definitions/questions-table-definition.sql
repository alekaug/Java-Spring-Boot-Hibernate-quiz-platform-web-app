CREATE TABLE public.questions
(
    q_id serial primary key,
    q_parent_id integer,
    content character varying NOT NULL,
    type boolean NOT NULL,
    CONSTRAINT fk_question
        FOREIGN KEY(q_parent_id)
            REFERENCES public.questions(q_id)
);

ALTER TABLE IF EXISTS public.questions
    OWNER to postgres;

COMMENT ON COLUMN public.questions.q_id
    IS 'Question identifier';

COMMENT ON COLUMN public.questions.q_parent_id
    IS 'Parent question id';

COMMENT ON COLUMN public.questions.content
    IS 'Content of a question';

COMMENT ON COLUMN public.questions.type
    IS 'false = closed, true = opened';