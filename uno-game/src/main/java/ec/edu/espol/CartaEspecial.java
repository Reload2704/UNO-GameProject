package ec.edu.espol;

public class CartaEspecial extends Carta{
    private TipoEspecial tipo;

    public CartaEspecial(Color color, TipoEspecial tipo){
        super(color);
        this.tipo = tipo;
    }
    public boolean esValida(Carta CartaActual){
        return true;
    }
    public boolean esComodin(){
        return true;
    }
    public TipoEspecial getTipo() {
        return tipo;
    }


    /// realizar las demas clases
    public static boolean cartapaz(){
        return false;
    }
}
