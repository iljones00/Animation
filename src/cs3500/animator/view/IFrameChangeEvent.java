package cs3500.animator.view;

import java.awt.Color;

/**
 * Represents a change in the keyframes corresponding to a shape in an animation.
 */
public interface IFrameChangeEvent {
  /**
   * Gets the type of change that was made.
   *
   * @return A frame was added, edited, or removed by the user
   */
  FrameChange getType();

  /**
   * Gets the ID of the shape the changed keyframe corresponds to.
   *
   * @return A shape ID
   */
  String getId();

  /**
   * Gets the time at which this new keyframe describes the state of its shape.
   *
   * @return A time in ticks
   */
  int getTime();

  /**
   * Gets the x coordinate of the shape at this new frame.
   *
   * @return An x coordinate
   */
  int getX();

  /**
   * Gets the y coordinate of the shape at this new frame.
   *
   * @return A y coordinate
   */
  int getY();

  /**
   * Gets the width of the shape at this new frame.
   *
   * @return A width in pixels
   */
  int getWidth();

  /**
   * Gets the height of the shape at this new frame.
   *
   * @return A height in pixels
   */
  int getHeight();

  /**
   * Gets the orientation of the shape at this new frame.
   *
   * @return An orientation in degrees
   */
  int getOrientation();

  /**
   * Gets the color of the shape at this new frame.
   *
   * @return A color object
   */
  Color getColor();
}
