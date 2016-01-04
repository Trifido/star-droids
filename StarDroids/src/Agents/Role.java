package Agents;

import com.eclipsesource.json.*;
import es.upv.dsic.gti_ia.core.ACLMessage;
/**
 * @author Andres Ortiz
 */
public abstract class Role {
    //Data (if any)

    Sensors datos;
    AgentAction action; //almacena los valores de la heurística

    //Constructor
    public Role() {
        this.datos = new Sensors();
        this.action=new AgentAction();
    }

    //basic logic classes, implement here if common
    public abstract void firstLogic();
    public abstract void secondLogic();
    
    /**
 * @author Andres Ortiz
 * @description ejemplo de logica básica
 */
    private void basicLogic(){
        if(datos.inGoal()) action.multiplyAction(ActionsEnum.sleep, 10); //Si esta en el objetivo, esperar
        // Si es 0, tenemos crash, asi que he corregido esta tonteria -Alberto
        //if( datos.getFuel() == 0 ) action.multiplyAction(ActionsEnum.battery,2); //recargar si esta sin bateria
        if( datos.getFuel() == 1 ) action.multiplyAction(ActionsEnum.battery,2); //recargar si esta sin bateria
    }
    /**
 * @author Andres Ortiz
 * @description devuelve la accion a realizar
 */ 
    public ActionsEnum getAction(){
       return action.getAction();
    }
    
    
    /**
     * @author Rafael Ruiz
     * 
     * @param in 
     */
    public void fillSensors(ACLMessage in)
    {
        //Datos {"result":{"battery":100,"x":83,"y":99,"sensor":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,2],"energy":1000,"goal":false}} 
        JsonObject message = new JsonObject();
        
        message = Json.parse(in.getContent()).asObject().get("result").asObject();
        
        this.datos.setFuel(message.getInt("battery", 0));
        this.datos.setGlobalFuel(message.getInt("energy", 0));
        this.datos.setGoal(message.getBoolean("goal", false));
        this.datos.setPosition(message.getInt("x", 0), message.getInt("y",0));
        this.datos.setShipPosition(0, 0, 0);
        this.datos.setWorldMap(0, 0, 0);
        
        this.datos.Show();
    }

}
