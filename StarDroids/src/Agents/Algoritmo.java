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
    private Pair<Integer, Integer> shipsPosition[];
    private int radar[][]; 
    private double scanner[][];
    private int battery;
    private double minValueFind;
    private double []finalPoint;
    private boolean initHeu2;
    private Integer [][]world;
    private String actionAnterior;
    private String action;
    private double minValue;
    
    public Algoritmo(Pair<Integer,Integer> posFinal, Pair<Integer,Integer> posActual, Integer[][]world, Pair<Integer,Integer> ships[], int fuel, int[][]radar){
        this.posFinal= posFinal;
        this.posActual= posActual;
        this.world= world;
        this.radar= radar;
        this.minValueFind= Double.POSITIVE_INFINITY;
        scanner= new double [3][3];
        shipsPosition= ships;
        initHeu2= true;
        finalPoint= new double[2];
        String actionAnterior= "";
        this.battery = fuel;
           
        for(int j=0; j<3; j++){
            for(int i=0; i<3; i++){
                Pair<Integer,Integer> pos= mapToWorld(i,j);
                if (pos.first >= 0 && pos.first <=499 && pos.second >= 0 && pos.second <= 499)
                    world[pos.first][pos.second]= radar[i][j];
            }
        }
    }
    
     protected Pair<Integer,Integer> mapToWorld (int x, int y) {
        Pair<Integer,Integer> myPosition = posActual;
        Pair<Integer,Integer> solution = new Pair(-1,-1);
        
        switch (x) {
            case 0:
                solution.first = myPosition.first - 1;
                break;
            case 1:
                solution.first = myPosition.first;
                break;
            case 2:
                solution.first = myPosition.first +1;
                break;
        }
        
        switch (y) {
            case 0:
                solution.second = myPosition.second - 1;
                break;
            case 1:
                solution.second = myPosition.second;
                break;
            case 2:
                solution.second = myPosition.second +1;
                break;
        }
        
        return solution;
    }
    
    /**
     * Método encargado de comprobar si existe un obstaculo en el camino.
     * @param pos
     * @return boolean
     * @author Vicente
     */
    private boolean isObstacle(Pair<Integer,Integer> pos){
        if(pos.first<0 || pos.second<0)
            return true;
        return ((world[pos.first][pos.second] == 1) || (world[pos.first][pos.second] == 2) || (world[pos.first][pos.second] == -1));
    }
    
    /**
     * Método que calcula la casilla con menor distancia al objetivo y devuelve una accion
     * @author Vicente
     * @return String
     */
    public String calcularAdyacentes(){
        HashMap acciones = new HashMap(); 
        double minDistancia= Double.POSITIVE_INFINITY;
        System.out.println("entra en calcular adyacentes");
        
        
        if(!isObstacle(new Pair(posActual.first-1,posActual.second-1))){    //NW
            acciones.put(calcularDistancia(new Pair(posActual.first-1,posActual.second-1), posFinal), "moveNW");
        }
        else{
            acciones.put(Double.POSITIVE_INFINITY, "moveNW");
        }
        if(!isObstacle(new Pair(posActual.first,posActual.second-1))){      //N
            acciones.put(calcularDistancia(new Pair(posActual.first,posActual.second-1), posFinal), "moveN");
        }
        else{
            acciones.put(Double.POSITIVE_INFINITY, "moveN");
        }
        if(!isObstacle(new Pair(posActual.first-1,posActual.second))){      //W
            acciones.put(calcularDistancia(new Pair(posActual.first-1,posActual.second), posFinal), "moveW");
        }
        else{
            acciones.put(Double.POSITIVE_INFINITY, "moveW");
        }
        if(!isObstacle(new Pair(posActual.first+1,posActual.second+1))){    //SE
            acciones.put(calcularDistancia(new Pair(posActual.first+1,posActual.second+1), posFinal), "moveSE");
        }
        else{
            acciones.put(Double.POSITIVE_INFINITY, "moveSE");
        }
        if(!isObstacle(new Pair(posActual.first,posActual.second+1))){      //S
            acciones.put(calcularDistancia(new Pair(posActual.first,posActual.second+1), posFinal), "moveS");
        }
        else{
            acciones.put(Double.POSITIVE_INFINITY, "moveS");
        }
        if(!isObstacle(new Pair(posActual.first+1,posActual.second))){      //E
            acciones.put(calcularDistancia(new Pair(posActual.first+1,posActual.second), posFinal), "moveE");
        }
        else{
            acciones.put(Double.POSITIVE_INFINITY, "moveE");
        }
        if(!isObstacle(new Pair(posActual.first+1,posActual.second-1))){    //NE
            acciones.put(calcularDistancia(new Pair(posActual.first+1,posActual.second-1), posFinal), "moveNE");
        }
        else{
            acciones.put(Double.POSITIVE_INFINITY, "moveNE");
        }
        if(!isObstacle(new Pair(posActual.first-1,posActual.second+1))){    //SW
            acciones.put(calcularDistancia(new Pair(posActual.first-1,posActual.second+1), posFinal), "moveSW");
        }
        else{
            acciones.put(Double.POSITIVE_INFINITY, "moveSW");
        }
        
        
        System.out.println("POSFX: " + posFinal.first + " , POSFY: " + posFinal.second);
        System.out.println("POSACTUAL= " + this.posActual.first + " , " + this.posActual.second);
        
        Iterator i = acciones.entrySet().iterator();
        // Display elements
        while(i.hasNext()) {
           Map.Entry me = (Map.Entry)i.next();
           if((double)me.getKey()<minDistancia){
               minDistancia= (double)me.getKey();
               action= (String) me.getValue();
               System.out.println(" --------Accion: " + action + "  Valor: " + me.getKey());
           }
        }
  
        System.out.println("MinDistancia= " + minDistancia);
        System.out.println(action);

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

    private ActionsEnum actionEnum(String act)
    {
        if (act == "moveN")
            return ActionsEnum.moveN;
        if (act == "moveS" )
            return ActionsEnum.moveS;
        if (act == "moveE")
            return ActionsEnum.moveE;
        if (act == "moveW") 
            return ActionsEnum.moveW;
        if (act == "moveNE")
            return ActionsEnum.moveNE;
        if (act == "moveNW") 
            return ActionsEnum.moveNW;
        if (act == "moveSW") 
            return ActionsEnum.moveSW;
        if (act == "refuel") 
            return ActionsEnum.battery;
        if (act == "moveSE") 
            return ActionsEnum.moveSE;
        System.out.println("-------------------------- No conoce la orden --------------------------");
            return ActionsEnum.moveS;
    }
    
    /**
     * Actualizar matriz scanner
     * @author Vicente
     */
    private void updateScanner(){
        
        System.out.println("UPDATE SCANNER");
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                scanner[i][j]= calcularDistancia(new Pair(posActual.first+i-1,posActual.second+j-1), posFinal);
            }
        }
    }
    
    /**
     * Método que actualiza las matrices de los sensores.
     * @author Vicente
     */
    private void updateMatrixSensor(){
        updateScanner();
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
        
        return act;
    }
    
    /**
     * Funciones encargadas de comprobar si es posible moverse a una dirección. 
     * @author Vicente Martínez
     * @return Presenta o no obstaculo.
     */
    public boolean isMovS(){
        return radar[2][1]!=1 && radar[2][1] !=2;
    }
    public boolean isMovSW(){
        return radar[2][0]!=1 && radar[2][0] !=2;
    }
    public boolean isMovSE(){
        return radar[2][2]!=1 && radar[2][2] !=2;
    }
    public boolean isMovE(){
        return radar[1][2]!=1 && radar[1][2] !=2;
    }
    public boolean isMovNE(){
        return radar[0][2]!=1 && radar[0][2] !=2;
    }
    public boolean isMovN(){
        return radar[0][1]!=1 && radar[0][1] !=2;
    }
    public boolean isMovNW(){
        return radar[0][0]!=1 && radar[0][0] !=2;
    }
    public boolean isMovW(){
        return radar[1][0]!=1 && radar[1][0] !=2;
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

        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if((i!=1 || j!=1) && (radar[i][j] == 1 || radar[i][j] == 2)){
                    if(scanner[i][j]<minObst)
                        minObst= scanner[i][j];
                }
                else if((i!=1 || j!=1) && (radar[i][j] != 1 && radar[i][j] != 2)){
                    if(scanner[i][j]<minVoid)
                        minVoid= scanner[i][j];
                }
            }
        }
        
        System.out.println("SCANNER:");
        for(int i=0; i<3; i++){
           for(int j=0; j<3; j++){
               System.out.print(scanner[i][j] + "  ");
           }
           System.out.println();
        }
        
        System.out.println("RADAR:");
        for(int i=0; i<3; i++){
           for(int j=0; j<3; j++){
               System.out.print(radar[i][j]);
           }
           System.out.println();
        }
        
        System.out.println("minVoid: " + minVoid + " --- minObst: " + minObst);
        
        if(minObst<minVoid && minVoid != Double.POSITIVE_INFINITY){
            if(this.minValueFind>scanner[1][1]){
                this.minValueFind= scanner[1][1];
            }
        }
        
        System.out.println("minValue= " + this.minValueFind + " --- scanner: " + scanner[1][1] + " --- minVoid: " + minVoid);
        
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
    public ActionsEnum heuristic1(){
        actionAnterior= calcularAdyacentes();
        return actionEnum(actionAnterior);
    }
    
    /**
     *  HACE FALTA CONTROLAR LOS CHOQUES ENTRE BOTS
     * @return String
     */
    public ActionsEnum heuristic(){
        if(this.battery <=4){
            System.out.println("Bot: REFUEL");
            return ActionsEnum.battery;
        }
        //Si el bot está sobre la casilla 3 (objetivo), fin
        
        updateMatrixSensor();
        
        if(radar[1][1] == 3){
            System.out.println("POSFINAL= " + this.posActual.first + " , " + this.posActual.second);
            System.out.println("Bot: FOUND");
            return actionEnum("found");
        }
        else if(initHeuristic2()){
            System.out.println("Bot: HEURISTICA 2");
            
            if(initHeu2){
                finalPoint[0]= posActual.first;
                finalPoint[1]= posActual.second;
                initHeu2= false;
            }
            actionAnterior= heuristic2();
            
            return actionEnum(actionAnterior);
        }
        else{
            System.out.println("Bot: HEURISTICA 1");
            initHeu2=true;
            ActionsEnum ac= heuristic1();
            System.out.println(ac.toString());
            return ac;
        }
    }
}