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
public class MillenniumFalcon extends Role {
    //Millennium Falcon or Falcon (Halcon) obviously

    private int[][] radar;
    private MoveAlgorithm heuristica;

    public MillenniumFalcon() {
        super();
        this.radar = new int[11][11];
        heuristica= new MoveAlgorithm(super.getGoalPosition(),super.getPosition(),super.getMap());
    }
    @Override
    public void firstLogic() {
        updateObstacles();
        action.multiplyAction(ActionsEnum.sleep, 10); //Pa q duerman
    }

    @Override
    public void secondLogic() {
        String result= heuristica.heuristic();
    }

    @Override
    protected void fillDatesRole(JsonArray sensor) {
        fillDates(5, 6, sensor);
    }

}
