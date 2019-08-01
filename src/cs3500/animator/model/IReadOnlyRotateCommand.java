package cs3500.animator.model;

/**
 * Represents a command that can rotate a shape, but is read-only
 */
public interface IReadOnlyRotateCommand extends IReadOnlyCommand {
  /**
   * Gets the orientation of this command which the starting orientation the shape.
   * @return int that represents the orientation in degrees
   */
  int getStartOrientation();

  /**
   * Gets the orientation of this command which the final orientation the shape.
   * @return int that represents the orientation in degrees
   */
  int getEndOrientation();
}
