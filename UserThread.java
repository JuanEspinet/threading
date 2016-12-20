import java.util.concurrent.ThreadLocalRandom;

/**
* Creates simulated users to interact with the playing field.
* Each user is intended to be its own thread with the shared playingField
* injected at instantiation.
*/
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

  public void collect(int amt) {
    if (amt > 0) {
      collected += amt;
    }
  }

  /**
  * Checks if this user thread meets the criteria to collect values
  * from the current position and performs that collection if able.
  *
  * @return boolean
  */
  public boolean checkAndCollect() {
    boolean canCollect = false;

    synchronized(playingField) {
      int positionValue = playingField.getValuesByCoord(this.position[0], this.position[1]);
      canCollect = this.canCollect(positionValue);

      if (canCollect) {
        this.collect(positionValue);
        playingField.resetValue(this.position[0], this.position[1]);
      }
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

  public int[] createRandomMove() {
    int[] randomMove = new int[]{position[0], position[1]};
    int axis = randomGenerator.nextInt(2);
    int direction = randomGenerator.nextBoolean() ? -1 : 1;

    randomMove[axis] += direction;

    return randomMove;
  }

  public boolean attemptUserMove(int[] newPosition) {
    boolean moved = false;
    synchronized(playingField) {
      if (playingField.isValidMove(newPosition, position)) {
        position = newPosition;
        moved = true;
      }
    }
    return moved;
  }
}
