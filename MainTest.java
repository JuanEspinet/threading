import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class MainTest extends TestCase {
  protected MainThread testMainThread;

  protected void setUp() {
    testMainThread = new MainThread();
  }

  public void testPlaceholderTest() {
    assertTrue(true);
  }
}
