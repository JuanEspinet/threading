import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

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

  public UserThread(String name, PlayingField field, int[] startPosition) {
    playingField = field;
    threadName = name;
    randomGenerator = ThreadLocalRandom.current();
    capacity = randomGenerator.nextInt(50, 80);
    speed = randomGenerator.nextInt(5,16);
    collected = 0;
    processed = 0;
    position = startPosition;
  }

  public void run() {
    while (!playingField.isObjectiveComplete()) {
      String currentLocation = "("+position[0]+","+position[1]+")";
      if (this.checkAndCollect()) {
        System.out.println(this.threadName + " collected " + this.collected
          + " at " + currentLocation + ". Processing now.");
        this.process(this.collected);

        System.out.println(this.threadName + " finished processing, and now has "
          + this.processed + " total.");

        synchronized(playingField) {
          if (thisUserWins()) {
            System.out.println(this.threadName + " reached the objective!");
            playingField.markObjectiveComplete(this);
            break;
          } else if (playingField.isObjectiveComplete()) {
            break;
          }
        }
      }

      boolean moved = this.tryUntilSuccessfulMove(10);
      if (moved) {
        System.out.println(this.threadName + " moved to " + currentLocation);
      }
    }

    System.out.println(this.threadName + " finished with " + processed);
    System.out.println(this.threadName + " exiting.");
  }

  public String getUserName() {
    return threadName;
  }

  public boolean thisUserWins() {
    return !playingField.isObjectiveComplete() && this.objectiveReached();
  }

  public boolean objectiveReached() {
    return this.processed >= playingField.objective;
  }

  public boolean canCollect(int amt) {
    if (amt <= capacity - collected && amt > 0) {
      return true;
    }
    return false;
  }

  public int getProcessed() {
    return this.processed;
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
      if (playingField.isObjectiveComplete()) {
        break;
      }

      try {
        collected -= 1;
        processed += 1;
        thisProcessTotal += 1;
        Thread.sleep(speed * randomGenerator.nextInt(10, 26));
      } catch (InterruptedException e) {
        System.out.println("User " +  threadName + " interrupted while processing." );
      }
    }

    return thisProcessTotal;
  }

  /**
  * Generates coordinates for a possible move based on the current position of
  * user.
  *
  * @return int[]
  */
  public int[] createRandomMove() {
    int[][] possibleMoves = new int[][]{
      {-1, 0},
      {+1, 0},
      {0, -1},
      {0, +1}
    };
    ArrayList<int[]> realMoves = new ArrayList<int[]>();

    for (int i = 0; i < possibleMoves.length; i++) {
      possibleMoves[i][0] += position[0];
      possibleMoves[i][1] += position[1];

      if (playingField.isPositionValidAndEmpty(possibleMoves[i])) {
        realMoves.add(possibleMoves[i]);
      }
    }

    if (realMoves.size() == 0) {
      return null;
    }

    return realMoves.get(randomGenerator.nextInt(realMoves.size()));
  }

  /**
  * Attempts to move the current user to the given coordinates. Move is
  * validated by the playingField.moveUserToPosition method, which is
  * a synchronized method and updates the playing field if the move
  * is valid.
  *
  * @return boolean Indicates success or failure of the move.
  */
  public boolean attemptUserMove(int[] newPosition) {
    boolean moved = false;

    if (playingField.moveUserToPosition(newPosition, position)) {
      position[0] = newPosition[0];
      position[1] = newPosition[1];
      moved = true;
    }

    return moved;
  }

  /**
  * Repeatedly attempts to move the user until a successful move is made. Or
  * max attempts are reached.
  */
  public boolean tryUntilSuccessfulMove(int maxAttempts) {
    int attempts = 0;
    int[] newPosition;
    boolean moved = false;

    do {
      attempts++;
      try {
        Thread.sleep(this.speed * 50);
      } catch (InterruptedException exception) {
        return false;
      }

      synchronized(playingField) {
        newPosition = this.createRandomMove();
        if (newPosition != null) {
          moved = attemptUserMove(newPosition);
        }
      }

    } while (!moved && attempts < maxAttempts);

    return moved;
  }
}
