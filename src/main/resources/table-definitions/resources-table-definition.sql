CREATE TABLE public.resources
(
    r_id serial primary key,
    q_id integer,
    url character varying NOT NULL,
    resource_type integer NOT NULL,
    CONSTRAINT fk_question
        FOREIGN KEY(q_id)
            REFERENCES public.questions(q_id)
);

ALTER TABLE IF EXISTS public.resources
    OWNER to postgres;

COMMENT ON COLUMN public.resources.q_id
    IS 'Question the resource is related to';

COMMENT ON COLUMN public.resources.url
    IS 'Resource''s url';

COMMENT ON COLUMN public.resources.resource_type
    IS '0 = audio, 1 = video, 2 = image';