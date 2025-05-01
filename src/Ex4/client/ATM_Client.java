package Ex4.client;
import java.io.*;
import java.net.*;
import java.util.*;

public class ATM_Client {
    public static void main(String[] args) {
        int PORT = 49152; // サーバ側のポート番号
        String IP = "127.0.0.1"; // サーバ側のIPアドレス
        byte[] rbuf = new byte[1024]; // 受信データを入れるバッファ
        int len = 0; // 受信データの長さを格納する変数

        try {
            Scanner sc = new Scanner(System.in);
            InetAddress IP_addr = InetAddress.getByName(IP);
            Socket socket = new Socket(IP_addr, PORT);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            // 口座名と口座番号を送信
            System.out.println("あなたの口座名を入力してください:");
            String accountName = sc.nextLine();
            System.out.println("あなたの口座番号を入力してください:");
            String accountNumber = sc.nextLine();
            String accountInfo = accountName + ":" + accountNumber;
            out.write(accountInfo.getBytes());
            out.flush();

            // サーバからの応答を確認
            len = in.read(rbuf);
            if (len != -1) {
                String msg = new String(rbuf, 0, len);
                if (msg.equals("NEW")) {
                    System.out.println("口座名が存在しません。新規口座を作成しますか？(y/n)");
                    String createAccount = sc.nextLine();
                    if (createAccount.equals("y")) {
                        System.out.println("口座人名とパスワードを設定してください");
                        System.out.println("口座人名:");
                        String accountName2 = sc.nextLine();
                        System.out.println("パスワード:");
                        String accountPassword = sc.nextLine();
                        String newAccountInfo = accountName2 + ":" + accountPassword;
                        out.write(newAccountInfo.getBytes());
                        out.flush();

                        len = in.read(rbuf);
                        if (len != -1) {
                            String msg2 = new String(rbuf, 0, len);
                            if (msg2.equals("OK")) {
                                System.out.println("新規口座作成が完了しました。");
                            } else {
                                System.out.println("新規口座作成に失敗しました。");
                                socket.close();
                                return;
                            }
                        }
                    } else {
                        System.out.println("新規口座作成をキャンセルしました。");
                        socket.close();
                        return;
                    }
                } else if (!msg.equals("OK")) {
                    System.out.println("受信確認のメッセージがOKでありません。");
                    socket.close();
                    return;
                }
            }

            System.out.println("ATMサービスにようこそ!");
            System.out.println("以下のサービスから選択してください:");

            while(true){
            // ATMサービスメニュー
            System.out.println("1.預金");
            System.out.println("2.引き出し");
            System.out.println("3.残高照会");
            System.out.println("4.取引履歴照会");
            System.out.println("5.他口座への振込");
            System.out.println("6.終了");

            String service = sc.nextLine();
            out.write(service.getBytes());
            out.flush();

            // サービスごとの処理
            if (service.equals("1")) {
                System.out.println("預金額を入力してください:");
                int depositAmount = Integer.parseInt(sc.nextLine());
                out.write(String.valueOf(depositAmount).getBytes());
                out.flush();
            } else if (service.equals("2")) {
                System.out.println("引き出し額を入力してください:");
                int withdrawAmount = Integer.parseInt(sc.nextLine());
                out.write(String.valueOf(withdrawAmount).getBytes());
                out.flush();
            } else if (service.equals("3")) {
                len = in.read(rbuf);
                if (len != -1) {
                    String balance = new String(rbuf, 0, len);
                    System.out.println("残高: " + balance + "円");
                }
            } else if (service.equals("4")) {
                len = in.read(rbuf);
                if (len != -1) {
                    String history = new String(rbuf, 0, len);
                    System.out.println("取引履歴:\n" + history);
                }
            } else if (service.equals("5")) {
                System.out.println("振込先口座名を入力してください:");
                String transferAccountName = sc.nextLine();
                System.out.println("振込先口座番号を入力してください:");
                String transferAccountNumber = sc.nextLine();
                System.out.println("振込額を入力してください:");
                int transferAmount = Integer.parseInt(sc.nextLine());
                String transferInfo = transferAccountName + ":" + transferAccountNumber + ":" + transferAmount;
                out.write(transferInfo.getBytes());
                out.flush();

                len = in.read(rbuf);
                if (len != -1) {
                    String transferResult = new String(rbuf, 0, len);
                    System.out.println(transferResult);
                }
            } else if (service.equals("6")) {
                System.out.println("ATMサービスを終了します。");
                break;
            }
        }
            socket.close();
            sc.close();
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}