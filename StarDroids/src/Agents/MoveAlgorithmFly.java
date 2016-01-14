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
