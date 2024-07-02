package ec.edu.espol;
import java.util.ArrayList;
import java.util.Collections;

public class Baraja {
    private ArrayList<Carta> cartas;

    public ArrayList<Carta> getCartas() {
        return cartas;
    }
    
    public void barajar(){
        Collections.shuffle(cartas);
         // esto nos servira para mezclar las cartas de la baraja
    }

    public Carta robarCarta(){
        Carta c = cartas.remove(0);
        return c;
    }

    
}
