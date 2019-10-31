package com.adinkwok.GameOn.models;

import javax.swing.*;

public class Player extends BaseModel {
    public Player(ImageIcon image) {
        super(image.getImage());
        imageWidth = 400;
        imageHeight = this.getImage().getHeight(image.getImageObserver())
                * imageWidth / this.getImage().getWidth(image.getImageObserver());
    }
}
