package ec.edu.espol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Baraja {

    public static void masoColores(){
        ArrayList<Carta> cartas = new ArrayList<>();
            for(int a = 0; a<10; a++){
                Carta cartaRojo = new CartaNumerica(Color.ROJO, a);
                cartas.add(cartaRojo);
            }
            for(int e= 0; e<10; e++){
                Carta cartaAmarillo = new CartaNumerica(Color.AMARILLO, e);
                cartas.add(cartaAmarillo);
            }
            for(int o = 0; o<10;o++){
                Carta cartaAzul = new CartaNumerica(Color.AZUL, o);
                cartas.add(cartaAzul);
            }
    
            for( int u = 0; u<10; u++){
                Carta cartaVerde = new CartaNumerica(Color.VERDE, u);
                cartas.add(cartaVerde);
            }
    }


    
   


    //public void barajar(){
        //Collections.shuffle(cartas);
         // esto nos servira para mezclar las cartas de la baraja
    //}
    

    //public Carta robarCarta(){
        //Carta c = cartas.remove(0);
        //return c;
    //}

    
}
