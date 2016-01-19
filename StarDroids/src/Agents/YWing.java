package Agents;

import com.eclipsesource.json.JsonArray;
import helpers.Pair;
import java.util.Random;

/**
 *
 * @author Andrés Ortiz, Alba Rios
 */
public class YWing extends Role {
    //YWing or bird (pajaro)

    private Algoritmo heuristica;
    
    // Variables de modo búsqueda de objetivo
    private boolean positioning;
    int[] movementActions = { 0, 0, 0, 0, 0, 0, 0, 0 }; // N, NE, E, SE, S, SW, W, NW

    public YWing() {
        super();
        this.radar = new int[5][5];
        
        this.positioning = false;
    }
    
    public void showRadar(){
    
        for (int i = 0; i < 5; i++) {
               
               System.out.println( radar[i][0] + " " +
                                    radar[i][1] + " " +
                                    radar[i][2] + " " +
                                    radar[i][3] + " " +
                                    radar[i][4] + " " 
               );
        }
    }
    /**
     * @author Alba Rios
     * @description Actualiza a true la variable found si la meta ha sido vista
     */
    @Override
    protected void isFound() {
        for (int i = 0; i < 5; i++) {
           for (int j = 0; j < 5; j++) {
               if (radar[i][j] == 3) {
                   this.found = true;
                   goal = mapToWorld (i, j);
                   //this.datos.setGoalPosition(mapToWorld (i, j).first, mapToWorld (i, j).second);
               }
            } 
        }
        System.out.println("isFound= " + this.found);
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
                solution.first = myPosition.first - 2;
                break;
            case 1:
                solution.first = myPosition.first - 1;
                break;
            case 2:
                solution.first = myPosition.first;
                break;
            case 3:
                solution.first = myPosition.first + 1;
                break;
            case 4:
                solution.first = myPosition.first + 2;
                break;
        }
        
