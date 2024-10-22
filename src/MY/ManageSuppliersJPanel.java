package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ManageSuppliersJPanel extends JPanel implements ActionListener{
    protected JTextField tf_Supplier_ID;
    protected JButton B_AddSuppliers,B_Search;
    protected Connection dbConn;
    static String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
    static String userName = "sa";
    static String userPwd = "llj2208long";

    protected JPanel p_;
    protected SuppliersInfoJPanel p_info;

    public ManageSuppliersJPanel(){
        try {
            Class.forName(driverName);
            this.dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("Succeed!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed!");
        }

        this.setLayout(new BorderLayout());
        p_info= new SuppliersInfoJPanel();

        JPanel p1=new JPanel(new GridLayout(2,2));
        p1.add(new JLabel("供应商号",SwingConstants.CENTER));
        p1.add(tf_Supplier_ID=new JTextField());
        p1.add(B_AddSuppliers=new JButton("添加供应商"));
        p1.add(B_Search=new JButton("查询供应商"));

        B_AddSuppliers.addActionListener(this);
        B_Search.addActionListener(this);

        this.add(p1,BorderLayout.WEST);
        this.add(p_= new JPanel(new BorderLayout()));
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(B_AddSuppliers)){
            p_.removeAll();
            JButton B_add=new JButton("添加");
            p_.add(B_add,BorderLayout.SOUTH);
            p_.add(p_info);
            p_.validate();
            p_.repaint();

            p_info.tf_Supplier_ID.setText("");
            p_info.tf_Supplier_Name.setText("");
            p_info.tf_Supplier_Address.setText("");
            p_info.tf_Supplier_Phone.setText("");

            p_info.tf_Supplier_ID.setEditable(true);
            p_info.tf_Supplier_Name.setEditable(true);
            p_info.tf_Supplier_Address.setEditable(true);
            p_info.tf_Supplier_Phone.setEditable(true);


            B_add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String sql = "INSERT INTO Suppliers (Supplier_ID, Supplier_Name,Supplier_Address,Supplier_Phone) VALUES (?, ?,?,?)";
                    PreparedStatement statement = null;
                    try {
                        statement = dbConn.prepareStatement(sql);
                        statement.setString(1, p_info.tf_Supplier_ID.getText()) ;
                        statement.setString(2, p_info.tf_Supplier_Name.getText());
                        statement.setString(3, p_info.tf_Supplier_Address.getText());
                        statement.setString(4, p_info.tf_Supplier_Phone.getText());
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
            JButton B_alter=new JButton("修改供应商");
            JButton B_delete=new JButton("删除供应商");
            JPanel p1=new JPanel(new GridLayout(1,2));
            p1.add(B_alter);
            p1.add(B_delete);
            p_.add(p1,BorderLayout.SOUTH);
            p_.add(p_info,BorderLayout.CENTER);

            p_info.validate();
            p_info.repaint();

            p_.validate();
            p_.repaint();

            String id=tf_Supplier_ID.getText();
            PreparedStatement stmt = null;
            try {
                String sql1 = "select * from Suppliers where Supplier_ID= '" + id + "'";
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                resultSet.next();
                p_info.tf_Supplier_ID.setText(resultSet.getString(1));
                p_info.tf_Supplier_Name.setText(resultSet.getString(2));
                p_info.tf_Supplier_Address.setText(resultSet.getString(3));
                p_info.tf_Supplier_Phone.setText(String.valueOf(resultSet.getString(4)));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            p_info.tf_Supplier_ID.setEditable(false);
            p_info.tf_Supplier_Name.setEditable(false);
            p_info.tf_Supplier_Address.setEditable(false);
            p_info.tf_Supplier_Phone.setEditable(false);

            B_alter.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame jf=new JFrame();
                    SuppliersInfoJPanel p_ci= new SuppliersInfoJPanel();
                    JButton B_alter=new JButton("修改");
                    jf.setBounds(300,300,200,600);
                    jf.setLayout(new BorderLayout());
                    jf.add(B_alter,BorderLayout.SOUTH);
                    jf.add(p_ci);

                    p_ci.tf_Supplier_ID.setText(p_info.tf_Supplier_ID.getText());
                    p_ci.tf_Supplier_ID.setEditable(false);
                    p_ci.tf_Supplier_Name.setText(p_info.tf_Supplier_Name.getText());
                    p_ci.tf_Supplier_Address.setText(p_info.tf_Supplier_Address.getText());
                    p_ci.tf_Supplier_Phone.setText(p_info.tf_Supplier_Phone.getText());

                    B_alter.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String sql = "UPDATE Suppliers SET Supplier_Name=?,Supplier_Address=?,Supplier_Phone=? WHERE Supplier_ID=?";
                            PreparedStatement statement = null;
                            try {
                                statement = dbConn.prepareStatement(sql);
                                statement.setString(1, p_ci.tf_Supplier_Name.getText());
                                statement.setString(2, p_ci.tf_Supplier_Address.getText());
                                statement.setString(3, p_ci.tf_Supplier_Phone.getText());
                                statement.setString(4,p_info.tf_Supplier_ID.getText());
                                statement.execute();
                                JOptionPane.showMessageDialog(jf,"修改成功");

                                sql = "SELECT * FROM Suppliers WHERE Supplier_ID='"+p_info.tf_Supplier_ID.getText()+"'";
                                statement = dbConn.prepareStatement(sql);
                                ResultSet rs=statement.executeQuery();
                                p_info.tf_Supplier_ID.setText(rs.getString(0));
                                p_info.tf_Supplier_Name.setText(rs.getString(1));
                                p_info.tf_Supplier_Address.setText(rs.getString(2));
                                p_info.tf_Supplier_Phone.setText(rs.getString(3));
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
                    String sql = "DELETE FROM Suppliers WHERE Supplier_ID='"+p_info.tf_Supplier_ID.getText()+"'";
                    PreparedStatement statement = null;
                    try {
                        statement = dbConn.prepareStatement(sql);
                        statement.execute();
                        JOptionPane.showMessageDialog(p_info,"删除成功");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    p_info.tf_Supplier_ID.setText("");
                    p_info.tf_Supplier_Name.setText("");
                    p_info.tf_Supplier_Address.setText("");
                    p_info.tf_Supplier_Phone.setText("");
                }
            });
        }
    }

    public static void main(String[] args) {
        JFrame jf=new JFrame();
        jf.setBounds(300,300,200,600);
        jf.add(new ManageSuppliersJPanel());
        jf.setVisible(true);
    }
}
