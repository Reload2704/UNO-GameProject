package ec.edu.espol;
import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private ArrayList<Carta> mano;

    public ArrayList<Carta> manoJugador(){
        for(int i= 0; i<7; i++){
            mano.add(Baraja.cartasBarajadas().get(i));
        }
        return mano;   
    }
    public Carta jugarCarta(int indice){
        Carta newcarta = mano.remove(indice);
        return newcarta;
    }
    public void robarCarta(){
        Carta robarc = Baraja.cartasBarajadas().remove(0);
        mano.add(robarc);
    }
    //Getters 
    public String getNombre() {
        return nombre;
    }
    public ArrayList<Carta> getMano() {
        return mano;
    }   
}
