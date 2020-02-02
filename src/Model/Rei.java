/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author caio
 */
public class Rei  extends Peca{
    private ArrayList Roque(){
        ArrayList<Point>A=new ArrayList<>();
        Point p;
        Cor adversario=this.cor==Cor.BRANCO?Cor.PRETO:Cor.BRANCO;
        if(this.movido)
            return null;//Nada de roque se já moveu o rei;
        
        if(Tabuleiro.Ataque(adversario).contains(this.pos()))
            return null;//Nada de roque com rei atacado
        
        p = this.translada(1, 0);
        while(Tabuleiro.findPeca(p)==null)
            p = this.translada(p, 1, 0);
        //Se for torre e não tiver sido movida, o roque é permitido
        if(Tabuleiro.findPeca(p).toString().equals("Torre"))
            if(!Tabuleiro.findPeca(p).movido){
                if(!Tabuleiro.Ataque(adversario).contains(this.translada(1, 0)))
                    A.add(this.translada(2, 0));    
            }
        
        p = this.translada( -1, 0);
        while(Tabuleiro.findPeca(p)==null)
            p = this.translada(p, -1, 0);
        //Se for torre e não tiver sido movida, o roque é permitido
        if(Tabuleiro.findPeca(p).toString().equals("Torre"))
            if(!Tabuleiro.findPeca(p).movido){
                if(!Tabuleiro.Ataque(adversario).contains(this.translada(-1, 0)))
                    A.add(this.translada(-2, 0));
            }
        
        return A;
    }
    @Override
    public ArrayList JogadasPossiveis(){
        ArrayList Aux;
        ArrayList A;
        Aux = this.Ataque();
        A = this.Roque();
        if(A!=null)
            Aux.addAll(A);
        return RemoveMovimentosImpossiveis(Aux);
    }
    
    @Override
    public void Joga(Point p){
        if(Tabuleiro.Vez()==this.cor){
            if(this.Tabuleiro.findPeca(p)!=null)//tem alguma coisa
                this.Tabuleiro.remove(p);

            if(Roque()!=null && Roque().contains(p)){//Se for o roque tem que mexer a torre...
                Peca torre;
                if(p.x!=6){
                    torre = Tabuleiro.findPeca(this.translada(-4, 0));
                    torre.quadrante = this.translada(-1, 0);
                }
                else{
                    torre = Tabuleiro.findPeca(this.translada(3, 0));

                    torre.quadrante = this.translada(1, 0);                    
                }
                torre.movido = true;    
            }

            this.movido = true;
            quadrante = p;
            this.Tabuleiro.TrocaVez();
        }
    }
    @Override
    public ArrayList Ataque(){
        Peca pc;
        Point p;
        int i,j;
        ArrayList<Point> Jogadas = new ArrayList<Point>(0);
        
        for(i=-1;i<2;i++,i=(j==0?i++:i))
            for(j=-1;j<2;j++,j=(i==0?j++:j)){
                p = this.translada(i, j);
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
    
    public Rei(Cor cor, int x, int y,ModelTabuleiro t)  {
        super(cor, x, y, t);
        this.xpos=60;
    }

    
    @Override
    public String toString() {
        return "Rei";
    }
    @Override
    public String tofenString(){
        if(this.cor==Cor.PRETO)return "k";
        return "K";
    }
}
