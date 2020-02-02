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
 * @author jbatista
 */

public class Peao  extends Peca{

    @Override
    public ArrayList Ataque() {
        ArrayList<Point> atacadas = new ArrayList<>(0);
        int m;
        m = this.cor==Cor.BRANCO?-1:1;
        atacadas.add(this.translada(-1, m));
        atacadas.add(this.translada(1, m));
        return atacadas;
    }
    
    /**
     *
     * @return
     */
    @Override
    public ArrayList JogadasPossiveis(){
        int m;
        Point aux;
        Peca p;
        ArrayList<Point> Jogadas = new ArrayList<>(0);
        
        //Define a direção do movimento baseado na cor da peça:
        m = this.cor==Cor.BRANCO?-1:1;
        aux = this.translada(0, m);
        
        if(this.Tabuleiro.findPeca(aux)==null)//Se não tem peça é válida
            Jogadas.add(aux);
        
        if(!this.movido){//Se não moveu pode andar duas
            aux = this.translada(0,2*m);
            if(this.Tabuleiro.findPeca(aux)==null)//Mas apenas se a casa estiver vazia!
                if(this.Tabuleiro.findPeca(this.translada(0,m))==null)//E se a anterior também estiver pq peão não pula peça!
                Jogadas.add(aux);
            
        }
        //Define direção de tomada de peças
        p=this.Tabuleiro.findPeca(this.translada(1, m));
        
        if(p!=null && p.cor!=this.cor)
            Jogadas.add(this.translada(1, m));
        
        p=this.Tabuleiro.findPeca(this.translada(-1, m));
        
        if(p!=null && p.cor!=this.cor)
            Jogadas.add(this.translada(-1, m));
        
        //Condição da captura en passant:

        if(en_passant()!=null){
            Jogadas.add(en_passant());
        }
            
        return RemoveMovimentosImpossiveis(Jogadas);
    }
    public Point en_passant(){
        ArrayList<Point> aux;
        int x,y;
        aux = Tabuleiro.UltimaJogada();
        
        if(aux == null)
            return null;
        
        if(this.cor==Cor.BRANCO && this.quadrante.y == 3){
            x=(aux.get(0).y);
            if(this.pos().y==3 && x != 1 )
                return null;

            x=aux.get(1).x;
            if(this.translada(1, 0)!=null)
                y=this.translada(1, 0).x;
            else
                y=-1;
            
            if(y==x)
                return this.translada(1,-1);

            x=aux.get(1).x;
            if(this.translada(-1, 0)!=null)
                y=this.translada(-1, 0).x;
            else
                y=-1;
            
            if(y==x)
                return this.translada(-1,-1);
        }
        
        if(this.cor==Cor.PRETO && this.quadrante.y == 4){
            x=(aux.get(0).y);
            if(this.pos().y==4 && x != 6)
                return null;

            x=aux.get(1).x;
            if(this.translada(1, 0)!=null)
                y=this.translada(1, 0).x;
            else
                y=-1;
            
            if(y==x)
                return this.translada(1,1);

            x=aux.get(1).x;
            if(translada(-1, 0)!=null)
                y=this.translada(-1, 0).x;
            if(y==x)
                return this.translada(-1,1);
        }
        
        return null;
    }
    @Override
    public void Joga(Point p){
        ArrayList<Point> aux = new ArrayList<>();
        if(Tabuleiro.Vez()==this.cor){
            Point pt = this.en_passant(); 
            Peca pc;
            if(p.equals(pt)){
                if(this.cor==Cor.BRANCO)
                    pc = this.Tabuleiro.findPeca(translada(p,0,1));
                else
                    pc = this.Tabuleiro.findPeca(translada(p,0,-1));
                Tabuleiro.remove(pc);
            }
            if(this.Tabuleiro.findPeca(p)!=null)//tem alguma coisa
                this.Tabuleiro.remove(p);
            this.movido = true;
            if(this.cor==Cor.PRETO && p.y==7){//Coroando a dama
                Peca Dama;
                Dama = new Dama(this.cor,p.x,p.y,Tabuleiro);
                Tabuleiro.add(Dama);
                Tabuleiro.remove(this);
            }

            if(this.cor==Cor.BRANCO && p.y==0){//Coroando a outra dama
                Peca Dama;
                Dama = new Dama(this.cor,p.x,p.y,Tabuleiro);
                Tabuleiro.add(Dama);
                Tabuleiro.remove(this);
            }

            aux.add(0,quadrante);
            aux.add(1,p);
            Tabuleiro.SetUltimaJogada(aux);
            quadrante = p;
            this.Tabuleiro.TrocaVez();
        }
    }
    public Peao(Cor cor, int x, int y,ModelTabuleiro t)  {
        super(cor, x, y,t);
        this.xpos=300;
    }
    
    @Override
    public String toString() {
        return "Peao";
    }
    @Override
    public String tofenString(){
        if(this.cor==Cor.PRETO)return "p";
        return "P";
    }


}
