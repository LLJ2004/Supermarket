package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;

public class OrderDetailsJPanel extends JPanel implements ActionListener {
    protected JTextField tf_Search;
    protected JLabel l_Price,l_searchid;
    protected JButton B_Search,B_Check,B_Pay,B_Back;
    protected String sellID;
    protected double VIPdiscount;

    protected GridLayout grid;
    protected JPanel sp1;
    protected AddGoodsJPanel ag;
    protected Connection dbConn;
    static String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
    static String userName = "sa";
    static String userPwd = "llj2208long";

    public OrderDetailsJPanel(String sellID,double VIPdiscount) throws SQLException {
        try {
            Class.forName(driverName);
            this.dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("Succeed!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed!");
        }

        this.sellID=sellID;
        this.VIPdiscount=VIPdiscount;

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
        p2.add(B_Check=new JButton("结账"));


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
        JPanel p5=new JPanel(new BorderLayout());
        this.add(p5,2);
        p5.add(new JScrollPane(ag=new AddGoodsJPanel()));
        ag.setEnabled(false);
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
                String sql = "INSERT INTO SellInfo (Goods_ID, Sell_ID,SellInfo_Quantity,SellInfo_UnitPrice,SellInfo_Discount,SellInfo_Total,Goods_Type) VALUES (?, ?,?,?,?,?,?)";
                PreparedStatement statement = null;
                PreparedStatement stmt = null;
                try {
                    statement = dbConn.prepareStatement(sql);
                    statement.setString(1, String.valueOf(ag.table_add.getValueAt(i,0))) ;
                    statement.setString(2, sellID);
                    statement.setInt(3, Integer.parseInt(String.valueOf(ag.table_add.getValueAt(i,2))));
                    statement.setDouble(4, Double.parseDouble(String.valueOf(ag.table_add.getValueAt(i,1))));
                    statement.setDouble(5, Double.parseDouble(String.valueOf(ag.table_add.getValueAt(i,3))));
                    statement.setDouble(6, Double.parseDouble(String.valueOf(ag.table_add.getValueAt(i,4))));
                    String sql1 = "select * from Goods where Goods_ID= '" + String.valueOf(ag.table_add.getValueAt(i,0)) + "'";
                    stmt=dbConn.prepareStatement(sql1);
                    ResultSet resultSet;
                    resultSet = stmt.executeQuery();
                    resultSet.next();
                    statement.setString(7, resultSet.getString(4));
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
            String sql1 = "select * from Sell where Sell_ID= '" + sellID + "'";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                resultSet.next();
                l_Price.setText(resultSet.getString(4)+"元");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            l_Price.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    public static void main(String[] args) throws SQLException {
        JFrame jf=new JFrame();
        jf.setBounds(300,300,200,600);
        jf.add(new OrderDetailsJPanel("000",0.9));
        jf.setVisible(true);
    }

    class SearchResult_GoodsJPanel extends JPanel implements ActionListener{
        protected JButton B_add,B_info;
        protected JLabel L_id,L_unit,L_stock,L_name;
        public SearchResult_GoodsJPanel(ResultSet rs) throws SQLException, MalformedURLException {
            if(rs.next()){
                //rs.beforeFirst();
                //while(rs.next()) {
                //while (rs.next()) {
                this.setLayout(new BorderLayout());

                JLabel l_Goods_Image = new JLabel();
                String url = rs.getString(8);
                URL imageUrl = new URL(url);
                ImageIcon imageIcon = new ImageIcon(imageUrl);
                l_Goods_Image.setIcon(imageIcon);

                JPanel p_info=new JPanel(new GridLayout(2,4));
                p_info.add(new JLabel("商品ID"),CENTER_ALIGNMENT);
                p_info.add(L_id=new JLabel(rs.getString(1)));
                p_info.add(new JLabel("商品名称"),CENTER_ALIGNMENT);
                p_info.add(L_name=new JLabel(rs.getString(2)));
                p_info.add(new JLabel("库存"),CENTER_ALIGNMENT);
                p_info.add(L_stock=new JLabel(rs.getString(7)));
                p_info.add(new JLabel("商品单价"),CENTER_ALIGNMENT);
                p_info.add(L_unit=new JLabel(rs.getString(3)));

                JPanel p_button=new JPanel(new GridLayout(1,2));
                p_button.add(B_add=new JButton("加入订单"));
                p_button.add(B_info=new JButton("商品详情"));
                B_add.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String id=L_id.getText();
                        String unitprice=L_unit.getText();
                        String discount= null;
                        try {
                            discount = String.valueOf(Double.parseDouble(rs.getString(10))*VIPdiscount);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        String[] add={id,unitprice,"0",discount,"0"};
                        ag.goods.addRow(add);
                    }
                });
                B_info.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame info=new JFrame("商品详情");
                        info.setLayout(new BorderLayout());

                        JLabel l_Goods_Image = new JLabel();
                        String url = null;
                        try {
                            url = rs.getString(8);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        URL imageUrl = null;
                        try {
                            imageUrl = new URL(url);
                        } catch (MalformedURLException ex) {
                            throw new RuntimeException(ex);
                        }
                        ImageIcon imageIcon = new ImageIcon(imageUrl);
                        l_Goods_Image.setIcon(imageIcon);

                        JPanel p1=new JPanel(new BorderLayout());
                        JPanel p2=new JPanel(new GridLayout(4,2));
                        JScrollPane p3=new JScrollPane();

                        p1.add(p2,BorderLayout.NORTH);
                        p1.add(p3);
                        try {
                            p2.add(new JLabel("商品ID："+rs.getString(1)));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            p2.add(new JLabel("商品名称："+rs.getString(2)));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            p2.add(new JLabel("商品类型："+rs.getString(4)));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        };
                        try {
                            p2.add(new JLabel("商品单价："+rs.getString(3)));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        };
                        try {
                            p2.add(new JLabel("有效期:"+rs.getString(6)));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            p2.add(new JLabel("库存:"+rs.getString(7)));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            p2.add(new JLabel("折扣"+rs.getString(10)));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            p2.add(new JLabel("状态"+rs.getString(11)));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            p3.add(new JTextArea(rs.getString(9)));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        info.add(l_Goods_Image,BorderLayout.WEST);
                        info.add(p1);
                        info.setVisible(true);
                    }
                });

                this.add(l_Goods_Image,BorderLayout.WEST);
                this.add(p_info,BorderLayout.CENTER);
                this.add(p_button,BorderLayout.EAST);
                //}
                //}
            }
            else{
                JOptionPane.showMessageDialog(null, "没有查询到相关商品", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }

//        public static void main(String args[]) throws ClassNotFoundException, SQLException, MalformedURLException {
//            String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
//            String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
//            String userName = "sa";
//            String userPwd = "llj2208long";
//            Class.forName(driverName);
//            Connection dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
//            JFrame jf=new JFrame();
//            jf.setBounds(300,300,110,320);
//            Statement stmt = dbConn.createStatement();
//            String sql = "select * from Goods where Goods_ID= 'A324'";
//            ResultSet resultSet= stmt.executeQuery(sql);
//
//            jf.add(new MY.SearchResult_GoodsJPanel(resultSet));
//            jf.setVisible(true);
//        }
    }
//    使用Java Swing的JLabel组件：
//
//        如果你在Java Swing应用程序中展示图片，可以使用JLabel组件，并设置Icon为通过HTTP访问图片的URL。例如：
//        JLabel label = new JLabel();
//        URL url = new URL("http://example.com/image.jpg");
//        ImageIcon imageIcon = new ImageIcon(url);
//        label.setIcon(imageIcon);
//        这样就可以在Java Swing界面上展示HTTP形式存储的图片。
//where Goods_ID= 'A324'
}
