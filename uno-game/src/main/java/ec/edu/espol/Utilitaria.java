package ec.edu.espol;
import java.util.ArrayList;
import java.util.Collections;

public class Utilitaria {
    
    public static ArrayList<Carta> crearBaraja(){
        ArrayList<Carta> baraja = new ArrayList<>();

        for(int a = 0; a<10; a++){
            Carta cartaRojo = new CartaNumerica(Color.ROJO, a);
            baraja.add(cartaRojo);
        }
        for(int e= 0; e<10; e++){
            Carta cartaAmarillo = new CartaNumerica(Color.AMARILLO, e);
            baraja.add(cartaAmarillo);
        }
        for(int o = 0; o<10;o++){
            Carta cartaAzul = new CartaNumerica(Color.AZUL, o);
            baraja.add(cartaAzul);
        }
        for( int u = 0; u<10; u++){
            Carta cartaVerde = new CartaNumerica(Color.VERDE, u);
            baraja.add(cartaVerde);
        }
        TipoEspecial [] especial = {TipoEspecial.REVERSE,TipoEspecial.BLOQUEO,TipoEspecial.MAS2,TipoEspecial.MAS4};
        Color [] colores = {Color.ROJO,Color.AMARILLO,Color.VERDE,Color.AZUL};
        for(int ba = 1; ba<3; ba++){
            for(Color color: colores){
                for(TipoEspecial cardEspecial : especial){
                    Carta newespecial = new CartaEspecial(color, cardEspecial);
                    baraja.add(newespecial);
                }
            }
        }
        TipoEspecial [] cambiocolor = {TipoEspecial.CAMBIO_DE_COLOR, TipoEspecial.MAS2, TipoEspecial.MAS4};
        Color [] negro = {Color.NEGRO};
        for(int da = 1; da<3; da++){
            for(Color extra: negro){
                for(TipoEspecial comodin: cambiocolor){
                    Carta cardcomodin = new CartaEspecial(extra, comodin);
                    baraja.add(cardcomodin);
                }
            }
        }
        Collections.shuffle(baraja);
        return baraja;
    }
}