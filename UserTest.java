import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class UserTest extends TestCase {
  protected UserThread userTest;
  protected PlayingField playingField;

  protected void setUp() {
    playingField = new PlayingField(10,10,100);
    userTest = new UserThread("userThreadTestUser", playingField);
    userTest.position = new int[]{0,0};
  }

  public void testUserSetup() {
    assertEquals(userTest.collected, 0);
    assertEquals(userTest.processed, 0);

    assertTrue(userTest.capacity < 80 && userTest.capacity >= 50);

    assertTrue(userTest.speed < 16 && userTest.speed >= 5);
  }

  public void testCollect() {
    userTest.collected = 0;

    userTest.collect(5);
    assertEquals(userTest.collected, 5);

    userTest.collect(500);
    assertEquals(userTest.collected, 505);
  }

  public void testCheckAndCollect() {
    userTest.collected = 0;
    playingField.setValueByCoord(0,0,5);

    assertTrue(userTest.checkAndCollect());
    assertEquals(userTest.collected, 5);
  }

  public void testProcess() {
    userTest.collected = 5;
    assertEquals(userTest.process(5), 5);
  }

  public void testCreateRandomMove() {
    int[] randomMove = userTest.createRandomMove();

    assertThat(userTest.position, not(equalTo(randomMove)));
    assertTrue(Math.abs(randomMove[0] - userTest.position[0]) < 2);
    assertTrue(Math.abs(randomMove[1] - userTest.position[1]) < 2);
  }

  public void testAttemptUserMove() {
    int[] move = new int[]{0,1};

    assertTrue(userTest.attemptUserMove(move));
    assertArrayEquals(userTest.position, move);
  }
}
