package org.teammeat.xml;

import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.*;

import java.io.*;

import org.teammeat.manager.Warehouse;
import org.teammeat.manager.Company;

public class XMLHandler {

	private DocumentBuilderFactory _factory;
	private DocumentBuilder _builder;
	private boolean _debug;
	private boolean _verbose;
	
	/**
	 * Constructor class
	 * @param debug	Whenever or not debug mode is on/off
	 * @param verbose	Whenever or not verbose mode is on/off
	 */
	public XMLHandler(boolean debug, boolean verbose)
	{
		this._debug = debug;
		this._verbose = verbose;
		
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
		
		File xmlSource = new File(xmlFile);
		
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
			System.out.println("DEBUG: Info collected, total of " + info.getLength());
			System.out.println("DEBUG: Warehouse manager: " + info.item(1).getTextContent()
					+ ", location: " + info.item(3).getTextContent());
		}
		
		Warehouse house = new Warehouse(info.item(1).getTextContent(), info.item(3).getTextContent());
		
		//TODO fill out details
		
		return house;
	}
	
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
			System.out.println("DEBUG: Info collected, total of " + info.getLength());
			System.out.println("DEBUG: Warehouse name: " + info.item(1).getTextContent()
					+ ", Manager: " + info.item(3).getTextContent()
					+ ", Location: " + info.item(5).getTextContent());
		}
		
		Company co = new Company(info.item(1).getTextContent(), 
				info.item(3).getTextContent(), 
				info.item(5).getTextContent());
		
		//TODO fill out details
		
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
					System.out.println("DEBUG: Adding warehouse in " + house.getLocation() + " to " + co.getName() );
				}
				co.addWarehouse(house);
			}
		}
		
		if(_debug)
		{
			System.out.println("DEBUG: Generating company complete");
			
		}
		
		return co;
	}
	
}
