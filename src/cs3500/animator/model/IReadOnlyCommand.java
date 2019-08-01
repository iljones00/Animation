package cs3500.animator.model;

import java.awt.Color;

/**
 * Represents a command on a shape during the animation without any methods to mutate
 * this command or a shape.
 */
public interface IReadOnlyCommand {
  /**
   * Gets the start time of this command which is the tick that the command begins on the shape.
   * @return int that represents the tick that the command begins.
   */
  int getStartTime();

  /**
   * Gets the end time of this command which is the tick that the command ends on the shape.
   * @return int that represents the tick that the command begins.
   */
  int getEndTime();

  /**
   * Gets the type of this command in a String.
   * @return a String that represents the type of command it is.
   */
  String getType();

  /**
   * Gets the starting X value of shape in his command as an int.
   * @return the starting X value of the shape in this command.
   */
  int getStartX();

  /**
   * Gets the starting Y value of the shape in this command as an int.
   * @return the starting Y value of the shape in this command.
   */
  int getStartY();

  /**
   * Gets the starting width of the shape in this command as an int.
   * @return the starting width value of the shape in this command.
   */
  int getStartWidth();

  /**
   * Gets the starting height of the shape in this command as an int.
   * @return the starting height value of the shape in this command.
   */
  int getStartHeight();

  /**
   * Gets the starting color of the shape in this command.
   * @return the starting color of the shape in this command.
   */
  Color getStartColor();

  /**
   * Gets the ending X value of shape in his command as an int.
   * @return the ending X value of the shape in this command.
   */
  int getEndX();

  /**
   * Gets the ending Y value of shape in his command as an int.
   * @return the ending Y value of the shape in this command.
   */
  int getEndY();

  /**
   * Gets the ending width of shape in his command as an int.
   * @return the ending width value of the shape in this command.
   */
  int getEndWidth();

  /**
   * Gets the ending height value of shape in his command as an int.
   * @return the ending height value of the shape in this command.
   */
  int getEndHeight();

  /**
   * Gets the ending color of the shape in his command as an int.
   * @return the ending color of the shape in this command.
   */
  Color getEndColor();
}
