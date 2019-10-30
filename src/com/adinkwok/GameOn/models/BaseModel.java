package com.adinkwok.GameOn.models;

import java.awt.image.BufferedImage;

public abstract class BaseModel {
    BaseModel(BufferedImage image) {
        this.image = image;
    }

    BufferedImage image;

    public BufferedImage getImage() {
        return image;
    }

    int imageWidth;
    int imageHeight;
    private int start;

    private int laneIndex;
    private int yPosition;

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getStart() {
        return start;
    }

    public void setImageWidth(int width) {
        imageWidth = width;
        start = width / 2;
    }

    public void setImageHeight(int height) {
        imageHeight = height;
    }

    public int getLaneIndex() {
        return laneIndex;
    }

    public int getY() {
        return yPosition;
    }

    public void setLaneIndex(int index) {
        laneIndex = index;
    }

    public void setY(int y) {
        yPosition = y;
    }
}
