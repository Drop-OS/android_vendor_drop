package com.adinkwok.GameOn;

import com.adinkwok.GameOn.models.Enemy;
import com.adinkwok.GameOn.models.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Game extends JPanel implements KeyListener {
    private static final int SPEED = 5;

    private int dragonsRemaining;
    private JFrame jFrame;
    private MainMenu mainMenu;

    private int x, y;
    private int[] laneCoordinates = new int[3];

    private Set<Enemy> launchedEnemies = new HashSet<>();

    private Player player;

    private ScheduledExecutorService launchExecutor = Executors.newSingleThreadScheduledExecutor();

    private boolean isEnding = false;

    private int gameMode;

    Game(int gameMode, JFrame jFrame, MainMenu mainMenu) {
        this.jFrame = jFrame;
        this.mainMenu = mainMenu;
        this.gameMode = gameMode;
        this.setDoubleBuffered(true);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        try {
            player = new Player(mainMenu.getPlayerImage());
            player.laneIndex = laneCoordinates.length / 2;
            dragonsRemaining = 30 + (gameMode + 1) * 5;
            startGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startGame() {
        launchDragons().run();
    }

    private Runnable launchDragons() {
        return new Runnable() {
            @Override
            public void run() {
                int randomLane = new Random().nextInt(laneCoordinates.length);
                launchedEnemies.add(new Enemy(mainMenu.getEnemyImage(), x, y, randomLane));
                dragonsRemaining--;
                if (dragonsRemaining > 0) {
                    launchExecutor.schedule(this, 750 - ((gameMode + 1) / 3 * 200), TimeUnit.MILLISECONDS);
                } else {
                    endGame();
                }
            }
        };
    }

    void resizeImages(int x, int y) {
        this.x = x;
        this.y = y;
        player.imageWidth = x * 145 / 1920;
        player.imageHeight = y * 250 / 1080;
        player.yPosition = (int) (y / 1.35);
        for (Enemy enemy : launchedEnemies) {
            enemy.imageWidth = x * 300 / 1920;
            enemy.imageHeight = y * 200 / 1080;
        }
        final int laneSeparation = x / 4;
        for (int i = 0; i < laneCoordinates.length; i++) {
            laneCoordinates[i] = (i + 1) * laneSeparation;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        moveLaunchedDragons();
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(mainMenu.getGameBackgroundImage(), 0, 0, getWidth(), getHeight(), this);
        g2d.drawImage(player.getImage(), laneCoordinates[player.laneIndex] - player.getStart(),
                player.yPosition, player.imageWidth, player.imageHeight, this);
        for (Enemy enemy : launchedEnemies) {
            if (enemy.isActive)
                g2d.drawImage(enemy.getImage(), laneCoordinates[enemy.laneIndex] - enemy.getStart(),
                        enemy.yPosition - enemy.imageHeight,
                        enemy.imageWidth, enemy.imageHeight, this);
        }
    }

    private void moveLaunchedDragons() {
        for (Enemy enemy : launchedEnemies) {
            if (enemy.isActive) {
                enemy.yPosition += SPEED;
                if (player.laneIndex == enemy.laneIndex && enemy.yPosition >= player.yPosition) {
                    endGame();
                } else if (enemy.yPosition >= y) {
                    enemy.isActive = false;
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (player.laneIndex > 0) {
                    player.laneIndex--;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (player.laneIndex < laneCoordinates.length - 1) {
                    player.laneIndex++;
                }
                break;
            case KeyEvent.VK_ESCAPE:
                endGame();
                break;
        }
    }

    private void endGame() {
        if (!isEnding) {
            isEnding = true;
            launchExecutor.shutdown();
            jFrame.setContentPane(mainMenu);
            jFrame.validate();
            mainMenu.requestFocus();
            mainMenu.endGame();

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
