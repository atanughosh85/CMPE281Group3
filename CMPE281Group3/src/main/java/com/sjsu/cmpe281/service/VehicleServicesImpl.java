package com.sjsu.cmpe281.service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sjsu.cmpe281.repositories.VehicleRepository;
import com.sjsu.cmpe281.user.model.User;
import com.sjsu.cmpe281.user.model.Vehicle;



/*
 * Author: Atanu Ghosh
 */

@Service
public class VehicleServicesImpl implements VehicleServices {

    private VehicleRepository vehicleRepository;

    @Autowired
    EntityManager em;
    
    @Autowired
    public VehicleServicesImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }
    /*
     * Service method to list all vehicles
     */
    @Override
    public List<Vehicle> listAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicleRepository.findAll().forEach(vehicles::add);
        return vehicles;
    }

    /*
     * Service method to list vehicle by id
     */
    @Override
    public Iterable<Vehicle> getById(Long id) {
    	
        List <Long> ids = new ArrayList<Long>();
        ((ArrayList<Long>) ids).add(id);
		return vehicleRepository.findAllById(ids);
    }

   
    /*
     * Service method to save new vehicles. This method is to be used for new vehicle registration.
     */
	@Override
	public void saveVehicle(Vehicle vehicle) {
		// TODO Auto-generated method stub
		try {
		Vehicle savedVehicle=vehicleRepository.save(vehicle);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/*
	 * Service method to delete vehicles, when Administrator wants to delete any vehicle.
	 */
    @Override
    public void delete(Long id) {
        vehicleRepository.deleteById(id);

    }
    
    
    /*
	 * Service method to search vehicle using any vehicle parameters.
	 */
	@Override
	public TypedQuery<Vehicle> constructQuery(Map<String, String> customQuery) {
		CriteriaBuilder cb = null;
		try {
			cb = em.getCriteriaBuilder();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		CriteriaQuery<Vehicle> cq = cb.createQuery(Vehicle.class);
		Root<Vehicle> vehicle = cq.from(Vehicle.class);
		Predicate existingpredicate = null;
		int predicateCount=0;
		Predicate masterPredicate=null;

		try {

			for (Map.Entry<String,String> entry : customQuery.entrySet())  
			{
				if(vehicle.get(entry.getKey().toString()) != null)
				{
					
					//Query for range values with comma(,) as delimiter
					if(entry.getValue().contains(","))
					{
						int minRange=Integer.parseInt(customQuery.get(entry.getKey().toString()).split(",")[0]);
						int maxRange=Integer.parseInt(customQuery.get(entry.getKey().toString()).split(",")[1]);
						if(predicateCount==0)
						{
							masterPredicate = cb.between(vehicle.get(entry.getKey().toString()),minRange, maxRange );
						}
						else
						{
							existingpredicate = cb.between(vehicle.get(entry.getKey().toString()),minRange, maxRange );
							masterPredicate=cb.and(masterPredicate,existingpredicate);
						}
						predicateCount++;
					}
					//Query for equals values
					else
					{
						
						if(predicateCount==0)
						{
							masterPredicate = cb.equal(vehicle.get(entry.getKey().toString()), customQuery.get(entry.getKey().toString()));
						}
						else
						{
							existingpredicate = cb.equal(vehicle.get(entry.getKey().toString()), customQuery.get(entry.getKey().toString()));
							masterPredicate=cb.and(masterPredicate,existingpredicate);
						}
						predicateCount++;
						//cq.where(predicate);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		cq.where(masterPredicate);
		TypedQuery<Vehicle> query = em.createQuery(cq);
		return query;
	}
	

    
    
	@Override
	public int numberOfAVs() {
		List<Vehicle> vehicles = new ArrayList<>();
		HashSet<String> av = new HashSet<String>();
        vehicleRepository.findAll().forEach(vehicles::add);
        
        for (int i = 0; i < vehicles.size(); i++) {
        	
            av.add(vehicles.get(i).getCarnumber());
        }
        
        
		return av.size();
	}


}
