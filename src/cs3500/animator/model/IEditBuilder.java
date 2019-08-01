package cs3500.animator.model;

import java.awt.Dimension;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Represents the builder for an editable view.
 */
public interface IEditBuilder extends AnimationBuilder {

  /**
   * Removes a command from the currnet builder with the given start time.
   * @param id is the id of the shape that the command you are removing is associated with.
   * @param startTime is the start time of the command you remove.
   * @return the builder after removal.
   */
  IEditBuilder removeCommand(String id, int startTime);

  /**
   * Removes a shape from the current builder.
   * @param id is the id of the shape that the command you are removing.
   * @return the builder after removal.
   */
  IEditBuilder removeShape(String id);

  /**
   * Gets the commands from the builder.
   * @return a mapping of the ids to the commands in the builder.
   */
  LinkedHashMap<String, List<IReadOnlyRotateCommand>> getCommands();

  /**
   * Gets the shapes from the builder.
   * @return a mapping of the ids to the shapes in the builder.
   */
  LinkedHashMap<String, IReadOnlyShape> getShapes();

  /**
   * Gets the needed space from the builder.
   * @return the dimensions of the animation.
   */
  Dimension getNeededSpace();
}
