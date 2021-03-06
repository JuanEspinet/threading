import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;

class MainThread extends Thread {
  private Thread t;
  private String threadName;

  private ThreadLocalRandom randomGenerator;

  private int userIdCounter;
  private int numUsers;

  private int fieldX;
  private int fieldY;
  private int maxVal;
  private int objective;
  public PlayingField playingField;

  private volatile UserThread[] allUsers;

  private static String bar = "=============================";

  public static void main(String[] args) {
    MainThread mainThread = new MainThread();
    mainThread.start();
  }

  MainThread() {
    threadName = "Main Thread";
    randomGenerator = ThreadLocalRandom.current();
    userIdCounter = 1;
    maxVal = 100;
  }

  public void initializeForTest() {
    numUsers = 5;
    fieldX = 10;
    fieldY = 10;
    objective = 200;
    playingField = new PlayingField(fieldX,fieldY,maxVal,objective);
  }

  public void run() {
    this.getUserDefinedValues();

    System.out.println("Running " +  threadName );
    System.out.println("Objective is " +  objective);
    String fieldSize = fieldX + "x" + fieldY;
    System.out.println("The field is " + fieldSize + ".");
    System.out.println("The max value per position is " + maxVal + ".");

    try {
      System.out.println("Creating " + numUsers + " simulated users.");
      System.out.println(this.bar);
      allUsers = this.initializeUsers(numUsers);
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
      System.out.println("Exiting " + threadName);
      return;
    }

    try {
      ThreadMonitor threadMonitor = new ThreadMonitor(this.allUsers);
      threadMonitor.setMonitorActive(true);
      threadMonitor.start();

      System.out.println("Simulation will start in 10 seconds...");
      Thread.sleep(10000);

      this.startAllUsers();

      while (!playingField.isObjectiveComplete()) {
        if (randomGenerator.nextInt(1, 101) >= 80) {
          playingField.scrambleField();
          System.out.println("Randomly scrambling field values.");
        }
        Thread.sleep(1000);
      }

      if (playingField.isObjectiveComplete()) {
        UserThread winningUser = playingField.getWinningUser();
        String winner = winningUser.getUserName();

        System.out.println(bar);
        System.out.println(winner + " reached the objective first with "
          + winningUser.getProcessed());
        System.out.println(bar);

        this.waitForAllUsers();

        Thread.sleep(500);

        threadMonitor.setMonitorActive(false);

        System.out.println(bar);
        System.out.println(winner + " was first to finish with "
          + winningUser.getProcessed());
        System.out.println(bar);
        System.out.println("The simulation is complete!");
      }
    } catch (InterruptedException e) {
       System.out.println("Thread " +  threadName + " interrupted." );
    }



    System.out.println("Thread " +  threadName + " exiting." );
  }

  public synchronized UserThread createUser() {
    String userName = "User " + userIdCounter;
    userIdCounter++;

    synchronized(playingField) {
      int[] startPosition = playingField.getFirstEmptyPosition();

      if (startPosition[0] != -1 && startPosition[1] != -1) {
        String startLoc = "("+startPosition[0]+","+startPosition[1]+")";
        UserThread ut = new UserThread(userName, playingField, startPosition);
        playingField.placeUserAtPosition(startPosition[0], startPosition[1]);

        System.out.println("Created " + userName + " at " + startLoc);
        return ut;
      }
    }

    return null;
  }

  public void getUserDefinedValues() {
    Scanner scan = new Scanner(System.in);

    System.out.println("Enter number of users (as int): ");
    numUsers = scan.nextInt();

    System.out.println("Enter field X dimension (as int): ");
    fieldX = scan.nextInt();

    System.out.println("Enter field Y dimension (as int): ");
    fieldY = scan.nextInt();

    System.out.println("Enter objective (as int): ");
    objective = scan.nextInt();

    playingField = new PlayingField(fieldX,fieldY,maxVal,objective);
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

  public void waitForAllUsers() {
    for (UserThread ut : allUsers) {
      if (ut.isAlive()) {
        try {
          System.out.println("Waiting for one or more users...");
          ut.join();
        } catch (InterruptedException e) {
          System.out.println("Waiting interrupted.");
        }
      }
    }
  }
}

class ThreadMonitor extends Thread {
  private UserThread[] allUsers;
  public volatile boolean monitorActive;

  public ThreadMonitor(UserThread[] users) {
    allUsers = users;
    monitorActive = false;
  }

  public void run() {
    System.out.println("Thread Monitor starting.");
    while (monitorActive) {
      try {
        this.displayThreadsWithStatus(Thread.State.BLOCKED);
        this.displayThreadsWithStatus(Thread.State.WAITING);
        Thread.sleep(50);
      } catch (InterruptedException exception) {
        System.out.println("Thread Monitor has been interrupted.");
        return;
      }
    }
    System.out.println("Thread Monitor exiting.");
  }

  public synchronized void setMonitorActive(boolean activeState) {
    monitorActive = activeState;
  }

  public void displayThreadsWithStatus(Thread.State status) {
    for (UserThread ut : allUsers) {
      Thread.State threadState = ut.getState();
      if (threadState == status) {
        System.out.println("Thread Monitor says " + ut.getUserName() + " is " + threadState);
      }
    }
  }
}
