package package1;

import javax.swing.JFrame;

public class Window extends JFrame {
	private final int HEIGHT =  1080;
	private final int WIDTH = 1920;
	public Window() {
		super("EPIC TITLE");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	public static void main(String[] args) {
		new Window();
	}
	
}
