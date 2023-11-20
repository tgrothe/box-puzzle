import javax.swing.*;
import java.awt.*;

public class Render {
    private static Color getColor(final int index, final int level) {
        float a = (5 - level) / 5f;
        return switch (index) {
            case 0 -> new Color(1f, 0.5f, 0f, a);
            case 1 -> new Color(1f, 1f, 0f, a);
            case 2 -> new Color(0f, 0f, 1f, a);
            default -> null;
        };
    }

    public Render(final int[][][] solution, final int maxCandidates) {
        int widthAndHeight = 800;

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(widthAndHeight, widthAndHeight);

        frame.add(new Canvas() {
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                int len = 150;
                int len2 = 15;
                int len3 = 140;
                for (int i = 4; i >= 0; i--) {
                    for (int j = 0; j < 5; j++) {
                        for (int k = 0; k < 5; k++) {
                            int x = k * len3 + i * len2 + (getWidth() - 695) / 2;
                            int y = j * len3 + i * len2 + (getHeight() - 695) / 2;
                            int x2 = x + len / 2;
                            int y2 = y + len / 2;
                            Color color = getColor(solution[i][j][k] / maxCandidates, i);
                            g.setColor(color);
                            g.fillRect(x, y, x2 - x, y2 - y);
                            g.setColor(Color.GRAY);
                            g.drawString(String.valueOf(solution[i][j][k]), x2 - 15, y + 10);
                        }
                    }
                }
            }
        });

        frame.setVisible(true);
    }

    public static void main(final String[] args) {
        Render render = new Render(new int[][][]{
                {{13, 6, 6, 6, 6}, {7, 6, 6, 6, 6}, {7, 0, 0, 1, 1}, {7, 0, 0, 1, 1}, {7, 0, 0, 8, 8}},
                {{9, 9, 2, 2, 2}, {7, 14, 2, 2, 2}, {7, 0, 0, 1, 1}, {7, 0, 0, 1, 1}, {7, 0, 0, 8, 8}},
                {{9, 9, 2, 2, 2}, {3, 3, 2, 2, 2}, {3, 3, 12, 1, 1}, {4, 4, 4, 1, 1}, {4, 4, 4, 8, 8}},
                {{9, 9, 5, 5, 10}, {3, 3, 5, 5, 10}, {3, 3, 5, 5, 10}, {4, 4, 4, 15, 10}, {4, 4, 4, 8, 8}},
                {{9, 9, 5, 5, 10}, {3, 3, 5, 5, 10}, {3, 3, 5, 5, 10}, {11, 11, 11, 11, 10}, {11, 11, 11, 11, 16}},
        }, 6);
    }
}
