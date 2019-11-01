package com.adinkwok.GameOn;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

class MainMenu extends JPanel implements KeyListener {
    private int mMenuSelection, mMenuItemX;
    private int[] mMenuItemY = {400, 500, 600};
    private int[] mImageDimen = new int[2];
    private JFrame mJFrame;
    private Game mGame;

    private int mScreenWidth, mScreenHeight;

    private BufferedImage mBackgroundImage, mKtoGr2Image, mGr3to4Image, mGr5plusImage, mSelectImage;

    private BufferedImage enemyImage;
    private BufferedImage gameBackgroundImage;

    private ImageIcon playerImage;

    MainMenu(JFrame jFrame) {
        mJFrame = jFrame;
        mGame = null;
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        try {
            mBackgroundImage = ImageIO.read(this.getClass().getResource("background.png"));
            mKtoGr2Image = ImageIO.read(this.getClass().getResource("ktogr2.png"));
            mGr3to4Image = ImageIO.read(getClass().getResource("gr3to4.png"));
            mGr5plusImage = ImageIO.read(getClass().getResource("gr5plus.png"));
            mSelectImage = ImageIO.read(getClass().getResource("select.png"));
            enemyImage = ImageIO.read(getClass().getResource("charizard.png"));
            gameBackgroundImage = ImageIO.read(getClass().getResource("gamebackground.jpg"));
            mImageDimen[0] = getWidth();
            mImageDimen[1] = getHeight();
            playerImage = new ImageIcon(getClass().getResource("player.gif"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void resizeImages(int x, int y) {
        this.mScreenWidth = x;
        this.mScreenHeight = y;
        mImageDimen[0] = x * 350 / 1920;
        mImageDimen[1] = y * 100 / 1080;
        mMenuItemX = (x / 2) - (mImageDimen[0] / 2);
        mMenuItemY[0] = (int) (y / 2.0655);
        mMenuItemY[1] = (int) (y / 1.6524);
        mMenuItemY[2] = (int) (y / 1.377);
        if (mGame != null) mGame.resizeImages(x, y);
    }

    void endGame() {
        mGame = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(mBackgroundImage, 0, 0, getWidth(), getHeight(), this);
        g2d.drawImage(mKtoGr2Image, mMenuItemX, mMenuItemY[0], mImageDimen[0], mImageDimen[1], this);
        g2d.drawImage(mGr3to4Image, mMenuItemX, mMenuItemY[1], mImageDimen[0], mImageDimen[1], this);
        g2d.drawImage(mGr5plusImage, mMenuItemX, mMenuItemY[2], mImageDimen[0], mImageDimen[1], this);
        g2d.drawImage(mSelectImage, mMenuItemX, mMenuItemY[mMenuSelection], mImageDimen[0], mImageDimen[1], this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (mMenuSelection <= 0) mMenuSelection = mMenuItemY.length - 1;
                else mMenuSelection--;
                break;
            case KeyEvent.VK_DOWN:
                if (mMenuSelection >= mMenuItemY.length - 1) mMenuSelection = 0;
                else mMenuSelection++;
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_ENTER:
                mGame = new Game(mMenuSelection, mJFrame, this);
                mJFrame.setContentPane(mGame);
                mJFrame.validate();
                resizeImages(mScreenWidth, mScreenHeight);
                mGame.requestFocus();
                break;
        }
    }

    ImageIcon getPlayerImage() {
        return playerImage;
    }

    BufferedImage getEnemyImage() {
        return enemyImage;
    }

    BufferedImage getGameBackgroundImage() {
        return gameBackgroundImage;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    void updateFrame() {
        if (mGame != null) {
            mGame.updateFrame();
        } else {
            repaint();
        }
    }
}
