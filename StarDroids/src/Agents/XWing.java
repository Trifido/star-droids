package Agents;

import com.eclipsesource.json.JsonArray;
import helpers.Pair;

/**
 *
 * @author Andrés Ortiz, Alba Rios
 * @description Representa el rol mosca
 */
public class XWing extends Role {
    //Or fly (mosca)

    
    private Algoritmo heuristica;

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
        this.positioning = true;
        this.turn = false;
        this.turnCount = 0;
    }
    
    public void showRadar(){
        for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) { 
                    System.out.print( radar[i][j] + " ");   
                }
                System.out.println();
        }
    }
    
    /**
     * @author Alba Rios
     * @description Actualiza a true la variable found si la meta ha sido vista
     */
    @Override
    protected void isFound() {
        for (int i = 0; i < 3; i++) {
           for (int j = 0; j < 3; j++) {
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
                solution.first = myPosition.first - 1;
                break;
            case 1:
                solution.first = myPosition.first;
                break;
            case 2:
                solution.first = myPosition.first +1;
                break;
        }
        
        switch (y) {
            case 0:
                solution.second = myPosition.second - 1;
                break;
            case 1:
                solution.second = myPosition.second;
                break;
            case 2:
                solution.second = myPosition.second +1;
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
            if (this.datos.getFuel() <= 2) {
                this.action = ActionsEnum.battery; // Recargar batería
            }
            else if (this.positioning) {
                //this.action = ; Trasladar a la posición 0,0 (funcion vicente)
                Algoritmo alg= new Algoritmo(new Pair(movDistance-2,movDistance-2), myPosition, this.datos.getWorldMap(), this.datos.getAllShips(), this.datos.getFuel());
                this.action= alg.heuristic1();

                if (myPosition.first == movDistance-2 && myPosition.second == movDistance-2) 
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
                            int x = (int) this.datos.getPosition().first + 1; 
                            int y = (int) this.datos.getPosition().second;
                            
                            if (checkShips(x, y)) {
                                this.action = ActionsEnum.moveN;
                            }
                            else {
                                this.turnCount += 1;
                                this.action = ActionsEnum.moveE;
                            }
                        }
                    } 
                    else { // Nos desplazamos hacia abajo
                        int border = this.radar[2][1];
                        int x = (int) this.datos.getPosition().first; 
                        int y = (int) this.datos.getPosition().second + 1;

                        if (border == 2 || checkShips(x, y)) { // Encontramos el borde -> este o una nave
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
                            int x = (int) this.datos.getPosition().first + 1; 
                            int y = (int) this.datos.getPosition().second;
                            
                            if (checkShips(x, y)) {
                                this.action = ActionsEnum.moveS;
                            }
                            else {
                                this.turnCount += 1;
                                this.action = ActionsEnum.moveE;
                            }
                        }
                    }
                    else { // Nos desplazamos hacia arriba
                        int border = this.radar[0][1];
                        int x = (int) this.datos.getPosition().first; 
                        int y = (int) this.datos.getPosition().second - 1;

                        if (border == 2 || checkShips(x, y)) { // Encontramos el borde -> este
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

            isFound();
        }
    }

    @Override
    public void secondLogic() {
        System.out.println("GOAL: " + goal.first + " , " + goal.second);
        heuristica= new Algoritmo(goal, this.datos.getPosition(), this.datos.getWorldMap(), this.datos.getAllShips(), this.datos.getFuel());
        this.action= heuristica.heuristic1();
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
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){ 
                this.radar[i][j]=sensor.get(count).asInt();
                count++;
            }
        }
        
        //Actualizar mapa del mundo
        int x = (Integer) this.datos.getPosition().first;
        int y = (Integer) this.datos.getPosition().second;
        int index = 0;

        for(int j = y-1 ; j < y+1; j++) {
            for(int i = x-1 ; i < x+1; i++) {
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
                Pair<Integer,Integer> coord = mapToWorld(i,j);
                if (checkShips(coord.first, coord.second)) // Si hay otra nave ahí
                    this.miniRadar[i][j] = 2; // Rellenar con un 2
                else
                    this.miniRadar[i][j] = this.radar[i][j]; // Rellenar con el radar
            }
        }
    }
    
}
