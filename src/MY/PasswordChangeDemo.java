package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Arrays;

public class PasswordChangeDemo extends JFrame{
    protected JPasswordField ps1,ps2,ps3;
    protected Connection dbConn;
    protected String eID;
    static String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
    static String userName = "sa";
    static String userPwd = "llj2208long";
    public PasswordChangeDemo(String eid){
        super("修改密码");

        try {
            Class.forName(driverName);
            this.dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("Succeed!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed!");
        }

        eID=eid;

        JPanel panel=new JPanel(new GridLayout(3,1));
        this.add(panel);
        panel.add(new JLabel("请输入原密码:"));
        panel.add(ps1=new JPasswordField(),CENTER_ALIGNMENT);
        JButton B_p=new JButton("确定");
        panel.add(B_p,CENTER_ALIGNMENT);
        B_p.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Statement stmt = dbConn.createStatement();
                    String sql = "select Employee_Epassword from Employee where Employee_ID='" + eID + "'";
                    ResultSet resultSet= null;
                    resultSet = stmt.executeQuery(sql);
                    if(resultSet.next()){
                        if(String.valueOf(ps1.getPassword()).equals(resultSet.getString(1))){
                            panel.removeAll();
                            JPanel p1,p2;
                            JButton B_ap;

                            panel.setLayout(new BorderLayout());
                            panel.add(p2=new JPanel(),BorderLayout.SOUTH);
                            panel.add(p1=new JPanel(new GridLayout(2,2)));
                            p1.add(new JLabel("输入新密码"));
                            p1.add(ps2=new JPasswordField());
                            p1.add(new JLabel("确定新密码"));
                            p1.add(ps3=new JPasswordField());
                            p2.add(B_ap=new JButton("修改密码"));
                            panel.validate();
                            panel.repaint();

                            B_ap.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if(Arrays.equals(ps2.getPassword(), ps3.getPassword())){
                                        String sql = "UPDATE Employee SET Employee_Epassword=? WHERE Employee_ID=?";
                                        PreparedStatement statement = null;
                                        PreparedStatement stmt = null;
                                        try {
                                            statement = dbConn.prepareStatement(sql);
                                            statement.setString(1, String.valueOf(ps2.getPassword())) ;
                                            statement.setString(2, eID);
                                            statement.execute();
                                            JOptionPane.showMessageDialog(panel,"修改成功");
                                        } catch (SQLException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }
                                    else {
                                        JOptionPane.showMessageDialog(panel,"新密码前后不一致，请重新输入");
                                    }
                                }
                            });
                        }
                        else{
                            JOptionPane.showMessageDialog(panel,"密码错误，请重试");
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "系统错误，请上报管理员", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });
        this.setBounds(300,330,100,50);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new PasswordChangeDemo("A0001");
    }
}