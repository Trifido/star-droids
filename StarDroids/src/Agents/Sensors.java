package Agents;

import helpers.Pair;

/**
 *
 * @author Rafael Ruiz, Alberto Meana, Vicente Martínez
 * @description Representa la estructura de datos de los sensores
 */
public class Sensors {
    //private MoveAlgorithm test;  
    
    private int myFuel; // Combustible de la nave
    private int globalFuel; // Combustible total
    private boolean goal;
    
    private Integer[][] worldMap; // El mapa   
    private Pair<Integer,Integer> myPosition; // Posición de la nave  
    private Pair<Integer,Integer>[] shipsPosition; // Posición del resto de naves
    private Pair<Integer,Integer> goalPosition; // Posición del objetivo
    private int[] fuelShips;
    
    /**
     * Constructor por defecto
     * 
     */
    public Sensors() {
        this.myFuel = 0;
        this.globalFuel = 0;
        this.goal = false;
        
        this.worldMap = new Integer[500][500];
        
        for(int i = 0; i < 500; i++)
            for(int j = 0; j < 500; j++)
                this.worldMap[i][j] = new Integer(-1); //unknown
        
        this.myPosition = new Pair(-1,-1);
        this.shipsPosition = new Pair[4];
        
        for (int i = 0; i < 4 ; i++)
            this.shipsPosition[i] = new Pair(-1,-1);
        
        this.fuelShips = new int[4];
        
        for(int i = 0; i < 4; i++)
        {
            this.fuelShips[i] = 0;
        }
    }
    
    
    // SETTERS & GETTERS
    
    public void setFuel(int i) {
        this.myFuel = i;
    }

    public int getFuel() {
        
        return myFuel;
        
    }
    
    public void setPosition(int x, int y) {
        
        this.myPosition.first = x;
        this.myPosition.second = y;
        
    }
    
    public Pair getPosition() {
    
        return new Pair( this.myPosition.first, this.myPosition.second );
    
    }
    
    public void setShipPosition( int x, int y, int ship ) {
    
        this.shipsPosition[ship].first = x;
        this.shipsPosition[ship].second = y;
    
    }
    
    public Pair getShipPosition( int ship ) {
    
        return new Pair( this.shipsPosition[ship].first, this.shipsPosition[ship].second );
    
    }
    
    public Pair<Integer,Integer> [] getAllShips() {    
        return this.shipsPosition;  
    }
    
    public void setWorldMap(int x, int y, int valor) { 
        this.worldMap[x][y]= valor;   
    }
    
    public Integer[][] getWorldMap() {
        return this.worldMap;
    }
    
    public int getMapPosition( int x, int y ) {
        return this.worldMap[x][y];    
    }
    
    public void setGlobalFuel(int n) {        
        this.globalFuel = n;        
    }
    
    public int getGlobalFuel() {    
        return this.globalFuel;    
    }
    
    public void setGoal(boolean valor) {        
        this.goal = valor;        
    }
    
    public boolean inGoal() {        
        return goal;        
    }
    
    public void setGoalPosition(int x, int y) {
        this.goalPosition.first = x;
        this.goalPosition.second = y;
    }
    
    public Pair getGoalPosition() {
        return this.goalPosition;
    }
    
    public void setFuelShip(int index, int fuel)
    {
        this.fuelShips[index] = fuel;
    }
    public int[] getFuelShips(){
        return this.fuelShips;
    }
    
}
