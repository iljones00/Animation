package cs3500.animator.model;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * The interface for shapes to be used in the animation that also includes setters.
 */
public interface IShape extends IReadOnlyShape {

  /**
   * Makes a deep copy of the current shape.
   *
   * @return an IShape identical to this one.
   */
  IShape makeCopy();

  /**
   * Sets the position of this shape to the given position.
   *
   * @param point X Y coordinate of the position that the shape will be set to.
   */

  void setPosition(Point2D point);

  /**
   * Sets the color of this shape to the given shape.
   *
   * @param color is the RGB color that the shape will be set to.
   */
  void setColor(Color color);


  /**
   * Sets the width of this shape to the given amount.
   *
   * @param width is the width of the shape that this shape should be changed to.
   */
  void setWidth(int width);

  /**
   * Sets the height of this shape to the given amount.
   *
   * @param height is the height of the shape that this shape should be changed to.
   */
  void setHeight(int height);

  /**
   * Sets the orientation of this shape to the given int.
   * @param orientation is the orientation that this shape should be changed to.
   */
  void setOrientation(int orientation);
}
