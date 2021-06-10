package titan.gui;

public class Textures {
    /**
     * Accesses the urls containing the textures of the planets for the GUI
     *
     * @return Array containing texture urls in order: sun, mercury, venus, earth, moon, mars, jupiter, saturn, titan, uranus, neptune
     */
    public static String[] getTextureUrls(){
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