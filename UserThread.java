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
    speed = randomGenerator.nextInt(5,16);
    collected = 0;
    processed = 0;
  }

  public void run() {

  }

  public void start() {
    System.out.println("Starting " +  threadName );
    if (t == null) {
       t = new Thread(this, threadName);
       t.start();
    }
  }

  public boolean canCollect(int amt) {
    if (amt <= capacity - collected) {
      return true;
    }
    return false;
  }

  public boolean checkAndCollect(int amt) {
    boolean canCollect = this.canCollect(amt);
    if (canCollect) {
      collected += amt;
    }
    return canCollect;
  }

  /**
  * Processes collected values into this user's processed total.
  *
  * @param int amnt The amount to be processed.
  * @return int
  */
  public int process(int amnt) {
    int thisProcessTotal = 0;

    while (collected > 0 && collected < capacity) {
      try {
        collected -= 1;
        processed += 1;
        thisProcessTotal += 1;
        Thread.sleep(speed * randomGenerator.nextInt(5, 25));
      } catch (InterruptedException e) {
        System.out.println("User " +  threadName + " interrupted while processing." );
      }
    }

    return thisProcessTotal;
  }
}
