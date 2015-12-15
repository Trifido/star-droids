/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stardroids;

import Yoda.Yoda;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;

/**
 *
 * @author Alberto Meana
 */
public class Resurrection {

    public static void main(String[] args) throws Exception {
        AgentsConnection.connect("isg2.ugr.es", 6000, "test", "Canmaior", "Ishiguro", false);
        Yoda yoda = new Yoda(new AgentID("Yoda1"));
        yoda.start();
    }

}
