package cs3500.animator.view;

import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import cs3500.animator.model.IReadOnlyShape;

/**
 * Represents a panel for a visual view that paints read-only shapes onto itself for display.
 */
public abstract class ADrawingPanel extends JPanel implements IDrawingPanel {
  protected List<IReadOnlyShape> shapes;

  protected ADrawingPanel() {
    super();
    this.shapes = null;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (this.shapes != null) {
      this.drawShapesOn(g);
    }
  }

  // delegates the actual painting of shapes to the extending class.
  protected abstract void drawShapesOn(Graphics g);

  @Override
  public void draw(List<IReadOnlyShape> shapes) {
    this.shapes = shapes;
    this.repaint();
  }
}
