package com.example.prueba1;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
public class Juego {
    private ArrayList<Carta> baraja;
    private Jugador jug1;
    private Jugador jug2;
    private Random rd = new Random();
    private ArrayList<Carta> lineaDeJuego;
    
    public Juego(ArrayList<Carta> baraja, Jugador jug1, Jugador jug2, ArrayList<Carta> lineaDeJuego) {
        this.baraja = baraja;
        this.jug1 = jug1;
        this.jug2 = jug2;
        this.lineaDeJuego = lineaDeJuego;
    }

    public ArrayList<Carta> getBaraja(){
        return baraja;
    }
    public ArrayList<Carta> getLineaJuego(){
        return lineaDeJuego;
    }

    public void setBaraja(ArrayList<Carta> baraja) {
        this.baraja = baraja;
    }
    public void iniciarJuego(){
        /*jugador.setMano(crearMano());
        System.out.println("Mano del jugador "+jugador.getNombre()+": "+ jugador.getMano());
        maquina.setMano(crearMano());
        System.out.println("Mano del jugador "+maquina.getNombre()+": "+ maquina.getMano());
        System.out.println("\n"+this);
        //Se refieren al turno maquina (-1) y turno jugador (1)

        BLOQUE DE CODIGO QUE IRÁ EN EL CONTROLADOR :D
         */
        int turno=0;



        while (!(jug1.getMano().isEmpty() || jug2.getMano().isEmpty())) {
            /*Aqui va la lógica completa del juego.
             * El bucle acaba cuando la mano de uno de los jugadores queda vacia.
            */
            if(turno==0){
                System.out.println("\n-----------------TURNO JUGADOR-----------------");
                System.out.println("Mano del jugador: "+jug1.getMano());
                System.out.println("Linea de Juego: "+lineaDeJuego);
    
                Carta ultcarta=lineaDeJuego.get(lineaDeJuego.size()-1);
                System.out.println("Ultima Carta: "+ultcarta);
            //Cuarta regla(SOLO PARA AGARRAR CARTA)
            if(Utilitaria.iscomodin(ultcarta)){ //se usa la validacion que indica si es +2 o +4 (true basta que solo 1 sea)
                CartaEspecial ct=(CartaEspecial)ultcarta;
                if(ct.getTipo()==TipoEspecial.MAS2){ //si la carta es +2 agarra +2 cartas a su baraja
                    for(int i = 0; i<2; i++){
                        jug1.anadirCarta(robarCarta());
                    }
                    System.out.println("Has robado 2 cartas!");
                } else if(ct.getTipo()==TipoEspecial.MAS4){
                    for(int i = 0; i<4; i++){ //si no es +2, significa que es +4. entonces agarra 4 cartas
                        jug1.anadirCarta(robarCarta());
                    }
                    System.out.println("Has robado 4 cartas!");
                }
                System.out.println("Mano actualizado: "+jug1.getMano());
            }
                Scanner sc = new Scanner (System.in);
                int cont = 0;
                for(Carta c: jug1.getMano()){
                    if(Utilitaria.validacionGeneral(c,ultcarta)){
                        cont+=1;
                    }
                }

                if(cont==0){
                    System.out.println("Usted ha robado una carta!");
                    jug1.anadirCarta(robarCarta());
                    turno=1;
                }
                else{
                System.out.println("¿Cuál es su carta a jugar? (Indique la posición)");
                int position= sc.nextInt()-1;
                sc.nextLine();
                Carta cartaajugar = jug1.getMano().get(position);
                System.out.println(cartaajugar);
                if(cartaajugar instanceof CartaNumerica){
                    CartaNumerica CNajugar = (CartaNumerica) cartaajugar;
                    
                    //Primera regla
                    if(Utilitaria.esIgualCyN(CNajugar, ultcarta)){
                        lineaDeJuego.add(CNajugar);
                        jug1.jugarCarta(position);
                        turno=1;
                        System.out.println("Validacion1");
                    }
                } else {
                    CartaEspecial CEajugar = (CartaEspecial) cartaajugar;
                    
                    //Segunda regla
                    if(Utilitaria.esCondicion2(CEajugar, ultcarta)){
                        lineaDeJuego.add(CEajugar);
                        jug1.jugarCarta(position);
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
                        jug1.jugarCarta(position);
                        System.out.println(colornew);
                        System.out.println("Linea de juego x:" + lineaDeJuego);
                        turno=1;
                    }
                    //quinta regla
                    else if(Utilitaria.isReverorBloq(CEajugar,ultcarta)){
                        System.out.println("Validacion5");
                        lineaDeJuego.add(cartaajugar);
                        jug1.jugarCarta(position);
                        System.out.println("Vuelve a ser su turno");
                    }
                    //Cuarta regla(SOLO PARA AGARRAR CARTA)
                    else if(Utilitaria.iscomodin(ultcarta)){ //se usa la validacion que indica si es +2 o +4 (true basta que solo 1 sea)
                        CartaEspecial ct=(CartaEspecial)ultcarta;
                        if(ct.getTipo()==TipoEspecial.MAS2){ //si la carta es +2 agarra +2 cartas a su baraja
                            for(int i = 0; i<2; i++){
                                jug1.getMano().add(baraja.remove((baraja.size()-1)));
                            }
                        } else{
                            for(int i = 0; i<4; i++){ //si no es +2, significa que es +4. entonces agarra 4 cartas
                                jug1.getMano().add(baraja.remove((baraja.size()-1)));
                            }
                        }
                        turno=1;
                    }
                    //Cuarta regla pt2. (PARA AGREGAR A LINEA DE JUEGO CARTAS +2 Y +4)
                    else if(Utilitaria.iscomodincartajuego(CEajugar,ultcarta)){
                       lineaDeJuego.add(CEajugar);
                       jug1.jugarCarta(position);
                       turno=1;
                       System.out.println("validacion agregar+2 y +4 /n");
                    } else
                        System.out.println("Su carta no es valida, por favor repita");
                }
                Utilitaria.lastCarta(jug1.getMano());
                sc.close();
            }
            } if(turno==1){
                
                System.out.println("\n-----------------TURNO MAQUINA-----------------");
                System.out.println("\nMano maquina: "+jug2.getMano());
                System.out.println("Linea de juego: "+lineaDeJuego);
                //Sacamos la ultima carta de linea de juego
                Carta ultcarta=lineaDeJuego.get(lineaDeJuego.size()-1);
                System.out.println("Ultima carta: "+ultcarta);

                //Validacion si ultCarta es Especial de tipo +2 o +4
                if(Utilitaria.iscomodin(ultcarta)){ //se usa la validacion que indica si es +2 o +4 (true basta que solo 1 sea)
                    CartaEspecial ct=(CartaEspecial)ultcarta;
                    if(ct.getTipo()==TipoEspecial.MAS2){ //si la carta es +2 agarra +2 cartas a su baraja
                        for(int i = 0; i<2; i++){
                            jug2.anadirCarta(robarCarta());
                        }
                        System.out.println("La máquina robó 2 cartas!");
                    } else if(ct.getTipo()==TipoEspecial.MAS4){
                        for(int i = 0; i<4; i++){ //si no es +2, significa que es +4. entonces agarra 4 cartas
                            jug2.anadirCarta(robarCarta());
                        }
                        System.out.println("La máquina robó 4 cartas!");
                    }
                    System.out.println("Mano actualizado: " + jug2.getMano());
                }

                //Seleccion de carta por maquina
                Carta cartaajugar = jug2.validarCartaMaquina(ultcarta);

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
                    Utilitaria.lastCarta(jug2.getMano());
                } else {
                    jug2.anadirCarta(robarCarta());
                    System.out.println("La maquina ha robado una carta!");
                    turno = 0;
                }
            } //cierre turno 1
        } //cierre while
        if (jug1.getMano().isEmpty()){
            System.out.println("Felicidades, has ganado!");
        } 
        else {
            System.out.println("Mala suerte, has perdido!");
        }
    }//cierre iniciar juego

    //-------------------METODOS--------------------//
    public void crearMano1(){
        ArrayList<Carta> manoJug1 = new ArrayList<>();
        for(int i = 0; i<7; i++){
            manoJug1.add(baraja.remove(this.rd.nextInt(baraja.size())));
        }
        jug1.setMano(manoJug1);
    }

    public void crearMano2(){
        ArrayList<Carta> manoJug2 = new ArrayList<>();
        for(int i = 0; i<7; i++){
            manoJug2.add(baraja.remove(this.rd.nextInt(baraja.size())));
        }
        jug2.setMano(manoJug2);
    }

    public Jugador getJug1() {
        return jug1;
    }

    public Jugador getJug2() {
        return jug2;
    }

    public Carta robarCarta(){
        return baraja.remove(baraja.size()-1);
    }
    @Override
    public String toString() {
        return "Linea de Juego: " + lineaDeJuego;
    }
}