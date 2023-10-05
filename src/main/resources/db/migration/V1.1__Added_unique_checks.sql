ALTER TABLE public.currency
    ADD UNIQUE ("name");
-- ddl-end --

ALTER TABLE public.account
    ADD UNIQUE ("name");
-- ddl-end --

ALTER TABLE public.contact
    ADD UNIQUE ("name");
-- ddl-end --

ALTER TABLE public.transaction_type
    ADD UNIQUE ("name");
-- ddl-end --