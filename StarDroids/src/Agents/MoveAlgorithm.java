/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import helpers.Pair;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Vicente
 */
public class MoveAlgorithm {
    private Pair<Integer,Integer> posFinal;
    private Pair<Integer,Integer> posActual;
    private Queue<Pair<Integer,Integer>> cola;
    private Set<Pair<Integer,Integer>> activos;
    private int radar[][], scanner[][];
    private int battery;
    private double minValueFind;
    private double []finalPoint;
    private boolean initHeu2;
    private Pair<Integer,Integer>[][]world;
    private String actionAnterior;
    private double minValue;
    
    public MoveAlgorithm(Pair<Integer,Integer> posFinal, Pair<Integer,Integer> posActual, Pair<Integer,Integer>[][]world){
        this.posFinal= posFinal;
        this.posActual= posActual;
        this.world= world;
        radar= new int[3][3];
        scanner= new int[3][3];
        initHeu2= true;
        finalPoint= new double[2];
        String actionAnterior= "";
    }
    
    private boolean isObstacle(Pair<Integer,Integer> pos){
        return ((world[pos.first][pos.second].first == 1) || (world[pos.first][pos.second].first == 2));
    }
    
    private void calcularAdyacentes(Pair<Integer,Integer> pos){
        
        if((pos.first!=0 || pos.first!=499) && (pos.second!=0 || pos.second!=499)){
            
            if( (isObstacle(new Pair(pos.first-1,pos.second-1))) && (!activos.contains(new Pair(pos.first-1, pos.second-1))) ){
                world[pos.first-1][pos.second-1].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first-1][pos.second-1]);
                activos.add(world[pos.first-1][pos.second-1]);
            }
            if( (isObstacle(new Pair(pos.first,pos.second-1))) && (!activos.contains(new Pair(pos.first, pos.second-1))) ){
                world[pos.first][pos.second-1].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first][pos.second-1]);
                activos.add(world[pos.first][pos.second-1]);
            }
            if( (isObstacle(new Pair(pos.first-1,pos.second))) && (!activos.contains(new Pair(pos.first-1, pos.second))) ){
                world[pos.first-1][pos.second].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first-1][pos.second]);
                activos.add(world[pos.first-1][pos.second]);
            }
            if( (isObstacle(new Pair(pos.first+1,pos.second+1))) && (!activos.contains(new Pair(pos.first+1, pos.second+1))) ){
                world[pos.first+1][pos.second+1].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first+1][pos.second+1]);
                activos.add(world[pos.first+1][pos.second+1]);
            }
            if( (isObstacle(new Pair(pos.first,pos.second+1))) && (!activos.contains(new Pair(pos.first, pos.second+1))) ){
                world[pos.first][pos.second+1].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first][pos.second+1]);
                activos.add(world[pos.first][pos.second+1]);
            }
            if( (isObstacle(new Pair(pos.first+1,pos.second))) && (!activos.contains(new Pair(pos.first+1, pos.second))) ){
                world[pos.first+1][pos.second].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first+1][pos.second]);
                activos.add(world[pos.first+1][pos.second]);
            }
            if( (isObstacle(new Pair(pos.first+1,pos.second-1))) && (!activos.contains(new Pair(pos.first+1, pos.second-1))) ){
                world[pos.first+1][pos.second-1].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first+1][pos.second-1]);
                activos.add(world[pos.first+1][pos.second-1]);
            }
            if( (isObstacle(new Pair(pos.first-1,pos.second+1))) && (!activos.contains(new Pair(pos.first-1, pos.second+1))) ){
                world[pos.first-1][pos.second+1].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first-1][pos.second+1]);
                activos.add(world[pos.first-1][pos.second+1]);
            }
        }
        cola.remove();
    }
    
    public void calcularMap(Pair<Integer,Integer> pos){
        pos.second=0;
        cola.add(pos);
        while(!cola.isEmpty()){
            calcularAdyacentes(cola.element());
        }
    }
    
    public void initRadarScanner(){
        int posX= posActual.first;
        int posY= posActual.second;
        
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(world[posX-i][posY-j].first == 2)
                    radar[i][j]= 1;
                else
                    radar[i][j]= world[posX-i][posY-j].first;
                scanner[i][j]= world[posX-i][posY-j].second;
            }
        }
    }
    
    /**
     * Funcion encargada de comprobar si se inicia heuristica 2 o 1, usará heuristica1
     * si la distancia minima es mayor a una casilla vacía adyacente, usará heuristica2
     * si la distancia minima es menor al resto de casillas vacias adyacentes.
     * 
     * @return boolean true Heuristica2, false Heuristica1 
     * @author Vicente Martínez
     */
    public boolean initHeuristic2(){
        double minObst= Double.POSITIVE_INFINITY;
        double minVoid= Double.POSITIVE_INFINITY;
        
        initRadarScanner();
        
        for(int i=1; i<4; i++){
            for(int j=1; j<4; j++){
                if((i!=2 || j!=2) && (radar[i][j] == 1)){
                    if(scanner[i][j]<minObst)
                        minObst= scanner[i][j];
                }
                else if((i!=2 || j!=2) && (radar[i][j] != 1)){
                    if(scanner[i][j]<minVoid)
                        minVoid= scanner[i][j];
                }
            }
        }
        
        if(minObst<minVoid && minVoid != Double.POSITIVE_INFINITY){
            if(this.minValueFind>scanner[2][2]){
                this.minValueFind= scanner[2][2];
            }
        }
        
        if(minObst==Double.POSITIVE_INFINITY || this.minValueFind>minVoid)
            return false;
        else if(this.minValueFind<minVoid)
            return true;
        else
            return true;

    }
    
     /**
     * Función encargada de establecer la acción elegida y guardar las visitas
     * realizadas en el mapa "world".
     * 
     * @param npos Es un pair con con las coordenadas de la casilla elegida.
     * @return String Devuelve la accion elegida.
     * @author Vicente Martínez
     */
    public String nextAction(Pair<Integer, Integer> npos){
        
        String act;
        
        if (npos.first == 1 && npos.second == 1){
            act = "moveNW";
            posActual.first= posActual.first - 1;
            posActual.second= posActual.second - 1;
        }
        else if (npos.first == 1 && npos.second == 2){
            act = "moveN";
            posActual.second= posActual.second - 1;
        }
        else if (npos.first == 1 && npos.second == 3){
            act = "moveNE";
            posActual.first= posActual.first + 1;
            posActual.second= posActual.second - 1;
        }
        else if (npos.first == 2 && npos.second == 1){
            act = "moveW";
            posActual.first= posActual.first - 1;
        }
        else if (npos.first == 2 && npos.second == 3){
            act = "moveE";
            posActual.first= posActual.first + 1;
        }
        else if (npos.first == 3 && npos.second == 1){
            act = "moveSW";
            posActual.first= posActual.first - 1;
            posActual.second= posActual.second + 1;
        }
        else if (npos.first == 3 && npos.second == 2){
            act = "moveS";
            posActual.second= posActual.second + 1;
        }
        else{
            act = "moveSE";
            posActual.first= posActual.first + 1;
            posActual.second= posActual.second + 1;
        }
        
        return act;
    }
    
    /**
     * Funcion encargada de realizar la heuristica 1, es un algoritmo Greedy que 
     * analiza el nºvisitas y la distancia gps para decidir la accion.
     * 
     * @return String con la accion elegida.
     * @author Vicente Martínez
     */
    public String heuristic1(){
        Pair<Integer, Integer> newpos = new Pair(2,2);
        this.minValue= Double.POSITIVE_INFINITY;
        double benefit;
        finalPoint[0]= 0;
        finalPoint[1]= 0;

        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if((i!=1 || j!=1) && this.radar[i][j] != 1){ 
                    benefit= scanner[i][j];//getBenefit(i, j);
                    if(this.minValue > benefit){
                        this.minValue= benefit;
                        newpos.first= i;
                        newpos.second= j;
                    }
                }
            }
        }
        actionAnterior= nextAction(newpos);
        return actionAnterior;
    }
    
    
    /**
     * Funciones encargadas de comprobar si es posible moverse a una dirección. 
     * @author Vicente Martínez
     * @return Presenta o no obstaculo.
     */
    public boolean isMovS(){
        return radar[2][1]!=1;
    }
    public boolean isMovSW(){
        return radar[2][0]!=1;
    }
    public boolean isMovSE(){
        return radar[2][2]!=1;
    }
    public boolean isMovE(){
        return radar[1][2]!=1;
    }
    public boolean isMovNE(){
        return radar[0][2]!=1;
    }
    public boolean isMovN(){
        return radar[0][1]!=1;
    }
    public boolean isMovNW(){
        return radar[0][0]!=1; 
    }
    public boolean isMovW(){
        return radar[1][0]!=1;
    }
    
    /**
     * Funcion encargada de comprobar que el mapa no tiene solución, para ello
     * se comprueba si se ha rodeado completamente al objetivo y se ha llegado a finalPoint
     * @param act Accion elegida para moverse.
     * @return boolean true en el caso de no existir solución, false en caso contrario.
     * @author Vicente Martínez
     */
    public boolean sinSolucion(String act){
                
        if("moveNW".equals(act))
            return ((this.posActual.first - 1)==finalPoint[0] && (this.posActual.second - 1)==finalPoint[1]);
        else if ("moveN".equals(act))
            return (this.posActual.first==finalPoint[0] && (this.posActual.second - 1)==finalPoint[1]);
        else if ("moveNE".equals(act))
            return ((this.posActual.first + 1)==finalPoint[0] && (this.posActual.second - 1)==finalPoint[1]);
        else if ( "moveW".equals(act))
            return ((this.posActual.first - 1)==finalPoint[0] && this.posActual.second==finalPoint[1]);
        else if ("moveE".equals(act))
            return ((this.posActual.first + 1)==finalPoint[0] && this.posActual.second==finalPoint[1]);
        else if ("moveSW".equals(act))
            return ((this.posActual.first - 1)==finalPoint[0] && (this.posActual.second + 1)==finalPoint[1]);
        else if ( "moveS".equals(act))
            return (this.posActual.first==finalPoint[0] && (this.posActual.second + 1)==finalPoint[1]);
        else
            return ((this.posActual.first + 1)==finalPoint[0] && (this.posActual.second + 1)==finalPoint[1]);
    }
    
    
    /**
     * Funcion encargada guardar las visitas realizadas en el mapa "world" para
     * la heuristica 2.
     * @param act Accion elegida.
     * @author Vicente Martínez
     */
    public void changeWorld(String act){            
        if(act == "moveNW")
            this.posActual= new Pair(posActual.first-1,posActual.second-1);
        else if (act == "moveN")
            this.posActual= new Pair(posActual.first,posActual.second-1);
        else if (act == "moveNE")
            this.posActual= new Pair(posActual.first+1,posActual.second-1);
        else if ( act == "moveW")
            this.posActual= new Pair(posActual.first-1,posActual.second);
        else if (act == "moveE")
            this.posActual= new Pair(posActual.first+1,posActual.second);
        else if (act == "moveSW")
            this.posActual= new Pair(posActual.first-1,posActual.second+-1);
        else if ( act == "moveS")
            this.posActual= new Pair(posActual.first,posActual.second+1);
        else
            this.posActual= new Pair(posActual.first+1,posActual.second+1);
    }
    /**
     * Funcion heuristica2 algortimo basado en el algoritmo de la mano derecha, es usada
     * cuando initHeuristic2() es true, dependiendo de las casillas adyacentes 
     * el bot se moverá a una u otra direccion.
     * 
     * @return String Con la acción elegida.
     * @author Vicente Martínez
     */
    public String heuristic2(){
        String act= new String();
        boolean accionElegida=false;
        
        if(!isMovE() && !isMovW() && !accionElegida){
            if(actionAnterior=="moveSE" || actionAnterior=="moveS" || actionAnterior=="moveSW"){
               if(isMovSW()){
                  act= "moveSW";
                  accionElegida=true;
               }
               else if(isMovS()){
                  act= "moveS";
                  accionElegida=true;
               }
            }
            else if(actionAnterior=="moveNE" || actionAnterior=="moveN" || actionAnterior=="moveNW"){
                if(isMovNE()){
                  act= "moveNE";
                  accionElegida=true;
               }
               else if(isMovN()){
                  act= "moveN";
                  accionElegida=true;
               }
            }
        }
        
        if(!isMovN() && !isMovS() && !accionElegida){ 
            if(actionAnterior=="moveSE" || actionAnterior=="moveE" || actionAnterior=="moveNE"){
               if(isMovSE()){
                  act= "moveSE";
                  accionElegida=true;
               }
               else if(isMovE()){
                  act= "moveE";
                  accionElegida=true;
               }
            }
            else if(actionAnterior=="moveSW" || actionAnterior=="moveW" || actionAnterior=="moveNW"){
                if(isMovSW()){
                  act= "moveSW";
                  accionElegida=true;
               }
               else if(isMovW()){
                  act= "moveW";
                  accionElegida=true;
               }
            }
        }
        
        if(!isMovE() && !accionElegida){
            if(isMovNE()){
                act= "moveNE";
                accionElegida=true;
            }
            else if(isMovN()){
                act= "moveN";
                accionElegida=true;
            }
        }
        if(!isMovN() && !accionElegida){
            if(isMovNW()){
                act= "moveNW";
                accionElegida=true;
            }
            else if(isMovW()){
                act= "moveW";
                accionElegida=true;
            }
        }
        if(!isMovW() && !accionElegida){
            if(isMovSW()){
                act= "moveSW";
                accionElegida=true;
            }
            else if(isMovS()){
                act= "moveS";
                accionElegida=true;
            }
        }
        if(!isMovS() && !accionElegida){
            if(isMovSE()){
                act= "moveSE";
                accionElegida=true;
            }
            else if(isMovE()){
                act= "moveE";
                accionElegida=true;
            }
        }
        if(!isMovSE() && !accionElegida){
            if(isMovE()){
                act= "moveE";
                accionElegida=true;
            }
            else if(isMovNE()){
                act= "moveNE";
                accionElegida=true;
            }
        }
        if(!isMovSW() && !accionElegida){
            if(isMovS()){
                act= "moveS";
                accionElegida=true;
            }
            else if(isMovSE()){
                act= "moveSE";
                accionElegida=true;
            }
        }
        if(!isMovNW() && !accionElegida){
            if(isMovW()){
                act= "moveW";
                accionElegida=true;
            }
            else if(isMovSW()){
                act= "moveSW";
                accionElegida=true;
            }
        }
        if(!accionElegida){
            if(isMovN())
                act= "moveN";
            else if(isMovNW())
                act= "moveNW";
        }
        
        if(sinSolucion(act)){
            System.out.println("-------------------------- No tiene solución. --------------------------");
            return "found";
        }
        
        changeWorld(act);
        return act;
    }
    
    public String heuristic(){
        if(this.battery < 2){
            System.out.println("Bot: REFUEL");
            return "refuel";
        }
        //Si el bot está sobre la casilla 2 (objetivo), fin
        if(this.posActual.first == 3){
            System.out.println("Bot: FOUND");
            return "found";
        }
        else if(initHeuristic2()){
            System.out.println("Bot: HEURISTICA 2");
            
            if(initHeu2){
                finalPoint[0]= posActual.first;
                finalPoint[1]= posActual.second;
                initHeu2= false;
            }
            actionAnterior= heuristic2();
            return actionAnterior;
        }
        else{
            initHeu2=true;
            return heuristic1();
        }
    }
    
}
