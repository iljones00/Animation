package cs3500.animator.view;

import java.util.Timer;
import java.util.List;
import java.util.TimerTask;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import cs3500.animator.model.IAnimationModel;
import cs3500.animator.model.IReadOnlyCommand;
import cs3500.animator.model.IReadOnlyRotateCommand;
import cs3500.animator.model.IReadOnlyShape;

/**
 * Represents a visual view that displays the animation as a swing animation.
 */
public class GUIAnimationView extends JFrame implements IView {
  private IAnimationModel model;
  private Timer timer;
  private ADrawingPanel panel;
  private final int tempo;

  /**
   * Constructor for GUIAnimation view that initializes the fields, creates a panel on which the
   * animation will be drawn, and adds the scroll bars.
   *
   * @param model the incoming model used for the animation.
   * @param ticksPerSecond is the tickRate of the animation that can be adjusted.
   */
  public GUIAnimationView(IAnimationModel model, int ticksPerSecond) {
    super();

    if (model == null) {
      throw new IllegalArgumentException("Model must not be null.");
    }
    if (ticksPerSecond <= 0) {
      throw new IllegalArgumentException("Ticks per second must be positive.");
    }

    this.model = model;
    this.tempo = ticksPerSecond;

    this.panel = new DrawingPanel();
    this.panel.setPreferredSize(this.getNeededSpace());

    JScrollPane scrollPane = new JScrollPane(this.panel);

    this.setSize(this.model.getWidth(), this.model.getHeight());

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.add(scrollPane);

    this.timer = new Timer();
  }

  @Override
  public void play() {
    this.setVisible(true);
    this.timer.schedule(new DrawFrameTask(this.model.getFinalTick()), 0, 1000 / this.tempo);
  }

  // returns a dimension exactly big enough so that no shape in the animation goes outside of it.
  private Dimension getNeededSpace() {
    int outputWidth = 0;
    int outputHeight = 0;
    for (List<IReadOnlyRotateCommand> commandList : this.model.getCommands().values()) {
      outputWidth = Math.max(outputWidth,
              commandList.get(0).getStartX() + commandList.get(0).getStartWidth());
      outputHeight = Math.max(outputHeight,
              commandList.get(0).getStartY() + commandList.get(0).getStartHeight());

      for (IReadOnlyCommand command : commandList) {
        outputWidth = Math.max(outputWidth, command.getEndX() + command.getEndWidth());
        outputHeight = Math.max(outputHeight, command.getEndY() + command.getEndHeight());
      }
    }
    return new Dimension(outputWidth, outputHeight);
  }

  /**
   * A {@link TimerTask} that updates this visual view at each tick of the animation.
   */
  private class DrawFrameTask extends TimerTask {
    private int tick = 0;
    private final int finalTick;

    // takes a final tick at which to stop the animation.
    private DrawFrameTask(int finalTick) {
      super();
      this.finalTick = finalTick;
    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
      if (this.tick >= this.finalTick) {
        timer.cancel();
      }
      List<IReadOnlyShape> shapesAtTick = model.getState(this.tick);
      panel.draw(shapesAtTick);
      this.tick ++;
    }
  }
}