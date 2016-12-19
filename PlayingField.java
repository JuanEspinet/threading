import java.util.concurrent.ThreadLocalRandom;

public class PlayingField {
  private volatile int[][] field;
  private final int maxVal;

  public volatile boolean userPositions[][];

  public ThreadLocalRandom randomGenerator;

  public PlayingField(int x, int y, int max) {
    randomGenerator = ThreadLocalRandom.current();
    field = generateField(x, y);
    userPositions = boolean[x][y];
    maxVal = max + 1;
    this.scrambleField();
  }

  public int[][] generateField(int x, int y) {
    return new int[x][y];
  }

  public synchronized void scrambleField() {
    for (int i = 0; i < field.length; i++) {
      for (int j = 0; j < field[i].length; j++) {
        int newVal = randomGenerator.nextInt(maxVal);
        this.setValueByCoord(i,j,newVal);
      }
    }
  }

  public synchronized int getFieldLength() {
    return field.length;
  }

  public synchronized int getFieldWidth() {
    return field[0].length;
  }

  public synchronized int getValuesByCoord(int x, int y) {
    return field[x][y];
  }

  public synchronized int setValueByCoord(int x, int y, int value) {
    field[x][y] = value;
    return this.getValuesByCoord(x,y);
  }

  public synchronized int getAndResetValue(int x, int y) {
    int value = this.getValuesByCoord(x, y);
    this.setValueByCoord(x, y, 0);
    return value;
  }
}
