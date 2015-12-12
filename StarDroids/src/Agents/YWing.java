/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

/**
 *
 * @author Andrés Ortiz
 */
public class YWing extends Role{
    //YWing or bird (pajaro)
    
    private int[][] radar;
    
    
    public YWing(){
        super();
        this.radar = new int[5][5];
    }
    @Override
    public void firstLogic() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void secondLogic() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
