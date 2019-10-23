package com.adinkwok.TreasureIsland;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Main extends JFrame implements ComponentListener {
    private MainMenu mMainMenu = new MainMenu(this);

    private Main() {
        this.setTitle("FunFest: Treasure Island");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //this.setUndecorated(true);
        //this.setExtendedState(MAXIMIZED_BOTH);
        this.setSize(1920, 1080);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setContentPane(mMainMenu);
        this.addComponentListener(this);
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
}
