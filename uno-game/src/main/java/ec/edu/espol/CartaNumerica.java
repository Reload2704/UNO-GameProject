package ec.edu.espol;

public class CartaNumerica extends Carta{
    private int valor;

    public CartaNumerica(Color color , int valor){
        super(color);
        this.valor = valor;
    }
    
    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "["+color + ":" + valor + "]";
    }
    
}
