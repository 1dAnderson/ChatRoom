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
        });//ʹ���ɼ�
        //���������벼��
        this.setEnabled(isVisible);
        this.setCaretColor(Color.LIGHT_GRAY);
        this.setAutoscrolls(true);
        this.setLineWrap(true);
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
        this.setFont(new Font("����", Font.BOLD, 16));

    }
    //ʵ��append����
	public void append(String string) {
		super.append(string);
		
	}

	
}
