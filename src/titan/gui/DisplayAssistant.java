package titan.gui;
/* Titan import statements here */

/* Java and JavaFX statements here */
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class DisplayAssistant {
    public static Double[] displayRadius(){
        double scale = 1.0E-3;
        Double[] r = new Double[11];
        r[0]    = 696340    * scale;
        r[1]    = 24397     * scale;
        r[2]    = 60518     * scale;
        r[3]    = 6371.0    * scale;
        r[4]    = 1737.1    * scale;
        r[5]    = 3389.5    * scale;
        r[6]    = 69911     * scale;
        r[7]    = 58232     * scale;
        r[8]    = 25747     * scale;
        r[9]    = 25362     * scale;
        r[10]   = 24622     * scale;
        return r;
    }
    public static String[] getUrls(){
        String[] urls = new String[11];
        urls[0] = "https://www.solarsystemscope.com/textures/download/2k_sun.jpg";
        urls[1] = "https://www.solarsystemscope.com/textures/download/2k_mercury.jpg";
        urls[2] = "https://www.solarsystemscope.com/textures/download/2k_venus_surface.jpg";
        urls[3] = "https://www.solarsystemscope.com/textures/download/2k_earth_daymap.jpg";
        urls[4] = "https://www.solarsystemscope.com/textures/download/2k_moon.jpg";
        urls[5] = "https://www.solarsystemscope.com/textures/download/2k_mars.jpg";
        urls[6] = "https://www.solarsystemscope.com/textures/download/2k_jupiter.jpg";
        urls[7] = "https://www.solarsystemscope.com/textures/download/2k_saturn.jpg";
        urls[8] = "http://www.planetaryvisions.com/images_new/221.jpg";
        urls[9] = "https://www.solarsystemscope.com/textures/download/2k_uranus.jpg";
        urls[10] = "https://www.solarsystemscope.com/textures/download/2k_neptune.jpg";
        return urls;
    }
}