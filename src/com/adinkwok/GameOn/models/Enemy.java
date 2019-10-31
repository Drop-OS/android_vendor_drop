package com.adinkwok.GameOn.models;

import java.awt.image.BufferedImage;

public class Enemy extends BaseModel {
    public Enemy(BufferedImage image, int x, int y, int laneIndex) {
        super(image);
        this.imageWidth = x * 400 / 1920;
        this.imageHeight = y * 216 / 1080;
        this.laneIndex = laneIndex;
    }

    public boolean isActive = true;
}
