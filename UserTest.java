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
  }

  public void testUserSetup() {
    assertEquals(userTest.collected, 0);
    assertEquals(userTest.processed, 0);

    assertTrue(userTest.capacity < 80 && userTest.capacity >= 50);

    assertTrue(userTest.speed < 16 && userTest.speed >= 5);
  }

  public void testCheckAndCollect() {
    userTest.collected = 0;

    assertTrue(userTest.checkAndCollect(5));
    assertEquals(userTest.collected, 5);

    userTest.collected = 0;
    assertFalse(userTest.checkAndCollect(500));
    assertEquals(userTest.collected, 0);
  }

  public void testProcess() {
    userTest.collected = 5;
    assertEquals(userTest.process(5), 5);
  }
}
