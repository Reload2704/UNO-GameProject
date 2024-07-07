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
                System.out.println(jugador.getMano());
                System.out.println(lineaDeJuego);
    
                Carta ultcarta=lineaDeJuego.get(lineaDeJuego.size()-1);
                System.out.println(ultcarta);
    
                Scanner sc = new Scanner (System.in);

                System.out.println("¿Cuál es su carta a jugar? (Indique la posición)");
                int position= sc.nextInt()-1;
                sc.nextLine();

                Carta cartaajugar = jugador.getMano().get(position);
                System.out.println(cartaajugar);

                //Primera regla
                if(esIgualCyN(cartaajugar, ultcarta)){
                    lineaDeJuego.add(cartaajugar);
                    jugador.jugarCarta(position);
                    turno=1;
                    System.out.println("Validacion1");
                }
                

           
                //Segunda regla
                else if(esCondicion2(cartaajugar, ultcarta)){
                    lineaDeJuego.add(cartaajugar);
                    jugador.getMano().remove(cartaajugar);
                    turno=1;
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
                    turno=1;
                }
                //quinta regla

                else if(isReverorBloq(cartaajugar,ultcarta)){
                    System.out.println("Validacion5");
                    lineaDeJuego.add(cartaajugar);
                    jugador.getMano().remove(cartaajugar);
                    System.out.println("Vuelve a ser su turno");
                }
            
                //Cuarta regla
                else if(iscomodin(ultcarta)){ //se usa la validacion que indica si es +2 o +4 (true basta que solo 1 sea)
                    System.out.println("Validacion4");
                    Random rd = new Random();
                    CartaEspecial ct=(CartaEspecial)ultcarta;
                    if(ct.getTipo()==TipoEspecial.MAS2) //si la carta es +2 agarra +2 cartas a su baraja
                    for(int i = 0; i<2; i++){
                        jugador.getMano().add(baraja.remove(rd.nextInt(baraja.size())));
                    }
                else{
                    for(int i = 0; i<4; i++){ //si no es +2, significa que es +4. entonces agarra 4 cartas
                        jugador.getMano().add(baraja.remove(rd.nextInt(baraja.size())));
                    }

                 }
                 turno=1;
            }

            else 
            System.out.println("Su carta no es valida, por favor repita");
        
        }

        if(turno==1){

            System.out.println("Mano maquina"+maquina.getMano());
            System.out.println("Linea de juego"+lineaDeJuego);

            //Sacamos la ultima carta de linea de juego
            Carta ultcarta=lineaDeJuego.get(lineaDeJuego.size()-1);
            System.out.println("Ultima carta"+ultcarta);
                
            //Dentro de un for ponemos las condiciones y en cuanto cambie por primera 
            //vez la cartabandera a =1, se deja de realizar el for.

            int i;
            int banderacarta=0;
            for(i=0; banderacarta==0 ;i++){
                int posicion= i;
                Carta cartaajugar=maquina.getMano().get(posicion);
                System.out.println("Carta agarrada"+cartaajugar);
                System.out.println(cartaajugar);

                //Primera regla
                if(esIgualCyN(cartaajugar, ultcarta)){
                    lineaDeJuego.add(cartaajugar);
                    jugador.jugarCarta(posicion);
                    turno=0;
                    banderacarta=1;
                    System.out.println("Validacion1.2");
                }
                    
               
                //Segunda regla
                else if(esCondicion2(cartaajugar, ultcarta)){
                    lineaDeJuego.add(cartaajugar);
                    maquina.getMano().remove(cartaajugar);
                    turno=0;
                    banderacarta=1;
                    System.out.println("prueba 2.2");
                }
    
                //Tercera regla
                else if (isNegro(cartaajugar)){
                    System.out.println("¿Cuál será el color para el siguiente turno?");
                    Random rd = new Random();
                    Color randomColor = getRandomColor(rd);
                    while(randomColor.equals(Color.NEGRO)){
                        randomColor = getRandomColor(rd);
                    }
                    System.out.println("Color aleatorio: " + randomColor);
                    lineaDeJuego.add(cartaajugar);
                    maquina.jugarCarta(posicion);
    
                    turno=0;
                    banderacarta=1;
                }

                //quinta regla
    
                else if(isReverorBloq(cartaajugar,ultcarta)){
                    System.out.println("Validacion5");
                        lineaDeJuego.add(cartaajugar);
                        maquina.getMano().remove(cartaajugar);
                        System.out.println("Vuelve a ser su turno maquina");
                }

                
            } //cierre for
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

    //Cuarta condición(recibe la ultimacarta de linea de juego e indica si es un +2 o +4)
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

    //obtener color aleatorio (Para que la maquina opueda colocar un color random en caso de escoger cambio de color)
    public static Color getRandomColor(Random rd) {
        Color[] colors = Color.values();
        int randomIndex = rd.nextInt(colors.length);
        return colors[randomIndex];
        }


    @Override
    public String toString() {
        return "Linea de Juego: " + lineaDeJuego;
    }
}