package puzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

    public JFrame frame;
    public JPanel panel;

    private int mouseX, mouseY;

    private Dimension screenSize = new Dimension(800, 800);
    private int gridSize = 10;
    private int squareSize = screenSize.height / gridSize;

    private int[][] grid = new int[gridSize][gridSize];

    private boolean end = false;
    private boolean levelEditor = false;
    private boolean win = false;

    private int keyDown = -1;

    private int cx, cy;

    public static void main(String[] args) {
        new Main().init();
    }

    private void init() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = 0;
            }
        }

        frame = new JFrame();
        frame.setSize(screenSize);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel = new JPanel(){
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                // paint grid
                for (int i = 0; i < gridSize; i++) {
                    for (int j = 0; j < gridSize; j++) {
                        switch (grid[i][j]) {
                            case 1: g.setColor(new Color(148, 148, 148)); break;
                            case 2: g.setColor(new Color(93, 161, 92)); break;
                            case 3: g.setColor(new Color(203, 127, 120)); break;
                        }
                        if (grid[i][j] != 0) {
                            g.fillRect(i * squareSize, j * squareSize, squareSize, squareSize);
                        }
                    }
                }

                // paint grid lines
                g.setColor(new Color(218, 218, 218));
                for (int i = 0; i < gridSize; i++) {
                    g.drawLine(i * squareSize, 0, i * squareSize, screenSize.height);
                    g.drawLine(0, i * squareSize, screenSize.width, i * squareSize);
                }
            }
        };
        panel.setPreferredSize(screenSize);
        MouseListener mouseListener = new MouseListener() {
            public void mouseClicked(MouseEvent mouseEvent) {}
            public void mousePressed(MouseEvent mouseEvent) {
                mouseX = mouseEvent.getX();
                mouseY = mouseEvent.getY();
                if (levelEditor) {
                    int gridX = (int)((float) mouseX - (mouseX % squareSize)) / squareSize;
                    int gridY = (int)((float) mouseY - (mouseY % squareSize)) / squareSize;
                    if (grid[gridX][gridY] == 1) {
                        grid[gridX][gridY] = 0;
                    } else {
                        grid[gridX][gridY] = 1;
                    }
                    if (keyDown == KeyEvent.VK_1) {
                        grid[gridX][gridY] = 2;
                        cx = gridX;
                        cy = gridY;
                    }
                    if (keyDown == KeyEvent.VK_2) {
                        grid[gridX][gridY] = 3;
                    }
                }
            }
            public void mouseReleased(MouseEvent mouseEvent) {}
            public void mouseEntered(MouseEvent mouseEvent) {}
            public void mouseExited(MouseEvent mouseEvent) {}
        };
        MouseMotionListener mouseMotionListener = new MouseMotionListener() {
            public void mouseDragged(MouseEvent mouseEvent) {}
            public void mouseMoved(MouseEvent mouseEvent) {}
        };
        KeyListener keyListener = new KeyListener() {
            public void keyTyped(KeyEvent keyEvent) {}
            public void keyPressed(KeyEvent keyEvent) {
                keyDown = keyEvent.getKeyCode();
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE: end = true; break;
                    case KeyEvent.VK_L: levelEditor = !levelEditor; break;
                    case KeyEvent.VK_P: print();
//                    end = true;
                    break;
                    case KeyEvent.VK_E: loadLevel(Levels.LEVEL_1); break;
                    case KeyEvent.VK_W: updatePuzzle(KeyEvent.VK_W); break;
                    case KeyEvent.VK_A: updatePuzzle(KeyEvent.VK_A); break;
                    case KeyEvent.VK_S: updatePuzzle(KeyEvent.VK_S); break;
                    case KeyEvent.VK_D: updatePuzzle(KeyEvent.VK_D); break;
                    case KeyEvent.VK_ENTER: System.out.println(cx + ", " + cy); break;
                }
            }
            public void keyReleased(KeyEvent keyEvent) {
                keyDown = -1;
            }
        };
        panel.addMouseListener(mouseListener);
        panel.addMouseMotionListener(mouseMotionListener);
        frame.addKeyListener(keyListener);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        panel.repaint();

        this.run();
    }

    private void updatePuzzle(int buttonInput) {
        int[] newPos = new int[2];
        switch (buttonInput) {
            case KeyEvent.VK_W:
                newPos = movePlayer(cx, cy, 0,  -1);
                break;
            case KeyEvent.VK_A:
                newPos = movePlayer(cx, cy,-1,   0);
                break;
            case KeyEvent.VK_S:
                newPos = movePlayer(cx, cy,  0,  1);
                break;
            case KeyEvent.VK_D:
                newPos = movePlayer( cx, cy, 1,  0);
                break;
        }
        grid[cx][cy] = 0;
        grid[newPos[0]][newPos[1]] = 2;
        cx = newPos[0];
        cy = newPos[1];
    }

    private int[] movePlayer(int x, int y, int dx, int dy) {
        int newX = x + dx;
        int newY = y + dy;
        if (newX < 0 || newX >= gridSize || newY < 0 || newY >= gridSize || grid[newX][newY] == 1) {
            return new int[]{x, y};
        } else if (grid[newX][newY] == 3) {
            win = true;
            return new int[]{x, y};
        } else {
            return movePlayer(x + dx, y + dy, dx, dy);
        }
    }

    private void run() {
        while (!end) {
            panel.repaint();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        frame.dispose();
    }

    private void loadLevel(String level) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int num = Integer.parseInt(String.valueOf(level.charAt(gridSize * i + j)));
                grid[j][i] = num;
                if (num == 2) {
                    cx = j;
                    cy = i;
                }
            }
        }
    }

    private void print() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                System.out.print(grid[j][i]);
            }
        }
        System.out.println();
    }

}