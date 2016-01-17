package Agents;

import es.upv.dsic.gti_ia.core.AgentID;

/**
 *
 * @author Andrés Ortiz
 * @description Nombres e ids de los agentes
 */

//"Static" class with agents names
public class AgentsNames {
    public static final String leaderShip="rojoLiderVicente";
    public static final String ship2="rojo1Vicente";
    public static final String ship3="rojo2Vicente";
    public static final String ship4="rojo3Vicente";
    public static AgentID getId(String name) {
        return new AgentID(name);
    }

}
