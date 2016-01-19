package Agents;

import es.upv.dsic.gti_ia.core.AgentID;

/**
 *
 * @author Andrés Ortiz
 * @description Nombres e ids de los agentes
 */

//"Static" class with agents names
public class AgentsNames {
    public static final String leaderShip="rojoLider1010";
    public static final String ship2="rojo110001";
    public static final String ship3="rojo2220001";
    public static final String ship4="rojo3330001";
    public static AgentID getId(String name) {
        return new AgentID(name);
    }

}
