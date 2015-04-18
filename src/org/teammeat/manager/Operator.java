package org.teammeat.manager;

import java.util.Vector;

import org.teammeat.manager.Company;
import org.teammeat.manager.Warehouse;
import org.teammeat.manager.Item;

public class Operator {

	private Vector<Action> operations;
	private Company _co;
	private boolean _debug;
	private boolean _verbose;
	
	/**
	 * Constructor
	 */
	public Operator(boolean debug, boolean verbose)
	{
		operations = new Vector<Action>();
		_co = null;
		_debug = debug;
		_verbose = verbose;
	}
	
	/**
	 * Sets the company that is going to be target of operations
	 * @param co	Company to be used
	 */
	public void setCompany(Company co)
	{
		_co = co;
	}
	
	/**
	 * Adds new action to the action que
	 * @param action	Action name
	 * @param param		Action parameter
	 */
	public void addActions(String action, String param)
	{
		if(_debug)
		{
			System.out.println("DEBUG: Adding new action " + action + " with param " + param);
		}
		
		operations.addElement(new Action(action, param));
	}
	
	/**
	 * Carries out acts given to it earlier. Will ignore commands that are not known
	 */
	public void act()
	{
		Vector<Item> result = null;
		
		for(int i = 0; i < operations.size(); i++)
		{
			String action = operations.elementAt(i).getAction();
			String param = operations.elementAt(i).getParam();
			
			if( action.equals("MERGE"))
			{
				if(_verbose)
				{
					System.out.println("Merging stocks");
				}
				result = mergeAll(param);
			}
			else if( action.equals("SORT") )
			{
				if(_verbose)
				{
					System.out.println("Sorting stock");
				}
				if(result == null)
				{
					if(_debug)
					{
						System.out.println("DEBUG: No stock to sort, creating one based on STORAGE");
					}
					result = mergeAll("STORAGE");
				}
				sort(result, param);
				
			}
			
		}
		
		if(_verbose)
		{
			System.out.println("All actions carried out");
		}
	}
	
	/**
	 * Merges all warehouse stocks into one vector.
	 * @param stock		What stocks to merge (INBOUND, OUTBOUND, STORAGE)
	 * @return			Merged vector
	 */
	private Vector<Item> mergeAll(String stock)
	{
		Vector<Item> result = new Vector<Item>();
		Vector <Warehouse> wares = _co.getWarehouse();	
		
		for(int i = 0; i < wares.size(); i++)
		{
			Vector<Item> temp = null;
			if(stock.equals("INBOUND"))
			{
				temp = wares.elementAt(i).getInbound();
			}
			else if (stock.equals("OUTBOUND"))
			{
				temp = wares.elementAt(i).getOutbound();
			}
			else if (stock.equals("STORAGE"))
			{
				temp = wares.elementAt(i).getStorage();
			}
			else
			{
				System.out.println("ERROR: Unknown storage");
				System.exit(11);
			}
			
			result = combine(result, temp);
		}
		
		if(_debug)
		{
			System.out.println("DEBUG: Merged stock:");
			
			for(int i = 0; i < result.size(); i++)
			{
				System.out.println("DEBUG: " + result.elementAt(i).getId() + ":" + result.elementAt(i).getName() + ":" + result.elementAt(i).getAmount() );
			}
		}
		
		return result;
	}
	
	/**
	 * Combines two vectors into one, combining duplicate ids into one item
	 * @param list1		First vector
	 * @param list2		Second vector
	 * @return			Combined list
	 */
	private Vector<Item> combine(Vector<Item> list1, Vector<Item> list2)
	{
		if(_debug)
		{
			System.out.println("Combining two lists with " + list1.size() + " and " + list2.size() + " items");
		}
		
		if(list1.size() == 0)
		{
			return list2;
		}
		
		if(list2.size() == 0)
		{
			return list1;
		}
		
		//We go through items in list2 one by one
		for(int i = 0; i < list2.size(); i++)
		{
			//We check the items against items on the list1
			for(int j = 0; j < list1.size(); j++)
			{
				if( list1.get(j).getId() == list2.get(i).getId() )
				{
					list1.get(j).addAmount( list2.get(i).getAmount() );
				}
			}
		}
		
		return list1;
	}
	
	/**
	 * Sorts list using merge-sort system. This one sorts
	 * @param list	List given
	 * @return		Sorted list
	 */
	private Vector<Item> sort(Vector<Item> list, String order)
	{
		if(_debug)
		{
			System.out.println("DEBUG: Sorting list with " + list.size() + " elements");
		}
		
		//If we have split the list to one by now, we return the list
		if(list.size() <= 1)
		{
			return list;
		}
		
		Vector<Item> result = new Vector<Item>();
		
		
		Vector<Item> list1 = split(list, order, 0, list.size() / 2);
		Vector<Item> list2 = split(list, order, list.size() / 2, list.size());
		
		//while both lists have elements, keep comparing and merging them
		while(list1.size() > 0 && list2.size() > 0)
		{
			if(order.equals("DECENDING"))
			{
				if( list1.get(0).getId() >= list2.get(0).getId() )
				{
					if(_debug)
					{
						System.out.println("DEBUG: Merging from list1 item " + list1.get(0).getId() + ":" + list1.get(0).getName()  );
					}
					result.add( list1.get(0) );
					list1.remove(0);
				}
				else
				{
					if(_debug)
					{
						System.out.println("DEBUG: Merging from list2 item " + list1.get(0).getId() + ":" + list1.get(0).getName()  );
					}
					result.add( list2.get(0) );
					list2.remove(0);
				}
			}
			else {
				if( list1.get(0).getId() <= list2.get(0).getId() )
				{
					if(_debug)
					{
						System.out.println("DEBUG: Merging from list1 item " + list1.get(0).getId() + ":" + list1.get(0).getName()  );
					}
					result.add( list1.get(0) );
					list1.remove(0);
				}
				else
				{
					if(_debug)
					{
						System.out.println("DEBUG: Merging from list2 item " + list1.get(0).getId() + ":" + list1.get(0).getName()  );
					}
					result.add( list2.get(0) );
					list2.remove(0);
				}
			}
		}
		
		
		
		//If either list has elements left, add them		
		for(int i = 0; i < list1.size(); i++)
		{
			result.add( list1.get(i) );
		}
		for(int i = 0; i < list2.size(); i++)
		{
			result.add( list2.get(i) );
		}
		
		if(_debug)
		{
			System.out.println("DEBUG: Merged list:");
			for(int i = 0; i < result.size(); i++)
			{
				System.out.println( "DEBUG: " + result.get(i).getId() + "|" + result.get(i).getName() + "|" + result.get(i).getAmount() );
			}
		}
		
		return result;
	}
	
	/**
	 * Splits vector by taking objects starting from low to high, then returning the resulting list
	 * @param list		Vector to split
	 * @param order		Ascending or decending order, passed to sort
	 * @param low		Starting point
	 * @param high		End point
	 * @return			Resulting vector which is then sorted
	 */
	private Vector<Item> split(Vector<Item> list, String order, int low, int high)
	{
		Vector<Item> result = new Vector<Item>();
		
		for(int i = low; i < high; i++)
		{
			result.add( list.get(i) );
		}
		
		//Before we send the result back, we do sorting to it
		return sort(result, order);
	}
	
	
	/**
	 *	Action container 
	 */
	public class Action
	{
		private String _action;
		private String _param;
		
		public Action(String action, String param)
		{
			_action = action;
			_param = param;
		}
		
		public String getAction()
		{
			return _action;
		}
		
		public String getParam()
		{
			return _param;
		}
	}

}

