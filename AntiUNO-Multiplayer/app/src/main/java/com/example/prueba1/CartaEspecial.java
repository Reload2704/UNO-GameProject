package com.example.prueba1;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class CartaEspecial extends Carta{
    private TipoEspecial tipo;

    public CartaEspecial(Context context, Color color, TipoEspecial tipo){
        super(color, obtenerImagenEspecial(context, color, tipo));
        this.tipo = tipo;
    }
    public TipoEspecial getTipo() {
        return tipo;
    }
    @Override
    // implemente esto para imprimir las cartas y saber si estaba bien.
    public String toString() {
        return "{" + color + ":" + tipo + "}";
    }

    private static int obtenerImagenEspecial(Context context, Color color, TipoEspecial tipo) {
        // Mapear colores a letras correspondientes
                String letraColor;
                switch (color) {
                    case ROJO:
                        letraColor = "r";
                        break;
                    case AMARILLO:
                        letraColor = "a";
                        break;
                    case VERDE:
                        letraColor = "v";
                        break;
                    case AZUL:
                        letraColor = "z";
                        break;
                    case NEGRO: // Supongo que negro es un caso especial
                        letraColor = "n"; // Ajusta según el nombre de los recursos
                        break;
                    default:
                        letraColor = "r"; // Fallback por si acaso
                        break;
                }
        // Mapear tipos especiales a letras o números
                String tipoEspecialNombre;
                switch (tipo) {
                    case REVERSE:
                        tipoEspecialNombre = "reverse"; // Ajusta si tienes nombres especiales para esto
                        break;
                    case BLOQUEO:
                        tipoEspecialNombre = "bloqueo";
                        break;
                    case MAS2:
                        tipoEspecialNombre = "mas2";
                        break;
                    case MAS4:
                        tipoEspecialNombre = "mas4";
                        break;
                    case CAMBIO_DE_COLOR:
                        tipoEspecialNombre = "cambio_color";
                        break;
                    default:
                        tipoEspecialNombre = "default"; // Fallback por si acaso
                        break;
                }
        // Crear un nombre de recurso dinámico basado en el color y tipo especial
                String recursoNombre = letraColor + "_" + tipoEspecialNombre;
        // Obtener el identificador del recurso a partir del nombre
                int resId = context.getResources().getIdentifier(recursoNombre, "drawable",context.getPackageName());
        // Retornar el identificador del recurso correspondiente o una imagen por defecto
                return resId != 0 ? resId : R.drawable.base;
            }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("color", color.toString()); // Cambié "tipo" a "color"
        map.put("tipoEspecial", tipo.toString()); // Cambié "valor" a "tipoEspecial" para mayor claridad
        map.put("imagenResId", imagenResId);
        return map;
    }

}
