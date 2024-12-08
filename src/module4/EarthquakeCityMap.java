package module4;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {
	
	// We will use member variables, instead of local variables, to store the data
	// that the setUp and draw methods will need to access (as well as other methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of earthquakes
	// per country.
	
	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = false;
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	

	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;
	
	public void setup() {		
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
		}
		else {
			// map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
			map = new UnfoldingMap(this, 200, 50, 650, 600, new OpenStreetMap.OpenStreetMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		    //earthquakesURL = "2.5_week.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// FOR TESTING: Set earthquakesURL to be one of the testing files by uncommenting
		// one of the lines below.  This will work whether you are online or offline
		//earthquakesURL = "test1.atom";
		//earthquakesURL = "test2.atom";
		
		// WHEN TAKING THIS QUIZ: Uncomment the next line
		earthquakesURL = "quiz1.atom";
		
		
		// (2) Reading in earthquake data and geometric properties
	    //     STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		//     STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		//     STEP 3: read in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes) {
		  //check if LandQuake
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  // OceanQuakes
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    // could be used for debugging
	    printQuakes();
	 		
	    // (3) Add markers to map
	    //     NOTE: Country markers are not added to the map.  They are used
	    //           for their geometric properties
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	    
	}  // End setup
	
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
		
	}
	
	// helper method to draw key in GUI
	// TODO: Update this method as appropriate
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		// Set background for the key
	    fill(255, 250, 240);
	    rect(25, 50, 150, 250);
	    
	    // Title of the key
	    fill(0);
	    textAlign(LEFT, CENTER);
	    textSize(12);
	    text("Earthquake Key", 50, 75);
	    
	    // City Marker
	    fill(139, 69, 19);
	    triangle(45, 100, 50, 90, 55, 100); // Circle shape for Land Earthquake
	    fill(0, 0, 0); // Black text
	    text("City Marker", 60, 95);
	        
	    //Land Earthquake (Circle) for 5.0+ Magnitude
	    fill(255, 255, 255);
	    ellipse(50,120 , 15, 15);
	    fill(0, 0, 0); // Black text
	    text("Land quake", 65, 118);
	    
	    // Ocean Earthquake (Square) for 5.0+ Magnitude
	    fill(255, 255, 255);
	    rect(43, 133, 15, 15); // Square shape for Ocean Earthquake
	    fill(0, 0, 0); // Black text
	    text("Ocean quake", 65, 140);
	    
	    fill(0, 0, 0); // Black text
	    text("Size  ~  Magnitude", 41, 160);

		// Color-coding for earthquake depth
		fill(255, 255, 0);  // Yellow for shallow
		ellipse(50, 180, 15, 15);  // Shallow earthquake color
		fill(0, 0, 0);  // Black text for label
		text("Shallow", 65, 180);  // Shallow label
	
		fill(0, 0, 255);  // Blue for intermediate
		ellipse(50, 200, 15, 15);  // Intermediate earthquake color
		fill(0, 0, 0);  // Black text for label
		text("Intermediate", 65, 200);  // Intermediate label
	
		fill(255, 0, 0);  // Red for deep
		ellipse(50, 220, 15, 15);  // Deep earthquake color
		fill(0, 0, 0);  // Black text for label
		text("Deep", 65, 220);  // Deep label
		
		// Add an "X" over a white circle for "Past Day" Earthquakes
	    fill(255);  // White color for the circle
	    ellipse(50, 240, 15, 15);  // Draw the white circle for "Past Day"
	    fill(0);  // Black color for the "X"
	    line(42, 232, 58, 248);  // Draw the first diagonal of "X"
	    line(42, 248, 58, 232);  // Draw the second diagonal of "X"
	    fill(0);  // Black text
	    text("Past Day", 65, 240);  // Label for "Past Day"
	}

	
	
	// Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.  Notice that the helper method isInCountry will
	// set this "country" property already.  Otherwise it returns false.
	private boolean isLand(PointFeature earthquake) {
		
		
		// Loop over all the country markers.  
		// For each, check if the earthquake PointFeature is in the 
		// country in m.  Notice that isInCountry takes a PointFeature
		// and a Marker as input.  
		// If isInCountry ever returns true, isLand should return true.
		for (Marker m : countryMarkers) {
			// TODO: Finish this method using the helper method isInCountry
			if (isInCountry(earthquake, m)) 
			{
				return true;	
			}		
		}
		
		
		// not inside any country
		return false;
	}
	
	/* prints countries with number of earthquakes as
	 * Country1: numQuakes1
	 * Country2: numQuakes2
	 * ...
	 * OCEAN QUAKES: numOceanQuakes
	 * */
	private void printQuakes() 
	{
		// TODO: Implement this method
		// One (inefficient but correct) approach is to:
		//   Loop over all of the countries, e.g. using 
		//        for (Marker cm : countryMarkers) { ... }
		//        
		//      Inside the loop, first initialize a quake counter.
		//      Then loop through all of the earthquake
		//      markers and check to see whether (1) that marker is on land
		//     	and (2) if it is on land, that its country property matches 
		//      the name property of the country marker.   If so, increment
		//      the country's counter.
		
		// Here is some code you will find useful:
		// 
		//  * To get the name of a country from a country marker in variable cm, use:
		//     String name = (String)cm.getProperty("name");
		//  * If you have a reference to a Marker m, but you know the underlying object
		//    is an EarthquakeMarker, you can cast it:
		//       EarthquakeMarker em = (EarthquakeMarker)m;
		//    Then em can access the methods of the EarthquakeMarker class 
		//       (e.g. isOnLand)
		//  * If you know your Marker, m, is a LandQuakeMarker, then it has a "country" 
		//      property set.  You can get the country with:
		//        String country = (String)m.getProperty("country");
		for (Marker cm : countryMarkers) {
			int count = 0;
			String country = (String) cm.getProperty("name");
	
			// Loop through all earthquake markers
			for (Marker em : quakeMarkers) {
				// Check if the earthquake is on land
				if (em instanceof EarthquakeMarker && ((EarthquakeMarker) em).isOnLand()) {
					// Check if the earthquake occurred in the current country
					if (country.equals(em.getProperty("country"))) {
						count++;
					}
				}
			}
			if(count !=0)
			{
				// Print the number of earthquakes for the current country
				System.out.printf("Number of earthquakes in %s = %d%n", country, count);
			}
		}
	
		// Count ocean quakes (earthquakes not on land)
		int oceanQuakes = 0;
		for (Marker em : quakeMarkers) {
			if (em instanceof EarthquakeMarker && !((EarthquakeMarker) em).isOnLand()) {
				oceanQuakes++;
			}
		}
	
		System.out.printf("Number of ocean quakes = %d%n", oceanQuakes);

	}
	
	
	
	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake 
	// feature if it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				// checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					// return if is inside one
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}

}
