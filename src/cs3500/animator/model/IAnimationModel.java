package cs3500.animator.model;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Interface for the model for the Animation. Can return the instantaneous state of the model
 * based on the given time.
 */
public interface IAnimationModel {

  /**
   * Outputs the list of shapes at their instantaneous state in the animation
   * based on the given time.
   * @param time in ticks
   * @return the list of shapes that represents the instantaneous state of the
   *         animation at the given time.
   */
  List<IReadOnlyShape> getState(int time);

  /**
   * Getter for each of the shapes in the map.
   * @return the map linking ID to each unique shape.
   */
  LinkedHashMap<String, IReadOnlyShape> getShapes();


  /**
   * Getter for all of the commands from the model.
   * @return the map linking the list of commands to the ID of the shape.
   */
  LinkedHashMap<String, List<IReadOnlyRotateCommand>> getCommands();

  /**
   * Getter for the x location of the model.
   * @return the x location of the model in pixels.
   */
  int getX();

  /**
   * Getter for the y location of model.
   * @return the y location of the model in pixels.
   */
  int getY();

  /**
   * Getter for the height of the model.
   * @return the height of the model in pixels.
   */
  int getHeight();

  /**
   * Getter for the width of the model.
   * @return the width of the model in pixels.
   */
  int getWidth();

  /**
   * Gets the final tick of the last command run in this model.
   * @return the final tick of the model.
   */
  int getFinalTick();
}
