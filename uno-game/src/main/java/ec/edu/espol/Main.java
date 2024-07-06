package ec.edu.espol;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rd = new Random();
        System.out.println("BIENVENIDO AL JUEGO UNO");
        System.out.println("Ingrese su nombre: ");
        String name = sc.nextLine();
        Jugador jug1 = new Jugador(name);
        Jugador maq = new Jugador("Maquina");
        ArrayList<Carta> baraja = Utilitaria.crearBaraja();
        ArrayList<Carta> lineaJuego = new ArrayList<>();

        while (lineaJuego.isEmpty()) {
            int numal = rd.nextInt(baraja.size());
            Carta cr = baraja.get(numal);
            if (cr instanceof CartaNumerica){
                lineaJuego.add(cr);
                baraja.remove(cr);
            }
        }
    }
}