import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Polygon;
import java.util.Random;

public class AsteroidFactory {

	private final static int ASTEROID_SIZE_MIN = 10;
	private final static int ASTEROID_SIZE_MAX = 40;
	private final static int ASTEROID_VEL_MIN = 1;
	private final static int ASTEROID_ANGLE_MIN = 11;
	private final static int ASTEROID_ANGLE_MAX = 15;

	private final static AsteroidFactory instance = new AsteroidFactory();

	private static Rectangle startBounds;
	private static Rectangle moveBounds;

	private AsteroidFactory() {
	}

	public static AsteroidFactory getInstance() {
		return instance;
	}

	public void setStartBounds(Rectangle r) {
		startBounds = r;
	}

	/**
	 * A union of the provided bounds and the start bounds, and make it 1 pixel
	 * wider.
	 */
	public void setMoveBounds(Rectangle r) {
		int x1 = Math.min(startBounds.x, r.x);
		int x2 = Math.max(startBounds.x + startBounds.width, r.x + r.width);
		int y1 = Math.min(startBounds.y, r.y);
		int y2 = Math.max(startBounds.y + startBounds.height, r.y + r.height);
		moveBounds = new Rectangle(x1, y1, x2 - x1 + 1, y2 - y1);
	}

	public Asteroid makeAsteroid(int NEW_ASTEROID_VEL) {
		
		return new AsteroidImpl(startBounds.x, random(startBounds.y, startBounds.height),
				random(ASTEROID_SIZE_MIN, ASTEROID_SIZE_MAX), random(ASTEROID_SIZE_MIN, ASTEROID_SIZE_MAX),
				random(ASTEROID_VEL_MIN, NEW_ASTEROID_VEL));
	}

	/**
	 * Randomly generates a value between min and max
	 * 
	 * @param min minimum value
	 * @param max maximum value
	 * @return a random value between min and max
	 */
	private static int random(int min, int max) {
		if (max - min == 0) {
			return min;
		}
		Random rand = java.util.concurrent.ThreadLocalRandom.current();
		return min + rand.nextInt(max + 1);
	}

	/** Constructs different shapes of asteroid. */
	private static class AsteroidImpl extends SpriteImpl implements Asteroid {
		private final static Color COLOR = Color.LIGHT_GRAY;

		public AsteroidImpl(int x, int y, int w, int h, float v) {
			super(polygon(x, y, w, h), moveBounds, false, COLOR);
			setVelocity(-v, 0);
		}

		/**
		 * Generates the shape for an asteroid
		 * 
		 * @param x starting x-coordinates
		 * @param y starting y-coordinates
		 * @param w width of the asteroid
		 * @param h height of the asteroid
		 * @return the shape for asteroid
		 */
		public static Polygon polygon(int x, int y, int w, int h) {
			Polygon p = new Polygon();
			int angles = ASTEROID_ANGLE_MIN + (int) (Math.random() * ((ASTEROID_ANGLE_MAX - ASTEROID_ANGLE_MIN) + 1));
			for (int i = 0; i < angles; i++) {
				p.addPoint((int) (x + w / 2 * Math.cos(i * 2 * Math.PI / angles)),
						(int) (y + h / 2 * Math.sin(i * 2 * Math.PI / angles)));
			}
			return p;
		}
	}
}
