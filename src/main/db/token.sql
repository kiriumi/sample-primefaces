CREATE TABLE public.token(
        token TEXT NOT NULL
        ,createdtime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ,updatedtime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ,CONSTRAINT token_pkey PRIMARY KEY (token)
    );

COMMENT ON TABLE public.token IS 'トークン';
COMMENT ON COLUMN public.token.token IS 'トークン';
COMMENT ON COLUMN public.token.createdtime IS '作成日時';
COMMENT ON COLUMN public.token.updatedtime IS '更新日時';
