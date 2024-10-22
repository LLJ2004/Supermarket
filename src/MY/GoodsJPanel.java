package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class GoodsJPanel extends JPanel implements ActionListener{
    protected JTextField tf_Goods_ID;
    protected JButton B_AddGoods,B_Search;
    protected Connection dbConn;
    static String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
    static String userName = "sa";
    static String userPwd = "llj2208long";

    protected JPanel p_;
    protected GoodsInfoJPanel p_info;

    public GoodsJPanel(){
        try {
            Class.forName(driverName);
            this.dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("Succeed!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed!");
        }

        this.setLayout(new BorderLayout());
        p_info= new GoodsInfoJPanel();

        JPanel p1=new JPanel(new GridLayout(2,2));
        p1.add(new JLabel("商品号",SwingConstants.CENTER));
        p1.add(tf_Goods_ID=new JTextField());
        p1.add(B_AddGoods=new JButton("添加商品"));
        p1.add(B_Search=new JButton("查询商品"));

        B_AddGoods.addActionListener(this);
        B_Search.addActionListener(this);

        this.add(p1,BorderLayout.WEST);
        this.add(p_= new JPanel(new BorderLayout()));
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(B_AddGoods)){
            p_.removeAll();
            JButton B_add=new JButton("添加");
            p_.add(B_add,BorderLayout.SOUTH);
            p_.add(p_info);
            p_.validate();
            p_.repaint();

            String output ;
            PreparedStatement statement4 = null;
            try {
                statement4 = dbConn.prepareStatement("SELECT TOP 1 * FROM Goods ORDER BY Goods_ID DESC");
                ResultSet resultSet4 = statement4.executeQuery();
                resultSet4.next();
                String input = resultSet4.getString(1);

                // 寻找字符串中的数字并递增

                String letterPart = input.substring(0, 1);
                String numberPart = input.substring(1);
                String formattedString = String.format("%03d", Integer.parseInt(numberPart)+1);
                output=letterPart+formattedString;

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            p_info.tf_Goods_ID.setEditable(true);
            p_info.tf_Goods_Name.setEditable(true);
            p_info.tf_Goods_UnitPrice.setEditable(true);
            p_info.combox_type.setEditable(true);
            p_info.tf_Store_ID.setEditable(true);
            p_info.tf_Goods_Image.setEditable(true);
            p_info.tf_Goods_Info.setEditable(true);
            p_info.tf_Goods_Show.setEditable(true);
            p_info.combox_type.setEditable(true);
            p_info.tf_Goods_ID.setText(output.toString());
            p_info.tf_Goods_Name.setText("");
            p_info.tf_Goods_UnitPrice.setText("");
            p_info.tf_Goods_Image.setText("");
            p_info.tf_Goods_Info.setText("");
            p_info.tf_Goods_Show.setText("");
            p_info.tf_Store_ID.setText("");


            B_add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String sql = "INSERT INTO Goods (Goods_ID, Goods_Name,Goods_UnitPrice,Goods_Type,Store_ID,Goods_Image,Goods_Info,Goods_Show) VALUES (?, ?,?,?,?,?,?,?)";
                    PreparedStatement statement = null;
                    try {
                        statement = dbConn.prepareStatement(sql);
                        statement.setString(1, p_info.tf_Goods_ID.getText()) ;
                        statement.setString(2, p_info.tf_Goods_Name.getText());
                        statement.setDouble(3, Double.parseDouble(p_info.tf_Goods_UnitPrice.getText()));
                        statement.setString(4, String.valueOf(p_info.combox_type.getSelectedItem()));
                        statement.setString(5, p_info.tf_Store_ID.getText());
                        statement.setString(6, p_info.tf_Goods_Image.getText());
                        statement.setString(7, p_info.tf_Goods_Info.getText());
                        statement.setString(8, p_info.tf_Goods_Show.getText());

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
            JButton B_alter=new JButton("修改商品");
            JButton B_delete=new JButton("删除商品");
            JPanel p1=new JPanel(new GridLayout(1,2));
            p1.add(B_alter);
            p1.add(B_delete);
            p_.add(p1,BorderLayout.SOUTH);
            p_.add(p_info,BorderLayout.CENTER);

            p_info.validate();
            p_info.repaint();

            p_.validate();
            p_.repaint();

            String id=tf_Goods_ID.getText();
            PreparedStatement stmt = null;
            try {
                String sql1 = "select * from Goods where Goods_ID= '" + id + "'";
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                resultSet.next();
                p_info.tf_Goods_ID.setText(resultSet.getString(1));
                p_info.tf_Goods_Name.setText(resultSet.getString(2));
                p_info.tf_Goods_UnitPrice.setText(resultSet.getString(3));
                p_info.combox_type.setSelectedItem(resultSet.getString(4));
                p_info.tf_Store_ID.setText(resultSet.getString(5));
                p_info.tf_Goods_Image.setText(resultSet.getString(8));
                p_info.tf_Goods_Info.setText(resultSet.getString(9));
                p_info.tf_Goods_Show.setText(resultSet.getString(12));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            p_info.tf_Goods_ID.setEditable(false);
            p_info.tf_Goods_Name.setEditable(false);
            p_info.tf_Goods_UnitPrice.setEditable(false);
            p_info.combox_type.setEditable(false);
            p_info.tf_Store_ID.setEditable(false);
            p_info.tf_Goods_Image.setEditable(false);
            p_info.tf_Goods_Info.setEditable(false);
            p_info.tf_Goods_Show.setEditable(false);
            p_info.combox_type.setEditable(false);

            B_alter.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame jf=new JFrame();
                    GoodsInfoJPanel p_ci= new GoodsInfoJPanel();
                    JButton B_alter=new JButton("修改");
                    jf.setBounds(300,300,200,600);
                    jf.setLayout(new BorderLayout());
                    jf.add(B_alter,BorderLayout.SOUTH);
                    jf.add(p_ci);

                    p_ci.tf_Goods_ID.setText(p_info.tf_Goods_ID.getText());
                    p_ci.tf_Goods_ID.setEditable(false);
                    p_ci.tf_Goods_Name.setText(p_info.tf_Goods_Name.getText());
                    p_ci.tf_Goods_UnitPrice.setText(p_info.tf_Goods_UnitPrice.getText());
                    p_ci.combox_type.setSelectedItem(p_info.combox_type.getSelectedItem());
                    p_ci.tf_Store_ID.setText(p_info.tf_Store_ID.getText());
                    p_ci.tf_Goods_Image.setText(p_info.tf_Goods_Image.getText());
                    p_ci.tf_Goods_Info.setText(p_info.tf_Goods_Info.getText());
                    p_ci.tf_Goods_Show.setText(p_info.tf_Goods_Show.getText());

                    B_alter.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String sql = "UPDATE Goods set Goods_Name=?,Goods_UnitPrice=?,Goods_Type=?,Store_ID=?,Goods_Image=?,Goods_Info=?,Goods_Show=? WHERE Goods_ID=?";
                            PreparedStatement statement = null;
                            try {
                                statement = dbConn.prepareStatement(sql);
                                statement.setString(1, p_ci.tf_Goods_Name.getText());
                                statement.setString(2, p_ci.tf_Goods_UnitPrice.getText());
                                statement.setString(3, String.valueOf(p_ci.combox_type.getSelectedItem()));
                                statement.setString(4, p_ci.tf_Store_ID.getText());
                                statement.setString(5, p_ci.tf_Goods_Image.getText());
                                statement.setString(6, p_ci.tf_Goods_Info.getText());
                                statement.setString(7, p_ci.tf_Goods_Show.getText());
                                statement.setString(8, p_ci.tf_Goods_ID.getText());

                                statement.execute();
                                JOptionPane.showMessageDialog(jf,"修改成功");

                                sql = "select * from Goods where Goods_ID= '" + id + "'";
                                statement=dbConn.prepareStatement(sql);
                                ResultSet resultSet;
                                resultSet = statement.executeQuery();
                                resultSet.next();
                                p_info.tf_Goods_ID.setText(resultSet.getString(1));
                                p_info.tf_Goods_Name.setText(resultSet.getString(2));
                                p_info.tf_Goods_UnitPrice.setText(resultSet.getString(3));
                                p_info.combox_type.setSelectedItem(resultSet.getString(4));
                                p_info.tf_Store_ID.setText(resultSet.getString(5));
                                p_info.tf_Goods_Image.setText(resultSet.getString(8));
                                p_info.tf_Goods_Info.setText(resultSet.getString(9));
                                p_info.tf_Goods_Show.setText(resultSet.getString(12));
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
                    String sql = "DELETE FROM Goods WHERE Goods_ID='"+p_info.tf_Goods_ID.getText()+"'";
                    PreparedStatement statement = null;
                    try {
                        statement = dbConn.prepareStatement(sql);
                        statement.execute();
                        JOptionPane.showMessageDialog(p_info,"删除成功");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    p_info.tf_Goods_ID.setText("");
                    p_info.tf_Goods_Name.setText("");
                    p_info.tf_Goods_UnitPrice.setText("");
                    p_info.tf_Goods_Image.setText("");
                    p_info.tf_Goods_Info.setText("");
                    p_info.tf_Goods_Show.setText("");
                    p_info.tf_Store_ID.setText("");
                }
            });
        }
    }

    public static void main(String[] args) {
        JFrame jf=new JFrame();
        jf.setBounds(300,300,200,600);
        jf.add(new GoodsJPanel());
        jf.setVisible(true);
    }
}
