# 次の13日の金曜日

## サイト名
[次の13日の金曜日](http://next-friday-the-13th.herokuapp.com)（[http://next-friday-the-13th.herokuapp.com](http://next-friday-the-13th.herokuapp.com)）

## これは何？
次の13日の金曜日を知るためのサービスです。

2012年の7月13日（13日の金曜日）に、ジェイソンとJSONを絡めたネタを作りたかったのに、それができなかった作者が、次回の13日の金曜日を逃さず事前に知るために、作られました。

パラメータを利用することで、最大9,999日分まで先の13日の金曜日を知ることができ、大変便利です。

また、JSON、JSONPのAPIも完備しており、最大9,999日分まで先の13日の金曜日を使って、何かマッシュアップしてWebサービスを作りたい方にとっては、大変便利です。

さらに、このサービスはMITライセンスでオープンソースとして公開しております。このソースをカスタマイズして、最大9,999日分まで先の12日の月曜日を知るためのWebサービスなども作ることができ、大変便利です。

## 使い方（パラメータの仕様)

### /
次の13日の金曜日を返す （ctype: text/html）

### /n
n日分の次の13日の金曜日を返す（ctype: text/html）

### /n/json
n日分の次の13日の金曜日のJSONを返す（ctype: application/json）

### /n/jsonp
n日分の次の13日の金曜日のJSONを返す（ctype: application/javascript）

（callback関数にはcallbackが指定されます）

### /n/jsonp?callback=hoge
n日分の次の13日の金曜日のJSONを返す（ctype: application/javascript）

（callback関数にはhogeが指定されます）

## 利用技術
- heroku
- Clojure
- noir

## License
MIT License
