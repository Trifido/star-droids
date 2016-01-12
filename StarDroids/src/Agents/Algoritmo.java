/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import helpers.Pair;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Vicente
 */
public class Algoritmo {
    private Pair<Integer,Integer> posFinal;
    private Pair<Integer,Integer> posActual;
    private int radar[][], scanner[][];
    private int battery;
    private double minValueFind;
    private double []finalPoint;
    private boolean initHeu2;
    private Pair<Integer,Integer>[][]world;
    private String actionAnterior;
    private String action;
    private double minValue;
    
    public Algoritmo(Pair<Integer,Integer> posFinal, Pair<Integer,Integer> posActual, Pair<Integer,Integer>[][]world){
        this.posFinal= posFinal;
        this.posActual= posActual;
        this.world= world;
        radar= new int[3][3];
        initHeu2= true;
        finalPoint= new double[2];
        String actionAnterior= "";
    }
    
    /**
     * Método encargado de comprobar si existe un obstaculo en el camino.
     * @param pos
     * @return boolean
     * @author Vicente
     */
    private boolean isObstacle(Pair<Integer,Integer> pos){
        return ((world[pos.first][pos.second].first == 1) || (world[pos.first][pos.second].first == 2));
    }
    
    /**
     * Método que calcula la casilla con menor distancia al objetivo y devuelve una accion
     * @author Vicente
     * @return String
     */
    public String calcularAdyacentes(){
        HashMap acciones = new HashMap(); 
        double minDistancia= Double.POSITIVE_INFINITY;
        
        if(!isObstacle(new Pair(posActual.first-1,posActual.second-1))){    //NW
            acciones.put(calcularDistancia(new Pair(posActual.first-1,posActual.second-1), posFinal), "moveNW");
        }
        if(!isObstacle(new Pair(posActual.first,posActual.second-1))){      //N
            acciones.put(calcularDistancia(new Pair(posActual.first,posActual.second-1), posFinal), "moveN");
        }
        if(!isObstacle(new Pair(posActual.first-1,posActual.second))){      //W
            acciones.put(calcularDistancia(new Pair(posActual.first-1,posActual.second), posFinal), "moveW");
        }
        if(!isObstacle(new Pair(posActual.first+1,posActual.second+1))){    //SE
            acciones.put(calcularDistancia(new Pair(posActual.first+1,posActual.second+1), posFinal), "moveSE");
        }
        if(!isObstacle(new Pair(posActual.first,posActual.second+1))){      //S
            acciones.put(calcularDistancia(new Pair(posActual.first,posActual.second+1), posFinal), "moveS");
        }
        if(!isObstacle(new Pair(posActual.first+1,posActual.second))){      //E
            acciones.put(calcularDistancia(new Pair(posActual.first+1,posActual.second), posFinal), "moveE");
        }
        if(!isObstacle(new Pair(posActual.first+1,posActual.second-1))){    //NE
            acciones.put(calcularDistancia(new Pair(posActual.first+1,posActual.second-1), posFinal), "moveNE");
        }
        if(!isObstacle(new Pair(posActual.first-1,posActual.second+1))){    //SW
            acciones.put(calcularDistancia(new Pair(posActual.first-1,posActual.second+1), posFinal), "moveSW");
        }
        
        Iterator i = acciones.entrySet().iterator();
        // Display elements
        while(i.hasNext()) {
           Map.Entry me = (Map.Entry)i.next();
           if((double)me.getKey()<minDistancia){
               minDistancia= (double)me.getKey();
               action= (String) me.getValue();
           }
        }
        i = acciones.entrySet().iterator();
        while(i.hasNext()) {
           Map.Entry me = (Map.Entry)i.next();
               System.out.println((double)me.getKey() + ": " + (String) me.getValue());
        }
        
        return action;
    }
    
    /**
     * Método de calcular la distancia mínima entre dos casillas
     * @param posInicial
     * @param posFinal
     * @return double
     * 
     */
    private double calcularDistancia(Pair<Integer,Integer> posInicial, Pair<Integer,Integer> posFinal){
        return Math.sqrt(Math.pow( (double)(posFinal.first-posInicial.first) , 2.0) + Math.pow( (double)(posFinal.second-posInicial.second), 2.0));
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
     * Funcion encargada de realizar la heuristica 1, es un algoritmo Greedy que 
     * analiza distancia a la casilla objetivo para decidir la accion.
     * 
     * @return String con la accion elegida.
     * @author Vicente Martínez
     */
    public String heuristic1(){
        actionAnterior= calcularAdyacentes();
        return actionAnterior;
    }
    
    /**
     *  HACE FALTA CONTROLAR LOS CHOQUES ENTRE BOTS
     * @return String
     */
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