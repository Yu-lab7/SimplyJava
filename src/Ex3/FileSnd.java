package Ex3;

import java.io.*;
import java.net.*;

public class FileSnd {
    public static void main(String[] args) throws IOException {
        int PORT = 49152; // サーバ側のポート番号
        byte[] sbuf = new byte[1024]; // 送信データを入れるバッファ定義
        String IP = "127.0.0.1"; // サーバ側のIPアドレス、今回は内部折り返しを指定
        int len = 0; // 受信データの長さを格納する変数

        try {
            InetAddress IP_addr = InetAddress.getByName(IP); // IPアドレスを設定
            Socket socket = new Socket(IP_addr, PORT); // ソケット作成(コネクション設定)
            OutputStream out = socket.getOutputStream(); // 送信用ストリーム定義
            InputStream in = socket.getInputStream(); // 受信用ストリーム定義
            byte[] rbuf = new byte[1024]; // 受信データを入れるバッファ定義

            // 最初のファイル名を送信
            String firstFileName = args[0].substring(args[0].lastIndexOf("\\") + 1); // 最初のファイル名を取得
            out.write(firstFileName.getBytes()); // 最初のファイル名を送信
            out.flush(); // 送信バッファをフラッシュ

            // サーバからの応答を確認
            len = in.read(rbuf); // サーバからの応答を受信
            if (len != -1) {
                String msg = new String(rbuf, 0, len); // 受信したメッセージを取得
                if (!msg.equals("OK")) { // 応答がOKでない場合
                    System.out.println("受信確認のメッセージがOKでありません。");
                    socket.close();
                    return;
                }
            }

            // 最後のファイル名を送信
            String lastFileName = args[1].substring(args[1].lastIndexOf("\\") + 1); // 最後のファイル名を取得
            out.write(lastFileName.getBytes()); // 最後のファイル名を送信
            out.flush(); // 送信バッファをフラッシュ

            // サーバからの応答を確認
            len = in.read(rbuf); // サーバからの応答を受信
            if (len != -1) {
                String msg = new String(rbuf, 0, len); // 受信したメッセージを取得
                if (!msg.equals("OK")) { // 応答がOKでない場合
                    System.out.println("受信確認のメッセージがOKでありません。");
                    socket.close();
                    return;
                }
            }

            // 最後のファイルのデータを送信
            FileInputStream streamRd = new FileInputStream(args[1]); // 最後のファイルを開く
            while ((len = streamRd.read(sbuf)) != -1) { // ファイルデータを読み込む
                out.write(sbuf, 0, len); // データを送信
            }

            // リソースをクローズ
            streamRd.close();
            out.flush(); // 送信バッファをフラッシュ
            out.close(); // 送信用ストリームクローズ
            socket.close();

            System.out.println("ファイル送信が完了しました。");

        } catch (SocketException e) {
            System.err.println("ソケットエラーが発生しました: " + e.getMessage());
            e.printStackTrace(); // ソケットエラー時の処理
        } catch (IOException e1) {
            System.err.println("IOエラーが発生しました: " + e1.getMessage());
            e1.printStackTrace(); // IOエラー時の処理
        } catch (Exception e2) {
            System.err.println("その他のエラーが発生しました: " + e2.getMessage());
            e2.printStackTrace(); // その他のエラー時の処理
        }
    }
}