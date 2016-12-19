import java.util.concurrent.ThreadLocalRandom;

public class UserThread extends Thread {
  private Thread t;
  private final String threadName;

  private PlayingField playingField;

  private ThreadLocalRandom randomGenerator;

  public final int capacity;
  public final int speed;

  public volatile int collected;
  public volatile int processed;

  public volatile int[] position;

  public UserThread(String name, PlayingField field) {
    playingField = field;
    threadName = name;
    randomGenerator = ThreadLocalRandom.current();
    capacity = randomGenerator.nextInt(50, 80);
    speed = randomGenerator.nextInt(5,15) * 25;
    collected = 0;
    processed = 0;
  }

  public void run() {

  }

  public void start() {

  }

  /**
  * Processes collected values into this user's processed total.
  *
  * @param int amnt The amount to be processed.
  * @return int
  */
  public int process(int amnt) {
    int thisProcess = 0;

    while (collected > 0) {
      try {
        collected -= 1;
        processed += 1;
        thisProcess += 1;
        Thread.sleep(speed);
      } catch (InterruptedException e) {
        System.out.println("User " +  threadName + " interrupted while processing." );
      }
    }

    return thisProcess;
  }
}
