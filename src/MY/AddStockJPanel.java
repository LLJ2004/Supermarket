package MY;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddStockJPanel extends JPanel {
    protected DefaultTableModel goods;
    protected JTable table_add;
    private static String[] columns={"商品ID","进货价","进货数量","进货折扣","总价","操作"};
    class TableCellRendererButton implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JButton button = new JButton("删除商品");
            return button;
        }

    }

    class TableCellEditorButton extends DefaultCellEditor {

        private JButton btn;

        public TableCellEditorButton() {
            super(new JTextField());
            //设置点击一次就激活，否则默认好像是点击2次激活。
            this.setClickCountToStart(1);
            btn = new JButton("删除商品");
            btn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton btn = (JButton)(e.getSource());
                    ChangeEvent ce = new ChangeEvent(btn);
                    table_add.editingStopped(ce);
                    //以上三行非常关键！！！没有以上三行当点击第一行delete按钮时，会删除当前行数据，但是继续点击第一行delete按钮，就会出现ArrayIndexOutOfBoundsException异常，原因就是：因为JTable中的editor值变化当时，并不会引发JTable的变化，只有焦点不在该editor时，才会有消息通知JTable，会生成一个javax.swing.AbstractCellEditor.fireEditingStopped消息，继而引发javax.swing.JTable.editingStopped事件，但事件发生时，该行已经被删除了，因而出现上面的错误。按钮作为示例，如果时其他组件则根据情况变化

                    System.out.println(table_add.getSelectedRow());
                    DefaultTableModel model = (DefaultTableModel)(table_add.getModel());
                    model.removeRow(table_add.getSelectedRow());
                }
            });

        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            return btn;
        }
    }

    public AddStockJPanel(){
        goods=new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                // 禁止编辑第三列（索引为2）的单元格
                return column != 0 && column != 4 && column != 5  ;
            }
        };
        goods.setColumnIdentifiers(columns);
        goods.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int type = e.getType();// 获得事件的类型

                int row = e.getFirstRow();// 获得触发此次事件的表格行索引
                System.out.println("row:" + row);
                int column = e.getColumn();// 获得触发此次事件的表格列索引
                System.out.println("column:" + column);

                if (column == 4) return; //这个判断必须加，哪一列需要自动修改，就在那一列return，不然会出现异常报错。

                if (type == TableModelEvent.UPDATE) {
                    TableModel model = (TableModel) e.getSource();// 自动获取表格模型

                    // 合法性检查
//                    Object value = model.getValueAt(row, column); // 使用TableModel的getValueAt()方法
//                    if (value != null && !value.toString().isEmpty()) {
//                        String cellValue = value.toString();
//                        try {
//                            double number = Double.parseDouble(cellValue);
//                            if (number < 0) {
//                                JOptionPane.showMessageDialog(null, "请输入一个正数");
//                                return;
//                            }
//                        } catch (NumberFormatException ex) {
//                            JOptionPane.showMessageDialog(null, "请输入一个有效数值");
//                            return;
//                        }
//                    } else {
//                        JOptionPane.showMessageDialog(null, "请输入一个有效数值");
//                        return;
//                    }


                    double UnitPrice = Double.parseDouble(table_add.getValueAt(row, 1).toString());
                    int Quantity = Integer.parseInt(table_add.getValueAt(row, 2).toString());
                    double Discount = Double.parseDouble(table_add.getValueAt(row, 3).toString());
                    table_add.setValueAt((UnitPrice * Quantity * Discount), row, 4);
                }

            }
        });
        table_add=new JTable(goods);
        table_add.getColumnModel().getColumn(5).setCellRenderer(new TableCellRendererButton());
        table_add.getColumnModel().getColumn(5).setCellEditor(new TableCellEditorButton());
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(table_add),BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Table Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Object[] data={"A000", 100, 0, 0.5, 0};


        AddStockJPanel table= new AddStockJPanel();
        table.goods.addRow(data);
        table.goods.addRow(data);
        table.goods.addRow(data);
        frame.add(table);

        frame.pack();
        frame.setVisible(true);
    }
}
