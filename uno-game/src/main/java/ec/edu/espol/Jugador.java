package ec.edu.espol;
import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private ArrayList<Carta> mano = new ArrayList<>();

    public Jugador(String nombre){
        this.nombre = nombre;
    }
    
    public Carta jugarCarta(int indice){
        Carta newcarta = mano.remove(indice);
        return newcarta;
    }

    //Revisar este metodo (creo que es un metodo de Juego):

    /*public void robarCarta(){
        Carta robarc = Baraja.cartasBarajadas().remove(0);
        mano.add(robarc);
    }*/

    //Getters 
    public String getNombre() {
        return nombre;
    }
    public ArrayList<Carta> getMano() {
        return mano;
    }
    
}