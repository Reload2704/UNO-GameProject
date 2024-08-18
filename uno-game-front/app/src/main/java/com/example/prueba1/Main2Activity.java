package com.example.prueba1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {
    private DatabaseReference roomRef;
    private FirebaseDatabase database;
    private String jug1name = "";
    private String jug2name = "";
    private boolean datosSubidos = false; // Control para asegurar que los datos solo se suben una vez

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Inicialización de FirebaseDatabase
        database = FirebaseDatabase.getInstance();

        // Variable random
        Random rd = new Random();

        // Obtener el roomCode del MainActivity
        String roomCode = getIntent().getStringExtra("roomCode");
        String nombrelocal = getIntent().getStringExtra("nombrelocal");
        roomRef = database.getReference("rooms/" + roomCode);

        // Escuchar cambios en el estado del juego
        roomRef.child("estado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String estado = dataSnapshot.getValue(String.class);
                if (estado != null && estado.equals("Iniciado") && !datosSubidos) {
                    Toast.makeText(Main2Activity.this, "El juego ha comenzado!", Toast.LENGTH_SHORT).show();

                        if(jug1name.equals(nombrelocal)){
                            // Mostrar las manos de los jugadores desde Firebase
                            mostrarManoDesdeFirebase("jug1", R.id.linearLayoutJugador1);
                            mostrarManoDesdeFirebase("jug2", R.id.linearLayoutJugador2);
                        }else {
                            mostrarManoDesdeFirebase("jug1", R.id.linearLayoutJugador2);
                            mostrarManoDesdeFirebase("jug2", R.id.linearLayoutJugador1);
                        }
                        datosSubidos = true; // Marcamos que los datos ya se subieron
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error al monitorear el estado: " + error.getMessage());
            }
        });

        // Monitorear el número de jugadores y subir datos cuando estén listos
        roomRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Contar el número de jugadores
                long playerCount = snapshot.getChildrenCount();

                // Verificar si hay dos jugadores en la sala y que los datos no se hayan subido previamente
                if (playerCount == 2 && !datosSubidos) {
                    // Cambiar el estado del juego a "Iniciado"
                    roomRef.child("estado").setValue("Iniciado");

                    // Obtener los nombres de los jugadores
                    jug1name = snapshot.child("jug1").child("nombre").getValue(String.class);
                    jug2name = snapshot.child("jug2").child("nombre").getValue(String.class);

                    // Inicialización de jugadores con los nombres obtenidos
                    Jugador jug1 = new Jugador(jug1name);
                    Jugador jug2 = new Jugador(jug2name);

                    // Creación de la baraja
                    ArrayList<Carta> baraja = Utilitaria.crearBaraja(Main2Activity.this);

                    // Creación de la línea de juego
                    ArrayList<Carta> lineaJuego = new ArrayList<>();

                    // Código para agregar 1 carta en Línea de Juego que no sea comodín
                    while (lineaJuego.isEmpty()) {
                        int numal = rd.nextInt(baraja.size());
                        Carta cr = baraja.get(numal);
                        if (cr instanceof CartaNumerica) {
                            lineaJuego.add(cr);
                            baraja.remove(cr);
                        }
                    }

                    // CREACIÓN DE INSTANCIA DE JUEGO
                    Juego juego = new Juego(baraja, jug1, jug2, lineaJuego);

                    // Crear las manos de los jugadores
                    juego.crearMano1();
                    juego.crearMano2();

                    // Subir la baraja, manos y línea de juego a Firebase
                    subirDatosAFirebase(juego, roomCode);

                    // Marcar que los datos ya fueron subidos
                    datosSubidos = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de errores
                Log.e("FirebaseError", error.getMessage());
            }
        });
    }

    private void mostrarManoDesdeFirebase(String jugadorId, int layoutId) {
        LinearLayout layout = findViewById(layoutId);
        layout.removeAllViews(); // Limpiar cualquier vista existente

        // Obtener la referencia a la mano del jugador en Firebase
        roomRef.child("players").child(jugadorId).child("mano").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Recorrer las cartas en la mano del jugador
                for (DataSnapshot cartaSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> cartaData = (Map<String, Object>) cartaSnapshot.getValue();

                    // Obtener la información de la carta
                    int imagenResId = ((Long) cartaData.get("imagenResId")).intValue(); // Convertir a int
                    String tipo = (String) cartaData.get("tipo");

                    // Crear la vista para la carta
                    ImageView cartaView = new ImageView(Main2Activity.this);
                    cartaView.setImageResource(imagenResId);

                    // Ajustar el tamaño de la carta
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            200, // Ancho de la carta (ajusta según sea necesario)
                            300  // Altura de la carta (ajusta según sea necesario)
                    );

                    // Opcional: Reducir espacio entre cartas
                    params.setMargins(5, 0, 0, 0);
                    cartaView.setLayoutParams(params);

                    // Escalar la imagen
                    cartaView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    // Agregar la carta al layout
                    layout.addView(cartaView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error al cargar la mano: " + databaseError.getMessage());
            }
        });
    }


    private void subirDatosAFirebase(Juego juego, String roomCode) {
        // Convertir la baraja a una lista
        List<Map<String, Object>> barajaList = new ArrayList<>();
        for (Carta carta : juego.getBaraja()) {
            barajaList.add(carta.toMap());
        }

        // Convertir la mano de Jugador 1 a una lista
        List<Map<String, Object>> mano1List = new ArrayList<>();
        for (Carta carta : juego.getJug1().getMano()) {
            mano1List.add(carta.toMap());
        }

        // Convertir la mano de Jugador 2 a una lista
        List<Map<String, Object>> mano2List = new ArrayList<>();
        for (Carta carta : juego.getJug2().getMano()) {
            mano2List.add(carta.toMap());
        }

        // Convertir la línea de juego a una lista
        List<Map<String, Object>> lineaJuegoList = new ArrayList<>();
        for (Carta carta : juego.getLineaJuego()) {
            lineaJuegoList.add(carta.toMap());
        }

        // Subir datos a Firebase
        DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference("rooms").child(roomCode);

        // Subir la baraja
        roomRef.child("baraja").setValue(barajaList);

        // Subir las manos a los respectivos jugadores
        roomRef.child("players").child("jug1").child("mano").setValue(mano1List);
        roomRef.child("players").child("jug2").child("mano").setValue(mano2List);

        // Subir la línea de juego
        roomRef.child("lineaJuego").setValue(lineaJuegoList);
    }
}
