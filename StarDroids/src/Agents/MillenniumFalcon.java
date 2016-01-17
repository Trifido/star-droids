package Agents;

import com.eclipsesource.json.JsonArray;
import helpers.Pair;

/**
 *
 * @author Andrés Ortiz, Alba Rios
 * @description Representa el rol halcón de los agentes
 */
public class MillenniumFalcon extends Role {
    //Millennium Falcon or Falcon (Halcon) obviously

    private MoveAlgorithm heuristica;
    
    // Variables de modo búsqueda de objetivo
    private boolean positioning;
    int[] movementActions = { 0, 0, 0, 0, 0, 0, 0, 0 }; // N, NE, E, SE, S, SW, W, NW

    public MillenniumFalcon() {
        super();
        this.radar = new int[11][11];
        
        this.positioning = false;
    }
    
    /**
     * Función que rellena el radar del halcon
     * 
     * @author Alberto Meana
     */
    @Override
    protected void fillRadar(){
       Pair<Integer,Integer> myPosition = this.datos.getPosition();
       
       for (int i = 0; i < 11; i++) {
           for (int j = 0; j < 11; j++) {
               
               radar[i][j] = this.datos.getMapPosition( myPosition.first + ( i-5 ) , myPosition.second + ( j-5 ) );
                   
            } 
        }
       
    }
    
    /**
     * @author Alba Rios
     * @description Actualiza a true la variable found si la meta ha sido vista
     */
    @Override
    protected void isFound() {
        for (int i = 0; i < 11; i++) {
           for (int j = 0; j < 11; j++) {
               if (radar[i][j] == 3) {
                   this.found = true;
                   goal = mapToWorld (i, j);
               }
            } 
        }
    }
    
    /**
     * @author Alba Rios
     * @description Transforma unas coordenadas de radar a coordenadas de mundo
     */
    @Override
    protected Pair<Integer,Integer> mapToWorld (int x, int y) {
        Pair<Integer,Integer> myPosition = this.datos.getPosition();
        Pair<Integer,Integer> solution = new Pair(-1,-1);
        
        switch (x) {
            case 0:
                solution.first = myPosition.first - 5;
                break;
            case 1:
                solution.first = myPosition.first - 4;
                break;
            case 2:
                solution.first = myPosition.first - 3;
                break;
            case 3:
                solution.first = myPosition.first - 2;
                break;
            case 4:
                solution.first = myPosition.first - 1;
                break;
            case 5:
                solution.first = myPosition.first;
                break;
            case 6:
                solution.first = myPosition.first + 1;
                break;
            case 7:
                solution.first = myPosition.first + 2;
                break;
            case 8:
                solution.first = myPosition.first + 3;
                break;
            case 9:
                solution.first = myPosition.first + 4;
                break;
            case 10:
                solution.first = myPosition.first + 5;
                break;
        }
        
        switch (y) {
            case 0:
                solution.first = myPosition.first - 5;
                break;
            case 1:
                solution.first = myPosition.first - 4;
                break;
            case 2:
                solution.first = myPosition.first - 3;
                break;
            case 3:
                solution.first = myPosition.first - 2;
                break;
            case 4:
                solution.first = myPosition.first - 1;
                break;
            case 5:
                solution.first = myPosition.first;
                break;
            case 6:
                solution.first = myPosition.first + 1;
                break;
            case 7:
                solution.first = myPosition.first + 2;
                break;
            case 8:
                solution.first = myPosition.first + 3;
                break;
            case 9:
                solution.first = myPosition.first + 4;
                break;
            case 10:
                solution.first = myPosition.first + 5;
                break;
        }
        
        return solution;
    }
    
