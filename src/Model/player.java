/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author Caio
 */
public class player extends Thread{
    private final ModelTabuleiro tabuleiro;
    private boolean jogar=false;
    private int depth;
    private boolean rro=false;
    public player(ModelTabuleiro tabuleiro) throws InterruptedException{
        this.tabuleiro = tabuleiro;
        this.start();
        
    }
    public void joga() throws erro{
        if(rro){
            throw new erro();
        }
        else{
            if(!tabuleiro.fim() && tabuleiro.VezDePC())
                jogar = true;
        }
    }
    public void joga(int dificuldade) throws erro{
        if(rro){
            throw new erro();
        }
        else{
            this.depth = dificuldade;
            jogar = true;
        }
    }
    
    @Override
    public void run(){
        while(true){
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(player.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(jogar){
                if(tabuleiro.VezDePC())
                    try {
                        tabuleiro.Joga(this.jogada(tabuleiro.fen()));
                } catch (IOException ex) {
                    Logger.getLogger(player.class.getName()).log(Level.SEVERE, null, ex);
                }
                jogar = false;
            }
        }
    }
    public String jogada(String fen) throws IOException{
        //String ret;
        String line;
        String aux="";
        
        URL url = new URL("https://alarcon.pw/stockfish.php?fen="+ fen +"&dificuldade=" + this.depth);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            for (line=""; (line = reader.readLine()) != null;) {
                aux += line;
            }
        }
        
        //System.out.println(this.depth);
        
        
        return  aux;
    }
}


