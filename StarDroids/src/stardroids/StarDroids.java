
package stardroids;

import Agents.Ship;
import Agents.AgentsNames;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;
import mapproject.MapProject;

/**
 *  Main principal del proyecto.
 * 
 * @author Alberto Meana
 */
public class StarDroids {

    public static void main(String[] args) throws Exception {
        
        // Inicializaciones
        MapProject map = new MapProject();
        
        AgentsConnection.connect("isg2.ugr.es", 6000, "Furud", "Canmaior", "Ishiguro", false);
        Ship ship1 = new Ship(new AgentID(AgentsNames.leaderShip),new AgentID(AgentsNames.ship2));
        Ship ship2 = new Ship(new AgentID(AgentsNames.ship2), new AgentID(AgentsNames.ship3));
        Ship ship3 = new Ship(new AgentID(AgentsNames.ship3), new AgentID(AgentsNames.ship4));
        Ship ship4 = new Ship(new AgentID(AgentsNames.ship4), new AgentID(AgentsNames.leaderShip));
        
        // Pedir el mundo
        ship1.setInterface( map );
        
        // Iniciar los bots
        ship1.start();
        ship2.start();
        ship3.start();
        ship4.start();
        
        // Iniciar la interfaz
        map.startInterface();
    }
}
