package cs3500.animator.controller;

import java.awt.Color;

/**
 *  Represents a keyframe that includes rotation.
 */
public class RotateKeyframe extends Keyframe implements IRotateKeyframe {
  private final int orientation;

  /**
   * Constructor for a keyframe that initializes all of its fields.
   *
   * @param time   is this time this key frame occurs.
   * @param x      is the x position of this keyframe.
   * @param y      is the y position of this keyframe.
   * @param width  is the width of the shape at this keyframe.
   * @param height is the height of the shape at this keyframe.
   * @param orientation is the orientation of the shape at this keyframe.
   * @param color  is the color of the shape at this keyframe.
   */
  public RotateKeyframe(int time, int x, int y, int width, int height,
                        int orientation, Color color) {
    super(time, x, y, width, height, color);
    this.orientation = orientation;
  }

  @Override
  public int getOrientation() {
    return this.orientation;
  }

  @Override
  public String toString() {
    return "t-" + this.time + ": at (" + this.x + ", " + this.y + "), " + this.width + "x"
            + this.height + ", orientation: " + this.orientation + ", color: ("
            + this.color.getRed() + ", " + this.color.getGreen() + ", "
            + this.color.getBlue() + ")";
  }
}
