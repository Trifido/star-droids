package Agents;

import com.eclipsesource.json.*;
import es.upv.dsic.gti_ia.core.ACLMessage;
import helpers.Pair;
import java.util.ArrayList;

/**
 * @author Andres Ortiz, Vicente Martínez, Alba Rios, Alberto Meana
 */
public abstract class Role {
    //Data (if any)

    protected Sensors datos;
    protected ActionsEnum action; //almacena los valores de la heurística

    //Constructor
    public Role() {
        this.datos = new Sensors();
        this.action = ActionsEnum.sleep;
    }

    //basic logic classes, implement here if common
    public abstract void firstLogic();
    public abstract void secondLogic();
    
    /**
     * @author Andres Ortiz, Alba Rios
     * @return Accion
     * @description Devuelve la accion a realizar
     */ 
    public ActionsEnum getAction(){
        //this.datos.show();
        return this.action;
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
     * @author Andrés Ortiz, Alba Rios
     * @return True si se encuentra en la meta
     */
    public boolean inGoal(){ 
        return this.datos.inGoal();
    }
    
    /**
    * @author Andres Ortiz, Alba Rios
    * @description ejemplo de logica básica
    */
    /*protected void basicLogic(){
        
        if(datos.inGoal()) action.multiplyAction(ActionsEnum.sleep, 10); //Si esta en el objetivo, esperar

        if(datos.getFuel() == 1) action.multiplyAction(ActionsEnum.battery, 2); //recargar si esta sin bateria
        
        checkShips();
    }*/
    
    /**
     * @author Andres Ortiz
     * @description Comprueba naves adyacentes
     */
    /*protected void checkShips(){
        Pair<Integer,Integer> myPos=datos.getPosition();
        Pair<Integer,Integer>[] ships=datos.getAllShips();
        int x=myPos.first,y=myPos.second; //Esto puede ir fuera?
        for(Pair<Integer,Integer> pos : ships){
            int x2=pos.first,y2=pos.second;

            if(x==x2-1 && y==y2-1) action.setToZero(ActionsEnum.moveNW);
            if(x==x2-1 && y==y2) action.setToZero(ActionsEnum.moveW);
            if(x==x2-1 && y==y2+1) action.setToZero(ActionsEnum.moveSW);
            if(x==x2 && y==y2-1) action.setToZero(ActionsEnum.moveN);
            if(x==x2 && y==y2+1) action.setToZero(ActionsEnum.moveS);
            if(x==x2+1 && y==y2-1) action.setToZero(ActionsEnum.moveNE);
            if(x==x2+1 && y==y2) action.setToZero(ActionsEnum.moveE);
            if(x==x2+1 && y==y2+1) action.setToZero(ActionsEnum.moveSE);
        }
    }*/
    
    /**
     * @author Alba Rios
     * @description Evita el movimiento hacia obstaculos
     */
    /*protected void updateObstacles(){
        Pair<Integer,Integer> myPos = datos.getPosition();
        int x = myPos.first; int y = myPos.second;
        
        //Obstaculo == 1 
        if (datos.getMapPosition(x-1, y-1) == 1) action.setToZero(ActionsEnum.moveNW);
        if (datos.getMapPosition(x, y-1) == 1) action.setToZero(ActionsEnum.moveN);
        if (datos.getMapPosition(x+1, y-1) == 1) action.setToZero(ActionsEnum.moveNE);
        if (datos.getMapPosition(x-1, y) == 1) action.setToZero(ActionsEnum.moveW);
        if (datos.getMapPosition(x+1, y) == 1) action.setToZero(ActionsEnum.moveE);
        if (datos.getMapPosition(x-1, y+1) == 1) action.setToZero(ActionsEnum.moveSW);
        if (datos.getMapPosition(x, y+1) == 1) action.setToZero(ActionsEnum.moveS);
        if (datos.getMapPosition(x+1, y+1) == 1) action.setToZero(ActionsEnum.moveSE);
    }*/
    
    /**
     * @author Alba Rios
     * @description Evita el movimiento hacia los bordes
     */
    /*protected void updateBorders(){
        Pair<Integer,Integer> myPos = datos.getPosition();
        int x = myPos.first; int y = myPos.second;
        
        //Borde == 2
        if (datos.getMapPosition(x-1, y-1) == 2) action.setToZero(ActionsEnum.moveNW);
        if (datos.getMapPosition(x, y-1) == 2) action.setToZero(ActionsEnum.moveN);
        if (datos.getMapPosition(x+1, y-1) == 2) action.setToZero(ActionsEnum.moveNE);
        if (datos.getMapPosition(x-1, y) == 2) action.setToZero(ActionsEnum.moveW);
        if (datos.getMapPosition(x+1, y) == 2) action.setToZero(ActionsEnum.moveE);
        if (datos.getMapPosition(x-1, y+1) == 2) action.setToZero(ActionsEnum.moveSW);
        if (datos.getMapPosition(x, y+1) == 2) action.setToZero(ActionsEnum.moveS);
        if (datos.getMapPosition(x+1, y+1) == 2) action.setToZero(ActionsEnum.moveSE);
    }*/
    
    /**
     * @author Rafael Ruiz
     * 
     * @param in 
     */
    public JsonObject fillSensors(ACLMessage in, Role role)
    {
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
    protected void fillDates(int a, int b, JsonArray sensor)
    {
        int x = (Integer) this.datos.getPosition().first;
            
        if(x-a < 0)
        {
            x = x + a;

        }else if(x+b > 500)
        {
            x = 500 - b;
        }


        int y = (Integer) this.datos.getPosition().second;

        if(y-a < 0)
        {
            y = y + a;

        }else if(y+b > 500)
        {
            y = 500 - b;
        }

        int index = 0;


        for(int i = x-a ; i < x+b; i++)
        {
            for(int j = y-a ; j < y+b; j++)
            {
                this.datos.setWorldMap(i, j, sensor.get(index).asInt());

                index++;

            }
        }
    }
    
    public Pair<Integer,Integer> getPosition(){
        return this.datos.getPosition();
    }
    
    public Pair<Integer,Integer> getGoalPosition(){
        return this.datos.getGoalPosition();
    }
    
    public Pair<Integer,Integer> [] getShipsPosition(){
        return this.datos.getAllShips();
    }
    
    public Integer[][] getMap(){
        return this.datos.getWorldMap();
    }
    
    public void parseTokenAgent(Token obj)
    {
        ArrayList<JsonObject> aux = obj.getShipData();
        
        
        System.out.println(aux.get(0).toString());
        System.out.println(aux.get(1).toString());
        System.out.println(aux.get(2).toString());
        System.out.println(aux.get(3).toString());
        
        
        
        if(aux.get(0).toString().length() > 2)
        {
            
            JsonObject sensor = aux.get(0);
            
            this.datos.setShipPosition( sensor.getInt("x", 0), sensor.getInt("y", 0), 0 );
            
            JsonArray pepe = aux.get(0).get("sensor").asArray();

            
            if(pepe.size()==9)
            {
                fillDatesShips(1, 2, pepe, this.datos.getShipPosition(0));
                
            }else if(pepe.size()==25)
            {
                fillDatesShips(2, 3, pepe, this.datos.getShipPosition(0));
                
            }else if(pepe.size() == 121)
            {
                fillDatesShips(5, 6, pepe, this.datos.getShipPosition(0));
            }
            
        }
        
        if(aux.get(1).toString().length() > 2)
        {
            JsonObject sensor = aux.get(1);
            
            this.datos.setShipPosition( sensor.getInt("x", 0), sensor.getInt("y", 0), 1 );
            
            JsonArray pepe = aux.get(1).get("sensor").asArray();

            
            if(pepe.size()==9)
            {
                fillDatesShips(1, 2, pepe, this.datos.getShipPosition(1));
                
            }else if(pepe.size()==25)
            {
                fillDatesShips(2, 3, pepe, this.datos.getShipPosition(1));
                
            }else if(pepe.size() == 121)
            {
                fillDatesShips(5, 6, pepe, this.datos.getShipPosition(1));
            }
            
        }
        if(aux.get(2).toString().length() > 2)
        {
            JsonObject sensor = aux.get(2);
            
            this.datos.setShipPosition( sensor.getInt("x", 0), sensor.getInt("y", 0), 2 );
            
            JsonArray pepe = aux.get(2).get("sensor").asArray();

            
            if(pepe.size()==9)
            {
                fillDatesShips(1, 2, pepe, this.datos.getShipPosition(2));
                
            }else if(pepe.size()==25)
            {
                fillDatesShips(2, 3, pepe, this.datos.getShipPosition(2));
                
            }else if(pepe.size() == 121)
            {
                fillDatesShips(5, 6, pepe, this.datos.getShipPosition(2));
            }
            
        }
        if(aux.get(3).toString().length() > 2)
        {
            JsonObject sensor = aux.get(3);
            
            this.datos.setShipPosition( sensor.getInt("x", 0), sensor.getInt("y", 0), 3 );
            
            JsonArray pepe = aux.get(3).get("sensor").asArray();

            
            if(pepe.size()==9)
            {
                fillDatesShips(1, 2, pepe, this.datos.getShipPosition(3));
                
            }else if(pepe.size()==25)
            {
                fillDatesShips(2, 3, pepe, this.datos.getShipPosition(3));
                
            }else if(pepe.size() == 121)
            {
                fillDatesShips(5, 6, pepe, this.datos.getShipPosition(3));
            }
            
        }
        
        //this.datos.show();
    }
    
    protected void fillDatesShips(int a, int b, JsonArray sensor, Pair<Integer,Integer> n)
    {
        int x = n.first;
            
        
        if(x-a < 0)
        {
            x = x + a;

        }else if(x+b > 500)
        {
            x = 500 - b;
        }


        int y = n.second;
        

        if(y-a < 0)
        {
            y = y + a;

        }else if(y+b > 500)
        {
            y = 500 - b;
        }

        int index = 0;


        for(int i = x-a ; i < x+b; i++)
        {
            for(int j = y-a ; j < y+b; j++)
            {
                this.datos.setWorldMap(i, j, sensor.get(index).asInt());

                index++;

            }
        }
    }
    

}
