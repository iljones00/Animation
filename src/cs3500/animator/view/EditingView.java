package cs3500.animator.view;

import com.sun.istack.internal.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Font;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs3500.animator.controller.IRotateKeyframe;
import cs3500.animator.controller.RotateKeyframe;
import cs3500.animator.model.IReadOnlyShape;

/**
 * Represents a GUI view for an animation that allows for editing of the animation displayed.
 * This includes adding shapes, removing shapes, adding keyframes, editing keyframes,
 * and removing keyframes. The animation can be played, paused, reset, looped, and exported
 * to an SVG file.
 */
public class EditingView extends JFrame implements IEditableView, ListSelectionListener,
        ActionListener, ChangeListener {
  private ADrawingPanel drawingPanel;
  private JPanel toolbar;
  private JPanel shapesEditor;
  private JPanel framesEditor;
  private JSlider slider;

  private JTextField tickSpeedField;
  private JList<IShapeCell> shapeListContainer;
  private JLabel frameListLabel;
  private JList<IRotateKeyframe> frameListContainer;
  private DefaultListModel<IShapeCell> shapesList;
  private DefaultListModel<IRotateKeyframe> framesList;
  private Map<String, List<IRotateKeyframe>> keyframes;

  private List<ActionListener> buttonListeners;
  private List<PropertyChangeListener> tickSpeedListeners;

  private int width = 1000;
  private int height = 600;
  private int shapeSelected = -1;
  private int frameSelected = -1;
  private IViewInputHandler handler;
  private boolean sliderPauser = true;

  /**
   * Constructs an Editing View, creating the GUI with all the interactive pieces and initializing
   * all listeners. Takes a ticks per second which sets the starting speed of the animation.
   *
   * @param ticksPerSecond the starting speed of the animation in ticks per second
   */
  public EditingView(int ticksPerSecond) {
    super();

    this.tickSpeedField = new JTextField("" + ticksPerSecond);

    this.handler = new InputHandler();
    this.buttonListeners = new ArrayList<>();
    this.tickSpeedListeners = new ArrayList<>();

    this.keyframes = new LinkedHashMap<>();

    this.shapesList = new DefaultListModel<>();
    this.framesList = new DefaultListModel<>();

    this.setLayout(new BorderLayout());

    this.drawingPanel = new DrawingPanel();
    this.drawingPanel.setPreferredSize(new Dimension(this.width, this.height));

    JScrollPane scrollPanel = new JScrollPane(this.drawingPanel);
    this.addBorder(scrollPanel);

    slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
    slider.addChangeListener(this);
    slider.setPreferredSize(new Dimension(200, 32));
    JPanel display = new JPanel(new BorderLayout());
    display.add(slider, BorderLayout.SOUTH);
    display.add(scrollPanel, BorderLayout.CENTER);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.add(display, BorderLayout.CENTER);

    this.createShapesEditor();
    this.add(this.shapesEditor, BorderLayout.WEST);

    this.createFramesEditor();
    this.add(this.framesEditor, BorderLayout.EAST);

    this.createToolbar();
    this.add(this.toolbar, BorderLayout.SOUTH);

    this.setSize(this.width + 508, this.height + 48);

  }

  private void createToolbar() {
    JButton play = new JButton("play");
    play.setActionCommand("play");
    play.addActionListener(this);
    JButton pause = new JButton("pause");
    pause.setActionCommand("pause");
    pause.addActionListener(this);
    JPanel playPause = new JPanel();
    playPause.add(play);
    playPause.add(pause);

    JLabel loop = new JLabel("loop:");
    JCheckBox loopToggle = new JCheckBox();
    loopToggle.setActionCommand("loop");
    loopToggle.addActionListener(this);
    JPanel loopPanel = new JPanel();
    loopPanel.add(loop);
    loopPanel.add(loopToggle);

    JButton restart = new JButton("restart");
    restart.setActionCommand("restart");
    restart.addActionListener(this);

    JButton export = new JButton("export to SVG");
    export.setActionCommand("export");
    export.addActionListener(this);

    JLabel speed = new JLabel("speed:");
    this.tickSpeedField.setPreferredSize(new Dimension(34, 26));
    this.tickSpeedField.setActionCommand("tick speed");
    this.tickSpeedField.addActionListener(this);
    JLabel tickPerSec = new JLabel("ticks/sec");
    JPanel tickPanel = new JPanel();
    tickPanel.add(speed);
    tickPanel.add(this.tickSpeedField);
    tickPanel.add(tickPerSec);

    this.toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
    this.toolbar.add(tickPanel);
    this.toolbar.add(playPause);
    this.toolbar.add(loopPanel);
    this.toolbar.add(restart);
    this.toolbar.add(export);
    this.addBorder(this.toolbar);
  }

  private void createShapesEditor() {
    JLabel title = new JLabel("Shapes", SwingConstants.CENTER);
    title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 25));
    title.setPreferredSize(new Dimension(254, 60));
    this.addBorder(title);

    this.shapeListContainer = new JList<>(this.shapesList);
    this.shapeListContainer.addListSelectionListener(this);
    JScrollPane scrollieShapes = new JScrollPane(this.shapeListContainer);
    this.addBorder(scrollieShapes);

    JButton add = new JButton("add");
    add.setActionCommand("shape add");
    add.addActionListener(this);
    JButton delete = new JButton("delete");
    delete.setActionCommand("shape delete");
    delete.addActionListener(this);
    JPanel buttons = new JPanel();
    buttons.add(add);
    buttons.add(delete);
    this.addBorder(buttons);

    this.shapesEditor = new JPanel(new BorderLayout());
    this.shapesEditor.add(title, BorderLayout.NORTH);
    this.shapesEditor.add(scrollieShapes, BorderLayout.CENTER);
    this.shapesEditor.add(buttons, BorderLayout.SOUTH);
    this.shapesEditor.setPreferredSize(new Dimension(254, 82));
  }

  private void createFramesEditor() {
    JLabel title = new JLabel("Keyframes", SwingConstants.CENTER);
    title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 25));
    title.setPreferredSize(new Dimension(254, 60));
    this.addBorder(title);

    this.frameListContainer = new JList<>(this.framesList);
    this.frameListContainer.addListSelectionListener(this);
    JScrollPane scrollieFrames = new JScrollPane(this.frameListContainer);
    this.addBorder(scrollieFrames);
    this.frameListLabel = new JLabel("Select a shape on the left", SwingConstants.CENTER);
    this.frameListLabel.setPreferredSize(new Dimension(254, 40));
    this.addBorder(this.frameListLabel);
    JPanel frameListTitled = new JPanel(new BorderLayout());
    frameListTitled.add(scrollieFrames, BorderLayout.CENTER);
    frameListTitled.add(this.frameListLabel, BorderLayout.NORTH);

    JButton edit = new JButton("edit");
    edit.setActionCommand("frame edit");
    edit.addActionListener(this);
    JButton add = new JButton("add");
    add.setActionCommand("frame add");
    add.addActionListener(this);
    JButton delete = new JButton("delete");
    delete.setActionCommand("frame delete");
    delete.addActionListener(this);
    JPanel buttons = new JPanel();
    buttons.add(edit);
    buttons.add(add);
    buttons.add(delete);
    this.addBorder(buttons);

    this.framesEditor = new JPanel(new BorderLayout());
    this.framesEditor.add(title, BorderLayout.NORTH);
    this.framesEditor.add(frameListTitled, BorderLayout.CENTER);
    this.framesEditor.add(buttons, BorderLayout.SOUTH);
    this.framesEditor.setPreferredSize(new Dimension(275, 271));
  }

  @Override
  public void play() {
    this.setVisible(true);
  }

  @Override
  public void display(List<IReadOnlyShape> shapes) {
    this.drawingPanel.draw(shapes);
  }

  @Override
  public List<IShapeCell> getShapes() {
    List<IShapeCell> output = new ArrayList<>();
    for (int i = 0 ; i < this.shapesList.size() ; i ++) {
      output.add(this.shapesList.elementAt(i));
    }

    return output;
  }

  @Override
  public void setShapes(Map<String, IReadOnlyShape> shapes) {
    this.shapesList.clear();

    for (String key : shapes.keySet()) {
      this.shapesList.addElement(new ShapeCell(key, shapes.get(key).getShapeType()));
    }

    this.framesList.clear();
    this.frameListContainer.clearSelection();
  }

  @Override
  public Map<String, List<IRotateKeyframe>> getKeyframes() {
    return this.keyframes;
  }

  @Override
  public void setKeyframes(Map<String, List<IRotateKeyframe>> keyframes) {
    this.keyframes = new LinkedHashMap<>();
    for (String id : keyframes.keySet()) {
      List<IRotateKeyframe> newList = new ArrayList<>();

      for (IRotateKeyframe frame : keyframes.get(id)) {
        newList.add(new RotateKeyframe(frame.getTime(), frame.getX(), frame.getY(),
                frame.getWidth(), frame.getHeight(), frame.getOrientation(),
                frame.getColor()));
      }
      this.keyframes.put(id, newList);
    }
    this.updateFramesList();
  }

  @Override
  public void setWidth(int width) {
    this.width = width;
    this.drawingPanel.setPreferredSize(new Dimension(this.width, this.height));
  }

  @Override
  public void setHeight(int height) {
    this.height = height;
    this.drawingPanel.setPreferredSize(new Dimension(this.width, this.height));
  }

  @Override
  public void addButtonListener(ActionListener listener) {
    this.buttonListeners.add(listener);
  }

  @Override
  public void addPropertyListener(PropertyChangeListener listener) {
    this.tickSpeedListeners.add(listener);
    this.handler.addExportListener(listener);
  }

  @Override
  public void addShapeChangeListener(IShapeChangeListener listener) {
    this.handler.addShapeChangeListener(listener);
  }

  @Override
  public void addFrameChangeListener(IFrameChangeListener listener) {
    this.handler.addFrameChangeListener(listener);
  }

  @Override
  public void displayError(String s) {
    JOptionPane.showMessageDialog(this, s, "An error occurred",
            JOptionPane.ERROR_MESSAGE);
  }

  private void fireButtonEvent(String type) {
    for (ActionListener listener : this.buttonListeners) {
      listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, type));
    }
  }

  private void firePropertyChangeEvent(String type, String newValue) {
    for (PropertyChangeListener listener : this.tickSpeedListeners) {
      listener.propertyChange(new PropertyChangeEvent(this, type,
              newValue, newValue));
    }
  }

  private void updateFramesList() {
    this.framesList.clear();

    if (this.shapeSelected >= 0 && this.shapeSelected < this.shapesList.size()) {
      String id = this.shapesList.elementAt(this.shapeSelected).getID();

      if (this.keyframes.containsKey(id)) {
        for (IRotateKeyframe cell : this.keyframes.get(id)) {
          this.framesList.addElement(cell);
        }
      }
    }
    if (this.frameSelected >= this.framesList.size()) {
      this.frameSelected = 0;
    }
    if (this.frameSelected >= 0) {
      this.frameListContainer.setSelectedIndex(this.shapeSelected);
    }
  }

  private void addBorder(@NotNull JComponent comp) {
    comp.setBorder(BorderFactory.createLineBorder(Color.lightGray));
  }

  /**
   * Called whenever the shape or keyframe currently selected changes.
   *
   * @param event the event that characterizes the change.
   */
  @Override
  public void valueChanged(ListSelectionEvent event) {
    int newShapeSelected = this.shapeListContainer.getSelectedIndex();

    int newFrameSelected = -1;

    try {
      newFrameSelected = this.frameListContainer.getSelectedIndex();
    } catch (NullPointerException e) {
      // there is no frame selected, do nothing
    }

    if (newFrameSelected != -1) {
      this.frameSelected = newFrameSelected;
    }

    if (newShapeSelected != -1 && newShapeSelected != this.shapeSelected) {
      this.shapeSelected = newShapeSelected;
      updateFramesList();
      this.frameListContainer.clearSelection();
      this.frameListLabel.setText("Shape: " + this.shapesList.getElementAt(this.shapeSelected));
    }
  }

  /**
   * Invoked whenever any button is pressed.
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    String command = event.getActionCommand();

    if (command.equals("tick speed")) {
      this.firePropertyChangeEvent("tick speed", this.tickSpeedField.getText());
      return;
    }

    if (command.equals("export")) {
      this.handler.exportToSVG(this);
    }

    if (command.equals("shape add")) {
      this.handler.changeShape(this, ShapeChange.ADD, "");
      return;
    }

    IShapeCell shape;
    if (this.shapeSelected != -1 || !this.shapeListContainer.isSelectionEmpty()) {
      shape = this.shapesList.elementAt(this.shapeSelected);
    }
    else {
      this.fireButtonEvent(command);
      return;
    }

    if (command.equals("shape delete")) {
      this.handler.changeShape(this, ShapeChange.DELETE, shape.getID());
      return;
    }

    if (command.equals("frame add")) {
      this.handler.changeFrame(this, FrameChange.ADD, shape.getID(), 0);
      return;
    }

    IRotateKeyframe keyframe;
    if (this.frameSelected != -1 || !this.frameListContainer.isSelectionEmpty()) {
      keyframe = this.framesList.elementAt(this.frameSelected);
    }
    else {
      this.fireButtonEvent(command);
      return;
    }

    switch (command) {
      case "frame edit":
        this.handler.changeFrame(this, FrameChange.EDIT, shape.getID(), keyframe.getTime(),
                keyframe.getX(), keyframe.getY(), keyframe.getWidth(), keyframe.getHeight(),
                keyframe.getOrientation(), keyframe.getColor());
        break;
      case "frame delete":
        this.handler.changeFrame(this, FrameChange.DELETE, shape.getID(), keyframe.getTime());
        break;
      default:
        this.fireButtonEvent(command);
    }
  }

  @Override
  public void stateChanged(ChangeEvent event) {
    if (sliderPauser) {
      this.firePropertyChangeEvent("slider", "" + slider.getValue());
    }
    else {
      sliderPauser = true;
    }
  }

  @Override
  public void setSlider(double tick) {
    sliderPauser = false;
    slider.setValue((int) (tick * 100));

  }
}