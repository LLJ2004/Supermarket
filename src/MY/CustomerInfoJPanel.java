package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerInfoJPanel extends JPanel implements ActionListener {
    public JTextField tf_Customer_ID;
    public JTextField tf_Customer_Name;
    public JTextField tf_VIP_ID;
    public JTextField tf_Customer_Deposit;
    public JTextField tf_Customer_Phone;
    public JTextField tf_Customer_Birthday_YY;
    public JTextField tf_Customer_Birthday_MM;
    public JTextField tf_Customer_Birthday_DD;
    public CustomerInfoJPanel(){
        this.setLayout(new GridLayout(6,1));
        JPanel p1=new JPanel(new GridLayout(1,2));
        JPanel p2=new JPanel(new GridLayout(1,2));
        JPanel p3=new JPanel(new GridLayout(1,2));
        JPanel p4=new JPanel(new GridLayout(1,2));
        JPanel p5=new JPanel(new GridLayout(1,2));
        JPanel p6=new JPanel(new GridLayout(1,7));

        p1.add(new JLabel("会员号"));
        p1.add(tf_Customer_ID=new JTextField());
        p2.add(new JLabel("会员姓名"));
        p2.add(tf_Customer_Name=new JTextField());
        p3.add(new JLabel("会员等级"));
        p3.add(tf_VIP_ID=new JTextField());
        p4.add(new JLabel("会员积分"));
        p4.add(tf_Customer_Deposit=new JTextField());
        p5.add(new JLabel("会员电话"));
        p5.add(tf_Customer_Phone=new JTextField());
        p6.add(new JLabel("会员生日"));
        p6.add(tf_Customer_Birthday_YY=new JTextField());
        p6.add(new JLabel("年"));
        p6.add(tf_Customer_Birthday_MM=new JTextField());
        p6.add(new JLabel("月"));
        p6.add(tf_Customer_Birthday_DD=new JTextField());
        p6.add(new JLabel("日"));
        this.add(p1);
        this.add(p2);
        this.add(p3);
        this.add(p4);
        this.add(p5);
        this.add(p6);

    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
