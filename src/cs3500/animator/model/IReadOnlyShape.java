package cs3500.animator.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * Represents a read only version of the shape has all of the getters for each field of the shape.
 */
public interface IReadOnlyShape {

  /**
   * Gets the type of shape this shape is.
   * @return String the is the type of Shape this shape is.
   */
  String getShapeType();

  /**
   * Gets the width of the shape.
   * @return int that is the width of the shape.
   */
  int getWidth();

  /**
   * Gets the height of the shape.
   * @return int that is the height of the shape.
   */
  int getHeight();


  /**
   * Gets the orientation of the shape.
   * @return int that is the orientation of the shape.
   */
  int getOrientation();

  /**
   * Gets the current position of the shape.
   * @return Point2D that is the X and Y coordinate of the shape.
   */
  Point2D getPosition();

  /**
   * Gets the color of the shape.
   * @return Color which is the color of the shape.
   */
  Color getColor();

  /**
   * Draws the shape onto the screen.
   * @param g The graphics needed to display the shape on the screen.
   */
  void draw(Graphics2D g);
}