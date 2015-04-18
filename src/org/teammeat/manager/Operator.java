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
	public Operator(Company co, boolean debug, boolean verbose)
	{
		operations = new Vector<Action>();
		_co = co;
		_debug = debug;
		_verbose = verbose;
	}
	
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
				sort(result);
				
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
	private Vector<Item> sort(Vector<Item> list)
	{
		//If we have split the list to one by now, we return the list
		if(list.size() == 1)
		{
			return list;
		}
		
		Vector<Item> result = new Vector<Item>();
		
		
		Vector<Item> list1 = split(list, 0, list.size() / 2);
		Vector<Item> list2 = split(list, list.size() / 2, list.size());
		
		//while both lists have elements, keep comparing and merging them
		while(list1.size() > 0 && list2.size() > 0)
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
	
	private Vector<Item> split(Vector<Item> list, int low, int high)
	{
		Vector<Item> result = new Vector<Item>();
		
		for(int i = low; i < high; i++)
		{
			result.add( list.get(i) );
		}
		
		//Before we send the result back, we do sorting to it
		return sort(result);
	}
	
	
	
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

