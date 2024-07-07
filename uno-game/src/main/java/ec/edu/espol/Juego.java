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
        int turno=0;

        while (!jugador.getMano().isEmpty() || !maquina.getMano().isEmpty()) {
            /*Aqui va la lógica completa del juego.
             * El bucle acaba cuando la mano de uno de los jugadores queda vacia.
            */
            if(turno==0){
                System.out.println("\n-----------------TURNO JUGADOR-----------------");
                System.out.println(jugador.getMano());
                System.out.println("Linea de Juego: "+lineaDeJuego);
    
                Carta ultcarta=lineaDeJuego.get(lineaDeJuego.size()-1);
                System.out.println("Ultima Carta: "+ultcarta);
    
                Scanner sc = new Scanner (System.in);

                System.out.println("¿Cuál es su carta a jugar? (Indique la posición)");
                int position= sc.nextInt()-1;
                sc.nextLine();

                Carta cartaajugar = jugador.getMano().get(position);
                System.out.println(cartaajugar);

                if(cartaajugar instanceof CartaNumerica){
                    CartaNumerica CNajugar = (CartaNumerica) cartaajugar;
                    
                    //Primera regla
                    if(Utilitaria.esIgualCyN(CNajugar, ultcarta)){
                        lineaDeJuego.add(CNajugar);
                        jugador.jugarCarta(position);
                        turno=1;
                        System.out.println("Validacion1");
                    }

                } else {
                    CartaEspecial CEajugar = (CartaEspecial) cartaajugar;
                    
                    //Segunda regla
                    if(Utilitaria.esCondicion2(CEajugar, ultcarta)){
                        lineaDeJuego.add(CEajugar);
                        jugador.jugarCarta(position);
                        turno=1;
                        System.out.println("prueba 2");
                    }

                    //Tercera regla
                    else if(Utilitaria.isNegro(CEajugar)){
                        System.out.println("¿Cuál será el color para el siguiente turno?");
                        String colornew= sc.nextLine();
                        lineaDeJuego.add(CEajugar);
                        jugador.jugarCarta(position);
                        System.out.println(colornew);
                        sc.close();
                        turno=1;
                    }

                    //quinta regla
                    else if(Utilitaria.isReverorBloq(CEajugar,ultcarta)){
                        System.out.println("Validacion5");
                        lineaDeJuego.add(cartaajugar);
                        jugador.jugarCarta(position);
                        System.out.println("Vuelve a ser su turno");
                    }

                    //Cuarta regla(SOLO PARA AGARRAR CARTA)
                    else if(Utilitaria.iscomodin(ultcarta)){ //se usa la validacion que indica si es +2 o +4 (true basta que solo 1 sea)
                        System.out.println("Validacion4");
                        CartaEspecial ct=(CartaEspecial)ultcarta;
                        if(ct.getTipo()==TipoEspecial.MAS2){ //si la carta es +2 agarra +2 cartas a su baraja
                            for(int i = 0; i<2; i++){
                                jugador.getMano().add(baraja.remove((baraja.size()-1)));
                            }
                        } else{
                            for(int i = 0; i<4; i++){ //si no es +2, significa que es +4. entonces agarra 4 cartas
                                jugador.getMano().add(baraja.remove((baraja.size()-1)));
                            }
                        }
                        turno=1;
                    }

                    //Cuarta regla pt2. (PARA AGREGAR A LINEA DE JUEGO CARTAS +2 Y +4)
                    else if(Utilitaria.iscomodincartajuego(CEajugar,ultcarta)){
                       lineaDeJuego.add(CEajugar);
                       jugador.jugarCarta(position);
                       turno=1;
                       System.out.println("validacion agregar+2 y +4 /n");
                    } else
                        System.out.println("Su carta no es valida, por favor repita");
                }
            } if(turno==1){
                
                System.out.println("\n-----------------TURNO MAQUINA-----------------");
                System.out.println("\nMano maquina: "+maquina.getMano());
                System.out.println("Linea de juego: "+lineaDeJuego);

                //Sacamos la ultima carta de linea de juego
                Carta ultcarta=lineaDeJuego.get(lineaDeJuego.size()-1);
                System.out.println("Ultima carta: "+ultcarta);
                
                //Seleccion de carta por maquina
                Carta cartaajugar = maquina.validarCartaMaquina(ultcarta);
                
                //Validacion de si la carta es null, roba una carta automaticamente
                if(cartaajugar != null){
                    System.out.println("Carta jugada: "+cartaajugar);
                    lineaDeJuego.add(cartaajugar);
                    turno = 0;
                } else {
                    robarCarta();
                }


                //Dentro de un for ponemos las condiciones y en cuanto cambie por primera 
                //vez la cartabandera a =1, se deja de realizar el for.

                /*
                int i;
                int banderacarta=0;
                for(i=0; banderacarta==0 ;i++){
                    int posicion= i;
                    Carta cartaajugar=maquina.getMano().get(posicion);
                    System.out.println("Carta agarrada"+cartaajugar+"/n");
                    System.out.println(cartaajugar);

                    //Primera regla
                    if(Utilitaria.esIgualCyN(cartaajugar, ultcarta)){
                        lineaDeJuego.add(cartaajugar);
                        jugador.jugarCarta(posicion);
                        turno=0;
                        banderacarta=1;
                        System.out.println("Validacion1.2");
                    }
                        
                
                    //Segunda regla
                    else if(Utilitaria.esCondicion2(cartaajugar, ultcarta)){
                        lineaDeJuego.add(cartaajugar);
                        maquina.getMano().remove(cartaajugar);
                        turno=0;
                        banderacarta=1;
                        System.out.println("prueba 2.2");
                    }
        
                    //Tercera regla
                    else if (Utilitaria.isNegro(cartaajugar)){
                        System.out.println("¿Cuál será el color para el siguiente turno?");
                        Random rd = new Random();
                        Color randomColor = Utilitaria.getRandomColor(rd);
                        while(randomColor.equals(Color.NEGRO)){
                            randomColor = Utilitaria.getRandomColor(rd);
                        }
                        System.out.println("Color aleatorio: " + randomColor);
                        lineaDeJuego.add(cartaajugar);
                        maquina.jugarCarta(posicion);
        
                        turno=0;
                        banderacarta=1;
                    }

                    //quinta regla
        
                    else if(Utilitaria.isReverorBloq(cartaajugar,ultcarta)){
                        System.out.println("Validacion5");
                            lineaDeJuego.add(cartaajugar);
                            maquina.getMano().remove(cartaajugar);
                            System.out.println("Vuelve a ser su turno maquina");
                    }

                    
                } //cierre for*/
            } //cierre turno 1
        } //cierre while

        if (jugador.getMano().isEmpty()){
            System.out.println("Felicidades, has ganado!");
        } 
        else {
            System.out.println("Mala suerte, has perdido!");
        }

    }//cierre iniciar juego

        /* ------------- Notas -------------
         * Para robar carta seria: jugador.anadirCarta(robarCarta()) y maquina.anadirCarta(robarCarta());
         * Habria que crear un metodo en Jugador que permita agregar listas de cartas (anadirCartas(ArrayList<Carta> cartas))
         * Habria que crear un metodo en Juego que devuelva una lista de cartas ArrayList<Carta> robarCartas()
         * Los dos metodos anteriores serviran al momento de usar un +2 o +4
        */

    //-------------------METODOS--------------------//
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