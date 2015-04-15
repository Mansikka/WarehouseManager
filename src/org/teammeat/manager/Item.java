package org.teammeat.manager;

public class Item {

	int _id;
	String _name;
	int	_amount;
	
	public Item(int id, String name, int amount)
	{
		_id = id;
		_name = name;
		_amount = amount;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getAmount()
	{
		return _amount;
	}
	
	//TODO Setters
	
	public void setAmount(int newValue)
	{
		_amount = newValue;
	}
	
	public void addAmount(int value)
	{
		_amount += value;
	}
	
	public void decreaseAmount(int value)
	{
		_amount -= value;
	}
}
