package MY;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SSJPanel extends JPanel implements ActionListener {
    protected JButton B_cq,B_gq,B_sq,B_xsq,B_rsq,B_oq,B_roq,B_rank_g,B_rank_t,B_day,B_week,B_month;
    protected JPanel panel;
    protected JTable t_q;
    protected DefaultTableModel qr;
    protected JScrollPane sp;
    private static String[] Customers={"会员号","姓名","VIP等级","电话","生日","积分"};
    private static String[] Goods={"商品号","商品名称","商品单价","折扣","商品类型","仓库号","有效期","过期警告","库存警告","库存量","商品图片","商品详细信息","商品状态","展示区域"};
    private static String[] Suppliers={"供应商号","供应商姓名","地址","联系电话"};
    private static String[] Sell={"销售单号","会员号","职工号","销售单总价","销售单开始日期","销售单完成日期","销售单种类"};
    private static String[] Return_={"销售单号","职工号","退货日期","退货手续费","退货备注"};
    private static String[] Order={"进货单号","供应商号","发货日期","预计到达日期","进货状态","进货完成日期","进货总价"};
    private static String[] Order_r={"进货单号","供应商号","进货状态","进货总价"};
    private static String[] rank_g={"商品号","商品名称","销售量"};
    private static String[] rank_t={"商品种类","销售量"};
    private static String[] day={"日期","日流水"};
    private static String[] week={"周","周流水"};
    private static String[] month={"月份","月流水"};
    protected Connection dbConn;
    static String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
    static String userName = "sa";
    static String userPwd = "llj2208long";

    public SSJPanel(){
        try {
            Class.forName(driverName);
            this.dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("Succeed!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed!");
        }
        this.setLayout(new BorderLayout());
        JPanel p1=new JPanel(new GridLayout(12,1));
        panel=new JPanel(new BorderLayout());

        this.add(p1,BorderLayout.WEST);
        this.add(panel,BorderLayout.CENTER);

        p1.add(B_cq=new JButton("客户查询"));
        p1.add(B_gq=new JButton("商品查询"));
        p1.add(B_sq=new JButton("供应商查询"));
        p1.add(B_xsq=new JButton("销售查询"));
        p1.add(B_rsq=new JButton("销售退货查询"));
        p1.add(B_oq=new JButton("入库查询"));
        p1.add(B_roq=new JButton("入库退货查询"));
        p1.add(B_rank_g=new JButton("商品销售排行"));
        p1.add(B_rank_t=new JButton("种类销售排行"));
        p1.add(B_day=new JButton("日流水"));
        p1.add(B_week=new JButton("周流水"));
        p1.add(B_month=new JButton("月流水"));
        B_cq.addActionListener(this);
        B_gq.addActionListener(this);
        B_sq.addActionListener(this);
        B_xsq.addActionListener(this);
        B_rsq.addActionListener(this);
        B_oq.addActionListener(this);
        B_roq.addActionListener(this);
        B_rank_g.addActionListener(this);
        B_rank_t.addActionListener(this);
        B_day.addActionListener(this);
        B_week.addActionListener(this);
        B_month.addActionListener(this);

        qr=new DefaultTableModel();
        qr.setColumnIdentifiers(Customers);
        t_q=new JTable(qr);
        t_q.setEnabled(false);
        sp=new JScrollPane(t_q);
        panel.add(sp);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(B_cq)){
            //移除所有行
            while (qr.getRowCount() > 0) {
                qr.removeRow(0);
            }

            qr.setColumnIdentifiers(Customers);
            // 刷新 JTable，使其显示更新后的数据
            qr.fireTableDataChanged(); // 这一步将触发 JTable 的重新绘制，显示没有行的空表格

            PreparedStatement stmt = null;
            String sql1 = "select * from Customer";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                while (resultSet.next()){
                    //"会员号","姓名","VIP等级","电话","生日","积分"
                    Object[] qs={resultSet.getString(1),resultSet.getString(3),resultSet.getString(4),resultSet.getString(6),resultSet.getString(7),resultSet.getString(5)};
                    qr.addRow(qs);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (e.getSource().equals(B_gq)){
            //移除所有行
            while (qr.getRowCount() > 0) {
                qr.removeRow(0);
            }

            qr.setColumnIdentifiers(Goods);
            // 刷新 JTable，使其显示更新后的数据
            qr.fireTableDataChanged(); // 这一步将触发 JTable 的重新绘制，显示没有行的空表格

            PreparedStatement stmt = null;
            String sql1 = "select * from Goods";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                while (resultSet.next()){
                    //"商品号","商品名称","商品单价","折扣","商品类型","仓库号","有效期","过期警告","库存警告","库存量","商品图片","商品详细信息","商品状态","展示区域"
                    Object[] qs={resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),resultSet.getString(10),resultSet.getString(11),resultSet.getString(12),resultSet.getString(13),resultSet.getString(14)};
                    qr.addRow(qs);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (e.getSource().equals(B_sq)){
            //移除所有行
            while (qr.getRowCount() > 0) {
                qr.removeRow(0);
            }

            qr.setColumnIdentifiers(Suppliers);
            // 刷新 JTable，使其显示更新后的数据
            qr.fireTableDataChanged(); // 这一步将触发 JTable 的重新绘制，显示没有行的空表格

            PreparedStatement stmt = null;
            String sql1 = "select * from Suppliers";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                while (resultSet.next()){
                    //"供应商号","供应商姓名","地址","联系电话"
                    Object[] qs={resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)};
                    qr.addRow(qs);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (e.getSource().equals(B_xsq)){
            //移除所有行
            while (qr.getRowCount() > 0) {
                qr.removeRow(0);
            }

            qr.setColumnIdentifiers(Sell);
            // 刷新 JTable，使其显示更新后的数据
            qr.fireTableDataChanged(); // 这一步将触发 JTable 的重新绘制，显示没有行的空表格

            PreparedStatement stmt = null;
            String sql1 = "select * from Sell";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                while (resultSet.next()){
                    //"销售单号","会员号","职工号","销售单总价","销售单开始日期","销售单完成日期","销售单种类"
                    Object[] qs={resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getString(7)};
                    qr.addRow(qs);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (e.getSource().equals(B_rsq)){
            //移除所有行
            while (qr.getRowCount() > 0) {
                qr.removeRow(0);
            }

            qr.setColumnIdentifiers(Return_);
            // 刷新 JTable，使其显示更新后的数据
            qr.fireTableDataChanged(); // 这一步将触发 JTable 的重新绘制，显示没有行的空表格

            PreparedStatement stmt = null;
            String sql1 = "select * from Return_";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                while (resultSet.next()){
                    //"销售单号","职工号","退货日期","退货手续费","退货备注"
                    Object[] qs={resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5)};
                    qr.addRow(qs);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (e.getSource().equals(B_oq)){
            //移除所有行
            while (qr.getRowCount() > 0) {
                qr.removeRow(0);
            }

            qr.setColumnIdentifiers(Order);
            // 刷新 JTable，使其显示更新后的数据
            qr.fireTableDataChanged(); // 这一步将触发 JTable 的重新绘制，显示没有行的空表格

            PreparedStatement stmt = null;
            String sql1 = "select * from Orders";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                while (resultSet.next()){
                    //"进货单号","供应商号","发货日期","预计到达日期","进货状态","进货完成日期","进货总价"
                    Object[] qs={resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getString(7)};
                    qr.addRow(qs);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (e.getSource().equals(B_roq)){
            //移除所有行
            while (qr.getRowCount() > 0) {
                qr.removeRow(0);
            }

            qr.setColumnIdentifiers(Order_r);
            // 刷新 JTable，使其显示更新后的数据
            qr.fireTableDataChanged(); // 这一步将触发 JTable 的重新绘制，显示没有行的空表格

            PreparedStatement stmt = null;
            String sql1 = "select * from Orders where Orders_Condition='退货'";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                while (resultSet.next()){
                    //"进货单号","供应商号","进货状态","进货总价"
                    Object[] qs={resultSet.getString(1),resultSet.getString(2),resultSet.getString(5),resultSet.getString(7)};
                    qr.addRow(qs);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

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

        if (e.getSource().equals(B_day)){
            //移除所有行
            while (qr.getRowCount() > 0) {
                qr.removeRow(0);
            }

            qr.setColumnIdentifiers(day);
            // 刷新 JTable，使其显示更新后的数据
            qr.fireTableDataChanged(); // 这一步将触发 JTable 的重新绘制，显示没有行的空表格

            PreparedStatement stmt = null;
            String sql1 = "exec DaySell";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                while (resultSet.next()){
                    //"日期","日流水"
                    Object[] qs={resultSet.getString(1),resultSet.getString(2)};
                    qr.addRow(qs);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (e.getSource().equals(B_week)){
            //移除所有行
            while (qr.getRowCount() > 0) {
                qr.removeRow(0);
            }

            qr.setColumnIdentifiers(week);
            // 刷新 JTable，使其显示更新后的数据
            qr.fireTableDataChanged(); // 这一步将触发 JTable 的重新绘制，显示没有行的空表格

            PreparedStatement stmt = null;
            String sql1 = "exec WeekSell";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                while (resultSet.next()){
                    //"周","周流水"
                    Object[] qs={resultSet.getString(1),resultSet.getString(2)};
                    qr.addRow(qs);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (e.getSource().equals(B_month)){
            //移除所有行
            while (qr.getRowCount() > 0) {
                qr.removeRow(0);
            }

            qr.setColumnIdentifiers(month);
            // 刷新 JTable，使其显示更新后的数据
            qr.fireTableDataChanged(); // 这一步将触发 JTable 的重新绘制，显示没有行的空表格

            PreparedStatement stmt = null;
            String sql1 = "exec MonthSell\n";
            try {
                stmt=dbConn.prepareStatement(sql1);
                ResultSet resultSet;
                resultSet = stmt.executeQuery();
                while (resultSet.next()){
                    //"月份","月流水"
                    Object[] qs={resultSet.getString(1),resultSet.getString(2)};
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
