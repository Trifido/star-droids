package Agents;

import com.eclipsesource.json.JsonArray;
import helpers.Pair;

/**
 *
 * @author Andrés Ortiz, Alba Rios
 */
public class XWing extends Role {
    //Or fly (mosca)

    private int[][] radar;
    private MoveAlgorithm heuristica;

    // Variables de modo búsqueda de objetivo
    private final int movDistance = 5;
    private ActionsEnum direction; 
    private boolean positioning;
    private boolean turn;
    private int turnCount;

    public XWing() {
        super();
        this.radar = new int[3][3];
        
        this.direction = ActionsEnum.moveS;
        this.positioning = false;
        this.turn = false;
        this.turnCount = 0;
    }

    /**
     * @author Alba Rios
     * @description Selecciona la acción a realizar en el modo búsqueda de objetivo
     * Llamar SI NO ESTÁ EN LA META (eso controlarlo fuera)
     */
    @Override
    public void firstLogic() {      
        Pair<Integer,Integer> myPosition = this.datos.getPosition();
        
        if (this.datos.getFuel() <= 2) {
            this.action = ActionsEnum.battery; // Recargar batería
        }
        else if (this.positioning) {
            //this.action = ; Trasladar a la posición 0,0 (funcion vicente)
            
            if (myPosition.first == movDistance-1 && myPosition.second == movDistance-1) 
                this.positioning = false;
        }     
        else {
            if (this.direction == ActionsEnum.moveS) { 
                if (this.turn) { // Nos desplazamos a la derecha
                    if (this.turnCount == this.movDistance) { // Nos hemos desplazado lo que debiamos -> norte
                      this.turnCount = 0;
                      this.turn = false;
                      this.direction = ActionsEnum.moveN;
                      this.action = ActionsEnum.moveN;
                    }
                    else { // Aun nos estamos desplazando -> este
                        this.turnCount += 1;
                        this.action = ActionsEnum.moveE;
                    }
                } 
                else { // Nos desplazamos hacia abajo
                    int border = this.radar[1][2];
                    int x = (int) this.datos.getPosition().first; 
                    int y = (int) this.datos.getPosition().second + 1;
                    
                    if (checkShips(x, y)) { // Si hay una nave 
                        this.action = ActionsEnum.moveW;
                    }
                    else if (border == 2) { // Encontramos el borde -> este
                            this.turn = true;
                            this.turnCount += 1;
                            this.action = ActionsEnum.moveE; 
                    }
                    else { // No encontramos el borde -> sur
                        this.action = ActionsEnum.moveS;
                    }
                }  
            }
            
            else if (this.direction == ActionsEnum.moveN) {
                if (this.turn) { // Nos desplazamos a la derecha
                    if (this.turnCount == this.movDistance) { // Nos hemos desplazado lo que debiamos -> sur
                      this.turnCount = 0;
                      this.turn = false;
                      this.direction = ActionsEnum.moveS;
                      this.action = ActionsEnum.moveS;
                    }
                    else { // Aun nos estamos desplazando -> este
                        this.turnCount += 1;
                        this.action = ActionsEnum.moveE;
                    }
                }
                else { // Nos desplazamos hacia arriba
                    int border = this.radar[1][0];
                    int x = (int) this.datos.getPosition().first; 
                    int y = (int) this.datos.getPosition().second - 1;
                    
                    if (checkShips(x, y)) { // Si hay una nave 
                        this.action = ActionsEnum.moveW;
                    }

                    if (border == 2) { // Encontramos el borde -> este
                        this.turn = true;
                        this.turnCount += 1;
                        this.action = ActionsEnum.moveE; 
                    }
                    else { // No encontramos el borde -> sur
                        this.action = ActionsEnum.moveN;
                    }
                }
            }
        }
    }

    @Override
    public void secondLogic() {
        String result = ((MoveAlgorithmFly)heuristica).heuristic();
    }

    @Override
    protected void fillDatesRole(JsonArray sensor) {
       fillDates(1, 2, sensor);
    }

}
