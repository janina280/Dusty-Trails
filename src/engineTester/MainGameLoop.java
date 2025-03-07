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
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();

        RawModel model = OBJLoader.loadObjModel("tree", loader);
        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("tree")));
        TexturedModel grass=new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
        		new ModelTexture(loader.loadTexture("grassTexture")));
        
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        TexturedModel fern=new TexturedModel(OBJLoader.loadObjModel("fern", loader),
        		new ModelTexture(loader.loadTexture("fern")));
        fern.getTexture().setHasTransparency(true);
        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        int worldSize = 5; 
        
        for (int i = 0; i < 50000; i++) {
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

        Light sunlight = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1,1,1)); 

        List<Terrain> terrains = new ArrayList<>();
        for (int i = -worldSize / 2; i < worldSize / 2; i++) {
            for (int j = -worldSize / 2; j < worldSize / 2; j++) {
                terrains.add(new Terrain(i, j, loader, new ModelTexture(loader.loadTexture("grass"))));
            }
        }

        Camera camera = new Camera();
        MasterRenderer renderer = new MasterRenderer();

        while (!Display.isCloseRequested()) {
            camera.move();
            for (Terrain terrain : terrains) {
                renderer.processTerrain(terrain);
            }

            for (Entity entity : entities) {
                renderer.processEntity(entity);
            }
            renderer.render(sunlight, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}

