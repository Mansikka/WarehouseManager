package org.teammeat.xml;

import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.*;

import java.io.*;

import org.teammeat.manager.Item;
import org.teammeat.manager.Operator;
import org.teammeat.manager.Warehouse;
import org.teammeat.manager.Company;

public class XMLHandler {

	private DocumentBuilderFactory _factory;
	private DocumentBuilder _builder;
	private boolean _debug;
	private boolean _verbose;
	private String _workfolder;
	
	/**
	 * Constructor class
	 * @param debug	Whenever or not debug mode is on/off
	 * @param verbose	Whenever or not verbose mode is on/off
	 */
	public XMLHandler(boolean debug, boolean verbose, String workfolder)
	{
		this._debug = debug;
		this._verbose = verbose;
		this._workfolder = workfolder;
		
		_factory = DocumentBuilderFactory.newInstance();
		
		//Make sure validation is set true;
		_factory.setValidating(true);
		
		try 
		{
			_builder = _factory.newDocumentBuilder();
			
		} catch (ParserConfigurationException e) {
			System.out.println("ERROR: Error while creating XML handler");
			System.out.println( e.toString() );
			System.exit(5);
			return;
		}
		
		//Set up error handling
		_builder.setErrorHandler(
				new ErrorHandler() {
			        public void warning(SAXParseException e) throws SAXException 
			        {
			        	System.out.println("WARNING : " + e.getMessage()); // do nothing
			        }

			        public void error(SAXParseException e) throws SAXException 
			        {
			        	System.out.println("ERROR: " + e.getMessage());
			        	throw e;
			        }

			        public void fatalError(SAXParseException e) throws SAXException 
			        {
			        	System.out.println("FATAL ERROR: " + e.getMessage());
			            throw e;
			        }
			 });
	}
	
	/**
	 * Parses given xml file
	 * @param xmlFile	XML file to parse
	 * @return			XML document
	 */
	public Document parseDocument(String xmlFile)
	{
		if(_debug)
		{
			System.out.println("DEBUG: Begining parsing operation for file " + xmlFile);
		}
		
		File xmlSource = new File(_workfolder + xmlFile);
		
		if(!xmlSource.isFile())
		{
			System.out.println("ERROR: Given source file is not an actual file");
			System.exit(6);
		}
		
		
		Document doc = null;
		
		if(_debug)
		{
			System.out.println("DEBUG: Creating document");
		}
		
		
		//We attempt to create the document
		try 
		{
			doc = _builder.parse(xmlSource);
		} catch (SAXException e) 
		{
			System.out.println("ERROR: XML parser error");
			e.printStackTrace();
			doc = null;
			System.exit(7);
			
		}
		catch (IOException e) 
		{
			System.out.println("ERROR: IO failure while parsing the document");
			e.printStackTrace();
			doc = null;
			System.exit(7);
		}
		
		if(_debug)
		{
			System.out.println("DEBUG: File parsed, moving to validation");
		}
		
		return doc;
	}
	
	/**
	 * Generates Warehouse class from given XML document
	 * @param xml	XML Document
	 * @return		Warehouse class, returns null in case of error
	 */
	public Warehouse generateWarehouse(Document xml)
	{
		if(!xml.getDoctype().getName().equals("warehouse"))
		{
			System.out.println("ERROR: Incorrect doctype. Expected: warehouse; received: " + xml.getDoctype().getName() );
			return null;
		}
		
		if(_debug)
		{
			System.out.println("DEBUG: Doctype matches generator. Getting info");
		}
		
		Node root = xml.getDocumentElement();
		
		//We get the info
		NodeList info = root.getChildNodes().item(1).getChildNodes();
		
		if(_debug)
		{
			System.out.println("DEBUG: Info collected, total of " + (info.getLength() - 1 - ( ( info.getLength() - 1) / 2 ) ) );
			System.out.println("DEBUG: Warehouse name: " + root.getAttributes().item(0).getTextContent() 
					+ ", manager: " + info.item(1).getTextContent()
					+ ", location: " + info.item(3).getTextContent());
		}
		
		Warehouse house = new Warehouse(root.getAttributes().item(0).getTextContent(), info.item(1).getTextContent(), info.item(3).getTextContent());

		NodeList inbound = root.getChildNodes().item(3).getChildNodes();
		NodeList outbound = root.getChildNodes().item(5).getChildNodes();
		NodeList storage = root.getChildNodes().item(7).getChildNodes();
		
		if(_debug)
		{
			System.out.println("DEBUG: Inbound items : " + (inbound.getLength() - 1 - ( ( inbound.getLength() - 1) / 2 ) ) );
			System.out.println("DEBUG: Outbound items: " + (outbound.getLength() - 1 - ( ( outbound.getLength() - 1) / 2 ) ) );
			System.out.println("DEBUG: Storage items : " + (storage.getLength() - 1 - ( ( storage.getLength() - 1) / 2 ) ) );
		}
		
		
		//We deal with the inbound items
		for(int i = 1; i < inbound.getLength(); i += 2)
		{
			NodeList item = inbound.item(i).getChildNodes();
			
			int id 		= 	Integer.parseInt( item.item(1).getTextContent() );
			String name = 	item.item(3).getTextContent();
			int amount 	= 	Integer.parseInt( item.item(5).getTextContent() );
			
			if(_debug)
			{
				System.out.println("DEBUG: New Inbound item, id: " + id + ", name: " + name + ", amount: " + amount);
			}
			
			Item object = new Item(id, name, amount );
			
			house.addInbound(object);
		}
		
		//Same deal for outbound items
		for(int i = 1; i < outbound.getLength(); i += 2)
		{
			NodeList item = outbound.item(i).getChildNodes();
			
			int id 		= 	Integer.parseInt( item.item(1).getTextContent() );
			String name = 	item.item(3).getTextContent();
			int amount 	= 	Integer.parseInt( item.item(5).getTextContent() );
			
			if(_debug)
			{
				System.out.println("DEBUG: New Outbound item, id: " + id + ", name: " + name + ", amount: " + amount);
			}
			
			Item object = new Item(id, name, amount );
			
			house.addOutbound(object);
		}
		
		
		//And finally, stuff in storage
		for(int i = 1; i < storage.getLength(); i += 2)
		{
			NodeList item = storage.item(i).getChildNodes();
			
			int id 		= 	Integer.parseInt( item.item(1).getTextContent() );
			String name = 	item.item(3).getTextContent();
			int amount 	= 	Integer.parseInt( item.item(5).getTextContent() );
			
			if(_debug)
			{
				System.out.println("DEBUG: New Storage item, id: " + id + ", name: " + name + ", amount: " + amount);
			}
			
			Item object = new Item(id, name, amount );
			
			house.addStorage(object);
		}
		
		if(_debug)
		{
			System.out.println("DEBUG: Generating warehouse " + house.getName() + " complete");
		}
	
		if(_verbose)
		{
			System.out.println("Parsed a warehouse " + house.getName());
		}
		
		return house;
	}
	
