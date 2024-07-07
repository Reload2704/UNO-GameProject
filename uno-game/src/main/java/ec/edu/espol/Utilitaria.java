package ec.edu.espol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Utilitaria {
    
    public static ArrayList<Carta> crearBaraja(){
        ArrayList<Carta> baraja = new ArrayList<>();
//Se agrega 9 cartas de cada color a la baraja
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

    
    //Primera validación. (Recibe carta a jugar y ultima carta de linea de juego. Dice si tiene igual color o número)
    public boolean esIgualCyN(Carta cartaajugar, Carta ulCarta){
        if(cartaajugar instanceof CartaNumerica){
       if( cartaajugar.getColor().equals(ulCarta.getColor()) || cartaajugar.getNumero()==(ulCarta.getNumero())){
        return true;
       }
       else
       return false;
    }
       else
       return false;
    }

    /*Segunda condición (Recibe carta jugar y ult carta linea de juego. 
    Dice si la carta comodin cambio de color tiene el mismo color que ult linea de juego)*/

    public boolean esCondicion2(Carta cartaajugar, Carta ulCarta){
        if(cartaajugar instanceof CartaEspecial){
            CartaEspecial playercarta=(CartaEspecial)cartaajugar;
            if((playercarta.getColor()==ulCarta.getColor())&&(playercarta.getTipo()==TipoEspecial.CAMBIO_DE_COLOR)){
                return true;
            }
            else
                return false;
        }
        else
            return false;
        }
    
    //Tercera condición(Recibe la carta a jugar y solo dice si es negro o no)

    public boolean isNegro(Carta cartaajugar){
        if(cartaajugar.getColor()==(Color.NEGRO)){
            return true;
        }
        else
        return false;
    }

    //Cuarta condición(recibe la ultimacarta de linea de juego o la carta de juego  e indica si es un +2 o +4)
    public boolean iscomodin(Carta cartaplay){
        if(cartaplay instanceof CartaEspecial){
        CartaEspecial ct=(CartaEspecial)cartaplay;
            if(ct.getTipo()==(TipoEspecial.MAS2) || ct.getTipo()==(TipoEspecial.MAS4)){
                return true;
             }
        else
             return false;
        }
    else
        return false;
    }

    //Quinta condición(recibe la carta a jugar y la ultima carta de linea de juego)
    //indica sila carta a jugar es reversa o bloqueo y q solo será usada si el color
    //de la ultima carta coincide con la misma o si son del mismo tipo (e.g Bloqueo se responde con Bloqueo))
    public boolean isReverorBloq(Carta cartaajugar,Carta ulCarta){
        if(cartaajugar instanceof CartaEspecial){
            CartaEspecial playercarta=(CartaEspecial)cartaajugar;
            if((playercarta.getTipo().equals(TipoEspecial.REVERSE) || playercarta.getTipo().equals(TipoEspecial.BLOQUEO))&&(ulCarta.getColor()==cartaajugar.getColor()||ulCartaescomodin(ulCarta, cartaajugar))){
                return true;
            }
            else 
                return false;
        }
        else
            return false;
    }

    //Septima Condicion (Condicion para gritar UNOOO. Aun no implementada)
    public boolean lastCarta(ArrayList<Carta> mano){
        if(mano.size()==1){
            return true;
        }
        return false;
    }

    //es ultcarta comodin? (Está siendo usada en la quinta condicion para validar que carta de bloqueo y reversa
    //se pueden responder entre si , independiente del color ) BLOQUEO ROJO-BLOQUEO AMARILLO / REVERSE ROJO-REVERSE AZUL

    public boolean ulCartaescomodin(Carta ulCarta, Carta cartaajugar){
        if(ulCarta instanceof CartaEspecial && cartaajugar instanceof CartaEspecial){
            CartaEspecial ct=(CartaEspecial)ulCarta;
            CartaEspecial playercarta=(CartaEspecial)cartaajugar;
            if(ct.getTipo()==playercarta.getTipo()){
                return true;
            }
            return false;
        }
        return false;
    }

    //ESta validadacion se usa para agregar a linea de juego cartas +2 y +4
    //Primero se verifica si es especial, se le hace downcasting, comparamos si
    //es un +2 o +4 Y SI TIENE EL MISMO COLOR QUE LA ULTIMA CARTA DE LINEA DE JUEGO
    public boolean iscomodincartajuego(Carta cartaajugar, Carta ulCarta){
        if (cartaajugar instanceof CartaEspecial) {
            CartaEspecial playercarta=(CartaEspecial)cartaajugar;
            if((playercarta.getTipo()==TipoEspecial.MAS2||playercarta.getTipo()==TipoEspecial.MAS4)&& cartaajugar.getColor()==(ulCarta.getColor())){
                return true;
            }
            return false;
        }
        return false;
    }

    //obtener color aleatorio (Para que la maquina opueda colocar un color random en caso de escoger cambio de color)
    public static Color getRandomColor(Random rd) {
        Color[] colors = Color.values();
        int randomIndex = rd.nextInt(colors.length);
        return colors[randomIndex];
        }

}
