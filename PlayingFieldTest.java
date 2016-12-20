import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class PlayingFieldTest extends TestCase {
  protected PlayingField playingFieldTest;
  protected UserThread testUser;
  protected int fieldX;
  protected int fieldY;

  protected void setUp() {
    playingFieldTest = new PlayingField(10,10,100,200);
    testUser = new UserThread("playingFieldTestUser", playingFieldTest, new int[]{0,0});
    fieldX = 7;
    fieldY = 9;
  }

  public void testIsObjectiveComplete() {
    assertFalse(playingFieldTest.isObjectiveComplete());
  }

  public void testMarkObjectiveComplete() {
    playingFieldTest.markObjectiveComplete(testUser);
    assertTrue(playingFieldTest.isObjectiveComplete());
    assertSame(testUser, playingFieldTest.getWinningUser());
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

  public void testResetValue() {
    playingFieldTest.resetValue(0,0);

    assertEquals(playingFieldTest.getValuesByCoord(0,0), 0);
  }

  public void testGetFirstEmptyPosition() {
    assertThat(playingFieldTest.getFirstEmptyPosition()[0], instanceOf(Integer.class));
    assertTrue(playingFieldTest.getFirstEmptyPosition().length == 2);
  }

  public void testIsPositionOccupied() {
    assertTrue(!(playingFieldTest.isPositionOccupied(0,0)));
  }

  public void testPlaceUserAtPosition() {
    playingFieldTest.placeUserAtPosition(0,0);
    assertTrue(playingFieldTest.isPositionOccupied(0,0));
  }

  public void testEmptyPosition() {
    playingFieldTest.placeUserAtPosition(0,0);
    playingFieldTest.emptyPosition(0,0);
    assertTrue(!(playingFieldTest.isPositionOccupied(0,0)));
  }

  public void testPlaceNewUser() {
    playingFieldTest.emptyPosition(0,0);
    assertThat(playingFieldTest.placeNewUser()[0], instanceOf(Integer.class));
    assertTrue(playingFieldTest.isPositionOccupied(0,0));
  }

  public void testIsPositionValidAndEmpty() {
    assertTrue(playingFieldTest.isPositionValidAndEmpty(new int[]{9,9}));
  }

  public void testIsPositionAdjacent() {
    assertTrue(playingFieldTest.isPositionAdjacent(new int[]{9,9}, new int[]{8,9}));
  }

  public void testIsValidMove() {
    int[] newPosition = {1,0};
    int[] oldPosition = {0,0};

    playingFieldTest.emptyPosition(newPosition[0], newPosition[1]);
    assertTrue(playingFieldTest.isValidMove(newPosition, oldPosition));

    playingFieldTest.placeUserAtPosition(newPosition[0], newPosition[1]);
    assertFalse(playingFieldTest.isValidMove(newPosition, oldPosition));

    newPosition[0] = -1;
    assertFalse(playingFieldTest.isValidMove(newPosition, oldPosition));

    newPosition[0] = 3;
    assertFalse(playingFieldTest.isValidMove(newPosition, oldPosition));
  }

  public void testMoveUserToPosition() {
    int[] newPosition = {1,0};
    int[] oldPosition = {0,0};

    playingFieldTest.emptyPosition(newPosition[0], newPosition[1]);
    assertTrue(playingFieldTest.moveUserToPosition(newPosition, oldPosition));
    assertTrue(playingFieldTest.isPositionOccupied(newPosition[0], newPosition[1]));
    assertFalse(playingFieldTest.isPositionOccupied(oldPosition[0], oldPosition[1]));

    playingFieldTest.placeUserAtPosition(oldPosition[0], oldPosition[1]);
    playingFieldTest.placeUserAtPosition(newPosition[0], newPosition[1]);
    assertFalse(playingFieldTest.moveUserToPosition(newPosition, oldPosition));
    assertTrue(playingFieldTest.isPositionOccupied(oldPosition[0], oldPosition[1]));

    newPosition[0] = -1;
    assertFalse(playingFieldTest.moveUserToPosition(newPosition, oldPosition));
    assertTrue(playingFieldTest.isPositionOccupied(oldPosition[0], oldPosition[1]));
  }
}
