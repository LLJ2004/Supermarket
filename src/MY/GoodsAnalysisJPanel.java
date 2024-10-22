package MY;

import MY.SSJPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class GoodsAnalysisJPanel extends JPanel implements ActionListener {
    protected JButton B_rank_g,B_rank_t,B_Outdatewarn,B_Storewarn;
    protected JPanel panel;
    protected JTable t_q;
    protected DefaultTableModel qr;
    protected JScrollPane sp;
    private static String[] GoodsDate={"商品号","商品名称","有效期","过期警告"};
    private static String[] GoodsStore={"商品号","商品名称","库存量","库存警告"};

    private static String[] rank_g={"商品号","商品名称","销售量"};
    private static String[] rank_t={"商品种类","销售量"};
    protected Connection dbConn;
    static String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
    static String userName = "sa";
    static String userPwd = "llj2208long";

    public GoodsAnalysisJPanel(){
        try {
            Class.forName(driverName);
            this.dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("Succeed!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed!");
        }
        this.setLayout(new BorderLayout());
        JPanel p1=new JPanel(new GridLayout(4,1));
        panel=new JPanel(new BorderLayout());

        this.add(p1,BorderLayout.WEST);
        this.add(panel,BorderLayout.CENTER);

        p1.add(B_rank_g=new JButton("商品销售排行"));
        p1.add(B_rank_t=new JButton("种类销售排行"));
        p1.add(B_Outdatewarn=new JButton("过期警告"));
        p1.add(B_Storewarn=new JButton("库存警告"));


        B_rank_g.addActionListener(this);
        B_rank_t.addActionListener(this);
        B_Outdatewarn.addActionListener(this);
        B_Storewarn.addActionListener(this);

        qr=new DefaultTableModel();
        qr.setColumnIdentifiers(rank_g);
        t_q=new JTable(qr);
        t_q.setEnabled(false);
        sp=new JScrollPane(t_q);
        panel.add(sp);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(B_rank_g)){
            //移除所有行
            while (qr.getRowCount() > 0) {
                qr.removeRow(0);
            }

            qr.setColumnIdentifiers(rank_g);
            // 刷新 JTable，使其显示更新后的数据
            qr.fireTableDataChanged(); // 这一步将触发 JTable 的重新绘制，显示没有行的空表格

            PreparedStatement stmt = null;
            String sql1 = "exec Rank_Sell_ID";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                while (resultSet.next()){
                    //"商品号","商品名称","销售量"
                    Object[] qs={resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)};
                    qr.addRow(qs);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (e.getSource().equals(B_rank_t)){
            //移除所有行
            while (qr.getRowCount() > 0) {
                qr.removeRow(0);
            }

            qr.setColumnIdentifiers(rank_t);
            // 刷新 JTable，使其显示更新后的数据
            qr.fireTableDataChanged(); // 这一步将触发 JTable 的重新绘制，显示没有行的空表格

            PreparedStatement stmt = null;
            String sql1 = "exec Rank_Sell_Goods_Type";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                while (resultSet.next()){
                    //"商品种类","销售量"
                    Object[] qs={resultSet.getString(1),resultSet.getString(2)};
                    qr.addRow(qs);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (e.getSource().equals(B_Outdatewarn)){
            //移除所有行
            while (qr.getRowCount() > 0) {
                qr.removeRow(0);
            }

            qr.setColumnIdentifiers(GoodsDate);
            // 刷新 JTable，使其显示更新后的数据
            qr.fireTableDataChanged(); // 这一步将触发 JTable 的重新绘制，显示没有行的空表格

            //"商品号","商品名称","有效期","过期警告"
            PreparedStatement stmt = null;
            String sql1 = "select Goods_ID,Goods_Name,Goods_Qualitydate,Goods_Outdatewarn from Goods where Goods_Outdatewarn= '过期警告'";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                while (resultSet.next()){
                    //"月份","月流水"
                    Object[] qs={resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)};
                    qr.addRow(qs);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (e.getSource().equals(B_Storewarn)){
            //移除所有行
            while (qr.getRowCount() > 0) {
                qr.removeRow(0);
            }

            qr.setColumnIdentifiers(GoodsStore);
            // 刷新 JTable，使其显示更新后的数据
            qr.fireTableDataChanged(); // 这一步将触发 JTable 的重新绘制，显示没有行的空表格

            //"商品号","商品名称","库存警告","库存量"
            PreparedStatement stmt = null;
            String sql1 = "select Goods_ID,Goods_Name,Goods_Stock,Goods_Storewarn from Goods where Goods_Storewarn= '库存警告'";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                while (resultSet.next()){
                    //"月份","月流水"
                    Object[] qs={resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)};
                    qr.addRow(qs);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void main(String[] args) {
        JFrame jf=new JFrame();
        jf.setBounds(300,300,200,600);
        jf.add(new SSJPanel());
        jf.setVisible(true);
    }
}
