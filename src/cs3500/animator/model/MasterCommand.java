package cs3500.animator.model;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * Represents a command on a Shape, with each of the starting values and ending values of the
 * fields of the shape.
 */
public class MasterCommand implements ICommand {
  private final String type;
  protected final int startTime;
  private final int startX;
  private final int startY;
  private final int startWidth;
  private final int startHeight;
  private final Color startColor;

  protected final int endTime;
  private final int endX;
  private final int endY;
  private final int endWidth;
  private final int endHeight;
  private final Color endColor;

  /**
   * Constructor for master command that initalizes each of the fields of the command.
   * @param type represents what type of command this is.
   * @param t1 represents starting time of the command.
   * @param x1 represents the starting x coordinate of the shape in the command.
   * @param y1 represents the starting y coordinate of the shape in this command.
   * @param w1 represents the starting width of the shape in this command.
   * @param h1 represents the starting height of the shape in this command.
   * @param r1 represents the starting value for red of the shape.
   * @param g1 represents the starting value for green of the shape.
   * @param b1 represents the starting value for blue of the shape.
   * @param t2 represents the ending time of the command.
   * @param x2 represents the ending x coordinate of the shape in this command.
   * @param y2 represents the ending y coordinate of the shape in this command.
   * @param w2 represents the ending width of the shape in this command.
   * @param h2 represents the ending height of the shape.
   * @param r2 represents the ending red value for the shape.
   * @param g2 represents the ending green value for the shape.
   * @param b2 represents the ending blue value for the shape.
   * @throws IllegalArgumentException when the start time or end time is negative, if the width
   *         or height is negative at any point, or if the start time is after the end time.
   */
  public MasterCommand(String type, int t1, int x1, int y1, int w1, int h1, int r1, int g1, int b1,
                       int t2, int x2, int y2, int w2, int h2, int r2, int g2, int b2)
          throws IllegalArgumentException {
    if (t1 > t2 || t1 < 0) {
      throw new IllegalArgumentException("Invalid start or end time.");
    }

    if (w1 < 0 || w2 < 0) {
      throw new IllegalArgumentException("Width cannot be negative.");
    }

    if (h1 < 0 || h2 < 0) {
      throw new IllegalArgumentException("Height cannot be negative.");
    }

    if (r1 < 0 || r2 < 0 || g1 < 0 || g2 < 0 || b1 < 0 || b2 < 0
            || r1 > 255 || r2 > 255 || g1 > 255 || g2 > 255 || b1 > 255 || b2 > 255) {
      throw new IllegalArgumentException("RGB values have to be [0, 255].");
    }

    this.type = type;
    this.startTime = t1;
    this.startX = x1;
    this.startY = y1;
    this.startWidth = w1;
    this.startHeight = h1;
    this.startColor = new Color(r1, g1, b1);
    this.endTime = t2;
    this.endX = x2;
    this.endY = y2;
    this.endWidth = w2;
    this.endHeight = h2;
    this.endColor = new Color(r2, g2, b2);
  }

  @Override
  public String getType() {
    return this.type;
  }

  @Override
  public int getStartTime() {
    return this.startTime;
  }

  @Override
  public int getStartX() {
    return startX;
  }

  @Override
  public int getStartY() {
    return startY;
  }

  @Override
  public int getStartWidth() {
    return startWidth;
  }

  @Override
  public int getStartHeight() {
    return startHeight;
  }

  @Override
  public Color getStartColor() {
    return startColor;
  }

  @Override
  public int getEndTime() {
    return this.endTime;
  }

  @Override
  public int getEndX() {
    return endX;
  }

  @Override
  public int getEndY() {
    return endY;
  }

  @Override
  public int getEndWidth() {
    return endWidth;
  }

  @Override
  public int getEndHeight() {
    return endHeight;
  }

  @Override
  public Color getEndColor() {
    return endColor;
  }

  /**
   * Mutates the shape to its instantaneous state at the given time after doing this command. Throws
   * an exception if the time is not within the start and end times of this command.
   *
   * @param time  in ticks that the shape gets changed to.
   * @param shape which is the starting shape before the command has been run to the given time.
   * @return IShape that is the mutated shape after the command has been run to the given time.
   */
  @Override
  public IShape setState(int time, IShape shape) {
    if (time < this.startTime || time > this.endTime) {
      throw new IllegalArgumentException("Invalid time input.");
    }
    else if (this.startTime == this.endTime) {
      shape.setPosition(new Point2D.Double(this.endX, this.endY));
      shape.setWidth(this.endWidth);
      shape.setHeight(this.endHeight);
      shape.setColor(this.endColor);
      return shape;
    }

    int newX = this.findPointAt(time, this.startX, this.endX);
    int newY = this.findPointAt(time, this.startY, this.endY);
    shape.setPosition(new Point2D.Double(newX, newY));

    shape.setWidth(this.findPointAt(time, this.startWidth, this.endWidth));
    shape.setHeight(this.findPointAt(time, this.startHeight, this.endHeight));

    int newRed = this.findPointAt(time, this.startColor.getRed(), this.endColor.getRed());
    int newGreen = this.findPointAt(time, this.startColor.getGreen(), this.endColor.getGreen());
    int newBlue = this.findPointAt(time, this.startColor.getBlue(), this.endColor.getBlue());
    shape.setColor(new Color(newRed, newGreen, newBlue));

    return shape;
  }

  //calculates the position of the shape at the middle of the two values.
  protected int findPointAt(int time, int startValue, int endValue) {
    if (this.startTime == this.endTime) {
      return endValue;
    }
    else {
      return ((endValue - startValue) * (time - this.startTime))
              / (this.endTime - this.startTime) + startValue;
    }
  }
}
