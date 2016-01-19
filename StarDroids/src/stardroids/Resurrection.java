package stardroids;

import Yoda.Yoda;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;

/**
 *  Main principal de Shenron.
 * 
 * @author Alberto Meana
 */
public class Resurrection {

    public static void main(String[] args) throws Exception {
        AgentsConnection.connect("isg2.ugr.es", 6000, "test", "Canmaior", "Ishiguro", false);
        Yoda yoda = new Yoda(new AgentID("Yodaaaa"));
        yoda.start();
    }

}
