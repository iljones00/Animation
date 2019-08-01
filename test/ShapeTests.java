import org.junit.Test;

import java.awt.Color;
import java.awt.geom.Point2D;

import cs3500.animator.model.Ellipse;
import cs3500.animator.model.Rectangle;

import static org.junit.Assert.assertEquals;

/**
 * Tests for all public methods of {@link Ellipse} and {@link Rectangle}.
 */
public class ShapeTests {
  private Rectangle rectangle;
  private Ellipse ellipse;

  //Initializes the data for the tests.
  private void initializeData() {
    rectangle = new Rectangle(1,3, new Point2D.Double(10,10), 0, Color.GREEN);
    ellipse = new Ellipse(2,2, new Point2D.Double(1,1), 0, Color.RED);
  }

  /**
   * Tests that the constructor for shapes throw a null pointer if it is passed null for the
   * incoming position.
   */
  @Test (expected = NullPointerException.class)
  public void testBadConstructor1() {
    initializeData();
    rectangle = new Rectangle(1,3, null, 0, Color.GREEN);
    ellipse = new Ellipse(2,2, null, 0, Color.RED);
  }

  /**
   * Tests that the constructor for shapes throw a null pointer if it is passed null for the
   * incoming color.
   */
  @Test (expected = NullPointerException.class)
  public void testBadConstructor2() {
    initializeData();
    rectangle = new Rectangle(1,3, new Point2D.Double(10,10), 0, null);
  }

  /**
   * Test for getting the shape type.
   */
  @Test
  public void testGetShapeType() {
    initializeData();
    assertEquals(ellipse.getShapeType(), "Ellipse");
    assertEquals(rectangle.getShapeType(), "Rectangle");
  }

  /**
   * Test for getting the color.
   */
  @Test
  public void testGetColor() {
    initializeData();
    assertEquals(ellipse.getColor(), Color.RED);
    assertEquals(rectangle.getColor(), Color.GREEN);
  }

  /**
   * Test for getting the color of the shape.
   */
  @Test
  public void testGetPosition() {
    initializeData();
    assertEquals(ellipse.getPosition(), new Point2D.Double(1,1));
    assertEquals(rectangle.getPosition(), new Point2D.Double(10,10));
  }

  /**
   * Test for getting the height of the shape.
   */
  @Test
  public void testGetHeight() {
    initializeData();
    assertEquals(ellipse.getHeight(), 2);
    assertEquals(rectangle.getHeight(), 3);
  }

  /**
   * Test for getting the width of the shape.
   */
  @Test
  public void testGetWidth() {
    initializeData();
    assertEquals(ellipse.getWidth(), 2);
    assertEquals(rectangle.getWidth(), 1);
  }

  /**
   * Test for setting the width of the shape.
   */
  @Test
  public void testSetWidth() {
    initializeData();
    this.ellipse.setWidth(5);
    assertEquals(ellipse.getWidth(), 5);
    this.rectangle.setWidth(10);
    assertEquals(rectangle.getWidth(), 10);
  }

  /**
   * Tests that the set width method throws an exception
   * when it is given a negative value for width.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testBadSetWidth() {
    initializeData();
    this.rectangle.setWidth(-10);
  }

  /**
   * Tests that the set width method throws an exception
   * when it is given a negative value for height.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testBadSetHeight() {
    initializeData();
    this.rectangle.setHeight(-10);
  }

  /**
   * Test for setting the height of the shape.
   */
  @Test
  public void testSetHeight() {
    initializeData();
    this.ellipse.setHeight(5);
    assertEquals(ellipse.getHeight(), 5);
    this.rectangle.setHeight(10);
    assertEquals(rectangle.getHeight(), 10);
  }

  /**
   * Test for setting the position of the shape.
   */
  @Test
  public void testSetPosition() {
    initializeData();
    assertEquals(ellipse.getPosition(), new Point2D.Double(1,1));
    this.ellipse.setPosition(new Point2D.Double(20,20));
    assertEquals(ellipse.getPosition(), new Point2D.Double(20,20));
    assertEquals(rectangle.getPosition(), new Point2D.Double(10,10));
    this.rectangle.setPosition(new Point2D.Double(25,15));
    assertEquals(rectangle.getPosition(), new Point2D.Double(25,15));
  }

  /**
   * Test that the setPosition() method throws an Illegal Argument Exception when it is given null
   * as a position.
   */
  @Test (expected = NullPointerException.class)
  public void testBadSetPosition() {
    initializeData();
    this.ellipse.setPosition(null);
  }

  /**
   * Test for setting the color of the shape.
   */
  @Test
  public void testSetColor() {
    initializeData();
    assertEquals(ellipse.getColor(), Color.RED);
    this.ellipse.setColor(Color.BLACK);
    assertEquals(ellipse.getColor(), Color.BLACK);
    assertEquals(rectangle.getColor(), Color.GREEN);
    this.rectangle.setColor(Color.CYAN);
    assertEquals(rectangle.getColor(), Color.CYAN);
  }

  /**
   * Test that the setColor() method throws an Illegal Argument Exception when it is given null
   * as a color.
   */
  @Test (expected = NullPointerException.class)
  public void testBadSetColor() {
    initializeData();
    this.ellipse.setColor(null);
  }
}
