import java.util.Arrays;

public class Box {
  private final int len = 5;
  private final int empty = -1;
  private final int maxCandidates;
  private final boolean withMagicCuboid;
  private final int[][][] solution = new int[len][len][len];
  private final int[][] candidates = {
    {2, 2, 3},
    {1, 2, 4},
    {1, 1, 1},
  };
  private final int[][] randomCandidates = {
    {0, 1, 2},
    {0, 2, 1},
    {1, 0, 2},
    {1, 2, 0},
    {2, 0, 1},
    {2, 1, 0},
  };
  private final int[] available = new int[candidates.length];
  private final int[] xyz = new int[3];
  private int freeSpace = len * len * len;

  public Box(final int maxCandidates, final boolean withMagicCuboid) {
    for (int i = 0; i < len; i++) {
      for (int j = 0; j < len; j++) {
        for (int k = 0; k < len; k++) {
          solution[i][j][k] = empty;
        }
      }
    }

    this.maxCandidates = maxCandidates;
    Arrays.fill(available, maxCandidates);

    this.withMagicCuboid = withMagicCuboid;
    if (withMagicCuboid) {
      // insert magic cuboid
      solution[2][2][2] = getIndexToInsert(2);
      available[2]--;
      freeSpace--;
    }
  }

  private void fillCoordinates(
      final int dir, final int x, final int y, final int z, final int i, final int j, final int k) {
    xyz[0] = x;
    xyz[1] = y;
    xyz[2] = z;
    switch (dir) {
      case 0 -> {
        xyz[0] += i;
        xyz[1] += j;
        xyz[2] += k;
      }
      case 1 -> {
        xyz[0] += i;
        xyz[1] += k;
        xyz[2] += j;
      }
      case 2 -> {
        xyz[0] += j;
        xyz[1] += i;
        xyz[2] += k;
      }
      case 3 -> {
        xyz[0] += j;
        xyz[1] += k;
        xyz[2] += i;
      }
      case 4 -> {
        xyz[0] += k;
        xyz[1] += i;
        xyz[2] += j;
      }
      case 5 -> {
        xyz[0] += k;
        xyz[1] += j;
        xyz[2] += i;
      }
    }
  }

  private boolean isInsertable(final int c, final int x, final int y, final int z, final int dir) {
    if (available[c] <= 0) {
      return false;
    }
    int[] a = candidates[c];
    for (int i = 0; i < a[0]; i++) {
      for (int j = 0; j < a[1]; j++) {
        for (int k = 0; k < a[2]; k++) {
          fillCoordinates(dir, x, y, z, i, j, k);
          for (int l : xyz) {
            if (l < 0 || l >= len) {
              return false;
            }
          }
          if (solution[xyz[0]][xyz[1]][xyz[2]] != empty) {
            return false;
          }
        }
      }
    }
    return true;
  }

  private int getIndexToInsert(final int c) {
    return c * maxCandidates + (maxCandidates - available[c]);
  }

  private void insert1(final int c, final int x, final int y, final int z, final int dir) {
    int toInsert = getIndexToInsert(c);
    int[] a = candidates[c];
    for (int i = 0; i < a[0]; i++) {
      for (int j = 0; j < a[1]; j++) {
        for (int k = 0; k < a[2]; k++) {
          fillCoordinates(dir, x, y, z, i, j, k);
          solution[xyz[0]][xyz[1]][xyz[2]] = toInsert;
          freeSpace--;
        }
      }
    }
    available[c]--;
  }

  private boolean insert(final int c, final int x, final int y, final int z, final int dir) {
    if (isInsertable(c, x, y, z, dir)) {
      insert1(c, x, y, z, dir);
      return true;
    }
    return false;
  }

  private void remove(final int c, final int x, final int y, final int z, final int dir) {
    int[] a = candidates[c];
    for (int i = 0; i < a[0]; i++) {
      for (int j = 0; j < a[1]; j++) {
        for (int k = 0; k < a[2]; k++) {
          fillCoordinates(dir, x, y, z, i, j, k);
          solution[xyz[0]][xyz[1]][xyz[2]] = empty;
          freeSpace++;
        }
      }
    }
    available[c]++;
  }

