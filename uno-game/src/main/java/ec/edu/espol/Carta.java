package ec.edu.espol;

public abstract class Carta {
    protected Color color;
    

    protected Carta(Color color){
        this.color = color;
    }
    
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    

}
