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
public class YWing extends Role {
    //YWing or bird (pajaro)

    private int[][] radar;
    private MoveAlgorithm heuristica;
    private boolean collided;

    public YWing() {
        super();
        this.radar = new int[5][5];
        collided = false;
    }
    
    @Override
    public void firstLogic() {
        updateObstacles();
        updateBorders();
        
        //Penalizar colisiones
        checkCollision();
        if (collided == true){
            action.multiplyAction(ActionsEnum.sleep, 2);
            collided = false;
        }
    }

    @Override
    public void secondLogic() {
        String result= ((MoveAlgorithmFly)heuristica).heuristic();
    }

    @Override
    protected void fillDatesRole(JsonArray sensor) {
       fillDates(2, 3, sensor);
    }
    
    /**
     * @author Alba Rios
     * @description Comprueba si se ha encontrado con un obstaculo
     */
    protected void checkCollision(){
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
    }

}
