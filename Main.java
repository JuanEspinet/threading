class MainThread extends Thread {
  private Thread t;
  private String threadName;

  public static void main(String[] args) {
    MainThread mainThread = new MainThread();
    mainThread.start();
  }

  MainThread() {
    threadName = "Main Thread";
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
}
