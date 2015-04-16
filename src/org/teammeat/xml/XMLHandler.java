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

	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private boolean debug;
	
	public XMLHandler(boolean debug)
	{
		this.debug = debug;
		
		factory = DocumentBuilderFactory.newInstance();
		
		factory.setValidating(true);
		
		try 
		{
			builder = factory.newDocumentBuilder();
			
		} catch (ParserConfigurationException e) {
			System.out.println("ERROR: Error while creating XML handler");
			System.out.println( e.toString() );
			System.exit(5);
			return;
		}
		
		builder.setErrorHandler(
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
	
	public Document parseDocument(String xmlFile)
	{
		if(debug)
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
		
		if(debug)
		{
			System.out.println("DEBUG: Creating document");
		}
		
		
		//We attempt to create the document
		try 
		{
			doc = builder.parse(xmlSource);
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
		
		if(debug)
		{
			System.out.println("DEBUG: File parsed, moving to validation");
		}
		
		return doc;
	}
	
	public Warehouse generateWarehouse(Document xml)
	{
		if(xml.getDoctype().toString() != "Warehouse")
		{
			System.out.println("ERROR: Incorrect doctype. Expected: Warehouse; received: " + xml.getDoctype().toString() );
			return null;
		}
		//Node root = xml.getDocumentElement();
		
		//Node info = root.
		//TODO get info
		
		Warehouse house = new Warehouse("Placeholder", "Placeholder", "Placeholder");
		
		//TODO fill out details
		return house;
	}
	
}