	/**
	 * Generates Company class from given XML document
	 * @param xml	XML Document
	 * @return		Company class, returns null in case of error
	 */
	public Company generateCompany(Document xml)
	{
		if(!xml.getDoctype().getName().equals("company"))
		{
			System.out.println("ERROR: Incorrect doctype. Expected: company; received: " + xml.getDoctype().getName() );
			return null;
		}
		
		if(_debug)
		{
			System.out.println("DEBUG: Doctype matches generator. Getting info");
		}
		
		Node root = xml.getDocumentElement();
		
		//We get the info
		NodeList info = root.getChildNodes().item(1).getChildNodes();
		
		if(_debug)
		{
			System.out.println("DEBUG: Info collected, total of " + (info.getLength() - 1 - ( ( info.getLength() - 1) / 2 ) ));
			System.out.println("DEBUG: Warehouse name: " + info.item(1).getTextContent()
					+ ", Manager: " + info.item(3).getTextContent()
					+ ", Location: " + info.item(5).getTextContent());
		}
		
		Company co = new Company(info.item(1).getTextContent(), 
				info.item(3).getTextContent(), 
				info.item(5).getTextContent());
		
		
		//We get the Warehouses		
		NodeList warehouses = root.getChildNodes().item(3).getChildNodes();
		
		if(_debug)
		{
			System.out.println("DEBUG: Company " + co.getName() + " has " + (warehouses.getLength() - 1 - ( ( warehouses.getLength() - 1) / 2 ) ) + " warehouses" ); 
			for(int i = 1; i < warehouses.getLength(); i += 2)
			{
				Node item = warehouses.item(i);
				System.out.println("DEBUG: " + item.getAttributes().item(0).getNodeValue() );
			}
		}
		
		for(int i = 1; i < warehouses.getLength(); i += 2)
		{
			Node item = warehouses.item(i);
			Document temp = parseDocument( item.getAttributes().item(0).getNodeValue() );
			Warehouse house = generateWarehouse(temp);
			if(house != null)
			{
				if(_debug)
				{
					System.out.println("DEBUG: Adding warehouse " + house.getName() + " to " + co.getName() );
				}
				co.addWarehouse(house);
			}
		}
		
		if(_debug)
		{
			System.out.println("DEBUG: Generating company complete");
			
		}
		
		if(_verbose)
		{
			System.out.println("Parsed a company " + co.getName());
		}
		
		return co;
	}
	
	
	/**
	 * Generates Operator class from given XML document,
	 * @param xml	XML Document
	 * @return		Operator class, returns null in case of error
	 */
	public Operator generateActions(Document xml)
	{
		Node root = xml.getDocumentElement();
		
		Operator op = new Operator(_debug, _verbose);
		
		NodeList actions = root.getChildNodes().item(5).getChildNodes();
		
		if(_debug)
		{
			for(int i = 1; i < actions.getLength(); i +=2)
			{
				Node temp = actions.item(i);
				System.out.println("DEBUG: action: " + temp.getAttributes().getNamedItem("type").getTextContent() + ", param: " + temp.getAttributes().getNamedItem("att").getTextContent() );
			}
		}
		
		for(int i = 1; i < actions.getLength(); i +=2)
		{
			Node temp = actions.item(i);
			String act = temp.getAttributes().getNamedItem("type").getTextContent();
			String param = temp.getAttributes().getNamedItem("att").getTextContent();
			
			op.addActions(act.toUpperCase(), param.toUpperCase());
		}
		
		op.sortActions();
		
		if(_verbose)
		{
			System.out.println("Actions parsed");
		}
		
		return op;
	}
	
}
