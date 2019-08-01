package cs3500.animator.model;

public class SuperMasterCommand extends MasterCommand implements IRotateCommand {
  private final int startOrientation;
  private final int endOrientation;

  /**
   * Constructor for super master command that initializes each of the fields of the command.
   *
   * @param type represents what type of command this is.
   * @param t1   represents starting time of the command.
   * @param x1   represents the starting x coordinate of the shape.
   * @param y1   represents the starting y coordinate of the shape.
   * @param w1   represents the starting width of the shape.
   * @param h1   represents the starting height of the shape.
   * @param o1   represents the starting orientation of the shape.
   * @param r1   represents the starting value for red of the shape.
   * @param g1   represents the starting value for green of the shape.
   * @param b1   represents the starting value for blue of the shape.
   * @param t2   represents the ending time of the command.
   * @param x2   represents the ending x coordinate of the shape.
   * @param y2   represents the ending y coordinate of the shape.
   * @param w2   represents the ending width of the shape.
   * @param h2   represents the ending height of the shape.
   * @param o2   represents the ending orientation of the shape.
   * @param r2   represents the ending red value for the shape.
   * @param g2   represents the ending green value for the shape.
   * @param b2   represents the ending blue value for the shape.
   * @throws IllegalArgumentException when the start time or end time is negative, if the width or
   *                                  height is negative at any point, or if the start time is after
   *                                  the end time.
   */
  public SuperMasterCommand(String type, int t1, int x1, int y1, int w1, int h1, int o1, int r1,
                            int g1, int b1, int t2, int x2, int y2, int w2, int h2, int o2, int r2,
                            int g2, int b2) throws IllegalArgumentException {
    super(type, t1, x1, y1, w1, h1, r1, g1, b1, t2, x2, y2, w2, h2, r2, g2, b2);

    this.startOrientation = o1;
    this.endOrientation = o2;
  }

  @Override
  public IShape setState(int time, IShape shape) {
    shape.setOrientation(this.findPointAt(time, this.startOrientation, this.endOrientation));
    return super.setState(time, shape);
  }

  /**
   * Gets the orientation of this command which the starting orientation the shape.
   *
   * @return int that represents the orientation in degrees
   */
  @Override
  public int getStartOrientation() {
    return this.startOrientation;
  }

  /**
   * Gets the orientation of this command which the final orientation the shape.
   *
   * @return int that represents the orientation in degrees
   */
  @Override
  public int getEndOrientation() {
    return this.endOrientation;
  }
}
