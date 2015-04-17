package org.teammeat.manager;

import java.util.Vector;

import org.teammeat.manager.Company;
import org.teammeat.manager.Warehouse;
import org.teammeat.manager.Item;

public class Actions {

	private Company co;
	
	public Actions(Company company){
		
		co = company;
	}
	
	/**
	 * Merges all warehouse stocks into one vector.
	 * @param stock		What stocks to merge (INBOUND, OUTBOUND, STORAGE)
	 * @return			Merged vector
	 */
	private Vector<Item> mergeAll(String stock)
	{
		Vector<Item> result = new Vector<Item>();
		Vector <Warehouse> wares = co.getWarehouse();
		
		
		
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
	 * Combines two vectors into one, combining duplicate ids into one
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
}
