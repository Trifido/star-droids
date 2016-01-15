package Agents;

import com.eclipsesource.json.JsonObject;
import java.util.ArrayList;

/**
 *
 * @author Andrés Ortiz Corrales
 * @description Representa los datos de sincronización entre agentes enviados con el token
 */
public class Token {
    //datos particulares de cada agente
    private JsonObject ship1Data; 
    private JsonObject ship2Data;
    private JsonObject ship3Data;
    private JsonObject ship4Data;
    private JsonObject meta; //datos comunes

    /**
     *
     * @author Andrés Ortiz Corrales
     */
    public Token(JsonObject json) {
        this.parse(json);
    }
    /**
     *
     * @author Andrés Ortiz Corrales
     */
    public Token(){
        this.ship1Data=new JsonObject();
        this.ship2Data=new JsonObject();
        this.ship3Data=new JsonObject();
        this.ship4Data=new JsonObject();
        this.meta=new JsonObject();
    }
    
    /**
     *
     * @author Andrés Ortiz Corrales
     * @description Modifica los datos particulares de un agente en el token
     */
    public void setToken(String name,JsonObject data){
        
        
        if(name.equals(AgentsNames.leaderShip)) this.ship1Data=data;
        if(name.equals(AgentsNames.ship2)) this.ship2Data=data;
        if(name.equals(AgentsNames.ship3)) this.ship3Data=data;
        if(name.equals(AgentsNames.ship4)) this.ship4Data=data;
        
    }
     
    /**
     *
     * @author Andrés Ortiz Corrales
     * @description Modifica los datos comunes del token
     */
    public void setMeta(JsonObject data){
        this.meta=data;
    }
    

    /**
     *
     * @author Andrés Ortiz Corrales
     * @description Traduce el token a una cadena json
     */
    public JsonObject toJson() {
        JsonObject res = new JsonObject();
        res.add("meta", this.meta);
        res.add(AgentsNames.leaderShip, this.ship1Data);
        res.add(AgentsNames.ship2, this.ship2Data);
        res.add(AgentsNames.ship3, this.ship3Data);
        res.add(AgentsNames.ship4, this.ship4Data);
        return res;
    }

    /**
     *
     * @author Andrés Ortiz Corrales
     * @description Genera el token a partir de una cadena json
     */
    public void parse(JsonObject json) {
        this.meta = json.get("meta").asObject();
        this.ship1Data = json.get(AgentsNames.leaderShip).asObject();
        this.ship2Data = json.get(AgentsNames.ship2).asObject();
        this.ship3Data = json.get(AgentsNames.ship3).asObject();
        this.ship4Data = json.get(AgentsNames.ship4).asObject();
    }

    /**
     *
     * @author Andrés Ortiz Corrales
     * @description Recibe todos los datos de las naves en un array de jsonobjects
     */
    public ArrayList<JsonObject> getShipData()
    {
        ArrayList<JsonObject> aux = new ArrayList<JsonObject>();
        
        aux.add(this.ship1Data);
        aux.add(this.ship2Data);
        aux.add(this.ship3Data);
        aux.add(this.ship4Data);
        
        
        return aux;
    }
    
    
}
