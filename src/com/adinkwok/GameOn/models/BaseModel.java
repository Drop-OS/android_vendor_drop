package com.adinkwok.GameOn.models;

import java.awt.image.BufferedImage;

public abstract class BaseModel {
    BaseModel(BufferedImage image) {
        this.image = image;
    }

    private BufferedImage image;

    public BufferedImage getImage() {
        return image;
    }

    public int imageWidth;
    public int imageHeight;

    public int laneIndex;
    public int yPosition;

    public int getStart() {
        return imageWidth / 2;
    }
}
