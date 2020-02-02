package Model;

import java.awt.Point;
import java.util.ArrayList;

public class Cavalo  extends Peca {
    
    @Override
    public ArrayList Ataque(){
        Peca pc;
        Point p;
        int i,j;
        ArrayList<Point> Jogadas = new ArrayList<>(0);
        
        for(i=-1;i<2;i++,i=(j==0?i++:i))
            for(j=-1;j<2;j++,j=(i==0?j++:j)){
                p = this.translada(j==0?i*2:(j==-i?-2*i:j),i==0?-j*2:(i==j?2*i:i));
                pc = Tabuleiro.findPeca(p);
                if(p!=null)//Se o ponto existe
                    if(pc!=null){//Se tem peça 
                        if(pc.cor!=this.cor)
                            Jogadas.add(p);//Se a peça que tem é de cor diferente
                    }else//Se não tem peça
                        Jogadas.add(p);
            }
        return Jogadas;
    }
    
    public Cavalo(Cor cor, int x, int y, ModelTabuleiro t)  {
        super(cor, x, y,t);
        this.xpos = 180;
    }

    
    @Override
    public String toString() {
        return "Cavalo";
    }
    @Override
    public String tofenString(){
        if(this.cor==Cor.PRETO)return "n";
        return "N";
    }
}
