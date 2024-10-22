package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;

public class StockDetailsJPanel extends JPanel implements ActionListener {
    protected JTextField tf_Search;
    protected JLabel l_Price,l_searchid;
    protected JButton B_Search,B_Check,B_Pay,B_Back;
    protected String orderID;

    protected GridLayout grid;
    protected JPanel sp1;
    protected AddStockJPanel ag;
    protected Connection dbConn;
    static String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
    static String userName = "sa";
    static String userPwd = "llj2208long";

    public StockDetailsJPanel(String oID) throws SQLException {
        try {
            Class.forName(driverName);
            this.dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("Succeed!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed!");
        }

        this.orderID=oID;

        this.setLayout(grid=new GridLayout(4,1));
        JPanel p1=new JPanel(new GridLayout(1,2));
        JPanel p2=new JPanel(new GridLayout(1,2));

        JPanel p3=new JPanel(new BorderLayout());
        p3.add(l_searchid=new JLabel("商品ID"),BorderLayout.WEST);
        p3.add(tf_Search=new JTextField());
        p1.add(p3);
        p1.add(B_Search=new JButton("查找商品"));

        JPanel p4=new JPanel(new BorderLayout());
        p4.add(new JLabel("总价"),BorderLayout.WEST);
        p4.add(l_Price=new JLabel("0.0元"));
        p2.add(p4);
        p2.add(B_Check=new JButton("生成进货单"));


        l_searchid.setSize(100,40);
        tf_Search.setSize(100,40);
        B_Search.setSize(100,40);
        l_Price.setSize(100,40);
        B_Check.setSize(100,40);

        B_Search.addActionListener(this);
        B_Check.addActionListener(this);

        this.add(p1,0);
        this.add(sp1= new JPanel(),1);
        sp1.add(new JLabel("查找商品"));
        this.add(new JScrollPane(ag=new AddStockJPanel()),2);
        this.add(p2,3);
        //sp1.setVisible(false);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(B_Search)) {
            sp1.removeAll();
            try {
                Statement stmt = null;
                stmt = dbConn.createStatement();
                String sql = "select * from Goods where Goods_ID= '" + tf_Search.getText() + "'";
                ResultSet resultSet;
                resultSet = stmt.executeQuery(sql);
                System.out.println("001");
                sp1.add(new SearchResult_GoodsJPanel(resultSet));
                sp1.validate();
                sp1.repaint();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getSource().equals(B_Check)){
            for (int i = 0; i < ag.table_add.getRowCount(); i++) {
                //"商品ID","进货价","进货数量","进货折扣","总价","操作"
                String sql = "INSERT INTO Order_Details (Orders_ID,Goods_ID,Order_Details_Quantity,Order_Details_UnitPrice,Order_Details_Discount,Order_Details_Price) VALUES (?, ?,?,?,?,?)";
                PreparedStatement statement = null;
                PreparedStatement stmt = null;
                try {
                    statement = dbConn.prepareStatement(sql);
                    statement.setString(2, String.valueOf(ag.table_add.getValueAt(i,0))) ;
                    statement.setString(1, orderID);
                    statement.setInt(3, Integer.parseInt(String.valueOf(ag.table_add.getValueAt(i,2))));
                    statement.setDouble(4, Double.parseDouble(String.valueOf(ag.table_add.getValueAt(i,1))));
                    statement.setDouble(5, Double.parseDouble(String.valueOf(ag.table_add.getValueAt(i,3))));
                    statement.setDouble(6, Double.parseDouble(String.valueOf(ag.table_add.getValueAt(i,4))));
                    statement.execute();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            int rowCount = ag.goods.getRowCount();
            for (int i = rowCount - 1; i >= 0; i--) {
                ag.goods.removeRow(i);
            }

            PreparedStatement stmt = null;
            String sql1 = "select * from Orders where Orders_ID= '" + orderID + "'";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                resultSet.next();
                l_Price.setText(resultSet.getString(7)+"元");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            l_Price.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    public static void main(String[] args) throws SQLException {
        JFrame jf=new JFrame();
        jf.setBounds(300,300,200,600);
        jf.add(new StockDetailsJPanel("00008"));
        jf.setVisible(true);
    }

    class SearchResult_GoodsJPanel extends JPanel implements ActionListener {
        protected JButton B_add;
        protected JLabel L_id, L_stock, L_name,L_Storewarn;

        public SearchResult_GoodsJPanel(ResultSet rs) throws SQLException, MalformedURLException {
            if (rs.next()) {
                //rs.beforeFirst();
                //while(rs.next()) {
                //while (rs.next()) {
                this.setLayout(new BorderLayout());

                JLabel l_Goods_Image = new JLabel();
                String url = rs.getString(8);
                URL imageUrl = new URL(url);
                ImageIcon imageIcon = new ImageIcon(imageUrl);
                l_Goods_Image.setIcon(imageIcon);

                JPanel p_info = new JPanel(new GridLayout(2, 4));
                p_info.add(new JLabel("商品ID"), CENTER_ALIGNMENT);
                p_info.add(L_id = new JLabel(rs.getString(1)));
                p_info.add(new JLabel("商品名称"), CENTER_ALIGNMENT);
                p_info.add(L_name = new JLabel(rs.getString(2)));
                p_info.add(new JLabel("库存"), CENTER_ALIGNMENT);
                p_info.add(L_stock = new JLabel(rs.getString(7)));
                p_info.add(new JLabel("库存警告"), CENTER_ALIGNMENT);
                p_info.add(L_Storewarn = new JLabel(rs.getString(14)));

                JPanel p_button = new JPanel(new BorderLayout());
                p_button.add(B_add = new JButton("加入进货单"));
                B_add.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //"商品ID","进货价","进货数量","进货折扣","总价","操作"
                        String id = L_id.getText();
                        Object[] add = {id, 0, 0, 1, 0};
                        ag.goods.addRow(add);
                    }
                });

                this.add(l_Goods_Image, BorderLayout.WEST);
                this.add(p_info, BorderLayout.CENTER);
                this.add(p_button, BorderLayout.EAST);
                //}
                //}
            } else {
                JOptionPane.showMessageDialog(null, "没有查询到相关商品", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }
}
