package org.teammeat.manager;

import java.util.Vector;

import org.teammeat.manager.Item;

public class Warehouse {

	private Vector<Item> storage;
	private Vector<Item> inbound;
	private Vector<Item> outbound;
	
	private String _name;
	private String _manager;
	private String _location;
	
	public Warehouse(String name, String manager, String location)
	{
		_name = name;
		_location = location;
		_manager = manager;
		
		storage = new Vector<Item>();
		inbound = new Vector<Item>();
		outbound = new Vector<Item>();

	}
	
	public String getName()
	{
		return _name;
	}
	
	public String getLocation()
	{
		return _location;
	}
	
	public String getManager()
	{
		return _manager;
	}
	
	public void addInbound(Item item)
	{
		if(item == null)
		{
			return;
		}
		
		for(int i = 0; i < inbound.size(); i++)
		{
			
			//If item is already on the list, we add it to the already existing one
			if(inbound.get(i).getId() == item.getId())
			{
				inbound.get(i).addAmount( item.getAmount() );
				return;
			}
		}
		
		//Item was not on the inbound list, so we add it to the list.
		inbound.add(item);
	}
	
	public void addOutbound(Item item)
	{
		if(item == null)
		{
			return;
		}
		
		for(int i = 0; i < outbound.size(); i++)
		{
			
			//If item is already on the list, we add it to the already existing one
			if(outbound.get(i).getId() == item.getId())
			{
				outbound.get(i).addAmount( item.getAmount() );
				return;
			}
		}
		
		//Item was not on the outbound list, so we add it to the list.
		outbound.add(item);
	}
	
	public void addStorage(Item item)
	{
		if(item == null)
		{
			return;
		}
		
		for(int i = 0; i < storage.size(); i++)
		{
			
			//If item is already on the list, we add it to the already existing one
			if(storage.get(i).getId() == item.getId())
			{
				storage.get(i).addAmount( item.getAmount() );
				return;
			}
		}
		
		//Item was not on the outbound list, so we add it to the list.
		storage.add(item);
	}
	
	//TODO remove functions, getters
}
