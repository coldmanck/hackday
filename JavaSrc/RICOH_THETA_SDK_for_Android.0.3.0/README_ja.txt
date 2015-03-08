==================================================
 README for "RICOH THETA SDK for Android"

 Version :0.3.0
==================================================

このファイルはRICOH THETA SDK for Androidに関する説明文書です。
ricoh-theta4jとr-exif4jはAndroid用のアプリケーションを作成するためのライブラリです。
ricoh-theta-sample-for-androidは上記のライブラリを利用したサンプルアプリケーションです。

----------------------------------------

* この文書に含まれる内容

    * 利用規約
    * アーカイブに含まれているファイルに関して
    * 開発に必要な環境に関して
    * 使い方に関して
    * 最新の情報に関して
    * トラブルシューティング
    * 商標について
    * 更新履歴

----------------------------------------

* 利用規約

    同梱されている、LICENSE.txt(LICENSE_ja.txt)に記載されています。
    RICOH THETA SDKをご使用になられた時点で、この規約に合意したものとみなします。

----------------------------------------

* アーカイブに含まれているファイルに関して

    README.txt                           ：このファイルです(英語)
    README_ja.txt                        ：このファイルです(日本語)
    LICENSE.txt                          ：規約に関するファイルです(英語)
    LICENSE_ja.txt                       ：規約に関するファイルです(日本語)
    ricoh-theta-sample-for-android
    ┣ libs                              ：サンプルプロジェクト用ライブラリです
    ┣ res                               ：サンプルプロジェクト用リソースファイルです
    ┣ src                               ：サンプルアプリケーションのソースです
    ┗ doc                               ：サンプルアプリケーションのJavadocです
    lib
    ┣ bin
    ┃  ┣ ricoh-theta4j.0.2.0.jar       ：RICOH THETAの操作に関するライブラリです
    ┃  ┗ r-exif4j.0.2.0.jar            ：RICOH THETAで撮影した全天球イメージのEXIF情報の取得ができるライブラリです
    ┣ src
    ┃  ┣ ricoh-theta4j                 ：ricoh-theta4jのソースです
    ┃  ┗ r-exif4j                      ：r-exif4jのソースです
    ┗ doc
       ┣ ricoh-theta4j                  ：ricoh-theta4jのJavadocです
       ┗ r-exif4j                       ：r-exif4jのJavadocです

----------------------------------------

* 開発に必要な環境に関して

    [ RICOH THETAについて ]
      以下の条件を満たすRICOH THETA専用のライブラリです。

      * ハードウェア
          RICOH THETA (2013年発売モデル)
          RICOH THETA (型番：RICOH THETA m15)
      * ファームウェア
          バージョン 1.21 以上
          (ファームウェアの確認およびアップデート方法はこちらです： https://theta360.com/ja/support/manual/content/pc/pc_05.html )


    [ サンプルアプリケーションの開発環境について ]
      サンプルアプリケーションは以下の条件で動作確認済みです。

      * 動作確認環境
          Nexus 5 
      * 開発・ビルド環境
          Eclipse Luna
          Android SDK (API Level 19)

----------------------------------------

* 使い方に関して

    [ サンプルアプリケーションを動かす場合 ]
        1. ricoh-theta-sample-for-android.0.2.0をプロジェクトとしてEclipseにインポートし、Android端末にビルドしてください
        2. RICOH THETAをAndroid端末とWi-Fiで接続してください
            (使用説明書、カメラとスマートフォンを接続する：https://theta360.com/ja/support/manual/content/prepare/prepare_06.html)
        3. サンプルプログラムの内、 ビューア部はOpenGL ES2.0を使用して作成しています
           動作に際して対応した実機を使用してください
        4. サンプルアプリケーションを操作する事ができます

    [ 自分のアプリケーションでRICOH THETA SDKを利用する場合 ]
        1. 自分のアプリケーションにricoh-theta4j.0.2.0.jarとr-exif4j.0.2.0.jarのクラスパスを通してください
        2. サンプルアプリケーションや後述の情報をもとに実装をしてください

    [ より詳しい情報に関して ]
        同梱されているライブラリのソースやJavadoc、およびWEB上のドキュメントをご確認ください。

        https://developers.theta360.com/ja/docs/

----------------------------------------

* 最新の情報に関して

    最新の情報はWEBサイト「RICOH THETA Developers」にて公開されています。

    https://developers.theta360.com/

----------------------------------------

* トラブルシューティング

    よくある質問についてはこちらのフォーラム上にまとめてあります。

    https://developers.theta360.com/ja/forums/viewforum.php?f=5

----------------------------------------

* 商標について

    本文書に記載されている商品・サービス名は、各社の商標または登録商標です。

    * Android、Nexusは、Google Inc.の商標または登録商標です
    * Eclipseは、Eclipse Foundation, Inc.の米国およびその他の国における商標または登録商標です
    * Wi-Fiは、Wi-Fi Allianceの商標です

    その他商標は全て、それぞれの所有者に帰属します。

----------------------------------------

* 更新履歴

    2015/02/20 0.3.0 サンプルアプリに全天球ビューア機能を追加
    2014/11/06 0.2.0 英語訳反映
    2014/10/28 0.1.0 初回リリース
