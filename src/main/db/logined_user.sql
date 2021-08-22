CREATE TABLE public.logined_user(
         session_id TEXT
        ,id TEXT NOT NULL
        ,createdtime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ,updatedtime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ,CONSTRAINT logined_user_pkey PRIMARY KEY (session_id)
        ,UNIQUE (id, session_id)
    );
CREATE INDEX ON public.logined_user(id);

COMMENT ON TABLE public.logined_user IS 'ログインユーザ';
COMMENT ON COLUMN public.logined_user.session_id IS 'セッションID';
COMMENT ON COLUMN public.logined_user.id IS 'ID';
COMMENT ON COLUMN public.logined_user.createdtime IS '作成日時';
COMMENT ON COLUMN public.logined_user.updatedtime IS '更新日時';
