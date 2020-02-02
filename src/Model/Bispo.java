package Model;

import java.util.ArrayList;

public class Bispo  extends Peca{
    
    @Override
    public ArrayList Ataque(){
        return this.Diagonais();
    }
    
    public Bispo(Cor cor, int x, int y, ModelTabuleiro t)  {
        super(cor, x, y,t);
        this.xpos=240;
    }

   
    @Override
    public String toString() {
        return "Bispo";
    }
    @Override
    public String tofenString(){
        if(this.cor==Cor.PRETO)return "b";
        return "B";
    }
}
