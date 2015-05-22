/*
 * 
 */
package edu.hm.am.stausimulator;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class Configuration.
 */
public class Configuration {

	private static Map<Property, Number> properties = init();

	public static Number getProperty(Property property) {
		return properties.get(property);
	}

	public static void putProperty(Property property, Number value) {
		properties.put(property, value);
	}

	/**
	 * Init default Configuration.
	 * 
	 * @return Default Configuration.
	 */
	private static Map<Property, Number> init() {
		Map<Property, Number> map = new HashMap<>();

		map.put(Property.INTERVAL, new Integer(1000));

		map.put(Property.ROADS, new Integer(2));
		map.put(Property.LANES, new Integer(2));
		map.put(Property.CELLS, new Integer(20));
		map.put(Property.MAX_SPEED, new Integer(5));

		map.put(Property.DENSITY, new Double(0.2));
		map.put(Property.PROBABILITY, new Double(0.0));

		return map;
	}

}
