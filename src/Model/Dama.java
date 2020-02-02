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
public class Dama  extends Peca{
    
    @Override
    public ArrayList Ataque(){
        ArrayList<Point> Ataques = new ArrayList<>(0);
        Ataques.addAll(this.Horizontal());
        Ataques.addAll(this.Vertical());
        Ataques.addAll(this.Diagonais());
        
        return Ataques;
    }
    
    public Dama(Cor cor, int x, int y,ModelTabuleiro t)  {
        super(cor, x, y,t);
        this.xpos=0;
    }

    
    @Override
    public String toString() {
        return "Dama";
    }
    @Override
    public String tofenString(){
        if(this.cor==Cor.PRETO)return "q";
        return "Q";
    }
}
