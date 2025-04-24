package Ex3;

import java.io.*;
import java.net.*;

public class FileRcv {
    public static void main(String[] args) {
        int PORT = 49152; // サーバ側のポート番号
        byte[] rbuf = new byte[1024];
        byte[] sbuf = new byte[1024];

        try {
            String firstFileName = ""; // 最初に受信したファイル名
            String lastFileName = ""; // 最後に受信したファイル名
            ServerSocket server = new ServerSocket(PORT); // PORT番号を指定してサーバソケット作成
            System.out.println("サーバが起動しました。クライアントからの接続を待っています...");
            Socket socket = server.accept(); // 接続待ち、接続されると送受信用ソケット作成

            InetAddress IP_addr = socket.getInetAddress(); // クライアント側のIPアドレス
            System.out.println("クライアントが接続しました: " + IP_addr);

            InputStream in = socket.getInputStream(); // 受信用ストリーム定義
            OutputStream out = socket.getOutputStream(); // 送信用ストリーム定義

            int len = in.read(rbuf); // rbufにデータ受信、長さはlenに設定される(終了時には-1が返る)
            if (len != -1) {
                firstFileName = new String(rbuf, 0, len); // 最初のファイル名を取得
                System.out.println("最初に受信したファイル名: " + firstFileName);

                // FileSndに受信確認のメッセージを送信(OKと送信)
                out.write("OK".getBytes()); // 受信確認のメッセージを送信
                out.flush(); // 送信バッファをフラッシュ
            }

            // 最後のファイル名を受信
            len = in.read(rbuf); // rbufにデータ受信、長さはlenに設定される(終了時には-1が返る)
            if (len != -1) {
                lastFileName = new String(rbuf, 0, len); // 最後のファイル名を取得
                System.out.println("最後に受信したファイル名: " + lastFileName);

                // FileSndに受信確認のメッセージを送信(OKと送信)
                out.write("OK".getBytes()); // 受信確認のメッセージを送信
                out.flush(); // 送信バッファをフラッシュ
            }

            // 最後のファイル名のデータを最初のファイルに出力
            FileOutputStream streamWr = new FileOutputStream(firstFileName, true); // 最初のファイルに追記モードで出力
            len = in.read(sbuf); // sbufにデータ受信、長さはlenに設定される(終了時には-1が返る)
            while (len != -1) { // sbufにデータ受信、長さはlenに設定される(終了時には-1が返る)
                streamWr.write(sbuf, 0, len); // sbufの内容を送信
                len = in.read(sbuf); // sbufにデータ受信、長さはlenに設定される(終了時には-1が返る)
            }

            // リソースをクローズ
            streamWr.close(); // 受信用ストリームクローズ
            in.close(); // 受信用ストリームクローズ
            socket.close();
            server.close(); // サーバソケットクローズ
            System.out.println("ファイル受信が完了しました。");

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