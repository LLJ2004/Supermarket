package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;

public class CustomerJPanel extends JPanel implements ActionListener {
    protected JTextField tf_Customer_ID;
    protected JButton B_AddCustomer,B_Search;
    protected Connection dbConn;
    static String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
    static String userName = "sa";
    static String userPwd = "llj2208long";

    protected JPanel p_;
    protected CustomerInfoJPanel p_info;

    public CustomerJPanel(){
        try {
            Class.forName(driverName);
            this.dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("Succeed!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed!");
        }

        this.setLayout(new BorderLayout());
        p_info=new CustomerInfoJPanel();

        JPanel p1=new JPanel(new GridLayout(2,2));
        p1.add(new JLabel("会员号",SwingConstants.CENTER));
        p1.add(tf_Customer_ID=new JTextField());
        p1.add(B_AddCustomer=new JButton("添加会员"));
        p1.add(B_Search=new JButton("查询会员"));

        B_AddCustomer.addActionListener(this);
        B_Search.addActionListener(this);

        this.add(p1,BorderLayout.WEST);
        this.add(p_= new JPanel(new BorderLayout()));
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(B_AddCustomer)){
            p_.removeAll();
            JButton B_add=new JButton("添加");
            p_.add(B_add,BorderLayout.SOUTH);
            p_.add(p_info);
            p_.validate();
            p_.repaint();

            String formattedString;
            PreparedStatement statement4 = null;
            try {
                statement4 = dbConn.prepareStatement("SELECT TOP 1 * FROM Customer ORDER BY Customer_ID DESC");
                ResultSet resultSet4 = statement4.executeQuery();
                resultSet4.next();
                formattedString= String.format("%05d", resultSet4.getInt(1)+1);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            p_info.tf_Customer_ID.setText(formattedString);
            p_info.tf_Customer_Name.setText("");
            p_info.tf_Customer_Phone.setText("");
            p_info.tf_Customer_Birthday_YY.setText("");
            p_info.tf_Customer_Birthday_DD.setText("");
            p_info.tf_Customer_Birthday_MM.setText("");
            p_info.tf_Customer_Deposit.setText("0");
            p_info.tf_Customer_Deposit.setEditable(false);
            p_info.tf_VIP_ID.setText("C");
            p_info.tf_VIP_ID.setEditable(false);
            p_info.tf_Customer_ID.setEditable(false);
            p_info.tf_Customer_Name.setEditable(true);
            p_info.tf_Customer_Phone.setEditable(true);
            p_info.tf_Customer_Birthday_YY.setEditable(true);
            p_info.tf_Customer_Birthday_MM.setEditable(true);
            p_info.tf_Customer_Birthday_DD.setEditable(true);

            B_add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String sql = "INSERT INTO Customer (Customer_ID, Customer_Name,Customer_Phone,Customer_Birthday) VALUES (?, ?,?,?)";
                    PreparedStatement statement = null;
                    try {
                        statement = dbConn.prepareStatement(sql);
                        statement.setString(1, p_info.tf_Customer_ID.getText()) ;
                        statement.setString(2, p_info.tf_Customer_Name.getText());
                        statement.setString(3, p_info.tf_Customer_Phone.getText());
                        int year = Integer.parseInt(p_info.tf_Customer_Birthday_YY.getText());
                        int month = Integer.parseInt(p_info.tf_Customer_Birthday_MM.getText());
                        int day = Integer.parseInt(p_info.tf_Customer_Birthday_DD.getText());
                        // 创建日期时间对象
                        Date date = new Date(year - 1900, month - 1, day); // 注意：Date的构造方法中月份是从0开始计数的
                        // 转换为Timestamp
                        Timestamp timestamp = new Timestamp(date.getTime());
                        statement.setTimestamp(4, timestamp);
                        statement.execute();
                        JOptionPane.showMessageDialog(p_,"添加成功");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

        }
        if(e.getSource().equals(B_Search)){
            p_.removeAll();
            JButton B_alter=new JButton("修改客户");
            JButton B_delete=new JButton("删除客户");
            JPanel p1=new JPanel(new GridLayout(1,2));
            p1.add(B_alter);
            p1.add(B_delete);
            p_.add(p1,BorderLayout.SOUTH);
            p_.add(p_info,BorderLayout.CENTER);

            p_info.validate();
            p_info.repaint();

            p_.validate();
            p_.repaint();

            String id=tf_Customer_ID.getText();
            PreparedStatement stmt = null;
            try {
                String sql1 = "select * from Customer where Customer_ID= '" + id + "'";
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                resultSet.next();
                p_info.tf_Customer_ID.setText(resultSet.getString(1));
                p_info.tf_Customer_Name.setText(resultSet.getString(3));
                p_info.tf_VIP_ID.setText(resultSet.getString(4));
                p_info.tf_Customer_Deposit.setText(String.valueOf(resultSet.getString(5)));
                p_info.tf_Customer_Phone.setText(resultSet.getString(6));
                Date date = resultSet.getDate(7);
                // 将日期分割成年、月、日
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = dateFormat.format(date);
                String[] dateParts = dateString.split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[2]);
                p_info.tf_Customer_Birthday_YY.setText(String.valueOf(year));
                p_info.tf_Customer_Birthday_MM.setText(String.valueOf(month));
                p_info.tf_Customer_Birthday_DD.setText(String.valueOf(day));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            p_info.tf_Customer_ID.setEditable(false);
            p_info.tf_Customer_Name.setEditable(false);
            p_info.tf_VIP_ID.setEditable(false);
            p_info.tf_Customer_Deposit.setEditable(false);
            p_info.tf_Customer_Phone.setEditable(false);
            p_info.tf_Customer_Birthday_YY.setEditable(false);
            p_info.tf_Customer_Birthday_MM.setEditable(false);
            p_info.tf_Customer_Birthday_DD.setEditable(false);

            B_alter.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame jf=new JFrame();
                    CustomerInfoJPanel p_ci=new CustomerInfoJPanel();
                    JButton B_alter=new JButton("修改");
                    jf.setBounds(300,300,200,600);
                    jf.setLayout(new BorderLayout());
                    jf.add(B_alter,BorderLayout.SOUTH);
                    jf.add(p_ci);

                    p_ci.tf_Customer_Deposit.setText("0");
                    p_ci.tf_Customer_Deposit.setEditable(false);
                    p_ci.tf_VIP_ID.setText("C");
                    p_ci.tf_VIP_ID.setEditable(false);
                    p_ci.tf_Customer_ID.setText(p_info.tf_Customer_ID.getText());
                    p_ci.tf_Customer_ID.setEditable(false);
                    p_ci.tf_Customer_Name.setText(p_info.tf_Customer_Name.getText());
                    p_ci.tf_Customer_Phone.setText(p_info.tf_Customer_Phone.getText());
                    p_ci.tf_Customer_Birthday_YY.setText(p_info.tf_Customer_Birthday_YY.getText());
                    p_ci.tf_Customer_Birthday_MM.setText(p_info.tf_Customer_Birthday_MM.getText());
                    p_ci.tf_Customer_Birthday_DD.setText(p_info.tf_Customer_Birthday_DD.getText());

                    B_alter.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String sql = "UPDATE Customer SET Customer_Name=?,Customer_Phone=?,Customer_Birthday=? WHERE Customer_ID=?";
                            PreparedStatement statement = null;
                            try {
                                statement = dbConn.prepareStatement(sql);
                                statement.setString(1, p_ci.tf_Customer_Name.getText());
                                statement.setString(2, p_ci.tf_Customer_Phone.getText());
                                int year = Integer.parseInt(p_ci.tf_Customer_Birthday_YY.getText());
                                int month = Integer.parseInt(p_ci.tf_Customer_Birthday_MM.getText());
                                int day = Integer.parseInt(p_ci.tf_Customer_Birthday_DD.getText());
                                // 创建日期时间对象
                                Date date = new Date(year - 1900, month - 1, day); // 注意：Date的构造方法中月份是从0开始计数的
                                // 转换为Timestamp
                                Timestamp timestamp = new Timestamp(date.getTime());
                                statement.setTimestamp(3, timestamp);
                                statement.setString(4,p_info.tf_Customer_ID.getText());
                                statement.execute();
                                JOptionPane.showMessageDialog(jf,"修改成功");

                                sql = "select * from Customer where Customer_ID= '" + id + "'";
                                statement=dbConn.prepareStatement(sql);
                                ResultSet resultSet;
                                resultSet = statement.executeQuery();
                                resultSet.next();
                                p_info.tf_Customer_ID.setText(resultSet.getString(1));
                                p_info.tf_Customer_Name.setText(resultSet.getString(3));
                                p_info.tf_VIP_ID.setText(resultSet.getString(4));
                                p_info.tf_Customer_Deposit.setText(String.valueOf(resultSet.getString(5)));
                                p_info.tf_Customer_Phone.setText(resultSet.getString(6));
                                date = resultSet.getDate(7);
                                // 将日期分割成年、月、日
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String dateString = dateFormat.format(date);
                                String[] dateParts = dateString.split("-");
                                year = Integer.parseInt(dateParts[0]);
                                month = Integer.parseInt(dateParts[1]);
                                day = Integer.parseInt(dateParts[2]);
                                p_info.tf_Customer_Birthday_YY.setText(String.valueOf(year));
                                p_info.tf_Customer_Birthday_MM.setText(String.valueOf(month));
                                p_info.tf_Customer_Birthday_DD.setText(String.valueOf(day));
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                    jf.setVisible(true);
                }
            });
            B_delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String sql = "DELETE FROM Customer WHERE Customer_ID='"+p_info.tf_Customer_ID.getText()+"'";
                    PreparedStatement statement = null;
                    try {
                        statement = dbConn.prepareStatement(sql);
                        statement.execute();
                        JOptionPane.showMessageDialog(p_info,"删除成功");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    p_info.tf_Customer_ID.setText("");
                    p_info.tf_Customer_Name.setText("");
                    p_info.tf_Customer_Phone.setText("");
                    p_info.tf_Customer_Birthday_YY.setText("");
                    p_info.tf_Customer_Birthday_DD.setText("");
                    p_info.tf_Customer_Birthday_MM.setText("");
                    p_info.tf_Customer_Deposit.setText("");
                    p_info.tf_VIP_ID.setText("");
                }
            });
        }
    }

    public static void main(String[] args) {
        JFrame jf=new JFrame();
        jf.setBounds(300,300,200,600);
        jf.add(new CustomerJPanel());
        jf.setVisible(true);
    }
}
