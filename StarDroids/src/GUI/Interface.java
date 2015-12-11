package GUI;

import java.awt.Color;
import javax.swing.JFrame;

/**
 * Clase contenedor para el grid de diseño para mejor abstracción.
 * Directamente relacionada con Frame.java
 * V 1.0
 * ---- || DEPRECATED || ----
 * 
 * @see Frame.java
 * @author Alberto Meana, Alba Ríos
 */
public class Interface extends JFrame {
    
    private Frame modelMap;
    
    /**
     * 
     * Constructor por defecto de la interfaz.
     * Inicializa el grid interior y la interfaz.
     * 
     * @author Alberto Meana
     * @param x Numero de columnas del grid
     * @param y Numero de filas del grid
     */
    public Interface( int x, int y ){
    
        super( "Interfaz" );
        
        this.modelMap = new Frame( x, y );
        this.add( modelMap );
        this.setExtendedState( JFrame.MAXIMIZED_BOTH );
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        
        this.setVisible(true);
        
        this.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                
                wheelAction( evt );
                
            }
        });
    }
    
    /**
     * Método que colorea una casilla del grid interior
     * 
     * @param x Coordenada x del grid
     * @param y Coordenada y del grid
     * @param c Color al que pintar la casilla
     * @author Alberto Meana
     */
    public void paint( int x, int y, Color c ){
    
        this.modelMap.specificPaint( x, y, c);
    
    }
    
    /**
     * Función que implementa el zoom con la rueda del ratón.
     * 
     * @param evt Movimiento detectado de la rueda del ratón
     * @author Alberto Meana
     */
    public void wheelAction( java.awt.event.MouseWheelEvent evt ){
        
        if( evt.getWheelRotation() < 0 && this.modelMap.getWidth() < 7366 ){
           
            this.modelMap.setSize( this.modelMap.getWidth() + 200, this.modelMap.getHeight() + 200 );
        
        }else if( this.modelMap.getWidth() >= 1200 ){
        
            this.modelMap.setSize( this.modelMap.getWidth() - 200, this.modelMap.getHeight() - 200);
            
        }
    }
}
