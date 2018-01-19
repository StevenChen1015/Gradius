import java.awt.*;
import java.awt.geom.*;

public abstract class SpriteImpl implements Sprite {

	// drawing
	private Shape shape;
	private final Color border;
	private final Color fill;

	// movement
	private float dx, dy;
	private final Rectangle2D bounds;
	private final boolean isBoundsEnforced;

	/**
	 * Constructs a temp sprite keeps the shape info and its boundary
	 * 
	 * @param shape the shape of the component. A ship or a asteroid.
	 * @param bounds the boundary of the shape
	 * @param boundsEnforced the shape's bound is enforced or not
	 * @param border border color around the shape
	 * @param fill the color of the shape
	 */
	protected SpriteImpl(Shape shape, Rectangle2D bounds, boolean boundsEnforced, Color border, Color fill) {
		this.shape = shape;
		this.bounds = bounds;
		this.isBoundsEnforced = boundsEnforced;
		this.border = border;
		this.fill = fill;
	}

	protected SpriteImpl(Shape shape, Rectangle2D bounds, boolean boundsEnforced, Color fill) {
		this(shape, bounds, boundsEnforced, null, fill);
	}

	public Shape getShape() {
		return shape;
	}

	public void setVelocity(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}

	/**
	 * Check if bounds are enforced and if the shape still in bounds after
	 * moving Ship's bound is enforced Asteroids' bounds are not enforced
	 */
	public void move() {

		AffineTransform af = AffineTransform.getTranslateInstance(dx, dy);
		Shape newShape = af.createTransformedShape(shape);
		if (isBoundsEnforced && isInBounds(bounds, newShape)) {
			shape = newShape;
		} else if (!isBoundsEnforced) {
			shape = newShape;
		}
	}

	/** Returns true if the shape is out of its boundary */
	public boolean isOutOfBounds() {
		return !shape.intersects(bounds);
	}

	/** Returns true if the shape is still in its boundary */
	public boolean isInBounds() {
		return isInBounds(bounds, shape);
	}

	private static boolean isInBounds(Rectangle2D bounds, Shape s) {
		return bounds.contains(s.getBounds());
	}

	public void draw(Graphics2D g2) {
		g2.setColor(fill);
		g2.fill(shape);
		g2.setColor(border);
		g2.draw(shape);
	}

	/** Check if two shapes are intersects with each other */
	public boolean intersects(Sprite other) {
		return intersects(other.getShape());
	}

	private boolean intersects(Shape other) {
		if (bounds.intersects(other.getBounds())) {
			Area area1 = new Area(other);
			Area area2 = new Area(this.shape);
			return intersects(area1, area2);
		}
		return false;

	}

	private static boolean intersects(Area a, Area b) {
		a.intersect(b);
		return !a.isEmpty();
	}
}
