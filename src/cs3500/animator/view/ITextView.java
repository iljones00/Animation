package cs3500.animator.view;

/**
 * Represents the views that display the animation by appending to an appendable.
 */
public interface ITextView extends IView {
  /**
   * Gets the texts that represents the animation.
   * @return String that is the description for the animation.
   */
  String getText();
}
