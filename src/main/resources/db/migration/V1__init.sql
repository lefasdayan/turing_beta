-- object: public.debt | type: TABLE --
-- DROP TABLE IF EXISTS public.debt CASCADE;
create TABLE public.debt
(
    id          bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name"      varchar(100) NOT NULL,
    amount      numeric(10, 2) NOT NULL,
    currency_id bigint NOT NULL,
    date_start  date,
    date_due    date,
    contact_id  bigint,
    type_id     bigint,
    CONSTRAINT debt_pk PRIMARY KEY (id)
);
-- ddl-end --
ALTER TABLE public.debt
    OWNER TO postgres;
-- ddl-end --

-- object: public.currency | type: TABLE --
-- DROP TABLE IF EXISTS public.currency CASCADE;
create TABLE public.currency
(
    id               bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name"           varchar(70) NOT NULL,
    course_to_rubble numeric(12, 7) NOT NULL,
    CONSTRAINT currency_pk PRIMARY KEY (id)
);
-- ddl-end --
ALTER TABLE public.currency
    OWNER TO postgres;
-- ddl-end --

-- object: public.account | type: TABLE --
-- DROP TABLE IF EXISTS public.account CASCADE;
create TABLE public.account
(
    id          bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name"      varchar(50) NOT NULL,
    amount      numeric(10, 2) NOT NULL,
    currency_id bigint NOT NULL,
    bank_name   varchar(50),
    CONSTRAINT account_pk PRIMARY KEY (id)
);
-- ddl-end --
ALTER TABLE public.account
    OWNER TO postgres;
-- ddl-end --

-- object: public.debt_type | type: TABLE --
-- DROP TABLE IF EXISTS public.debt_type CASCADE;
create TABLE public.debt_type
(
    id     bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" varchar(100) NOT NULL,
    note   varchar(240),
    CONSTRAINT debt_type_pk PRIMARY KEY (id)
);
-- ddl-end --
ALTER TABLE public.debt_type
    OWNER TO postgres;
-- ddl-end --

-- object: public.contact | type: TABLE --
-- DROP TABLE IF EXISTS public.contact CASCADE;
create TABLE public.contact
(
    id               bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name"           varchar(140) NOT NULL,
    ph_number        varchar(20),
    bank_card_number numeric(16),
    note             varchar(500),
    CONSTRAINT contact_pk PRIMARY KEY (id)
);
-- ddl-end --
COMMENT ON COLUMN public.contact.ph_number IS E'Mobile phone number of contact';
-- ddl-end --
COMMENT ON COLUMN public.contact.bank_card_number IS E'Номер приоритетной банковской карточки';
-- ddl-end --
ALTER TABLE public.contact
    OWNER TO postgres;
-- ddl-end --

-- object: public.transaction | type: TABLE --
-- DROP TABLE IF EXISTS public.transaction CASCADE;
create TABLE public.transaction
(
    id          bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name"      varchar(50) NOT NULL,
    amount      numeric(10, 2) NOT NULL,
    currency_id bigint NOT NULL,
    date_time   date,
    type_id     bigint,
    from_acc_id bigint,
    to_acc_id   bigint,
    CONSTRAINT transaction_pk PRIMARY KEY (id)
);
-- ddl-end --
ALTER TABLE public.transaction
    OWNER TO postgres;
-- ddl-end --

create TABLE public.debt_history
(
    debt_id     bigint NOT NULL,
    transact_id bigint NOT NULL,
    CONSTRAINT debt_history_pk PRIMARY KEY (debt_id, transact_id)
);
-- ddl-end --
ALTER TABLE public.debt_history
    ADD CONSTRAINT debt_fk FOREIGN KEY (debt_id)
        REFERENCES public.debt (id) MATCH FULL
        ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --
ALTER TABLE public.debt_history
    ADD CONSTRAINT transact_fk FOREIGN KEY (transact_id)
        REFERENCES public.transaction (id) MATCH FULL
        ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --
-- object: public.transaction_type | type: TABLE --
-- DROP TABLE IF EXISTS public.transaction_type CASCADE;
create TABLE public.transaction_type
(
    id     bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" varchar(50) NOT NULL,
    note   varchar(240),
    CONSTRAINT transaction_type_pk PRIMARY KEY (id)
);
-- ddl-end --
ALTER TABLE public.transaction_type
    OWNER TO postgres;
-- ddl-end --

-- object: transaction_type_fk | type: CONSTRAINT --
-- ALTER TABLE public.transaction DROP CONSTRAINT IF EXISTS transaction_type_fk CASCADE;
ALTER TABLE public.transaction
    ADD CONSTRAINT transaction_type_fk FOREIGN KEY (type_id)
        REFERENCES public.transaction_type (id) MATCH FULL
        ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: currency_fk | type: CONSTRAINT --
-- ALTER TABLE public.debt DROP CONSTRAINT IF EXISTS currency_fk CASCADE;
ALTER TABLE public.debt
    ADD CONSTRAINT currency_fk FOREIGN KEY (currency_id)
        REFERENCES public.currency (id) MATCH FULL
        ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: currency_fk | type: CONSTRAINT --
-- ALTER TABLE public.account DROP CONSTRAINT IF EXISTS currency_fk CASCADE;
ALTER TABLE public.account
    ADD CONSTRAINT currency_fk FOREIGN KEY (currency_id)
        REFERENCES public.currency (id) MATCH FULL
        ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: account_fk | type: CONSTRAINT --
-- ALTER TABLE public.transaction DROP CONSTRAINT IF EXISTS account_fk CASCADE;
ALTER TABLE public.transaction
    ADD CONSTRAINT account_fk FOREIGN KEY (from_acc_id)
        REFERENCES public.account (id) MATCH FULL
        ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: account_fk1 | type: CONSTRAINT --
-- ALTER TABLE public.transaction DROP CONSTRAINT IF EXISTS account_fk1 CASCADE;
ALTER TABLE public.transaction
    ADD CONSTRAINT account_fk1 FOREIGN KEY (to_acc_id)
        REFERENCES public.account (id) MATCH FULL
        ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: currency_fk | type: CONSTRAINT --
-- ALTER TABLE public.transaction DROP CONSTRAINT IF EXISTS currency_fk CASCADE;
ALTER TABLE public.transaction
    ADD CONSTRAINT currency_fk FOREIGN KEY (currency_id)
        REFERENCES public.currency (id) MATCH FULL
        ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: contact_fk | type: CONSTRAINT --
-- ALTER TABLE public.debt DROP CONSTRAINT IF EXISTS contact_fk CASCADE;
ALTER TABLE public.debt
    ADD CONSTRAINT contact_fk FOREIGN KEY (contact_id)
        REFERENCES public.contact (id) MATCH FULL
        ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: debt_type_fk | type: CONSTRAINT --
-- ALTER TABLE public.debt DROP CONSTRAINT IF EXISTS debt_type_fk CASCADE;
ALTER TABLE public.debt
    ADD CONSTRAINT debt_type_fk FOREIGN KEY (type_id)
        REFERENCES public.debt_type (id) MATCH FULL
        ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --


