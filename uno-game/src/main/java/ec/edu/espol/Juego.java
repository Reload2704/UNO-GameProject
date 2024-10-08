package ec.edu.espol;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
public class Juego {
    private ArrayList<Carta> baraja;
    private Jugador jugador;
    private Jugador maquina;
    private Random rd = new Random();
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

        while (!(jugador.getMano().isEmpty() || maquina.getMano().isEmpty())) {
            /*Aqui va la lógica completa del juego.
             * El bucle acaba cuando la mano de uno de los jugadores queda vacia.
            */
            if(turno==0){
                System.out.println("\n-----------------TURNO JUGADOR-----------------");
                System.out.println("Mano del jugador: "+jugador.getMano());
                System.out.println("Linea de Juego: "+lineaDeJuego);
    
                Carta ultcarta=lineaDeJuego.get(lineaDeJuego.size()-1);
                System.out.println("Ultima Carta: "+ultcarta);
            //Cuarta regla(SOLO PARA AGARRAR CARTA)
            if(Utilitaria.iscomodin(ultcarta)){ //se usa la validacion que indica si es +2 o +4 (true basta que solo 1 sea)
                CartaEspecial ct=(CartaEspecial)ultcarta;
                if(ct.getTipo()==TipoEspecial.MAS2){ //si la carta es +2 agarra +2 cartas a su baraja
                    for(int i = 0; i<2; i++){
                        jugador.anadirCarta(robarCarta());
                    }
                    System.out.println("Has robado 2 cartas!");
                } else if(ct.getTipo()==TipoEspecial.MAS4){
                    for(int i = 0; i<4; i++){ //si no es +2, significa que es +4. entonces agarra 4 cartas
                        jugador.anadirCarta(robarCarta());
                    }
                    System.out.println("Has robado 4 cartas!");
                }
                System.out.println("Mano actualizado: "+jugador.getMano());
            }
                Scanner sc = new Scanner (System.in);
                int cont = 0;
                for(Carta c: jugador.getMano()){
                    if(Utilitaria.validacionGeneral(c,ultcarta)){
                        cont+=1;
                    }
                }

                if(cont==0){
                    System.out.println("Usted ha robado una carta!");
                    jugador.anadirCarta(robarCarta());
                    turno=1;
                }
                else{
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
                        CartaEspecial cartaCambiada=CEajugar;
                        String colornew;
                        do{
                            System.out.println("¿Cuál será el color para el siguiente turno?");
                            colornew= sc.nextLine();
                            sc.nextLine();
                            try{
                                cartaCambiada.setColor(Color.valueOf(colornew.toUpperCase()));
                            }catch (IllegalArgumentException e){
                                System.out.println("Color no válido. Por favor, ingrese un color válido.");
                            }
                        }while(cartaCambiada.getColor() == Color.NEGRO);
                        
                        
                        lineaDeJuego.add(cartaCambiada);
                        jugador.jugarCarta(position);
                        System.out.println(colornew);
                        System.out.println("Linea de juego x:" + lineaDeJuego);
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
                Utilitaria.lastCarta(jugador.getMano());
                sc.close();
            }
            } if(turno==1){
                
                System.out.println("\n-----------------TURNO MAQUINA-----------------");
                System.out.println("\nMano maquina: "+maquina.getMano());
                System.out.println("Linea de juego: "+lineaDeJuego);
                //Sacamos la ultima carta de linea de juego
                Carta ultcarta=lineaDeJuego.get(lineaDeJuego.size()-1);
                System.out.println("Ultima carta: "+ultcarta);

                //Validacion si ultCarta es Especial de tipo +2 o +4
                if(Utilitaria.iscomodin(ultcarta)){ //se usa la validacion que indica si es +2 o +4 (true basta que solo 1 sea)
                    CartaEspecial ct=(CartaEspecial)ultcarta;
                    if(ct.getTipo()==TipoEspecial.MAS2){ //si la carta es +2 agarra +2 cartas a su baraja
                        for(int i = 0; i<2; i++){
                            maquina.anadirCarta(robarCarta());
                        }
                        System.out.println("La máquina robó 2 cartas!");
                    } else if(ct.getTipo()==TipoEspecial.MAS4){
                        for(int i = 0; i<4; i++){ //si no es +2, significa que es +4. entonces agarra 4 cartas
                            maquina.anadirCarta(robarCarta());
                        }
                        System.out.println("La máquina robó 4 cartas!");
                    }
                    System.out.println("Mano actualizado: " + maquina.getMano());
                }

                //Seleccion de carta por maquina
                Carta cartaajugar = maquina.validarCartaMaquina(ultcarta);

                //Validacion de si la carta es null, roba una carta automaticamente
                if(cartaajugar != null){
                    System.out.println("Carta jugada: "+cartaajugar);
                    if(Utilitaria.isReverorBloq(cartaajugar, ultcarta)){
                        System.out.println("Vuelve a ser su turno");
                        lineaDeJuego.add(cartaajugar);
                    } else{
                        lineaDeJuego.add(cartaajugar);
                        turno = 0;
                    }
                    Utilitaria.lastCarta(maquina.getMano());
                } else {
                    maquina.anadirCarta(robarCarta());
                    System.out.println("La maquina ha robado una carta!");
                    turno = 0;
                }
            } //cierre turno 1
        } //cierre while
        if (jugador.getMano().isEmpty()){
            System.out.println("Felicidades, has ganado!");
        } 
        else {
            System.out.println("Mala suerte, has perdido!");
        }
    }//cierre iniciar juego

    //-------------------METODOS--------------------//
    public ArrayList<Carta> crearMano(){
        ArrayList<Carta> manoJug = new ArrayList<>();
        for(int i = 0; i<7; i++){
            manoJug.add(baraja.remove(this.rd.nextInt(baraja.size())));
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