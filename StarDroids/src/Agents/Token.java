/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import com.eclipsesource.json.JsonObject;


/**
 *
 * @author Andrés Ortiz Corrales
 * @description Token class representing the data passed between agents
 */
public class Token {
     private JsonObject ship1Data; //Change this to matrix if neccessary
     private JsonObject ship2Data;
     private JsonObject ship3Data;
     private JsonObject ship4Data;
     private JsonObject meta; //other data (change into different variables if necessary)
     
     
     /**
      *
      * @author Andrés Ortiz Corrales
      */
     public Token(){
     }
     
     /**
      *
      * @author Andrés Ortiz Corrales
      */
     public JsonObject toJson(){
         JsonObject res=new JsonObject();
         //create res (TODO)
         return res;
     }
    
}
