package MY;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class Demo {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        String[] columnIdentifiers = {"one", "two", "three"};//表头
        String[][] data = new String[5][3];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                if(j == 2) {
                    continue;
                }
                data[i][j] = "asd";
            }
        }
        model.setDataVector(data, columnIdentifiers);
        table.setModel(model);

        table.getColumnModel().getColumn(2).setCellRenderer(new TableCellRendererButton());
        table.getColumnModel().getColumn(2).setCellEditor(new TableCellEditorButton());

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);
        frame.setBounds(100, 100, 300, 200);
        frame.setVisible(true);
    }



}

class TableCellRendererButton implements TableCellRenderer{



    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        JButton button = new JButton("按钮");
        return button;
    }

}

class TableCellEditorButton extends DefaultCellEditor{

    private JButton btn;
    public TableCellEditorButton() {
        super(new JTextField());
        //设置点击一次就激活，否则默认好像是点击2次激活。
        this.setClickCountToStart(1);
        btn = new JButton("按钮");
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("按钮事件触发----");

            }
        });

    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        return btn;
    }


}
 