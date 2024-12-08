package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import processing.core.PGraphics;

/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {
	
	public OceanQuakeMarker(PointFeature quake) {
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = false;
	}
	

	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		// Drawing a centered square for Ocean earthquakes
		// DO NOT set the fill color.  That will be set in the EarthquakeMarker
		// class to indicate the depth of the earthquake.
		// Simply draw a centered square.
		
		// HINT: Notice the radius variable in the EarthquakeMarker class
		// and how it is set in the EarthquakeMarker constructor
		
		// TODO: Implement this method
		float magnitude = getMagnitude();
	    
	    // Calculate the size of the square based on magnitude
	    float sideLength = magnitude * 2; // Example: magnitude 4 gives a side length of 8, magnitude 6 gives 12, etc.
	    
	    // Draw the square centered at (x, y)
	    pg.rectMode(PGraphics.CENTER); // Set rectMode to center to ensure the square is centered at (x, y)
	    pg.rect(x, y, sideLength, sideLength);
		
	}
	


	

}
