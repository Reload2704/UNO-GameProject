package ec.edu.espol;

public class CartaEspecial extends Carta{
    private TipoEspecial tipo;

    public CartaEspecial(Color color, TipoEspecial tipo){
        super(color);
        this.tipo = tipo;
    }
    public TipoEspecial getTipo() {
        return tipo;
    }
    @Override
    // implemente esto para imprimir las cartas y saber si estaba bien.
    public String toString() {
        return "{" + color + ":" + tipo + "}";
    }
    

}
