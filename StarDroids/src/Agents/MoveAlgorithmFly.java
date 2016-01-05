/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import helpers.Pair;

/**
 *
 * @author Vicente
 */
public class MoveAlgorithmFly extends MoveAlgorithm{

    public MoveAlgorithmFly(Pair<Integer, Integer> posFinal, Pair<Integer, Integer> posActual, Pair<Integer, Integer>[][] world) {
        super(posFinal, posActual, world);
    }
    
    @Override
    public String heuristic(){
        return super.heuristic1();
    }
    
}
