package MY;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GoodsInfoJPanel extends JPanel implements ActionListener {
    public JTextField tf_Goods_ID;
    public JTextField tf_Goods_Name;
    public JTextField tf_Goods_UnitPrice;
    public JTextField tf_Goods_Image;
    public JTextArea tf_Goods_Info;
    public JTextField tf_Goods_Show;
    public JTextField tf_Store_ID;
    public JComboBox<String> combox_type;
    private static String[] type = {"日用品","食品","家电","数码产品","服装","文具","玩具","其他"};
    public GoodsInfoJPanel(){
        this.setLayout(new GridLayout(8,1));
        JPanel p1=new JPanel(new GridLayout(1,2));
        JPanel p2=new JPanel(new GridLayout(1,2));
        JPanel p3=new JPanel(new GridLayout(1,2));
        JPanel p4=new JPanel(new GridLayout(1,2));
        JPanel p5=new JPanel(new GridLayout(1,2));
        JPanel p6=new JPanel(new GridLayout(1,2));
        JPanel p7=new JPanel(new GridLayout(1,2));
        JPanel p8=new JPanel(new GridLayout(1,2));

        p1.add(new JLabel("商品号"));
        p1.add(tf_Goods_ID=new JTextField());
        p2.add(new JLabel("商品名"));
        p2.add(tf_Goods_Name=new JTextField());
        p3.add(new JLabel("商品单价"));
        p3.add(tf_Goods_UnitPrice=new JTextField());
        p4.add(new JLabel("商品图片"));
        p4.add(tf_Goods_Image=new JTextField());
        p8.add(new JLabel("商品详情"));
        p8.add(tf_Goods_Info= new JTextArea());
        p6.add(new JLabel("商品展示区域"));
        p6.add(tf_Goods_Show=new JTextField());
        p7.add(new JLabel("商品存放仓库"));
        p7.add(tf_Store_ID=new JTextField());
        p5.add(new JLabel("商品类型"));
        p5.add(combox_type=new JComboBox<>(type));

        this.add(p1);
        this.add(p2);
        this.add(p3);
        this.add(p4);
        this.add(p5);
        this.add(p6);
        this.add(p7);
        this.add(p8);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
