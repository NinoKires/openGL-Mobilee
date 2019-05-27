package edu.berlin.htw.ds.cg.helper;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import java.io.IOException;
import org.lwjgl.input.Keyboard;

public class Fork {
	
    private boolean sphere = false;
    
    private int size = 50;
    private int textureID = 0;
    
    private float pitch;
    private float pitchPos = 0;
    private float pitchDir = 1;
    private float maxPitchAngle = 30;
    private float yaw;
    private float yawPos = 0;
    private float speed = .003f;
    private float height;
    private float width;
    
    private Fork leftChild;
    private Fork rightChild;
    
    private final String texturePath = GLDrawHelper.getRandomTexturePath("../CGSS15Ex3MobileDS/dataEx3/Textures");
    
    public Fork(float pitch, float yaw, float height, float width, Fork leftChild, Fork rightChild) {
        this.pitch  = pitch;
        this.yaw    = yaw;
        this.height = height;
        this.width  = width;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }
    public Fork() {
        sphere = true;
        
		textureID = glGenTextures();
	
		TextureReader.Texture texture = null; 
		try { 
			texture = TextureReader.readTexture(texturePath,true); 
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} 
		
		glBindTexture(GL_TEXTURE_2D, textureID);
		
		glTexImage2D(
			GL_TEXTURE_2D, 0, GL_RGBA, texture.getWidth(), texture.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, texture.getPixels()
				);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		sphere = true;	 
	}
    
    public void render() {
        glPushMatrix();
        glColor3f(1.0f, 1.0f, 1.0f);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureID);
        
        //alternate tilt (up and down)
        if (pitchPos > maxPitchAngle) {
        	pitchDir = -1;
        }
        else if (pitchPos < -maxPitchAngle) {
        	pitchDir = 1;
        }
        
        //tilt pitch
        pitchPos += pitch * pitchDir * speed * 0.5;
        
        //tilt yaw
        yawPos += yaw * speed;

        if(sphere) {   	
        	
            GLDrawHelper.drawSphere(size, 10, 10);
    		glEnable(GL_TEXTURE_2D);
    		glBindTexture(GL_TEXTURE_2D, textureID); 
    		glTexCoord2f(0.5f, 1.0f);
    		glBindTexture(GL_TEXTURE_2D, 0); 
    		glDisable(GL_TEXTURE_2D);
    		
        } else {
        	
            glLineWidth(2f);

            // vertical line
            glBegin(GL_LINES);
            glVertex2f(0, 0);
            glVertex2f(0, -height);
            glEnd();

            glTranslatef(0, -height, 0);
            //rotate around y
            glRotatef(yawPos, 0, 1, 0);
            //switch rotation direction not working
            if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            	glRotatef(-yawPos, 0, 1, 0);
            	}

            // horizontal line
            glBegin(GL_LINES);
            glVertex2f(-width, pitchPos);
            glVertex2f(width, -pitchPos);
            glEnd();

        }

        glTranslatef(width, -pitchPos, 0);
        if (leftChild  != null) {
        	leftChild.render();
        }
        glTranslatef(-2*width, 2*pitchPos,0);
        if (rightChild != null) {
        	rightChild.render();
        }

        glPopMatrix();

    }
}

