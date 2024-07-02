package ec.edu.espol;

public abstract class Carta {
    protected Color color;
    protected CartaNumerica numero;
    protected CartaEspecial especial;

    public Carta(Color color, CartaNumerica numero, CartaEspecial especial){
        this.color = color;
        this.numero = numero;
        this.especial = especial;
    }

    public Carta(Color color){
        this.color= color;
    }


    //Este metodo valida si la carta que se quiere jugar es valida con la carta que está en la parte superior de la pila de juego.
    public abstract boolean esValida(Carta CartaActual);

    public Color getColor() {
        return color;
    }

    public CartaNumerica getNumero() {
        return numero;
    }

    public CartaEspecial getEspecial() {
        return especial;
    } 
}
