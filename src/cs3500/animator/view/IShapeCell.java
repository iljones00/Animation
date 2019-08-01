package cs3500.animator.view;

/**
 * Represents a cell in a list displayed on the GUI that corresponds to a shape. The toString
 * method should be overriden in all implementing classes, as that is how it will be displayed
 * in the GUI.
 */
public interface IShapeCell {
  /**
   * Gets the shape's ID.
   *
   * @return A unique shape ID
   */
  String getID();

  /**
   * Gets the shape's type (Rectangle, Ellipse, etc).
   *
   * @return A shape type
   */
  String getType();
}
