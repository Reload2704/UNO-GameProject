package com.example.prueba1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class room_sala extends AppCompatActivity {

    private TextView codSala;
    private TextView jugName;
    private TextView jug2Name;
    private Button startButton;
    private String roomCode;
    private String nombrejugador;

    private FirebaseDatabase database;
    private DatabaseReference roomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sala_espera);

        // Referenciar los elementos del layout
        codSala = findViewById(R.id.cod_sala);
        jugName = findViewById(R.id.jug_name);
        jug2Name = findViewById(R.id.jug2_name);
        startButton = findViewById(R.id.start);

        // Obtener el código de la sala desde el intent
        roomCode = getIntent().getStringExtra("roomCode");
        nombrejugador = getIntent().getStringExtra("nombrejugador");

        // Mostrar el código de la sala en la interfaz
        codSala.setText(roomCode);

        // Inicializar Firebase
        database = FirebaseDatabase.getInstance();
        roomRef = database.getReference("rooms/" + roomCode);

        // Deshabilitar el botón de inicio hasta que ambos jugadores estén presentes
        startButton.setEnabled(false);

        // Escuchar los cambios en los jugadores
        roomRef.child("players").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int playerCount = 0;
                for (DataSnapshot playerSnapshot : snapshot.getChildren()) {
                    String playerName = playerSnapshot.child("nombre").getValue(String.class);
                    if (playerCount == 0) {
                        jugName.setText(playerName);
                    } else if (playerCount == 1) {
                        jug2Name.setText(playerName);
                    }
                    playerCount++;
                }

                // Habilitar el botón cuando ambos jugadores estén presentes
                if (playerCount >= 2) {
                    startButton.setEnabled(true);
                } else {
                    startButton.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de errores
            }
        });

        // Configurar el botón "Iniciar Partida"
        startButton.setOnClickListener(v -> {
            // Cambiar el estado de la sala a "Iniciado" en Firebase
            roomRef.child("estado").setValue("Iniciado");

            // Asignar roles a los jugadores
            String hostName = jugName.getText().toString();
            String guestName = jug2Name.getText().toString();

            roomRef.child("players").child(hostName).child("role").setValue("host");
            roomRef.child("players").child(guestName).child("role").setValue("guest");

            // Notificar a ambos jugadores que la partida ha comenzado y redirigirlos a Main2Activity
            roomRef.child("players").child(hostName).child("iniciarJuego").setValue(true);
            roomRef.child("players").child(guestName).child("iniciarJuego").setValue(true);
        });

        // Escuchar cambios para saber cuándo redirigir a Main2Activity
        roomRef.child("players").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot playerSnapshot : snapshot.getChildren()) {
                    Boolean iniciarJuego = playerSnapshot.child("iniciarJuego").getValue(Boolean.class);
                    if (iniciarJuego != null && iniciarJuego) {
                        // Navegar a Main2Activity
                        Intent intent = new Intent(room_sala.this, Main2Activity.class);
                        intent.putExtra("roomCode", roomCode);
                        intent.putExtra("nombrelocal", nombrejugador);
                        startActivity(intent);
                        finish(); // Finalizar room_sala para que no pueda volver a ella con el botón de retroceso
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de errores
            }
        });
    }
}