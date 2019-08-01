package cs3500.animator.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * Represents an Ellipse object in the animation.
 */
public class Ellipse extends AShape {

  public Ellipse(int width, int height, Point2D position, int orientation, Color color) {
    super(width, height, position, orientation, color);
    this.shapeType = "Ellipse";
  }

  /**
   * Initializes the Ellipse at 0,0 with empty fields.
   */
  public Ellipse() {
    super();
    this.shapeType = "Ellipse";
  }

  /**
   * Draws the ellipse onto the screen.
   * @param g The graphics needed to display the shape on the screen.
   */
  @Override
  public void draw(Graphics2D g) {
    if (this.orientation == 0) {
      g.fillOval((int) this.position.getX(), (int) this.position.getY(), this.width, this.height);
    }
    else {
      Graphics2D gg = (Graphics2D) g.create();
      gg.rotate(Math.toRadians(this.orientation), this.position.getX() + ((double) this.width) / 2,
              this.position.getY() + ((double) this.height) / 2);
      gg.fillOval((int) this.position.getX(), (int) this.position.getY(), this.width, this.height);
      gg.dispose();
    }
  }

  /**
   * Makes a deep copy of the current shape.
   *
   * @return an IShape identical to this one.
   */
  @Override
  public IShape makeCopy() {
    return new Ellipse(this.width, this.height,
            new Point2D.Double(this.position.getX(), this.position.getY()),
            this.orientation,
            new Color(this.color.getRGB()));
  }
}
