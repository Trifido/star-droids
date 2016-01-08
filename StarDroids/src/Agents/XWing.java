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
public class XWing extends Role {
    //Or fly(mosca)

    private int[][] radar;
    private MoveAlgorithm heuristica;
    private ActionsEnum direction; //Aux para basic logic

    public XWing() {
        super();
        this.radar = new int[3][3];
        heuristica= new MoveAlgorithmFly(super.getGoalPosition(),super.getPosition(),super.getMap());
        direction = ActionsEnum.moveS;
    }

    @Override
    public void firstLogic() {        
        Pair<Integer,Integer> myPos = datos.getPosition();
        int x = myPos.first; int y = myPos.second;
        
        updateBorders();
        
        //Moverse abajo-derecha o arriba-derecha
        if (direction == ActionsEnum.moveS){
            if (datos.getMapPosition(x, y-1) != 2) action.multiplyAction(ActionsEnum.moveS, 2);
            else{
                action.multiplyAction(ActionsEnum.moveE, 2);
                direction = ActionsEnum.moveN;
            }
        }
        if (direction == ActionsEnum.moveN){
            if (datos.getMapPosition(x, y+1) != 2) action.multiplyAction(ActionsEnum.moveN, 2);
            else{
                action.multiplyAction(ActionsEnum.moveE, 2);
                direction = ActionsEnum.moveS;
            }
        }
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