        switch (y) {
            case 0:
                solution.second = myPosition.second - 2;
                break;
            case 1:
                solution.second = myPosition.second - 1;
                break;
            case 2:
                solution.second = myPosition.second;
                break;
            case 3:
                solution.second = myPosition.second + 1;
                break;
            case 4:
                solution.second = myPosition.second + 2;
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
        this.showRadar();
        
        if (!found) {
            if (this.datos.getFuel() <= 1) {
                this.action = ActionsEnum.battery; // Recargar batería
            }
            // Al final no se posiciona en el centro. Se podria hacer.  
            else {
                // Hacemos dos niveles de búsqueda
                //Nivel 1
                movementActions[0] = checkNorth(myPosition.first, myPosition.second);
                movementActions[1] = checkNorthEast(myPosition.first, myPosition.second);
                movementActions[2] = checkEast(myPosition.first, myPosition.second);
                movementActions[3] = checkSouthEast(myPosition.first, myPosition.second);
                movementActions[4] = checkSouth(myPosition.first, myPosition.second);
                movementActions[5] = checkSouthWest(myPosition.first, myPosition.second);
                movementActions[6] = checkWest(myPosition.first, myPosition.second);
                movementActions[7] = checkNorthWest(myPosition.first, myPosition.second);  

                //Nivel 2
                if (movementActions[maxMovementIndex()] < 6) { // 2/3 del max (9)
                    if (movementActions[0] != -1) calculateSubMovementActions(myPosition.first, myPosition.second-1, 0);
                    if (movementActions[1] != -1) calculateSubMovementActions(myPosition.first+1, myPosition.second-1, 1);
                    if (movementActions[2] != -1) calculateSubMovementActions(myPosition.first+1, myPosition.second, 2);
                    if (movementActions[3] != -1) calculateSubMovementActions(myPosition.first+1, myPosition.second+1, 3);
                    if (movementActions[4] != -1) calculateSubMovementActions(myPosition.first, myPosition.second+1, 4);
                    if (movementActions[5] != -1) calculateSubMovementActions(myPosition.first-1, myPosition.second+1, 5);
                    if (movementActions[6] != -1) calculateSubMovementActions(myPosition.first-1, myPosition.second, 6);
                    if (movementActions[7] != -1) calculateSubMovementActions(myPosition.first-1, myPosition.second-1, 7);
                }
                

                //N es 1, solo se aplicara cuando no hay nada mejor (cambiar si es necesario)
                if (movementActions[maxMovementIndex()] < 1) { // SI EL MOVIMIENTO MAXIMO ES MENOR QUE N (LO QUE TU QUIERAS)
                   action=getRandomAction();
                }else{
                // Se selecciona la acción con el valor más alto
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
                
                /*for (int i = 0; i < this.movementActions.length; i++)
                    System.out.print(" --- " + this.movementActions[i]); //Para mostrar el vector con las posiciones y sus valores
                System.out.println();*/
            }
        }
        isFound();
       
    }
    
   /**
     * @author Andres Ortiz
     * @description Accion aleatoria si la heuristica no sabe que hacer
     */
    protected ActionsEnum getRandomAction(){
        boolean actionFound=false;
        ActionsEnum act=ActionsEnum.sleep;
        while(!actionFound){
            Random rand=new Random();
            int x=rand.nextInt(3); //between 0 and 2 (inclusive both)
            int y=rand.nextInt(3);
            
           x++;
           y++; //between 1 and 3
                    
        if ((x==2 && y==2) || this.radar[x][y] == 1 || this.radar[x][y] == 2 || checkShips(x,y)) { // Obstáculos
        actionFound=false;
        }else{
            actionFound=true;
            if(x==1 && y==1) act=ActionsEnum.moveNW;
            if(x==1 && y==2) act=ActionsEnum.moveN;
            if(x==1 && y==3) act=ActionsEnum.moveNE;
            if(x==2 && y==1) act=ActionsEnum.moveW;
            if(x==2 && y==3) act=ActionsEnum.moveE;
            if(x==3 && y==1) act=ActionsEnum.moveSW;
            if(x==3 && y==2) act=ActionsEnum.moveS;
            if(x==3 && y==3) act=ActionsEnum.moveSE;
        }                     
        }
       return act; 
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
        int result;
        
        result = checkNorth(x,y);
        if (result > -1)
            movementActions[index] += result;
        result = checkNorthEast(x,y);
        if (result > -1)
            movementActions[index] += result;
        result = checkEast(x,y);
        if (result > -1)
            movementActions[index] += result;
        result = checkSouthEast(x,y);
        if (result > -1)
            movementActions[index] += result;
        result = checkSouth(x,y);
        if (result > -1)
            movementActions[index] += result;
        result = checkSouthWest(x,y);
        if (result > -1)
            movementActions[index] += result;
        result = checkWest(x,y);
        if (result > -1)
            movementActions[index] += result;
        result = checkNorthWest(x,y);
        if (result > -1)
            movementActions[index] += result;      
    }
    
    /**
     * @author Alba Rios
     * @param x Coordenada
     * @param y Coordenada
     * @return Numero de casillas 0-5
     * @description Calcula e numero de casillas sin visitar si nos movemos al norte desde x,y
     */
    private int checkNorth (int x, int y) {
        if (this.radar[1][2] == 1 || this.radar[1][2] == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x, y-1)) {
            return -1;
        }
        else {
            int result = 0;
            
            for (int i = -2; i <= +2; i++ ) {
                if (x+i >= 0 && x+i <=499 && y-3 >= 0 && y-3 <= 499) { // Si se puede acceder
                    if ( this.datos.getMapPosition(x+i, y-3) == -1) result++;
                }
            }
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
        if (this.radar[1][3] == 1 || this.radar[1][3] == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x+1, y-1)) {
            return -1;
        }
        else {
            int result = 0;
            
            if (x+3 >= 0 && x+3 <=499 && y-3 >= 0 && y-3 <= 499)
                if (this.datos.getMapPosition(x+3, y-3) == -1) result++;
            if (x+2 >= 0 && x+2 <=499 && y-3 >= 0 && y-3 <= 499)
                if (this.datos.getMapPosition(x+2, y-3) == -1) result++;
            if (x+1 >= 0 && x+1 <=499 && y-3 >= 0 && y-3 <= 499)
                if (this.datos.getMapPosition(x+1, y-3) == -1) result++;
            if (x >= 0 && x <=499 && y-3 >= 0 && y-3 <= 499)
                if (this.datos.getMapPosition(x, y-3) == -1) result++;
            if (x-1 >= 0 && x-1 <=499 && y-3 >= 0 && y-3 <= 499)
                if (this.datos.getMapPosition(x-1, y-3) == -1) result++;
            if (x+3 >= 0 && x+3 <=499 && y-2 >= 0 && y-2 <= 499)
                if (this.datos.getMapPosition(x+3, y-2) == -1) result++;
            if (x+3 >= 0 && x+3 <=499 && y-1 >= 0 && y-1 <= 499)
                if (this.datos.getMapPosition(x+3, y-1) == -1) result++;
            if (x+3 >= 0 && x+3 <=499 && y >= 0 && y <= 499)
                if (this.datos.getMapPosition(x+3, y) == -1) result++;
            if (x+3 >= 0 && x+3 <=499 && y+1 >= 0 && y+1 <= 499)
                if (this.datos.getMapPosition(x+3, y+1) == -1) result++;
            
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
        if (this.radar[2][3] == 1 || this.radar[2][3] == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x+1, y)) {
            return -1;
        }
        else {
            int result = 0;
            
            for (int i = -2; i <= +2; i++ ) {
                if (x+3 >= 0 && x+3 <=499 && y+i >= 0 && y+i <= 499) { // Si se puede acceder
                    if ( this.datos.getMapPosition(x+3, y+i) == -1) result++;
                }
            }
            
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
        if (this.radar[3][3] == 1 || this.radar[3][3] == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x+1, y+1)) {
            return -1;
        }
        else {
            int result = 0;
            
            if (x+3 >= 0 && x+3 <=499 && y+3 >= 0 && y+3 <= 499)
                if (this.datos.getMapPosition(x+3, y+3) == -1) result++;
            if (x+2 >= 0 && x+2 <=499 && y+3 >= 0 && y+3 <= 499)
                if (this.datos.getMapPosition(x+2, y+3) == -1) result++;
            if (x+1 >= 0 && x+1 <=499 && y+3 >= 0 && y+3 <= 499)
                if (this.datos.getMapPosition(x+1, y+3) == -1) result++;
            if (x >= 0 && x <=499 && y+3 >= 0 && y+3 <= 499)
                if (this.datos.getMapPosition(x, y+3) == -1) result++;
            if (x-1 >= 0 && x-1 <=499 && y+3 >= 0 && y+3 <= 499)
                if (this.datos.getMapPosition(x-1, y+3) == -1) result++;
            if (x+3 >= 0 && x+3 <=499 && y-2 >= 0 && y-2 <= 499)
                if (this.datos.getMapPosition(x+3, y-2) == -1) result++;
            if (x+3 >= 0 && x+3 <=499 && y-1 >= 0 && y-1 <= 499)
                if (this.datos.getMapPosition(x+3, y-1) == -1) result++;
            if (x+3 >= 0 && x+3 <=499 && y >= 0 && y <= 499)
                if (this.datos.getMapPosition(x+3, y) == -1) result++;
            if (x+3 >= 0 && x+3 <=499 && y+1 >= 0 && y+1 <= 499)
                if (this.datos.getMapPosition(x+3, y+1) == -1) result++;
            
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
        if (this.radar[3][2] == 1 || this.radar[3][2] == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x, y+1)) {
            return -1;
        }
        else {
            int result = 0;
            
            for (int i = -2; i <= +2; i++ ) {
                if (x+i >= 0 && x+i <=499 && y+3 >= 0 && y+3 <= 499) { // Si se puede acceder
                    if ( this.datos.getMapPosition(x+i, y+3) == -1) result++;
                }
            }
            
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
        if (this.radar[3][1] == 1 || this.radar[3][1] == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x-1, y+1)) {
            return -1;
        }
        else {
            int result = 0;
            
            if (x-3 >= 0 && x-3 <=499 && y+3 >= 0 && y+3 <= 499)
                if (this.datos.getMapPosition(x-3, y+3) == -1) result++;
            if (x-2 >= 0 && x-2 <=499 && y+3 >= 0 && y+3 <= 499)
                if (this.datos.getMapPosition(x-2, y+3) == -1) result++;
            if (x-1 >= 0 && x-1 <=499 && y+3 >= 0 && y+3 <= 499)
                if (this.datos.getMapPosition(x-1, y+3) == -1) result++;
            if (x >= 0 && x <=499 && y+3 >= 0 && y+3 <= 499)
                if (this.datos.getMapPosition(x, y+3) == -1) result++;
            if (x+1 >= 0 && x+1 <=499 && y+3 >= 0 && y+3 <= 499)
                if (this.datos.getMapPosition(x+1, y+3) == -1) result++;
            if (x-3 >= 0 && x-3 <=499 && y-2 >= 0 && y-2 <= 499)
                if (this.datos.getMapPosition(x-3, y-2) == -1) result++;
            if (x-3 >= 0 && x-3 <=499 && y-1 >= 0 && y-1 <= 499)
                if (this.datos.getMapPosition(x-3, y-1) == -1) result++;
            if (x-3 >= 0 && x-3 <=499 && y >= 0 && y <= 499)
                if (this.datos.getMapPosition(x-3, y) == -1) result++;
            if (x-3 >= 0 && x-3 <=499 && y+1 >= 0 && y+1 <= 499)
                if (this.datos.getMapPosition(x-3, y+1) == -1) result++;
            
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
        if (this.radar[2][1] == 1 || this.radar[2][1] == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x-1, y)) {
            return -1;
        }
        else {
            int result = 0;
            
            for (int i = -2; i < +2; i++ ) {
                if (x-3 >= 0 && x-3 <=499 && y+i >= 0 && y+i <= 499) { // Si se puede acceder
                    if ( this.datos.getMapPosition(x-3, y+i) == -1) result++;
                }
            }
            
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
        if (this.radar[1][1] == 1 || this.radar[1][1] == 2) { // Obstáculos
            return -1;
        }
        else if (checkShips(x-1, y-1)) {
            return -1;
        }
        else {
            int result = 0;
            
            if (x-3 >= 0 && x-3 <=499 && y-3 >= 0 && y-3 <= 499)
                if (this.datos.getMapPosition(x-3, y-3) == -1) result++;
            if (x-2 >= 0 && x-2 <=499 && y-3 >= 0 && y-3 <= 499)
                if (this.datos.getMapPosition(x-2, y-3) == -1) result++;
            if (x-1 >= 0 && x-1 <=499 && y-3 >= 0 && y-3 <= 499)
                if (this.datos.getMapPosition(x-1, y-3) == -1) result++;
            if (x >= 0 && x <=499 && y-3 >= 0 && y-3 <= 499)
                if (this.datos.getMapPosition(x, y-3) == -1) result++;
            if (x+1 >= 0 && x+1 <=499 && y-3 >= 0 && y-3 <= 499)
                if (this.datos.getMapPosition(x+1, y-3) == -1) result++;
            if (x-3 >= 0 && x-3 <=499 && y-2 >= 0 && y-2 <= 499)
                if (this.datos.getMapPosition(x-3, y-2) == -1) result++;
            if (x-3 >= 0 && x-3 <=499 && y-1 >= 0 && y-1 <= 499)
                if (this.datos.getMapPosition(x-3, y-1) == -1) result++;
            if (x-3 >= 0 && x-3 <=499 && y >= 0 && y <= 499)
                if (this.datos.getMapPosition(x-3, y) == -1) result++;
            if (x-3 >= 0 && x-3 <=499 && y+1 >= 0 && y+1 <= 499)
                if (this.datos.getMapPosition(x-3, y+1) == -1) result++;
            
            return result;
        }
    }

    @Override
    public void secondLogic() {
        System.out.println("GOAL: " + goal.first + " , " + goal.second);
        heuristica= new Algoritmo(goal, this.datos.getPosition(), this.datos.getWorldMap(), this.datos.getAllShips(), this.datos.getFuel(), this.miniRadar);
        this.action= heuristica.heuristic();
        System.out.println("Action= " + this.action);
    }
    
    /**
     *
     * @author Andrés Ortiz Corrales y Rafael Ruiz
     * @description Almacenamiento de datos de sensor particular a la mosca
     */
    @Override
    protected void fillDatesRole(JsonArray sensor) {
        //Rellenar el radar interno
        int count=0;
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                this.radar[i][j]=sensor.get(count).asInt();
                count++;
            }
        }
        
        //Actualizar mapa del mundo
        int x = (Integer) this.datos.getPosition().first;
        int y = (Integer) this.datos.getPosition().second;
        int index = 0;

        for(int j = y-2 ; j < y+2; j++) {
            for(int i = x-2 ; i < x+2; i++) {
                if(i>=0 && i<=499 && j>=0 && j<=499)
                    this.datos.setWorldMap(i, j, sensor.get(index).asInt());
                index++;
            }
        }
    }

    /**
     * @author Alba Rios
     * @description Transforma el radar en un radar de 3x3 y lo almacena en la variable de clase "miniRadar"
     */
    @Override
    protected void calculateMiniRadar() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Pair<Integer,Integer> coord = mapToWorld(i+1,j+1);
                if (checkShips(coord.first, coord.second)) // Si hay otra nave ahí
                    this.miniRadar[i][j] = 2; // Rellenar con un 2
                else
                    this.miniRadar[i][j] = this.radar[i+1][j+1]; // Rellenar con el radar
            }
        }
        this.miniRadar[1][1]=0;
    }
}
