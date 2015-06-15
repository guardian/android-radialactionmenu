package com.theguardian.ui;

public class RadialActionMenuAction<T> {
    public final T data;
    public final String description;
    public final int id;

    public RadialActionMenuAction(T data, String description, int id) {
        this.data = data;
        this.id = id;
        this.description = description;
    }
}