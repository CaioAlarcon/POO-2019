package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Observer;
import javax.swing.JPanel;

/**
 *
 * @author jbatista
 */
public class Canvas extends JPanel {
    
    
    private final ArrayList<Observer> observers;

    public Canvas() {
        super();
        observers = new ArrayList<>();
    }
    
    public void registerObserver(Observer ob){
        this.observers.add(ob);
    }
    
    private void drawBoard(Graphics2D g){
        g.setBackground(Color.WHITE);
        g.setColor(Color.GRAY);
        
        float maxWidth=this.getWidth()+4;
        float maxHeight=this.getHeight()+4;
        
        float boardSize = (maxWidth < maxHeight) ? maxWidth : maxHeight;
        int spotSize = Math.round(boardSize/8.0f);
        for(int i = 0; i<8; ++i){
            for(int j = 0; j<8; ++j){
                //varia a cor do quadrante
                if(g.getColor() == Color.WHITE) g.setColor(Color.GRAY);
                else g.setColor(Color.WHITE);

                //Desenha o tabuleiro
                g.fillRect(i*spotSize,j*spotSize,spotSize, spotSize);
            }

            if(g.getColor() == Color.WHITE) g.setColor(Color.GRAY);
            else g.setColor(Color.WHITE);
        }
    }
    

    
    @Override //sobrescrita do metodo paintComponent da classe JPanel
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //desenhaTabuleiro(g);
        Graphics2D g2 = (Graphics2D)g;
        this.drawBoard(g2);
        observers.stream().forEach((ob) -> {
            ob.update(null, g2);
        });
    }
    
}
