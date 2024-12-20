# Earthquake Map

This project is a learning exercise developed as part of the UC San Diego/Coursera MOOC on Object Oriented Programming in Java. The project uses the Unfolding Maps library to visualize earthquake data on a map.

## Project Structure

- `src/`: Contains the source code for the project.
  - `EarthquakeMarker.java`: Handles the representation of earthquake markers on the map.
  - `LandQuakeMarker.java`: Extends `EarthquakeMarker` to represent earthquakes that occur on land.
  - `OceanQuakeMarker.java`: Extends `EarthquakeMarker` to represent earthquakes that occur in the ocean.
  - `EarthquakeCityMap.java`: The main class that sets up the map and integrates all components.
  - `CityMarker.java`: Represents cities on the map.
  - `CountryMarker.java`: Represents countries on the map.
  - `Marker.java`: A base class for all markers on the map.
  - `CommonMarker.java`: An abstract class that extends `Marker` and is extended by `CityMarker`, `CountryMarker`, and `EarthquakeMarker`.

## Features

- Visualizes earthquake data on an interactive map.
- Custom markers to represent different magnitudes of earthquakes.
- Ability to compare earthquake magnitudes.
- Differentiates between land and ocean earthquakes.
- Displays cities and countries on the map.

## Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/MohamdOmar/Earthquake_Map.git