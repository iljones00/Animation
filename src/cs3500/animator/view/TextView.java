package cs3500.animator.view;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import cs3500.animator.model.IAnimationModel;
import cs3500.animator.model.IReadOnlyCommand;
import cs3500.animator.model.IReadOnlyRotateCommand;
import cs3500.animator.model.IReadOnlyShape;

/**
 * Represents a text description of the given animation.
 */
public class TextView implements ITextView {
  private Appendable out;
  private IAnimationModel model;

  /**
   * Constructor for TextView that takes the model to convert into a string.
   *
   * @param model is the model for which the TextView will create a constructor for.
   */
  public TextView(IAnimationModel model) {
    this.model = model;
    out = new StringWriter();
  }

  @Override
  public String getText() {
    return this.out.toString();
  }

  @Override
  public void play() {

    StringBuilder output = new StringBuilder();
    for (Map.Entry<String,
            List<IReadOnlyRotateCommand>> entry : this.model.getCommands().entrySet()) {
      IReadOnlyShape shape = this.model.getShapes().get(entry.getKey());
      output.append("Shape ").append(entry.getKey()).append(" ").append(shape.getShapeType())
              .append("\n");
      for (IReadOnlyRotateCommand command : entry.getValue()) {
        StringBuilder temp = new StringBuilder(command.getType()).append(" ");
        temp.append(entry.getKey()).append(" ")
                .append(command.getStartTime()).append(" ")
                .append(command.getStartX()).append(" ")
                .append(command.getStartY()).append(" ")
                .append(command.getStartWidth()).append(" ")
                .append(command.getStartHeight()).append(" ")
                .append(command.getStartOrientation()).append(" ")
                .append(command.getStartColor().getRed()).append(" ")
                .append(command.getStartColor().getGreen()).append(" ")
                .append(command.getStartColor().getBlue()).append("    ")
                .append(command.getEndTime()).append(" ")
                .append(command.getEndX()).append(" ")
                .append(command.getEndY()).append(" ")
                .append(command.getEndWidth()).append(" ")
                .append(command.getEndHeight()).append(" ")
                .append(command.getEndOrientation()).append(" ")
                .append(command.getEndColor().getRed()).append(" ")
                .append(command.getEndColor().getGreen()).append(" ")
                .append(command.getEndColor().getBlue()).append("\n");
        output.append(temp);
      }
      output.append("\n");
    }


    if (output.length() != 0) {
      output.delete(output.length() - 2, output.length());
    }
    out = output;
  }
}
