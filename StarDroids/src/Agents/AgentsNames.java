/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import es.upv.dsic.gti_ia.core.AgentID;

/**
 *
 * @author Andrés Ortiz
 */

//"Static" class with agents names
public class AgentsNames {
    public static final String leaderShip="rojoLider";
    public static final String ship2="rojo1"; 
    public static final String ship3="rojo2"; 
    public static final String ship4="rojo3"; 
    public static AgentID getId(String name){
        return new AgentID(name);
    }
    
}