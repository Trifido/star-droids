/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import java.util.HashMap;

/**
 *
 * @author Andrés Ortiz
 * @description Contiene los valores de las acciones a realizar en la heurística
 */
public class AgentAction {
    HashMap<ActionsEnum,Double> actionValue=new HashMap<ActionsEnum,Double>(); 
    

  /**
    *
    * @author Andrés Ortiz
    */
    public AgentAction(){
          reset();
    }
    /**
    *
    * @author Andrés Ortiz
    * @description Devuelve todos los valores de actionValue a 1
    */
    private void reset(){
        actionValue.clear();
        for(ActionsEnum act : ActionsEnum.values()){
            actionValue.put(act, 1.0);
        }
    }
    
   /**
    *
    * @author Andrés Ortiz
    * @description Devuelve la acción con el valor más alto y resetea los valores
    */
    public ActionsEnum getAction(){
      ActionsEnum res=ActionsEnum.sleep;
      double maxValue=0;
     for(ActionsEnum act: actionValue.keySet()){
        if(actionValue.get(act)>maxValue){
            res=act;
            maxValue=actionValue.get(res);  
        } 
     }      
      reset();
      return res;
    }
    
   /**
    *
    * @author Andrés Ortiz
    * @description Pone la acción seleccionada a valor 0 (mínimo valor)
    */
    public void setToZero(ActionsEnum action){
        actionValue.put(action, 0.0);
    }
   /**
    *
    * @author Andrés Ortiz
    * @description Multiplica el valor de una acción por el valor dado (el valor debe ser positivo)
    */
    public void multiplyAction(ActionsEnum action,double value){
        if(value>0){
         actionValue.put(action,actionValue.get(action)*value);   
            
        }
    }
    
    
}
