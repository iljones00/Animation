package cs3500.animator.model;

/**
 * Represents a command on a shape during the animation with a setState() method so that
 * it can be given a shape and change it to what it's supposed to be at the given time.
 */
public interface ICommand extends IReadOnlyCommand {
  /**
   * Mutates the shape to its instantaneous state at the given time after doing this command.
   * Throws an exception if the time is not within the start and end times of this command.
   * @param time in ticks that the shape gets changed to.
   * @param shape which is the starting shape before the command has been run to the given time.
   * @return IShape that is the mutated shape after the command has been run to the given time.
   */
  IShape setState(int time, IShape shape);
}
