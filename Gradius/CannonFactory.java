import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
public class CannonFactory {
	private final static int CANNON_SIZE_WIDTH = 10;
	private final static int CANNON_SIZE_HEIGHT = 1;
	private final static Color CANNON_COLOR = Color.WHITE;
	private final static int CANNON_MOVELENGTH = 500;
	private final static int CANNON_VEL = 10;
	private final static CannonFactory instance = new CannonFactory();
	private static float X_VEL;
	private static float Y_VEL;
	private static Rectangle startBounds;
	private static int startX;
	private static int startY;

	private CannonFactory() {
	}

	public static CannonFactory getInstance() {
		return instance;
	}

	public void setStartBounds(Rectangle r) {
		startBounds = r;
		startX = startBounds.x;
		startY = startBounds.y;
	}

	public Cannon makeCannon() {
		Shape shape = new Rectangle2D.Float(startX, startY, CANNON_SIZE_WIDTH, CANNON_SIZE_HEIGHT);
		return new CannonImpl(shape, CANNON_VEL, 0);
	}

	public Cannon cKeyPressed(double rotDeg) {
		AffineTransform af = AffineTransform.getRotateInstance(Math.toRadians(rotDeg), startX, startY );
		Shape s = new Rectangle2D.Float(startX, startY, CANNON_SIZE_WIDTH, CANNON_SIZE_HEIGHT);
		s = af.createTransformedShape(s);
		X_VEL = CANNON_VEL * (float) Math.cos(Math.toRadians(rotDeg));
		Y_VEL = CANNON_VEL * (float) Math.sin(Math.toRadians(rotDeg));
		return new CannonImpl(s, X_VEL, Y_VEL);
	}

	public static class CannonImpl extends SpriteImpl implements Cannon {

		public CannonImpl(Shape cannonShape, float vx, float vy) {
			super(cannonShape, new Rectangle2D.Float(0, 0, CANNON_MOVELENGTH + startX, Gradius.HEIGHT), false,
					CANNON_COLOR);
			setVelocity(vx, vy);
		}
	}
}
