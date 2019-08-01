package cs3500.animator.controller;

import java.awt.Color;

/**
 * Interface for a KeyFrame including its setters for its fields, that represents the various
 * keyframes of an animation.
 */
public interface IKeyframe extends IReadOnlyKeyframe {

  /**
   * Sets the time of this key frame to the given time.
   * @param time is the time that the key frame should be changed to.
   */
  void setTime(int time);

  /**
   * Sets the X value of this key frame to the given X.
   * @param x is the x value that this key frame should be changed to.
   */
  void setX(int x);

  /**
   * Sets the Y value of this keyframe to the given Y.
   * @param y is the y value that this key frame should be changed to.
   */
  void setY(int y);

  /**
   * Sets the current width of this key frame to the given width.
   * @param width is the width that this key frame should be changed to.
   */
  void setWidth(int width);

  /**
   * Sets the current height of this key frame to the given height.
   * @param height is the height that this key frame should be changed to.
   */
  void setHeight(int height);

  /**
   * Sets the current color of this key frame to the given color.
   * @param color is the color that this key frame should be change to.
   */
  void setColor(Color color);
}
