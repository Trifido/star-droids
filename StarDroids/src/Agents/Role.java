/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

/**
 *
 * @author Andres Ortiz
 */
public abstract class Role {
    //Data (if any)

    Sensors datos;

    //Constructor
    public Role() {
        this.datos = new Sensors();
    }

    //basic logic classes, implement here if common
    public abstract void firstLogic();
    public abstract void secondLogic();


}
