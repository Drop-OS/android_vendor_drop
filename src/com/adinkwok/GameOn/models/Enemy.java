package com.adinkwok.GameOn.models;

import java.awt.image.BufferedImage;

public class Enemy extends BaseModel {
    public Enemy(BufferedImage image, int laneIndex) {
        super(image);
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        setLaneIndex(laneIndex);
    }
}
