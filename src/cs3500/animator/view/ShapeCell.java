package cs3500.animator.view;

/**
 * Represents a cell in a list displayed on the GUI that corresponds to a shape. The toString
 * method is overriden to how it should be displayed on screen for the user.
 */
public class ShapeCell implements IShapeCell {
  private String id;
  private String type;

  /**
   * Constructs a shape cell for a shape with the given ID and type.
   *
   * @param id the unique ID of the shape this cell represents.
   * @param type the type of shape this cell represents (Rectangle, Ellipse, etc).
   */
  public ShapeCell(String id, String type) {
    this.id = id;
    this.type = type;
  }

  @Override
  public String getID() {
    return this.id;
  }

  @Override
  public String getType() {
    return this.type;
  }

  @Override
  public String toString() {
    return this.id + " - " + this.type;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }

    if (!(other instanceof ShapeCell)) {
      return false;
    }

    ShapeCell otherShapeCell = (ShapeCell) other;

    return this.id.equals(otherShapeCell.getID())
            && this.type.equals(otherShapeCell.getType());
  }

  @Override
  public int hashCode() {
    int result = 17;

    result = 31 * result + this.id.hashCode();
    result = 31 * result + this.type.hashCode();

    return result;
  }
}
