package org.teammeat.manager;

import java.util.Vector;

import org.teammeat.manager.Warehouse;

public class Company {

	//TODO Member variables
	private Vector<Warehouse> _warehouses;
	
	private String _name;
	private String _manager;
	private String _location;
	
	/**
	 * Constructor 
	 * @param name		Name
	 * @param manager	Manager
	 * @param location	Location
	 */
	public Company(String name, String manager, String location)
	{
		//TODO complete constructor
		_name = name;
		_manager = manager;
		_location = location;
		
		_warehouses = new Vector<Warehouse>();
	}
	
	//TODO Everything :/
	
	//Getters
	public String getName()
	{
		return _name;
	}
	
	public String getManager()
	{
		return _manager;
	}
	
	public String getLocation()
	{
		return _location;
	}
	
	/**
	 * Adds new Warehouse to the company
	 * @param addition	New Warehouse to add
	 */
	public void addWarehouse(Warehouse addition)
	{
		_warehouses.addElement(addition);
	}
	
	/**
	 * Returns list of warehouses
	 * @return	List of warehouses
	 */
	public Vector<Warehouse> getWarehouse()
	{
		return _warehouses;
	}
	
	/**
	 * Gets the warehouse with the given name
	 * @param 	name		Name of the warehouse
	 * @return	Warehouse object or null if not found
	 */
	public Warehouse getWarehouse(String name)
	{
		for(int i = 0; i < _warehouses.size(); i++)
		{
			if( _warehouses.elementAt(i).getName().equals(name))
			{
				return _warehouses.elementAt(i);
			}
		}
		
		return null;
	}
	
	public Vector<Item> mergeInbound()
	{
		Vector <Item> inbounds = new Vector<Item>();
		
		for(int i = 0; i < _warehouses.size(); i++)
		{
			//Vector <Item> list = _warehouses.get(i);
		}
		
		return inbounds;
	}
}
