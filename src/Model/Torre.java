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
public class Torre  extends Peca{
    
    @Override
    public ArrayList Ataque(){
        ArrayList<Point> Jogadas = new ArrayList<>(0);
        Jogadas.addAll(this.Horizontal());
        Jogadas.addAll(this.Vertical());
        return Jogadas;
    }
    
    public Torre(Cor cor, int x, int y, ModelTabuleiro t)  {
        super(cor, x, y, t);
        this.xpos = 120;
    }


    @Override
    public String toString() {
        return "Torre";
    }
    @Override
    public String tofenString(){
        if(this.cor==Cor.PRETO)return "r";
        return "R";
    }
}
