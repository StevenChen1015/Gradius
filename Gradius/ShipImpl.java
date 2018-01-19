import java.awt.*;
import java.awt.geom.*;

public class ShipImpl extends SpriteImpl implements Ship {

	private final static Color FILL = Color.GREEN;
	private final static Color BORDER = Color.WHITE;

	private final static int HEIGHT = 20;
	private final static int WIDTH = HEIGHT;

	public ShipImpl(int x, int y, Rectangle2D moveBounds) {
		super(drawPolygon(x, y), moveBounds, true, BORDER, FILL);
	}
	/**
	 * Customize the shape of the ship
	 * @param x the starting x-coordinates
	 * @param y the starting y-coordinates
	 * @return the shape of the ship
	 */
	public static Shape drawPolygon(int x, int y) {
		// Body
		Polygon BODY = new Polygon(
				new int[] { x + 3, x + 7, x + 8, x + 8, x + 7, x + 3, x, x + 1, x + 4, x + 4, x + 1, x },
				new int[] { y + 5, y + 5, y + 7, y + 13, y + 15, y + 15, y + 14, y + 13, y + 13, y + 7, y + 7, y + 6 },
				12);
		// Right Front Wing
		Polygon RFW = new Polygon(new int[] { x + 9, x + 9, x + 8, x + 10, x + 11, x + 20, x + 10, x + 10 },
				new int[] { y + 11, y + 13, y + 14, y + 15, y + 16, y + 13, y + 13, y + 12 }, 8);
		// Left Front Wing
		Polygon LFW = new Polygon(new int[] { x + 9, x + 9, x + 8, x + 10, x + 11, x + 20, x + 10, x + 10 },
				new int[] { y + 9, y + 7, y + 6, y + 5, y + 4, y + 7, y + 7, y + 8 }, 8);

		// Right Back Wing
		Polygon RBW = new Polygon(new int[] { x + 10, x + 7, x + 3 }, new int[] { y + 20, y + 15, y + 15 }, 3);
		// Left Back Wing
		Polygon LBW = new Polygon(new int[] { x + 10, x + 7, x + 3 }, new int[] { y, y + 5, y + 5 }, 3);

		Area shape;
		shape = new Area();
		shape.add(new Area(LBW));
		shape.add(new Area(RBW));
		shape.add(new Area(BODY));
		shape.add(new Area(LFW));
		shape.add(new Area(RFW));
		return shape;
	}
}