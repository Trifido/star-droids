package helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import mapproject.MapProject;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.*;
import org.newdawn.slick.util.ResourceLoader;

/**
 * Clase que gestiona la creación de la interfaz gráfica
 * 
 * @author Alba Ríos
 */
public class Artist {

    public static final int WIDTH = 1000, HEIGHT = 1000;
    private static int minX = 0, minY= 0, maxX = WIDTH, maxY = HEIGHT;
    private static int activeZoom = 1;
    
    /**
    * Constructor para inicializar los datos
    * 
    * @author Alba Ríos
    */
    public static void BeginSession(){
        Display.setTitle("Bot Simulator"); //Screen title
        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT)); //Screen dimension
            Display.create(); //Screen creation
        } catch (LWJGLException ex) {
            Logger.getLogger(MapProject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);
    }  
    
    /**
     * Método que dibuja un cuadrado
     * 
     * @param x La coordenada x de la esquina superior izquierda
     * @param y La coordenada  de la esquina superior izquierda
     * @param width El ancho
     * @param height El alto
     * 
     * @author Alba Ríos 
     */
    public static void DrawQuad(float x, float y, float width, float height){
        glBegin(GL_QUADS);
            glVertex2f(x,y); //Top left corner
            glVertex2f(x+width,y); //Top right corner
            glVertex2f(x+width,y+height); //Bottom right corner
            glVertex2f(x,y+height); //Bottom left corner
        glEnd();
    }
    
    /**
     * Método que dibuja un cuadrado con una textura
     * 
     * @param tex Textura
     * @param x La coordenada x de la esquina superior izquierda
     * @param y La coordenada  de la esquina superior izquierda
     * @param width El ancho
     * @param height El alto 
     * 
     * @author Alba Ríos
     */
    public static void DrawQuadTex(Texture tex, float x, float y, float width, float height){
        tex.bind();
        glTranslatef(x,y,0);
        glBegin(GL_QUADS);
            glTexCoord2f(0,0);
            glVertex2f(0,0);
            glTexCoord2f(1,0);
            glVertex2f(width,0);
            glTexCoord2f(1,1);
            glVertex2f(width,height);
            glTexCoord2f(0,1);
            glVertex2f(0,height);
        glEnd();
        glLoadIdentity();
    }
    
    /**
     * Método para crear una texura
     * 
     * @param path Path de la textura
     * @param fileType Tipo de la textura
     * @return Textura
     * 
     * @author Alba Ríos
     */
    public static Texture LoadTexture(String path, String fileType){
        System.out.println( System.getProperty("user.dir"));
        Texture tex = null;
        InputStream in = ResourceLoader.getResourceAsStream(path);
        try {
            tex = TextureLoader.getTexture(fileType, in);
        } catch (IOException ex) {
            Logger.getLogger(Artist.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tex;
    }
    
    /**
     * Método rápido para crear una textura PNG ubicada en la carpeta res/
     * 
     * @param name Nombre del archivo
     * @return Textura
     * 
     * @author Alba Ríos
     */
    public static Texture QuickLoad(String name){
        Texture tex = null;
        tex = LoadTexture("res/"+name+".png", "PNG");
        return tex;
    }
    
    /**
     * Función que modifica la vista haciendo o deshaciendo ZOOM.
     * ATENCIÓN: 
     *  Mouse.getY() devuelve la posición de Y de abajo a arriba. 
     *  Mouse.getX() de izquierda a derecha.
     * 
     * @param zoom Valor del zoom. Min: 1. Max: N (especificado en MapProject.
     * 
     * @author Alba Ríos
     */
    public static void applyZoom(int zoom){
        int size = WIDTH/zoom, halfSize = size/2; //WIDTH o HEIGHT, porque es cuadrado. Largo y ancho del viewport.
        int mouseX = (Mouse.getX()/activeZoom + minX), mouseY = ((HEIGHT - Mouse.getY())/activeZoom + minY);
        activeZoom = zoom;
        
        //Calcular el X máximo y mínimo de la vista
        if (mouseX < halfSize){ //
            minX = 0; 
            maxX = size;
        }
        else if (mouseX > (WIDTH - halfSize)){ 
            minX = WIDTH - size; 
            maxX = WIDTH;
        }
        else{
            minX = mouseX - halfSize; 
            maxX = mouseX + halfSize;
        }
        
        //Calcular el Y máximo y mínimo de la vista
        if (mouseY < halfSize){ 
            minY = 0; 
            maxY = size;
        }
        else if (mouseY > (HEIGHT - halfSize)){ 
            minY = HEIGHT - size; 
            maxY = HEIGHT;
        }
        else{
            minY = mouseY - halfSize; 
            maxY = mouseY + halfSize;
        }

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(minX, maxX, maxY, minY, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        
        //System.out.println("Prueba: " + " Pinchado en : " + mouseX + " , "+ mouseY);
        //System.out.println(" Min x: " + minX + " , Max x: "+ maxX);
        //System.out.println(" Min y: " + minY + " , Max y: "+ maxY);
    }
    
}
