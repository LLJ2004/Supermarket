package MY;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class EmployeeInfoJPanel extends JPanel {
    protected JTextField tf_eID,tf_eName,tf_eAge,tf_ePhone,tf_eJob,tf_eSalary;
    //public JComboBox<String> combox_job;
    //private static String[] job = {"管理员","会员号","预订单","",""};

    public EmployeeInfoJPanel(){
        this.setLayout(new GridLayout(6,2));
        this.add(new JLabel("职工号",SwingConstants.CENTER),CENTER_ALIGNMENT);
        this.add(tf_eID=new JTextField());
        this.add(new JLabel("姓名",SwingConstants.CENTER),CENTER_ALIGNMENT);
        this.add(tf_eName=new JTextField());
        this.add(new JLabel("年龄",SwingConstants.CENTER),CENTER_ALIGNMENT);
        this.add(tf_eAge=new JTextField());
        this.add(new JLabel("电话",SwingConstants.CENTER),CENTER_ALIGNMENT);
        this.add(tf_ePhone=new JTextField());
        this.add(new JLabel("职务",SwingConstants.CENTER),CENTER_ALIGNMENT);
        this.add(tf_eJob=new JTextField());
        this.add(new JLabel("工资",SwingConstants.CENTER),CENTER_ALIGNMENT);
        this.add(tf_eSalary=new JTextField());
        tf_eID.setEditable(false);
        tf_eName.setEditable(false);
        tf_eAge.setEditable(false);
        tf_ePhone.setEditable(false);
        tf_eJob.setEditable(false);
        tf_eSalary.setEditable(false);
    }

    public static void main(String[] args) {
        JFrame jf=new JFrame();
        jf.setBounds(300,300,200,600);
        jf.add(new EmployeeInfoJPanel());
        jf.setVisible(true);
    }
}
