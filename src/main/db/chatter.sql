CREATE TABLE public.chatter(
        group_id TEXT NOT NULL
        ,user_id TEXT NOT NULL
        ,message TEXT NOT NULL
        ,createdtime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ,updatedtime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );
CREATE INDEX ON public.chatter(group_id);

COMMENT ON TABLE public.chatter IS 'チャット';
COMMENT ON COLUMN public.chatter.group_id IS 'グループID';
COMMENT ON COLUMN public.chatter.user_id IS 'ユーザID';
COMMENT ON COLUMN public.chatter.message IS 'メッセージ';
COMMENT ON COLUMN public.chatter.createdtime IS '作成日時';
COMMENT ON COLUMN public.chatter.updatedtime IS '更新日時';
