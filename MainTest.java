import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class MainTest extends TestCase {
  protected MainThread testMainThread;

  protected void setUp() {
    testMainThread = new MainThread();
    testMainThread.initializeForTest();
  }

  public void testCreateUser() {
    assertThat(testMainThread.createUser(), instanceOf(UserThread.class));

    UserThread testUser = testMainThread.createUser();
    int[] userPos = testUser.position;
    assertTrue(testMainThread.playingField.isPositionOccupied(userPos[0], userPos[1]));
  }

  public void testInitializeUsers() {
    try {
      UserThread[] allUsers = testMainThread.initializeUsers(2);

      assertThat(allUsers[0], instanceOf(UserThread.class));

      assertTrue(allUsers.length == 2);
    } catch (Exception exception) {
      fail(exception.getMessage());
    }
  }
}
