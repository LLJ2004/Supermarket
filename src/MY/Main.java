package MY;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        System.out.println("Hello world!");

        JFrame jf=new JFrame();
        jf.setBounds(300,300,900,600);
        jf.add(new LoginJPanel());
        //设置相对位置：屏幕中间
        jf.setLocationRelativeTo(null);

        //确保使用窗口关闭按钮，能够正常退出，结束进程！
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //禁止对窗口大小进行缩放处理
        jf.setResizable(true);
        jf.setVisible(true);
    }
}