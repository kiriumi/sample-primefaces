CREATE TABLE public.api_token(
        origin_context TEXT NOT NULL
        ,api_token TEXT NOT NULL
        ,CONSTRAINT api_token_pkey PRIMARY KEY (origin_context)
    );
CREATE INDEX ON public.api_token(origin_context);

COMMENT ON TABLE public.api_token IS 'APIトークン';
COMMENT ON COLUMN public.api_token.origin_context IS 'オリジンコンテントルート';
COMMENT ON COLUMN public.api_token.api_token IS 'APIトークン';
