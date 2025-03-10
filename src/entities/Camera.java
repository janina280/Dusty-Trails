package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position = new Vector3f(0, 10, 50);
    private float pitch = 10; 
    private float yaw = 180;    
    private float roll;       

    public Camera() {}

    public void move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.x -= (float) Math.sin(Math.toRadians(yaw)) * 0.2f;  
            position.z += (float) Math.cos(Math.toRadians(yaw)) * 0.2f; 
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.x += (float) Math.sin(Math.toRadians(yaw)) * 0.2f;
            position.z -= (float) Math.cos(Math.toRadians(yaw)) * 0.2f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x += 0.2f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x -= 0.2f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            position.y += 0.2f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            position.y -= 0.2f;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
