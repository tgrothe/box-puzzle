import java.util.Arrays;

public class Box {
    final int len = 5;
    final int empty = -1;
    final int max;
    final int[][][] solution = new int[len][len][len];
    final int[][] candidates = {
            {2, 2, 3},
            {1, 2, 4},
            {1, 1, 1},
    };
    final int[] taken = new int[candidates.length];
    final int[] indices = new int[candidates.length];
    final int[] xyz = new int[3];
    int freeSpace = len * len * len;

    Box(final int maxDepth, final boolean withMagicCuboid) {
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                for (int k = 0; k < len; k++) {
                    solution[i][j][k] = empty;
                }
            }
        }
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }

        this.max = maxDepth;
        if (withMagicCuboid) {
            // insert magic cuboid
            solution[2][2][2] = 2;
            taken[2]++;
            freeSpace--;
        }
    }

    void fillCoordinates(final int c, final int dir, final int x, final int y, final int z, final int i, final int j, final int k) {
        xyz[0] = x;
        xyz[1] = y;
        xyz[2] = z;
        if (c == 2) {
            xyz[0] += i;
            xyz[1] += j;
            xyz[2] += k;
        } else {
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
    }

    boolean isInsertable(final int c, final int x, final int y, final int z, final int dir) {
        if (taken[c] >= max) {
            return false;
        }
        int[] a = candidates[c];
        for (int i = 0; i < a[0]; i++) {
            for (int j = 0; j < a[1]; j++) {
                for (int k = 0; k < a[2]; k++) {
                    fillCoordinates(c, dir, x, y, z, i, j, k);
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

    void insert1(final int c, final int x, final int y, final int z, final int dir) {
        int[] a = candidates[c];
        for (int i = 0; i < a[0]; i++) {
            for (int j = 0; j < a[1]; j++) {
                for (int k = 0; k < a[2]; k++) {
                    fillCoordinates(c, dir, x, y, z, i, j, k);
                    solution[xyz[0]][xyz[1]][xyz[2]] = c;
                    freeSpace--;
                }
            }
        }
        taken[c]++;
    }

    boolean insert(final int c, final int x, final int y, final int z, final int dir) {
        if (isInsertable(c, x, y, z, dir)) {
            insert1(c, x, y, z, dir);
            return true;
        }
        return false;
    }

    void remove(final int c, final int x, final int y, final int z, final int dir) {
        int[] a = candidates[c];
        for (int i = 0; i < a[0]; i++) {
            for (int j = 0; j < a[1]; j++) {
                for (int k = 0; k < a[2]; k++) {
                    fillCoordinates(c, dir, x, y, z, i, j, k);
                    solution[xyz[0]][xyz[1]][xyz[2]] = empty;
                    freeSpace++;
                }
            }
        }
        taken[c]--;
    }

    boolean isSolved() {
        return freeSpace == 0;
    }

    boolean isSolvable() {
        return freeSpace > 0;
    }

    void startSolving(final boolean nextRandom) {
        solve(0, 0, nextRandom);
    }

    boolean solve(final int depth, final int current, final boolean nextRandom) {
        if (depth >= max || current >= candidates.length) {
            return isSolved();
        }
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                for (int k = 0; k < len; k++) {
                    if (current == 2) {
                        // take an abbreviation
                        if (insert(current, i, j, k, 0)) {
                            if (isSolved()) {
                                return true;
                            }
                            if (isSolvable()) {
                                if (nextRandom) {
                                    if (solve(depth + 1, (int) (Math.random() * candidates.length), true)) {
                                        return true;
                                    }
                                } else {
                                    if (solve(depth + 1, current, false)) {
                                        return true;
                                    }
                                    if (solve(depth + 1, current + 1, false)) {
                                        return true;
                                    }
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
                                if (isSolvable()) {
                                    if (nextRandom) {
                                        if (solve(depth + 1, (int) (Math.random() * candidates.length), true)) {
                                            return true;
                                        }
                                    } else {
                                        if (solve(depth + 1, current, false)) {
                                            return true;
                                        }
                                        if (solve(depth + 1, current + 1, false)) {
                                            return true;
                                        }
                                    }
                                }
                                remove(current, i, j, k, d);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void solveAndPrint(final int maxDepth, final boolean withMagicCuboid, final boolean nextRandom) {
        System.out.println("Start solving with: maxDepth = " + maxDepth + ", withMagicCuboid = " + withMagicCuboid + ", nextRandom = " + nextRandom);
        Box box = new Box(maxDepth, withMagicCuboid);
        box.startSolving(nextRandom);
        System.out.println(box.isSolved());
        System.out.println(Arrays.toString(box.taken));
        System.out.println(Arrays.deepToString(box.solution));
    }

    public static void main(final String[] args) {
        solveAndPrint(0, true, true);
        solveAndPrint(1, false, false);
        solveAndPrint(2, true, false);
        solveAndPrint(3, false, true);

        // The problematic cases:
        solveAndPrint(5, true, false);

        solveAndPrint(6, true, true);

        solveAndPrint(7, true, true);

        solveAndPrint(10, true, true);

        solveAndPrint(20, true, true);

        solveAndPrint(23, true, true);

        solveAndPrint(4, true, false);
    }
}
