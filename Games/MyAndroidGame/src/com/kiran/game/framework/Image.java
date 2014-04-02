package com.kiran.game.framework;

import com.kiran.game.framework.Graphics.ImageFormat;


public interface Image {
    public int getWidth();
    public int getHeight();
    public ImageFormat getFormat();
    public void dispose();
}