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
    private JFrame jFrame;
    private MainMenu mainMenu;

    private int x, y;
    private int[] laneCoordinates;

    private Set<Enemy> launchedEnemies = new HashSet<>();
    private Player player;
    private ScheduledExecutorService launchExecutor = Executors.newSingleThreadScheduledExecutor();

    private boolean isGameStarted = false;
    private int gameMode;
    private double speed = 10.0;

    private int frameCount = 0;

    Game(int gameMode, JFrame jFrame, MainMenu mainMenu) {
        this.jFrame = jFrame;
        this.mainMenu = mainMenu;
        this.gameMode = gameMode;
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        switch (gameMode) {
            case 0:
                laneCoordinates = new int[3];
                break;
            case 1:
                laneCoordinates = new int[4];
            case 2:
                laneCoordinates = new int[4];
                break;
        }
        player = new Player(mainMenu.getPlayerImage());
        player.laneIndex = laneCoordinates.length / 2;
        startGame();
    }

    private void startGame() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        isGameStarted = true;
                        launchDragons().run();
                    }
                },
                1000
        );
        launchDragons().run();
    }

    private Runnable launchDragons() {
        return new Runnable() {
            @Override
            public void run() {
                int randomLane = new Random().nextInt(laneCoordinates.length);
                launchedEnemies.add(new Enemy(mainMenu.getEnemyImage(), x, y, randomLane));
                if (isGameStarted)
                    launchExecutor.schedule(this,
                            1250 - (int) (10 * speed * (gameMode + 1)), TimeUnit.MILLISECONDS);
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
            enemy.imageWidth = x * 525 / 1920;
            enemy.imageHeight = y * 350 / 1080;
        }
        int laneSeparation = x / (laneCoordinates.length + 1);
        for (int i = 0; i < laneCoordinates.length; i++) {
            laneCoordinates[i] = (i + 1) * laneSeparation;
        }
    }

    private Font font = new Font("Nexa Bold", Font.BOLD, 40);

    @Override
    protected void paintComponent(Graphics g) {
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
        g2d.setFont(font);
        g2d.drawString("Score: " + frameCount, 25, 50);
    }

    private void moveLaunchedDragons() {
        speed += 0.01;
        for (Enemy enemy : launchedEnemies) {
            if (enemy.isActive) {
                enemy.yPosition += y * speed / 1080;
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
                if (isGameStarted)
                    if (player.laneIndex > 0) {
                        player.laneIndex--;
                    }
                break;
            case KeyEvent.VK_RIGHT:
                if (isGameStarted)
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
        if (isGameStarted) {
            isGameStarted = false;
            launchExecutor.shutdown();
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            jFrame.setContentPane(mainMenu);
                            jFrame.validate();
                            mainMenu.requestFocus();
                            mainMenu.endGame();
                        }
                    },
                    3000
            );
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    void updateFrame() {
        if (isGameStarted) {
            moveLaunchedDragons();
            frameCount++;
        }
        repaint();
    }
}
