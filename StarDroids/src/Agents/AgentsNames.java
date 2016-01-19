package Agents;

import es.upv.dsic.gti_ia.core.AgentID;

/**
 *
 * @author Andrés Ortiz
 * @description Nombres e ids de los agentes
 */

//"Static" class with agents names
public class AgentsNames {
    public static final String leaderShip="rojoLider1000";
    public static final String ship2="rojo11000";
    public static final String ship3="rojo222000";
    public static final String ship4="rojo333000";
    public static AgentID getId(String name) {
        return new AgentID(name);
    }

}
