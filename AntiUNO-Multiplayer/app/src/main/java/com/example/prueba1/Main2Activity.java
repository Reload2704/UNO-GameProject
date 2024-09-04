package com.example.prueba1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {
    // Componentes de la UI
    ImageView imageViewUltimaCarta;
    ImageView robarButton;
    ImageView cambio_color;
    ImageView unoButton;
    TextView name2_usu;
    TextView numCartas;
    LinearLayout linearLayoutJugador1;
    private String deudor;
    private DatabaseReference roomRef;
    private FirebaseDatabase database;
    private String playerName = ""; // Nombre del jugador actual
    private String roomCode = "";
    private String turnoActual = "";
    private String nombreOtroJugador;
    private boolean isGameReversed = false; // Para manejar el estado de reversa
    private boolean isCardDraw = false;     // Para manejar el estado de cartas de +2 o +4


    // Cartas y manos locales
    ArrayList<Carta> baraja;
    ArrayList<Carta> manoJugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Inicialización de FirebaseDatabase
        database = FirebaseDatabase.getInstance();

        // Obtener el roomCode y nombrelocal del Intent
        roomCode = getIntent().getStringExtra("roomCode");
        playerName = getIntent().getStringExtra("nombrelocal");
        roomRef = database.getReference("rooms/" + roomCode);

        // Inicializar componentes de la UI
        imageViewUltimaCarta = findViewById(R.id.imageViewUltimaCarta);
        name2_usu = findViewById(R.id.name2_usu);
        numCartas = findViewById(R.id.numCartas);
        linearLayoutJugador1 = findViewById(R.id.linearLayoutJugador1);
        robarButton = findViewById(R.id.robarButton);
        unoButton = findViewById(R.id.unoButton);
        cambio_color = findViewById(R.id.cambio_color);

        // Crear la baraja y repartir las cartas
        baraja = Utilitaria.crearBaraja(this);
        manoJugador = new ArrayList<>();
        repartirCartas(7);
        setNombreOtroJugador();

        // Verificar el rol del jugador (host o guest)
        verificarRolJugador();


        // Monitorear siempre los cambios en la línea de juego para ambos roles
        monitorizarCambiosEnLineaDeJuego();

        inicializarDeudorSiEsNecesario();

        monitorizarCambiosEnEstadoEspecial();

        // Monitorear el último movimiento
        monitorizarUltimoMovimiento();

        // Monitorear Color Seleccionado
        monitorizarColorSeleccionado();


        // Monitorear si el otro jugador ha gritado UNO
        monitorizarGritoUNO();

        // Agregar funcionalidad al botón de robar carta
        robarButton.setOnClickListener(v -> {
            verificarYRobarCarta();
        });

        // Agregar funcionalidad al botón de gritar UNO
        unoButton.setOnClickListener(v -> {
            if (manoJugador.size() == 1) {
                String mensajeUNO = playerName + " ha gritado UNO!";
                roomRef.child("uno").setValue(mensajeUNO);
                Toast.makeText(Main2Activity.this, mensajeUNO, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Main2Activity.this, "No puedes gritar UNO si no tienes una sola carta.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void monitorizarGritoUNO() {
        roomRef.child("uno").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String mensajeUNO = snapshot.getValue(String.class);
                    if (mensajeUNO != null && !mensajeUNO.contains(playerName)) {
                        // Mostrar notificación al otro jugador
                        Toast.makeText(Main2Activity.this, mensajeUNO + " ¡Es tu turno!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error al monitorear el grito de UNO: " + error.getMessage());
            }
        });
    }


    private void verificarPenalizacionUNO() {
        if (manoJugador.size() == 1) {
            roomRef.child("uno").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String gritoUNO = snapshot.getValue(String.class);
                    if (gritoUNO == null || !gritoUNO.contains(playerName)) {
                        // Penalizar al jugador porque no gritó UNO
                        añadirCartasAMano(2);  // Añadir dos cartas como penalización
                        Toast.makeText(Main2Activity.this, "No gritaste UNO, se te han añadido 2 cartas.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", "Error al verificar el grito de UNO: " + error.getMessage());
                }
            });
        }
    }



    private void monitorizarUltimoMovimiento() {
        roomRef.child("ultimoMovimiento").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String mensaje = snapshot.getValue(String.class);
                    if (mensaje != null && !mensaje.contains(playerName)) {
                        // Mostrar notificación al jugador
                        Toast.makeText(Main2Activity.this, mensaje + " Es tu turno ahora.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error al monitorear el último movimiento: " + error.getMessage());
            }
        });
    }


    private void verificarYRobarCarta() {
        roomRef.child("turnoActual").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String turnoActual = snapshot.getValue(String.class);

                if (turnoActual != null && turnoActual.equals(playerName)) {
                    verificarPenalizacionUNO();  // Verificar si debe penalizarse al jugador
                    añadirCartasAMano(1);  // Robar una carta y actualizar la vista

                    // Notificar al otro jugador que se robó una carta y cambiar el turno
                    roomRef.child("ultimoMovimiento").setValue(playerName + " ha robado una carta.");

                    cambiarTurno();  // Pasar el turno al siguiente jugador
                } else {
                    Toast.makeText(Main2Activity.this, "No es tu turno para robar una carta.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error al verificar el turno actual: " + error.getMessage());
            }
        });
    }




    private void verificarRolJugador() {
        roomRef.child("players").child(playerName).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String role = dataSnapshot.getValue(String.class);
                    if ("host".equals(role)) {
                        // Si es el host, crear y subir la carta inicial
                        inicializarLineaDeJuego();
                    } else if ("guest".equals(role)) {
                        // Si es guest, esperar a que el host suba la carta inicial y luego mostrarla
                        esperarCartaInicialDeHost();
                    }
                    // Monitorear siempre los cambios en la línea de juego para ambos roles
                    monitorizarCambiosEnLineaDeJuego();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error al obtener el rol del jugador: " + databaseError.getMessage());
            }
        });
    }



    private void esperarCartaInicialDeHost() {
        roomRef.child("lineaJuego").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Obtener la primera carta de la línea de juego
                    DataSnapshot primeraCartaSnapshot = snapshot.getChildren().iterator().next();
                    Map<String, Object> cartaData = (Map<String, Object>) primeraCartaSnapshot.getValue();

                    // Mostrar la carta en el ImageView
                    int imagenResId = ((Long) cartaData.get("imagenResId")).intValue();
                    imageViewUltimaCarta.setImageResource(imagenResId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error al esperar la carta inicial del host: " + databaseError.getMessage());
            }
        });
    }


    public void repartirCartas(int numberOfCards) {
        if (manoJugador.isEmpty()) {
            manoJugador.clear();
        }
        linearLayoutJugador1.removeAllViews(); // Limpiar las cartas previas solo en la UI

        for (int i = 0; i < numberOfCards; i++) {
            // Obtener una carta aleatoria de la baraja
            Carta carta = baraja.remove(0);
            manoJugador.add(carta);

            // Crear una vista para la carta
            ImageView cartaView = new ImageView(this);
            cartaView.setImageResource(carta.getImagenResId());

            // Configurar el tamaño y margen de la carta en la UI
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 300); // Tamaño de la carta
            params.setMargins(5, 0, 5, 0); // Margen entre cartas
            cartaView.setLayoutParams(params);

            // Añadir la carta a la vista
            linearLayoutJugador1.addView(cartaView);

            // Configurar el listener para la carta
            cartaView.setOnClickListener(v -> {
                Log.d("CartaOnClick", "Carta clickeada: " + carta.toString());
                jugarCarta(carta);
            });

            // Actualizar la cantidad de cartas en la UI
            numCartas.setText(String.valueOf(manoJugador.size()));
        }
    }

    // Método para añadir cartas sin limpiar la mano
    public void añadirCartasAMano(int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            // Obtener una carta aleatoria de la baraja
            Carta carta = baraja.remove(0);
            manoJugador.add(carta);

            // Crear una vista para la carta
            ImageView cartaView = new ImageView(this);
            cartaView.setImageResource(carta.getImagenResId());

            // Configurar el tamaño y margen de la carta en la UI
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 300); // Tamaño de la carta
            params.setMargins(5, 0, 5, 0); // Margen entre cartas
            cartaView.setLayoutParams(params);

            // Añadir la carta a la vista
            linearLayoutJugador1.addView(cartaView);

            // Configurar el listener para la carta
            cartaView.setOnClickListener(v -> {
                Log.d("CartaOnClick", "Carta clickeada: " + carta.toString());
                jugarCarta(carta);
            });
        }

        // Actualizar la cantidad de cartas en la UI
        numCartas.setText(String.valueOf(manoJugador.size()));
    }


    private void jugarCarta(Carta carta) {
        Log.d("JugarCarta", "Intentando jugar carta: " + carta.toString());

        roomRef.child("turnoActual").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String turnoActual = snapshot.getValue(String.class);

                // Introduce una pausa antes de ejecutar debescarta()
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        debescarta();
                    }
                }, 1500); // 1000 milisegundos = 1 segundo

                if (turnoActual != null && turnoActual.equals(playerName)) {
                    Log.d("JugarCarta", "Es el turno del jugador: " + playerName);

                    // Validar y jugar la carta
                    jugarCartaSiValida(carta);
                } else {
                    Log.d("JugarCarta", "No es el turno del jugador: " + playerName);
                    Toast.makeText(Main2Activity.this, "No es tu turno", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error al obtener el turno actual: " + error.getMessage());
            }
        });
    }


    private void debescarta() {
        roomRef.child("estadoEspecial").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String estado = snapshot.getValue(String.class);
                    // Introduce una pausa antes de ejecutar debescarta()
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            manejarEstadoEspecial(estado);
                        }
                    }, 1500); // 1000 milisegundos = 1 segundo



                    // Elimina el nodo después de manejar el estado
                    roomRef.child("estadoEspecial").removeValue();
