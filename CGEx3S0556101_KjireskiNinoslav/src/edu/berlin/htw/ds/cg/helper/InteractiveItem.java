package edu.berlin.htw.ds.cg.helper;

public interface InteractiveItem {

	//This will setup the entity or world, i.e. initialize settings, 
	// create a display, prepare OpenGL
	public abstract void setup();

	// this will update all internal calculations, 
	// get keyboard state, do physics
	public abstract void update();

	//this will render the actual entity/world 
	public abstract void render();

	//delete resources, etc. ... maybe do nothing
	public abstract void finish();

}