package edu.hm.am.stausimulator.factory;

import java.util.ArrayList;
import java.util.List;

import edu.hm.am.stausimulator.model.Car;
import edu.hm.am.stausimulator.model.Vehicle;

public class VehicleFactory {
	public static List<Vehicle> createVehicles(int count, int maxspeed) {
		List<Vehicle> vehicles = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			vehicles.add(new Car(maxspeed));
		}
		return vehicles;
	}
}
