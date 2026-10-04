package com.alpha.CustomerService.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alpha.CustomerService.Dto.CustomerDto;
import com.alpha.CustomerService.Dto.FairPriceAllVehicles;
import com.alpha.CustomerService.Dto.Fairprice;
import com.alpha.CustomerService.Dto.ResponceStructure;
import com.alpha.CustomerService.Dto.RidefairDTO;
import com.alpha.CustomerService.Dto.SearchDestinationResponeDto;
import com.alpha.CustomerService.Dto.SelectRideDTO;
import com.alpha.CustomerService.Entity.Booking;
import com.alpha.CustomerService.Entity.Cordinate;
import com.alpha.CustomerService.Entity.Customer;
import com.alpha.CustomerService.Exception.CustomerNotExist;
import com.alpha.CustomerService.Exception.RIdeDetailNotFoundInRedis;
import com.alpha.CustomerService.Repository.BookingRepository;
import com.alpha.CustomerService.Repository.Cardinationrepository;
import com.alpha.CustomerService.Repository.CustomerRepositor;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepositor customerRepositor;
	@Autowired
	private Cardinationrepository cardinationrepository;
	@Autowired
	private RedisService redisserver;
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private BookingRepository bookingRepository;

	public ResponceStructure<Customer> CreateCustomer(CustomerDto custDto) {
		Customer c = new Customer();
		c.setName(custDto.getName());
		c.setMobile(custDto.getMobile());
		c.setEmail(custDto.getEmail());
		c.setGender(custDto.getGender());
		Customer cust = customerRepositor.save(c);

		int custId = cust.getId();

		if (custId >= 1 && custId <= 9999) {
			String otp = String.format("%04d", custId);
			System.out.println(otp);
			c.setOtp(otp);
		} else if (custId >= 10000) {
			System.out.println(custId % 10000);
			c.setOtp(custId % 10000 + "");
		}
		customerRepositor.save(c);
		ResponceStructure<Customer> cs = new ResponceStructure<Customer>();
		cs.setStatusCode(HttpStatus.CREATED.value());
		cs.setMessage("Customer is created");
		cs.setData(cust);
		return cs;
	}

	public ResponceStructure<String> DeleteCustomer(int custid) {
		Customer cust = customerRepositor.findById(custid).orElseThrow(() -> new CustomerNotExist());
		customerRepositor.deleteById(custid);

		ResponceStructure<String> rs = new ResponceStructure<String>();
		rs.setStatusCode(HttpStatus.OK.value());
		rs.setMessage("Delete Succesfully");
		rs.setData("Deleted record");
		return rs;
	}

	//returen responce structure
	public ResponceStructure<Customer> findCustomer(int id) {
		Customer cust = customerRepositor.findById(id).orElseThrow(() -> new CustomerNotExist());
		ResponceStructure<Customer> c = new ResponceStructure<Customer>();
		c.setStatusCode(HttpStatus.FOUND.value());
		c.setMessage("Customer is Found");
		c.setData(cust);
		return c;
	}

	@Autowired
	private RestTemplate restTemplate;

	public ResponceStructure<List<SearchDestinationResponeDto>> searchdroplocation(String searchkey) {
		// TODO Auto-generated method stub
		String url = "https://us1.locationiq.com/v1/search?key=pk.ee69342003ac6bc7ebb859fb52baf933&q=" + searchkey
				+ "&format=json&";
		ArrayList<Object> list = restTemplate.getForObject(url, ArrayList.class);
//		Map<String, Object> map = (Map<String, Object>)list;
		List<SearchDestinationResponeDto> searchDestinationResponeDtos = new ArrayList<SearchDestinationResponeDto>();
		for (Object searchDestinationResponeDto : list) {
			Map<String, Object> map = (Map<String, Object>) searchDestinationResponeDto;
			SearchDestinationResponeDto searchDestinationResponeDto2 = new SearchDestinationResponeDto();
			searchDestinationResponeDto2.setAdderss((String) map.get("display_name"));
			searchDestinationResponeDto2.setLatitude((Double.parseDouble((String) map.get("lon"))));
			searchDestinationResponeDto2.setLongitude(Double.parseDouble((String) map.get("lat")));
			searchDestinationResponeDtos.add(searchDestinationResponeDto2);
		}
		ResponceStructure<List<SearchDestinationResponeDto>> response = new ResponceStructure<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("The matching addresses are:");
		response.setData(searchDestinationResponeDtos);

		return response;
	}
	public ResponceStructure<FairPriceAllVehicles> selectRide(SelectRideDTO selectRideDTO) {
	//To Get Duration And Distance from the api		
		Customer c = customerRepositor.findById(selectRideDTO.getCustid()).orElseThrow(()-> new CustomerNotExist());
		String url = "https://us1.locationiq.com/v1/directions/driving/"
				+ selectRideDTO.getSourcelocation().getLongitude() + ","
				+ selectRideDTO.getSourcelocation().getLatitude() + ";"
				+ selectRideDTO.getDestinationlocation().getLongitude() + ","
				+ selectRideDTO.getDestinationlocation().getLatitude()
				+ "?key=pk.ee69342003ac6bc7ebb859fb52baf933&steps=true&alternatives=true&geometries=polyline&overview=full&";
		Map<String, Object> response = restTemplate.getForObject(url, Map.class);
		List<Map<String, Object>> routes = (List<Map<String, Object>>) response.get("routes");
		
		
	//if route is found get first route from list and adding into the FairPriceAllVehicles
		if (routes != null && !routes.isEmpty()) {
			Map<String, Object> firstRoute = routes.get(0);
			int custId = selectRideDTO.getCustid();
			double distance =(((Number) firstRoute.get("distance")).doubleValue())/1000;
			double duration=(((Number) firstRoute.get("duration")).doubleValue())/60;	
			double bikePrice = calculateFare(Fairprice.BIKE, distance, duration);
	        double autoPrice = calculateFare(Fairprice.AUTO, distance, duration);
	        double cabPrice = calculateFare(Fairprice.CAB, distance, duration);
	        List<Double> coordinates = Arrays.asList(
	        	    selectRideDTO.getSourcelocation().getLatitude(),
	        	    selectRideDTO.getSourcelocation().getLongitude(),
	        	    selectRideDTO.getDestinationlocation().getLatitude(),
	        	    selectRideDTO.getDestinationlocation().getLongitude()
	        	);	        
	        
	  // To get the location name And detination location name   
	        String pickupUrl ="https://us1.locationiq.com/v1/reverse?key=pk.ee69342003ac6bc7ebb859fb52baf933&lat="+selectRideDTO.getSourcelocation().getLatitude()+
	        		"&lon="+selectRideDTO.getSourcelocation().getLongitude()+"&format=json&";
	        String DestinationUrl ="https://us1.locationiq.com/v1/reverse?key=pk.ee69342003ac6bc7ebb859fb52baf933&lat="+
	        		selectRideDTO.getDestinationlocation().getLatitude()+
	        		"&lon="+selectRideDTO.getDestinationlocation().getLongitude()+"&format=json&";
	        Map<String, Object> pickupname = restTemplate.getForObject(pickupUrl, Map.class);
	        Map<String, Object>destinationname = restTemplate.getForObject(DestinationUrl, Map.class);
	        System.out.println(pickupname);
	        System.out.println(destinationname);
	        System.out.println(pickupUrl);
	        System.out.println(DestinationUrl);
	        Map<String, Object> address1 = (Map<String, Object>) pickupname.get("address");
	        String pickupLocation = (String) address1.get("suburb");
	        Map<String, Object> address2 = (Map<String, Object>) destinationname.get("address");
	        String destinationLocation = (String) address2.get("suburb");
	        
	    // Adding Details into the redis
	        redisserver.saveRideDetails(custId,selectRideDTO.getSourcelocation().getLongitude(),selectRideDTO.getSourcelocation().getLatitude(), selectRideDTO.getDestinationlocation().getLongitude(),selectRideDTO.getDestinationlocation().getLatitude(),pickupLocation,  destinationLocation,distance,  duration,  bikePrice, autoPrice,cabPrice);
	        	 
	    // Giving the responce Structure 
	        ResponceStructure<FairPriceAllVehicles> rs = new ResponceStructure<FairPriceAllVehicles>();
	        FairPriceAllVehicles fp = new FairPriceAllVehicles();
	        fp.setDistance(distance);
	        fp.setDuration(duration);
	        fp.setCordinates(coordinates);
	        fp.setBikePrice(bikePrice);
	        fp.setAutoPrice(autoPrice);
	        fp.setCarPrice(cabPrice);
	        fp.setPickupLocation(pickupLocation);
	        fp.setDestinationLocation(destinationLocation);
	        
	        rs.setStatusCode(HttpStatus.FOUND.value());
	        rs.setMessage("Select the Vehicle According to you fare");
	        rs.setData(fp);
	        return rs;
			}
        ResponceStructure<FairPriceAllVehicles> rs = new ResponceStructure<FairPriceAllVehicles>();
        rs.setStatusCode(HttpStatus.NOT_FOUND.value());
		rs.setMessage("Route Not Found");
		rs.setData(null);
		return rs;
		}
	
	
