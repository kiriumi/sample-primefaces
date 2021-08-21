CREATE TABLE public.user_info(
        id TEXT NOT NULL
        ,password TEXT NOT NULL
        ,email TEXT NOT NULL
        ,email_new TEXT
        ,last_name TEXT
        ,first_name TEXT
        ,postal_code CHAR(7)
        ,region TEXT
        ,locality TEXT
        ,street_address TEXT
        ,extended_address TEXT DEFAULT ''
        ,fail_count INTEGER NOT NULL DEFAULT 0
        ,unlocked_date_time TIMESTAMP DEFAULT '4714-11-24 00:00:00 BC'
        ,locked BOOLEAN NOT NULL DEFAULT false
        ,version INTEGER NOT NULL DEFAULT 1
        ,createdtime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ,updatedtime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ,CONSTRAINT user_info_pkey PRIMARY KEY (id)
    );
CREATE INDEX ON public.user_info(email);
CREATE INDEX ON public.user_info(postal_code);

COMMENT ON TABLE public.user_info IS 'ユーザ情報';
COMMENT ON COLUMN public.user_info.id IS 'ID';
COMMENT ON COLUMN public.user_info.password IS 'パスワード';
COMMENT ON COLUMN public.user_info.email IS 'Eメールアドレス';
COMMENT ON COLUMN public.user_info.email_new IS '新規Eメールアドレス';
COMMENT ON COLUMN public.user_info.last_name IS '姓';
COMMENT ON COLUMN public.user_info.first_name IS '名';
COMMENT ON COLUMN public.user_info.password IS 'パスワード';
COMMENT ON COLUMN public.user_info.postal_code IS '郵便番号';
COMMENT ON COLUMN public.user_info.region IS '都道府県';
COMMENT ON COLUMN public.user_info.locality IS '市区町村';
COMMENT ON COLUMN public.user_info.street_address IS '町域';
COMMENT ON COLUMN public.user_info.extended_address IS 'その他住所';
COMMENT ON COLUMN public.user_info.fail_count IS '認証失敗回数';
COMMENT ON COLUMN public.user_info.unlocked_date_time IS 'アカウント解除日時';
COMMENT ON COLUMN public.user_info.locked IS 'アカウントロックフラグ';
COMMENT ON COLUMN public.user_info.version IS 'バージョン';
COMMENT ON COLUMN public.user_info.createdtime IS '作成日時';
COMMENT ON COLUMN public.user_info.updatedtime IS '更新日時';
