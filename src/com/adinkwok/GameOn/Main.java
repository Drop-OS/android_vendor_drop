package com.adinkwok.GameOn;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.InputStream;

public class Main extends JFrame implements ComponentListener, ActionListener {
    private MainMenu mMainMenu = new MainMenu(this);

    private Main() {
        Timer timer = new Timer(17, this);
        timer.setCoalesce(true);
        timer.start();
        this.setTitle("Funfest: Game On!");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //this.setUndecorated(true);
        //this.setExtendedState(MAXIMIZED_BOTH);
        this.setSize(1920, 1080);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setContentPane(mMainMenu);
        this.addComponentListener(this);
        try {
            InputStream input = getClass().getResourceAsStream("music.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(input);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            //
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        int W = 16;
        int H = 9;
        Rectangle b = e.getComponent().getBounds();
        e.getComponent().setBounds(b.x, b.y, b.width, b.width * H / W);
        mMainMenu.resizeImages(getWidth(), getHeight());
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (mMainMenu != null) {
            mMainMenu.updateFrame();
        }
    }
}
