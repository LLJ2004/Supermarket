package MY;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SEJPanel extends JPanel implements ActionListener{
    protected DefaultTableModel sell;
    protected JTable table_sell;
    private static String[] columns={"职工号","姓名","电话","年龄","职务","薪水","修改","解雇"};
    protected Connection dbConn;
    static String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
    static String userName = "sa";
    static String userPwd = "llj2208long";

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    class TableCellRendererButton implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JButton button = new JButton("操作");
            return button;
        }

    }

    class TableCellEditorButtonDelete extends DefaultCellEditor {

        private JButton btn;

        public TableCellEditorButtonDelete() {
            super(new JTextField());
            //设置点击一次就激活，否则默认好像是点击2次激活。
            this.setClickCountToStart(1);
            btn = new JButton("解雇");
            btn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // 弹出一个对话框
                    int choice = JOptionPane.showConfirmDialog(null, "确定解雇该员工？", "提示", JOptionPane.OK_CANCEL_OPTION);
                    if (choice == JOptionPane.OK_OPTION) {
                        // 用户点击了确定
                        System.out.println("用户点击了确定，继续执行");
                        // 在这里添加需要执行的逻辑
                        JButton btn = (JButton)(e.getSource());
                        ChangeEvent ce = new ChangeEvent(btn);
                        table_sell.editingStopped(ce);
                        //以上三行非常关键！！！没有以上三行当点击第一行delete按钮时，会删除当前行数据，但是继续点击第一行delete按钮，就会出现ArrayIndexOutOfBoundsException异常，原因就是：因为JTable中的editor值变化当时，并不会引发JTable的变化，只有焦点不在该editor时，才会有消息通知JTable，会生成一个javax.swing.AbstractCellEditor.fireEditingStopped消息，继而引发javax.swing.JTable.editingStopped事件，但事件发生时，该行已经被删除了，因而出现上面的错误。按钮作为示例，如果时其他组件则根据情况变化

                        String sql = "DELETE Employee WHERE Employee_ID=?";
                        PreparedStatement statement = null;
                        PreparedStatement stmt = null;
                        try {
                            statement = dbConn.prepareStatement(sql);
                            statement.setString(1, String.valueOf(table_sell.getValueAt(table_sell.getSelectedRow(),0)));
                            // 执行更新操作
                            statement.execute();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        System.out.println(table_sell.getSelectedRow());
                        DefaultTableModel model = (DefaultTableModel)(table_sell.getModel());
                        model.removeRow(table_sell.getSelectedRow());
                    }
                }
            });

        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            return btn;
        }

    }

    class TableCellEditorButtonAlter extends DefaultCellEditor {

        private JButton btn;

        public TableCellEditorButtonAlter() {
            super(new JTextField());
            //设置点击一次就激活，否则默认好像是点击2次激活。
            this.setClickCountToStart(1);
            btn = new JButton("修改");
            btn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame alter=new JFrame("修改职工信息");
                    alter.setSize(600,600);
                    JButton btn = (JButton)(e.getSource());
                    ChangeEvent ce = new ChangeEvent(btn);
                    table_sell.editingStopped(ce);
                    AEIJPanel aei=new AEIJPanel(0);
                    alter.add(aei);

                    PreparedStatement stmt = null;
                    String sql1 = "select * from Employee where Employee_ID= '" + table_sell.getValueAt(table_sell.getSelectedRow(),0) + "'";
                    try {
                        stmt=dbConn.prepareStatement(sql1);
                        ResultSet resultSet;
                        resultSet = stmt.executeQuery();
                        resultSet.next();
                        aei.tf_eID.setText(resultSet.getString(1));
                        aei.tf_eName.setText(resultSet.getString(2));
                        aei.tf_eSalary.setText(resultSet.getString(3));
                        aei.tf_ePhone.setText(resultSet.getString(5));
                        aei.tf_eAge.setText(resultSet.getString(7));
                        int JID=0;
                        String Jid=resultSet.getString(4);
                        switch (Jid){
                            case "A":
                                JID=0;
                                break;
                            case "B":
                                JID=1;
                                break;
                            case "C":
                                JID=2;
                                break;
                            case "D":
                                JID=3;
                                break;
                            case "Z":
                                JID=4;
                                break;
                        }
                        aei.combox_job.setSelectedIndex(JID);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    alter.setVisible(true);
                    System.out.println(table_sell.getSelectedRow());
                }
            });

        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            return btn;
        }
    }

    public SEJPanel(){
        try {
            Class.forName(driverName);
            this.dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("Succeed!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed!");
        }
        sell=new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                // 禁止编辑第三列（索引为2）的单元格
                return column != 0 && column != 1 && column != 2 && column != 3 && column !=4 && column !=5;
            }
        };
        sell.setColumnIdentifiers(columns);
        table_sell=new JTable(sell);
        table_sell.getColumnModel().getColumn(7).setCellRenderer(new TableCellRendererButton());
        table_sell.getColumnModel().getColumn(7).setCellEditor(new TableCellEditorButtonDelete());
        table_sell.getColumnModel().getColumn(6).setCellRenderer(new TableCellRendererButton());
        table_sell.getColumnModel().getColumn(6).setCellEditor(new TableCellEditorButtonAlter());

        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(table_sell),BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Table Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SEJPanel table=new SEJPanel();
        Object[] data0={"003", "000", "000", "000", "000","000"};
        table.sell.addRow(data0);
        Object[] data1={"004", "000", "000", "000", "000"};
        table.sell.addRow(data1);
        Object[] data3={"005", "000", "000", "000", "000"};
        table.sell.addRow(data3);
        frame.add(table);

        frame.pack();
        frame.setVisible(true);
    }
}
