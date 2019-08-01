package cs3500.animator.view;

import java.awt.event.ActionListener;

/**
 * Represents a listener that receives a {@link IShapeChangeEvent} when the user makes a change
 * to the shapes of an animation.
 */
public interface IShapeChangeListener extends ActionListener {
  /**
   * Called when the user changes a shape, with a custom event that holds all the necessary
   * information to update the model.
   *
   * @param event The change that was made to a shape in the animation.
   */
  void shapeChanged(IShapeChangeEvent event);
}
