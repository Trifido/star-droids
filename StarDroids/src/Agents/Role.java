/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

/**
 * @author Andres Ortiz
 */
public abstract class Role {
    //Data (if any)

    Sensors datos;
    AgentAction action; //almacena los valores de la heurística

    //Constructor
    public Role() {
        this.datos = new Sensors();
        this.action=new AgentAction();
    }

    //basic logic classes, implement here if common
    public abstract void firstLogic();
    public abstract void secondLogic();
    
    /**
 * @author Andres Ortiz
 * @description ejemplo de logica básica
 */
    private void basicLogic(){
        if(datos.inGoal()) action.multiplyAction(ActionsEnum.sleep, 10); //Si esta en el objetivo, esperar
        if(datos.getBattery()==0) action.multiplyAction(ActionsEnum.battery,2); //recargar si esta sin bateria
    }
    /**
 * @author Andres Ortiz
 * @description devuelve la accion a realizar
 */ 
    public ActionsEnum getAction(){
       return action.getAction();
    }


}
