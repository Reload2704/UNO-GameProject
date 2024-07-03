package ec.edu.espol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Jugador {
    private String nombre;
    private ArrayList<Carta> mano;

    public ArrayList<Carta> manoJugador(){
        Random azar = new Random();
        ArrayList<Carta> cartasBarajadas = new ArrayList<>(Baraja.maso());
        Collections.shuffle(cartasBarajadas, azar);
        for(int i= 0; i<7; i++){
            mano.add(cartasBarajadas.get(i));
        }
        return mano;   
    }
    
    
        


    public Carta jugarCarta(int indice){
        Carta newcarta = mano.remove(indice);
        return newcarta;
    }

    public String getNombre() {
        return nombre;
    }


    public ArrayList<Carta> getMano() {
        return mano;
    }

    //public void robarCarta(Baraja baraja){
        //mano.add(baraja.robarCarta());
    //}


        


}
