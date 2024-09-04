package com.example.prueba1;
import java.util.ArrayList;
import java.util.Random;

public class Jugador {
    private String nombre;
    private Random rd = new Random();
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
    public Carta validarCartaMaquina(Carta ultCard){ //Lineajuego y turno irán en Juego.
        for(Carta c : mano){
            if(c instanceof CartaNumerica){
                CartaNumerica cn = (CartaNumerica) c;
                
                //Primera regla
                if(Utilitaria.esIgualCyN(cn, ultCard)){
                    CartaNumerica cnret = cn;
                    mano.remove(cn);
                    return cnret;
                }
            } else {
                CartaEspecial ce = (CartaEspecial) c;

                //Segunda regla
                if(Utilitaria.esCondicion2(ce, ultCard)){
                    CartaEspecial ceret = ce;
                    mano.remove(ce);
                    return ceret;
                } 

                //Tercera regla
                else if(Utilitaria.isNegro(ce)){
                    Color [] colores = {Color.ROJO,Color.AMARILLO,Color.VERDE,Color.AZUL};
                    CartaEspecial ceret = ce;
                    int idxrd = this.rd.nextInt(colores.length-1);
                    ceret.setColor(colores[idxrd]);
                    System.out.println("Se ha cambiado el color de la carta a: "+colores[idxrd]);
                    mano.remove(ce);
                    return ceret;
                }

                //Quinta regla
                else if(Utilitaria.isReverorBloq(ce,ultCard)){
                    CartaEspecial ceret = ce;
                    mano.remove(ce);
                    return ceret;
                }

                //Cuarta Regla
                else if(Utilitaria.iscomodincartajuego(ce, ultCard)){
                    CartaEspecial ceret = ce;
                    mano.remove(ce);
                    return ceret;
                }
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