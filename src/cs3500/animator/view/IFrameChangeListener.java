package cs3500.animator.view;

import java.awt.event.ActionListener;

/**
 * Represents a listener that receives a {@link IFrameChangeEvent} when the user makes a change
 * to the keyframes of a shape of an animation.
 */
public interface IFrameChangeListener extends ActionListener {
  /**
   * Called when the user changes a keyframe, with a custom event that holds all the necessary
   * information to update the model.
   *
   * @param event The change that was made to a keyframe in the animation.
   */
  void keyframeChanged(IFrameChangeEvent event);
}
