import java.util.concurrent.ThreadLocalRandom;

public class PlayingField {
  private volatile int[][] field;
  private final int maxVal;

  private volatile boolean[][] userPositions;

  public ThreadLocalRandom randomGenerator;

  public PlayingField(int x, int y, int max) {
    randomGenerator = ThreadLocalRandom.current();
    field = generateField(x, y);
    userPositions = new boolean[x][y];
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

  public synchronized void resetValue(int x, int y) {
    this.setValueByCoord(x, y, 0);
  }

  public synchronized int[] getFirstEmptyPosition() {
    int[] coords = {-1, -1};

    for (int i = 0; i < userPositions.length; i++) {
      for (int j = 0; j< userPositions[i].length; j++) {
        if (!userPositions[i][j]) {
          coords[0] = i;
          coords[1] = j;
          return coords;
        }
      }
    }

    return coords;
  }

  public synchronized boolean isPositionOccupied(int x, int y) {
    return userPositions[x][y];
  }

  public synchronized void placeUserAtPosition(int x, int y) {
    userPositions[x][y] = true;
  }

  public synchronized void emptyPosition(int x, int y) {
    userPositions[x][y] = false;
  }

  public synchronized int[] placeNewUser() {
    int[] firstOpen = this.getFirstEmptyPosition();

    if (firstOpen[0] == -1) {
      return firstOpen;
    }

    this.placeUserAtPosition(firstOpen[0], firstOpen[1]);

    return firstOpen;
  }

  public synchronized boolean isValidMove(int[] newPosition, int[] oldPosition) {
    try {
      boolean spacesAdjacent = (Math.abs(newPosition[0] - oldPosition[0]) <= 1)
        && (Math.abs(newPosition[1] - oldPosition[1]) <= 1);

      boolean newOccupied = this.isPositionOccupied(newPosition[0], newPosition[1]);

      return spacesAdjacent && !newOccupied;
    } catch (ArrayIndexOutOfBoundsException exception) {
      return false;
    }
  }

  public synchronized boolean moveUserToPosition(int[] newPosition, int[] oldPosition) {
    if (this.isValidMove(newPosition, oldPosition)) {
      this.emptyPosition(oldPosition[0], oldPosition[1]);
      this.placeUserAtPosition(newPosition[0], newPosition[1]);
      return true;
    }
    return false;
  }
}
