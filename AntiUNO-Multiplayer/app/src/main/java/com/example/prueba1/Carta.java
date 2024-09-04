package com.example.prueba1;

import java.util.HashMap;
import java.util.Map;

public abstract class Carta {
    protected Color color;
    protected int imagenResId;
    private int valor; //Le agregamos valor
    

    protected Carta(Color color, int imagenResId){
        this.color = color;
        this.imagenResId = imagenResId;
    }
    
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public int getImagenResId() {
        return imagenResId;
    }

    public abstract Map<String, Object> toMap();


}
