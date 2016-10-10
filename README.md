# springboot-twitter-crawler
Spring-bootで作るTwitter Crawler。指定したキーワードを含む日本語のツイートを現時点から過去1週間まで全部取得します。クローラーとしては充分使えると思いますし、Spring-bootのサンプルアプリとしても役に立つと思います。

## Requirement
- Java 8
- Maven 3.0
- Twitter auth
 - application.propertiesに書き込んで下さい。

## Input
- searchlist.txt
 - 検索キーワードを改行区切りで羅列
 - 「//」で始まる行はスキップ

## Output
- 「data」フォルダにタブ区切りテキスト
 - status id
 - キーワード
 - ツイート

## Specification
以下はデフォルトの設定です。ソースコードを弄れば変えられます。

- \r,\n,\t,\s は半角スペースに変換（連続する場合は半角スペース一文字に）
- RTは対象外
- 日本語に限定
- httpを含むツイートは対象外
- usernameにキーワードを含む場合は対象外