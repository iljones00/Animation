package cs3500.animator.view;

import java.util.List;

import cs3500.animator.model.IReadOnlyShape;

/**
 * A drawing panel for an animation. The draw() method paints the given shapes on the panel.
 */
public interface IDrawingPanel {

  /**
   * Paints the given list of shapes onto the panel on the screen.
   * @param shapes is the list of shapes that are painted on the screen.
   */
  void draw(List<IReadOnlyShape> shapes);
}
