package com.alpha.CustomerService.Service;

import java.util.List;

import java.util.Map;
import org.springframework.data.geo.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.geo.GeoResults;

@Service
public class RedisService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	public void saveRideDetails(long custId, double sourceLongitude, double sourceLatitude, double destinationLongitude,
			double destinationLatitude, String pickupLocation, String destinationLocation, double distance,
			double duration, double bikePrice, double autoPrice, double cabPrice) {

		String key = "custId_" + custId + ":details";

		// HASH
		redisTemplate.opsForHash().put(key, "pickupLocation", pickupLocation);
		redisTemplate.opsForHash().put(key, "destinationLocation", destinationLocation);
		redisTemplate.opsForHash().put(key, "distance", String.valueOf(distance));
		redisTemplate.opsForHash().put(key, "duration", String.valueOf(duration));
		redisTemplate.opsForHash().put(key, "bikePrice", String.valueOf(bikePrice));
		redisTemplate.opsForHash().put(key, "autoPrice", String.valueOf(autoPrice));
		redisTemplate.opsForHash().put(key, "cabPrice", String.valueOf(cabPrice));

		// GEO
		String locationKey = "custId_" + custId + ":locations";
		redisTemplate.opsForGeo().add(locationKey, new Point(sourceLongitude, sourceLatitude), "source");

		redisTemplate.opsForGeo().add(locationKey, new Point(destinationLongitude, destinationLatitude), "destination");
	}

//  Getting the riderDetails Not locations, From the  saveRideDetails(Redis)
	public Map<Object, Object> getRideDetails(long custId) {
		String key = "custId_" + custId + ":details";
		return redisTemplate.opsForHash().entries(key);
	}

//  Getting the riderLocation Not Details, From the saveRideDetails(Redis)	
	public Map<Object, Object> getRideLocations(long custId) {
		String key = "custId_" + custId + ":details";
		return redisTemplate.opsForHash().entries(key);
	}

//	Remaining vehicle fare is removed to identify this customer is booked for bike or auto or cab
	public void removeOtherFares(long custId, String vehicle) {

	    String key = "custId_" + custId + ":details";

	    if (vehicle.equalsIgnoreCase("BIKE")) {

	        redisTemplate.opsForHash().delete(key, "autoPrice", "cabPrice");

	    } else if (vehicle.equalsIgnoreCase("AUTO")) {

	        redisTemplate.opsForHash().delete(key, "bikePrice", "cabPrice");

	    } else if (vehicle.equalsIgnoreCase("CAB")) {

	        redisTemplate.opsForHash().delete(key, "bikePrice", "autoPrice");
	    }
	}


	public List<String> findNearbyRiders(long custId, String vehicleType, double radiusKm) {
		String customerKey = "custId_" + custId + ":locations";
		String riderKey = "vehicle:" + vehicleType.trim().toUpperCase() + ":locations";
		System.out.println("Customer GEO Key = " + customerKey);
		System.out.println("Rider GEO Key = " + riderKey);
		List<Point> points = redisTemplate.opsForGeo().position(customerKey, "source");
		System.out.println("Customer Source = " + points);
		if (points == null || points.isEmpty() || points.get(0) == null) {
			System.out.println("Customer source location NOT FOUND");
			return List.of();
		}

		Point customerLocation = points.get(0);

		System.out.println("Customer Longitude = " + customerLocation.getX());
		System.out.println("Customer Latitude = " + customerLocation.getY());

		Circle circle = new Circle(customerLocation, new Distance(radiusKm, Metrics.KILOMETERS));

		GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo().radius(riderKey, circle);
		System.out.println("Nearby Riders = " + results);
		return results.getContent().stream().map(result -> result.getContent().getName()).toList();
	}

	public void saveRideRequest(String rider, int bookingId, long custId, double distance, double duration, String otp, String vehicle) {

	    String rideKey = "AssignedRide_for_" + rider + ":" + bookingId;

	    redisTemplate.opsForHash().put(rideKey, "bookingId", String.valueOf(bookingId));
	    redisTemplate.opsForHash().put(rideKey, "customerId", String.valueOf(custId));
	    redisTemplate.opsForHash().put(rideKey, "distance", String.valueOf(distance));
	    redisTemplate.opsForHash().put(rideKey, "duration", String.valueOf(duration));
	    redisTemplate.opsForHash().put(rideKey, "otp", otp);
	    redisTemplate.opsForHash().put(rideKey, "vehicle", vehicle);
	}
	public Point getCustomerLocation(int custId, String string) {
		Point customerLocation = getCustomerLocation(custId, "customers:locations");
		return customerLocation;
	}


}
