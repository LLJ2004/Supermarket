package MY;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class test1 {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Dialog Example");

        // 创建一个按钮
        JButton button = new JButton("点击我");

        // 给按钮添加点击事件监听器
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 弹出一个对话框
                int choice = JOptionPane.showConfirmDialog(frame, "确定要继续执行吗？", "提示", JOptionPane.OK_CANCEL_OPTION);
                if (choice == JOptionPane.OK_OPTION) {
                    // 用户点击了确定
                    System.out.println("用户点击了确定，继续执行");
                    // 在这里添加需要执行的逻辑
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(button);
        frame.pack();
        frame.setVisible(true);
    }
}
