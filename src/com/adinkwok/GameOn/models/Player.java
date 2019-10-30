package com.adinkwok.GameOn.models;

import java.awt.image.BufferedImage;

public class Player extends BaseModel {
    public Player(BufferedImage image) {
        super(image);
        imageWidth = 400;
        imageHeight = image.getHeight() * imageWidth / image.getWidth();
    }
}
