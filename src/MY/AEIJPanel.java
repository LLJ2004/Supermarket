package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AEIJPanel extends JPanel implements ActionListener {
    protected JTextField tf_eID,tf_eName,tf_eAge,tf_ePhone,tf_eSalary;
    protected JButton B;
    protected JComboBox<String> combox_job;
    private static String[] job = {"管理员","收银员","仓库管理员","商品管理员","待定职务"};
    protected int cz;
    protected Connection dbConn;
    static String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
    static String userName = "sa";
    static String userPwd = "llj2208long";

    public AEIJPanel(int selected){
        try {
            Class.forName(driverName);
            this.dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("Succeed!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed!");
        }
        cz=selected;
        JPanel p1=new JPanel(new GridLayout(6,2));
        JPanel p2=new JPanel();
        this.setLayout(new BorderLayout());
        this.add(p2,BorderLayout.SOUTH);
        this.add(p1);
        p1.add(new JLabel("职工号",SwingConstants.CENTER),CENTER_ALIGNMENT);
        p1.add(tf_eID=new JTextField());
        p1.add(new JLabel("姓名",SwingConstants.CENTER),CENTER_ALIGNMENT);
        p1.add(tf_eName=new JTextField());
        p1.add(new JLabel("年龄",SwingConstants.CENTER),CENTER_ALIGNMENT);
        p1.add(tf_eAge=new JTextField());
        p1.add(new JLabel("电话",SwingConstants.CENTER),CENTER_ALIGNMENT);
        p1.add(tf_ePhone=new JTextField());
        p1.add(new JLabel("职务",SwingConstants.CENTER),CENTER_ALIGNMENT);
        p1.add(combox_job=new JComboBox<>(job));
        p1.add(new JLabel("工资",SwingConstants.CENTER),CENTER_ALIGNMENT);
        p1.add(tf_eSalary=new JTextField());
        p2.add(B=new JButton());
        if(selected==0){
            B.setText("修改");
            tf_eID.setEditable(false);
            tf_eName.setEditable(false);
            tf_eAge.setEditable(false);
            tf_ePhone.setEditable(false);
            tf_eSalary.setEditable(true);
            cz=0;
        }
        else if(selected==1){
            B.setText("添加");
            tf_eID.setEditable(true);
            tf_eName.setEditable(true);
            tf_eAge.setEditable(true);
            tf_ePhone.setEditable(true);
            tf_eSalary.setEditable(true);
            cz=1;
        }
        B.addActionListener(this);
    }

    public static void main(String[] args) {
        JFrame jf=new JFrame();
        jf.setBounds(300,300,200,600);
        jf.add(new AEIJPanel(1));
        jf.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(B)&&cz==0){
            String sql = "UPDATE Employee SET Job_ID=?,Employee_Salary=? WHERE Employee_ID=?";
            int JID=combox_job.getSelectedIndex();
            String Jid=null;
            switch (JID){
                case 0:
                    Jid="A";
                    break;
                case 1:
                    Jid="B";
                    break;
                case 2:
                    Jid="C";
                    break;
                case 3:
                    Jid="D";
                    break;
                case 4:
                    Jid="Z";
                    break;
            }
            PreparedStatement statement = null;
            PreparedStatement stmt = null;
            try {
                statement = dbConn.prepareStatement(sql);
                statement.setString(1, Jid) ;
                statement.setDouble(2, Double.parseDouble(tf_eSalary.getText()));
                statement.setString(3, tf_eID.getText());
                statement.execute();
                JOptionPane.showMessageDialog(this,"修改成功");

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSource().equals(B)&&cz==1){
            String sql = "INSERT INTO Employee (Employee_ID, Employee_Name,Employee_Salary,Job_ID,Employee_Phone) VALUES (?, ?,?,?,?)";
            PreparedStatement statement = null;
            try {
                statement = dbConn.prepareStatement(sql);
                statement.setString(1, tf_eID.getText()) ;
                statement.setString(2, tf_eName.getText());
                statement.setString(3, tf_eSalary.getText());

                int JID=combox_job.getSelectedIndex();
                String Jid=null;
                switch (JID){
                    case 0:
                        Jid="A";
                        break;
                    case 1:
                        Jid="B";
                        break;
                    case 2:
                        Jid="C";
                        break;
                    case 3:
                        Jid="D";
                        break;
                    case 4:
                        Jid="Z";
                        break;
                }
                statement.setString(4, Jid);
                statement.setString(5,tf_ePhone.getText());
                statement.execute();
                JOptionPane.showMessageDialog(this,"添加成功");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
