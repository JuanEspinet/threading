class MainThread extends Thread {
  private Thread t;
  private String threadName;

  private int userIdCounter;

  private PlayingField playingField;

  public static void main(String[] args) {
    MainThread mainThread = new MainThread();
    mainThread.start();
  }

  MainThread() {
    threadName = "Main Thread";
    userIdCounter = 1;
    playingField = new PlayingField(10,10,100,500);
  }

  public void run() {
    System.out.println("Running " +  threadName );
    System.out.println("Running " +  threadName );
    try {

      Thread.sleep(500);

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

    synchronized(playingField) {
      int[] startPosition = playingField.getFirstEmptyPosition();
      if (startPosition[0] != -1 && startPosition[1] != -1) {
        UserThread ut = new UserThread(userName, playingField, startPosition);
        System.out.println("Created " + userName);
        return ut;
      }
    }

    System.out.println("Field is full, could not create " + userName);
    return null;
  }
}
