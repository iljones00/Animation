package cs3500.animator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import javax.swing.JOptionPane;

import cs3500.animator.controller.Controller;
import cs3500.animator.controller.IController;
import cs3500.animator.model.AnimationBuilder;
import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationReader;
import cs3500.animator.model.IAnimationModel;
import cs3500.animator.model.IEditBuilder;
import cs3500.animator.view.EditingView;
import cs3500.animator.view.GUIAnimationView;
import cs3500.animator.view.IEditableView;
import cs3500.animator.view.ITextView;
import cs3500.animator.view.IView;
import cs3500.animator.view.SVGAnimationView;
import cs3500.animator.view.TextView;

/**
 * Represents the main class that creates and runs an animation based on the given file.
 */
public final class Excellence {

  /**
   * The main function that initializes the model and the view based on the given arguments and
   * runs an animation.
   * @param args the input that allows the user to specify what kind of animation they desire.
   */
  public static void main(String [] args) {
    AnimationBuilder builder = new AnimationModel.AnimationModelBuilder();
    IEditBuilder editBuilder = new AnimationModel.AnimationModelBuilder();
    IAnimationModel model;
    ITextView textView;
    IEditableView editView;
    IController controller;
    Readable in = new StringReader("");
    int ticksPerSecond = 1;
    String viewType = "";
    Appendable out = System.out;
    FileWriter writer = null;

    if (!(Arrays.stream(args).anyMatch("-in"::equals)
            && (Arrays.stream(args).anyMatch("-view"::equals)))) {
      popUpError("Does not have the necessary fields to run the program");
    }

    for (int i = 0; i < args.length; i++) {

      if (args[i].equals("-in")) {
        try {
          in = new FileReader(args[i + 1]);
        }
        catch (FileNotFoundException e) {
          popUpError("This file does not exist");
        }
        catch (IndexOutOfBoundsException e) {
          popUpError("There is no file specified");
        }
      }

      if (args[i].equals("-view")) {
        try {
          viewType = args[i + 1];
        }
        catch (IndexOutOfBoundsException e) {
          popUpError("There is no view specified");
        }
      }

      if (args[i].equals("-out")) {
        try {
          writer = new FileWriter(args[i + 1]);
        }
        catch (IOException e) {
          popUpError("File couldn't be found or created");
        }
        catch (IndexOutOfBoundsException e) {
          popUpError("There was no file specified");
        }
      }

      if (args[i].equals("-speed")) {
        try {
          int newSpeed = Integer.parseInt(args[i + 1]);
          if (newSpeed > 0) {
            ticksPerSecond = newSpeed;
          }
          else {
            popUpError("Cannot pass a non-positive speed");
          }
        }
        catch (NumberFormatException e) {
          popUpError("There was no valid speed specified");
        }
        catch (IndexOutOfBoundsException e) {
          popUpError("There was no speed specified");
        }
      }
    }

    if (viewType.equals("edit")) {
      AnimationReader.parseFile(in, editBuilder);
      editView = new EditingView(ticksPerSecond);
      controller = new Controller(editBuilder, editView, ticksPerSecond);
      controller.start();
      return;
    }

    model = AnimationReader.parseFile(in, builder);

    switch (viewType) {
      //“text”, “svg”, or “visual”
      case "text":
        textView = new TextView(model);
        break;
      case "svg":
        textView = new SVGAnimationView(model, ticksPerSecond);
        break;
      case "visual":
        IView view = new GUIAnimationView(model, ticksPerSecond);
        view.play();
        return;
      default:
        popUpError("no valid view type given");
        return;
    }

    textView.play();

    try {
      if (writer != null) {
        writer.append(textView.getText());
        writer.close();
      }
      else {
        out.append(textView.getText());
      }
    }
    catch (IOException e) {
      popUpError("There was an issue writing the output");
    }
  }

  private static void popUpError(String message) {
    JOptionPane.showMessageDialog(null, message, "Error running the Animation", 0);
    System.exit(1);
  }
}