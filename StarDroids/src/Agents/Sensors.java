/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

/**
 *
 * @author Rafael Ruiz
 */
public class Sensors {

    private int battery;
    private int x, y;
    private int energy;
    private boolean goal;
    //Poner aqui el worldmap¿?

    public Sensors() {
        this.battery = 0;
        this.x = 0;
        this.y = 0;
        this.energy = 0;
        this.goal = false;
    }

    public void setBattery(int i) {
        this.battery = i;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setRadar(int x, int y, int valor) {
        //this.radar[x][y] = valor;
    }

    public void setEnergy(int n) {
        this.energy = n;
    }

    public void setGoal(boolean valor) {
        this.goal = valor;
    }

    public void Show() {
        System.out.println("Datos");
        System.out.println("Battery " + this.battery);
    }
  /**
 * @author Andrés Ortiz
 */
    public int getBattery(){
        return battery;
    }
    public boolean inGoal(){
        return goal;
    }
}
