/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
                    tabuleiro.Joga(this.jogada(tabuleiro.fen()));
                jogar = false;
            }
        }
    }
    public String jogada(String fen){
        //String ret;
        String aux;
        aux = "";
        //ret = "";
        int dificuldade;
        dificuldade = this.depth;
        
        try {
        SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
            try (SSLSocket socket = (SSLSocket)factory.createSocket("caioalarcon.com", 443)) {
                socket.startHandshake();
                
                try (PrintWriter out = new PrintWriter(
                        new BufferedWriter(
                            new OutputStreamWriter(
                                socket.getOutputStream()
                            )
                        )
                    )   
                ){
                    //System.out.println("GET /stockfish.php?fen=" + fen + "&dificuldade="+dificuldade + " HTTP/1.1");
                
                    out.println("GET /stockfish.php?fen=" + fen + "&dificuldade="+dificuldade + " HTTP/1.1");
                    out.println("Host: caioalarcon.com");
                    out.println("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.90 Safari/537.36");
                    out.println("Accept: text/html");
                    out.println();
                    out.flush();

                    if (out.checkError())
                       System.out.println(
                            "SSLSocketClient:  java.io.PrintWriter error");

                    try (BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()
                            )
                    )) {
                        String inputLine;
                        while ((inputLine = in.readLine()) != null){
                            //ret+=inputLine;
                            aux = inputLine;
                        }}
                }
            }

        } catch (Exception e) {
            aux = e.getMessage();
            rro = true;
        }
        
        return  aux;
    }
}


