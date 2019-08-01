package cs3500.animator.view;

/**
 * Represents a change in the shapes of an animation.
 */
public interface IShapeChangeEvent {
  /**
   * Gets the type of change that is being made.
   *
   * @return A shape being added or deleted
   */
  ShapeChange getChangeType();

  /**
   * Gets the type of shape being changed (Rectangle, Ellipse, etc).
   *
   * @return A shape type.
   */
  String getShapeType();

  /**
   * Gets the ID of the shape being changed.
   *
   * @return A unique shape ID
   */
  String getId();
}
