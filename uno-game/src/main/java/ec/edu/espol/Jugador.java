package ec.edu.espol;
import java.util.ArrayList;
import java.util.Random;

public class Jugador {
    private String nombre;
    private ArrayList<Carta> mano;

    Random azar = new Random();
        for(int i= 0; i<8; i++){
            int indice = azar.nextInt(Baraja.maso().size());  /// creo que esto esta mal porque debemos indexar en al baraja que hay que crearse. 
            Color aleColor = ;
            Carta Cardnew = new CartaNumerica(aleColor, indice);
            mano.add(Cardnew);
        }
    
        


    public Carta jugarCarta(int indice){
        Carta newcarta = mano.remove(indice);
        return newcarta;
    }

    //public void robarCarta(Baraja baraja){
        //mano.add(baraja.robarCarta());
    //}


        


}
