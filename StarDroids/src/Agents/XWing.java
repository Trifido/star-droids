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

    public XWing() {
        super();
        this.radar = new int[3][3];
    }

    @Override
    public void firstLogic() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void secondLogic() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void fillDatesRole(JsonArray sensor) {
       fillDates(1, 2, sensor);
    }

}
