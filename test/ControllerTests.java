import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cs3500.animator.controller.Controller;
import cs3500.animator.controller.IReadOnlyKeyframe;
import cs3500.animator.controller.IRotateKeyframe;
import cs3500.animator.controller.Keyframe;
import cs3500.animator.controller.RotateKeyframe;
import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.AnimationReader;
import cs3500.animator.model.IEditBuilder;
import cs3500.animator.view.EditingView;
import cs3500.animator.view.FrameChange;
import cs3500.animator.view.FrameChangeEvent;
import cs3500.animator.view.IEditableView;
import cs3500.animator.view.IFrameChangeEvent;
import cs3500.animator.view.IShapeCell;
import cs3500.animator.view.IShapeChangeEvent;
import cs3500.animator.view.ShapeCell;
import cs3500.animator.view.ShapeChange;
import cs3500.animator.view.ShapeChangeEvent;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link Controller} to ensure it properly handles changes sent by the editing view.
 */
public class ControllerTests {
  private Controller controller;
  private IEditBuilder builder;
  private IEditableView view;
  private Map<String, List<IRotateKeyframe>> originalFrames;

  @Before
  public void setUp() {
    this.builder = new AnimationModel.AnimationModelBuilder();
    FileReader in;
    try {
      in = new FileReader("./inputs/smalldemo.txt");
    }
    catch (FileNotFoundException e) {
      throw new IllegalArgumentException("check file name.");
    }
    AnimationReader.parseFile(in, this.builder);
    this.view = new EditingView(20);
    this.controller = new Controller(this.builder, this.view, 20);
    this.initOriginalFrames();
  }

  private void initOriginalFrames() {
    List<IRotateKeyframe> rect = new ArrayList<>();
    rect.add(new RotateKeyframe(1, 200, 200, 50, 100, 0, new Color(255, 0, 0)));
    rect.add(new RotateKeyframe(10, 200, 200, 50, 100, 0, new Color(255, 0, 0)));
    rect.add(new RotateKeyframe(50, 300, 300, 50, 100, 0, new Color(255, 0, 0)));
    rect.add(new RotateKeyframe(51, 300, 300, 50, 100, 0, new Color(255, 0, 0)));
    rect.add(new RotateKeyframe(70, 300, 300, 25, 100, 0, new Color(255, 0, 0)));
    rect.add(new RotateKeyframe(100, 200, 200, 25, 100, 0, new Color(255, 0, 0)));

    List<IRotateKeyframe> ellipse = new ArrayList<>();
    ellipse.add(new RotateKeyframe(6, 440, 70, 120, 60, 0, new Color(0, 0, 255)));
    ellipse.add(new RotateKeyframe(20, 440, 70, 120, 60, 0, new Color(0, 0, 255)));
    ellipse.add(new RotateKeyframe(50, 440, 250, 120, 60, 0, new Color(0, 0, 255)));
    ellipse.add(new RotateKeyframe(70, 440, 370, 120, 60, 0, new Color(0, 170, 85)));
    ellipse.add(new RotateKeyframe(80, 440, 370, 120, 60, 0, new Color(0, 255, 0)));
    ellipse.add(new RotateKeyframe(100, 440, 370, 120, 60, 0, new Color(0, 255, 0)));

    this.originalFrames = new LinkedHashMap<>();
    this.originalFrames.put("R", rect);
    this.originalFrames.put("C", ellipse);
  }

  @Test (expected = NullPointerException.class)
  public void testConstructorNullBuilder() {
    new Controller(null, this.view, 20);
  }

  @Test (expected = NullPointerException.class)
  public void testConstructorNullView() {
    new Controller(this.builder, null, 20);
  }

  @Test
  public void testKeyframeAddEnd() {
    this.controller.start();
    assertEquals(this.originalFrames, this.originalFrames);
    IFrameChangeEvent event = new FrameChangeEvent(this.view, FrameChange.ADD,
            "R", 110, 200, 200, 25, 125, 0, new Color(255, 0, 0));
    this.controller.keyframeChanged(event);
    this.originalFrames.get("R").add(new RotateKeyframe(110, 200, 200, 25, 125, 0,
            new Color(255, 0, 0)));
    assertEquals(this.originalFrames, this.view.getKeyframes());
  }

  @Test
  public void testKeyframeAddMiddle() {
    this.controller.start();
    IFrameChangeEvent event = new FrameChangeEvent(this.view, FrameChange.ADD,
            "R", 80, 200, 200, 25, 125, 0, new Color(255, 0, 0));
    this.controller.keyframeChanged(event);
    this.originalFrames.get("R").add(5, new RotateKeyframe(80, 200, 200, 25, 125, 0,
            new Color(255, 0, 0)));
    assertEquals(this.originalFrames, this.view.getKeyframes());
  }

  @Test
  public void testKeyframeAddFront() {
    this.controller.start();
    IFrameChangeEvent event = new FrameChangeEvent(this.view, FrameChange.ADD,
            "C", 1, 440, 70, 120, 60, 0, new Color(0, 0, 255));
    this.controller.keyframeChanged(event);
    this.originalFrames.get("C").add(0, new RotateKeyframe(1, 440, 70, 120, 60, 0,
            new Color(0, 0, 255)));
    assertEquals(this.originalFrames, this.view.getKeyframes());
  }

  @Test
  public void testKeyframeEdit() {
    this.controller.start();
    IFrameChangeEvent event = new FrameChangeEvent(this.view, FrameChange.EDIT,
            "R", 100, 200, 200, 25, 125, 0, new Color(255, 0, 0));
    this.controller.keyframeChanged(event);
    this.originalFrames.get("R").remove(5);
    this.originalFrames.get("R").add(new RotateKeyframe(100, 200, 200, 25, 125, 0,
            new Color(255, 0, 0)));
    assertEquals(this.originalFrames, this.view.getKeyframes());
  }

  @Test
  public void testKeyframeDelete() {
    this.controller.start();
    IFrameChangeEvent event = new FrameChangeEvent(this.view, FrameChange.DELETE,
            "R", 100, 0, 0, 0, 0, 0, new Color(0));
    this.controller.keyframeChanged(event);
    this.originalFrames.get("R").remove(5);
    assertEquals(this.originalFrames, this.view.getKeyframes());
  }

  @Test
  public void testShapeAdd() {
    this.controller.start();
    IShapeChangeEvent event = new ShapeChangeEvent(this.view, ShapeChange.ADD,
            "Rectangle", "Dave");
    this.controller.shapeChanged(event);
    List<IShapeCell> shapes = new ArrayList<>();
    shapes.add(new ShapeCell("R", "Rectangle"));
    shapes.add(new ShapeCell("C", "Ellipse"));
    shapes.add(new ShapeCell("Dave", "Rectangle"));
    assertEquals(shapes, this.view.getShapes());
  }

  @Test
  public void testShapeDelete() {
    this.controller.start();
    IShapeChangeEvent event = new ShapeChangeEvent(this.view, ShapeChange.DELETE,
            "", "R");
    this.controller.shapeChanged(event);
    List<IShapeCell> shapes = new ArrayList<>();
    shapes.add(new ShapeCell("C", "Ellipse"));
    assertEquals(shapes, this.view.getShapes());
  }
}
