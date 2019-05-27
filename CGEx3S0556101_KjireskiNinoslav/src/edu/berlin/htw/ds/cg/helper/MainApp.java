package edu.berlin.htw.ds.cg.helper;

import static org.lwjgl.opengl.GL11.GL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BITS;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLight;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.util.glu.GLU.gluLookAt;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.nio.FloatBuffer;
import java.util.LinkedList;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class MainApp implements InteractiveItem {
    
    private int width = 1200;
    private int height = 800;
    private int len = 300;
    private int wid = 200;
    private int distanceCam = 2000;
    //private int limitFPS = 60;
    private int counterFrames = 0;
    private int fps = 0;

    private long time = 0;
    private long lastTime = 0;
    private long frameCounterTime = 0;
    private long avgTime = 0;
    private long lastFPS = 0;

    private float dx = 0f;                   // mouse x distance
    private float dy = 0f;                   // mouse y distance
    private float diffTime = 0f;             // frame length
    private float movementSpeed = 800.0f;    // camera movement speed

    private Fork fork;
    private CameraController camera;

    FloatBuffer noAmbient     = GLDrawHelper.directFloatBuffer(new float[] {0.0f, 0.0f, 0.0f, 1.0f});
    FloatBuffer whiteDiffuse  = GLDrawHelper.directFloatBuffer(new float[] {1.0f, 1.0f, 1.0f, 1.0f});
    FloatBuffer positionLight = GLDrawHelper.directFloatBuffer(new float[] {0.0f, 0.0f, 0.0f, 1.0f});

    static LinkedList<Integer> textureIDs = new LinkedList<>();
    
    public static void main(String[] args) {
        new MainApp();
    }

    public MainApp() {
        run();
    }

    private void run() {
        setup();
        while (!exit()) {
            update();
            draw();
            updateFPS();
        }
        finish();
    }
    
    private void draw() {
        // clear screen
        glClearColor(0.3f, 0.3f, 0.3f, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // set camera
        gluLookAt(0,0,distanceCam, 0,-(height/3),0, 0,1,0);

        // position light
        glLight(GL_LIGHT0, GL_POSITION, positionLight);

        // apply camera movement
        camera.lookThrough();

        fork.render();

        Display.update();
    }

    private boolean exit() {
        return Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);
    }

    private void updateFPS() {
        long time = getTime();
        String title;

        if (time - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            title = "FPS: " + fps + "  ||  avg time per frame: " + (avgTime != 0 ? avgTime/1000f : "-/-") + " ms";
            Display.setTitle(title);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;

        // Frame Count over 1000
        if (counterFrames == 1000) {
            avgTime = time - frameCounterTime;
            System.out.println("Time for 1000 frames: " + avgTime + " ms.");
            counterFrames = 0;
            frameCounterTime = time;
        }
        counterFrames++;
    }
    
    private long getTime() {
        return (Sys.getTime() * 1000 / Sys.getTimerResolution());
    }


	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
        // create display
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setTitle("Game");
            Display.create();


        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(45.f, width / (float) height, 10f, 20000.f);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LINE_SMOOTH);

        //Following setups for..
        
        //..Mobile
        //..Main fork
		fork = new Fork(40,70,len,wid,
				// Children
				new Fork(40,70,len,wid,new Fork(),new Fork()),
				new Fork(40,70,len,wid,
						new Fork(40,70,len*0.75f,wid*0.75f,new Fork(), new Fork()), 
						new Fork(40,70,len*0.75f,wid*0.75f,new Fork(), new Fork())));
        
        //..Camera
        camera = new CameraController(0, 0, 0);

        //..Lightning
        glEnable(GL_LIGHTING);
        glLight(GL_LIGHT0, GL_AMBIENT, noAmbient);
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteDiffuse);
        glEnable(GL_LIGHT0);

        //..Timer/FPS Count
        frameCounterTime = getTime();
        lastFPS = getTime();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
        // limit framerate
        //Display.sync(limitFPS);

        // get time
        time = getTime();
        diffTime = (time - lastTime)/1000.0f;
        lastTime = time;

        // Distance that mouse moved
        dx = Mouse.getDX();
        dy = Mouse.getDY();

        // wireframe not working?
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
        	GL11.glPolygonMode( GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            }
        if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
        	GL11.glPolygonMode( GL11.GL_FRONT_AND_BACK, GL11.GL_FILL); 
        	}

        // mouse control
        camera.rotateYaw(dx);
        camera.rotatePitch(dy); 

        // WASD control
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            camera.moveForwrd(movementSpeed * diffTime);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            camera.moveBackwrd(movementSpeed * diffTime);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            camera.moveLeft(movementSpeed * diffTime);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            camera.moveRight(movementSpeed * diffTime);
        }
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	//clean up when finished
	public void finish() {
		// TODO Auto-generated method stub
		
        glDisable(GL_DEPTH_BITS);

        // Delete textures
        textureIDs.stream().forEach(GL11::glDeleteTextures);

        Display.destroy();
		
	}
}
