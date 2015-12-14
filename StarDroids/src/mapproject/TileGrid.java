package mapproject;

import static helpers.Artist.*;
import org.newdawn.slick.opengl.Texture;

/**
 * Clase que contiene el mapa del mundo
 *
 * @author Alba Ríos
 */
public class TileGrid {
    
    public static final int TILE_SIZE = 2; //Anchura y altura de un Tile
    public Tile[][] map;
    private int tilesWide = 500, tilesHigh = 500; //Ancho y alto del mapa
    private Texture empty, unknown, obstacle, goal, bot;
    
    /**
     * Constructor que crea un mapa
     * 
     * @author Alba Ríos
     */
    public TileGrid(){
        empty = QuickLoad((TileType.Grass).textureName);
        unknown = QuickLoad((TileType.Dirt).textureName);
        obstacle = QuickLoad((TileType.Rock).textureName);
        goal = QuickLoad((TileType.Goal).textureName);
        bot = QuickLoad((TileType.Bot).textureName);
        map = new Tile[tilesWide][tilesHigh];
        for(int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                map[i][j] = new Tile(i*TILE_SIZE,j*TILE_SIZE,TILE_SIZE,TILE_SIZE, unknown);
            }
        }
    }
    
    /**
     * Constructor que crea un mapa a partir de una matriz
     * 
     * @param newMap Matriz de enteros con la información del mapa
     * 
     * @author Alba Ríos
     */
    public TileGrid(int[][] newMap){
        empty = QuickLoad((TileType.Grass).textureName);
        unknown = QuickLoad((TileType.Dirt).textureName);
        obstacle = QuickLoad((TileType.Rock).textureName);
        goal = QuickLoad((TileType.Goal).textureName);
        bot = QuickLoad((TileType.Bot).textureName);
        map = new Tile[tilesWide][tilesHigh];
        for(int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                switch (newMap[j][i]){
                    case 0:
                        map[i][j] = new Tile(i*TILE_SIZE,j*TILE_SIZE,TILE_SIZE,TILE_SIZE, empty);
                        break;
                    case 1:
                        map[i][j] = new Tile(i*TILE_SIZE,j*TILE_SIZE,TILE_SIZE,TILE_SIZE, unknown);
                        break;
                    case 2:
                        map[i][j] = new Tile(i*TILE_SIZE,j*TILE_SIZE,TILE_SIZE,TILE_SIZE, obstacle);
                        break;
                    case 3:
                        map[i][j] = new Tile(i*TILE_SIZE,j*TILE_SIZE,TILE_SIZE,TILE_SIZE, goal);
                        break;
                }
            }
        }
    }
    
    /**
     * Método que cambia la textura de un tile
     * 
     * @param xCoord Coordenada x de la localización del tile en el mapa
     * @param yCoord Coordenada y de la localización del tile en el mapa
     * @param type Tipo de textura a la que se va a actualizar el tile 
     *
     * @author Alba Ríos
     */
    public void setTile(int xCoord, int yCoord, TileType type){
        switch(type){
            case Grass:
                map[xCoord][yCoord] = new Tile(xCoord*TILE_SIZE, yCoord*TILE_SIZE, TILE_SIZE, TILE_SIZE, empty);
                break;
            case Dirt:
                map[xCoord][yCoord] = new Tile(xCoord*TILE_SIZE, yCoord*TILE_SIZE, TILE_SIZE, TILE_SIZE, unknown);
                break;
            case Rock:
                map[xCoord][yCoord] = new Tile(xCoord*TILE_SIZE, yCoord*TILE_SIZE, TILE_SIZE, TILE_SIZE, obstacle);
                break;
            case Goal:
                map[xCoord][yCoord] = new Tile(xCoord*TILE_SIZE, yCoord*TILE_SIZE, TILE_SIZE, TILE_SIZE, goal);
                break;
            case Bot:
                map[xCoord][yCoord] = new Tile(xCoord*TILE_SIZE, yCoord*TILE_SIZE, TILE_SIZE, TILE_SIZE, bot);
                break;
        }
    }
    
    /**
     * Método que devuelve un tile específico
     * 
     * @param xCoord Coordenada x de la localización del tile en el mapa
     * @param yCoord Coordenada y de la localización del tile en el mapa
     * @return El tile que se especifica
     * 
     * @author Alba Ríos
     */
    public Tile getTile (int xCoord, int yCoord){
        return map[xCoord][yCoord];
    }
    
    /**
     * Método que dibuja el mapa 
     * 
     * @author Alba Ríos
     */
    public void Draw(){
        for(int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                Tile t = map[i][j];
                DrawQuadTex(t.getTexture(), t.getX(), t.getY(), t.getWidth(), t.getHeight());
            }
        }
    }
}
