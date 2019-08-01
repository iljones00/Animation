package cs3500.animator.model;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Represents an animation, with a list of shapes that represents the shapes on the screen, as
 * well as the commands, that represents the actions of each shape that happens at different times
 * during the animation.
 */
public class AnimationModel implements IAnimationModel {
  private final LinkedHashMap<String, List<IRotateCommand>> commands;
  private final LinkedHashMap<String, IShape> shapes;
  private final int x;
  private final int y;
  private final int width;
  private final int height;


  /**
   * A private constructor for Animation model to allow for only our builder to create new models.
   * This allows us to
   * @param commands is a LinkedHashmap that connects an ID of a shape, to all of the commands
   *                 that are lined up for that shape. All of the commands associated to an ID are
   *                 placed in a list in time order.
   * @param shapes is a LinkedHashMap that connects an ID to each shape, to ensure that ID's are
   *               unique and associated one-to-one with shapes.
   */
  private AnimationModel(LinkedHashMap<String, List<IRotateCommand>> commands,
                         LinkedHashMap<String, IShape> shapes,
                         int x, int y, int width, int height) {
    this.commands = commands;
    this.shapes = shapes;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /**
   * Gets the list of shapes at a certain time. It runs through each of the commands that occur
   * before the given time and runs the animation up until the time, to get the state of the
   * animation at the given time. It also converts the shapes into IReadOnlyShapes so that
   * when it gets passed to the user that the setters are hidden from access.
   * @param time int that represents the time in ticks that the method will return the state of
   *             the animation at.
   * @return the list of IReadOnlyShapes that have been changed based on the commands of the model,
   *         up until the given time. This list of shapes will be the current state of the
   *         animation at the given int time.
   */
  @Override
  public List<IReadOnlyShape> getState(int time) {
    List<IReadOnlyShape> output = new ArrayList<>();
    for (Map.Entry<String, List<IRotateCommand>> entry : this.commands.entrySet()) {
      String id = entry.getKey();
      IShape shape = this.shapes.get(id);

      int index = indexOfCommand(this.commands.get(id), time);
      if (index == -1) {
        continue;
      }
      if (time <= this.commands.get(id).get(index).getEndTime()) {
        this.commands.get(id).get(index).setState(time, shape);
      }
      else { // no command for the current time.
        continue;
      }
      double newX = shape.getPosition().getX() - this.x;
      double newY = shape.getPosition().getY() - this.y;
      shape.setPosition(new Point2D.Double(newX, newY));
      output.add(shape);
    }
    return output;
  }

  @Override
  public LinkedHashMap<String, IReadOnlyShape> getShapes() {
    LinkedHashMap<String, IReadOnlyShape> output = new LinkedHashMap<>();
    for (String key : shapes.keySet()) {
      output.put(key, shapes.get(key));
    }
    return output;
  }

  @Override
  public LinkedHashMap<String, List<IReadOnlyRotateCommand>> getCommands() {
    LinkedHashMap<String, List<IReadOnlyRotateCommand>> output = new LinkedHashMap<>();
    for (String key : this.commands.keySet()) {
      List<IReadOnlyRotateCommand> newCommands = new ArrayList<>();
      for (IReadOnlyRotateCommand command : this.commands.get(key)) {
        newCommands.add(command);
      }
      output.put(key, newCommands);
    }
    return output;
  }

  @Override
  public int getX() {
    return this.x;
  }

  @Override
  public int getY() {
    return this.y;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public int getFinalTick() {
    int output = 0;
    for (List<IRotateCommand> commandList : this.commands.values()) {
      output = Math.max(output, commandList.get(commandList.size() - 1).getEndTime());
    }
    return output;
  }

  /**
   * Uses binary search to find the index of the command in a list of commands that would be
   * running at a given time.
   *
   * @param list the list of commands through which to search
   * @param startTime the time to search for
   * @return the index of the command that would running at a given time
   */
  private static int indexOfCommand(List<IRotateCommand> list, int startTime) {
    if (list.isEmpty()) {
      return 0;
    }

    int indexMin = 0;
    int indexMax = list.size() - 1;

    while (indexMin < indexMax) {
      int indexMiddle = (indexMin + indexMax) / 2;
      int middleTime = list.get(indexMiddle).getStartTime();

      if (startTime == middleTime) {
        return indexMiddle;
      }
      else if (startTime > middleTime) {
        indexMin = indexMiddle + 1;
      }
      else {
        indexMax = indexMiddle - 1;
      }
    }

    if (startTime < list.get(indexMin).getStartTime()) {
      return indexMin - 1;
    }
    else {
      return indexMin;
    }
  }

  /**
   * Provides a description of each of the shapes, including its size, position, and color, as well
   * as how each of the shapes change based on the commands that are associated with that shape.
   * @return String that is the description of its movement and changes to its fields.
   */
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    for (Map.Entry<String, List<IRotateCommand>> entry : this.commands.entrySet()) {
      IShape shape = this.shapes.get(entry.getKey());
      output.append("Shape ").append(entry.getKey()).append(" ").append(shape.getShapeType())
              .append("\n");
      for (ICommand command : entry.getValue()) {
        StringBuilder temp = new StringBuilder(command.getType()).append(" ")
                .append(entry.getKey()).append(" ").append(command.getStartTime()).append(" ");

        command.setState(command.getStartTime(), shape);
        temp.append(this.getShapeInfo(shape)).append("    ");

        command.setState(command.getEndTime(), shape);
        temp.append(command.getEndTime()).append(" ").append(this.getShapeInfo(shape)).append("\n");
        output.append(temp);
      }
      output.append("\n");
    }
    return output.toString().trim();
  }

  /**
   * Helper for the toString() method that gives the user a description of the given shape.
   * @param shape is the shape that the method is outputting a description for.
   * @return String that is the description of each of the fields of the shape in the format that
   *         we want.
   */
  private String getShapeInfo(IShape shape) {
    StringBuilder temp = new StringBuilder();
    temp.append(shape.getPosition().getX()).append(" ")
            .append(shape.getPosition().getY()).append(" ")
            .append(shape.getWidth()).append(" ")
            .append(shape.getHeight()).append(" ")
            .append(shape.getColor().getRed()).append(" ")
            .append(shape.getColor().getGreen()).append(" ")
            .append(shape.getColor().getBlue());
    return temp.toString();
  }

  /**
   * A Builder class for the AnimationModel. Allows the user to give a list of commands and shapes,
   * and with those fields the builder creates the model.
   */
  public static class AnimationModelBuilder implements IEditBuilder {
    private LinkedHashMap<String, List<IRotateCommand>> commands;
    private LinkedHashMap<String, IShape> shapes;
    private int x = 0; //left most x
    private int y = 0; //top most y
    private int width = 1000;
    private int height = 600;


    /**
     * Constructor for the AnimationModelBuilder that initializes the empty list of commands.
     */
    public AnimationModelBuilder() {
      this.commands = new LinkedHashMap<>();
      this.shapes = new LinkedHashMap<>();
    }

    /**
     * Constructs a model based on the commands and shapes currently stored by the builder.
     */
    public IAnimationModel build() {

      for (String key : this.shapes.keySet()) {
        if (!this.commands.containsKey(key)) {
          throw new IllegalStateException("A shape must contain commands");
        }
      }
      for (String key : this.commands.keySet()) {
        if (!this.shapes.containsKey(key)) {
          throw new IllegalStateException("A command must have an associated shape");
        }
      }
      return new AnimationModel(this.commands, this.shapes, this.x, this.y,
              this.width, this.height);
    }

    /**
     * Specify the bounding box to be used for the animation.
     *
     * @param x      The leftmost x value
     * @param y      The topmost y value
     * @param width  The width of the bounding box
     * @param height The height of the bounding box
     * @return This {@link AnimationBuilder}
     */
    @Override
    public AnimationBuilder setBounds(int x, int y, int width, int height) {
      if (width < 0 || height < 0) {
        throw new IllegalArgumentException("Cannot have a negative width or height");
      }
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      return this;
    }

    /**
     * Adds a new shape to the growing document.
     *
     * @param name The unique name of the shape to be added. No shape with this name should already
     *             exist.
     * @param type The type of shape (e.g. "ellipse", "rectangle") to be added. The set of supported
     *             shapes is unspecified, but should include "ellipse" and "rectangle" as a
     *             minimum.
     * @return This {@link AnimationBuilder}
     */
    @Override
    public AnimationBuilder declareShape(String name, String type) {
      if (this.shapes.containsKey(name)) {
        throw new IllegalArgumentException("This ID has already been declared to a shape");
      }

      IShape shape;

      switch (type.toLowerCase()) {
        case "rectangle":
          shape = new Rectangle();
          break;
        case "ellipse":
          shape = new Ellipse();
          break;
        default:
          shape = null;
      }
      if (shape == null) {
        throw new IllegalArgumentException("Type is invalid");
      }
      this.shapes.put(name, shape);
      return this;
    }

    /**
     * Adds a transformation to the growing document.
     *
     * @param name The name of the shape (added with {@link AnimationBuilder#declareShape})
     * @param t1   The start time of this transformation
     * @param x1   The initial x-position of the shape
     * @param y1   The initial y-position of the shape
     * @param w1   The initial width of the shape
     * @param h1   The initial height of the shape
     * @param r1   The initial red color-value of the shape
     * @param g1   The initial green color-value of the shape
     * @param b1   The initial blue color-value of the shape
     * @param t2   The end time of this transformation
     * @param x2   The final x-position of the shape
     * @param y2   The final y-position of the shape
     * @param w2   The final width of the shape
     * @param h2   The final height of the shape
     * @param r2   The final red color-value of the shape
     * @param g2   The final green color-value of the shape
     * @param b2   The final blue color-value of the shape
     * @return This {@link AnimationBuilder}
     */
    @Override
    public AnimationBuilder addMotion(String name, int t1, int x1, int y1, int w1, int h1, int r1,
                                      int g1, int b1, int t2, int x2, int y2, int w2, int h2,
                                      int r2, int g2, int b2) {
      String type = this.getType(x1, y1, w1, h1, r1, g1, b1, x2, y2, w2, h2, r2, g2, b2);
      IRotateCommand command = new SuperMasterCommand(type, t1, x1, y1, w1, h1, 0, r1, g1, b1,
              t2, x2, y2, w2, h2, 0, r2, g2, b2);
      if (this.commands.containsKey(name)) {
        this.addIfValidCommand(name, this.commands.get(name), command,
                indexOfCommand(this.commands.get(name), command.getStartTime()) + 1);
      } else {
        this.commands.put(name, new ArrayList<>(Arrays.asList(command)));
      }
      return this;
    }

    @Override
    public AnimationBuilder addMotion(String name, int t1, int x1, int y1, int w1, int h1, int o1,
                                      int r1, int g1, int b1, int t2, int x2, int y2, int w2,
                                      int h2, int o2, int r2, int g2, int b2) {
      String type = this.getType(x1, y1, w1, h1, o1, r1, g1, b1, x2, y2, w2, h2, o2, r2, g2, b2);
      IRotateCommand command = new SuperMasterCommand(type, t1, x1, y1, w1, h1, o1, r1, g1, b1,
              t2, x2, y2, w2, h2, o2, r2, g2, b2);
      if (this.commands.containsKey(name)) {
        this.addIfValidCommand(name, this.commands.get(name), command,
                indexOfCommand(this.commands.get(name), command.getStartTime()) + 1);
      } else {
        this.commands.put(name, new ArrayList<>(Arrays.asList(command)));
      }
      return this;
    }

    // returns a description of a motion based off what properties are changed while it runs.
    private String getType(int x1, int y1, int w1, int h1, int o1, int r1, int g1, int b1,
                           int x2, int y2, int w2, int h2, int o2, int r2, int g2, int b2) {
      String description = this.getType(x1, y1, w1, h1, r1, g1, b1, x2, y2, w2, h2, r2, g2, b2);

      if (o1 != o2) {
        if (description.equals("Nothing")) {
          description = "Rotate";
        }
        else {
          description += " and Rotate";
        }
      }

      return description;
    }

    // returns a description of a motion based off what properties are changed while it runs.
    private String getType(int x1, int y1, int w1, int h1, int r1, int g1, int b1, int x2, int y2,
                           int w2, int h2, int r2, int g2, int b2) {
      String description = "";

      if (x1 != x2 || y1 != y2) {
        description += " and Motion";
      }
      if (w1 != w2 || h1 != h2) {
        description += " and Size";
      }
      if (r1 != r2 || g1 != g2 || b1 != b2) {
        description += " and Color";
      }

      if (description.length() == 0) {
        return "Nothing";
      } else {
        return description.substring(5);
      }
    }

    /**
     * Adds the given command into the list at the given index if the command doesn't overlap (i.e.
     * one command from tick 1 - 6 and another command from tick 3 - 8), although a new command is
     * allowed to start at the tick that the previous command ends. The shape's state as it was left
     * before the command must also match the state at its start time (if the shape would be at (10,
     * 10) when the command started, the first tick of the command must place it at (10, 10)).
     */
    private void addIfValidCommand(String id, List<IRotateCommand> list, IRotateCommand command,
                                   int addIndex) {
      int startTick = command.getStartTime();
      IShape shapeCopy1 = this.shapes.get(id).makeCopy();
      IShape shapeCopy2 = this.shapes.get(id).makeCopy();

      if (addIndex == 0) {
        list.add(addIndex, command);
        return;
      }

      ICommand previousCommand = list.get(addIndex - 1);

      previousCommand.setState(previousCommand.getEndTime(), shapeCopy1);
      command.setState(startTick, shapeCopy2);

      if (previousCommand.getEndTime() != startTick) {
        throw new IllegalArgumentException("Commands must overlap at their start/end times");
      }
      else if (this.shapesAreEqual(shapeCopy1, shapeCopy2)) {
        list.add(addIndex, command);
      }
      else {
        throw new IllegalArgumentException("There is an illegal teleportation here");
      }
    }

    //Checks if the two given shapes are equal.
    private boolean shapesAreEqual(IShape shape1, IShape shape2) {
      return shape1.getShapeType().equals(shape2.getShapeType())
              && shape1.getWidth() == shape2.getWidth()
              && shape1.getHeight() == shape2.getHeight()
              && shape1.getPosition().getX() == shape2.getPosition().getX()
              && shape1.getPosition().getY() == shape2.getPosition().getY()
              && shape1.getOrientation() == shape2.getOrientation()
              && shape1.getColor().getRGB() == shape2.getColor().getRGB();
    }

    @Override
    public IEditBuilder removeCommand(String id, int time) {
      if (commands.get(id) == null) {
        throw new IllegalArgumentException("No commands associated with this id");
      }
      for (int i = 0; i < commands.get(id).size(); i++) {
        if (time == commands.get(id).get(i).getStartTime()) {
          commands.get(id).remove(i);
          return this;
        }
      }
      if (time == commands.get(id).get(commands.get(id).size() - 1).getEndTime()) {
        commands.get(id).remove(commands.get(id).size() - 1);
        return this;
      }
      throw new IllegalArgumentException("No commands at this time.");
    }



    @Override
    public IEditBuilder removeShape(String id) {
      if (!shapes.containsKey(id)) {
        throw new IllegalArgumentException("No shape exists with this ID");
      }
      shapes.remove(id);
      commands.remove(id);
      return this;
    }

    @Override
    public LinkedHashMap<String, IReadOnlyShape> getShapes() {
      LinkedHashMap<String, IReadOnlyShape> output = new LinkedHashMap<>();
      for (String key : shapes.keySet()) {
        output.put(key, shapes.get(key));
      }
      return output;
    }

    @Override
    public LinkedHashMap<String, List<IReadOnlyRotateCommand>> getCommands() {
      LinkedHashMap<String, List<IReadOnlyRotateCommand>> output = new LinkedHashMap<>();
      for (String key : this.commands.keySet()) {
        List<IReadOnlyRotateCommand> newCommands = new ArrayList<>();
        for (IRotateCommand command : this.commands.get(key)) {
          newCommands.add(command);
        }
        output.put(key, newCommands);
      }
      return output;
    }

    @Override
    public AnimationBuilder addKeyframe(String name, int t, int x, int y, int w,
                                        int h, int r, int g, int b) {
      throw new UnsupportedOperationException("This method is unneeded.");
    }

    @Override
    public Dimension getNeededSpace() {
      int minX = this.x;
      int minY = this.y;
      int maxX = this.x;
      int maxY = this.y;

      for (List<IReadOnlyRotateCommand> commandList : this.getCommands().values()) {
        minX = Math.min(minX,
                commandList.get(0).getStartX());
        minY = Math.min(minY,
                commandList.get(0).getStartY());
        maxX = Math.max(maxX,
                commandList.get(0).getStartX() + commandList.get(0).getStartWidth());
        maxY = Math.max(maxY,
                commandList.get(0).getStartY() + commandList.get(0).getStartHeight());

        for (IReadOnlyCommand command : commandList) {
          minX = Math.min(minX, command.getEndX());
          minY = Math.min(minY, command.getEndY());
          maxX = Math.max(maxX, command.getEndX() + command.getEndWidth());
          maxY = Math.max(maxY, command.getEndY() + command.getEndHeight());
        }
      }
      return new Dimension(maxX - minX, maxY - minY);
    }
  }
}