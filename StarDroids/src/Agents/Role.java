package Agents;

import com.eclipsesource.json.*;
import es.upv.dsic.gti_ia.core.ACLMessage;
import helpers.Pair;
import java.util.ArrayList;

/**
 * 
 * @author Andres Ortiz, Vicente Martínez, Alba Rios, Alberto Meana, Rafael Ruiz
 */
public abstract class Role {
    
    protected Sensors datos;
    protected ActionsEnum action; // Almacena los valores de la heurística
    protected int[][] radar;
    protected int[][] radar2;
    
    protected boolean found; // Meta vista
    protected Pair <Integer, Integer> goal;
    private int[] roles;
    

    // Constructor
    public Role() {
        this.datos = new Sensors();
        this.action = ActionsEnum.sleep;
        this.roles = new int[4];
        this.found = false;
        this.goal = new Pair(-1,-1);
        
        for(int i = 0 ; i< 4 ; i++)
        {
            this.roles[i]=-1;
        }
    }

    // Basic logic classes
    public abstract void firstLogic();
    public abstract void secondLogic();
    
    // Comprobar si se ha visto la meta
    protected abstract void isFound();
    // Pasar de posición de rada a mundo
    protected abstract Pair<Integer,Integer> mapToWorld (int x, int y);
    
    // Get 
    public ActionsEnum getAction() {
        return this.action;
    }
    
    public Pair<Integer,Integer> getPosition() {
        return this.datos.getPosition();
    }
    
    public Pair<Integer,Integer> getGoalPosition() {
        return this.datos.getGoalPosition();
    }
    
    public Pair<Integer,Integer> [] getShipsPosition() {
        return this.datos.getAllShips();
    }
    
    public Integer[][] getMap() {
        return this.datos.getWorldMap();
    }
    
    public boolean getFound() {
        return this.found;
    }
    
    public Pair<Integer,Integer> getShipPosition( int ship ) {
        return this.datos.getShipPosition( ship );
    }
    
    /**
     * @author Andrés Ortiz, Alba Rios
     * @return True si se encuentra en la meta
     */
    public boolean inGoal() {
        Pair<Integer,Integer> myPosition = this.datos.getPosition();
        return (datos.getMapPosition(myPosition.first, myPosition.second) == 3);
    }
    
     /**
     * @author Andrés Ortiz
     * @return True si todos estan en meta
     */
    public boolean allInGoal(){
       Pair<Integer,Integer> [] shipPos=datos.getAllShips();
       
       if(!inGoal()) return false; 
       for(Pair<Integer,Integer> pos : shipPos){
            if(datos.getMapPosition(pos.first, pos.second) != 3) return false;
        }
        return true;
    }
         /**
     * @author Andrés Ortiz
     * @return True no queda bateria global o en agentes
     */
    public boolean noFuel(){
           if(datos.getFuel()==0 && datos.getGlobalFuel()==0) return true;
           //TODO: probar el fuel de todos los agentes
           else return false;
    }
    
    /**
     * @author Alba Rios
     * @param x Coordenada 
     * @param y Coordenada
     * @return True si hay una nave en x,y
     */
    protected boolean checkShips(int x, int y) {
        Pair<Integer,Integer>[] ships = this.datos.getAllShips();
        
        for(Pair<Integer,Integer> pos : ships) {
            if (pos.first == x && pos.second == y) return true;
        }
        
        return false;
    }
    
    /**
     * @author Rafael Ruiz, Andrés Ortiz
     * @description rellena la estructura de datos de los sensores a partir de un mensaje
     * @param in 
     */
    public JsonObject fillSensors(ACLMessage in) {
        JsonObject message = new JsonObject();
        
        message = Json.parse(in.getContent()).asObject().get("result").asObject();
        this.datos.setFuel(message.getInt("battery", 0));
        this.datos.setGlobalFuel(message.getInt("energy", 0));
        this.datos.setGoal(message.getBoolean("goal", false));
        this.datos.setPosition(message.getInt("x", 0), message.getInt("y",0));
        
        JsonArray sensor = Json.parse(in.getContent()).asObject().get("result").asObject().get("sensor").asArray();
        fillDatesRole(sensor);
        
        return message;
    }
  
    /**
     * 
     * @author Andrés Ortiz
     * @description metodo particular de cada rol para rellenar sus datos
     */
    protected abstract void fillDatesRole(JsonArray sensor); //rellena los datos dependiendo del rol
    
