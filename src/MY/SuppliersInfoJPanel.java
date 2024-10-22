package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SuppliersInfoJPanel extends JPanel  {
    public JTextField tf_Supplier_ID;
    public JTextField tf_Supplier_Name;
    public JTextField tf_Supplier_Address;
    public JTextField tf_Supplier_Phone;


    public SuppliersInfoJPanel() {
        this.setLayout(new GridLayout(4, 1));
        JPanel p1 = new JPanel(new GridLayout(1, 2));
        JPanel p2 = new JPanel(new GridLayout(1, 2));
        JPanel p3 = new JPanel(new GridLayout(1, 2));
        JPanel p4 = new JPanel(new GridLayout(1, 2));

        p1.add(new JLabel("供应商号"));
        p1.add(tf_Supplier_ID = new JTextField());
        p2.add(new JLabel("姓名"));
        p2.add(tf_Supplier_Name = new JTextField());
        p3.add(new JLabel("地址"));
        p3.add(tf_Supplier_Address = new JTextField());
        p4.add(new JLabel("联系电话"));
        p4.add(tf_Supplier_Phone = new JTextField());
        this.add(p1);
        this.add(p2);
        this.add(p3);
        this.add(p4);
    }
}