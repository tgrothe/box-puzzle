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
    int freeSpace = len * len * len;

    Box(final int max, final boolean withMagicCuboid) {
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                for (int k = 0; k < len; k++) {
                    solution[i][j][k] = empty;
                }
            }
        }

        this.max = max;
        if (withMagicCuboid) {
            // insert magic cuboid
            solution[2][2][2] = 2;
            taken[2]++;
            freeSpace--;
        }

        solve(0, 0);
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

    boolean isSolution() {
        return freeSpace <= 0;
    }

    boolean hasSolution() {
        return freeSpace > 0;
    }

    boolean solve(final int c, final int n) {
        if (c >= candidates.length || n >= max || !hasSolution()) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                for (int k = 0; k < len; k++) {
                    if (c == 2) {
                        // take an abbreviation
                        if (insert(c, i, j, k, 0)) {
                            if (isSolution()) {
                                return true;
                            }
                            if (hasSolution() && (solve(c, n + 1) || solve(c + 1, 0))) {
                                return true;
                            }
                            remove(c, i, j, k, 0);
                        }
                    } else {
                        // take all directions
                        for (int d = 0; d < 6; d++) {
                            if (insert(c, i, j, k, d)) {
                                if (isSolution()) {
                                    return true;
                                }
                                if (hasSolution() && (solve(c, n + 1) || solve(c + 1, 0))) {
                                    return true;
                                }
                                remove(c, i, j, k, d);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void main(final String[] args) {
        for (int n = 25; n >= 21; n--) {
            System.out.println("n = " + n);
            Box box = new Box(n, true);
            System.out.println(box.isSolution());
            System.out.println(Arrays.toString(box.taken));
            System.out.println(Arrays.deepToString(box.solution));
        }
        // The problematic cases:
        {
            int n = 20;
            System.out.println("n = " + n);
            Box box = new Box(n, true);
            System.out.println(box.isSolution());
            System.out.println(Arrays.toString(box.taken));
            System.out.println(Arrays.deepToString(box.solution));
        }
        for (int n = 1; n <= 6; n++) {
            System.out.println("n = " + n);
            Box box = new Box(n, true);
            System.out.println(box.isSolution());
            System.out.println(Arrays.toString(box.taken));
            System.out.println(Arrays.deepToString(box.solution));
        }
    }
}
