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
public class XWing extends Role {
    //Or fly(mosca)

    private int[][] radar;
    private MoveAlgorithm heuristica;

    public XWing() {
        super();
        this.radar = new int[3][3];
        heuristica= new MoveAlgorithmFly(super.getGoalPosition(),super.getPosition(),super.getMap());
    }

    @Override
    public void firstLogic() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void secondLogic() {
        String result= ((MoveAlgorithmFly)heuristica).heuristic();
    }

    @Override
    protected void fillDatesRole(JsonArray sensor) {
       fillDates(1, 2, sensor);
    }

}
