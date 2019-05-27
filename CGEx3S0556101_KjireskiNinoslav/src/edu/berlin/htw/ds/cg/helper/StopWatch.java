package edu.berlin.htw.ds.cg.helper;

public class StopWatch {

	private static long sysstart = System.nanoTime();
	
	public static float getActTime() {
		long stopTime = System.nanoTime();
		float result = (stopTime - sysstart)  / 1000000.f;
		return  result;
	}
	
	public static void resetActTime() {
		sysstart = System.nanoTime();
	}
	
	long startTime;
	double result = -1.;
	public void start() {startTime = System.nanoTime();}
	
	public void stop() { 
		long stopTime = System.nanoTime();
		result = (stopTime - startTime)  / 1000000.;
	}
	
	public double getTime() {return result;}
	
	public void printTimeMilliseconds(String remark){
		if(result != -1) System.out.println("Time used for "+remark+": " + result + " uSec");
	}
}
