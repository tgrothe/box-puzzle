import java.util.Arrays;

/**
 * The Box class represents a 3D box puzzle solver.
 * It attempts to fill a 5x5x5 box with smaller boxes of different dimensions.
 * The class supports an optional "magic" box that is always placed in the center.
 * The solving process uses backtracking.
 */
public class Box {
  /**
     * The length of the box.
     */
    private static final int len = 5;

    /**
     * The value representing an empty space in the box.
     */
    private static final int empty = -1;

    /**
     * The maximum number of candidates for each type of smaller box.
     */
    private final int maxCandidates;

    /**
     * Indicates whether the box includes a "magic" cuboid.
     */
    private final boolean withMagicCuboid;

    /**
     * The 3D array representing the solution.
     */
    private final int[][][] solution = new int[len][len][len];

    /**
     * The array of candidate smaller boxes.
     */
    private final int[][] candidates = {
            {2, 2, 3},
            {1, 2, 4},
            {1, 1, 1},
    };

    /**
     * The array of random candidate orders.
     */
    private final int[][] randomCandidates = {
            {0, 1, 2},
            {0, 2, 1},
            {1, 0, 2},
            {1, 2, 0},
            {2, 0, 1},
            {2, 1, 0},
    };

    /**
     * The array representing the number of available smaller boxes for each type.
     */
    private final int[] available = new int[candidates.length];

    /**
     * The array representing the coordinates of a box.
     */
    private final int[] xyz = new int[3];

    /**
     * The number of free spaces left in the box.
     */
  private int freeSpace = len * len * len;

  /**
     * Constructs a Box object with the specified maximum number of candidates and whether it includes a magic cuboid.
     *
     * @param maxCandidates   The maximum number of candidates for each type of smaller box.
     * @param withMagicCuboid Indicates whether the box includes a "magic" cuboid.
     */
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

  /**
     * Fills the coordinates array with the coordinates of a box.
     *
     * @param dir The direction of the box.
     * @param x   The x-coordinate of the box.
     * @param y   The y-coordinate of the box.
     * @param z   The z-coordinate of the box.
     * @param i   The x-offset of the box.
     * @param j   The y-offset of the box.
     * @param k   The z-offset of the box.
     */
    private void fillCoordinates(final int dir, final int x, final int y, final int z, final int i, final int j, final int k) {
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
            }default -> throw new IllegalArgumentException("Invalid direction: " + dir);
    }
  }

  /**
     * Checks if a box can be inserted at the specified coordinates and direction.
     *
     * @param c   The index of the candidate box.
     * @param x   The x-coordinate of the box.
     * @param y   The y-coordinate of the box.
     * @param z   The z-coordinate of the box.
     * @param dir The direction of the box.
     * @return True if the box can be inserted, false otherwise.
     */
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

  /**
     * Gets the index to insert a box.
     *
     * @param c The index of the candidate box.
     * @return The index to insert the box.
     */
    private int getIndexToInsert(final int c) {
        return c * maxCandidates + (maxCandidates - available[c]);
    }

  /**
     * Inserts a box at the specified coordinates and direction.
     *
     * @param c   The index of the candidate box.
     * @param x   The x-coordinate of the box.
     * @param y   The y-coordinate of the box.
     * @param z   The z-coordinate of the box.
     * @param dir The direction of the box.
     */
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

  /**
     * Attempts to insert a box at the specified coordinates and direction.
     *
     * @param c   The index of the candidate box.
     * @param x   The x-coordinate of the box.
     * @param y   The y-coordinate of the box.
     * @param z   The z-coordinate of the box.
     * @param dir The direction of the box.
     * @return True if the box was successfully inserted, false otherwise.
     */
    private boolean insert(final int c, final int x, final int y, final int z, final int dir) {
        if (isInsertable(c, x, y, z, dir)) {
            insert1(c, x, y, z, dir);
            return true;
        }
        return false;
    }

  /**
     * Removes a box from the specified coordinates and direction.
     *
     * @param c   The index of the candidate box.
     * @param x   The x-coordinate of the box.
     * @param y   The y-coordinate of the box.
     * @param z   The z-coordinate of the box.
     * @param dir The direction of the box.
     */
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

  /**
     * Checks if the box is completely filled.
     *
     * @return True if the box is completely filled, false otherwise.
     */
    private boolean isSolved() {
        return freeSpace == 0;
    }

  /**
     * Checks if the box is probably solvable at the specified level.
     *
     * @param level The level to check.
     * @return True if the box is probably solvable, false otherwise.
     */
    private boolean isProbableSolvable(final int level) {
        if (freeSpace <= 0) {
            return false;
        }
        int i = level - 1;
        if (i >= 0) {
            //for (int i = 0; i < level; i++) {
            for (int j = 0; j < len; j++) {
                for (int k = 0; k < len; k++) {
                    if (solution[i][j][k] == empty) {
                        return false;
                    }
                }
            }
            //}
        }
        return true;
    }

  /**
     * Solves the box puzzle using backtracking.
     *
     * @param index      The current index.
     * @param nextRandom Indicates whether to use random candidate order.
     * @return True if the box puzzle is solved, false otherwise.
     */
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

  /**
     * Starts solving the box puzzle.
     *
     * @param nextRandom Indicates whether to use random candidate order.
     */
    public void startSolving(final boolean nextRandom) {
        solve(0, nextRandom);
    }

  /**
     * Prints information about the box puzzle.
     */
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

  /**
     * Solves the box puzzle and prints the solution.
     *
     * @param maxCandidates   The maximum number of candidates for each type of smaller box.
     * @param withMagicCuboid Indicates whether the box includes a "magic" cuboid.
     * @param nextRandom      Indicates whether to use random candidate order.
     */
    public static void solveAndPrint(final int maxCandidates, final boolean withMagicCuboid, final boolean nextRandom) {
        System.out.println("Start solving with: maxCandidates = " + maxCandidates + ", withMagicCuboid = " + withMagicCuboid + ", nextRandom = " + nextRandom);
        Box box = new Box(maxCandidates, withMagicCuboid);
        box.startSolving(nextRandom);
        box.printInfo();
    }

  /**
     * The main method to run the box puzzle solver.
     *
     * @param args The command-line arguments.
     */
    public static void main(final String[] args) {
        solveAndPrint(15, true, true);
        solveAndPrint(6, true, true);
    }
}
