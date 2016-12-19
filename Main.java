class MainLoop {
  public static void main(String[] args) {
    System.out.println("It works!");
  }
}

class MainThread extends Thread {
  private Thread t;
  private String threadName;

  MainThread() {
    threadName = "Main Thread";
  }

  public void run() {
    System.out.println("Running " +  threadName );
    try {
      for (int i = 0; i < 5; i++) {
        System.out.println(threadName + " printing loop iteration " + i );
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
}
