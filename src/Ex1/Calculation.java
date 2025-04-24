package Ex1;
public class Calculation {
    public static void main(String[] args) {
        int goal; // 目標金額
        int depositOfYear; // 毎年預金額
        int years = 0; // 所要年数
        int total_balance; // 預金残額

        try {
            // Account のインスタンス myAccount 作成
            Account myAccount = new Account(args[0], args[1], args[2], Integer.parseInt(args[3]));
            // 引数で指定された目標金額を goal に代入
            goal = Integer.parseInt(args[4]);
            // 引数で指定された毎年預金額を depositOfYear に代入
            depositOfYear = Integer.parseInt(args[5]);

            // 預金残額が目標金額に達しない場合に繰り返す条件
            while (myAccount.getBalance() < goal) {
                myAccount.deposit(depositOfYear);
                years++;
            }

            // 変数 total_balance に預金残額を代入
            total_balance = myAccount.getBalance();

            // 銀行名、支店名、口座所有者、開設金額、目標金額、預金残額、所要年数をそれぞれ改行して標準出力
            System.out.println("銀行名 : " + myAccount.bankName);
            System.out.println("支店名 : " + myAccount.branchName);
            System.out.println("口座所有者 : " + myAccount.accountHolder + "様");
            System.out.println("口座開設金額 : " + Integer.parseInt(args[3]) + "円");
            System.out.println("目標金額 : " + goal + "円");
            System.out.println("年預金残高 : " + depositOfYear + "円");
            System.out.println("所要年数 : " + years + "年");

            // 預金残額 +1 万円を引き出す処理を行い、引出し要求金額、実際の引出し金額、引出し後の預金残額をそれぞれ改行して標準出力
            System.out.println();
            int withdrawRequest = total_balance + 10000;
            int actualWithdrawn = myAccount.draw(withdrawRequest);
            System.out.println("引出し要求金額: " + withdrawRequest + "円");
            System.out.println("実際の引出し金額: " + actualWithdrawn + "円");
            System.out.println("引出し後の預金残高: " + myAccount.getBalance() + "円");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}