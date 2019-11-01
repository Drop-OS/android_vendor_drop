package com.adinkwok.GameOn.models;

import java.awt.image.BufferedImage;

public class Enemy extends BaseModel {
    public Enemy(BufferedImage image, int x, int y, int laneIndex) {
        super(image);
        this.imageWidth = x * 525 / 1920;
        this.imageHeight = y * 350 / 1080;
        this.laneIndex = laneIndex;
    }
}