  private boolean isSolved() {
    return freeSpace == 0;
  }

  private boolean isProbableSolvable(final int level) {
    if (freeSpace <= 0) {
      return false;
    }
    int i = level - 1;
    if (i >= 0) {
      // for (int i = 0; i < level; i++) {
      for (int j = 0; j < len; j++) {
        for (int k = 0; k < len; k++) {
          if (solution[i][j][k] == empty) {
            return false;
          }
        }
      }
      // }
    }
    return true;
  }

  private boolean solve(final int index, final boolean nextRandom) {
    if (index >= len * len * len) {
      return isSolved();
    }
    final int i = index / len / len;
    final int j = index / len % len;
    final int k = index % len;
    if (!isProbableSolvable(i)) {
      return false;
    }

    if (withMagicCuboid && i == 2 && j == 2 && k == 2) {
      // skip this step
      return solve(index + 1, nextRandom);
    }

    if (nextRandom) {
      int r = (int) (Math.random() * randomCandidates.length);
      for (int ri = 0; ri < candidates.length; ri++) {
        int current = randomCandidates[r][ri];
        if (current == 2) {
          // take an abbreviation
          if (insert(current, i, j, k, 0)) {
            if (isSolved()) {
              return true;
            }
            if (isProbableSolvable(i)) {
              if (solve(index + 1, true)) {
                return true;
              }
            }
            remove(current, i, j, k, 0);
          }
        } else {
          // take all directions
          for (int d = 0; d < 6; d++) {
            if (insert(current, i, j, k, d)) {
              if (isSolved()) {
                return true;
              }
              if (isProbableSolvable(i)) {
                if (solve(index + 1, true)) {
                  return true;
                }
              }
              remove(current, i, j, k, d);
            }
          }
        }
      }
    } else {
      for (int current = 0; current < candidates.length; current++) {
        if (current == 2) {
          // take an abbreviation
          if (insert(current, i, j, k, 0)) {
            if (isSolved()) {
              return true;
            }
            if (isProbableSolvable(i)) {
              if (solve(index + 1, false)) {
                return true;
              }
            }
            remove(current, i, j, k, 0);
          }
        } else {
          // take all directions
          for (int d = 0; d < 6; d++) {
            if (insert(current, i, j, k, d)) {
              if (isSolved()) {
                return true;
              }
              if (isProbableSolvable(i)) {
                if (solve(index + 1, false)) {
                  return true;
                }
              }
              remove(current, i, j, k, d);
            }
          }
        }
      }
    }

    // May we find a solution by leaving this index empty?
    return solve(index + 1, nextRandom);
  }

  public void startSolving(final boolean nextRandom) {
    solve(0, nextRandom);
  }

  public void printInfo() {
    System.out.println("len             = " + len);
    System.out.println("maxCandidates   = " + maxCandidates);
    System.out.println("withMagicCuboid = " + withMagicCuboid);
    System.out.println("candidates      = " + Arrays.deepToString(candidates));
    System.out.println("available       = " + Arrays.toString(available));
    System.out.println("isSolved        = " + isSolved());
    System.out.println("solution        = ");
    for (int[][] ints : solution) {
      System.out.println(Arrays.deepToString(ints));
    }
  }

  public static void solveAndPrint(
      final int maxCandidates, final boolean withMagicCuboid, final boolean nextRandom) {
    System.out.println(
        "Start solving with: maxCandidates = "
            + maxCandidates
            + ", withMagicCuboid = "
            + withMagicCuboid
            + ", nextRandom = "
            + nextRandom);
    Box box = new Box(maxCandidates, withMagicCuboid);
    box.startSolving(nextRandom);
    box.printInfo();
  }

  public static void main(final String[] args) {
    solveAndPrint(15, true, true);
    solveAndPrint(6, true, true);
  }
}
