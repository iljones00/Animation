package cs3500.animator.view;

import java.awt.Color;
import java.awt.event.ActionEvent;

/**
 * A custom Action Event that represents a keyframe change from an editing view. It contains
 * information about the keyframe being changed and how its being changed (edited, added, deleted).
 */
public class FrameChangeEvent extends ActionEvent implements IFrameChangeEvent {
  private final FrameChange type;
  private final String id;
  private final int time;
  private final int x;
  private final int y;
  private final int width;
  private final int height;
  private final int orientation;
  private final Color color;

  /**
   * Constructs a Frame Change Event, with the necessary information for the receiving listener
   * to update and reflect the change.
   *
   * @param source the object sending the event
   * @param type the type of change being made (one of: edit, add, delete)
   * @param id the id of the shape that this keyframe corresponds to
   * @param time the tick at which this keyframe represents its shape
   * @param x the x coordinate of the shape at this frame
   * @param y the y coordinate of the shape at this frame
   * @param width the width of the shape at this frame
   * @param height the height of the shape at this frame
   * @param color the color of the shape at this frame
   */
  public FrameChangeEvent(Object source, FrameChange type, String id, int time, int x, int y,
                          int width, int height, int orientation, Color color) {
    super(source, ActionEvent.ACTION_PERFORMED, "frame change");

    this.type = type;
    this.id = id;
    this.time = time;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.orientation = orientation;
    this.color = color;
  }

  @Override
  public FrameChange getType() {
    return this.type;
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public int getTime() {
    return this.time;
  }

  @Override
  public int getX() {
    return this.x;
  }

  @Override
  public int getY() {
    return this.y;
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
  public Color getColor() {
    return new Color(this.color.getRGB());
  }
}
