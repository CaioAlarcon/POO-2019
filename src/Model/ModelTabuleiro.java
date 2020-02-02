package Model;

import Model.Peca.Cor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ModelTabuleiro implements Observer, java.io.Serializable{
    private Cor vez=Cor.BRANCO;//As brancas jogam primeiro
    private ArrayList<Point> UltimaJogada;
    private final  ArrayList<Peca> pecasPretas;
    private final  ArrayList<Peca> pecasBrancas;
    private ArrayList<Point> Possibilities = new ArrayList<>(0);
    private Peca Selected;
    private Point timeW;
    private Point timeB;
    private boolean fimdejogo=false;
    private String vencedor = "";
    private int dificuldadeb = 10;
    private int dificuldadep = 10;
    private boolean Adversario = false;
    private boolean Player = true;
    
    public Cor Vez(){
        if(fimdejogo)return null;
        return vez;
    }
    public boolean fim(){
        return fimdejogo;
    }
    public String fen(){//Retorna a posiçao do tabuleiro na notação fen
        Peca aux;
        Peao p;
        String ret;
        String roque;
        String enPassant;
        enPassant = "";
        roque = "";
        ret = "";
        String sp;
        sp = "%20";
        
        int n=0;
        for(int i=0;i<8;i++,n=0){
            for(int j=0;j<8;j++){
                aux = findPeca(j,i);
                if(aux!=null){
                    if(aux.toString().equals("Peao")){
                        Point ponto;
                        p = (Peao) aux;
                        ponto = p.en_passant();
                        enPassant = Coordenada(ponto);
                    }
                    if(n==0)
                        ret += aux.tofenString();
                    else{
                         ret += n + aux.tofenString();
                         n=0;
                    }
                }
                else
                    n++;
            }
            
            if(i < 7)
                if(n==0)
                    ret += "/";
                else
                    ret += n + "/";
        }
        if(this.vez==Cor.BRANCO)
            ret += sp + "w";
        else
            ret += sp + "b";
        ret += sp;
        
        aux = findPeca(4,7);
        if(aux != null && !aux.movido){//Se não mexeu o rei branco
            aux = findPeca(7,7);
            if(aux != null && !aux.movido)//Se não moveu a torre do rei
                roque+="K";
            aux = findPeca(0,7);
            if(aux != null && !aux.movido)//Se não moveu a torre da dama
                roque+="Q";
        }
        
        aux = findPeca(4,0);
        if(aux != null && !aux.movido){//Se não mexeu o rei preto
            aux = findPeca(7,0);
            if(aux != null && !aux.movido)//Se não moveu a torre do rei
                roque+="k";
            aux = findPeca(0,0);
            if(aux != null && !aux.movido)//Se não moveu a torre da dama
                roque+="q";
        }
        if(roque.equals(""))
            ret += "-";
        else
            ret += roque;
        
        ret += sp + enPassant;
        
        return ret;
    }
    public void SetDificuldade(int c, int dificuldade){
        if(c==1)
            this.dificuldadeb = dificuldade;
        if(c==2)
            this.dificuldadep = dificuldade;
    }
    public int dificuldade(){
        if(vez == Cor.BRANCO)
            return dificuldadeb;
        else
            return dificuldadep;
    }
   
    
    public String Coordenada(Point p){
        String[] letras = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] numeros = {"8", "7", "6", "5", "4", "3", "2", "1"};
        String ret;
        if(p == null)return "-";
        ret = letras[p.x] + numeros[p.y];
        return ret;
    }
    public Point Coordenada(String p){
        Point P = new Point();
        P.x =  ((int)p.charAt(0) - (int)'a');
        P.y =  8-((int)p.charAt(1) - (int)'0');
        return P;
    }
    public ArrayList<Point> Possibilities(){
        return this.Possibilities;
    }
    public ArrayList<Point> UltimaJogada(){
        return this.UltimaJogada;
    }
    public void SetUltimaJogada(ArrayList<Point> UltimaJogada){
        this.UltimaJogada=UltimaJogada;
    }
    public ArrayList<Point> Ataque(Cor c){//Retorna lista de casas atacadas por uma determinada cor
        ArrayList<Point> A = new ArrayList();
        ArrayList<Peca> Aux;
        ArrayList<Point> p;
        Aux = c == Cor.BRANCO?pecasBrancas:pecasPretas;
        
        for(Peca pc:Aux){
            p = pc.Ataque();
            A.addAll(p);
        }
        return A;
    }
    public boolean Check(Cor c){//Verifica se o time de cor c está em cheque
        ArrayList<Peca> Aux;
        Peca rei = null;
        Cor adversaria;
        adversaria = c==Cor.BRANCO?Cor.PRETO:Cor.BRANCO;
        
        Aux = c == Cor.BRANCO?pecasBrancas:pecasPretas;
        for(Peca pc:Aux){//Procura o Rei
            if(pc.toString().equals("Rei")){
                rei = pc;
                break;
            }
        }
        return this.Ataque(adversaria).contains(rei.pos());
    }
    public boolean CheckMate(){
        //Verifica se está em cheque e não tem o que fazer
        ArrayList<Peca> Aux;
        ArrayList<Point> tudo=new ArrayList<>();
        ArrayList<Point> x;
        
        if(Check(this.vez)){
            Aux = this.vez == Cor.BRANCO?pecasBrancas:pecasPretas;
            for(Peca pc:Aux){
                x = pc.JogadasPossiveis();
                if(x!=null)
                    tudo.addAll(x);
            }
            if(tudo.isEmpty()){//Está em ceque e não tem o que fazer
                fimdejogo = true;
                if(this.vez!=Cor.BRANCO)
                    vencedor = "Brancas";
                else
                    vencedor = "Pretas";
                return true;
            }
        }
            
        return false;
    }
    public void Joga(String jogada){//Joga a partir da notação padrão de jogada. Ex: d2d4
        if(fimdejogo)return;
        Peca pc;
        Point aux;
        //Encontrar a peça a partir dos dois primeiros caractéres
        pc = findPeca(Coordenada(jogada.substring(0,2)));
        //mandar jogar baseado nos dois ultimos
        try{
            aux = Coordenada(jogada.substring(2));
            if(pc!= null && pc.JogadasPossiveis().contains(aux))
                pc.Joga(aux);
            
        }finally{}        
        
    }
    public void TrocaVez(){
        this.vez = this.vez==Cor.BRANCO?Cor.PRETO:Cor.BRANCO;
    }
    public void Click(Point j){
        Peca aux;
        aux = findPeca(j.x,j.y);
        if(Possibilities != null && Possibilities.contains(j)){//É uma possibilidade destacada
            Selected.Joga(j);
            Possibilities = null;
            return;
        }
        
        if(aux!=null && aux.cor == this.vez){//Tem peça e é vez dela
            Selected = aux;
            Possibilities = aux.JogadasPossiveis();
        }
        else
            Possibilities = null;
    }
    public String vencedor(){
        return vencedor;
    }
    public ModelTabuleiro()  {
        this.pecasPretas = new ArrayList<>();
        this.pecasBrancas  = new ArrayList<>();
        
        init();
    }
    void add(Peca pc){
        ArrayList<Peca> aux;
        aux = pc.cor==Cor.BRANCO?pecasBrancas:pecasPretas;
        aux.add(pc);
    }

    public Point time1(){
        return timeW;
    }
    public Point time2(){
        return timeB;
    }
    public void time1d(){
        timeW.x--;
        if(timeW.x<0){
            timeW.x=59;
            timeW.y--;
        }
        if(timeW.y+timeW.x==0){
            fimdejogo=true;
            vencedor = "Pretas";
        }
    }
    public void time2d(){
        timeB.x--;
        if(timeB.x<0){
            timeB.x=59;
            timeB.y--;
        }
        if(timeB.y+timeB.x==0){
            fimdejogo=true;
            vencedor = "Brancas";
        }
    }
    private void SetTime(int i){
        timeW.x=0;
        timeW.y=i;
        timeB.x=0;
        timeB.y=i;
    }
    
    public void init(int t){
        init();
        SetTime(t);
    }
    public void init() {
        pecasBrancas.clear();
        pecasPretas.clear();
        vez = Cor.BRANCO;
        
        this.UltimaJogada = null;
        timeW = new Point();
        timeB = new Point();
        fimdejogo = false;
        vencedor = "";
        SetTime(10);
        
        pecasBrancas.add(new Peao(Peca.Cor.BRANCO,0,6,this));
        pecasBrancas.add(new Peao(Peca.Cor.BRANCO,1,6,this));
        pecasBrancas.add(new Peao(Peca.Cor.BRANCO,2,6,this));
        pecasBrancas.add(new Peao(Peca.Cor.BRANCO,3,6,this));
        pecasBrancas.add(new Peao(Peca.Cor.BRANCO,4,6,this));
        pecasBrancas.add(new Peao(Peca.Cor.BRANCO,5,6,this));
        pecasBrancas.add(new Peao(Peca.Cor.BRANCO,6,6,this));
        pecasBrancas.add(new Peao(Peca.Cor.BRANCO,7,6,this));
        pecasBrancas.add(new Torre(Peca.Cor.BRANCO,0,7,this));
        pecasBrancas.add(new Torre(Peca.Cor.BRANCO,7,7,this));
        pecasBrancas.add(new Cavalo(Peca.Cor.BRANCO,1,7,this));
        pecasBrancas.add(new Cavalo(Peca.Cor.BRANCO,6,7,this));
        pecasBrancas.add(new Bispo(Peca.Cor.BRANCO,2,7,this));
        pecasBrancas.add(new Bispo(Peca.Cor.BRANCO,5,7,this));
        pecasBrancas.add(new Rei(Peca.Cor.BRANCO,4,7,this));
        pecasBrancas.add(new Dama(Peca.Cor.BRANCO,3,7,this));
        
        pecasPretas.add(new Peao(Peca.Cor.PRETO,0,1,this));
        pecasPretas.add(new Peao(Peca.Cor.PRETO,1,1,this));
        pecasPretas.add(new Peao(Peca.Cor.PRETO,2,1,this));
        pecasPretas.add(new Peao(Peca.Cor.PRETO,3,1,this));
        pecasPretas.add(new Peao(Peca.Cor.PRETO,4,1,this));
        pecasPretas.add(new Peao(Peca.Cor.PRETO,5,1,this));
        pecasPretas.add(new Peao(Peca.Cor.PRETO,6,1,this));
        pecasPretas.add(new Peao(Peca.Cor.PRETO,7,1,this));
        pecasPretas.add(new Torre(Peca.Cor.PRETO,7,0,this));
        pecasPretas.add(new Torre(Peca.Cor.PRETO,0,0,this));
        pecasPretas.add(new Cavalo(Peca.Cor.PRETO,6,0,this));
        pecasPretas.add(new Cavalo(Peca.Cor.PRETO,1,0,this));
        pecasPretas.add(new Bispo(Peca.Cor.PRETO,5,0,this));
        pecasPretas.add(new Bispo(Peca.Cor.PRETO,2,0,this));
        pecasPretas.add(new Rei(Peca.Cor.PRETO,4,0,this));
        pecasPretas.add(new Dama(Peca.Cor.PRETO,3,0,this));
    }
    public void remove(Peca p){
        if(p!=null && p.cor==Cor.BRANCO)
            pecasBrancas.remove(p);
        else
            pecasPretas.remove(p);
    }
    public void remove(Point p){
        this.remove(this.findPeca(p));
    }
    
    public Peca findPeca(int x, int y) {
        Peca peca = null;
        
        for(Peca p : pecasBrancas){
            if(p.inSquare(x,y)){
                return p;
            }
        }
        
        for(Peca p : pecasPretas){
            if(p.inSquare(x,y)){
                return p;
            }
        }
        
        return peca;
    }
    public Peca findPeca(Point p) {
        if(p==null)return null;
        return findPeca(p.x,p.y);
    }
    
    public void SetPlayer(boolean pla){
        this.Player = pla;
    }
    public boolean GetPLayer(){
        return this.Player;
    }
    public void SetAdversario(boolean adv){
        this.Adversario = adv;
    }
    public boolean GetAdversario(){
        return this.Adversario;
    }
    public boolean VezDePC(){
        if(vez == Cor.BRANCO)
            return !Player;
        else
            return !Adversario;
    }
    public void draw(Graphics2D g){
        //desenha pecas Brancas
        pecasBrancas.stream().forEach((p) -> {
                p.draw(g);
        });
        
        //desenha pecas pretas
        pecasPretas.stream().forEach((p) -> {
                p.draw(g);
        });
    }
    
    
    @Override
    public void update(Observable o, Object arg) {
        draw((Graphics2D) arg);
    }
}
