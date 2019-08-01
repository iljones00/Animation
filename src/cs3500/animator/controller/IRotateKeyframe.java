package cs3500.animator.controller;

public interface IRotateKeyframe extends IReadOnlyKeyframe {
  /**
   * Getter for the orientation of this key frame.
   *
   * @return the orientation in degrees of the shape at this frame.
   */
  int getOrientation();
}
