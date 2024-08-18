package com.example.prueba1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText usuario_jugador;
    private EditText codigo_sala;
    private Button boton_crear_sala;
    private Button boton_unirse_sala;
    private String playerName = "";
    private String roomCode = "";

    private FirebaseDatabase database;
    private DatabaseReference playerRef;
    private DatabaseReference roomRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        usuario_jugador = findViewById(R.id.usuario_jugador);
        codigo_sala = findViewById(R.id.codigo_sala);
        boton_crear_sala = findViewById(R.id.boton_crear_sala);
        boton_unirse_sala = findViewById(R.id.boton_unirse_sala);

        database = FirebaseDatabase.getInstance();

        preferences = getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("playerName", "");
        roomCode = preferences.getString("roomCode", "");

        if (!playerName.equals("") && !roomCode.equals("")) {
            roomRef = database.getReference("rooms/" + roomCode);
            roomRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        playerRef = roomRef.child("players").child("jug1");
                        Map<String, Object> playerData = new HashMap<>();
                        playerData.put("nombre", playerName);
                        playerData.put("mano", ""); // Valor temporal
                        playerRef.setValue(playerData);
                        roomRef.child("estado").setValue("No iniciado"); // Añadir el nodo estado con valor "No iniciado"
                        updatePreferences("jug1");
                    } else if (snapshot.getChildrenCount() == 1) {
                        playerRef = roomRef.child("players").child("jug2");
                        Map<String, Object> playerData = new HashMap<>();
                        playerData.put("nombre", playerName);
                        playerData.put("mano", ""); // Valor temporal
                        playerRef.setValue(playerData);
                        roomRef.child("estado").setValue("No iniciado"); // Asegurar que el nodo estado exista
                        updatePreferences("jug2");
                    } else {
                        Toast.makeText(MainActivity.this, "La sala ya está llena", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (playerRef != null) {
                        addRoomEventListener();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Error al acceder a la sala", Toast.LENGTH_SHORT).show();
                }
            });
        }

        boton_crear_sala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerName = usuario_jugador.getText().toString();
                roomCode = codigo_sala.getText().toString();

                if (!playerName.equals("") && roomCode.length() == 4) {
                    boton_crear_sala.setText("CREANDO SALA...");
                    boton_crear_sala.setEnabled(false);
                    roomRef = database.getReference("rooms/" + roomCode);
                    roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                boton_crear_sala.setText("CREAR SALA");
                                boton_crear_sala.setEnabled(true);
                                Toast.makeText(MainActivity.this, "La sala ya existe", Toast.LENGTH_SHORT).show();
                            } else {
                                playerRef = roomRef.child("players").child("jug1");
                                Map<String, Object> playerData = new HashMap<>();
                                playerData.put("nombre", playerName);
                                playerData.put("mano", ""); // Valor temporal
                                playerRef.setValue(playerData);
                                roomRef.child("estado").setValue("No iniciado"); // Añadir el nodo estado con valor "No iniciado"
                                updatePreferences("jug1");
                                addRoomEventListener();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            boton_crear_sala.setText("CREAR SALA");
                            boton_crear_sala.setEnabled(true);
                            Toast.makeText(MainActivity.this, "Error al crear la sala", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        boton_unirse_sala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerName = usuario_jugador.getText().toString();
                roomCode = codigo_sala.getText().toString();
                if (!playerName.equals("") && roomCode.length() == 4) {
                    boton_unirse_sala.setText("UNIÉNDOSE A LA SALA...");
                    boton_unirse_sala.setEnabled(false);
                    roomRef = database.getReference("rooms/" + roomCode);
                    roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                long playerCount = snapshot.child("players").getChildrenCount();
                                if (playerCount < 2) {
                                    if (playerCount == 0) {
                                        playerRef = roomRef.child("players").child("jug1");
                                        Map<String, Object> playerData = new HashMap<>();
                                        playerData.put("nombre", playerName);
                                        playerData.put("mano", ""); // Valor temporal
                                        playerRef.setValue(playerData);
                                        updatePreferences("jug1");
                                    } else {
                                        playerRef = roomRef.child("players").child("jug2");
                                        Map<String, Object> playerData = new HashMap<>();
                                        playerData.put("nombre", playerName);
                                        playerData.put("mano", ""); // Valor temporal
                                        playerRef.setValue(playerData);
                                        updatePreferences("jug2");
                                    }
                                    roomRef.child("estado").setValue("No iniciado"); // Asegurar que el nodo estado exista
                                    addRoomEventListener();
                                } else {
                                    boton_unirse_sala.setText("UNIRSE A SALA");
                                    boton_unirse_sala.setEnabled(true);
                                    Toast.makeText(MainActivity.this, "La sala está llena", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                boton_unirse_sala.setText("UNIRSE A SALA");
                                boton_unirse_sala.setEnabled(true);
                                Toast.makeText(MainActivity.this, "La sala no existe", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            boton_unirse_sala.setText("UNIRSE A SALA");
                            boton_unirse_sala.setEnabled(true);
                            Toast.makeText(MainActivity.this, "Error al unirse a la sala", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void addRoomEventListener() {
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!playerName.equals("") && !roomCode.equals("")) {
                    SharedPreferences preferences = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("playerID", playerRef.getKey());
                    editor.putString("roomCode", roomCode);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                    intent.putExtra("roomCode", roomCode);
                    intent.putExtra("nombrelocal",playerName);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                boton_crear_sala.setText("CREAR SALA");
                boton_crear_sala.setEnabled(true);
                boton_unirse_sala.setText("UNIRSE A SALA");
                boton_unirse_sala.setEnabled(true);
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePreferences(String playerID) {
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("playerID", playerID);
        editor.putString("roomCode", roomCode);
        editor.apply();
    }
}
