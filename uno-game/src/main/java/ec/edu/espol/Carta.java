package ec.edu.espol;

public abstract class Carta {
    protected Color color;

    public Carta(Color color){
        this.color = color;
    }

    


    //Este metodo valida si la carta que se quiere jugar es valida con la carta que está en la parte superior de la pila de juego.
    public abstract boolean esValida(Carta CartaActual); 
}
