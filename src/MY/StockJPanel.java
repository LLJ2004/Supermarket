package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StockJPanel extends JPanel implements ActionListener {
    protected JTextField tf_Order_ID,tf_Supplier_ID;
    protected JButton B_AddSell,B_Search_Sell;
    public JComboBox<String> combox_searchtype;
    private static String[] searchtype = {"进货单号","供应商号","未完成进货单"};
    protected Connection dbConn;
    static String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
    static String userName = "sa";
    static String userPwd = "llj2208long";

    protected JPanel p_;
    public StockJPanel(){
        try {
            Class.forName(driverName);
            this.dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("Succeed!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed!");
        }

        this.setLayout(new BorderLayout());

        JPanel p1=new JPanel(new GridLayout(4,2));
        p1.add(new JLabel("进货单号",SwingConstants.CENTER));
        p1.add(tf_Order_ID=new JTextField());
        p1.add(new JLabel("供应商号",SwingConstants.CENTER));
        p1.add(tf_Supplier_ID=new JTextField());
        p1.add(new JLabel("查询类型",SwingConstants.CENTER));
        p1.add(combox_searchtype=new JComboBox<>(searchtype));
        p1.add(B_AddSell=new JButton("添加进货单"));
        p1.add(B_Search_Sell=new JButton("查询进货单"));

        B_AddSell.addActionListener(this);
        B_Search_Sell.addActionListener(this);

        this.add(p1,BorderLayout.WEST);
        this.add(p_= new JPanel(new BorderLayout()));
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(B_AddSell)){
            p_.removeAll();
            String query = "SELECT * FROM Suppliers WHERE Supplier_ID = ?";
            PreparedStatement statement1 = null;
            try {
                statement1 = dbConn.prepareStatement(query);
                statement1.setString(1, tf_Supplier_ID.getText());
                ResultSet resultSet = null;
                resultSet = statement1.executeQuery();
                if (resultSet.next()) {
                    String insert = "INSERT INTO Orders (Orders_ID, Supplier_ID) VALUES (?, ?)";
                    PreparedStatement statement2 = null;

                    PreparedStatement statement4 = dbConn.prepareStatement("SELECT TOP 1 * FROM Orders ORDER BY Orders_ID DESC");
                    ResultSet resultSet4 = statement4.executeQuery();
                    resultSet4.next();
                    String formattedString = String.format("%05d", resultSet4.getInt(1)+1);
                    tf_Order_ID.setText(formattedString);

                    statement2 = dbConn.prepareStatement(insert);
                    statement2.setString(1, tf_Order_ID.getText());
                    statement2.setString(2, tf_Supplier_ID.getText());
                    int rowsInserted = 0;
                    rowsInserted = statement2.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("A new row has been inserted.");
                    }
                    p_.add(new StockDetailsJPanel(tf_Order_ID.getText()),BorderLayout.CENTER);
                    p_.validate();
                    p_.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "供应商不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Data does not exist in the table.");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }
        if(e.getSource().equals(B_Search_Sell)){
            p_.removeAll();
            SearchStockJPanel ss= new SearchStockJPanel();
            p_.add(ss,BorderLayout.CENTER);
            String type= String.valueOf(combox_searchtype.getSelectedItem());
            Statement stmt = null;
            String sql=null;
            try {
                stmt = dbConn.createStatement();

                switch (type){
                    //"进货单号","供应商号","未完成进货单"
                    //"进货单号","供应商号","发货日期","预计到达日期","进货总价","进货状态","入库","退货","修改进货单"
                    case "进货单号":
                        sql= "select * from Orders where Orders_ID= '" + tf_Order_ID.getText() + "' and Orders_Condition='运输'";
                        break;
                    case "供应商号":
                        sql = "select * from Orders where Supplier_ID= '" + tf_Supplier_ID.getText() + "' and Orders_Condition='运输'";
                        break;
                    case "未完成进货单":
                        sql = "select * from Orders where Orders_Condition='运输'";
                        break;
                }

                ResultSet resultSet;
                resultSet = stmt.executeQuery(sql);
                while (resultSet.next()){
                    Object[] qs={resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(7),"运输"};
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
        jf.add(new StockJPanel());
        jf.setVisible(true);
    }
}
