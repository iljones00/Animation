import org.junit.Before;
import org.junit.Test;

import cs3500.animator.view.IShapeCell;
import cs3500.animator.view.ShapeCell;

import static org.junit.Assert.assertEquals;

/**
 * Tester for the shape cell class.
 */
public class ShapeCellTests {
  IShapeCell cell1;
  IShapeCell cell2;

  @Before
  public void setUp() {
    cell1 = new ShapeCell("Dave","Rect");
    cell2 = new ShapeCell("John","Ellipse");
  }

  @Test
  public void testGetId() {
    assertEquals(cell1.getID(), "Dave");
    assertEquals(cell2.getID(), "John");
  }

  @Test
  public void testGetType() {
    assertEquals(cell1.getType(), "Rect");
    assertEquals(cell2.getType(), "Ellipse");
  }
}
