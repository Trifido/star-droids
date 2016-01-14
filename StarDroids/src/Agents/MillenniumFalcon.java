/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import com.eclipsesource.json.JsonArray;
import helpers.Pair;

/**
 *
 * @author Andrés Ortiz, Alba Rios
 */
public class MillenniumFalcon extends Role {
    //Millennium Falcon or Falcon (Halcon) obviously

    private int[][] radar;
    private MoveAlgorithm heuristica;
    private boolean collided;

    public MillenniumFalcon() {
        super();
        this.radar = new int[11][11];
        collided = false;
    }
    
    @Override
    public void firstLogic() {

    }

    @Override
    public void secondLogic() {
        String result= heuristica.heuristic();
    }

    @Override
    protected void fillDatesRole(JsonArray sensor) {
        fillDates(5, 6, sensor);
    }
    
    /**
     * @author Alba Rios
     * @description Comprueba si se ha encontrado con un obstaculo
     */
    /*protected void checkCollision(){
        Pair<Integer,Integer> myPos = datos.getPosition();
        int x = myPos.first; int y = myPos.second;
        
        //Obstaculo == 1
        if (lastAction == ActionsEnum.moveNW && datos.getMapPosition(x-1, y-1) == 1) collided = true;
        if (lastAction == ActionsEnum.moveN && datos.getMapPosition(x, y-1) == 1) collided = true;
        if (lastAction == ActionsEnum.moveNE && datos.getMapPosition(x+1, y-1) == 1) collided = true;
        if (lastAction == ActionsEnum.moveW && datos.getMapPosition(x-1, y) == 1) collided = true;
        if (lastAction == ActionsEnum.moveE && datos.getMapPosition(x+1, y) == 1) collided = true;
        if (lastAction == ActionsEnum.moveSW && datos.getMapPosition(x-1, y+1) == 1) collided = true;
        if (lastAction == ActionsEnum.moveS && datos.getMapPosition(x, y+1) == 1) collided = true;
        if (lastAction == ActionsEnum.moveSE && datos.getMapPosition(x+1, y+1) == 1) collided = true;
    }*/
    
}
