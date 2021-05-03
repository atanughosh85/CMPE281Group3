package com.sjsu.cmpe281.service;

import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import com.sjsu.cmpe281.user.model.User;
import com.sjsu.cmpe281.user.model.Vehicle;



/*
 * Author: Atanu Ghosh
 */

public interface VehicleServices
{
    List<Vehicle> listAll();
    
    Iterable<Vehicle> getById(Long id);

    void delete(Long id);

    void saveVehicle(Vehicle vehicle);
    
    TypedQuery<Vehicle> constructQuery(Map<String, String> customQuery);
    
    int numberOfAVs();

}
