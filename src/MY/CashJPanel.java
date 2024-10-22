package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CashJPanel extends JPanel implements ActionListener {
    protected JTextField tf_Sell_ID,tf_Customer_ID;
    protected JButton B_AddSell,B_Search_Sell;
    public JComboBox<String> combox_ordertype,combox_searchtype;
    private static String[] ordertype = {"现单","预订单"};
    private static String[] searchtype = {"销售单号","会员号","预订单"};
    protected Connection dbConn;
    static String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
    static String userName = "sa";
    static String userPwd = "llj2208long";

    protected JPanel p_;
    public CashJPanel(){
        try {
            Class.forName(driverName);
            this.dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("Succeed!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed!");
        }

        this.setLayout(new BorderLayout());

        JPanel p1=new JPanel(new GridLayout(5,2));
        p1.add(new JLabel("销售单号",SwingConstants.CENTER));
        p1.add(tf_Sell_ID=new JTextField());
        p1.add(new JLabel("会员号",SwingConstants.CENTER));
        p1.add(tf_Customer_ID=new JTextField());
        p1.add(new JLabel("订单类型",SwingConstants.CENTER));
        p1.add(combox_ordertype=new JComboBox<>(ordertype));
        p1.add(new JLabel("查询类型",SwingConstants.CENTER));
        p1.add(combox_searchtype=new JComboBox<>(searchtype));
        p1.add(B_AddSell=new JButton("添加订单"));
        p1.add(B_Search_Sell=new JButton("查询订单"));

        B_AddSell.addActionListener(this);
        B_Search_Sell.addActionListener(this);

        this.add(p1,BorderLayout.WEST);
        this.add(p_= new JPanel(new BorderLayout()));
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(B_AddSell)){
            p_.removeAll();
            String query = "SELECT * FROM Customer WHERE Customer_ID = ?";
            PreparedStatement statement1 = null;
            try {
                statement1 = dbConn.prepareStatement(query);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {
                statement1.setString(1, tf_Customer_ID.getText());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            ResultSet resultSet = null;
            try {
                resultSet = statement1.executeQuery();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {
                if (resultSet.next()) {
                    PreparedStatement statement3 = dbConn.prepareStatement("SELECT VIP_Discount FROM VIP,Customer WHERE Customer.VIP_ID=VIP.VIP_ID and Customer_ID = "+tf_Customer_ID.getText());
                    ResultSet resultSet3 = statement3.executeQuery();
                    resultSet3.next();
                    Double discount=resultSet3.getDouble(1);

                    PreparedStatement statement4 = dbConn.prepareStatement("SELECT TOP 1 * FROM Sell ORDER BY Sell_ID DESC");
                    ResultSet resultSet4 = statement4.executeQuery();
                    resultSet4.next();
                    String formattedString = String.format("%05d", resultSet4.getInt(1)+1);
                    tf_Sell_ID.setText(formattedString);

                    String insert = "INSERT INTO Sell (Sell_ID, Customer_ID, Sell_OrderType) VALUES (?, ?, ?)";
                    PreparedStatement statement2 = null;
                    try {
                        statement2 = dbConn.prepareStatement(insert);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        statement2.setString(1, tf_Sell_ID.getText());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        statement2.setString(2, tf_Customer_ID.getText());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        statement2.setString(3, String.valueOf(combox_ordertype.getSelectedItem()));
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                    int rowsInserted = 0;
                    try {
                        rowsInserted = statement2.executeUpdate();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (rowsInserted > 0) {
                        System.out.println("A new row has been inserted.");
                    }
                    try {
                        //JPanel wrap=new JPanel(new BorderLayout());
                        p_.add(new OrderDetailsJPanel(tf_Sell_ID.getText(),discount),BorderLayout.CENTER);
                        p_.validate();
                        p_.repaint();
                        //wrap.validate();
                        //wrap.repaint();
                        //p_.add(wrap);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "会员不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Data does not exist in the table.");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }
        if(e.getSource().equals(B_Search_Sell)){
            p_.removeAll();
            SearchSellJPanel ss=new SearchSellJPanel();
            JScrollPane sp=new JScrollPane(ss);
            p_.add(sp);
            String type= String.valueOf(combox_searchtype.getSelectedItem());
            Statement stmt = null;
            String sql=null;
            try {
                stmt = dbConn.createStatement();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            switch (type){
                //"销售单号","会员号","预订单"
                //"销售单号","会员号","开始日期","金额","订单类型","完成订单","删除订单","修改订单"
                case "销售单号":
                    sql= "select * from Sell where Sell_ID= '" + tf_Sell_ID.getText() + "' and Sell_OrderType='预订单'";
                    break;
                case "会员号":
                    sql = "select * from Sell where Customer_ID= '" + tf_Customer_ID.getText() + "' and Sell_OrderType='预订单'";
                    break;
                case "预订单":
                    sql = "select * from Sell where Sell_OrderType='预订单'";
                    break;
            }
            try {
                ResultSet resultSet;
                resultSet = stmt.executeQuery(sql);
                while (resultSet.next()){
                    Object[] qs={resultSet.getString(1),resultSet.getString(2),resultSet.getString(5),resultSet.getString(4),"预订单"};
                    System.out.println(qs);
                    ss.sell.addRow(qs);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            p_.validate();
            p_.repaint();
        }
    }

    public static void main(String[] args) {
        JFrame jf=new JFrame();
        jf.setBounds(300,300,200,600);
        jf.add(new CashJPanel());
        jf.setVisible(true);
    }
}
