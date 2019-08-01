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
import cs3500.animator.view.ITextView;
import cs3500.animator.view.SVGAnimationView;

import static org.junit.Assert.assertEquals;

/**
 * Represents the tester class for the SVGAnimationView class.
 */
public class SVGViewTests {
  private AnimationModel.AnimationModelBuilder builder;
  private IAnimationModel model;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  @Before
  public void setUp() {
    this.builder = new AnimationModel.AnimationModelBuilder();
    System.setOut(new PrintStream(outContent));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testBadConstructor1() {
    model = builder.build();
    SVGAnimationView view = new SVGAnimationView(model, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testBadConstructor2() {
    model = builder.build();
    SVGAnimationView view = new SVGAnimationView(model, -1);
  }

  @Test
  public void testGetAppendable1() {
    this.model = this.builder.build();
    ITextView view = new SVGAnimationView(model, 1);
    assertEquals(view.getText(), "");
    view.play();
    assertEquals(view.getText(), "<svg width=\"1000\" height=\"600\""
            + " version=\"1.1\"\n"
            + "    xmlns=\"http://www.w3.org/2000/svg\">\n</svg>");
  }

  @Test
  public void testGetAppendable2() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20,
                    10, 10, 30, 0, 255, 0)
            .addMotion("Dave", 5, 20, 10, 10, 30, 0, 255, 0, 10, 20,
                    20, 10, 30, 0, 255, 0)
            .declareShape("Steve", "Ellipse")
            .addMotion("Steve", 0, 1, 1, 20, 20, 255, 0, 0, 10, 20,
                    20, 20, 20, 255, 0, 0);
    this.model = this.builder.build();
    ITextView view = new SVGAnimationView(model, 1);
    view.play();
    assertEquals(view.getText(), "<svg width=\"1000\" height=\"600\""
            + " version=\"1.1\"\n    xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<rect id=\"Dave\" x=\"10\" y=\"10\" width=\"10\" height=\"30\""
            + " fill=\"rgb(0,255,0)\" visibility=\"visible\" >\n"
            + "    <animate attributeType=\"xml\" begin=\"0ms\" dur=\"5000ms\" attributeName=\"x\""
            + " from=\"10\" to=\"20\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"5000ms\" dur=\"5000ms\" attributeName=\""
            + "y\" from=\"10\" to=\"20\" fill=\"freeze\" />\n</rect>\n\n"
            + "<ellipse id=\"Steve\" cx=\"11\" cy=\"11\" rx=\"10\" ry=\"10\""
            + " fill=\"rgb(255,0,0)\" visibility=\"visible\" >\n"
            + "    <animate attributeType=\"xml\" begin=\"0ms\" dur=\"10000ms\""
            + " attributeName=\"cx\" from=\"11\" to=\"30\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"0ms\" dur=\"10000ms\""
            + " attributeName=\"cy\" from=\"11\" to=\"30\" fill=\"freeze\" />\n"
            + "</ellipse>\n\n</svg>");
  }

  @Test
  public void testPlay1() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20,
                    10, 10, 30, 0, 255, 0)
            .addMotion("Dave", 5, 20, 10, 10, 30, 0, 255, 0, 10, 20,
                    20, 10, 30, 0, 255, 0)
            .declareShape("Steve", "Ellipse")
            .addMotion("Steve", 0, 1, 1, 20, 20, 255, 0, 0, 10, 20,
                    20, 20, 20, 255, 0, 0);
    this.model = this.builder.build();
    ITextView view = new SVGAnimationView(model, 1);
    view.play();
    assertEquals(view.getText(), "<svg width=\"1000\" height=\"600\""
            + " version=\"1.1\"\n    xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<rect id=\"Dave\" x=\"10\" y=\"10\" width=\"10\" height=\"30\""
            + " fill=\"rgb(0,255,0)\" visibility=\"visible\" >\n"
            + "    <animate attributeType=\"xml\" begin=\"0ms\" dur=\"5000ms\" attributeName=\"x\""
            + " from=\"10\" to=\"20\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"5000ms\" dur=\"5000ms\" attributeName=\""
            + "y\" from=\"10\" to=\"20\" fill=\"freeze\" />\n</rect>\n\n"
            + "<ellipse id=\"Steve\" cx=\"11\" cy=\"11\" rx=\"10\" ry=\"10\""
            + " fill=\"rgb(255,0,0)\" visibility=\"visible\" >\n"
            + "    <animate attributeType=\"xml\" begin=\"0ms\" dur=\"10000ms\""
            + " attributeName=\"cx\" from=\"11\" to=\"30\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"0ms\" dur=\"10000ms\""
            + " attributeName=\"cy\" from=\"11\" to=\"30\" fill=\"freeze\" />\n"
            + "</ellipse>\n\n</svg>");
  }

  @Test
  public void testPlay2() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20,
                    10, 10, 30, 0, 255, 0)
            .addMotion("Dave", 5, 20, 10, 10, 30, 0, 255, 0, 8, 20,
                    10, 10, 30, 0, 0, 255);
    this.model = this.builder.build();
    ITextView view = new SVGAnimationView(model, 5);
    view.play();
    assertEquals(view.getText(), "<svg width=\"1000\" height=\"600\""
            + " version=\"1.1\"\n    xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<rect id=\"Dave\" x=\"10\" y=\"10\" width=\"10\" height=\"30\""
            + " fill=\"rgb(0,255,0)\" visibility=\"visible\" >\n"
            + "    <animate attributeType=\"xml\" begin=\"0ms\" dur=\"1000ms\""
            + " attributeName=\"x\" from=\"10\" to=\"20\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"1000ms\" dur=\"600ms\" attributeName=\""
            + "fill\" from=\"rgb(0,255,0)\" to=\"rgb(0,0,255)\" fill=\"freeze\" />\n"
            + "</rect>\n\n</svg>");
  }

  @Test
  public void testMain1() {
    Excellence.main(new String[]{"-view", "svg", "-in", "./inputs/smalldemo.txt"});
    assertEquals("<svg width=\"360\" height=\"360\" version=\"1.1\"\n"
            + "    xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<rect id=\"R\" x=\"0\" y=\"130\" width=\"50\" height=\"100\" fill=\"rgb(255,0,0)\""
            + " visibility=\"visible\" >\n"
            + "    <animate attributeType=\"xml\" begin=\"10000ms\" dur=\"40000ms\""
            + " attributeName=\"x\" from=\"0\" to=\"100\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"10000ms\" dur=\"40000ms\" "
            + "attributeName=\"y\" from=\"130\" to=\"230\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"51000ms\" dur=\"19000ms\" "
            + "attributeName=\"width\" from=\"50\" to=\"25\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"70000ms\" dur=\"30000ms\" "
            + "attributeName=\"x\" from=\"100\" to=\"0\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"70000ms\" dur=\"30000ms\" "
            + "attributeName=\"y\" from=\"230\" to=\"130\" fill=\"freeze\" />\n"
            + "</rect>\n\n"
            + "<ellipse id=\"C\" cx=\"300\" cy=\"30\" rx=\"60\" ry=\"30\" "
            + "fill=\"rgb(0,0,255)\" visibility=\"visible\" >\n"
            + "    <animate attributeType=\"xml\" begin=\"20000ms\" dur=\"30000ms\" "
            + "attributeName=\"cy\" from=\"30\" to=\"210\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"50000ms\" dur=\"20000ms\" "
            + "attributeName=\"cy\" from=\"210\" to=\"330\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"50000ms\" dur=\"20000ms\" "
            + "attributeName=\"fill\" from=\"rgb(0,0,255)\" to=\"rgb(0,170,85)\" "
            + "fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"70000ms\" dur=\"10000ms\" "
            + "attributeName=\"fill\" from=\"rgb(0,170,85)\" to=\"rgb(0,255,0)\" fill=\"freeze\" "
            + "/>\n</ellipse>\n\n</svg>", outContent.toString());
  }

  @Test
  public void testMain2() {
    Excellence.main(new String[]{"-view", "svg", "-in", "./inputs/smalldemo.txt", "-out",
        "./inputs/code.svg"});
    List<String> lines;
    try {
      lines = Files.readAllLines(Paths.get("inputs/code.svg"));
    } catch (IOException e) {
      throw new IllegalArgumentException("Bruh");
    }
    StringBuilder output = new StringBuilder();
    for (String line : lines) {
      output.append(line);
      output.append("\n");
    }

    assertEquals("<svg width=\"360\" height=\"360\" version=\"1.1\"\n"
            + "    xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<rect id=\"R\" x=\"0\" y=\"130\" width=\"50\" height=\"100\" fill=\"rgb(255,0,0)\""
            + " visibility=\"visible\" >\n"
            + "    <animate attributeType=\"xml\" begin=\"10000ms\" dur=\"40000ms\""
            + " attributeName=\"x\" from=\"0\" to=\"100\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"10000ms\" dur=\"40000ms\" "
            + "attributeName=\"y\" from=\"130\" to=\"230\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"51000ms\" dur=\"19000ms\" "
            + "attributeName=\"width\" from=\"50\" to=\"25\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"70000ms\" dur=\"30000ms\" "
            + "attributeName=\"x\" from=\"100\" to=\"0\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"70000ms\" dur=\"30000ms\" "
            + "attributeName=\"y\" from=\"230\" to=\"130\" fill=\"freeze\" />\n"
            + "</rect>\n\n"
            + "<ellipse id=\"C\" cx=\"300\" cy=\"30\" rx=\"60\" ry=\"30\" "
            + "fill=\"rgb(0,0,255)\" visibility=\"visible\" >\n"
            + "    <animate attributeType=\"xml\" begin=\"20000ms\" dur=\"30000ms\" "
            + "attributeName=\"cy\" from=\"30\" to=\"210\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"50000ms\" dur=\"20000ms\" "
            + "attributeName=\"cy\" from=\"210\" to=\"330\" fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"50000ms\" dur=\"20000ms\" "
            + "attributeName=\"fill\" from=\"rgb(0,0,255)\" to=\"rgb(0,170,85)\" "
            + "fill=\"freeze\" />\n"
            + "    <animate attributeType=\"xml\" begin=\"70000ms\" dur=\"10000ms\" "
            + "attributeName=\"fill\" from=\"rgb(0,170,85)\" to=\"rgb(0,255,0)\" fill=\"freeze\" "
            + "/>\n</ellipse>\n\n</svg>",
            output.toString().trim());
  }
}

