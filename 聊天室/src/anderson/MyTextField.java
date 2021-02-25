package anderson;

import javax.swing.*;
import java.awt.*;

public class MyTextField extends JTextField {
    public MyTextField() {
    	//设置字体与布局
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
        this.setCaretColor(Color.LIGHT_GRAY);
        this.setFont(new Font("楷体", Font.BOLD, 16));


    }
}
