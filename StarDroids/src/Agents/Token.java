package Agents;

import com.eclipsesource.json.JsonObject;
import java.util.ArrayList;

/**
 *
 * @author Andrés Ortiz Corrales
 * @description Token class representing the data passed between agents
 */
public class Token {

    private JsonObject ship1Data; //Change this to matrix if neccessary
    private JsonObject ship2Data;
    private JsonObject ship3Data;
    private JsonObject ship4Data;
    private JsonObject meta; //other data (change into different variables if necessary)

    /**
     *
     * @author Andrés Ortiz Corrales
     */
    public Token(JsonObject json) {
        this.parse(json);
    }
    public Token(){
        this.ship1Data=new JsonObject();
        this.ship2Data=new JsonObject();
        this.ship3Data=new JsonObject();
        this.ship4Data=new JsonObject();
        this.meta=new JsonObject();
    }
    public void setToken(String name,JsonObject data){
        
        
        if(name.equals(AgentsNames.leaderShip)) this.ship1Data=data;
        if(name.equals(AgentsNames.ship2)) this.ship2Data=data;
        if(name.equals(AgentsNames.ship3)) this.ship3Data=data;
        if(name.equals(AgentsNames.ship4)) this.ship4Data=data;
        
    }
    public void setMeta(JsonObject data){
        this.meta=data;
    }
    

    /**
     *
     * @author Andrés Ortiz Corrales
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
     */
    public void parse(JsonObject json) {
        this.meta = json.get("meta").asObject();
        this.ship1Data = json.get(AgentsNames.leaderShip).asObject();
        this.ship2Data = json.get(AgentsNames.ship2).asObject();
        this.ship3Data = json.get(AgentsNames.ship3).asObject();
        this.ship4Data = json.get(AgentsNames.ship4).asObject();
    }

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
