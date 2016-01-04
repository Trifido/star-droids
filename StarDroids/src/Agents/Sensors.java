package Agents;

import helpers.Pair;

/**
 *
 * @author Rafael Ruiz, Alberto Meana
 */
public class Sensors {
    
    // Combustible de la nave
    private int myFuel;
    // Combustible total
    private int globalFuel;
    // ??
    private boolean goal;
    
    // El mapa
    private Integer[][] worldMap;
    // Posición de la nave
    private Pair<Integer,Integer> myPosition;
    // Posición del resto de naves
    private Pair<Integer,Integer>[] shipsPosition;
    
    /////////////////
    
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
                this.worldMap[i][j] = -1; //unknown
        
        this.myPosition = new Pair(-1,-1);
        this.shipsPosition = new Pair[3];
        
        for (int i = 0; i < 3 ; i++)
            this.shipsPosition[i] = new Pair(-1,-1);
        
    }
    
    
    // SETTERS & GETTERS
    
    public void setFuel(int i) {
        this.myFuel = i;
    }

    public int getFuel(){
        
        return myFuel;
        
    }
    
    public void setPosition(int x, int y) {
        
        this.myPosition.first = x;
        this.myPosition.second = y;
        
    }
    
    public Pair getPosition(){
    
        return new Pair( this.myPosition.first, this.myPosition.second );
    
    }
    
    public void setShipPosition( int x, int y, int ship ){
    
        this.shipsPosition[ship].first = x;
        this.shipsPosition[ship].second = y;
    
    }
    
    public Pair getShipPosition( int ship ){
    
        return new Pair( this.shipsPosition[ship].first, this.shipsPosition[ship].second );
    
    }
    
    public Pair [] getAllShips(){
    
        return this.shipsPosition;
    
    }
    
    public void setWorldMap(int x, int y, int valor) {
        
        this.worldMap[x][y] = valor;
    
    }
    
    public int getMapPosition( int x, int y ){
    
        return this.worldMap[x][y];
    
    }
    
    public void setGlobalFuel(int n) {
        
        this.globalFuel = n;
        
    }
    
    public int getGlobalFuel(){
    
        return this.globalFuel;
    
    }
    
    public void setGoal(boolean valor) {
        
        this.goal = valor;
        
    }
    
    public boolean inGoal(){
        
        return goal;
        
    }
    
    
    /**
     * funcion para mostrar cierta información por consola
     * 
     */
    public void Show() {
        
        System.out.println("Datos");
        System.out.println("Combustible " + this.myFuel);
        
    }
}
