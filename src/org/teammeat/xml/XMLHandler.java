package org.teammeat.xml;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

import java.io.*;

//import java.io.File;

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
		
		
	}
	
	public boolean validateDTD(Document xml, String dtd)
	{

		 if(debug)
		{
			 System.out.println("DEBUG: Starting validation using " + dtd + " DTD file");
		}
		
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Source schemaFile = new StreamSource(new File(dtd));
	    Schema schema;
	    
	    if(debug)
	    {
	    	System.out.println("DEBUG: Getting the DTD");
	    }
	    
	    //TODO Figure out what goes wrong here
	    
		try {
			schema = factory.newSchema(schemaFile);
		} catch (SAXException e) {
			System.out.println("DEBUG: Error while handling the DTD");
			System.out.println(e.toString());
			e.printStackTrace();
			return false;
		}
		Validator validator = schema.newValidator();
		
		if(debug)
		{
			System.out.println("DEBUG: Validating xml file");
		}
		
		try {
			validator.validate(new DOMSource(xml));
		} catch (SAXException e) {
			System.out.println("ERROR: SAXException");
			System.out.println( e.toString() );
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			System.out.println("ERROR: IOException");
			System.out.println( e.toString() );
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
	public Document parseDocument(String xmlFile, String type)
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
			System.out.println( e.toString() );
			e.printStackTrace();
			doc = null;
			System.exit(7);
			
		} catch (IOException e) 
		{
			System.out.println("ERROR: IO failure while parsing the document");
			System.out.println( e.toString() );
			e.printStackTrace();
			doc = null;
			System.exit(7);
		}
		
		if(debug)
		{
			System.out.println("DEBUG: File parsed, moving to validation");
		}
		//TODO Fix validation
		/*
		 * 
		String dtd = "Warehouse.dtd";
		
		if(type.equals("Company"))
		{
			dtd = "Company.dtd";
		}
		
		
		if( !validateDTD( doc, dtd) )
		{
			System.out.println("ERROR: XML file is not a valid" );
			System.exit(8);
		}
		*/
		
		return doc;
	}
	
}
