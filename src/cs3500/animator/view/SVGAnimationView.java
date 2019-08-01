package cs3500.animator.view;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cs3500.animator.model.IAnimationModel;
import cs3500.animator.model.IReadOnlyCommand;
import cs3500.animator.model.IReadOnlyRotateCommand;
import cs3500.animator.model.IReadOnlyShape;
import cs3500.animator.model.IRotateCommand;

/**
 * Represents a view for the animation that writes XML to create a .svg file to view the animation.
 */
public class SVGAnimationView implements ITextView {
  Appendable out;
  IAnimationModel model;
  private final int multiplier;
  private final int x;
  private final int y;


  /**
   * Constructor for SVGAnimation view that takes in the model for the animation and the tickspeed.
   * @param model model used for the animation.
   * @param ticksPerSecond is the tickspeed of the animation.
   */
  public SVGAnimationView(IAnimationModel model, int ticksPerSecond) {
    if (ticksPerSecond < 1) {
      throw new IllegalArgumentException("Cannot pass a negative ticks per second");
    }
    this.model = model;
    out = new StringBuilder();
    this.multiplier = 1000 / ticksPerSecond;
    this.x = model.getX();
    this.y = model.getY();
  }

  /**
   * Formats the given information as the XML language that converts the list of commands
   * and shapes into XML to be turned into an .svg file.
   */
  @Override
  public void play() {
    String type;
    tryAppend("<svg width=\"" + model.getWidth() + "\" height=\""
            + model.getHeight() + "\" version=\"1.1\"\n    "
            + "xmlns=\"http://www.w3.org/2000/svg\">\n");

    for (Map.Entry<String, IReadOnlyShape> entry : model.getShapes().entrySet()) {
      List<IReadOnlyRotateCommand> commands = model.getCommands().get(entry.getKey());
      switch (entry.getValue().getShapeType()) {
        case "Rectangle" :
          type = "rect";
          break;
        case "Ellipse" :
          type = "ellipse";
          break;
        default:
          type = "";
      }
      this.tryAppend("<" + type + " id=\""
              + entry.getKey());
      this.tryAppend(this.getStartState(commands.get(0), type));
      this.tryAppend(this.convertCommandsToString(commands, type));
      this.tryAppend("</" + type + ">\n\n");
    }
    tryAppend("</svg>");
  }

  // the try catch for the append.
  private void tryAppend(String input) {
    try {
      out.append(input);
    } catch (IOException e) {
      throw new IllegalStateException("Bad Appendable");
    }
  }

