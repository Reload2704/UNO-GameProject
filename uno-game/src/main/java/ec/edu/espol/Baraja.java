package ec.edu.espol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Baraja {
    private static ArrayList<Carta> cartas;

    ArrayList<Carta> masoCarta = new ArrayList<>();

    Random azar = new Random();
    for(int i= 0; i<8; i++){
        
    }


    
    //public void barajar(){
        //Collections.shuffle(cartas);
         // esto nos servira para mezclar las cartas de la baraja
    }

    public Carta robarCarta(){
        Carta c = cartas.remove(0);
        return c;
    }

    
}
