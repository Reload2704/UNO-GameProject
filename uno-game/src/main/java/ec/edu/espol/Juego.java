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

        //Se refieren al turno maquina (1) y turno jugador (0)
        int turno=0;

        while (!jugador.getMano().isEmpty() || !maquina.getMano().isEmpty()) {
            /*Aqui va la lógica completa del juego.
             * El bucle acaba cuando la mano de uno de los jugadores queda vacia.
            */
            Carta ultcarta=lineaDeJuego.get(lineaDeJuego.size()-1);
            System.out.println(ultcarta);
            Scanner sc = new Scanner (System.in);
            System.out.println("¿Cuál es su carta a jugar? (Indique la posición)");
            int position= sc.nextInt()-1;
            Carta cartaajugar=jugador.getMano().get(position);
            System.out.println(cartaajugar);
            //Primera regla
            if(cartaajugar.getColor().equals(ultcarta.getColor()) || cartaajugar.getNumero().equals(ultcarta.getNumero())){
                lineaDeJuego.add(cartaajugar);
                jugador.jugarCarta(position);
                System.out.println("1");
                turno=1;
            }
            //Segunda regla
            else if(cartaajugar instanceof CartaEspecial){
                if(cartaajugar.getColor().equals(ultcarta.getColor())){
                    lineaDeJuego.add(cartaajugar);
                    jugador.getMano().remove(cartaajugar);
                    System.out.println("2");
                }
                //Tercera regla
                else if (cartaajugar.getColor().equals(Color.NEGRO)){
                    System.out.println("¿Cuál será el color para el siguiente turno?");
                    String colornew= sc.nextLine();
                }
            }
            //Cuarta regla
            if(ultcarta instanceof CartaEspecial){
                Random rd = new Random();
                CartaEspecial ct=(CartaEspecial)ultcarta;
                if(ct.getTipo().equals(TipoEspecial.MAS2) || ct.getTipo().equals(TipoEspecial.MAS4)){
                    if(turno==0)
                    for(int i = 0; i<4; i++){
                        jugador.getMano().add(baraja.remove(rd.nextInt(baraja.size())));
                }
            }

            System.out.println(jugador.getMano());
            System.out.println(lineaDeJuego);
        }
        if (jugador.getMano().isEmpty()){
            System.out.println("Felicidades, has ganado!");
        } else{
            System.out.println("Mala suerte, has perdido!");
        }

        /* ------------- Notas -------------
         * Para robar carta seria: jugador.anadirCarta(robarCarta()) y maquina.anadirCarta(robarCarta());
         * Habria que crear un metodo en Jugador que permita agregar listas de cartas (anadirCartas(ArrayList<Carta> cartas))
         * Habria que crear un metodo en Juego que devuelva una lista de cartas ArrayList<Carta> robarCartas()
         * Los dos metodos anteriores serviran al momento de usar un +2 o +4
        */
    }}
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


    @Override
    public String toString() {
        return "Linea de Juego: " + lineaDeJuego;
    }
}