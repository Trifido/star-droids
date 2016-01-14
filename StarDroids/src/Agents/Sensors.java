package Agents;

import helpers.Pair;

/**
 *
 * @author Rafael Ruiz, Alberto Meana, Vicente Martínez
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
    
    /**
     * funcion para mostrar cierta información por consola
     * 
     */
    public void show() {        
    /*    System.out.println("Datos");
        System.out.println("Combustible: " + this.myFuel);
        System.out.println("Global Energy: " + this.globalFuel);
        System.out.println("Goal: " + this.goal);
        System.out.println("X: " + this.myPosition.first + " Y: " + this.myPosition.second );
        
        System.out.println("1 ----- X: " + this.shipsPosition[0].first + " Y: " + this.shipsPosition[0].second);
        System.out.println("2 ----- X: " + this.shipsPosition[1].first + " Y: " + this.shipsPosition[1].second);
        System.out.println("3 ----- X: " + this.shipsPosition[2].first + " Y: " + this.shipsPosition[2].second);
        System.out.println("4 ----- X: " + this.shipsPosition[3].first + " Y: " + this.shipsPosition[3].second); */
        //Pair<Integer,Integer> posFinal = new Pair(499,499);
        
        //test = new MoveAlgorithm(posFinal,this.myPosition,this.worldMap);
                
        //posFinal = new Pair(0,0);
        
        //test.calcularMap(posFinal);
    /*    
        for(int i = 0 ; i < 100; i++)
        {
            for(int j = 0 ; j < 100; j++)
            {
                System.out.print(this.worldMap[i][j]);
            }
            System.out.println();
        }*/
    }
    
}
