
/**
* A game contains a ship and many asteroids
* A ship can move up, down, left, and right and can not fly out the screen
* An asteroid moves horizontally from right to left and will get removes if fly out of the screen
* Game will over if a ship hits an asteroid
* For: Langara College, CPSC 1181-003 Fall 2017, Instructor: Jeremy Hilliker.
*
* @author Steven Chen @ langara
* @version 2017-10-27
*/
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class Gradius extends JFrame {

	private final static int WIDTH = 900;
	public final static int HEIGHT = 700;
	public static JProgressBar bar;
	private final GradiusComp comp;
	private final static int MIN = 0;
	public final static int MAX = 50;
	public Gradius() {
		setResizable(false);
		comp = new GradiusComp();
		setContentPane(comp);
	}

	public static void main(String[] args) {
		Gradius frame = new Gradius();

		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setBackground(Color.BLACK);
		// Progress bar of the special attack
		frame.setLayout(new FlowLayout(0, 110, 58));
		bar = new JProgressBar();
		bar.setMinimum(MIN);
		bar.setMaximum(MAX);
		bar.setForeground(Color.GREEN);
		frame.getContentPane().add(bar);

		frame.comp.start();
	}
}