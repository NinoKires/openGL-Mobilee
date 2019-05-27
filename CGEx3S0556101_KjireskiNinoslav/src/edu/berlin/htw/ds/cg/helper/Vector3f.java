package edu.berlin.htw.ds.cg.helper;

import java.io.Serializable;

/* Copyright D.Strippgen 2010
 * 
 */
public class Vector3f implements Serializable{
	public static final Vector3f UNIT_X = new Vector3f(1,0,0);
	public static final Vector3f UNIT_Y = new Vector3f(0,1,0);
	public static final Vector3f UNIT_Z = new Vector3f(0,0,1);

	public float x,y,z;
	
	public Vector3f(double x, double y, double z){
		this.x = (float)x;
		this.y = (float)y;
		this.z = (float)z	;
	}

	public Vector3f() {
		x=y=z=0;
	}

	public void set(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void get (float[] dest){
		dest[0] = x;
		dest[1] = y;
		dest[2] = z;
	}

	public void addVector(Vector3f b) {
		this.x += b.getX(); this.y += b.getY(); this.z += b.getZ();
	}
	public void subtractVector(Vector3f b) {
		this.x -= b.getX(); this.y -= b.getY(); this.z -= b.getZ();
	}

	public float length() {
		return (float)Math.sqrt(this.x*this.x +this.y*this.y +this.z*this.z);
	}

	public void mult(float s){
		x*=s;y*=s;z*=s;
	}
	
	public Vector3f cross(Vector3f b){
		Vector3f a = this;
		Vector3f erg = new Vector3f(a.getY()*b.getZ() - a.getZ()*b.getY(),
				a.getZ()*b.getX() - a.getX()*b.getZ(),
				a.getX()*b.getY() - a.getY()*b.getX());
		return erg;
	}

	public float dot(Vector3f other){
		double dd = 0;
		dd = this.length() * other.length();
		if(dd <= 0.0001f) return 0.0f;
		dd = ((this.x*other.getX() + this.y*other.getY() + this.z*other.getZ())/dd);
		if(dd> 0.9999) return 1.0f;
		return (float)dd;
	}

	public void setLength(float f) {
		double factor = f / this.length();
		x *= factor;
		y *= factor;
		z *= factor;
	}

	public Vector3f clone() {
		return new Vector3f(x,y,z);
	}
	
	public String toString() {
		return "( " + x + ", " + y + ", "+z+")";
	}

	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getZ() {
		return z;
	}
	
	public void set3f(float x, float y ,float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void set3f(float[] arr, int offset){
		this.x = arr[offset + 0 ];
		this.y = arr[offset + 1 ];
		this.z = arr[offset + 2 ];
	}
	public void get3f(float[] arr, int offset){
		arr[offset + 0 ] = this.x;
		arr[offset + 1 ] = this.y;
		arr[offset + 2 ] = this.z;
	}
}
