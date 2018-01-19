import java.awt.Graphics2D;
import java.awt.Shape;

public interface Sprite {
	public Shape getShape();
	public void setVelocity(float dx, float dy);
	public void move();
	public boolean isOutOfBounds();
	public boolean isInBounds();
	public void draw(Graphics2D g2);
	public boolean intersects(Sprite other);
}
