package edu.brown.cs.rankers;

import com.genfit.attribute.attributevals.Color;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ColorTest {
  @Test
  public void testBasic() {
    assertEquals(Color.convertToHexVal(66, 134, 244), 4359924);
    assertEquals(Color.convertToHexVal(113, 132, 99), 7439459);
    assertEquals(Color.convertToHexVal(181, 21, 69), 11867461);
    assertEquals(Color.convertToHexVal(0, 0, 0), 0);
    assertEquals(Color.convertToHexVal(0, -1350, 0), 0);
    assertEquals(Color.convertToHexVal(30, 03, -130), 1966848);
    assertEquals(Color.convertToHexVal(30, -03, 40), 1966120);
    assertEquals(Color.convertToHexVal(255, 255, 255), 16777215);
    assertEquals(Color.convertToHexVal(426, 23, 34724), 16717823);
  }
}
