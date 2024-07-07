package ec.edu.espol;
import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private ArrayList<Carta> mano = new ArrayList<>();

    public Jugador(String nombre){
        this.nombre = nombre;
    }
    
    public void jugarCarta(int indice){
        mano.remove(indice);
    }
    public void anadirCarta(Carta carta){
        mano.add(carta);
    }
    public Carta validarCartaMaquina(Carta ultCard){
        for(Carta c : mano){
            if(c instanceof CartaNumerica){
                CartaNumerica cn = (CartaNumerica) c;
                if(Utilitaria.esIgualCyN(cn, ultCard)){
                    CartaNumerica cnret = cn;
                    mano.remove(cn);
                    return cnret;
                }
            } else {
                CartaEspecial ce = (CartaEspecial) c;
            }
        }
        return null;
    }
    //Getters 
    public String getNombre() {
        return nombre;
    }
    public ArrayList<Carta> getMano() {
        return mano;
    }
    public void setMano(ArrayList<Carta> mano) {
        this.mano = mano;
    }
    
}