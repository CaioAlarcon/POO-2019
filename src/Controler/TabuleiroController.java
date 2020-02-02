/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controler;

import Model.ModelTabuleiro;
import Model.Peca.Cor;
import Model.erro;
import Model.player;
import View.Tabuleiro;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TabuleiroController  extends Thread implements  MouseListener, MouseMotionListener{
    private Tabuleiro view;
    private ModelTabuleiro model;
    private player stockfish;
    private void SalvaJogo() throws FileNotFoundException, IOException{
        try (FileOutputStream fileOut = new FileOutputStream("save.sav")) {
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(model);
            out.close();
        }
    }
    @Override
    public void run(){
        int t=0;
        while(true){
            if(model.VezDePC())
                try {
                    stockfish.joga(model.dificuldade());
                } catch (erro ex) {
                   view.prompt(ex.ErrorMSG());
                }
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(TabuleiroController.class.getName()).log(Level.SEVERE, null, ex);
            }
            t++;
            
            view.repaint();
            if(model.fim() || model.CheckMate())
                view.status(model.vencedor() + " ganham!");
            
            if(t%10==0){//De segundo em segundo...
                try {
                SalvaJogo();
                } catch (IOException ex) {
                    Logger.getLogger(TabuleiroController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(model.Vez()!=null){
                    if(model.Vez()!=Cor.PRETO){
                        model.time1d();
                    }
                        
                    else{
                        model.time2d();
                    }
                }
            }
            view.setTimer1(model.time1().y,model.time1().x);
            view.setTimer2(model.time2().y,model.time2().x);
        }
    }
    public void addView (Tabuleiro view){
        this.view = view;
    }
    
    public void addModel (ModelTabuleiro model){
        this.model = model;
    }
    public void runTabuleiro() throws InterruptedException {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - view.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - view.getHeight()) / 2);
        view.setLocation(x, y);
        
        view.setVisible(true);
        InitPlayer();
    }
    public void InitPlayer(){
        try {
            this.stockfish = new player(this.model);
            this.start();
        } catch (InterruptedException ex) {
            Logger.getLogger(TabuleiroController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();//pega as coordenadas do mouse
        int y = e.getY();
        
        model.Click(new Point(x/60, y/60));
        //Se for casa jogável selecionada em amarelo, jogar
        //Se for peça do jogador, selecionar
        
        if(!model.VezDePC())
           view.SetPossibilities(model.Possibilities());
        else
            view.SetPossibilities(null);
        
        if(model.CheckMate()){
            //System.err.println("Cheque Mate!");
            view.status(model.vencedor() + " ganham!");
        }
        //view.repaint();
        if(model.VezDePC()){
            try {
                stockfish.joga();
            } catch (erro ex) {
                this.view.prompt(ex.ErrorMSG());
            }
        }
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();//pega as coordenadas do mouse
        int y = e.getY();
        view.getClickLabel().setText("x:"+x+"  y:"+y+"   -   Quadrante: ["+x/60+","+y/60+"]");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();//pega as coordenadas do mouse
        int y = e.getY();
        view.getCoordenadaLabel().setText("x:"+x+"  y:"+y+"   -   Quadrante: ["+x/60+","+y/60+"]");
        view.getMouseCoord().setLocation(x, y);
        
        //view.repaint();
    }

}
