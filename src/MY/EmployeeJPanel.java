package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EmployeeJPanel extends JPanel implements ActionListener {
    protected JTextField tf_eID,tf_eName,tf_eAge,tf_ePhone,tf_eJob,tf_eSalary;
    protected JTextArea ta_info;
    protected JButton B_alter,B_password;
    protected String eID;
    protected Connection dbConn;
    static String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
    static String userName = "sa";
    static String userPwd = "llj2208long";

    public EmployeeJPanel(String eid) throws SQLException {
        try {
            Class.forName(driverName);
            this.dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("Succeed!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed!");
        }
        eID=eid;
        ta_info=new JTextArea();

        this.setLayout(new BorderLayout());
        JPanel p1=new JPanel(new BorderLayout());
        JPanel p2=new JPanel(new GridLayout(3,4));
        JScrollPane sp=new JScrollPane(ta_info);
        JPanel p3=new JPanel(new GridLayout(1,2));

        this.add(p1,BorderLayout.CENTER);
        this.add(p3,BorderLayout.SOUTH);
        p1.add(p2,BorderLayout.CENTER);
        p1.add(sp,BorderLayout.SOUTH);
        p2.add(new JLabel("职工号"),CENTER_ALIGNMENT);
        p2.add(tf_eID=new JTextField());
        p2.add(new JLabel("姓名"),CENTER_ALIGNMENT);
        p2.add(tf_eName=new JTextField());
        p2.add(new JLabel("年龄"),CENTER_ALIGNMENT);
        p2.add(tf_eAge=new JTextField());
        p2.add(new JLabel("电话"),CENTER_ALIGNMENT);
        p2.add(tf_ePhone=new JTextField());
        p2.add(new JLabel("职务"),CENTER_ALIGNMENT);
        p2.add(tf_eJob=new JTextField());
        p2.add(new JLabel("工资"),CENTER_ALIGNMENT);
        p2.add(tf_eSalary=new JTextField());
        p3.add(B_alter=new JButton("修改信息"));
        p3.add(B_password=new JButton("修改密码"));
        B_alter.addActionListener(this);
        B_password.addActionListener(this);

        Statement stmt = this.dbConn.createStatement();
        String sql = "select Employee_ID,Employee_Name,Employee_Salary,Employee_Phone,Employee_Epassword,Employee_Age,Job_Name,Job_Info,Job_Permissions from Employee,Job where Employee_ID='" + eID + "' and Job.Job_ID=Employee.Job_ID";
        ResultSet resultSet= stmt.executeQuery(sql);

        if(resultSet.next()){
            tf_eID.setText(resultSet.getString(1));
            tf_eName.setText(resultSet.getString(2));
            tf_eAge.setText(resultSet.getString(6));
            tf_eJob.setText(resultSet.getString(7));
            tf_ePhone.setText(resultSet.getString(4));
            tf_eSalary.setText(resultSet.getString(3));
            ta_info.setText("职责描述:\n"+resultSet.getString(8)+"\n职务权限:\n"+resultSet.getString(9));
        }
        else{
            JOptionPane.showMessageDialog(null, "系统错误，请上报管理员", "错误", JOptionPane.ERROR_MESSAGE);
        }
        tf_eID.setEditable(false);
        tf_eName.setEditable(false);
        tf_eAge.setEditable(false);
        tf_ePhone.setEditable(false);
        tf_eJob.setEditable(false);
        tf_eSalary.setEditable(false);
        ta_info.setEditable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(B_alter)){
            JFrame jf=new JFrame();
            EmployeeInfoJPanel ei=new EmployeeInfoJPanel();
            JButton B_ae=new JButton("修改信息");

            jf.setLayout(new BorderLayout());
            jf.add(B_ae,BorderLayout.SOUTH);
            jf.add(ei);
            jf.setVisible(true);

            Statement stmt = null;
            try {
                stmt = this.dbConn.createStatement();
                String sql = "select Employee_ID,Employee_Name,Employee_Salary,Employee_Phone,Employee_Epassword,Employee_Age,Job_Name,Job_Info,Job_Permissions from Employee,Job where Employee_ID='" + eID + "'";
                ResultSet resultSet= stmt.executeQuery(sql);

                if(resultSet.next()){
                    ei.tf_eID.setText(resultSet.getString(1));
                    ei.tf_eName.setText(resultSet.getString(2));
                    ei.tf_eAge.setText(resultSet.getString(6));
                    ei.tf_eJob.setText(resultSet.getString(7));
                    ei.tf_ePhone.setText(resultSet.getString(4));
                    ei.tf_eSalary.setText(resultSet.getString(3));
                }
                else{
                    JOptionPane.showMessageDialog(null, "系统错误，请上报管理员", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            ei.tf_eName.setEditable(true);
            ei.tf_eAge.setEditable(true);
            ei.tf_ePhone.setEditable(true);

            B_ae.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String sql = "UPDATE Employee SET Employee_Name=?,Employee_Phone=?,Employee_Age=? WHERE Employee_ID=?";
                    PreparedStatement statement = null;
                    PreparedStatement stmt = null;
                    try {
                        statement = dbConn.prepareStatement(sql);
                        statement.setString(1, ei.tf_eName.getText()) ;
                        statement.setInt(3, Integer.parseInt(ei.tf_eAge.getText()));
                        statement.setString(2, ei.tf_ePhone.getText());
                        statement.setString(4, eID);
                        statement.execute();
                        JOptionPane.showMessageDialog(ei,"修改成功");

                        Statement stmt1 = dbConn.createStatement();
                        String sql1 = "select Employee_Name,Employee_Phone,Employee_Age from Employee where Employee_ID='" + eID + "'";
                        ResultSet resultSet= stmt1.executeQuery(sql1);

                        //tf_eName.setEditable(true);
                        //tf_eAge.setEditable(true);
                        //tf_ePhone.setEditable(true);
                        if(resultSet.next()){
                            tf_eName.setText(resultSet.getString(1));
                            tf_eAge.setText(resultSet.getString(3));
                            tf_ePhone.setText(resultSet.getString(2));
                            //tf_eName.setEditable(false);
                            //tf_eAge.setEditable(false);
                            //tf_ePhone.setEditable(false);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "系统错误，请上报管理员", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }
        if(e.getSource().equals(B_password)){
            new PasswordChangeDemo(eID);
        }
    }
    public static void main(String[] args) throws SQLException {
        JFrame jf=new JFrame();
        jf.setBounds(300,300,200,600);
        jf.add(new EmployeeJPanel("D0019"));
        jf.setVisible(true);
    }
}
