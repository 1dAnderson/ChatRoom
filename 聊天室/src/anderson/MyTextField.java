package anderson;

import javax.swing.*;
import java.awt.*;

public class MyTextField extends JTextField {
    public MyTextField() {
    	//���������벼��
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
        this.setCaretColor(Color.LIGHT_GRAY);
        this.setFont(new Font("����", Font.BOLD, 16));


    }
}
