package edu.hm.am.stausimulator;

import java.util.ArrayList;
import java.util.List;

import edu.hm.am.stausimulator.model.Car;
import edu.hm.am.stausimulator.model.Vehicle;

public class VehiclePool {

	private static final int DEFAULT_CAPACITY = 30;

	private static List<Vehicle> pool = init();

	private static List<Vehicle> init() {
		List<Vehicle> pool = new ArrayList<Vehicle>();
		for (int i = 0; i < DEFAULT_CAPACITY; i++) {
			pool.add(new Car());
		}
		return pool;
	}

	public static void setPool(List<Vehicle> p) {
		pool = p;
	}

	public static Vehicle get() {
		if (pool.size() == 0) {
			pool = init();
		}
		return pool.remove(pool.size() - 1);
	}

	public static List<Vehicle> get(int amount) {
		List<Vehicle> v = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			v.add(get());
		}
		return v;
	}

	public static boolean put(Vehicle vehicle) {
		return pool.add(vehicle);
	}
}
