/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import com.eclipsesource.json.JsonObject;

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
    private void parse(JsonObject json) {
        this.meta = json.get("meta").asObject();
        this.ship1Data = json.get(AgentsNames.leaderShip).asObject();
        this.ship2Data = json.get(AgentsNames.ship2).asObject();
        this.ship3Data = json.get(AgentsNames.ship3).asObject();
        this.ship4Data = json.get(AgentsNames.ship4).asObject();
    }

}
