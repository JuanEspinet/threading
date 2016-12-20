import java.util.concurrent.ThreadLocalRandom;

class MainThread extends Thread {
  private Thread t;
  private String threadName;

  private ThreadLocalRandom randomGenerator;

  private int userIdCounter;
  private final int numUsers;

  public PlayingField playingField;

  private volatile UserThread[] allUsers;

  public static void main(String[] args) {
    MainThread mainThread = new MainThread();
    mainThread.start();
  }

  MainThread() {
    threadName = "Main Thread";
    randomGenerator = ThreadLocalRandom.current();
    userIdCounter = 1;
    numUsers = 1;
    playingField = new PlayingField(10,10,100,500);
  }

  public void run() {
    System.out.println("Running " +  threadName );
    System.out.println("Objective is " +  playingField.objective);

    try {
      allUsers = this.initializeUsers(numUsers);
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
      System.out.println("Exiting " + threadName);
      return;
    }

    this.startAllUsers();

    try {
      while (!playingField.isObjectiveComplete()) {
        if (randomGenerator.nextInt(1, 101) >= 80) {
          playingField.scrambleField();
        }
        Thread.sleep(500);
      }
    } catch (InterruptedException e) {
       System.out.println("Thread " +  threadName + " interrupted." );
    }

    System.out.println("Thread " +  threadName + " exiting." );
  }

  public void start() {
    System.out.println("Starting " +  threadName );
    if (t == null) {
       t = new Thread(this, threadName);
       t.start();
    }
  }

  public synchronized UserThread createUser() {
    String userName = "User " + userIdCounter;
    userIdCounter++;

    synchronized(playingField) {
      int[] startPosition = playingField.getFirstEmptyPosition();

      if (startPosition[0] != -1 && startPosition[1] != -1) {
        UserThread ut = new UserThread(userName, playingField, startPosition);
        playingField.placeUserAtPosition(startPosition[0], startPosition[1]);

        System.out.println("Created " + userName);
        return ut;
      }
    }

    return null;
  }

  /**
  * Attempts to create the specified number of users an returns them
  * as an array.
  *
  * @return UserThread[]
  * @throws Exception Throws an exception if the specified number of users could
  * not be created.
  */
  public synchronized UserThread[] initializeUsers(int userCount)
  throws Exception {
    UserThread[] allUsers = new UserThread[userCount];
    for (int i = 0; i < userCount; i++) {
      UserThread newUser = this.createUser();
      if (newUser == null) {
        throw new Exception("Field is full, could not create users.");
      }
      allUsers[i] = newUser;
    }
    return allUsers;
  }

  /**
  * Starts all user threads running.
  */
  public void startAllUsers() {
    synchronized(playingField) {
      for (UserThread i : allUsers) {
        i.start();
      }
    }
  }
}