  //converts each of the commands into the correct format for .svg
  private String convertCommandsToString(List<IReadOnlyRotateCommand> list, String type) {
    StringBuilder output = new StringBuilder();
    String startString;
    String endString = "\" fill=\"freeze\" />\n";


    for (IReadOnlyRotateCommand command : list) {
      startString = "    <animate attributeType=\"xml\" begin=\""
              + command.getStartTime() * multiplier + "ms\" dur=\""
              + (command.getEndTime() - command.getStartTime()) * multiplier + "ms\" "
              + "attributeName=\"";

      if (command.getStartOrientation() != command.getEndOrientation()) {
        if (type.equals("rect")) {
          output.append("<animateTransform attributeName=\"transform\" attributeType=\"xml\""
          + " type=\"rotate\" from=\"" + command.getStartOrientation() + " "
                  + (command.getStartX() + command.getStartWidth() / 2 - this.x) + " "
                  + (command.getStartY() + command.getStartHeight() / 2 - this.y)
                  + "\" to=\"" + command.getEndOrientation() + " "
                  + (command.getStartX() + command.getStartWidth() / 2 - this.x) + " "
                  + (command.getStartY() + command.getStartHeight() / 2 - this.y) + "\" dur=\""
                  + ((command.getEndTime() - command.getStartTime()) * multiplier) + "ms\" "
                  + "repeatCount=\"0\"/>\n");
        }
        else if (type.equals("ellipse")) {
          output.append("<animateTransform attributeName=\"transform\" attributeType=\"xml\""
                  + " type=\"rotate\" from=\"" + command.getStartOrientation() + " "
                  + (command.getStartX() - this.x + command.getStartWidth() / 2) + " "
                  + (command.getStartY() - this.y + command.getStartHeight() / 2)
                  + "\" to=\"" + command.getEndOrientation() + " "
                  + (command.getStartX() - this.x + command.getStartWidth() / 2) + " "
                  + (command.getStartY() - this.y + command.getStartHeight() / 2)
                  + "\" dur=\"" + ((command.getEndTime() - command.getStartTime()) * multiplier)
                  + "ms\" repeatCount=\"0\"/>\n");
        }
        continue;
      }
      if (command.getStartX() != command.getEndX()) {
        if (type.equals("rect")) {
          output.append(startString).append("x\" from=\"").append(command.getStartX() - this.x)
                  .append("\" to=\"").append(command.getEndX() - this.x).append(endString);
        }
        else {
          output.append(startString).append("cx\" from=\"")
                  .append(command.getStartX() - this.x + command.getStartWidth() / 2)
                  .append("\" to=\"")
                  .append(command.getEndX() - this.x + command.getEndWidth() / 2)
                  .append(endString);
        }
      }
      if (command.getStartY() != command.getEndY()) {
        if (type.equals("rect")) {
          output.append(startString).append("y\" from=\"")
                  .append(command.getStartY() - this.y)
                  .append("\" to=\"")
                  .append(command.getEndY() - this.y)
                  .append(endString);
        }
        else {
          output.append(startString).append("cy\" from=\"")
                  .append(command.getStartY() - this.y + command.getStartHeight() / 2)
                  .append("\" to=\"")
                  .append(command.getEndY() - this.y + command.getEndHeight() / 2)
                  .append(endString);
        }
      }
      if (command.getStartHeight() != command.getEndHeight()) {
        if (type.equals("rect")) {
          output.append(startString).append("height\" from=\"").append(command.getStartHeight())
                  .append("\" to=\"").append(command.getEndHeight()).append(endString);
        }
        else {
          output.append(startString).append("ry\" from=\"").append(command.getStartHeight() / 2)
                  .append("\" to=\"").append(command.getEndHeight() / 2).append(endString);
        }
      }
      if (command.getStartWidth() != command.getEndWidth()) {
        if (type.equals("rect")) {
          output.append(startString).append("width\" from=\"").append(command.getStartWidth())
                  .append("\" to=\"").append(command.getEndWidth()).append(endString);
        }
        else {
          output.append(startString).append("ry\" from=\"").append(command.getStartWidth() / 2)
                  .append("\" to=\"").append(command.getEndWidth() / 2).append(endString);
        }
      }
      if (command.getStartColor().getRed() != command.getEndColor().getRed()
              || command.getStartColor().getGreen() != command.getEndColor().getGreen()
              || command.getStartColor().getBlue() != command.getEndColor().getBlue()) {
        output.append(startString).append("fill\" from=\"rgb(")
                .append(command.getStartColor().getRed()).append(",")
                .append(command.getStartColor().getGreen()).append(",")
                .append(command.getStartColor().getBlue()).append(")\" to=\"rgb(")
                .append(command.getEndColor().getRed()).append(",")
                .append(command.getEndColor().getGreen()).append(",")
                .append(command.getEndColor().getBlue()).append(")").append(endString);
      }
    }
    return output.toString();
  }

  //creates the string that initializes the shape in the svg format.
  private String getStartState(IReadOnlyCommand command, String type) {
    StringBuilder output = new StringBuilder();
    switch (type) {
      case "rect":
        output = new StringBuilder("\" x=\"").append(command.getStartX() - this.x)
                .append("\" y=\"").append(command.getStartY() - this.y).append("\" width=\"")
                .append(command.getStartWidth()).append("\" height=\"")
                .append(command.getStartHeight())
                .append("\" fill=\"rgb(").append(command.getStartColor().getRed()).append(",")
                .append(command.getStartColor().getGreen()).append(",")
                .append(command.getStartColor().getBlue())
                .append(")\" visibility=\"visible\" >\n");
        break;
      case "ellipse":
        output = new StringBuilder("\" cx=\"")
                .append(command.getStartX() - this.x + command.getStartWidth() / 2)
                .append("\" cy=\"")
                .append(command.getStartY() - this.y + command.getStartHeight() / 2)
                .append("\" rx=\"")
                .append(command.getStartWidth() / 2).append("\" ry=\"")
                .append(command.getStartHeight() / 2)
                .append("\" fill=\"rgb(").append(command.getStartColor().getRed()).append(",")
                .append(command.getStartColor().getGreen()).append(",")
                .append(command.getStartColor().getBlue())
                .append(")\" visibility=\"visible\" >\n");
        break;
      default:
        return output.toString();
    }
    return output.toString();
  }

  @Override
  public String getText() {
    return this.out.toString();
  }

}
