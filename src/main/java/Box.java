import java.util.Arrays;

public class Box {
    final int len = 5;
    final int empty = -1;
    final int max;
    int[][][] solution = new int[len][len][len];
    int[][] candidates = {
            {2, 2, 3},
            {1, 2, 4},
            {1, 1, 1},
    };
    int[] taken = new int[candidates.length];
    int[] indices = new int[candidates.length];
    int freeSpace = len * len * len;

    Box(final int max, final boolean withMagicCuboid) {
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

        this.max = max;
        if (withMagicCuboid) {
            // insert magic cuboid
            solution[2][2][2] = 2;
            taken[2]++;
            freeSpace--;
        }
    }

    boolean insert(final int c, final int x, final int y, final int z, final int dir) {
        if (taken[c] >= max) {
            return false;
        }
        int[] a = candidates[c];
        for (int i = 0; i < a[0]; i++) {
            for (int j = 0; j < a[1]; j++) {
                for (int k = 0; k < a[2]; k++) {
                    int x2 = x;
                    int y2 = y;
                    int z2 = z;
                    if (dir == 0) {
                        x2 += i;
                        y2 += j;
                        z2 += k;
                    }
                    if (dir == 1) {
                        x2 += i;
                        y2 += k;
                        z2 += j;
                    }
                    if (dir == 2) {
                        x2 += j;
                        y2 += i;
                        z2 += k;
                    }
                    if (dir == 3) {
                        x2 += j;
                        y2 += k;
                        z2 += i;
                    }
                    if (dir == 4) {
                        x2 += k;
                        y2 += i;
                        z2 += j;
                    }
                    if (dir == 5) {
                        x2 += k;
                        y2 += j;
                        z2 += i;
                    }
                    if (x2 < 0 || x2 >= len || y2 < 0 || y2 >= len || z2 < 0 || z2 >= len) {
                        return false;
                    }
                    if (solution[x2][y2][z2] != empty) {
                        return false;
                    }
                }
            }
        }
        for (int i = 0; i < a[0]; i++) {
            for (int j = 0; j < a[1]; j++) {
                for (int k = 0; k < a[2]; k++) {
                    int x2 = x;
                    int y2 = y;
                    int z2 = z;
                    if (dir == 0) {
                        x2 += i;
                        y2 += j;
                        z2 += k;
                    }
                    if (dir == 1) {
                        x2 += i;
                        y2 += k;
                        z2 += j;
                    }
                    if (dir == 2) {
                        x2 += j;
                        y2 += i;
                        z2 += k;
                    }
                    if (dir == 3) {
                        x2 += j;
                        y2 += k;
                        z2 += i;
                    }
                    if (dir == 4) {
                        x2 += k;
                        y2 += i;
                        z2 += j;
                    }
                    if (dir == 5) {
                        x2 += k;
                        y2 += j;
                        z2 += i;
                    }
                    solution[x2][y2][z2] = c;
                    freeSpace--;
                }
            }
        }
        taken[c]++;
        return true;
    }

    void remove(final int c, final int x, final int y, final int z, final int dir) {
        int[] a = candidates[c];
        for (int i = 0; i < a[0]; i++) {
            for (int j = 0; j < a[1]; j++) {
                for (int k = 0; k < a[2]; k++) {
                    int x2 = x;
                    int y2 = y;
                    int z2 = z;
                    if (dir == 0) {
                        x2 += i;
                        y2 += j;
                        z2 += k;
                    }
                    if (dir == 1) {
                        x2 += i;
                        y2 += k;
                        z2 += j;
                    }
                    if (dir == 2) {
                        x2 += j;
                        y2 += i;
                        z2 += k;
                    }
                    if (dir == 3) {
                        x2 += j;
                        y2 += k;
                        z2 += i;
                    }
                    if (dir == 4) {
                        x2 += k;
                        y2 += i;
                        z2 += j;
                    }
                    if (dir == 5) {
                        x2 += k;
                        y2 += j;
                        z2 += i;
                    }
                    solution[x2][y2][z2] = empty;
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

    void startSolving() {
        solve(0);
    }

    boolean solve(final int n) {
        if (n >= max) {
            return false;
        }
        int[] cs = indices;
        if (n == 0) {
            cs = new int[candidates.length];
            for (int i = 0; i < cs.length; i++) {
                cs[i] = i;
            }
            for (int i = 0; i < cs.length; i++) {
                int r = (int) (Math.random() * cs.length);
                int t = cs[i];
                cs[i] = cs[r];
                cs[r] = t;
            }
        }
        for (final int current : cs) {
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    for (int k = 0; k < len; k++) {
                        if (current == 2) {
                            // take a abbreviation
                            if (insert(current, i, j, k, 0)) {
                                if (isSolved()) {
                                    return true;
                                }
                                if (isSolvable() && solve(n + 1)) {
                                    return true;
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
                                    if (isSolvable() && solve(n + 1)) {
                                        return true;
                                    }
                                    remove(current, i, j, k, d);
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void solveAndPrint(final int n, final boolean withMagicCuboid) {
        System.out.println("Start solving with " + n + " depth and " + (withMagicCuboid ? "magic cuboid" : "no magic cuboid"));
        Box box = new Box(n, withMagicCuboid);
        box.startSolving();
        System.out.println(box.isSolved());
        System.out.println(Arrays.toString(box.taken));
        System.out.println(Arrays.deepToString(box.solution));
    }

    public static void main(final String[] args) {
        solveAndPrint(0, false);
        solveAndPrint(1, false);
        solveAndPrint(2, false);
        solveAndPrint(3, false);

        // The problematic cases:
        solveAndPrint(4, false);
        solveAndPrint(5, false);
        solveAndPrint(6, false);

        solveAndPrint(23, false);
        solveAndPrint(20, false);
    }
}
