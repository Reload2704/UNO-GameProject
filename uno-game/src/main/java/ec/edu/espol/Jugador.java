package ec.edu.espol;
import java.util.ArrayList;
import java.util.Random;

public class Jugador {
    private String nombre;
    private ArrayList<Carta> mano;

    Color [] color = Color.values();
        Random azar = new Random();{
            for(int i= 0; i<8; i++){
                int indice = azar.nextInt(color.length);  /// creo que esto esta mal porque debemos indexar en al baraja que hay que crearse. 
                Color aleColor = color[indice];
                Carta Cardnew = new CartaNumerica(aleColor, indice);
                mano.add(Cardnew);
            }
        }
        


    public Carta jugarCarta(int indice){
        Carta newcarta = mano.remove(indice);
        return newcarta;
    }

    //public void robarCarta(Baraja baraja){
        //mano.add(baraja.robarCarta());
    //}


        


}
