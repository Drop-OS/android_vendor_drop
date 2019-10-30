package com.adinkwok.GameOn;

import com.adinkwok.GameOn.models.Enemy;
import com.adinkwok.GameOn.models.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Game extends JPanel implements KeyListener {
    private static final int SPEED = 20;

    private int dragonsRemaining;
    private JFrame jFrame;
    private MainMenu mainMenu;

    private int mScreenHeight;
    private int[] laneCoordinates = new int[5];

    private Set<Enemy> launchedEnemies = new HashSet<>();

    private Player player;

    private BufferedImage mBackgroundImage, enemyImage;
    private ScheduledExecutorService launchExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService moveExecutor = Executors.newSingleThreadScheduledExecutor();


    Game(int gameMode, JFrame jFrame, MainMenu mainMenu) {
        this.jFrame = jFrame;
        this.mainMenu = mainMenu;
        this.setDoubleBuffered(true);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        try {
            player = new Player(ImageIO.read(getClass().getResource("flygon.gif")));
            enemyImage = ImageIO.read(getClass().getResource("dragon.png"));
            mBackgroundImage = ImageIO.read(getClass().getResource("water.png"));
            player.setLaneIndex(laneCoordinates.length / 2);
            dragonsRemaining = 30 + (gameMode + 1) * 5;
            startGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startGame() {
        launchDragons().run();
        moveLaunchedDragons().run();
    }

    private Runnable launchDragons() {
        return new Runnable() {
            @Override
            public void run() {
                int randomLane = (int) (Math.random() * ((laneCoordinates.length - 1) + 1));
                launchedEnemies.add(new Enemy(enemyImage, randomLane));
                dragonsRemaining--;
                if (dragonsRemaining > 0) {
                    launchExecutor.schedule(this, 500, TimeUnit.MILLISECONDS);
                } else {
                    endGame();
                }
            }
        };
    }

    void resizeImages(int x, int y) {
        this.mScreenHeight = y;
        player.setImageWidth(x * 100 / 1920);
        player.setImageHeight(y * 250 / 1080);
        player.setY((int) (y / 1.35));
        for (Enemy enemy : launchedEnemies) {
            enemy.setImageWidth(x * 400 / 1920);
            enemy.setImageHeight(y * 216 / 1080);
        }
        final int laneSeparation = x / 6;
        for (int i = 0; i < laneCoordinates.length; i++) {
            laneCoordinates[i] = (i + 1) * laneSeparation;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(mBackgroundImage, 0, 0, getWidth(), getHeight(), this);
        g2d.drawImage(player.getImage(), laneCoordinates[player.getLaneIndex()] - player.getStart(),
                player.getY(), player.getImageWidth(), player.getImageHeight(), this);
        for (Enemy enemy : launchedEnemies) {
            g2d.drawImage(enemy.getImage(), laneCoordinates[enemy.getLaneIndex()] - enemy.getStart(),
                    enemy.getY() - enemy.getImageHeight(),
                    enemy.getImageWidth(), enemy.getImageHeight(), this);
        }
    }

    private Runnable moveLaunchedDragons() {
        return new Runnable() {
            @Override
            public void run() {
                for (Enemy enemy : launchedEnemies) {
                    enemy.setY(enemy.getY() + SPEED);
                    if (player.getLaneIndex() == enemy.getLaneIndex() && enemy.getY() >= player.getY()) {
                        endGame();
                    } else if (enemy.getY() >= mScreenHeight) {
                        launchedEnemies.remove(enemy);
                    }
                }
                moveExecutor.schedule(this, 30, TimeUnit.MILLISECONDS);
            }
        };
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (player.getLaneIndex() > 0) {
                    player.setLaneIndex(player.getLaneIndex() - 1);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (player.getLaneIndex() < laneCoordinates.length - 1) {
                    player.setLaneIndex(player.getLaneIndex() + 1);
                }
                break;
            case KeyEvent.VK_ESCAPE:
                endGame();
                break;
        }
    }

    private void endGame() {
        launchExecutor.shutdown();
        jFrame.setContentPane(mainMenu);
        jFrame.validate();
        mainMenu.requestFocus();
        mainMenu.endGame();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
