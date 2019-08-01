package cs3500.animator.view;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

import cs3500.animator.controller.IRotateKeyframe;
import cs3500.animator.model.IReadOnlyShape;

/**
 * Represents a view for an animation that has a GUI for editing the animation.
 */
public interface IEditableView extends IView {
  /**
   * Displays the given list of shapes on the GUI. Used to send a new frame of the animation.
   *
   * @param shapes the shapes to display in the view
   */
  void display(List<IReadOnlyShape> shapes);

  /**
   * Gets the shapes that are being displayed in the list of shapes the user sees.
   *
   * @return a list of shape cells
   */
  List<IShapeCell> getShapes();

  /**
   * Gives the view a map of all the shapes in the animation and their ids, so that they can
   * be displayed for the user to see.
   *
   * @param shapes all the shapes in the animation, mapped by their unique ID
   */
  void setShapes(Map<String, IReadOnlyShape> shapes);

  /**
   * Gets all the keyframes that the view knows about.
   *
   * @return lists of keyframes mapped by the unique ID of the shape they correspond to
   */
  Map<String, List<IRotateKeyframe>> getKeyframes();

  /**
   * Gives the view a map of all the keyframes associated with each shape id, so that they can
   * be displayed for the user to see.
   *
   * @param keyframes all the keyframes in the animation, mapped by their unique ID
   */
  void setKeyframes(Map<String, List<IRotateKeyframe>> keyframes);

  /**
   * Sets the width of the display for the animation to run on.
   *
   * @param width the new width to set the animation display
   */
  void setWidth(int width);

  /**
   * Sets the height of the display for the animation to run on.
   *
   * @param height the new width to set the animation display
   */
  void setHeight(int height);

  /**
   * Adds a listener that receives action events from button presses.
   *
   * @param listener the object that should receive events when buttons are pressed
   */
  void addButtonListener(ActionListener listener);

  /**
   * Adds a listener that receives property change events from new user input.
   *
   * @param listener the object that should receive events when properties are updated
   */
  void addPropertyListener(PropertyChangeListener listener);

  /**
   * Adds a listener that receives a {@link IShapeChangeEvent} when the user changes the shapes
   * in the animation.
   *
   * @param listener the object that should receive events when the shapes in an animation
   *                 are changed
   */
  void addShapeChangeListener(IShapeChangeListener listener);

  /**
   * Adds a listener that receives a {@link IFrameChangeEvent} when the user changes the keyframes
   * for a shape in the animation.
   *
   * @param listener the object that should receive events when the keyframes in an animation
   *                 are changed.
   */
  void addFrameChangeListener(IFrameChangeListener listener);

  /**
   * Displays an error pop up on the screen with the given string so the user can see that an
   * error occurred and what might have caused it.
   *
   * @param s the error message to be displayed.
   */
  void displayError(String s);


  /**
   * Updates the slider to the given tick.
   * @param tick sets the slider to the given percent of the animation.
   */
  void setSlider(double tick);
}