    /**
     * @author Alba Rios
     * @description Selecciona la acción a realizar en el modo búsqueda de objetivo
     * Llamar SI NO ESTÁ EN LA META (eso controlarlo fuera)
     */
    @Override
    public void firstLogic() {
        Pair<Integer,Integer> myPosition = this.datos.getPosition();
        
        if (!found) {
            if (this.datos.getFuel() <= 4) {
                this.action = ActionsEnum.battery; // Recargar batería
            }
            // Al final no se posiciona en el centro. Se podria hacer.
            else {
                // Hacemos dos niveles de búsqueda
                movementActions[0] = checkNorth(myPosition.first, myPosition.second);
                movementActions[1] = checkNorthEast(myPosition.first, myPosition.second);
                movementActions[2] = checkEast(myPosition.first, myPosition.second);
                movementActions[3] = checkSouthEast(myPosition.first, myPosition.second);
                movementActions[4] = checkSouth(myPosition.first, myPosition.second);
                movementActions[5] = checkSouthWest(myPosition.first, myPosition.second);
                movementActions[6] = checkWest(myPosition.first, myPosition.second);
                movementActions[7] = checkNorthWest(myPosition.first, myPosition.second);  

                if (maxMovementIndex() < 14) { // 2/3 del valor maximo posible
                    if (movementActions[0] != -1) calculateSubMovementActions(myPosition.first, myPosition.second-1, 0);
                    if (movementActions[1] != -1) calculateSubMovementActions(myPosition.first+1, myPosition.second-1, 1);
                    if (movementActions[2] != -1) calculateSubMovementActions(myPosition.first+1, myPosition.second, 2);
                    if (movementActions[3] != -1) calculateSubMovementActions(myPosition.first+1, myPosition.second+1, 3);
                    if (movementActions[4] != -1) calculateSubMovementActions(myPosition.first, myPosition.second+1, 4);
                    if (movementActions[5] != -1) calculateSubMovementActions(myPosition.first-1, myPosition.second+1, 5);
                    if (movementActions[6] != -1) calculateSubMovementActions(myPosition.first-1, myPosition.second, 6);
                    if (movementActions[7] != -1) calculateSubMovementActions(myPosition.first-1, myPosition.second-1, 7);
                }

                switch (maxMovementIndex()) {
                    case 0:
                        action = ActionsEnum.moveN;
                    break;
                    case 1:
                        action = ActionsEnum.moveNE;
                    break;
                    case 2:
                        action = ActionsEnum.moveE;
                    break;
                    case 3:
                        action = ActionsEnum.moveSE;
                    break;
                    case 4:
                        action = ActionsEnum.moveS;
                    break;
                    case 5:
                        action = ActionsEnum.moveSW;
                    break;
                    case 6:
                        action = ActionsEnum.moveW;
                    break;
                    case 7:
                        action = ActionsEnum.moveNW;
                    break;
                }
            }

            isFound();
        }
    }

    /**
     * @author Alba Rios
     * @return Indice de un movimiento
     * @description Calcula el indice del movimiento que verá más casillas sin explorar
     */
    public int maxMovementIndex() {
        int maxValue = -1, maxIndex = -1;
            for (int i = 0; i < movementActions.length; i++) {
                if (movementActions[i] > maxValue) {
                    maxValue = movementActions[i];
                    maxIndex = i;
                }
        }
        return maxIndex;
    }
    
    /**
     * @author Alba Rios
     * @param x Coordenada
     * @param y Coordenada
     * @param index Indice del elemento a actualizar
     * @description Actualiza los valores de casillas sin explorar para todos los movimientos desde la posicion x,y
     */
    public void calculateSubMovementActions (int x, int y, int index) {
        movementActions[index] += checkNorth(x,y);
        movementActions[index] += checkNorthEast(x,y);
        movementActions[index] += checkEast(x,y);
        movementActions[index] += checkSouthEast(x,y);
        movementActions[index] += checkSouth(x,y);
        movementActions[index] += checkSouthWest(x,y);
        movementActions[index] += checkWest(x,y);
        movementActions[index] += checkNorthWest(x,y);      
    }
    
