package mapproject;

/**
 * Enum con los diferentes tiles que vamos a utilizar
 * 
 * @author Alba Ríos
 */
public enum TileType {
    Grass("grass", true), Dirt("dirt", false), Rock("rock", false), Goal("goal", false), Bot("bot",false);
    
    String textureName;
    boolean buildable;
    
    TileType(String textureName, boolean buildable){
        this.textureName = textureName;
        this.buildable = buildable;
    }
}
