/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;
import java.util.*;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author jbatista
 */
public abstract class Peca  implements java.io.Serializable{
    protected int xpos;
    protected boolean movido=false;
    protected final static String imgPath = "img/ChessPiecesArray.png";
    protected static BufferedImage pecasImg = null;    
    protected Cor cor;
    protected Point quadrante;
    protected ModelTabuleiro Tabuleiro;
    
    
    /***Funções dos movimentos das peça
     * @return s***/
    
    //Retorna conjunto de jogadas possíveis para todas as  diagonais
    //considerando apenas impenetrabilidade
    protected ArrayList Diagonais(){
        Point p;
        Peca pc;
        int i,j;
        ArrayList <Point> aux = new ArrayList<>();
        
        for(i=-1;i<2;i++,i++)
            for(j=-1;j<2;j++,j++){
                p=this.pos();
                while(true){
                    p = this.translada(p, i, j);
                    if(p==null)//Saiu do tabuleiro, para
                        break;
                    pc = Tabuleiro.findPeca(p);
                    if(pc!=null)
                        if(pc.cor == this.cor)//Cor igual, para
                            break;
                        else{//Cor diferente, add e para
                            aux.add(p);
                            break;
                        }
                    else
                        aux.add(p);
                }
            }
        return aux;
    }
    
    protected ArrayList Horizontal(){
        Point p;
        Peca pc;
        int i=0,j;
        ArrayList <Point> aux = new ArrayList<>();
        for(j=-1;j<2;j++,j++){
            p=this.pos();
            while(true){
                p = this.translada(p, i, j);
                if(p==null)//Saiu do tabuleiro, para
                    break;
                pc = Tabuleiro.findPeca(p);
                if(pc!=null)
                    if(pc.cor == this.cor)//Cor igual, para
                        break;
                    else{//Cor diferente, add e para
                        aux.add(p);
                        break;
                    }
                else
                    aux.add(p);
            }
        }
        return aux;
    }
    
    protected ArrayList Vertical(){
        Point p;
        Peca pc;
        int i,j=0;
        ArrayList <Point> aux = new ArrayList<>();
        
        for(i=-1;i<2;i++,i++){
            p=this.pos();
            while(true){
                p = this.translada(p, i, j);
                if(p==null)//Saiu do tabuleiro, para
                    break;
                pc = Tabuleiro.findPeca(p);
                if(pc!=null)
                    if(pc.cor == this.cor)//Cor igual, para
                        break;
                    else{//Cor diferente, add e para
                        aux.add(p);
                        break;
                    }
                else
                    aux.add(p);
            }
        }
        return aux;
    }
    
    private Point ValidaPonto(Point p){
        if(p.x<0)return null;
        if(p.x>7)return null;
        if(p.y<0)return null;
        if(p.y>7)return null;
        return p;
    }
    protected Point translada(Point origem, int dx, int dy){
        if(origem == null) return null;
        return ValidaPonto(new Point(origem.x+dx,origem.y+dy)); 
    }
    protected Point translada(Peca origem, int dx, int dy){
        return ValidaPonto(new Point(origem.pos().x+dx,origem.pos().y+dy)); 
    }
    protected Point translada(int dx, int dy){
        return ValidaPonto(new Point(this.pos().x+dx,this.pos().y+dy)); 
    }
    public Point pos(){
        return quadrante;
    }
    
    public abstract ArrayList Ataque();
    public abstract String tofenString();
    
    protected ArrayList<Point>RemoveMovimentosImpossiveis(ArrayList<Point> Movimentos){
        Point ant;
        Peca pc;
        ArrayList<Point> invalidos = new ArrayList<>();
        //A ideia é remover das jogadas de ataque as jogadas possíveis
        //Para isso é necessário simular cada jogada e ver se o rei se encontra em xeque
        for(Point x: Movimentos){
            ant = this.quadrante;
            pc=Tabuleiro.findPeca(x);
            if(pc!=null)
                Tabuleiro.remove(pc);
            
            this.quadrante = x;
            
            if(Tabuleiro.Check(this.cor))
                invalidos.add(x);
            if(pc!=null)
                Tabuleiro.add(pc);
            
            this.quadrante=ant;
        }
        
        Movimentos.removeAll(invalidos);
        return Movimentos;
    }
    public ArrayList JogadasPossiveis(){
        return RemoveMovimentosImpossiveis(this.Ataque());
    }
    
    public enum Cor{
        PRETO,
        BRANCO
    }
    
    public Peca(Cor cor, int x, int y,ModelTabuleiro t)  {
        this.Tabuleiro = t;
        this.cor = cor;
        this.quadrante = new Point(x,y);
        if(pecasImg == null){
            try {
                pecasImg = ImageIO.read(new File(imgPath));
            } catch (IOException ex) {
                Logger.getLogger(Peca.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void Joga(Point p){
        ArrayList<Point> aux = new ArrayList<>();
        if(Tabuleiro.Vez()==this.cor){//Só joga se for mesmo sua vez
            if(this.Tabuleiro.findPeca(p)!=null)//tem alguma coisa
                this.Tabuleiro.remove(p);
            this.movido = true;
            aux.add(0,quadrante);
            aux.add(1,p);
            Tabuleiro.SetUltimaJogada(aux);
            quadrante = p;
            this.Tabuleiro.TrocaVez();
        }
    }
    
    public boolean inSquare(int x, int y){
        return x == quadrante.x && y == quadrante.y;
    }
    
    public void setQuadrante(int x, int y){
        quadrante.setLocation(x, y);
    }
    
    public void draw(Graphics2D g) {
        int squareWidth = 60;
        int squareHeight = 60;
        
        int x0 = quadrante.x * squareWidth;
        int y0 = quadrante.y * squareHeight;
        int x1 = x0 + squareWidth;
        int y1 = y0 + squareHeight;
        
        if(pecasImg == null){
            try {
                pecasImg = ImageIO.read(new File(imgPath));
            } catch (IOException ex) {
                Logger.getLogger(Peca.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(this.cor == Peca.Cor.PRETO){
            g.drawImage(pecasImg, x0, y0, x1, y1, xpos, 0, xpos+60, 60, null);
        } else {
            g.drawImage(pecasImg, x0, y0, x1, y1, xpos, 60, xpos+60, 120, null);
        }
    }
}
