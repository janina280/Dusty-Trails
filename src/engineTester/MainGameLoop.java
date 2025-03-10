package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();
        List<Terrain> terrains = new ArrayList<>();
        
        TerrainTexture backgroundTexture  = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTexture           = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture           = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture           = new TerrainTexture(loader.loadTexture("path"));
        TerrainTexture blendMap           = new TerrainTexture(loader.loadTexture("blendMap"));
        TerrainTexturePack texturePack    = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

        
        RawModel model = OBJLoader.loadObjModel("tree", loader);
        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("tree")));
        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
                new ModelTexture(loader.loadTexture("grassTexture")));
        
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
                new ModelTexture(loader.loadTexture("fern")));
        fern.getTexture().setHasTransparency(true);

        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        int worldSize = 5;

        // Crearea entităților
        for (int i = 0; i < 5000; i++) {
            float x = random.nextFloat() * (worldSize * 800) - (worldSize * 400);
            float z = random.nextFloat() * (worldSize * -800) + (worldSize * 400);
            
            entities.add(new Entity(staticModel, new Vector3f(x, 0, z), 0, 0, 0, 3));

            if (random.nextFloat() > 0.8) {
                float fernX = x + random.nextFloat() * 10 - 5;
                float fernZ = z + random.nextFloat() * 10 - 5;
                entities.add(new Entity(fern, new Vector3f(fernX, 0, fernZ), 0, 0, 0, 1));
            }
            if (random.nextFloat() > 0.7) {
                float grassX = x + random.nextFloat() * 10 - 5;
                float grassZ = z + random.nextFloat() * 10 - 5;
                entities.add(new Entity(grass, new Vector3f(grassX, 0, grassZ), 0, 0, 0, 1));
            }
        }

        Light sunlight = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));

        // Crearea terenului pe mai multe secțiuni
        for (int i = -worldSize / 2; i < worldSize / 2; i++) {
            for (int j = -worldSize / 2; j < worldSize / 2; j++) {
                terrains.add(new Terrain(i, j, loader, texturePack, blendMap));
            }
        }

        Camera camera = new Camera();
        MasterRenderer renderer = new MasterRenderer();

        RawModel bunnyModel=OBJLoader.loadObjModel("stanfordBunny", loader);
        TexturedModel stanfordBunny= new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("white")));
        
        Player player=new Player(stanfordBunny, new Vector3f(100, 0, -50), 0, 0, 0, 1);
        
        while (!Display.isCloseRequested()) {
            camera.move();  // Mișcarea camerei
            player.move();
            
            for (Terrain terrain : terrains) {
                renderer.processTerrain(terrain);
            }

            for (Entity entity : entities) {
                renderer.processEntity(entity);
            }
            renderer.processEntity(player);
            renderer.render(sunlight, camera); // Randare
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
