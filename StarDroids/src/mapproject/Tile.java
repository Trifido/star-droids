package mapproject;

import static helpers.Artist.*;
import org.newdawn.slick.opengl.Texture;
/**
 * Clase que representa una celda individual del mapa
 *
 * @author Alba Ríos
 */
public class Tile {
    private float x, y, width, height;
    private Texture texture;
    
    /**
     * 
     * @param x Coordenada x del pixel del mapa
     * @param y Coordenada y del pixel del mapa
     * @param width Ancho del tile
     * @param height Alto del tile
     * @param tex Textura del tile
     * 
     * @author Alba Ríos
     */
    public Tile(float x, float y, float width, float height, Texture tex){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.texture = tex;
    }
    
    public void Draw(){
        DrawQuadTex(texture, x, y, width, height);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    
}
