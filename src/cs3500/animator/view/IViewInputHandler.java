package cs3500.animator.view;

import java.awt.Component;
import java.awt.Color;
import java.beans.PropertyChangeListener;

/**
 * Represents an input handler for an {@link IEditableView} that will display pop-ups to receive
 * user input and then fire events with those inputs to all subscribed listeners.
 */
public interface IViewInputHandler {
  /**
   * Adds a listener that receives a {@link IShapeChangeEvent} when the user changes the shapes
   * in the animation.
   *
   * @param listener the listener to be added
   */
  void addShapeChangeListener(IShapeChangeListener listener);

  /**
   * Adds a listener that receives a {@link IFrameChangeEvent} when the user changes the keyframes
   * for a shape in the animation.
   *
   * @param listener the listener to be added
   */
  void addFrameChangeListener(IFrameChangeListener listener);

  /**
   * Adds a listener that receives a property change event when the user requests an SVG export.
   *
   * @param listener the listener to be added
   */
  void addExportListener(PropertyChangeListener listener);

  /**
   * Creates a pop-up dialogue that takes a name for the SVG file to be exported, then fires an
   * event that tells export listeners to export the current animation to an SVG file under
   * the given name.
   *
   * @param parent the component which this dialogue should sit on top of
   */
  void exportToSVG(Component parent);

  /**
   * Creates a pop-up dialogue if a shape is being added that asks what the new shapes ID should
   * be and what type of shape it should be. Then fires a {@link IShapeChangeEvent} that
   * reflects this change.
   *
   * @param parent the component which this dialogue should sit on top of
   * @param type the type of change being made to the shapes in the animation
   * @param id the unique ID of the shape being changed
   */
  void changeShape(Component parent, ShapeChange type, String id);

  /**
   * A changeFrame method with less arguments because when shapes are added or deleted these
   * arguments are sufficient.
   *
   * @param parent the component which this dialogue should sit on top of
   * @param type the type of change being made to the keyframes of the animation
   * @param id the unique ID of the shape this keyframe corresponds to
   * @param time the time that this keyframe describes its shapes state
   */
  void changeFrame(Component parent, FrameChange type, String id, int time);

  /**
   * Creates a pop-up dialogue if a keyframe is being added or edited (if editing, the keyframe's
   * old info is displayed as it is passed in to this method). Once given input, it fires a
   * {@link IFrameChangeEvent} that reflects this change.
   *
   * @param parent the component which this dialogue should sit on top of
   * @param type the type of change being made to the keyframes of the animation
   * @param id the unique ID of the shape this keyframe corresponds to
   * @param time the time that this keyframe describes its shapes state
   * @param x the x coordinate of the shape at this new frame
   * @param y the y coordinate of the shape at this new frame.
   * @param width the width of the shape at this new frame.
   * @param height the height of the shape at this new frame.
   * @param orientation the orientation of the shape at this new frame.
   * @param color the color of the shape at this new frame.
   */
  void changeFrame(Component parent, FrameChange type, String id, int time, int x,
                   int y, int width, int height, int orientation, Color color);
}
