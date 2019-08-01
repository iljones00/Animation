import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.Ellipse;
import cs3500.animator.model.IAnimationModel;
import cs3500.animator.model.IReadOnlyCommand;
import cs3500.animator.model.IReadOnlyShape;
import cs3500.animator.model.MasterCommand;
import cs3500.animator.model.Rectangle;

import static org.junit.Assert.assertEquals;

/**
 * Tests for all public methods of {@link AnimationModel}.
 */
public class ModelTests {
  private AnimationModel.AnimationModelBuilder builder;
  private IAnimationModel model;

  @Before
  public void setUp() {
    this.builder = new AnimationModel.AnimationModelBuilder();
  }

  @Test
  public void testBuildAndConstructor() {
    this.builder = new AnimationModel.AnimationModelBuilder();
    this.model = this.builder.build();
    assertEquals("", this.model.toString());
  }

  @Test
  public void testAddCommand() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20,
                    10, 10, 30, 0, 255, 0);
    this.model = this.builder.build();
    assertEquals("Shape Dave Rectangle\n"
                    + "Motion Dave 0 10.0 10.0 10 30 0 255 0    5 20.0 10.0 10 30 0 255 0",
            this.model.toString());
  }

  @Test
  public void testAddTwoCommands() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20,
                    10, 10, 30, 0, 255, 0)
            .addMotion("Dave", 5, 20, 10, 10, 30, 0, 255, 0, 10, 20,
                    20, 10, 30, 0, 255, 0);
    this.model = this.builder.build();
    assertEquals("Shape Dave Rectangle\n"
                    + "Motion Dave 0 10.0 10.0 10 30 0 255 0    5 20.0 10.0 10 30 0 255 0\n"
                    + "Motion Dave 5 20.0 10.0 10 30 0 255 0    10 20.0 20.0 10 30 0 255 0",
            this.model.toString());
  }

  @Test
  public void testAddTwoCommandsOutOfOrder() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 5, 20, 10, 10, 30, 0, 255, 0, 10, 20,
                    20, 10, 30, 0, 255, 0)
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20,
                    10, 10, 30, 0, 255, 0);
    this.model = this.builder.build();
    assertEquals("Shape Dave Rectangle\n"
                    + "Motion Dave 0 10.0 10.0 10 30 0 255 0    5 20.0 10.0 10 30 0 255 0\n"
                    + "Motion Dave 5 20.0 10.0 10 30 0 255 0    10 20.0 20.0 10 30 0 255 0",
            this.model.toString());
  }

  @Test
  public void testAddThreeCommandsTwoShapes() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20,
                    10, 10, 30, 0, 255, 0)
            .addMotion("Dave", 5, 20, 10, 10, 30, 0, 255, 0, 10, 20,
                    20, 10, 30, 0, 255, 0)
            .declareShape("Steve", "Ellipse")
            .addMotion("Steve", 0, 1, 1, 20, 20, 255, 0, 0, 10, 20,
                    20, 20, 20, 255, 0, 0);
    this.model = this.builder.build();
    assertEquals("Shape Dave Rectangle\n"
                    + "Motion Dave 0 10.0 10.0 10 30 0 255 0    5 20.0 10.0 10 30 0 255 0\n"
                    + "Motion Dave 5 20.0 10.0 10 30 0 255 0    10 20.0 20.0 10 30 0 255 0\n"
                    + "\n"
                    + "Shape Steve Ellipse\n"
                    + "Motion Steve 0 1.0 1.0 20 20 255 0 0    10 20.0 20.0 20 20 255 0 0",
            this.model.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddShapeFailsSameIds() {
    this.builder.declareShape("Dave", "Rectangle").declareShape("Dave", "Ellipse");
  }

  @Test(expected = IllegalStateException.class)
  public void testAddCommandFailsDiffIds() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20,
                    10, 10, 30, 0, 255, 0)
            .addMotion("Steve", 5, 20, 10, 10, 30, 0, 255, 0, 10, 20,
                    20, 10, 30, 0, 255, 0);
    this.model = this.builder.build();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testAddCommandsInvalidOverlap() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20,
                    10, 10, 30, 0, 255, 0)
            .addMotion("Dave", 3, 16, 10, 10, 30, 0, 255, 0, 7, 26,
                    15, 10, 30, 0, 255, 0);
    this.model = this.builder.build();
    assertEquals("Shape Dave Rectangle\n"
                    + "Motion Dave 0 10.0 10.0 10 30 0 255 0    5 20.0 10.0 10 30 0 255 0",
            this.model.toString());
  }

  @Test //commands who start on the same tick that another ends are valid and work.
  public void testAddCommandsValidOverlap() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20,
                    10, 10, 30, 0, 255, 0)
            .addMotion("Dave", 5, 20, 10, 10, 30, 0, 255, 0, 8, 20,
                    10, 10, 30, 0, 0, 255);
    this.model = this.builder.build();
    assertEquals("Shape Dave Rectangle\n"
                    + "Motion Dave 0 10.0 10.0 10 30 0 255 0    5 20.0 10.0 10 30 0 255 0\n"
                    + "Color Dave 5 20.0 10.0 10 30 0 255 0    8 20.0 10.0 10 30 0 0 255",
            this.model.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddCommandsInvalidTeleport() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20,
                    10, 10, 30, 0, 255, 0)
            .addMotion("Dave", 5, 35, 10, 10, 30, 0, 255, 0, 10, 45,
                    15, 10, 30, 0, 255, 0);
    this.model = this.builder.build();
    assertEquals("Shape Dave Rectangle\n"
                    + "Motion Dave 0 10.0 10.0 10 30 0 255 0    5 20.0 10.0 10 30 0 255 0",
            this.model.toString());
  }

  @Test
  public void testGetStateEmptyModel() {
    this.model = this.builder.build();
    assertEquals(new ArrayList<IReadOnlyShape>(),
            this.model.getState(3));
  }

  @Test
  public void testGetState() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20, 10,
                    10, 30, 0, 255, 0);
    this.model = this.builder.build();
    List<IReadOnlyShape> expected = new ArrayList<>(Arrays.asList(
            new Rectangle(10, 30, new Point2D.Double(10, 10), 0, Color.GREEN)));
    assertEquals(expected, this.model.getState(0));
  }

  @Test
  public void testToString() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20, 10,
                    10, 30, 0, 255, 0)
            .addMotion("Dave", 5, 20, 10, 10, 30, 0, 255, 0, 10, 20, 20,
                    10, 30, 0, 255, 0)
            .declareShape("Steve", "Ellipse")
            .addMotion("Steve", 0, 1, 1, 20, 20, 255, 0, 0, 10, 20, 20,
                    20, 20, 255, 0, 0);
    this.model = this.builder.build();
    assertEquals("Shape Dave Rectangle\n"
                    + "Motion Dave 0 10.0 10.0 10 30 0 255 0    5 20.0 10.0 10 30 0 255 0\n"
                    + "Motion Dave 5 20.0 10.0 10 30 0 255 0    10 20.0 20.0 10 30 0 255 0\n"
                    + "\n"
                    + "Shape Steve Ellipse\n"
                    + "Motion Steve 0 1.0 1.0 20 20 255 0 0    10 20.0 20.0 20 20 255 0 0",
            this.model.toString());
  }

  @Test
  public void testToStringComboCommand() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 5, 20, 10, 10, 30, 0, 255, 0, 8, 20,
                    10, 100, 300, 0, 255, 255);
    this.model = this.builder.build();
    assertEquals("Shape Dave Rectangle\n"
                    + "Size and Color Dave 5 20.0 10.0 10 30 0 255 0  "
                    + "  8 20.0 10.0 100 300 0 255 255",
            this.model.toString());
  }

  @Test
  public void testGetWidth() {
    this.model = this.builder.build();
    assertEquals(model.getWidth(), 1000);
  }

  @Test
  public void testGetHeight() {
    this.model = this.builder.build();
    assertEquals(model.getHeight(), 600);
  }

  @Test
  public void testGetShapes() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20,
                    10, 10, 30, 0, 255, 0)
            .addMotion("Dave", 5, 20, 10, 10, 30, 0, 255, 0, 10, 20,
                    20, 10, 30, 0, 255, 0)
            .declareShape("Steve", "Ellipse")
            .addMotion("Steve", 0, 1, 1, 20, 20, 255, 0, 0, 10, 20,
                    20, 20, 20, 255, 0, 0);
    this.model = this.builder.build();
    LinkedHashMap<String, IReadOnlyShape> map = new LinkedHashMap<>();
    map.put("Dave", new Rectangle());
    map.put("Steve", new Ellipse());
    assertEquals(model.getShapes(), map);
  }

  @Test
  public void testGetCommands() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5, 20,
                    10, 10, 30, 0, 255, 0)
            .addMotion("Dave", 5, 20, 10, 10, 30, 0, 255, 0, 10, 20,
                    20, 10, 30, 0, 255, 0)
            .declareShape("Steve", "Ellipse")
            .addMotion("Steve", 0, 1, 1, 20, 20, 255, 0, 0, 10, 20,
                    20, 20, 20, 255, 0, 0);
    this.model = this.builder.build();
    LinkedHashMap<String, List<IReadOnlyCommand>> map = new LinkedHashMap<>();
    List<IReadOnlyCommand> commands1 = new ArrayList<>();
    List<IReadOnlyCommand> commands2 = new ArrayList<>();
    commands1.add(new MasterCommand("Motion", 0, 10, 10, 10, 30, 0, 255, 0, 5,
            20, 10, 10, 30, 0, 255, 0));
    commands1.add(new MasterCommand("Motion", 6, 20, 10, 10, 30, 0, 255, 0, 10,
            20, 20, 10, 30, 0, 255, 0));
    commands2.add(new MasterCommand("Motion", 0, 1, 1, 20, 20, 255, 0, 0, 10,
            20, 20, 20, 20, 255, 0, 0));
    map.put("Dave", commands1);
    map.put("Steve", commands2);
    assertEquals(model.getCommands().get("Dave").get(1).getStartX(),20 );
    assertEquals(model.getCommands().get("Steve").get(0).getEndWidth(),20 );
  }

  @Test
  public void testGetFinalTick() {
    this.builder.declareShape("Dave", "Rectangle")
            .addMotion("Dave", 0, 10, 10, 10, 30, 0, 255, 0, 5,
                    20, 10, 10, 30, 0, 255, 0)
            .addMotion("Dave", 5, 20, 10, 10, 30, 0, 255, 0, 10,
                    20, 20, 10, 30, 0, 255, 0)
            .declareShape("Steve", "Ellipse")
            .addMotion("Steve", 0, 1, 1, 20, 20, 255, 0, 0, 30,
                    20, 20, 20, 20, 255, 0, 0);
    this.model = this.builder.build();
    assertEquals(model.getFinalTick(), 30);
  }
}