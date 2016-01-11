package Sonido;
/*
import java.io.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author Nikolai Giovanni González López
 * @description Clase Sonido que reproduce MP3 en una hebra por debajo del programa principal
 */
/*
public class Sonido {
    private String ruta;
    private Player player;
    
    public Sonido(String file)
    {
        ruta = file;
    }
    
    public void reproducirMP3() 
    {
        try
        {
            InputStream fis = this.getClass().getResourceAsStream(ruta);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        }
        catch (JavaLayerException e) 
        {
            e.printStackTrace();
        } 
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    player.play();
                }
                catch(JavaLayerException e){}
            }
        }.start();
    }
    
    public Player getPlayer()
    {
        return player;
    }
}

*/