package Agents;

import es.upv.dsic.gti_ia.core.AgentID;

/**
 *
 * @author Andrés Ortiz
 * @description Nombres e ids de los agentes
 */

//"Static" class with agents names
public class AgentsNames {
    public static final String leaderShip="rojoLider8";
    public static final String ship2="rojo18";
    public static final String ship3="rojo28";
    public static final String ship4="rojo38";
    public static AgentID getId(String name) {
        return new AgentID(name);
    }

}