    /**
     * @author Alba Rios
     * @param x Coordenada
     * @param y Coordenada
     * @return Numero de casillas 0-11
     * @description Calcula e numero de casillas sin visitar si nos movemos al norte desde x,y
     */
    private int checkNorth (int x, int y) {
        if (this.datos.getMapPosition(x,y-1) == 1 || this.datos.getMapPosition(x,y-1) == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x, y-1)) {
            return -1;
        }
        else {
            int result = 0;
            
            if (this.datos.getMapPosition(x, y-6) == -1) result++;
            if (this.datos.getMapPosition(x-1, y-6) == -1) result++;
            if (this.datos.getMapPosition(x-2, y-6) == -1) result++;
            if (this.datos.getMapPosition(x-3, y-6) == -1) result++;
            if (this.datos.getMapPosition(x-4, y-6) == -1) result++;
            if (this.datos.getMapPosition(x-5, y-6) == -1) result++;
            if (this.datos.getMapPosition(x+1, y-6) == -1) result++;
            if (this.datos.getMapPosition(x+2, y-6) == -1) result++;
            if (this.datos.getMapPosition(x+3, y-6) == -1) result++;
            if (this.datos.getMapPosition(x+4, y-6) == -1) result++;
            if (this.datos.getMapPosition(x+5, y-6) == -1) result++;
            
            return result;
        }
    }
    
    /**
     * @author Alba Rios
     * @param x Coordenada
     * @param y Coordenada
     * @return Numero de casillas 0-9
     * @description Calcula e numero de casillas sin visitar si nos movemos al norte desde x,y
     */
    private int checkNorthEast (int x, int y) {
        if (this.datos.getMapPosition(x+1,y-1) == 1 || this.datos.getMapPosition(x+1,y-1) == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x+1, y-1)) {
            return -1;
        }
        else {
            int result = 0;
            
            if (this.datos.getMapPosition(x-4, y-6) == -1) result++;
            if (this.datos.getMapPosition(x-3, y-6) == -1) result++;
            if (this.datos.getMapPosition(x-2, y-6) == -1) result++;
            if (this.datos.getMapPosition(x-1, y-6) == -1) result++;
            if (this.datos.getMapPosition(x, y-6) == -1) result++;
            if (this.datos.getMapPosition(x+1, y-6) == -1) result++;
            if (this.datos.getMapPosition(x+2, y-6) == -1) result++;
            if (this.datos.getMapPosition(x+3, y-6) == -1) result++;
            if (this.datos.getMapPosition(x+4, y-6) == -1) result++;
            if (this.datos.getMapPosition(x+5, y-6) == -1) result++;
            if (this.datos.getMapPosition(x+6, y-6) == -1) result++;
            if (this.datos.getMapPosition(x+6, y-5) == -1) result++;
            if (this.datos.getMapPosition(x+6, y-4) == -1) result++;
            if (this.datos.getMapPosition(x+6, y-3) == -1) result++;
            if (this.datos.getMapPosition(x+6, y-2) == -1) result++;
            if (this.datos.getMapPosition(x+6, y-1) == -1) result++;
            if (this.datos.getMapPosition(x+6, y) == -1) result++;
            if (this.datos.getMapPosition(x+6, y+1) == -1) result++;
            if (this.datos.getMapPosition(x+6, y+2) == -1) result++;
            if (this.datos.getMapPosition(x+6, y+3) == -1) result++;
            if (this.datos.getMapPosition(x+6, y+4) == -1) result++;
            
            
            
            return result;
        }
    }
    
    /**
     * @author Alba Rios
     * @param x Coordenada
     * @param y Coordenada
     * @return Numero de casillas 0-5
     * @description Calcula e numero de casillas sin visitar si nos movemos al este desde x,y
     */
    private int checkEast (int x, int y) {
        if (this.datos.getMapPosition(x+1,y) == 1 || this.datos.getMapPosition(x+1,y) == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x+1, y)) {
            return -1;
        }
        else {
            int result = 0;
            
            if (this.datos.getMapPosition(x+6, y) == -1) result++;
            if (this.datos.getMapPosition(x+6, y-1) == -1) result++;
            if (this.datos.getMapPosition(x+6, y-2) == -1) result++;
            if (this.datos.getMapPosition(x+6, y-3) == -1) result++;
            if (this.datos.getMapPosition(x+6, y-4) == -1) result++;
            if (this.datos.getMapPosition(x+6, y-5) == -1) result++;
            if (this.datos.getMapPosition(x+6, y+1) == -1) result++;
            if (this.datos.getMapPosition(x+6, y+2) == -1) result++;
            if (this.datos.getMapPosition(x+6, y+3) == -1) result++;
            if (this.datos.getMapPosition(x+6, y+4) == -1) result++;
            if (this.datos.getMapPosition(x+6, y+5) == -1) result++;
            
            return result;
        }
    }
    
    /**
     * @author Alba Rios
     * @param x Coordenada
     * @param y Coordenada
     * @return Numero de casillas 0-9
     * @description Calcula e numero de casillas sin visitar si nos movemos al norte desde x,y
     */
    private int checkSouthEast (int x, int y) {
        if (this.datos.getMapPosition(x+1,y+1) == 1 || this.datos.getMapPosition(x+1,y+1) == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x+1, y+1)) {
            return -1;
        }
        else {
            int result = 0;
            
            if (this.datos.getMapPosition(x-4, y+6) == -1) result++;
            if (this.datos.getMapPosition(x-3, y+6) == -1) result++;
            if (this.datos.getMapPosition(x-2, y+6) == -1) result++;
            if (this.datos.getMapPosition(x-1, y+6) == -1) result++;
            if (this.datos.getMapPosition(x, y+6) == -1) result++;
            if (this.datos.getMapPosition(x+1, y+6) == -1) result++;
            if (this.datos.getMapPosition(x+2, y+6) == -1) result++;
            if (this.datos.getMapPosition(x+3, y+6) == -1) result++;
            if (this.datos.getMapPosition(x+4, y+6) == -1) result++;
            if (this.datos.getMapPosition(x+5, y+6) == -1) result++;
            if (this.datos.getMapPosition(x+6, y+6) == -1) result++;
            if (this.datos.getMapPosition(x+6, y+5) == -1) result++;
            if (this.datos.getMapPosition(x+6, y+4) == -1) result++;
            if (this.datos.getMapPosition(x+6, y+3) == -1) result++;
            if (this.datos.getMapPosition(x+6, y+2) == -1) result++;
            if (this.datos.getMapPosition(x+6, y+1) == -1) result++;
            if (this.datos.getMapPosition(x+6, y) == -1) result++;
            if (this.datos.getMapPosition(x+6, y-1) == -1) result++;
            if (this.datos.getMapPosition(x+6, y-2) == -1) result++;
            if (this.datos.getMapPosition(x+6, y-3) == -1) result++;
            if (this.datos.getMapPosition(x+6, y-4) == -1) result++;
            
            return result;
        }
    }
    
    /**
     * @author Alba Rios
     * @param x Coordenada
     * @param y Coordenada
     * @return Numero de casillas 0-5
     * @description Calcula e numero de casillas sin visitar si nos movemos al sur desde x,y
     */
    private int checkSouth (int x, int y) {
        if (this.datos.getMapPosition(x,y+1) == 1 || this.datos.getMapPosition(x,y+1) == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x, y+1)) {
            return -1;
        }
        else {
            int result = 0;
            
            if (this.datos.getMapPosition(x, y+6) == -1) result++;
            if (this.datos.getMapPosition(x-1, y+6) == -1) result++;
            if (this.datos.getMapPosition(x-2, y+6) == -1) result++;
            if (this.datos.getMapPosition(x-3, y+6) == -1) result++;
            if (this.datos.getMapPosition(x-4, y+6) == -1) result++;
            if (this.datos.getMapPosition(x-5, y+6) == -1) result++;
            if (this.datos.getMapPosition(x+1, y+6) == -1) result++;
            if (this.datos.getMapPosition(x+2, y+6) == -1) result++;
            if (this.datos.getMapPosition(x+3, y+6) == -1) result++;
            if (this.datos.getMapPosition(x+4, y+6) == -1) result++;
            if (this.datos.getMapPosition(x+5, y+6) == -1) result++;
            
            return result;
        }
    }
    
    /**
     * @author Alba Rios
     * @param x Coordenada
     * @param y Coordenada
     * @return Numero de casillas 0-9
     * @description Calcula e numero de casillas sin visitar si nos movemos al norte desde x,y
     */
    private int checkSouthWest (int x, int y) {
        if (this.datos.getMapPosition(x-1,y+1) == 1 || this.datos.getMapPosition(x-1,y+1) == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x-1, y+1)) {
            return -1;
        }
        else {
            int result = 0;
            
            if (this.datos.getMapPosition(x+4, y+6) == -1) result++;
            if (this.datos.getMapPosition(x+3, y+6) == -1) result++;
            if (this.datos.getMapPosition(x+2, y+6) == -1) result++;
            if (this.datos.getMapPosition(x+1, y+6) == -1) result++;
            if (this.datos.getMapPosition(x, y+6) == -1) result++;
            if (this.datos.getMapPosition(x-1, y+6) == -1) result++;
            if (this.datos.getMapPosition(x-2, y+6) == -1) result++;
            if (this.datos.getMapPosition(x-3, y+6) == -1) result++;
            if (this.datos.getMapPosition(x-4, y+6) == -1) result++;
            if (this.datos.getMapPosition(x-5, y+6) == -1) result++;
            if (this.datos.getMapPosition(x-6, y+6) == -1) result++;
            if (this.datos.getMapPosition(x-6, y+5) == -1) result++;
            if (this.datos.getMapPosition(x-6, y+4) == -1) result++;
            if (this.datos.getMapPosition(x-6, y+3) == -1) result++;
            if (this.datos.getMapPosition(x-6, y+2) == -1) result++;
            if (this.datos.getMapPosition(x-6, y+1) == -1) result++;
            if (this.datos.getMapPosition(x-6, y) == -1) result++;
            if (this.datos.getMapPosition(x-6, y-1) == -1) result++;
            if (this.datos.getMapPosition(x-6, y-2) == -1) result++;
            if (this.datos.getMapPosition(x-6, y-3) == -1) result++;
            if (this.datos.getMapPosition(x-6, y-4) == -1) result++;
            
            return result;
        }
    }
    
    /**
     * @author Alba Rios
     * @param x Coordenada
     * @param y Coordenada
     * @return Numero de casillas 0-5
     * @description Calcula e numero de casillas sin visitar si nos movemos al este desde x,y
     */
    private int checkWest (int x, int y) {
        if (this.datos.getMapPosition(x-1,y) == 1 || this.datos.getMapPosition(x-1,y) == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x-1, y)) {
            return -1;
        }
        else {
            int result = 0;
            
            if (this.datos.getMapPosition(x-6, y) == -1) result++;
            if (this.datos.getMapPosition(x-6, y-1) == -1) result++;
            if (this.datos.getMapPosition(x-6, y-2) == -1) result++;
            if (this.datos.getMapPosition(x-6, y-3) == -1) result++;
            if (this.datos.getMapPosition(x-6, y-4) == -1) result++;
            if (this.datos.getMapPosition(x-6, y-5) == -1) result++;
            if (this.datos.getMapPosition(x-6, y+1) == -1) result++;
            if (this.datos.getMapPosition(x-6, y+2) == -1) result++;
            if (this.datos.getMapPosition(x-6, y+3) == -1) result++;
            if (this.datos.getMapPosition(x-6, y+4) == -1) result++;
            if (this.datos.getMapPosition(x-6, y+5) == -1) result++;
            
            return result;
        }
    }
    
    /**
     * @author Alba Rios
     * @param x Coordenada
     * @param y Coordenada
     * @return Numero de casillas 0-9
     * @description Calcula e numero de casillas sin visitar si nos movemos al norte desde x,y
     */
    private int checkNorthWest (int x, int y) {
        if (this.datos.getMapPosition(x-1,y-1) == 1 || this.datos.getMapPosition(x-1,y-1) == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x-1, y-1)) {
            return -1;
        }
        else {
            int result = 0;
            
            if (this.datos.getMapPosition(x+4, y-6) == -1) result++;
            if (this.datos.getMapPosition(x+3, y-6) == -1) result++;
            if (this.datos.getMapPosition(x+2, y-6) == -1) result++;
            if (this.datos.getMapPosition(x+1, y-6) == -1) result++;
            if (this.datos.getMapPosition(x, y-6) == -1) result++;
            if (this.datos.getMapPosition(x-1, y-6) == -1) result++;
            if (this.datos.getMapPosition(x-2, y-6) == -1) result++;
            if (this.datos.getMapPosition(x-3, y-6) == -1) result++;
            if (this.datos.getMapPosition(x-4, y-6) == -1) result++;
            if (this.datos.getMapPosition(x-5, y-6) == -1) result++;
            if (this.datos.getMapPosition(x-6, y-6) == -1) result++;
            if (this.datos.getMapPosition(x-6, y-5) == -1) result++;
            if (this.datos.getMapPosition(x-6, y-4) == -1) result++;
            if (this.datos.getMapPosition(x-6, y-3) == -1) result++;
            if (this.datos.getMapPosition(x-6, y-2) == -1) result++;
            if (this.datos.getMapPosition(x-6, y-1) == -1) result++;
            if (this.datos.getMapPosition(x-6, y) == -1) result++;
            if (this.datos.getMapPosition(x-6, y+1) == -1) result++;
            if (this.datos.getMapPosition(x-6, y+2) == -1) result++;
            if (this.datos.getMapPosition(x-6, y+3) == -1) result++;
            if (this.datos.getMapPosition(x-6, y+4) == -1) result++;
            
            return result;
        }
    }
    
    @Override
    public void secondLogic() {
        String result= heuristica.heuristic();
    }

    /**
     *
     * @author Andrés Ortiz Corrales
     * @description Almacenamiento de datos de sensor particular al halcón
     */
    @Override
    protected void fillDatesRole(JsonArray sensor) {
        fillDates(5, 6, sensor);
    }
}
