package anderson;
import javax.swing.*;
import javax.swing.event.AncestorListener;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionListener;

public class MyTextArea extends JTextArea {
    public MyTextArea(boolean isVisible) {
        this.setCaret(new DefaultCaret() {
            public boolean isVisible() {
                return isVisible;
            }
        });//使光标可见
        //设置字体与布局
        this.setEnabled(isVisible);
        this.setCaretColor(Color.LIGHT_GRAY);
        this.setAutoscrolls(true);
        this.setLineWrap(true);
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
        this.setFont(new Font("楷体", Font.BOLD, 16));

    }
    //实现append函数
	public void append(String string) {
		super.append(string);
		
	}

	
}
