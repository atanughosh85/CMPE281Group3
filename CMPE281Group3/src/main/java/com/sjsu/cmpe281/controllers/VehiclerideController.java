package com.sjsu.cmpe281.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sjsu.cmpe281.repositories.VehiclerideRepository;
import com.sjsu.cmpe281.service.VehiclerideServices;
import com.sjsu.cmpe281.user.model.User;
import com.sjsu.cmpe281.user.model.Vehicleride;
import com.sjsu.cmpe281.user.model.VehicleStatus;

/*
 * Author: Atanu Ghosh
 */

@Repository
@RestController
public class VehiclerideController {
	private VehiclerideServices vehiclerideServices;
	@Autowired
	EntityManager em;

	@Autowired
	VehiclerideRepository vehiclerideRepository;

	@Autowired
	public void setVehicleRideService(VehiclerideServices vehiclerideService) {
		this.vehiclerideServices = vehiclerideService;
	}

	/*
	 * Shows all vehicles
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/vehicleride/list")
	public List<Vehicleride> listVehiclesride(Model model){

		ArrayList<Vehicleride> listOfVehicleride = new ArrayList<Vehicleride>();

		model.addAttribute("vehicleride", vehiclerideServices.listAll());

		Map<String, Object> modelMap = model.asMap();
		listOfVehicleride  = (ArrayList<Vehicleride>) modelMap.get("vehicleride");
		return listOfVehicleride;
	}

	/*
	 * Shows property by vehicleID
	 */
	@RequestMapping("vehicleride/show/{id}")
	public Vehicleride getVehicleride(@PathVariable String id, Model model){
		model.addAttribute("vehicleride", vehiclerideServices.getById(String.valueOf(id)));
		ArrayList<Vehicleride> listOfVehicleride = new ArrayList<Vehicleride>();        
		Map<String, Object> modelMap = model.asMap();
		listOfVehicleride=(ArrayList<Vehicleride>) modelMap.get("vehicleride");
		return listOfVehicleride.get(0);
	}


	/*
	 * Add new vehicles to the table
	 */
	@RequestMapping(method = RequestMethod.POST, value = "vehicleride/add")
	public void addVehicleride(@RequestBody Vehicleride vehicleride)
	{
		vehiclerideServices.saveVehicleride(vehicleride);
	}



	/*
	 * Delete vehicleride by ID
	 */
	@RequestMapping("/vehicleride/delete/{id}")
	public String delete(@PathVariable String id){
		vehiclerideServices.delete(id);
		return "delete successful";
	}
	
	/*
	 * Search vehicle by any vehicle parameter
	 */
	@RequestMapping(method = RequestMethod.GET, value = "vehicleride/search")
	public List<Vehicleride> getVehicleride(@RequestParam Map<String, String> customQuery)
	{
		return vehiclerideServices.constructQuery(customQuery).getResultList();

	}
	

}
