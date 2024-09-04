package com.example.prueba1;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class CartaNumerica extends Carta{
    private int valor;

    public CartaNumerica(Context context, Color color, int valor) {
        super(color, obtenerImagenNumerica(context, color, valor));
        this.valor = valor;
    }


    
    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "["+color + ":" + valor + "]";
    }

    private static int obtenerImagenNumerica(Context context, Color color, int valor) {
        String letraColor="z";

        if (color.name()=="AZUL"){
            letraColor= "z";
        }else {
            letraColor = color.name().toLowerCase().substring(0, 1);
        }
        String recursoNombre = letraColor + (valor + 1); // Ej: r1, a2, v3, z4
        int resId = context.getResources().getIdentifier(recursoNombre, "drawable", context.getPackageName());
        return resId != 0 ? resId : R.drawable.base; // R.drawable.base es una imagen de fallback.
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("color", color.toString()); // Cambié "tipo" a "color"
        map.put("numero", valor);          // Cambié "valor" a "numero" para mayor claridad
        map.put("imagenResId", imagenResId);
        return map;
    }


}
