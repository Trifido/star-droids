package mapproject;

import static helpers.Artist.*;
import java.awt.Color;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 * Para que corra en Netbeans con Windows:
 * ==>Right click on the Project
 * ==>Properties
 * ==>Click on RUN
 * ==>VM Options : -Djava.library.path="C:\Your Directory where Dll is present"
 * ==>Ok
 * También están añadidos los natives de Linux
 * 
 * @author Alba Ríos
 */
public class MapProject {
    public static int ZOOM = 1;
    public static final int ZOOMSPEED = 2;
    public static final int MINZOOM = 1, MAXZOOM = 16;
    
    public TileGrid grid;
    
    /**
     * Constructor que crea el mapa y la interfaz
     * 
     * @author Alba Ríos
     */
    public MapProject(){
        
        BeginSession();
        
        this.grid = new TileGrid();
        
    }
    
    /**
     * Método que inicializa y controla el ciclo de vida de la interfaz
     * 
     * @author Alberto Meana, Alba Ríos
     */
    public void startInterface() {
        
        while(!Display.isCloseRequested()){
            this.grid.Draw();
            Display.update();
            Display.sync(60);
              
            //Interacción con el MOUSE
            int dWheel = Mouse.getDWheel();
            if (dWheel < 0) zoomOut();
            else if (dWheel > 0) zoomIn();
        }
        
        Display.destroy(); //Cerrar la interfaz
        
        System.exit( 0 );
    }
    
    /**
     * Método que aplica la ampliación de zoom
     * 
     * @author Alba Ríos
     */
    public final void zoomIn(){
        if (ZOOM < MAXZOOM){
            ZOOM *= ZOOMSPEED;
            applyZoom(ZOOM);
        }
    }
    
    /**
     * Método que aplica la reducción de zoom
     * 
     * @author Alba Ríos
     */
    public final void zoomOut(){
        if (ZOOM > MINZOOM){
            ZOOM /= ZOOMSPEED;
            applyZoom(ZOOM);
        }
    }
}
