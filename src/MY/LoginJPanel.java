package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginJPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    protected JButton B_Login;
    protected JTextField tf_user;
    protected JPasswordField pf_password;
    protected Connection dbConn;
    static String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
    static String userName = "sa";
    static String userPwd = "llj2208long";

    public LoginJPanel() throws SQLException, ClassNotFoundException {
        this.setName("登录");
        try {
            Class.forName(driverName);
            this.dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("Succeed!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed!");
        }
        //设置窗口大小
        this.setSize(900, 507);

        //先将布局管理器置为null
        this.setLayout(null);

        //添加标签【学生管理系统】
        JLabel t_Supermarket = new JLabel("超市进销存管理系统");//创建一个标签,并命名为“超市进销存管理系统“
        t_Supermarket.setForeground(new Color(0x0010FF));//设置字体颜色
        t_Supermarket.setFont(new Font("黑体", Font.PLAIN, 50));//设置字体大小
        t_Supermarket.setBounds(280, 50, 800, 100);//设置标签的绝对位置
        this.add(t_Supermarket);//向框架中添加组件【标签(学生管理系统)】

        //添加标签【用户名】
        JLabel t_User = new JLabel("用户名:");
        t_User.setForeground(new Color(0xFF0000));
        t_User.setFont(new Font("黑体", Font.PLAIN, 30));
        t_User.setBounds(200, 140, 200, 100);
        this.add(t_User);

        //添加输入框【用户名输入框】
        tf_user = new JTextField(20);
        tf_user.setFont(new Font("黑体", Font.PLAIN, 18));
        tf_user.setSelectedTextColor(new Color(0xFF0000));
        tf_user.setBounds(330, 170, 280, 40);
        this.add(tf_user);

        //添加标签【密码】
        JLabel t_Password = new JLabel("密码  :");
        t_Password.setForeground(new Color(0xFF0000));
        t_Password.setFont(new Font("黑体", Font.PLAIN, 30));
        t_Password.setBounds(200, 200, 200, 100);
        this.add(t_Password);

        //添加密码输入框【密码】
        pf_password = new JPasswordField(20);
        pf_password.setBounds(330, 230, 280, 40);
        this.add(pf_password);

        //添加按钮【登录】
        B_Login = new JButton("登录");
        B_Login.setForeground(new Color(0x023BF6));
        B_Login.setBackground(new Color(0x38FF00));
        B_Login.setFont(new Font("黑体", Font.PLAIN, 20));
        B_Login.setBorderPainted(false);
        B_Login.setBounds(300, 330, 100, 50);
        B_Login.addActionListener(this);
        this.add(B_Login);

        //设置相对位置：屏幕中间
        //this.setLocationRelativeTo(null);

        //确保使用窗口关闭按钮，能够正常退出，结束进程！
        //this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //禁止对窗口大小进行缩放处理
        //this.setResizable(true);

        //设置可见
        this.setVisible(true);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //new LoginJPanel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(B_Login)){
            try {
                Login();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void Login() throws SQLException {
        Statement stmt = this.dbConn.createStatement();
        String account = tf_user.getText();
        String password = new String(pf_password.getPassword());
        String sql = "select * from Employee where Employee_ID='" + account + "' and Employee_Epassword='" + password + "'";
        ResultSet resultSet= stmt.executeQuery(sql);
        this.setLayout(new BorderLayout());

        if(resultSet.next()){
            switch (resultSet.getString(4)){
                case "A":
                    this.removeAll();
                    //this.repaint();
                    this.setName("管理员工作界面");
                    this.add(new AdminJPanel(account),BorderLayout.CENTER);
                    System.out.println(000);
                    this.revalidate();
                    this.repaint();
                    break;
                case "B":
                    this.removeAll();
                    //this.repaint();
                    this.setName("收银员工作界面");
                    this.add(new CashierJPanel(account),BorderLayout.CENTER);
                    System.out.println(000);
                    this.revalidate();
                    this.repaint();
                    break;
                case "C":
                    this.removeAll();
                    //this.repaint();
                    this.setName("仓库管理员工作界面");
                    this.add(new StorerJPanel(account),BorderLayout.CENTER);
                    System.out.println(000);
                    this.revalidate();
                    this.repaint();
                    break;
                case "D":
                    this.removeAll();
                    //this.repaint();
                    this.setName("商品管理员工作界面");
                    this.add(new GMJPanel(account),BorderLayout.CENTER);
                    System.out.println(000);
                    this.revalidate();
                    this.repaint();
                    break;
                case "Z":break;
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "用户名或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
