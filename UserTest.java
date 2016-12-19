import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class UserTest extends TestCase {
  protected UserThread userTest;

  protected void setUp() {
    PlayingField playingField = new PlayingField(10,10,100);
    userTest = new UserThread("Test User", playingField);
  }

  public void testPlaceholderTest() {
    assertTrue(true);
  }
}