//	Fare Prices calculating for each Vehicle 
	
		private double calculateFare(Fairprice vehicle,double distance,double duration) {
		    double baseFare = 0;
		    double pricePerKm = 0;
		    double pricePerMinute = 0;
		    switch (vehicle) {
		        case BIKE:
		            baseFare = 20;
		            pricePerKm = 8;
		            pricePerMinute = 1;
		            break;

		        case AUTO:
		            baseFare = 30;
		            pricePerKm = 12;
		            pricePerMinute = 1.5;
		            break;

		        case CAB:
		            baseFare = 50;
		            pricePerKm = 18;
		            pricePerMinute = 2;
		            break;
		    }

		    return baseFare+ (distance * pricePerKm)+ (duration * pricePerMinute);
		}
		
		
//     Getting the data from the Redis, In Separate for riderDetail in hash format and Separate for Geo Points which are in the same folder
		public ResponceStructure<List<String>> bookRide(int custId, String vehicle) {
		    Customer customer = customerRepositor.findById(custId).orElseThrow(() -> new CustomerNotExist());

//		    Getting rider data From Redis
		    Map<Object, Object> rideData = redisserver.getRideDetails(custId);
//		    Getting rider Source And Destination Locations From Redis
		    List<Point> points = redisTemplate.opsForGeo().position(
		                "custId_"+custId+":locations",
		                "source",
		                "destination"
		            );
		    if (rideData.isEmpty() || points.isEmpty()) {
		        throw new RIdeDetailNotFoundInRedis();
		    }
//		    Source LOcation
		    Point SourcePoints = points.get(0);
//		    Destination Location
		    Point DestinationPoints = points.get(1);


//			Vehicle Prices And Set Fare Price According To the Choosen Vehicle
		    double bikePrice = Double.parseDouble((String) rideData.get("bikePrice"));
		    double autoPrice = Double.parseDouble((String) rideData.get("autoPrice"));
		    double cabPrice = Double.parseDouble((String) rideData.get("cabPrice"));
		    double fare = 0;
		    if (vehicle.equalsIgnoreCase("BIKE"))
		        fare = bikePrice;
		    else if (vehicle.equalsIgnoreCase("AUTO"))
		        fare = autoPrice;
		    else if (vehicle.equalsIgnoreCase("CAB"))
		        fare = cabPrice;
		    
//		Source Cordinates
		    Cordinate source = new Cordinate();
		    source.setLongitude(SourcePoints.getX());
		    source.setLatitude(SourcePoints.getY());
//		Detination Cordinates
		    Cordinate destination = new Cordinate();
		    destination.setLongitude(DestinationPoints.getX());
		    destination.setLatitude(DestinationPoints.getY());
//		Location Names	    	
		    String pickupLocationName=(String)rideData.get("pickupLocation");
		    String destinationLocationName=(String)rideData.get("destinationLocation");
		    
//		    Saving the data into the Booking Object
		    Booking b = new Booking();
		    b.setCustomer(customer);
		    b.setPickupLoc(pickupLocationName);
		    b.setDestinationLoc(destinationLocationName);
		    b.setSourceCordinate(source);
		    b.setDesCordinate(destination);
		    b.setVechicType(vehicle);
		    b.setFare(fare);
		    LocalDate date = LocalDate.now();
		    LocalTime time = LocalTime.now();
		    b.setBookingdate(date.toString());
		    b.setBookingtime(time.toString());
		    bookingRepository.save(b);

//		    Showing the near by Rider for the Customer
		    List<String> s = redisserver.findNearbyRiders(custId, vehicle, 10);
		    System.out.println("Nearby Riders = " + s);

		    ResponceStructure<List<String>> rs = new ResponceStructure<List<String>>();
		    rs.setStatusCode(HttpStatus.CREATED.value());
		    rs.setMessage("Successfully Ride as Booked");
		    rs.setData(s);

		    return rs;
		}
}
