import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import cs3500.animator.Excellence;
import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.IAnimationModel;
import cs3500.animator.view.TextView;

import static org.junit.Assert.assertEquals;


/**
 * Represents the tester class for the Text view public methods.
 */
public class TextViewTests {
  private AnimationModel.AnimationModelBuilder builder;
  private IAnimationModel model;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  @Before
  public void setUp() {
    this.builder = new AnimationModel.AnimationModelBuilder();
    System.setOut(new PrintStream(outContent));
  }

  @Test
  public void testGetText1() {
    this.model = this.builder.build();
    TextView view = new TextView(model);
    assertEquals(view.getText(), "");
  }

  @Test
  public void testGetText2() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20,
                    10, 10, 30, 0, 255, 0)
            .addMotion("Dave", 5, 20, 10, 10, 30, 0, 255, 0, 10, 20,
                    20, 10, 30, 0, 255, 0)
            .declareShape("Steve", "Ellipse")
            .addMotion("Steve", 0, 1, 1, 20, 20, 255, 0, 0, 10, 20,
                    20, 20, 20, 255, 0, 0);
    this.model = this.builder.build();
    TextView view = new TextView(model);
    assertEquals(view.getText(), "");
    view.play();
    assertEquals(view.getText(), "Shape Dave Rectangle\n"
            + "Motion Dave 0 10 10 10 30 0 0 255 0    5 20 10 10 30 0 0 255 0\n"
            + "Motion Dave 5 20 10 10 30 0 0 255 0    10 20 20 10 30 0 0 255 0\n"
            + "\n"
            + "Shape Steve Ellipse\n"
            + "Motion Steve 0 1 1 20 20 0 255 0 0    10 20 20 20 20 0 255 0 0");
  }

  @Test
  public void testGetPlay1() {
    this.model = this.builder.build();
    TextView view = new TextView(model);
    view.play();
    assertEquals(view.getText(), "");
  }

  @Test
  public void testPlay2() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 90, 0, 255, 0, 5, 20,
                    10, 10, 30, 180, 0, 255, 0)
            .addMotion("Dave", 5, 20, 10, 10, 30, 180, 0, 255, 0, 10, 20,
                    20, 10, 30, 0, 0, 255, 0)
            .declareShape("Steve", "Ellipse")
            .addMotion("Steve", 0, 1, 1, 20, 20, 255, 0, 0, 10, 20,
                    20, 20, 20, 255, 0, 0);
    this.model = this.builder.build();
    TextView view = new TextView(model);
    view.play();
    assertEquals(view.getText(), "Shape Dave Rectangle\n"
            + "Motion and Rotate Dave 0 10 10 10 30 90 0 255 0    5 20 10 10 30 180 0 255 0\n"
            + "Motion and Rotate Dave 5 20 10 10 30 180 0 255 0    10 20 20 10 30 0 0 255 0\n"
            + "\n"
            + "Shape Steve Ellipse\n"
            + "Motion Steve 0 1 1 20 20 0 255 0 0    10 20 20 20 20 0 255 0 0");
  }

  @Test
  public void testPlay3() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20,
                    10, 10, 30, 0, 255, 0)
            .addMotion("Dave", 5, 20, 10, 10, 30, 0, 255, 0, 8, 20,
                    10, 10, 30, 0, 0, 255);
    this.model = this.builder.build();
    TextView view = new TextView(model);
    view.play();
    assertEquals("Shape Dave Rectangle\n"
                    + "Motion Dave 0 10 10 10 30 0 0 255 0    5 20 10 10 30 0 0 255 0\n"
                    + "Color Dave 5 20 10 10 30 0 0 255 0    8 20 10 10 30 0 0 0 255",
            view.getText());
  }

  @Test
  public void testMain1() {
    Excellence.main(new String[]{"-view", "text", "-in", "./inputs/smalldemo.txt"});
    assertEquals(outContent.toString(), "Shape R Rectangle\n"
            + "Nothing R 1 200 200 50 100 0 255 0 0    10 200 200 50 100 0 255 0 0\n"
            + "Motion R 10 200 200 50 100 0 255 0 0    50 300 300 50 100 0 255 0 0\n"
            + "Nothing R 50 300 300 50 100 0 255 0 0    51 300 300 50 100 0 255 0 0\n"
            + "Size R 51 300 300 50 100 0 255 0 0    70 300 300 25 100 0 255 0 0\n"
            + "Motion R 70 300 300 25 100 0 255 0 0    100 200 200 25 100 0 255 0 0\n"
            + "\n"
            + "Shape C Ellipse\n"
            + "Nothing C 6 440 70 120 60 0 0 0 255    20 440 70 120 60 0 0 0 255\n"
            + "Motion C 20 440 70 120 60 0 0 0 255    50 440 250 120 60 0 0 0 255\n"
            + "Motion and Color C 50 440 250 120 60 0 0 0 255    70 440 370 120 60 0 0 170 85\n"
            + "Color C 70 440 370 120 60 0 0 170 85    80 440 370 120 60 0 0 255 0\n"
            + "Nothing C 80 440 370 120 60 0 0 255 0    100 440 370 120 60 0 0 255 0");
  }

  @Test
  public void testMain2() {
    Excellence.main(new String[]{"-view", "text", "-in", "./inputs/smalldemo.txt", "-out",
        "./inputs/code.txt"});
    List<String> lines;
    try {
      lines = Files.readAllLines(Paths.get("inputs/code.txt"));
    } catch (IOException e) {
      throw new IllegalArgumentException("smh my head");
    }
    StringBuilder output = new StringBuilder();
    for (String line : lines) {
      output.append(line);
      output.append("\n");
    }

    assertEquals("Shape R Rectangle\n"
            + "Nothing R 1 200 200 50 100 0 255 0 0    10 200 200 50 100 0 255 0 0\n"
            + "Motion R 10 200 200 50 100 0 255 0 0    50 300 300 50 100 0 255 0 0\n"
            + "Nothing R 50 300 300 50 100 0 255 0 0    51 300 300 50 100 0 255 0 0\n"
            + "Size R 51 300 300 50 100 0 255 0 0    70 300 300 25 100 0 255 0 0\n"
            + "Motion R 70 300 300 25 100 0 255 0 0    100 200 200 25 100 0 255 0 0\n"
            + "\n"
            + "Shape C Ellipse\n"
            + "Nothing C 6 440 70 120 60 0 0 0 255    20 440 70 120 60 0 0 0 255\n"
            + "Motion C 20 440 70 120 60 0 0 0 255    50 440 250 120 60 0 0 0 255\n"
            + "Motion and Color C 50 440 250 120 60 0 0 0 255    70 440 370 120 60 0 0 170 85\n"
            + "Color C 70 440 370 120 60 0 0 170 85    80 440 370 120 60 0 0 255 0\n"
            + "Nothing C 80 440 370 120 60 0 0 255 0    100 440 370 120 60 0 0 255 0",
            output.toString().trim());
  }
}