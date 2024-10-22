package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PMJPanle extends JPanel implements ActionListener {
    protected JComboBox<String> combox_searchtype;
    private static String[] searchtype = {"职工号","职工姓名","职务","所有员工"};
    protected JComboBox<String> combox_job;
    private static String[] job = {"收银员","仓库管理员","商品管理员","待定职务"};
    protected JButton B_add,B_search;
    protected JTextField tf_eID,tf_ename,tf_ejob;
    protected JPanel p2;
    protected Connection dbConn;
    static String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://192.168.248.1;DatabaseName=supermarket";
    static String userName = "sa";
    static String userPwd = "llj2208long";

    public PMJPanle(){
        try {
            Class.forName(driverName);
            this.dbConn= DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("Succeed!");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed!");
        }
        this.setLayout(new BorderLayout());
        JPanel p1=new JPanel(new GridLayout(5,2));
        p2=new JPanel(new BorderLayout());

        this.add(p1,BorderLayout.WEST);
        this.add(p2);
        p1.add(new JLabel("查询条件"),CENTER_ALIGNMENT);
        p1.add(combox_searchtype=new JComboBox<>(searchtype));
        p1.add(new JLabel("职工号"),CENTER_ALIGNMENT);
        p1.add(tf_eID=new JTextField());
        p1.add(new JLabel("职工姓名"),CENTER_ALIGNMENT);
        p1.add(tf_ename=new JTextField());
        p1.add(new JLabel("职务"),CENTER_ALIGNMENT);
        p1.add(combox_job= new JComboBox<>(job));
        p1.add(B_add=new JButton("添加员工"));
        p1.add(B_search=new JButton("查询员工"));
        p2.add(new AEIJPanel(1),BorderLayout.CENTER);

        B_add.addActionListener(this);
        B_search.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(B_add)) {
            p2.removeAll();
            p2.add(new AEIJPanel(1),BorderLayout.CENTER);
            p2.validate();
            p2.repaint();
        }
        if(e.getSource().equals(B_search)){
            p2.removeAll();
            SEJPanel se=new SEJPanel();

            String type= String.valueOf(combox_searchtype.getSelectedItem());
            Statement stmt = null;
            String sql=null;
            try {
                stmt = dbConn.createStatement();

                switch (type){
                    //"职工号","职工姓名","职务"
                    case "职工号":
                        sql= "select * from Employee where Employee_ID= '" + tf_eID.getText() + "'";
                        break;
                    case "职工姓名":
                        sql = "select * from Employee where Employee_Name like '%'+'" + tf_ename.getText() + "'+'%'";
                        break;
                    case "职务":
                        int JID=combox_job.getSelectedIndex();
                        String Jid=null;
                        switch (JID){
                            case 0:
                                Jid="B";
                                break;
                            case 1:
                                Jid="C";
                                break;
                            case 2:
                                Jid="D";
                                break;
                            case 3:
                                Jid="Z";
                                break;
                        }
                        sql = "select * from Employee where Job_ID='" + Jid + "'";
                        break;
                    case "所有员工":
                        sql= "select * from Employee";
                        break;
                }

                ResultSet resultSet;
                resultSet = stmt.executeQuery(sql);
                while (resultSet.next()){
                    //"职工号","姓名","电话","年龄","职务","薪水","修改","解雇"
                    Object[] qs={resultSet.getString(1),resultSet.getString(2),resultSet.getString(5),resultSet.getString(7),resultSet.getString(4),resultSet.getString(3)};
                    se.sell.addRow(qs);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            p2.add(se);
            p2.validate();
            p2.repaint();
        }
    }
    public static void main(String[] args) {
        JFrame jf=new JFrame();
        jf.setBounds(300,300,200,600);
        jf.add(new PMJPanle());
        jf.setVisible(true);
    }
}
