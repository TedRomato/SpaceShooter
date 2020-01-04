package package1;

import javax.swing.JFrame;

public class Window extends JFrame {
	public Window() {
		super("EPIC TITLE");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	public static void main(String[] args) {
		new Window();
	}
	
}