//                    roomRef.child("Deudor").removeValue();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar error si es necesario
            }
        });
    }



    private void cambiarTurno() {
        verificarPenalizacionUNO();  // Verificar si debe penalizarse al jugador

        roomRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<String> jugadores = new ArrayList<>();
                    for (DataSnapshot jugadorSnapshot : snapshot.getChildren()) {
                        jugadores.add(jugadorSnapshot.getKey());
                    }

                    int indiceActual = jugadores.indexOf(playerName);
                    int indiceSiguiente = isGameReversed
                            ? (indiceActual - 1 + jugadores.size()) % jugadores.size()
                            : (indiceActual + 1) % jugadores.size();

                    String nuevoTurno = jugadores.get(indiceSiguiente);

                    roomRef.child("turnoActual").setValue(nuevoTurno)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("Firebase", "El turno ha cambiado a: " + nuevoTurno);
                                } else {
                                    Log.e("FirebaseError", "Error al cambiar el turno: " + task.getException().getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error al obtener la lista de jugadores: " + error.getMessage());
            }
        });
    }




    private void monitorizarCambiosEnLineaDeJuego() {
        roomRef.child("lineaJuego").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot ultimaCartaSnapshot = null;

                // Introduce una pausa antes de ejecutar debescarta()
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        debescarta();
                    }
                }, 1500); // 1000 milisegundos = 1 segundo


                // Iterar sobre las cartas en la línea de juego para obtener la última carta
                for (DataSnapshot cartaSnapshot : snapshot.getChildren()) {
                    ultimaCartaSnapshot = cartaSnapshot;
                }

                if (ultimaCartaSnapshot != null) {
                    // Convertir los datos de Firebase en un mapa
                    Map<String, Object> cartaData = (Map<String, Object>) ultimaCartaSnapshot.getValue();

                    int imagenResId = ((Long) cartaData.get("imagenResId")).intValue();

                    // Mostrar la última carta jugada en el ImageView
                    imageViewUltimaCarta.setImageResource(imagenResId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error al monitorear la línea de juego: " + error.getMessage());
            }
        });
    }


    public Carta transformarHashMapACarta(Map<String, Object> ultimaCartaData, Context context) {
        if (ultimaCartaData == null) {
            throw new IllegalArgumentException("Los datos de la carta no pueden ser nulos.");
        }

        String colorString = (String) ultimaCartaData.get("color");
        if (colorString == null || colorString.isEmpty()) {
            throw new IllegalArgumentException("El color de la carta es nulo o no está presente.");
        }

        Color color;
        try {
            color = Color.valueOf(colorString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Color de carta inválido: " + colorString);
        }

        if (ultimaCartaData.containsKey("numero")) {
            Object numeroObj = ultimaCartaData.get("numero");
            int valor;

            // Verifica si numeroObj es Long o String y conviértelo correctamente
            if (numeroObj instanceof Long) {
                valor = ((Long) numeroObj).intValue();
            } else if (numeroObj instanceof String) {
                valor = Integer.parseInt((String) numeroObj);
            } else {
                throw new IllegalArgumentException("Tipo inesperado para numero: " + numeroObj.getClass().getName());
            }

            return new CartaNumerica(context, color, valor);
        } else if (ultimaCartaData.containsKey("tipoEspecial")) {
            String tipoEspecialString = (String) ultimaCartaData.get("tipoEspecial");
            if (tipoEspecialString == null) {
                throw new IllegalArgumentException("El tipo especial de la carta es nulo.");
            }
            TipoEspecial tipoEspecial;
            try {
                tipoEspecial = TipoEspecial.valueOf(tipoEspecialString.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Tipo especial inválido: " + tipoEspecialString);
            }
            return new CartaEspecial(context, color, tipoEspecial);
        } else {
            throw new IllegalArgumentException("Datos de la carta incompletos o no válidos.");
        }
    }


    private void inicializarDeudorSiEsNecesario() {
        roomRef.child("Deudor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // Establecer un valor predeterminado para el nodo "Deudor"
                    roomRef.child("Deudor").setValue("NombrePorDefecto");  // Puedes ajustar "NombrePorDefecto" según tu lógica
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error al verificar o inicializar el nodo 'Deudor': " + error.getMessage());
            }
        });
    }




    private void inicializarLineaDeJuego() {
        // Crear el nodo 'lineaJuego' y asegurarse de que se crea correctamente
        roomRef.child("lineaJuego").setValue("inicializando")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Si el nodo se creó correctamente, entonces procedemos a subir la carta inicial
                        Carta cartaInicial = obtenerCartaInicial();

                        // Subir la carta inicial al nodo 'lineaJuego'
                        roomRef.child("lineaJuego").removeValue(); // Limpiar el valor inicial para evitar conflicto
                        subirDatosAFirebase(cartaInicial);

                        // Mostrar la carta en el ImageView
                        imageViewUltimaCarta.setImageResource(cartaInicial.getImagenResId());
                    } else {
                        Log.e("FirebaseError", "Error al crear el nodo lineaJuego: " + task.getException().getMessage());
                    }
                });
    }


    private Carta obtenerCartaInicial() {
        Random rd = new Random();
        Carta cartaInicial = null;

        // Buscar una carta inicial que no sea comodín
        while (cartaInicial == null) {
            int numal = rd.nextInt(baraja.size());
            Carta cr = baraja.get(numal);
            if (cr instanceof CartaNumerica) { // Verifica que sea una carta numérica
                cartaInicial = cr;
                baraja.remove(cr); // Remover la carta de la baraja
            }
        }

        return cartaInicial;
    }

    private void subirDatosAFirebase(Carta cr) {
        // Subir la carta inicial a la línea de juego en Firebase
        roomRef.child("lineaJuego").push().setValue(cr.toMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Carta inicial subida con éxito.");
                    } else {
                        Log.e("FirebaseError", "Error al subir la carta inicial: " + task.getException().getMessage());
                    }
                });
    }

    private void jugarCartaSiValida(Carta carta) {
        // Obtener la última carta en la línea de juego desde Firebase
        roomRef.child("lineaJuego").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot ultimaCartaSnapshot = snapshot.getChildren().iterator().next();
                    Map<String, Object> ultimaCartaData = (Map<String, Object>) ultimaCartaSnapshot.getValue();

                    if (ultimaCartaData != null) {
                        Carta ultimaCarta = transformarHashMapACarta(ultimaCartaData, getApplicationContext());

                        // Validar la carta usando el método validacionGeneral de Utilitaria
                        if (Utilitaria.validacionGeneral(carta, ultimaCarta)) {
                            // La carta es válida, proceder a jugarla
                            int index = manoJugador.indexOf(carta);
                            if (index != -1) {
                                // Eliminar la vista de la carta
                                linearLayoutJugador1.removeViewAt(index);
                                // Eliminar la carta de la mano del jugador
                                manoJugador.remove(index);

                                // Mostrar la carta jugada en la línea de juego
                                imageViewUltimaCarta.setImageResource(carta.getImagenResId());

                                actualizarImageViewColor(carta.getColor());

                                // Subir la carta jugada a Firebase
                                roomRef.child("lineaJuego").push().setValue(carta.toMap());

                                // Manejar cartas especiales
                                manejarCartaEspecial(carta);

                                // Actualizar la cantidad de cartas en la UI
                                numCartas.setText(String.valueOf(manoJugador.size()));

                                if (carta instanceof CartaEspecial){
                                    manejarCartaEspecial(carta);
                                    CartaEspecial cartaEspecial = (CartaEspecial) carta;
                                    TipoEspecial tipoEspecial = cartaEspecial.getTipo();

                                    // Introduce una pausa antes de ejecutar debescarta()
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (tipoEspecial == TipoEspecial.REVERSE) {
                                                roomRef.child("turnoActual").setValue(playerName);
                                            } else if (tipoEspecial == TipoEspecial.BLOQUEO) {
                                                roomRef.child("turnoActual").setValue(playerName);
                                            } else if (tipoEspecial == TipoEspecial.MAS2) {
                                                cambiarTurno();
                                            } else if (tipoEspecial == TipoEspecial.MAS4) {
                                                cambiarTurno();}
                                        }
                                    }, 2500); // 1000 milisegundos = 1 segundo
                                }else{
                                    // Cambiar de turno
                                    cambiarTurno();}

                            }
                        } else {
                            // La carta no es válida
                            Toast.makeText(Main2Activity.this, "Carta inválida, selecciona otra.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Log.e("Main2Activity", "No hay cartas en la línea de juego para validar.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error al obtener la última carta de la línea de juego: " + error.getMessage());
            }
        });
    }

    // Método para contar cartas en el LinearLayout y subir el resultado a Firebase
    public void digomiscartas() {
        // Contar la cantidad de cartas en el LinearLayout
        int numCartas = linearLayoutJugador1.getChildCount();

        // Obtener referencia a Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rooms/" + roomCode + "/players");

        // Navegar hasta el nodo del jugador actual
        myRef.child(playerName).child("mano").setValue(String.valueOf(numCartas))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Éxito al subir la cantidad de cartas
                        Toast.makeText(this, "Cantidad de cartas actualizada en Firebase", Toast.LENGTH_SHORT).show();
                    } else {
                        // Error al subir la cantidad de cartas
                        Toast.makeText(this, "Error al actualizar la cantidad de cartas en Firebase", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método para leer la mano del rival desde Firebase y actualizar un TextView
    public void cartasrival() {
        // Obtener referencia a Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference playersRef = database.getReference("rooms/" + roomCode + "/players");

        // Leer los datos de los jugadores
        playersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot playerSnapshot : dataSnapshot.getChildren()) {
                    String playerKey = playerSnapshot.getKey();

                    // Verificar si el nodo es del rival (diferente a playerName)
                    if (!playerKey.equals(playerName)) {
                        // Obtener la cantidad de cartas en la mano del rival
                        String cantidadManoRival = playerSnapshot.child("mano").getValue(String.class);

                        // Actualizar el TextView con la cantidad de cartas del rival
                        numCartas.setText(String.valueOf(cantidadManoRival));
                        break; // Ya encontramos al rival, no es necesario seguir iterando
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar posibles errores al leer la base de datos
                Toast.makeText(getApplicationContext(), "Error al leer los datos del rival", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Monitorear los cambios en el estado especial (reversa, +2, +4)
    private void monitorizarCambiosEnEstadoEspecial() {
        roomRef.child("estadoEspecial").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String estado = snapshot.getValue(String.class);
                    manejarEstadoEspecial(estado);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error al monitorear el estado especial: " + error.getMessage());
            }
        });
    }

    // Manejo de los diferentes estados especiales
    private void manejarEstadoEspecial(String estado) {
        roomRef.child("Deudor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Obtén el nombre del deudor desde Firebase
                    // Introduce una pausa antes de ejecutar debescarta()
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String deudor = snapshot.getValue(String.class);
                        }
                    }, 2000);

                    // Verifica si el jugador actual es el deudor
                    if (playerName.equals(deudor)) {
                        Log.d(deudor,""+deudor);
                        // El deudor debe tomar cartas según el estado
                        if ("draw2".equals(estado)) {
                            añadirCartasAMano(2);
                            roomRef.child("Deudor").setValue("default");

                        } else if ("draw4".equals(estado)) {
                            añadirCartasAMano(4);
                            roomRef.child("Deudor").setValue("default");
                        }
                    } else {
                        Log.d("EstadoEspecial", "El jugador actual no es el deudor.");
                    }
                } else {
                    Log.e("Firebase", "El nodo 'Deudor' no existe.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error al obtener el deudor: " + error.getMessage());
            }
        });

    }

    // Manejo de cartas especiales
    private void manejarCartaEspecial(Carta carta) {
        if (carta instanceof CartaEspecial) {
            CartaEspecial cartaEspecial = (CartaEspecial) carta;
            TipoEspecial tipoEspecial = cartaEspecial.getTipo();

            switch (tipoEspecial) {
                case REVERSE:
                    roomRef.child("estadoEspecial").setValue("reverse");
                    break;
                case MAS2:
                    roomRef.child("estadoEspecial").setValue("draw2");
                    roomRef.child("Deudor").setValue(nombreOtroJugador);
                    break;
                case MAS4:
                    roomRef.child("estadoEspecial").setValue("draw4");
                    roomRef.child("Deudor").setValue(nombreOtroJugador);
                    break;
                case BLOQUEO:
                    roomRef.child("estadoEspecial").setValue("skip");
                    break;
                case CAMBIO_DE_COLOR:
                    // Mostrar interfaz para elegir color (esto es un ejemplo)
                    mostrarAlertDialog();
                    break;
            }
        }
    }

    public void mostrarAlertDialog() {
        // Inflar el layout personalizado
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_cambio_color, null);

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setView(dialogView);

        // Crear el diálogo
        AlertDialog dialog = builder.create();

        // Obtener las referencias a los ImageButton
        ImageButton buttonAzul = dialogView.findViewById(R.id.button_azul);
        ImageButton buttonRojo = dialogView.findViewById(R.id.button_rojo);
        ImageButton buttonAmarillo = dialogView.findViewById(R.id.button_amarilla);
        ImageButton buttonVerde = dialogView.findViewById(R.id.button_verde);

        // Configurar las acciones para cada ImageButton
        buttonAzul.setOnClickListener( v ->{
            actualizarColorSeleccionadoEnFirebase("AZUL");
            cambio_color.setImageResource(R.drawable.azul_color);
            dialog.dismiss();
        });

        buttonRojo.setOnClickListener(v -> {
            actualizarColorSeleccionadoEnFirebase("ROJO");
            cambio_color.setImageResource(R.drawable.rojo_color);
            dialog.dismiss();
        });

        buttonAmarillo.setOnClickListener(v->{
            actualizarColorSeleccionadoEnFirebase("AMARILLO");
            cambio_color.setImageResource(R.drawable.amarillo_color);
            dialog.dismiss();
        });

        buttonVerde.setOnClickListener(v ->{
            actualizarColorSeleccionadoEnFirebase("VERDE");
            cambio_color.setImageResource(R.drawable.verde_color);
            dialog.dismiss();
        });

        // Mostrar el diálogo
        dialog.show();
    }
    private void actualizarColorSeleccionadoEnFirebase(String color) {
        roomRef.child("lineaJuego").child("colorSeleccionado").setValue(color)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Color seleccionado actualizado correctamente.");
                    } else {
                        Log.e("FirebaseError", "Error al actualizar el color seleccionado: " + task.getException().getMessage());
                    }
                });
    }
    private void actualizarImageViewColor(Color color){
        int colorResId = 0;
        switch (color){
            case ROJO:
                colorResId = R.drawable.rojo_color;
                break;
            case AZUL:
                colorResId = R.drawable.azul_color;
                break;
            case VERDE:
                colorResId = R.drawable.verde_color;
                break;
            case AMARILLO:
                colorResId = R.drawable.amarillo_color;
                break;
        }
        cambio_color.setImageResource(colorResId);

    }

    private void monitorizarColorSeleccionado() {
        roomRef.child("lineaJuego").child("colorSeleccionado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String colorSeleccionado = snapshot.getValue(String.class);
                    try {
                        // Convertir el String a un valor del enum Color
                        Color colorEnum = Color.valueOf(colorSeleccionado.toUpperCase());
                        actualizarImageViewColor(colorEnum);
                    } catch (IllegalArgumentException e) {
                        Log.e("Main2Activity", "Color inválido recibido: " + colorSeleccionado, e);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error al monitorear el color seleccionado: " + error.getMessage());
            }
        });
    }

    public void setNombreOtroJugador() {
        roomRef.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nombreJugadorActual = playerName;  // Suponiendo que `playerName` es el nombre del jugador actual
                for (DataSnapshot playerSnapshot : snapshot.getChildren()) {
                    String nombreJugador = playerSnapshot.getKey();
                    if (!nombreJugador.equals(nombreJugadorActual)) {
                        nombreOtroJugador = nombreJugador;
                        break;  // Termina el bucle una vez que encuentres al otro jugador
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar error si es necesario
            }
        });
    }
}