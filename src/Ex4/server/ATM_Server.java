package Ex4.server;
import Ex4.server.PassAccount;

import java.io.*;
import java.net.*;


class ATMServerThread extends Thread {
    private Socket socket;

    public ATMServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            byte[] rbuf = new byte[1024];
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            int len = in.read(rbuf);
            if (len != -1) {
                String accountInfo = new String(rbuf, 0, len);
                String[] accountParts = accountInfo.split(":");
                String accountName = accountParts[0];
                String accountNumber = accountParts[1];

                // ファイルから口座情報を読み込む
                File file = new File("accounts/" + accountNumber + ".txt");
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                
                if (!file.exists()) {
                    out.write("NEW".getBytes());
                    out.flush();
                
                    len = in.read(rbuf);
                    if (len != -1) {
                        String newAccountInfo = new String(rbuf, 0, len);
                        String[] newAccountParts = newAccountInfo.split(":");
                        String newAccountName = newAccountParts[0];
                        String newAccountPassword = newAccountParts[1];
                
                        PassAccount newAccount = new PassAccount("銀行名", "支店名", newAccountName, 0, newAccountPassword, accountNumber);
                        ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(file));
                        objectOut.writeObject(newAccount);
                        objectOut.close();
                
                        out.write("OK".getBytes());
                        out.flush();
                    }
                } else {
                    ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(file));
                    PassAccount account = (PassAccount) objectIn.readObject();
                    objectIn.close();

                    out.write("OK".getBytes());
                    out.flush();

                    // サービス処理
                    while(true){
                    len = in.read(rbuf);
                    if (len != -1) {
                        String service = new String(rbuf, 0, len);
                        if (service.equals("1")) {
                            len = in.read(rbuf);
                            if (len != -1) {
                                int depositAmount = Integer.parseInt(new String(rbuf, 0, len));
                                account.deposit(depositAmount);
                                System.out.println("預金処理完了: " + depositAmount + "円");
                            }
                        } else if (service.equals("2")) {
                            len = in.read(rbuf);
                            if (len != -1) {
                                int withdrawAmount = Integer.parseInt(new String(rbuf, 0, len));
                                account.draw(withdrawAmount);
                                System.out.println("引き出し処理完了: " + withdrawAmount + "円");
                            }
                        } else if (service.equals("3")) {
                            out.write(String.valueOf(account.getBalance()).getBytes());
                            out.flush();
                        } else if (service.equals("4")) {
                            out.write(account.getHistory().getBytes());
                            out.flush();
                        } else if (service.equals("5")) {
                            len = in.read(rbuf);
                            if (len != -1) {
                                String transferInfo = new String(rbuf, 0, len);
                                String[] transferParts = transferInfo.split(":");
                                String targetAccountNumber = transferParts[0];
                                int transferAmount = Integer.parseInt(transferParts[1]);

                                // 他口座への振込処理
                                File targetFile = new File("accounts/" + targetAccountNumber + ".txt");
                                if (targetFile.exists()) {
                                    ObjectInputStream targetObjectIn = new ObjectInputStream(new FileInputStream(targetFile));
                                    PassAccount targetAccount = (PassAccount) targetObjectIn.readObject();
                                    targetObjectIn.close();

                                    account.transfer(targetAccount, transferAmount);
                                    System.out.println("振込処理完了: " + transferAmount + "円");
                                    out.write("振込完了".getBytes());
                                    out.flush();
                                } else {
                                    System.out.println("振込先口座が存在しません。");
                                }
                            }
                        } else if (service.equals("6")) {
                            System.out.println("ATMサービスを終了します。");
                            socket.close();
                            return;
                        }
                    }
                    

                    // 口座情報を保存
                    ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(file));
                    objectOut.writeObject(account);
                    objectOut.close();
                }
                }
            }

            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class ATM_Server {
    public static void main(String[] args) {
        try {
            int PORT = 49152;
            ServerSocket server = new ServerSocket(PORT);
            while (true) {
                Socket socket = server.accept();
                ATMServerThread thread = new ATMServerThread(socket);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}