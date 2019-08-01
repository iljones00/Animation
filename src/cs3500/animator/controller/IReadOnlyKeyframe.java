package cs3500.animator.controller;

import java.awt.Color;

/**
 * Represents the read only version of a key frame of an animation that has only the getters
 * of the key frame.
 */
public interface IReadOnlyKeyframe {

  /**
   * Getter for the time of this key frame.
   * @return the time that this key frame is stationed at.
   */
  int getTime();

  /**
   * Getter for the X value of this keyframe.
   * @return an integer that is the X position for this keyframe.
   */
  int getX();

  /**
   * Getter for the Y value of this key frame.
   * @return an integer that is the Y position of this keyframe.
   */
  int getY();

  /**
   * Getter for the width of this key frame.
   * @return an integer that is the width of this keyframe.
   */
  int getWidth();

  /**
   * Getter for the height of this keyframe.
   * @return an integer that is the height of this keyframe.
   */
  int getHeight();

  /**
   * Getter for the color of this key frame.
   * @return a color that is the color of this keyframe.
   */
  Color getColor();
}
