package org.teammeat.manager;

import java.io.File;

import org.w3c.dom.*;
import org.teammeat.xml.XMLHandler;


/**
 * The main function
 * @author Tommi Mansikka
 *
 */

public class WarehouseManager {

	/**
	 * Main function that is run when program is run
	 * @param args		Program parameters
	 */
	public static void main(String[] args) {
		
		System.out.println("Warehouse Manager 0.0.1");
		
		//Program variables
		boolean debug = false;
		boolean verbose = false;
		String workfolder = "";
		String output = "output.csv";
		String config = "";
		
		//We check that config file was given

		
		//Check the arguments given to the program
		for(int i = 0; i < args.length; i++)
		{
			System.out.print(args[i].toString() + " ");
			
			if(args[i].toString().equals("-s"))
			{
				i++;
				if(i < args.length)
				{
					config = args[i].toString();
					System.out.print(config + " ");
				}
			}
			else if(args[i].toString().equals("-d"))
			{
				debug = true;
			}
			else if (args[i].toString().equals("-v"))
			{
				verbose = true;
			}
			else if (args[i].toString().equals("-w"))
			{
				i++;
				if(i < args.length)
				{
					workfolder = args[i].toString();
					
					if( !workfolder.endsWith("/") )
					{
						workfolder += "/";
					}
					
					System.out.print(workfolder + " ");
				}
			}
			else if (args[i].toString().equals("-o"))
			{
				i++;
				if(i < args.length)
				{
					output = args[i].toString();
					System.out.print(output + " ");
				}
			}
			else
			{
				System.out.println("ERROR: Unknown option " + args[i].toString());
				System.exit(-1);
			}
		}
		
		//New line
		System.out.println("");
		
		//Print the debug info
		if(debug)
		{
			System.out.println("DEBUG: Debug messages activated");
			System.out.println("DEBUG: config file is " +config);
			System.out.println("DEBUG: Verbose messages is set " + verbose);
			System.out.println("DEBUG: Workfolder is set as " + workfolder);
			System.out.println("DEBUG: Output file is set as " + output);
			
		}
		
		
		
		
		
		//Check the config file
		if(config.equals(""))
		{
			System.out.println(error(1));
			System.exit(1);
		}
		
		if(debug)
		{
			System.out.println("DEBUG: Config file: " + config);
		}
		
		if(config.indexOf(".xml") != config.length() - 4)
		{
			System.out.println( error(2) );
			System.exit(2);
		}
		
		File configFile = new File(workfolder + config);
			
		if(!configFile.isFile())
		{
			System.out.println( error(4) );
			System.exit(4);
		}
		
		
		
		if(debug)
		{
			System.out.println("DEBUG: Source file: " + config);
		}
		
		//Create the XMLHandler
		XMLHandler handler = new XMLHandler(debug, verbose, workfolder);
		if(verbose)
		{
			System.out.println("Begin operations");
		}
		//And parse the config file
		Document doc = handler.parseDocument(config);
		
		if(doc == null)
		{
			System.out.println("ERROR: Parsing the document resulted in null value");
			System.exit(8);
		}
		
		Company co = handler.generateCompany(doc);
		
		Operator op = handler.generateActions(doc);
		
		if(op == null)
		{
			System.out.println("ERROR: Operator fail");
			System.exit(12);
		}
		op.setCompany(co);
		op.setOutput(output);
		
		op.act();
				
		if(debug)
		{
			System.out.println("DEBUG: All operations carried out successfully, exiting software");
		}
		
		if(verbose)
		{
			System.out.println("Exiting software");
		}
		System.exit(0);
	}
	
	//PRIVATE
	/**
	 * Prints out various error messages
	 * @param e		Error cose
	 * @return		Message
	 */
	private static String error(int e)
	{
		String message = "Undefined";
		switch(e)
		{
		case 0:
			message = "Incorrect error call";
			break;
		case 1:
			message = "No config file given";
			break;
		case 2:
			message = "Config file is not a xml file";
			break;
		case 3:
			message = "Given source file is not a valid file. Given file does not match DTD";
			break;
		case 4:
			message = "Config file does not exist";
			break;
		default:
			message = "Undefined error " + e;
			break;
		}
		
		return "ERROR:" + message;
		
	}

}
