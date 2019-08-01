package cs3500.animator.model;

import java.awt.Color;
import java.awt.geom.Point2D;

import static java.util.Objects.requireNonNull;

/**
 * Represents an abstract shape with getters and setters already made, so that inheriting classes
 * can just override draw and makeCopy.
 */
public abstract class AShape implements IShape {
  protected String shapeType;
  protected int width;
  protected int height;
  protected Point2D position;
  protected Color color;
  protected int orientation;

  protected AShape(int width, int height, Point2D position, int orientation, Color color) {
    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Cannot have a negative width or height");
    }
    this.width = width;
    this.height = height;
    this.position = requireNonNull(position);
    this.orientation = orientation;
    this.color = requireNonNull(color);
  }

  protected AShape() {
    this(0,0, new Point2D.Double(0,0), 0, new Color(0));
  }

  @Override
  public String getShapeType() {
    return this.shapeType;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public int getOrientation() {
    return this.orientation;
  }

  @Override
  public Point2D getPosition() {
    return new Point2D.Double(this.position.getX(), this.position.getY());
  }

  @Override
  public Color getColor() {
    return new Color(this.color.getRGB());
  }

  @Override
  public void setColor(Color color) {
    this.color = requireNonNull(color);
  }

  @Override
  public void setPosition(Point2D position) {
    this.position = requireNonNull(position);
  }

  @Override
  public void setWidth(int width) {
    if (width < 0) {
      throw new IllegalArgumentException("Cannot pass a negative width");
    }
    this.width = width;
  }

  @Override
  public void setHeight(int height) {
    if (height < 0) {
      throw new IllegalArgumentException("Cannot pass a negative height");
    }
    this.height = height;
  }

  @Override
  public void setOrientation (int orientation) { this.orientation = orientation; }

  @Override
  public String toString() {
    return this.shapeType + "  " + this.position.getX() + "  " + this.position.getY() + "  "
            + this.width + "  " + this.height + "  " + this.orientation + "  "
            + this.color.getRed() + "  " + this.color.getGreen() + "  " + this.color.getBlue();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }

    if (!(other instanceof AShape)) {
      return false;
    }

    AShape otherShape = (AShape) other;

    return this.shapeType.equals(otherShape.getShapeType())
            && this.width == otherShape.getWidth()
            && this.height == otherShape.getHeight()
            && this.position.getX() == otherShape.getPosition().getX()
            && this.position.getY() == otherShape.getPosition().getY()
            && this.orientation == otherShape.getOrientation()
            && this.color.getRGB() == otherShape.getColor().getRGB();
  }

  @Override
  public int hashCode() {
    int result = 17;

    result = 31 * result + this.shapeType.hashCode();
    result = 31 * result + this.width;
    result = 31 * result + this.height;
    result = 31 * result + this.orientation;
    long xLong = Double.doubleToLongBits(this.position.getX());
    result = 31 * result + (int) (xLong ^ (xLong >>> 32));
    long yLong = Double.doubleToLongBits(this.position.getX());
    result = 31 * result + (int) (yLong ^ (yLong >>> 32));
    result = 31 * result + this.color.getRGB();

    return result;
  }
}

