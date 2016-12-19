import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class PlayingFieldTest extends TestCase {
  protected PlayingField playingFieldTest;
  protected int fieldX;
  protected int fieldY;

  protected void setUp() {
    playingFieldTest = new PlayingField(10,10,100);
    fieldX = 7;
    fieldY = 9;
  }

  public void testFieldGenerator() {
    int[][] fieldTest = playingFieldTest.generateField(fieldX, fieldY);

    assertEquals(fieldTest.length, fieldX);
    assertEquals(fieldTest[0].length, fieldY);

    for (int[] i : fieldTest) {
      for (int j : i) {
        assertThat(j, instanceOf(Integer.class));
        assertTrue((j >= 0) && (j <= 100));
      }
    }
  }

  public void testGetFieldLength() {
    assertEquals(playingFieldTest.getFieldLength(), 10);
    assertThat(playingFieldTest.getFieldLength(), instanceOf(Integer.class));
  }

  public void testGetFieldWidth() {
    assertEquals(playingFieldTest.getFieldWidth(), 10);
    assertThat(playingFieldTest.getFieldWidth(), instanceOf(Integer.class));
  }

  public void testGetValuesByCoord() {
    assertTrue(playingFieldTest.getValuesByCoord(0,0) <= 100);
  }

  public void testSetValueByCoord() {
    assertTrue(playingFieldTest.setValueByCoord(0,0,100) == 100);
  }

  public void testScrambleField() {
    boolean valChanged = false;

    for (int i = 0; i < 10; i++) {
      int before = playingFieldTest.getValuesByCoord(0,0);
      playingFieldTest.scrambleField();
      int after = playingFieldTest.getValuesByCoord(0,0);
      if (before != after) {
        valChanged = true;
        break;
      }
    }

    assertTrue(valChanged);
  }

  public void testGetAndResetValue() {
    int gottenVal = playingFieldTest.getAndResetValue(0,0);

    assertEquals(playingFieldTest.getValuesByCoord(0,0), 0);
    assertTrue(gottenVal >= 0);
    assertTrue(gottenVal <= 100);
  }
}
