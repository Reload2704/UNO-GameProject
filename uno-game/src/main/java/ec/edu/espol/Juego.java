package ec.edu.espol;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class Juego {
    private ArrayList<Carta> baraja;
    private Jugador jugador;
    private Jugador maquina;
    private ArrayList<Carta> lineaDeJuego;
    
    public Juego(ArrayList<Carta> baraja, Jugador jugador, Jugador maquina, ArrayList<Carta> lineaDeJuego) {
        this.baraja = baraja;
        this.jugador = jugador;
        this.maquina = maquina;
        this.lineaDeJuego = lineaDeJuego;
    }

    public void setBaraja(ArrayList<Carta> baraja) {
        this.baraja = baraja;
    }

    public void iniciarJuego(){
        jugador.setMano(crearMano());
        System.out.println("Mano del jugador "+jugador.getNombre()+": "+ jugador.getMano());
        maquina.setMano(crearMano());
        System.out.println("Mano del jugador "+maquina.getNombre()+": "+ maquina.getMano());
        System.out.println("\n"+this);

        //Se refieren al turno maquina (-1) y turno jugador (1)
        int turno=1;

        while (!jugador.getMano().isEmpty() || !maquina.getMano().isEmpty()) {
            /*Aqui va la lógica completa del juego.
             * El bucle acaba cuando la mano de uno de los jugadores queda vacia.
            */
            System.out.println(jugador.getMano());
            System.out.println(lineaDeJuego);

            Carta ultcarta=lineaDeJuego.get(lineaDeJuego.size()-1);
            System.out.println(ultcarta);

            Scanner sc = new Scanner (System.in);

            if(turno==0){
            System.out.println("¿Cuál es su carta a jugar? (Indique la posición)");
            int position= sc.nextInt()-1;
            sc.nextLine();

            Carta cartaajugar = jugador.getMano().remove(position);
            System.out.println(cartaajugar);

            //Primera regla
            if (cartaajugar instanceof CartaNumerica){
            if(esIgualCyN(cartaajugar, ultcarta)){
                lineaDeJuego.add(cartaajugar);
                jugador.jugarCarta(position);
                turno=1;
            }
            }

           
            //Segunda regla
            else if(esCondicion2(cartaajugar, ultcarta)){
                    lineaDeJuego.add(cartaajugar);
                    jugador.getMano().remove(cartaajugar);
                    turno=turno*-1;
                    System.out.println("prueba 2");
             }

            //Tercera regla
            else if (isNegro(cartaajugar)){
                System.out.println("¿Cuál será el color para el siguiente turno?");
                String colornew= sc.nextLine();
                lineaDeJuego.add(cartaajugar);
                jugador.jugarCarta(position);
                System.out.println(colornew);
                sc.close();
            }
            //quinta regla

            if(isReverorBloq(cartaajugar)){
                    lineaDeJuego.add(cartaajugar);
                    jugador.getMano().remove(cartaajugar);
                    System.out.println("Vuelve a ser su turno");
            }
            
            //Cuarta regla
            if(iscomodin(ultcarta)){
                Random rd = new Random();
                CartaEspecial ct=(CartaEspecial)ultcarta;
                if(ct.getTipo()==TipoEspecial.MAS2)
                    for(int i = 0; i<2; i++){
                        jugador.getMano().add(baraja.remove(rd.nextInt(baraja.size())));
                }
                else{
                    for(int i = 0; i<4; i++){
                        jugador.getMano().add(baraja.remove(rd.nextInt(baraja.size())));
                    }

                 }
            }

            else 
            System.out.println("Su carta no es valida, por favor repita");
        
        }
        } 
    
        if (jugador.getMano().isEmpty()){
            System.out.println("Felicidades, has ganado!");
        } else{
            System.out.println("Mala suerte, has perdido!");
        }
    }
        /* ------------- Notas -------------
         * Para robar carta seria: jugador.anadirCarta(robarCarta()) y maquina.anadirCarta(robarCarta());
         * Habria que crear un metodo en Jugador que permita agregar listas de cartas (anadirCartas(ArrayList<Carta> cartas))
         * Habria que crear un metodo en Juego que devuelva una lista de cartas ArrayList<Carta> robarCartas()
         * Los dos metodos anteriores serviran al momento de usar un +2 o +4
        */


    public ArrayList<Carta> crearMano(){
        Random rd = new Random();
        ArrayList<Carta> manoJug = new ArrayList<>();
        for(int i = 0; i<7; i++){
            manoJug.add(baraja.remove(rd.nextInt(baraja.size())));
        }
        return manoJug;
    }
    public Carta robarCarta(){
        return baraja.remove(baraja.size()-1);
    }

    //Primera validación
    public boolean esIgualCyN(Carta cartaajugar, Carta ulCarta){
       if( cartaajugar.getColor().equals(ulCarta.getColor()) || cartaajugar.getNumero().equals(ulCarta.getNumero())){
        return true;
       }
       else
       return false;
    }
    //Segunda condición
    public boolean esCondicion2(Carta cartaajugar, Carta ulCarta){
        if(cartaajugar instanceof CartaEspecial){
            CartaEspecial playercarta=(CartaEspecial)cartaajugar;
            if(cartaajugar.getColor()==(ulCarta.getColor())){
                return true;
            }
            else
                return false;
        }
        else
            return false;
        }
    
    //Tercera condición

    public boolean isNegro(Carta cartaajugar){
        if(cartaajugar.getColor()==(Color.NEGRO)){
            return true;
        }
        else
        return false;
    }

    //Cuarta condición
    public boolean iscomodin(Carta ulCarta){
        if(ulCarta instanceof CartaEspecial){
        CartaEspecial ct=(CartaEspecial)ulCarta;
            if(ct.getTipo()==(TipoEspecial.MAS2) || ct.getTipo()==(TipoEspecial.MAS4)){
                return true;
             }
        else
             return false;
        }
    else
        return false;
    }
    //Quinta condición
    public boolean isReverorBloq(Carta cartaajugar){
        if(cartaajugar instanceof CartaEspecial){
            CartaEspecial playercarta=(CartaEspecial)cartaajugar;
            if(playercarta.getTipo().equals(TipoEspecial.REVERSE) || playercarta.getTipo().equals(TipoEspecial.BLOQUEO)){
                return true;
            }
            else 
                return false;
        }
        else
            return false;
    }

    //Septima Condicion
    public boolean lastCarta(ArrayList<Carta> mano){
        if(mano.size()==0){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Linea de Juego: " + lineaDeJuego;
    }
}