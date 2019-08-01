package cs3500.animator.view;

import java.awt.*;

import cs3500.animator.model.IReadOnlyShape;

/**
 * Represents a drawing panel for the GUI view, which draws the shapes at each tick of an animation.
 */
public class DrawingPanel extends ADrawingPanel implements IDrawingPanel {

  /**
   * A default constructor for a drawing panel which just calls the default constructor
   * of {@link ADrawingPanel}.
   */
  public DrawingPanel() {
    super();
  }

  @Override
  protected void drawShapesOn(Graphics g) {
    Graphics2D g2D = (Graphics2D) g;
    for ( IReadOnlyShape shape : this.shapes ) {
      g2D.setColor(shape.getColor());
      shape.draw(g2D);
    }
  }
}
