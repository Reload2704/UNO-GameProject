package ec.edu.espol;
import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private ArrayList<Carta> mano;

    public Carta jugarCarta(int indice){
        Carta newcarta = mano.remove(indice);
        return newcarta;
    }

    public void robarCarta(Baraja baraja){
        mano.add(baraja.robarCarta());
    }


}
