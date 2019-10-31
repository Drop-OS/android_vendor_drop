package com.adinkwok.GameOn.models;

import java.awt.*;

public abstract class BaseModel {
    BaseModel(Image image) {
        this.image = image;
    }

    private Image image;

    public Image getImage() {
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
