/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import helpers.Pair;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Vicente
 */
public class MoveAlgorithm {
    private Pair<Integer,Integer> posFinal;
    private Queue<Pair<Integer,Integer>> cola;
    private Set<Pair<Integer,Integer>> activos;
    
    private void calcularAdyacentes(Pair<Integer,Integer> pos, Pair<Integer,Integer>[][]world){
        
        if((pos.first!=0 || pos.first!=499) && (pos.second!=0 || pos.second!=499)){
            
            if( (world[pos.first-1][pos.second-1].first != 1 /*obstaculo*/) && (!activos.contains(new Pair(pos.first-1, pos.second-1))) ){
                world[pos.first-1][pos.second-1].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first-1][pos.second-1]);
                activos.add(world[pos.first-1][pos.second-1]);
            }
            if( (world[pos.first][pos.second-1].first != 1 /*obstaculo*/) && (!activos.contains(new Pair(pos.first, pos.second-1))) ){
                world[pos.first][pos.second-1].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first][pos.second-1]);
                activos.add(world[pos.first][pos.second-1]);
            }
            if( (world[pos.first-1][pos.second].first != 1 /*obstaculo*/) && (!activos.contains(new Pair(pos.first-1, pos.second))) ){
                world[pos.first-1][pos.second].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first-1][pos.second]);
                activos.add(world[pos.first-1][pos.second]);
            }
            if( (world[pos.first+1][pos.second+1].first != 1 /*obstaculo*/) && (!activos.contains(new Pair(pos.first+1, pos.second+1))) ){
                world[pos.first+1][pos.second+1].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first+1][pos.second+1]);
                activos.add(world[pos.first+1][pos.second+1]);
            }
            if( (world[pos.first][pos.second+1].first != 1 /*obstaculo*/) && (!activos.contains(new Pair(pos.first, pos.second+1))) ){
                world[pos.first][pos.second+1].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first][pos.second+1]);
                activos.add(world[pos.first][pos.second+1]);
            }
            if( (world[pos.first+1][pos.second].first != 1 /*obstaculo*/) && (!activos.contains(new Pair(pos.first+1, pos.second))) ){
                world[pos.first+1][pos.second].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first+1][pos.second]);
                activos.add(world[pos.first+1][pos.second]);
            }
            if( (world[pos.first+1][pos.second-1].first != 1 /*obstaculo*/) && (!activos.contains(new Pair(pos.first+1, pos.second-1))) ){
                world[pos.first+1][pos.second-1].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first+1][pos.second-1]);
                activos.add(world[pos.first+1][pos.second-1]);
            }
            if( (world[pos.first-1][pos.second+1].first != 1 /*obstaculo*/) && (!activos.contains(new Pair(pos.first-1, pos.second+1))) ){
                world[pos.first-1][pos.second+1].second= world[pos.first][pos.second].second + 1;
                cola.add(world[pos.first-1][pos.second+1]);
                activos.add(world[pos.first-1][pos.second+1]);
            }
        }
        cola.remove();
    }
    
    public void calcularMap(Pair<Integer,Integer> pos, Pair<Integer,Integer>[][]world){
        pos.second=0;
        cola.add(pos);
        while(!cola.isEmpty()){
            calcularAdyacentes(cola.element(),world);
        }
    }
    
    
}
