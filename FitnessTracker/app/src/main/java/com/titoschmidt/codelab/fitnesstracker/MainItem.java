package com.titoschmidt.codelab.fitnesstracker;

public class MainItem {

    private int id;
    private int drawable;
    private int drawableColor;
    private int textStringId;
    private int color;

    public MainItem(int id, int drawable, int drawableColor, int textStringId, int color) {
        this.id = id;
        this.drawable = drawable;
        this.drawableColor = drawableColor;
        this.textStringId = textStringId;
        this.color = color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public void setDrawableColor(int drawableColor) {
        this.drawableColor = drawableColor;
    }

    public void setTextStringId(int textStringId) {
        this.textStringId = textStringId;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public int getDrawable() {
        return drawable;
    }

    public int getDrawableColor() {
        return drawableColor;
    }

    public int getTextStringId() {
        return textStringId;
    }

    public int getColor() {
        return color;
    }
}
