package Agents;

import es.upv.dsic.gti_ia.core.AgentID;

/**
 *
 * @author Andrés Ortiz
 * @description Nombres e ids de los agentes
 */

//"Static" class with agents names
public class AgentsNames {
    public static final String leaderShip="rojoLiderAlberto";
    public static final String ship2="rojo1Alberto";
    public static final String ship3="rojo2Alberto";
    public static final String ship4="rojo3Alberto";
    public static AgentID getId(String name) {
        return new AgentID(name);
    }

}
