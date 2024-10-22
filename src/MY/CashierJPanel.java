package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CashierJPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    protected JButton B_Cash,B_Customer,B_Info,B_Return;
    protected JPanel p2;
    protected String eID;

    public CashierJPanel(String eid){
        this.setLayout(new BorderLayout());
        JPanel p1=new JPanel(new GridLayout(4,1));
        p2=new JPanel(new BorderLayout());
        eID=eid;

        p1.add(B_Cash=new JButton("收银"));
        p1.add(B_Customer=new JButton("客户管理"));
        p1.add(B_Info=new JButton("个人信息"));
        p1.add(B_Return=new JButton("退出登录"));

        B_Cash.addActionListener(this);
        B_Customer.addActionListener(this);
        B_Info.addActionListener(this);
        B_Return.addActionListener(this);

        this.add(p1,BorderLayout.WEST);
        this.add(p2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(B_Cash)){
            p2.removeAll();
            p2.add(new CashJPanel());
            p2.validate();
            p2.repaint();
        }
        if(e.getSource().equals(B_Customer)){
            p2.removeAll();
            p2.add(new CustomerJPanel());
            p2.validate();
            p2.repaint();
        }
        if(e.getSource().equals(B_Info)){
            p2.removeAll();
            try {
                p2.add(new EmployeeJPanel(eID));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            p2.validate();
            p2.repaint();
        }
        if(e.getSource().equals(B_Return)){
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "确定退出登陆吗?",
                    "退出",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                this.removeAll();
                try {
                    this.add(new LoginJPanel());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                this.validate();
                this.repaint();
            }
        }
    }

    public static void main(String[] args) {
        JFrame jf=new JFrame();
        jf.setBounds(300,300,200,600);
        jf.add(new CashierJPanel("B0004"));
        jf.setVisible(true);
    }
}
