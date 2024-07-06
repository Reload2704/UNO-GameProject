package ec.edu.espol;
import java.util.ArrayList;

public class Juego {
    private ArrayList<Carta> baraja;
    private Jugador jugador;
    private Jugador maquina;
    private ArrayList<Carta> lineaDeJuego;
    
    public Juego(){
        
    }

    public void setBaraja(ArrayList<Carta> baraja) {
        this.baraja = baraja;
    }
    
    
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }
    
    
    public void setMaquina(Jugador maquina) {
        this.maquina = maquina;
    }


    public void setLineaDeJuego(ArrayList<Carta> lineaDeJuego) {
        this.lineaDeJuego = lineaDeJuego;
    }
    
    @Override
    public String toString() {
        return "Baraja ("+baraja.size()+"): "+ baraja + "\n\nLinea de Juego: " + lineaDeJuego;
    }
}