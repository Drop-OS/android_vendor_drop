package com.adinkwok.GameOn;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Game extends JPanel implements KeyListener {
    private static final int SPEED = 5;

    private int mGameMode, mRepeatsRemaining;
    private JFrame mJFrame;
    private MainMenu mMainMenu;

    private int mScreenHeight;
    private int[] mLaneX = new int[5];

    private int mDragonStart;
    private int[] mDragonDimens = new int[2], mDragonYs = new int[mLaneX.length];
    private boolean[] mDragonLaunches = new boolean[mLaneX.length];
    private LinkedList<Integer> mLaunchedDragons = new LinkedList<>();

    private int mBoatStart, mBoatX, mBoatY;
    private int[] mBoatDimens = new int[2];

    private BufferedImage mBackgroundImage, mBoatImage, mDragonImage;
    private ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();


    Game(int gameMode, JFrame jFrame, MainMenu mainMenu) {
        mGameMode = gameMode;
        mJFrame = jFrame;
        mMainMenu = mainMenu;
        mBoatX = 2;
        this.setDoubleBuffered(true);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        try {
            mBackgroundImage = ImageIO.read(getClass().getResource("water.png"));
            mBoatImage = ImageIO.read(getClass().getResource("boat.png"));
            mDragonImage = ImageIO.read(getClass().getResource("dragon.png"));
            mBoatDimens[0] = mBoatImage.getWidth();
            mBoatDimens[1] = mBoatImage.getHeight();
            mDragonDimens[0] = mDragonImage.getWidth();
            mDragonDimens[1] = mDragonImage.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        resetLaunchedDragons();
        mRepeatsRemaining = (mGameMode + 1) * 5;
        launchDragons().run();
    }

    private Runnable launchDragons() {
        return new Runnable() {
            @Override
            public void run() {
                int randomNum = mLaunchedDragons.getFirst();
                mDragonLaunches[randomNum] = true;
                mLaunchedDragons.removeFirst();
                if (mLaunchedDragons.isEmpty()) {
                    --mRepeatsRemaining;
                    if (mRepeatsRemaining <= 0) {
                        endGame();
                        return;
                    } else {
                        resetLaunchedDragons();
                    }
                }
                mExecutor.schedule(this, 1500 / (mGameMode + 1), TimeUnit.MILLISECONDS);
            }
        };
    }

    void resizeImages(int x, int y) {
        this.mScreenHeight = y;
        mBoatDimens[0] = x * 100 / 1920;
        mBoatDimens[1] = y * 250 / 1080;
        mDragonDimens[0] = x * 400 / 1920;
        mDragonDimens[1] = y * 216 / 1080;
        mBoatStart = mBoatDimens[0] / 2;
        mDragonStart = mDragonDimens[0] / 2;
        mBoatY = (int) (y / 1.35);
        final int laneSeparation = x / 6;
        for (int i = 0; i < mLaneX.length; i++) {
            mLaneX[i] = (i + 1) * laneSeparation;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(mBackgroundImage, 0, 0, getWidth(), getHeight(), this);
        g2d.drawImage(mBoatImage, mLaneX[mBoatX] - mBoatStart, mBoatY,
                mBoatDimens[0], mBoatDimens[1], this);
        lowerDragon();
        for (int i = 0; i < mLaneX.length; i++) {
            if (mDragonLaunches[i]) {
                g2d.drawImage(mDragonImage, mLaneX[i] - mDragonStart, mDragonYs[i] - mDragonDimens[1],
                        mDragonDimens[0], mDragonDimens[1], this);
            }
        }
    }

    private void resetLaunchedDragons() {
        for (int i = 0; i < mLaneX.length; i++) {
            mLaunchedDragons.add(i);
        }
        Collections.shuffle(mLaunchedDragons);
    }

    private void lowerDragon() {
        for (int i = 0; i < mLaneX.length; i++) {
            if (mDragonLaunches[i]) {
                mDragonYs[i] += (mGameMode + SPEED);
                if (mBoatX == i && mDragonYs[i] >= mBoatY)
                    endGame();
                else if (mDragonYs[i] >= this.mScreenHeight) {
                    mDragonYs[i] = 0;
                    mDragonLaunches[i] = false;
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
                if (mBoatX > 0) {
                    --mBoatX;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (mBoatX < mLaneX.length - 1) {
                    ++mBoatX;
                }
                break;
            case KeyEvent.VK_ESCAPE:
                endGame();
                break;
        }
    }

    private void endGame() {
        mExecutor.shutdown();
        mJFrame.setContentPane(mMainMenu);
        mJFrame.validate();
        mMainMenu.requestFocus();
        mMainMenu.endGame();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
