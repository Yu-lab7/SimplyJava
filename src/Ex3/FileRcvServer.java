package Ex3;
import java.io.*;
import java.net.*;

class FileRcvThread extends Thread{
    private Socket socket;
    public FileRcvThread(Socket socket){
        this.socket = socket;
    }
    public void run(){
        try{
            //socketを使用した送受信処理
            byte[] rbuf = new byte[1024]; // 受信データを入れるバッファ定義
            byte[] sbuf = new byte[1024]; // 送信データを入れるバッファ定義

            String firstFileName = ""; // 最初に受信したファイル名
            String lastFileName = ""; // 最後に受信したファイル名

            InputStream in = socket.getInputStream();   // 受信用ストリーム定義
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
            System.out.println("ファイル受信が完了しました。");
        }catch(Exception e){
            e.printStackTrace();  
        }
    }
}

class FileRcvServer{
    public static void main(String[] args){
        try{
            // サーバソケットを作成
            int PORT = 49152; // サーバ側のポート番号
            ServerSocket server = new ServerSocket(PORT); // PORT番号を指定してサーバソケット作成
            while(true){
                Socket socket = server.accept();
                FileRcvThread th1 = new FileRcvThread(socket);
                System.out.println("クライアントが接続をしました。");
                th1.start(); // スレッドを開始
            }
        } catch(SocketException e){
            System.out.println("SocketExceptionが発生しました。");
            e.printStackTrace();
        } catch(IOException e1){
            System.out.println("IOExceptionが発生しました。");
            e1.printStackTrace();
        } catch(Exception e2){
            System.out.println("Exceptionが発生しました。");
            e2.printStackTrace();
        }
    } 
}




