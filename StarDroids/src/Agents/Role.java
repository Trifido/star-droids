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

    // Constructor
    public Role() {
        this.datos = new Sensors();
        this.action = ActionsEnum.sleep;
    }

    // Basic logic classes
    public abstract void firstLogic();
    public abstract void secondLogic();
    
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
    
    /**
     * @author Andrés Ortiz, Alba Rios
     * @return True si se encuentra en la meta
     */
    public boolean inGoal() { 
        return this.datos.inGoal();
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
     * @author Rafael Ruiz
     * 
     * @param in 
     */
    public JsonObject fillSensors(ACLMessage in, Role role) {
        //Datos {"result":{"battery":100,"x":83,"y":99,"sensor":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,2],"energy":1000,"goal":false}} 
        JsonObject message = new JsonObject();
        
        message = Json.parse(in.getContent()).asObject().get("result").asObject();
        
        JsonArray sensor = Json.parse(in.getContent()).asObject().get("result").asObject().get("sensor").asArray();
        
        this.datos.setFuel(message.getInt("battery", 0));
        this.datos.setGlobalFuel(message.getInt("energy", 0));
        this.datos.setGoal(message.getBoolean("goal", false));
        this.datos.setPosition(message.getInt("x", 0), message.getInt("y",0));
        //this.datos.setShipPosition(0, 0, 0); // AUN NO SE QUE ES ESTO
               
        //relleno mapa de datos segun lo que es
        fillDatesRole(sensor);
       /* if(role.getClass().equals(XWing.class))
        {
            fillDates(1, 2, sensor);
            
        }else if (role.getClass().equals(YWing.class))
        {
            fillDates(2, 3, sensor);
            
        }else if (role.getClass().equals(MillenniumFalcon.class))
        {
            fillDates(5, 6, sensor);
        }*/
        System.out.println("Hello");
        //this.datos.show();
        return message;
    }
  
    /**
     * 
     * @author Andrés Ortiz
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
    protected void fillDates(int a, int b, JsonArray sensor) {
        int x = (Integer) this.datos.getPosition().first;
            
        if(x-a < 0) {
            x = x + a;

        }
        else if(x+b > 500) {
            x = 500 - b;
        }

        int y = (Integer) this.datos.getPosition().second;

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
    
    public void parseTokenAgent(Token obj) {
        ArrayList<JsonObject> aux = obj.getShipData();
                
        System.out.println(aux.get(0).toString());
        System.out.println(aux.get(1).toString());
        System.out.println(aux.get(2).toString());
        System.out.println(aux.get(3).toString());
        
        if(aux.get(0).toString().length() > 2) {
            
            JsonObject sensor = aux.get(0);
            
            this.datos.setShipPosition( sensor.getInt("x", 0), sensor.getInt("y", 0), 0 );
            
            JsonArray pepe = aux.get(0).get("sensor").asArray();
          
            if(pepe.size()==9) {
                fillDatesShips(1, 2, pepe, this.datos.getShipPosition(0));
            }
            else if(pepe.size()==25) {
                fillDatesShips(2, 3, pepe, this.datos.getShipPosition(0));
            }
            else if(pepe.size() == 121) {
                fillDatesShips(5, 6, pepe, this.datos.getShipPosition(0));
            }
        }
        
        if(aux.get(1).toString().length() > 2) {
            JsonObject sensor = aux.get(1);
            
            this.datos.setShipPosition( sensor.getInt("x", 0), sensor.getInt("y", 0), 1 );
            
            JsonArray pepe = aux.get(1).get("sensor").asArray();

            if(pepe.size()==9) {
                fillDatesShips(1, 2, pepe, this.datos.getShipPosition(1));    
            }
            else if(pepe.size()==25) {
                fillDatesShips(2, 3, pepe, this.datos.getShipPosition(1));
            }
            else if(pepe.size() == 121) {
                fillDatesShips(5, 6, pepe, this.datos.getShipPosition(1));
            } 
        }
        
        if(aux.get(2).toString().length() > 2) {
            JsonObject sensor = aux.get(2);
            
            this.datos.setShipPosition( sensor.getInt("x", 0), sensor.getInt("y", 0), 2 );
            
            JsonArray pepe = aux.get(2).get("sensor").asArray();
       
            if(pepe.size()==9) {
                fillDatesShips(1, 2, pepe, this.datos.getShipPosition(2));
            }
            else if(pepe.size()==25) {
                fillDatesShips(2, 3, pepe, this.datos.getShipPosition(2));  
            }
            else if(pepe.size() == 121) {
                fillDatesShips(5, 6, pepe, this.datos.getShipPosition(2));
            }
            
        }
        
        if(aux.get(3).toString().length() > 2) {
            JsonObject sensor = aux.get(3);
            
            this.datos.setShipPosition( sensor.getInt("x", 0), sensor.getInt("y", 0), 3 );
            
            JsonArray pepe = aux.get(3).get("sensor").asArray();

            if(pepe.size()==9) {
                fillDatesShips(1, 2, pepe, this.datos.getShipPosition(3));        
            }
            else if(pepe.size()==25) {
                fillDatesShips(2, 3, pepe, this.datos.getShipPosition(3));
            }
            else if(pepe.size() == 121) {
                fillDatesShips(5, 6, pepe, this.datos.getShipPosition(3));
            }
        }
        
        //this.datos.show();
    }
    
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
