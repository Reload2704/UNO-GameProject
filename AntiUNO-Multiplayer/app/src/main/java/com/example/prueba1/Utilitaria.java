package com.example.prueba1;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Utilitaria {

    private Utilitaria(){
        throw new IllegalStateException("Clase Utilitaria");
    }
    
    public static ArrayList<Carta> crearBaraja(Context context){
        ArrayList<Carta> baraja = new ArrayList<>();
        Color[] colores = {Color.ROJO, Color.AMARILLO, Color.VERDE, Color.AZUL};

        // CREACIÓN DE LAS CARTAS NUMÉRICAS
        // Se agregan 10 cartas de cada color a la baraja (números del 0 al 9)
        for (int i = 0; i < 10; i++) {
            for (Color color : colores) {
                // Aquí se crea la carta numérica con su respectiva imagen
                Carta carta = new CartaNumerica(context, color, i);
                baraja.add(carta);
            }
        }

        //CREACION DE LAS CARTAS ESPECIALES
        TipoEspecial [] especial = {TipoEspecial.REVERSE,TipoEspecial.BLOQUEO,TipoEspecial.MAS2,TipoEspecial.MAS4};
        for(int ba = 1; ba<3; ba++){
            for(Color color: colores){
                for(TipoEspecial cardEspecial : especial){
                    Carta newespecial = new CartaEspecial(context,color, cardEspecial);
                    baraja.add(newespecial);
                }
            }
        }
        TipoEspecial [] cambiocolor = {TipoEspecial.CAMBIO_DE_COLOR, TipoEspecial.MAS2, TipoEspecial.MAS4};
        Color [] negro = {Color.NEGRO};
        for(int da = 1; da<3; da++){
            for(Color extra: negro){
                for(TipoEspecial comodin: cambiocolor){
                    Carta cardcomodin = new CartaEspecial(context,extra, comodin);
                    baraja.add(cardcomodin);
                }
            }
        }
        Collections.shuffle(baraja);
        return baraja;
    }

    
    //Primera validación. (Recibe carta a jugar y ultima carta de linea de juego. Dice si tiene igual color o número)
    public static boolean esIgualCyN(CartaNumerica cartaajugar, Carta ulCarta){
        if(ulCarta instanceof CartaNumerica){
            CartaNumerica cartlok=(CartaNumerica)ulCarta;
            return  cartaajugar.getColor().equals(ulCarta.getColor()) || cartlok.getValor()==(cartaajugar.getValor());
        }
        else
            return cartaajugar.getColor().equals(ulCarta.getColor());
    }

    /*Segunda condición (Recibe carta jugar y ult carta linea de juego. 
    Dice si la carta comodin cambio de color tiene el mismo color que ult linea de juego)*/

    public static boolean esCondicion2(CartaEspecial cartaajugar, Carta ulCarta){
            return (cartaajugar.getColor()==ulCarta.getColor())&&(cartaajugar.getTipo()==TipoEspecial.CAMBIO_DE_COLOR);
        }
    
    //Tercera condición(Recibe la carta a jugar y solo dice si es negro o no)

    public static boolean isNegro(Carta cartaajugar){
        return cartaajugar.getColor()==(Color.NEGRO);
    }

    //Cuarta condición(recibe la ultimacarta de linea de juego o la carta de juego  e indica si es un +2 o +4)
    public static boolean iscomodin(Carta cartaplay) {
        return cartaplay instanceof CartaEspecial && 
               (((CartaEspecial) cartaplay).getTipo() == TipoEspecial.MAS2 || 
                ((CartaEspecial) cartaplay).getTipo() == TipoEspecial.MAS4);
    }

    //Quinta condición(recibe la carta a jugar y la ultima carta de linea de juego)
    //indica sila carta a jugar es reversa o bloqueo y q solo será usada si el color
    //de la ultima carta coincide con la misma o si son del mismo tipo (e.g Bloqueo se responde con Bloqueo))
    public static boolean isReverorBloq(Carta cartaajugar,Carta ulCarta){
        if (cartaajugar instanceof CartaEspecial) {
            CartaEspecial cartaTrans = (CartaEspecial) cartaajugar;
            return (cartaTrans.getTipo().equals(TipoEspecial.REVERSE) || cartaTrans.getTipo().equals(TipoEspecial.BLOQUEO))&&(ulCarta.getColor()==cartaajugar.getColor()||ulCartaescomodin(ulCarta, cartaajugar));
        }
        return false;
    }

    //Septima Condicion (Condicion para gritar UNOOO. Aun no implementada)
    public static void lastCarta(ArrayList<Carta> mano){
        if(mano.size()==1)
            System.out.println("\nUNOOOOO!");
    }

    //es ultcarta comodin? (Está siendo usada en la quinta condicion para validar que carta de bloqueo y reversa
    //se pueden responder entre si , independiente del color ) BLOQUEO ROJO-BLOQUEO AMARILLO / REVERSE ROJO-REVERSE AZUL

    public static boolean ulCartaescomodin(Carta ulCarta, Carta cartaajugar) {
        return ulCarta instanceof CartaEspecial &&
               cartaajugar instanceof CartaEspecial &&
               ((CartaEspecial) ulCarta).getTipo() == ((CartaEspecial) cartaajugar).getTipo();
    }

    //ESta validadacion se usa para agregar a linea de juego cartas +2 y +4
    //Comparamos si es un +2 o +4 Y SI TIENE EL MISMO COLOR QUE LA ULTIMA CARTA DE LINEA DE JUEGO
    public static boolean iscomodincartajuego(CartaEspecial cartaajugar, Carta ulCarta){
            return (cartaajugar.getTipo()==TipoEspecial.MAS2||cartaajugar.getTipo()==TipoEspecial.MAS4)&& cartaajugar.getColor()==(ulCarta.getColor());
    }

    //obtener color aleatorio (Para que la maquina opueda colocar un color random en caso de escoger cambio de color)
    public static Color getRandomColor(Random rd) {
        Color[] colors = Color.values();
        int randomIndex = rd.nextInt(colors.length);
        return colors[randomIndex];
        }
        
        public static boolean validacionGeneral(Carta cartaajugar, Carta ulCarta){
            if (cartaajugar instanceof CartaNumerica){
                CartaNumerica cartanum=(CartaNumerica)cartaajugar;
    
                if(esIgualCyN(cartanum,ulCarta)){
                    return true;
                }
                return false;
            }
            else if (cartaajugar instanceof CartaEspecial){
                CartaEspecial especards= (CartaEspecial)cartaajugar;
                if(isNegro(cartaajugar) || esCondicion2(especards, ulCarta)||isReverorBloq(especards,ulCarta)||iscomodincartajuego(especards,ulCarta)){
                    return true;
                }
                else
                    return false;
            }
            return false;
        }
}
