import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

import cs3500.animator.controller.IReadOnlyKeyframe;
import cs3500.animator.controller.Keyframe;

import static org.junit.Assert.assertEquals;

/**
 * Represents the Tester class for the Keyframe class.
 */
public class KeyframeTests {
  private IReadOnlyKeyframe keyframe;

  @Before
  public void setUp() {
    keyframe = new Keyframe(1,2,3,4,5, new Color(1,2,3));
  }

  @Test
  public void testGetTime() {
    assertEquals(1, keyframe.getTime());
  }

  @Test
  public void testGetX() {
    assertEquals(2, keyframe.getX());
  }

  @Test
  public void testGetY() {
    assertEquals(3, keyframe.getY());
  }

  @Test
  public void getWidth() {
    assertEquals(4, keyframe.getWidth());
  }

  @Test
  public void testGetHeight() {
    assertEquals(5, keyframe.getHeight());
  }

  @Test
  public void testGetColor() {
    assertEquals(new Color(1,2,3), keyframe.getColor());
  }

  @Test
  public void testToString() {
    assertEquals(keyframe.toString(), "t-1: at (2, 3), 4x5, color: (1, 2, 3)");
  }
}