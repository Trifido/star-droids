/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import com.eclipsesource.json.JsonArray;

/**
 *
 * @author Andrés Ortiz
 */
public class YWing extends Role {
    //YWing or bird (pajaro)

    private int[][] radar;
    private MoveAlgorithm heuristica;

    public YWing() {
        super();
        this.radar = new int[5][5];
        heuristica= new MoveAlgorithm(super.getGoalPosition(),super.getPosition(),super.getMap());
    }
    @Override
    public void firstLogic() {
        updateObstacles();
        action.multiplyAction(ActionsEnum.sleep, 10); //Pa q duerman
    }

    @Override
    public void secondLogic() {
        String result= ((MoveAlgorithmFly)heuristica).heuristic();
    }

    @Override
    protected void fillDatesRole(JsonArray sensor) {
       fillDates(2, 3, sensor);
    }

}
