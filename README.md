# 概要
* PrimeFacesを使用したWebアプリケーションのサンプル
* Webアプリケーションのアーキテクチャとして必要な機能を実装
* セキュリティ・認証に必要な機能を実装

# 環境
## 言語
* Java SE 11
* Jakarta EE 8
## Webアプリケーション
* JSF 2.3
* PrimeFaces 10.0.0
## APサーバ
* WildFly 24.0.1 Final
## DBアクセス
* MyBatis 3.x（+ MyBatis CDI 1.1.1）
## DB
Postgres 11.x
## ログ
* Log4j2
## ビルド
* Gradle 6.8
# 機能
## Webアプリケーション
* 入力チェック
* メッセージ管理
* ダブルクリック抑止
* ショートカットキーのカスタマイズ
* CSRFトークンチェック
* カレンダーによる日付入力
* 郵便番号による住所検索
* 子画面の開閉

# セキュリティ
### 基礎
* POSTメソッドの強制（PRG）
* 文字エンコーディングの統一（UTF-8）
* 危険な文字の入力チェック
### XXS
* レスポンスの文字エンコーディングを強制的に指定（Content-Type: text/html; charset=UTF-8）
* 自動HTMLエスケープ
* StringEscapeUtilsによるJavaScriptのエスケープ
* セッションクッキーにHttpOnly属性を設定
* X-XSS-Protection
* Content-Security-Policy
### SQLインジェクション
* 静的プレースホルダの使用（MyBatis）
### CSRF
* トークンチェック
### クリックジャッキング
* X-Frame-Options
### セッション
* JakartaEEの機能でセッションIDを発行
* URLにセッションを埋め込まない（tracking-modeをCOOKIE）
* 認証前はリクエスト毎にセッションIDを変更
HTTPヘッダ・インジェクション
* JakartaEEの機能でHTTPヘッダを設定
### アップロードファイル
* 拡張子のチェック
* アップロードファイルのContent-Typeチェック
* ファイル名の長さチェック
* ファイルサイズチェック
* ファイル名の重複防止
### ダウンロードファイル
* X-Content-Type-Optnion
* POSTメソッドでダウンロード
### ブラウザキャッシュ
* Cache-Control
* Pragma: no-cach

* HTMLタグの属性値を「"」で囲むよう強制

# 認証
## ログイン
* パスワードポリシーチェック
* アカウントロック
* 遅いハッシュ関数の使用
* 自動ログイン
* 多重ログイン制御
* ログアウト
### アカウント
* ユーザ登録
* アカウント停止
