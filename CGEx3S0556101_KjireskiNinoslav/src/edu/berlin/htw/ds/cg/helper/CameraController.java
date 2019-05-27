package edu.berlin.htw.ds.cg.helper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class CameraController {

        private Vector3f CameraPosition = null;

        private float yaw  = 0.0f; // Z rotation
        private float pitch  = 0.0f; // X rotation

        public CameraController(float x, float y, float z) {
                CameraPosition = new Vector3f(x, y, z);
        }

        //Mouse
        //increment Z rotation
        public void rotateYaw(float x) {
                yaw += x;
        }

        //increment X rotation
        public void rotatePitch(float y) {
                pitch -= y;
        }

        //WASD
        //forward movement
        public void moveForwrd(float distance) {
                CameraPosition.x -= distance * (float) Math.sin(Math.toRadians(yaw));
                CameraPosition.z -= distance * (float) Math.cos(Math.toRadians(yaw));
        }

        //backward movement
        public void moveBackwrd(float distance) {
                CameraPosition.x += distance * (float) Math.sin(Math.toRadians(yaw));
                CameraPosition.z += distance * (float) Math.cos(Math.toRadians(yaw));
        }

        //left movement
        public void moveLeft(float distance) {
                CameraPosition.x -= distance * (float) Math.sin(Math.toRadians(pitch + 88));
                CameraPosition.z += distance * (float) Math.cos(Math.toRadians(pitch + 88));
        }

        //right movement
        public void moveRight(float distance) {
                CameraPosition.x += distance * (float) Math.sin(Math.toRadians(pitch + 88));
                CameraPosition.z -= distance * (float) Math.cos(Math.toRadians(pitch + 88));
        }

        /// translates and rotate the matrix so that it looks through the camera
        public void lookThrough() //was glueLookAt()
        {
                //rotate around X
                GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);

                //rotate around Z
                GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);

                //translate to the position vector's location
                GL11.glTranslatef(CameraPosition.x, CameraPosition.y, CameraPosition.z);
        }
}
