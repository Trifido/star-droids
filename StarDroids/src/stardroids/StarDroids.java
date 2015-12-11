
package stardroids;

import Agents.Ship;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;

/**
 *
 * @author Alberto Meana
 */
public class StarDroids {

    public static void main(String[] args) throws Exception {

        AgentsConnection.connect("isg2.ugr.es", 6000, "Furud", "Canmaior", "Ishiguro", false);
        
        Ship ship1 = new Ship( new AgentID( "rojoLider" ) );
        Ship ship2 = new Ship( new AgentID( "rojo1" ) );
        Ship ship3 = new Ship( new AgentID( "rojo2" ) );
        Ship ship4 = new Ship( new AgentID( "rojo3" ) );
        
        ship1.start();
        ship2.start();
        ship3.start();
        ship4.start();
        
    }
    
}
