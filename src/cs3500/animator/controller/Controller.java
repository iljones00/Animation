package cs3500.animator.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cs3500.animator.model.IAnimationModel;
import cs3500.animator.model.IEditBuilder;
import cs3500.animator.model.IReadOnlyRotateCommand;
import cs3500.animator.model.IReadOnlyShape;
import cs3500.animator.model.SuperMasterCommand;
import cs3500.animator.view.IEditableView;
import cs3500.animator.view.IFrameChangeEvent;
import cs3500.animator.view.IFrameChangeListener;
import cs3500.animator.view.IShapeChangeEvent;
import cs3500.animator.view.IShapeChangeListener;
import cs3500.animator.view.ITextView;
import cs3500.animator.view.SVGAnimationView;

import static java.util.Objects.requireNonNull;

/**
 * Represents a controller that works the logic of the editing view.
 */
public class Controller implements IController, ActionListener, IFrameChangeListener,
        IShapeChangeListener, PropertyChangeListener {

  private IEditBuilder builder;
  private IEditableView view;
  private int ticksPerSecond = 20;
  private Timer timer;
  private int firstTick;
  private int finalTick;
  private int currentTick;
  private boolean isPaused = true;
  private boolean isLooping = false;

  /**
   * Constructor for the controller that takes an Animation builder, a view, and a tick speed
   * to run the animation at.
   * @param builder an editable builder for an animation model
   * @param view an editable view to ask for changes to the model and react to them
   * @param ticksPerSecond the speed the animation should initially play at
   */
  public Controller(IEditBuilder builder, IEditableView view, int ticksPerSecond) {
    this.builder = requireNonNull(builder);
    this.view = requireNonNull(view);
    this.ticksPerSecond = ticksPerSecond;
    this.firstTick = this.getFirstTick();
    this.finalTick = builder.build().getFinalTick();
    this.currentTick = this.firstTick;
  }

  /**
   * Starts the current view, creating the timer and running the animation at the given tick
   * speed.
   */
  public void play() {
    IAnimationModel model = this.builder.build();
    if (this.isPaused && this.currentTick < model.getFinalTick()) {
      this.firstTick = this.getFirstTick();
      this.timer = new Timer();
      this.timer.schedule(new DrawFrameTask(model), 0, 1000 / this.ticksPerSecond);
      isPaused = false;
    }
  }

  /**
   * Action Listener for a key frame change event that allows the user to add, edit, or remove a
   * key frame from the animation. Once a change has been made, send the new list of keyframes
   * from the model to the view. If any bad fields are sent with the command
   * call a display error method in the view to show to the user that the given operation is not
   * permitted.
   * @param event is the event sent by the view to this controller to allow for communication
   *              between the view and the controller.
   */
  @Override
  public void keyframeChanged(IFrameChangeEvent event) {
    List<IReadOnlyRotateCommand> commands = builder.getCommands().get(event.getId());
    if (event.getTime() < 0) {
      view.displayError("Keyframes can only be added at a non-negative tick");
      return;
    }
    switch (event.getType()) {
      case ADD:
        if (commands == null) {
          try {
            builder.addMotion(event.getId(), event.getTime(), event.getX(), event.getY(),
                    event.getWidth(), event.getHeight(), event.getOrientation(),
                    event.getColor().getRed(), event.getColor().getGreen(),
                    event.getColor().getBlue(), event.getTime(), event.getX(), event.getY(),
                    event.getWidth(), event.getHeight(), event.getOrientation(),
                    event.getColor().getRed(), event.getColor().getGreen(),
                    event.getColor().getBlue());
            this.view.setKeyframes(this.convertToKeyFrames(this.builder.getCommands()));
            finalTick = builder.build().getFinalTick();
          }
          catch (IllegalArgumentException e) {
            view.displayError(e.getMessage());
          }
          return;
        }
        if (timeExists(event.getTime(), commands)) {
          view.displayError("Keyframe already exists at this time.");
          return;
        }
        if (event.getTime() < commands.get(0).getStartTime()) {
          try {
            builder.addMotion(event.getId(), event.getTime(), event.getX(), event.getY(),
                    event.getWidth(), event.getHeight(), event.getOrientation(),
                    event.getColor().getRed(), event.getColor().getGreen(),
                    event.getColor().getBlue(), commands.get(0).getStartTime(),
                    commands.get(0).getStartX(), commands.get(0).getStartY(),
                    commands.get(0).getStartWidth(), commands.get(0).getStartHeight(),
                    commands.get(0).getStartOrientation(), commands.get(0).getStartColor().getRed(),
                    commands.get(0).getStartColor().getGreen(),
                    commands.get(0).getStartColor().getBlue());
            view.setKeyframes(convertToKeyFrames(builder.getCommands()));
            finalTick = builder.build().getFinalTick();
          }
          catch (IllegalArgumentException e) {
            view.displayError(e.getMessage());
          }
          return;
        }
        else if (event.getTime() > commands.get(commands.size() - 1).getEndTime()) {
          IReadOnlyRotateCommand temp = commands.get(commands.size() - 1);
          try {
            builder.addMotion(event.getId(), temp.getEndTime(),
                    temp.getEndX(), temp.getEndY(), temp.getEndWidth(),
                    temp.getEndHeight(), temp.getEndOrientation(), temp.getEndColor().getRed(),
                    temp.getEndColor().getGreen(), temp.getEndColor().getBlue(),
                    event.getTime(), event.getX(), event.getY(),
                    event.getWidth(), event.getHeight(), event.getOrientation(),
                    event.getColor().getRed(), event.getColor().getGreen(),
                    event.getColor().getBlue());
            if (temp.getStartTime() == temp.getEndTime()) {
              this.builder.removeCommand(event.getId(), temp.getStartTime());
            }
            view.setKeyframes(convertToKeyFrames(builder.getCommands()));
            finalTick = builder.build().getFinalTick();
          }
          catch (IllegalArgumentException e) {
            view.displayError(e.getMessage());
          }
          return;
        }
        else {
          for (IReadOnlyRotateCommand command : commands) {
            if (event.getTime() == command.getStartTime()
                    || event.getTime() == command.getEndTime()) {
              this.view.displayError("There is already a keyframe at this tick.");
              return;
            }
            if ( event.getTime() > command.getStartTime()
                    && event.getTime() < command.getEndTime()) {
              try {
                builder.removeCommand(event.getId(), command.getStartTime());
                builder.addMotion(event.getId(), command.getStartTime(),
                        command.getStartX(), command.getStartY(), command.getStartWidth(),
                        command.getStartHeight(), command.getStartOrientation(),
                        command.getStartColor().getRed(), command.getStartColor().getGreen(),
                        command.getStartColor().getBlue(), event.getTime(), event.getX(),
                        event.getY(), event.getWidth(), event.getHeight(), event.getOrientation(),
                        event.getColor().getRed(), event.getColor().getGreen(),
                        event.getColor().getBlue());
                builder.addMotion(event.getId(), event.getTime(), event.getX(), event.getY(),
                        event.getWidth(), event.getHeight(), event.getOrientation(),
                        event.getColor().getRed(), event.getColor().getGreen(),
                        event.getColor().getBlue(), command.getEndTime(), command.getEndX(),
                        command.getEndY(), command.getEndWidth(), command.getEndHeight(),
                        command.getEndOrientation(), command.getEndColor().getRed(),
                        command.getEndColor().getGreen(), command.getEndColor().getBlue());
                view.setKeyframes(convertToKeyFrames(builder.getCommands()));
                finalTick = builder.build().getFinalTick();
              }
              catch (IllegalArgumentException e) {
                view.displayError(e.getMessage());
              }
              return;
            }
          }
        }
        return;
      case EDIT:
        if (!timeExists(event.getTime(), commands)) {
          view.displayError("Keyframe does not exist at this time.");
          return;
        }

        if (event.getTime() == commands.get(0).getStartTime()) {
          try {
            builder.removeCommand(event.getId(), event.getTime());
            builder.addMotion(event.getId(), event.getTime(), event.getX(), event.getY(),
                    event.getWidth(), event.getHeight(), event.getOrientation(),
                    event.getColor().getRed(), event.getColor().getGreen(),
                    event.getColor().getBlue(), commands.get(0).getEndTime(),
                    commands.get(0).getEndX(), commands.get(0).getEndY(),
                    commands.get(0).getEndWidth(), commands.get(0).getEndHeight(),
                    commands.get(0).getEndOrientation(), commands.get(0).getEndColor().getRed(),
                    commands.get(0).getEndColor().getGreen(),
                    commands.get(0).getEndColor().getBlue());
            view.setKeyframes(convertToKeyFrames(builder.getCommands()));
          } catch (IllegalArgumentException e) {
            view.displayError(e.getMessage());
          }
        }
        else if (event.getTime() == commands.get(commands.size() - 1).getEndTime()) {
          try {
            builder.removeCommand(event.getId(), event.getTime());

            IReadOnlyRotateCommand lastCommand = commands.get(commands.size() - 1);
            builder.addMotion(event.getId(),
                    lastCommand.getStartTime(),
                    lastCommand.getStartX(),
                    lastCommand.getStartY(),
                    lastCommand.getStartWidth(),
                    lastCommand.getStartHeight(),
                    lastCommand.getStartOrientation(),
                    lastCommand.getStartColor().getRed(),
                    lastCommand.getStartColor().getGreen(),
                    lastCommand.getStartColor().getBlue(), event.getTime(),
                    event.getX(), event.getY(),
                    event.getWidth(), event.getHeight(),
                    event.getOrientation(), event.getColor().getRed(),
                    event.getColor().getGreen(), event.getColor().getBlue());
            view.setKeyframes(convertToKeyFrames(builder.getCommands()));
          }
          catch (IllegalArgumentException e) {
            view.displayError(e.getMessage());
          }
        }

        for (int i = 1; i < commands.size(); i++) {
          if (event.getTime() == commands.get(i).getStartTime()) {
            try {
              builder.removeCommand(event.getId(), commands.get(i).getStartTime());
              builder.removeCommand(event.getId(), commands.get(i - 1).getStartTime());
              builder.addMotion(event.getId(), commands.get(i - 1).getStartTime(),
                      commands.get(i - 1).getStartX(),
                      commands.get(i - 1).getStartY(), commands.get(i - 1).getStartWidth(),
                      commands.get(i - 1).getStartHeight(),
                      commands.get(i - 1).getStartOrientation(),
                      commands.get(i - 1).getStartColor().getRed(),
                      commands.get(i - 1).getStartColor().getGreen(),
                      commands.get(i - 1).getStartColor().getBlue(),
                      event.getTime(), event.getX(), event.getY(),
                      event.getWidth(), event.getHeight(), event.getOrientation(),
                      event.getColor().getRed(), event.getColor().getGreen(),
                      event.getColor().getBlue());
              builder.addMotion(event.getId(),
                      event.getTime(), event.getX(), event.getY(),
                      event.getWidth(), event.getHeight(), event.getOrientation(),
                      event.getColor().getRed(), event.getColor().getGreen(),
                      event.getColor().getBlue(), commands.get(i).getEndTime(),
                      commands.get(i).getEndX(), commands.get(i).getEndY(),
                      commands.get(i).getEndWidth(), commands.get(i).getEndHeight(),
                      commands.get(i).getEndOrientation(), commands.get(i).getEndColor().getRed(),
                      commands.get(i).getEndColor().getGreen(),
                      commands.get(i).getEndColor().getBlue());
              view.setKeyframes(convertToKeyFrames(builder.getCommands()));
            }
            catch (IllegalArgumentException e) {
              view.displayError(e.getMessage());
            }
          }
        }
        return;
      case DELETE:
        if (!timeExists(event.getTime(), commands)) {
          view.displayError("Keyframe does not exist at this time.");
          return;
        }
        if (event.getTime() == commands.get(0).getStartTime()
                || (event.getTime() == commands.get(commands.size() - 1).getEndTime())) {
          try {
            builder.removeCommand(event.getId(), event.getTime());
            view.setKeyframes(convertToKeyFrames(builder.getCommands()));
          }
          catch (IllegalArgumentException e) {
            view.displayError(e.getMessage());
          }
        }
        else {
          for (int i = 0; i < commands.size(); i++) {
            if (event.getTime() == commands.get(i).getStartTime()) {
              SuperMasterCommand command = combine(commands.get(i - 1), commands.get(i));
              try {
                builder.removeCommand(event.getId(), event.getTime());
                builder.removeCommand(event.getId(), commands.get(i - 1).getStartTime());
                builder.addMotion(event.getId(), command.getStartTime(), command.getStartX(),
                        command.getStartY(), command.getStartWidth(), command.getStartHeight(),
                        command.getStartOrientation(),
                        command.getStartColor().getRed(), command.getStartColor().getGreen(),
                        command.getStartColor().getBlue(), command.getEndTime(), command.getEndX(),
                        command.getEndY(), command.getEndWidth(), command.getEndHeight(),
                        command.getEndOrientation(),
                        command.getEndColor().getRed(), command.getEndColor().getGreen(),
                        command.getEndColor().getBlue());
                view.setKeyframes(convertToKeyFrames(builder.getCommands()));
              }
              catch (IllegalArgumentException e) {
                view.displayError(e.getMessage());
              }
            }
          }
        }
        finalTick = builder.build().getFinalTick();
        return;
      default:
    }
  }

  // takes the starting values of one command and the ending values of a second to make one new
  // command.
  private SuperMasterCommand combine(IReadOnlyRotateCommand command1, IReadOnlyRotateCommand command2) {
    return new SuperMasterCommand(command1.getType(), command1.getStartTime(), command1.getStartX(),
            command1.getStartY(), command1.getStartWidth(), command1.getStartHeight(),
            command1.getStartOrientation(),
            command1.getStartColor().getRed(), command1.getStartColor().getGreen(),
            command1.getStartColor().getBlue(), command2.getEndTime(), command2.getEndX(),
            command2.getEndY(), command2.getEndWidth(), command2.getEndHeight(),
            command2.getEndOrientation(),
            command2.getEndColor().getRed(), command2.getEndColor().getGreen(),
            command2.getEndColor().getBlue());
  }


  // Checks if any command starts with the given time, or the last command ends on this time.
  private boolean timeExists(int time, List<IReadOnlyRotateCommand> list) {
    if (list == null) {
      return false;
    }
    else if (time == list.get(list.size() - 1).getEndTime()) {
      return true;
    }
    for (IReadOnlyRotateCommand command : list) {
      if (time == command.getStartTime()) {
        return true;
      }
    }
    return false;
  }

  /**
   * The action listener method for a shape change event that allows a user to add or remove a
   * shape from an anmiation. If a shape is deleted, delete its associated commands and
   * send an updated list of shapes and keyframes. If any bad fields are sent with the command
   * call a display error method in the view to show to the user that the given operation is not
   * permitted.
   * @param event is the event created by and sent by the view to this controller to allow
   *              for communication between the two.
   */
  @Override
  public void shapeChanged(IShapeChangeEvent event) {
    switch (event.getChangeType()) {
      case ADD:
        if (builder.getShapes().get(event.getId()) != null) {
          view.displayError("Shape already exists with this id");
          return;
        }
        try {
          builder.declareShape(event.getId(), event.getShapeType());
          view.setShapes(builder.getShapes());
          view.setKeyframes(this.convertToKeyFrames(this.builder.getCommands()));
        }
        catch (IllegalArgumentException e) {
          view.displayError(e.getMessage());
        }
        return;
      case DELETE:
        if (builder.getShapes().get(event.getId()) == null) {
          view.displayError("This shape cannot be deleted as the id does not exist");
          return;
        }
        try {
          builder.removeShape(event.getId());
          view.setShapes(builder.getShapes());
          view.setKeyframes(this.convertToKeyFrames(this.builder.getCommands()));
          finalTick = builder.build().getFinalTick();
        }
        catch (IllegalArgumentException e) {
          view.displayError(e.getMessage());
        }
        return;
      default:
    }
  }

  /**
   * Invoked when various buttons are pressed from the view, allowing the user to play, pause,
   * loop, and restart the current animation.
   * @param event is the event created by and sent by the view.
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    switch (event.getActionCommand()) {
      case "play" :
        this.play();
        return;
      case "pause" :
        if (!isPaused) {
          isPaused = true;
        }
        return;
      case "loop" :
        isLooping = !isLooping;
        return;
      case "restart":
        currentTick = firstTick;
        play();
        break;
      default:
        // no valid command received.
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    switch (event.getPropertyName()) {
      case "tick speed":
        int speed;
        try {
          speed = Integer.parseInt(event.getNewValue().toString());
        }
        catch (NumberFormatException e) {
          view.displayError("Not a number.");
          return;
        }
        if (Integer.parseInt(event.getNewValue().toString()) < 1) {
          view.displayError("Cannot pass a negative number.");
          return;
        }

        ticksPerSecond = speed;
        if (!isPaused) {
          this.timer.cancel();
          this.timer = new Timer();
          this.timer.schedule(new DrawFrameTask(this.builder.build()), 0,
                  1000 / this.ticksPerSecond);
        }
        return;
      case "export":
        ITextView svg = new SVGAnimationView(builder.build(), ticksPerSecond);
        svg.play();
        try {
          FileWriter writer = new FileWriter("./inputs/" + event.getNewValue().toString() + ".svg");
          writer.append(svg.getText());
          writer.close();
        }
        catch (IOException e) {
          view.displayError("Error occurred when trying to create a file.");
        }
        return;
      case "slider" :
        currentTick = (int) ((Double.parseDouble(event.getNewValue().toString()) / 100.0)
                * (double) (finalTick - this.firstTick)) + firstTick;
        if (!isPaused) {
          isPaused = true;
        }
        view.display(builder.build().getState(currentTick));
        return;
      default:
    }
  }


  @Override
  public void start() {
    this.view.setKeyframes(this.convertToKeyFrames(this.builder.getCommands()));
    this.view.setShapes(this.builder.getShapes());
    this.view.addButtonListener(this);
    this.view.addFrameChangeListener(this);
    this.view.addShapeChangeListener(this);
    this.view.addPropertyListener(this);
    this.view.setWidth((int) this.builder.getNeededSpace().getWidth());
    this.view.setHeight((int) this.builder.getNeededSpace().getHeight());
    this.view.play();
    this.view.display(this.builder.build().getState(this.firstTick));
  }

  /**
   * A {@link TimerTask} that updates this visual view at each tick of the animation.
   */
  private class DrawFrameTask extends TimerTask {
    private final int finalTick;
    private final IAnimationModel model;

    // takes a final tick at which to stop the animation.
    private DrawFrameTask(IAnimationModel model) {
      super();
      this.finalTick = model.getFinalTick();
      this.model = model;
    }

    /**
     * The action to be performed by this timer task, changing the states of each shape based
     * on its key frames and calls the display method on the view. It also checks if the isLooping
     * field and the isPaused field are true and either loop the animation or just end it after
     * the final tick.
     */
    @Override
    public void run() {
      if (isPaused) {
        timer.cancel();
      }
      else if (currentTick >= this.finalTick) {
        if (isLooping) {
          currentTick = firstTick;
        }
        else {
          timer.cancel();
          isPaused = true;
        }
      }
      List<IReadOnlyShape> shapesAtTick = this.model.getState(currentTick);
      view.display(shapesAtTick);
      view.setSlider((double) currentTick / (double) (finalTick - firstTick));
      currentTick++;
    }
  }

  // Converts a list of commands into a list of keyframes.
  private Map<String, List<IRotateKeyframe>> convertToKeyFrames(Map<String,
          List<IReadOnlyRotateCommand>> map) {
    Map<String, List<IRotateKeyframe>> output = new LinkedHashMap<>();

    for (Map.Entry<String, List<IReadOnlyRotateCommand>> entry : map.entrySet()) {
      ArrayList<IRotateKeyframe> temp = new ArrayList<>();
      if (map.get(entry.getKey()) == null)  {
        output.put(entry.getKey(), new ArrayList<>());
        continue;
      }

      for (int i = 0; i < entry.getValue().size(); i++) {
        temp.add(this.convertToKeyFrame(entry.getValue().get(i)));
      }
      IReadOnlyRotateCommand last = entry.getValue().get(entry.getValue().size() - 1);

      if (last.getStartTime() != last.getEndTime()) {
        temp.add(new RotateKeyframe(last.getEndTime(), last.getEndX(), last.getEndY(),
                last.getEndWidth(), last.getEndHeight(), last.getEndOrientation(),
                last.getEndColor()));
      }
      output.put(entry.getKey(), temp);
    }
    return output;
  }

  // converts a command to keyframes using its starting values.
  private IRotateKeyframe convertToKeyFrame(IReadOnlyRotateCommand command) {
    return new RotateKeyframe(command.getStartTime(), command.getStartX(), command.getStartY(),
            command.getStartWidth(), command.getStartHeight(), command.getStartOrientation(),
            command.getStartColor());
  }

  // Gets the first tick of the animation and starts it at that. If there
  // are no commands then set tick to 0;
  private int getFirstTick() {
    Map<String, List<IReadOnlyRotateCommand>> commands = this.builder.getCommands();
    int firstTick = Integer.MAX_VALUE;

    for (String id : commands.keySet()) {
      if (!commands.get(id).isEmpty()) {
        firstTick = Math.min(firstTick, commands.get(id).get(0).getStartTime());
      }
    }
    if (firstTick == Integer.MAX_VALUE) {
      return 0;
    }
    return firstTick;
  }
}