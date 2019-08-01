package cs3500.animator.view;

import java.awt.event.ActionEvent;

/**
 * A custom Action Event that represents a shape change from an editing view. It contains
 * information about the shape being changed and how its being changed (added or deleted).
 */
public class ShapeChangeEvent extends ActionEvent implements IShapeChangeEvent {
  private final ShapeChange changeType;
  private final String shapeType;
  private final String id;

  /**
   * Constructs a Shape Change Event, with the necessary information for the receiving listener
   * to update and reflect the change.
   *
   * @param source the object sending the event
   * @param changeType the type of change being made (one of: add, delete)
   * @param shapeType the type of shape being changed
   * @param id the id of the shape that this keyframe corresponds to
   */
  public ShapeChangeEvent(Object source, ShapeChange changeType, String shapeType, String id) {
    super(source, ActionEvent.ACTION_PERFORMED, "shape change");

    this.changeType = changeType;
    this.shapeType = shapeType;
    this.id = id;
  }

  @Override
  public ShapeChange getChangeType() {
    return this.changeType;
  }

  @Override
  public String getShapeType() {
    return this.shapeType;
  }

  @Override
  public String getId() {
    return this.id;
  }
}