    /**
     * 
     * @author Rafael Ruiz
     * 
     * @param a
     * @param b
     * @param sensor 
     */
    protected void fillDates(int a, JsonArray sensor) {
        
        int x = (Integer) this.datos.getPosition().first;
        int y = (Integer) this.datos.getPosition().second;
        int index = 0;

        for(int i = x-a ; i < x+a; i++) {
            for(int j = y-a ; j < y+a; j++) {
                if(i>=0 && i<=499 && j>=0 && j<=499)
                    this.datos.setWorldMap(i, j, sensor.get(index).asInt());
                index++;
            }
        }
    }
    
        
    /**
     * 
     * @author Rafael Ruiz
     */
    public void parseTokenAgent(Token obj) {
        ArrayList<JsonObject> aux = obj.getShipData();
                
        System.out.println(aux.get(0).toString());
        System.out.println(aux.get(1).toString());
        System.out.println(aux.get(2).toString());
        System.out.println(aux.get(3).toString());
        
        if(aux.get(0).toString().length() > 2) {
            
            JsonObject sensor = aux.get(0);
            
            this.datos.setFuelShip(0, sensor.getInt("battery", 0));
            
            this.datos.setShipPosition( sensor.getInt("x", 0), sensor.getInt("y", 0), 0 );
            
            JsonArray pepe = aux.get(0).get("sensor").asArray();
          
            if(pepe.size()==9) {
                fillDatesShips(1, 2, pepe, this.datos.getShipPosition(0));
                this.roles[0]=0;
            }
            else if(pepe.size()==25) {
                fillDatesShips(2, 3, pepe, this.datos.getShipPosition(0));
                this.roles[0]=1;
            }
            else if(pepe.size() == 121) {
                fillDatesShips(5, 6, pepe, this.datos.getShipPosition(0));
                this.roles[0]=2;
            }
        }
        
        if(aux.get(1).toString().length() > 2) {
            
            JsonObject sensor = aux.get(1);
            
            this.datos.setFuelShip(1, sensor.getInt("battery", 0));
            
            this.datos.setShipPosition( sensor.getInt("x", 0), sensor.getInt("y", 0), 1 );
            
            JsonArray pepe = aux.get(1).get("sensor").asArray();

            if(pepe.size()==9) {
                fillDatesShips(1, 2, pepe, this.datos.getShipPosition(1));
                this.roles[1]=0;
            }
            else if(pepe.size()==25) {
                fillDatesShips(2, 3, pepe, this.datos.getShipPosition(1));
                this.roles[1]=1;
            }
            else if(pepe.size() == 121) {
                fillDatesShips(5, 6, pepe, this.datos.getShipPosition(1));
                this.roles[1]=2;
            } 
        }
        
        if(aux.get(2).toString().length() > 2) {
            
            JsonObject sensor = aux.get(2);
            
            this.datos.setFuelShip(2, sensor.getInt("battery", 0));
            
            this.datos.setShipPosition( sensor.getInt("x", 0), sensor.getInt("y", 0), 2 );
            
            JsonArray pepe = aux.get(2).get("sensor").asArray();
       
            if(pepe.size()==9) {
                fillDatesShips(1, 2, pepe, this.datos.getShipPosition(2));
                this.roles[2]=0;
            }
            else if(pepe.size()==25) {
                fillDatesShips(2, 3, pepe, this.datos.getShipPosition(2)); 
                this.roles[2]=1;
            }
            else if(pepe.size() == 121) {
                fillDatesShips(5, 6, pepe, this.datos.getShipPosition(2));
                this.roles[2]=2;
            }
            
        }
        
        if(aux.get(3).toString().length() > 2) {
            
            JsonObject sensor = aux.get(3);
            
            this.datos.setFuelShip(3, sensor.getInt("battery", 0));
            
            this.datos.setShipPosition( sensor.getInt("x", 0), sensor.getInt("y", 0), 3 );
            
            JsonArray pepe = aux.get(3).get("sensor").asArray();

            if(pepe.size()==9) {
                fillDatesShips(1, 2, pepe, this.datos.getShipPosition(3));
                this.roles[3]=0;
            }
            else if(pepe.size()==25) {
                fillDatesShips(2, 3, pepe, this.datos.getShipPosition(3));
                this.roles[3]=1;
            }
            else if(pepe.size() == 121) {
                fillDatesShips(5, 6, pepe, this.datos.getShipPosition(3));
                this.roles[3]=2;
            }
        }
        
    }
    
    
    public int[] getRoles()
    {
        return this.roles;
    }
    
        
    /**
     * 
     * @author Rafael Ruiz
     */
    protected void fillDatesShips(int a, int b, JsonArray sensor, Pair<Integer,Integer> n) {
        int x = n.first;
            
        
        if(x-a < 0) {
            x = x + a;

        }
        else if(x+b > 500) {
            x = 500 - b;
        }

        int y = n.second;
        
        if(y-a < 0) {
            y = y + a;

        }
        else if(y+b > 500) {
            y = 500 - b;
        }

        int index = 0;

        for(int i = x-a ; i < x+b; i++) {
            for(int j = y-a ; j < y+b; j++) {
                this.datos.setWorldMap(i, j, sensor.get(index).asInt());

                index++;
            }
        }
    }

}
