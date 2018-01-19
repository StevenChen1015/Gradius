import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.*;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GradiusComp extends JComponent {

	private final static int GAME_TICK = 1000 / 60;
	private static int ASTEROID_MAKE_TICK = 1000;

	private final static int SHIP_INIT_X = 10;
	private final static int SHIP_INIT_Y = Gradius.HEIGHT / 3;
	private final static int SHIP_VEL_BASE = 4;
	private final static int SHIP_VEL_FAST = 8;
	private final static int ASTEROID_INIT_X = 900;
	private final static int ASTEROID_INIT_Y = 0;
	private final static int CANNON_SHOTS_CKEY = 5;
	private final static int CANNON_SHOTS_XKEY = 36;
	private final static double ROT_DEG_C = 90 / CANNON_SHOTS_CKEY;
	private final static double ROT_DEG_X = 360 / CANNON_SHOTS_XKEY;
	private final static int FONT = 20;
	private final static int TEXT_ALIGH_X = 0;
	private static int TEXT_ALIGH_Y = 20;
	private Ship ship;
	private final Timer[] gameTick;
	Collection<Asteroid> roids;
	private static int lives = 5;
	private static int level = 1;
	Collection<Cannon> cannon;
	ArrayList<Shape> l;
	private long startTime;
	public static long current;
	private static int energy = 0;

	/**
	 * Constructs a new timer, a ship, and a collection of asteroids Timer will stop
	 * if ship hits an asteroid Asteroids will get remove if they out of boundary A
	 * ship can only move inside the screen boundary
	 */
	public GradiusComp() {
		ShipKeyListener listener = new ShipKeyListener();
		addKeyListener(listener);

		roids = new HashSet<Asteroid>();
		cannon = new HashSet<Cannon>();

		CannonKeyListner clistner = new CannonKeyListner();
		addKeyListener(clistner);

		gameTick = new Timer[2];
		gameTick[0] = new Timer(GAME_TICK, this::update);
		gameTick[1] = new Timer(ASTEROID_MAKE_TICK, (a) -> {
			this.makeAsteroid();
		});
	}

	private void update(ActionEvent ae) {
		requestFocusInWindow();
		ship.move();
		roids.parallelStream().forEach(Asteroid::move);
		roids.removeIf(Asteroid::isOutOfBounds);
		cannon.parallelStream().forEach(Cannon::move);
		cannon.removeIf(Cannon::isOutOfBounds);

		// Removes the asteroid if cannon hits it
		Collection<Asteroid> hitR = roids.parallelStream().filter(a -> cannon.stream().anyMatch(a::intersects))
				.collect(Collectors.toSet());
		Collection<Cannon> hitC = cannon.parallelStream().filter(c -> roids.stream().anyMatch(c::intersects))
				.collect(Collectors.toSet());

		// Display the Special Attack progress bar based on how many asteroid
		// are destroyed
		if ((hitR.size() != 0) && (energy < Gradius.MAX)) {
			Gradius.bar.setValue(energy += 2);
		}
		cannon.removeAll(hitC);
		roids.removeAll(hitR);
		repaint();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paintComponent(g2);
	}

	/** Draw the ship and all the asteroids */
	private void paintComponent(Graphics2D g2) {
		current = System.currentTimeMillis() - startTime;
		long second = (current / 1000) % 60;
		long minute = (current / (1000 * 60)) % 60;
		long hour = (current / (1000 * 60 * 60)) % 24;
		String time = String.format("%02d:%02d:%02d", hour, minute, second);

		if (ship != null) {
			ship.draw(g2);
		}
		roids.stream().forEach(a -> a.draw(g2));
		cannon.stream().forEach(a -> a.draw(g2));

		Graphics2D status = (Graphics2D) g2;
		status.setColor(Color.GREEN);
		status.setFont(new Font("Arial", Font.BOLD, FONT));
		// Display the Game Time
		status.drawString(" Game Time: " + time, TEXT_ALIGH_X, TEXT_ALIGH_Y);
		// Display Progress bar
		status.drawString(" Energy: " + energy, TEXT_ALIGH_X, 70);
		// Display the Game Lives (by number)
		status.drawString(" Lives: " + lives, TEXT_ALIGH_X, 45);
		if (lives <= 2) {
			status.setColor(Color.RED);
		}
		l.forEach(g2::draw);

		status.setColor(Color.WHITE);
		// Display the Game Levels
		status.drawString(" Level: " + level, this.getWidth() / 2, TEXT_ALIGH_Y);
		Asteroid as = roids.parallelStream().filter(a -> a.intersects(ship)).findAny().orElse(null);
		if (as != null) {
			// Removes lives and ship (real and the display shape)
			l.remove(--lives);
			if (lives != 0) {
				roids.remove(as);
				// Resets the position of the ship
				ship = new ShipImpl(SHIP_INIT_X, SHIP_INIT_Y, getBounds(null));
			} else {
				// Stop the game timer
				Stream.of(gameTick).forEach(Timer::stop);
				// Display the "Game Over" message
				status.setFont(new Font("Arial", Font.BOLD, 80));
				status.setColor(Color.RED);
				status.drawString("Game Over", this.getWidth() / 3, this.getHeight() / 2);
				status.setFont(new Font("Arial", Font.BOLD, FONT));
				status.drawString("Press F12 to restart", this.getWidth() / 2, this.getHeight() / 2 + 50);
			}
		}
	}

	/**
	 * Starts the game Creates the ship and a collection of all the asteroids
	 */
	public void start() {
		ship = new ShipImpl(SHIP_INIT_X, SHIP_INIT_Y, getBounds(null));
		AsteroidFactory.getInstance()
				.setStartBounds(new Rectangle(ASTEROID_INIT_X, ASTEROID_INIT_Y, 0, Gradius.HEIGHT));
		AsteroidFactory.getInstance().setMoveBounds(getBounds(null));

		// Display how many lives left (by shapes)
		l = new ArrayList<>();
		for (int i = 0; i < lives; i++) {
			Shape ship1 = ShipImpl.drawPolygon(90 + (i * 20), 28);
			l.add(ship1);
		}

		Stream.of(gameTick).forEach(Timer::restart);
		startTime = System.currentTimeMillis();
	}

	/** Makes all asteroids */
	private void makeAsteroid() {
		if (((current / 1000) % 60 == level * 30 % 60) && (current != 0)) {
			level++;
		}
		roids.add(AsteroidFactory.getInstance().makeAsteroid(level));
	}

	/**
	 * 
	 * @param degree
	 * @param shots
	 */
	private void makeCannon(double degree, int shots) {
		int x = ship.getShape().getBounds().x + ship.getShape().getBounds().width;
		int y = ship.getShape().getBounds().y + ship.getShape().getBounds().height / 2;
		CannonFactory.getInstance().setStartBounds(new Rectangle(x, y, 0, 0));
		if (degree == 0) {
			cannon.add(CannonFactory.getInstance().makeCannon());
		} else {
			for (int i = 0; i < shots; i++) {
				CannonFactory.getInstance().setStartBounds(new Rectangle(x, y, 0, 0));
				cannon.add(CannonFactory.getInstance().cKeyPressed(ROT_DEG_X * Math.floor(i - 2.0)));
			}
		}
	}

	/**
	 * Event listener class provides four key events Z key - Shots single cannon. C
	 * key - Shots five cannons within an arc. X key - Shots cannon around the ship.
	 * F12 - Resets the game.
	 */
	private class CannonKeyListner extends KeyAdapter {
		// Get the ship's coordinates

		@Override
		public void keyPressed(KeyEvent e) {
			shotCannon(e);
		}

		private void shotCannon(KeyEvent e) {

			// Z key - Shots single cannon.
			if (e.getKeyCode() == KeyEvent.VK_Z) {
				makeCannon(0, 0);
			}
			// C key - Shots five cannons within an arc.
			if ((e.getKeyCode() == KeyEvent.VK_C) && energy != 0) {
				makeCannon(ROT_DEG_C, CANNON_SHOTS_CKEY);
				if ((energy - 1) >= 0) {
					Gradius.bar.setValue(--energy);
				}
			}
			// X key - Shots cannon around the ship.
			if ((e.getKeyCode() == KeyEvent.VK_X) && (energy >= Gradius.MAX / 2)) {
				makeCannon(ROT_DEG_X, CANNON_SHOTS_XKEY);
				energy = 0;
			}
			// F12 - Resets the game.
			if ((e.getKeyCode() == KeyEvent.VK_F12)) {
				roids.clear();
				Gradius.bar.setValue(0);
				energy = 0;
				lives = 5;
				start();
			}
		}
	}

	/**
	 * Event listener class Allows the ship move up, down, left, and right by
	 * getting commands from the keyboard If the "shift" key pressed, the
	 * velocity/speed of ship will increase
	 */
	private class ShipKeyListener extends KeyAdapter {

		private boolean up;
		private boolean down;
		private boolean left;
		private boolean right;

		@Override
		public void keyPressed(KeyEvent e) {
			setVelocity(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			setVelocity(e);
		}

		private void setVelocity(KeyEvent e) {
			movingDir(e);
			final int dp = e.isShiftDown() ? SHIP_VEL_FAST : SHIP_VEL_BASE;
			int dx = 0;
			int dy = 0;

			if (up && !down) {
				dy = -dp;
			} else if (down && !up) {
				dy = dp;
			}
			if (left && !right) {
				dx = -dp;
			} else if (right && !left) {
				dx = dp;
			}
			ship.setVelocity(dx, dy);
		}

		private void movingDir(KeyEvent e) {
			boolean key = false;
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				key = true;
			} else if (e.getID() == KeyEvent.KEY_RELEASED) {
				key = false;
			}
			switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				up = key;
				break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				down = key;
				break;
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_KP_LEFT:
				left = key;
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_KP_RIGHT:
				right = key;
				break;
			}
		}
	}
}
