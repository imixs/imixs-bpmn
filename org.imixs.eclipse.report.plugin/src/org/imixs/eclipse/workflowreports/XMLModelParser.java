package org.imixs.eclipse.workflowreports;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IFile;
import org.imixs.eclipse.workflowreports.model.ReportObject;
import org.imixs.workflow.ItemCollection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This Class parses an XML File to create an IX WorkflowModel Object or save an
 * IX WorkflowModel into an File. The class uses the Interface
 * org.eclipse.core.resources.IFile
 * 
 * @see org.eclipse.core.resources.IFile
 * @author Ralph Soika
 * 
 */
public class XMLModelParser {

	/**
	 * This Method parses an XML File and creates a new WorkflowModel Instnace
	 * 
	 * @param fileInput
	 * @return
	 */
	public static ReportObject parseModel(IFile fileInput) {
		//System.out.println("[XMLModelParser] parseModel....");
		ReportObject queryObject = new ReportObject(fileInput.getName());
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			Document doc = builder.parse(fileInput.getContents());

			// Alle Enviroments auslesen
			NodeList nodes = doc.getElementsByTagName("itemcollection");
			if (nodes.getLength() > 0) {
				Element element = (Element) nodes.item(0);
				ItemCollection itemCollection = parseItemCollection(element);
				queryObject.setItemCollection(itemCollection);
			}

		} catch (Exception e) {
			// create empty object
			 queryObject = new ReportObject(fileInput.getName());
		}

		queryObject.clearDirtyFlag();
		return queryObject;
	}

	/**
	 * Diese Methode parst einen ItemColleciton Tag und �bertr�gt die Inhalte in
	 * eine ItemCollection
	 * 
	 * @param nodeSection
	 * @return
	 */
	private static ItemCollection parseItemCollection(Element nodeItemcollection) {
		ItemCollection itemCollection = new ItemCollection();
		String sFieldType, sFieldName, sFieldValue;
		// Hashtable hashFields = new Hashtable();
		try {

			// Items Tags parsen.....
			NodeList items = nodeItemcollection.getElementsByTagName("item");
			for (int j = 0; j < items.getLength(); j++) {
				// System.out.println("parse ein Item..");
				Element item = (Element) items.item(j);

				sFieldName = item.getAttribute("name");
				sFieldType = item.getAttribute("type");
				// System.out.println(" Name=" + sFieldName);
				// System.out.println(" Type=" + sFieldType);
				Vector vectorValue = new Vector();

				// check if multi value
				NodeList valueList = item.getElementsByTagName("value");
				if (valueList != null && valueList.getLength() > 0) {
					for (int i = 0; i < valueList.getLength(); i++) {
						// changed for JDK 1.4
						// Node textnode=textlist.item(i);
						Element textnode = (Element) valueList.item(i);
						if ("number".equalsIgnoreCase(sFieldType)) {
							// changed for JDK 1.4
							// vectorValue.add(new
							// Double(textnode.getTextContent()));
							sFieldValue = getCharacterDataFromElement(textnode);

							// change : 24.09.2006
							// test if sFieldValue should become an Integer or
							// Double Object
							try {
								// double or integer?
								if (sFieldValue.indexOf(".") > -1)
									vectorValue.add(new Double(sFieldValue));
								else
									vectorValue.add(new Integer(sFieldValue));
							} catch (NumberFormatException e) {
								// convertion not possible
								vectorValue.add(new Integer(0));
							}
							// OLD CODE: did not checks integer or double type :
							// vectorValue.add(new Double(sFieldValue));
						} else {
							// changed for JDK 1.4
							// vectorValue.add(textnode.getTextContent());
							sFieldValue = getCharacterDataFromElement(textnode);
							vectorValue.add(sFieldValue.trim());
						}
					}
				}

				itemCollection.replaceItemValue(sFieldName, vectorValue);
			}
		} catch (Exception e) {
			System.out
					.println("[WorkflowmodelerPlugin] parseItemCollection error: "
							+ e.toString());
		}
		return itemCollection;
	}

	private static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child == null)
			return "";
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "?";
	}

	/**
	 * This Method transforms a QueryObject into a OutputStream This Method can
	 * be used to save a model into a IFile
	 * 
	 * @see <code>org.imixs.eclipse.workflowmodeler.WorkflowmodelerPlugin</code>
	 * @param workflowModel
	 * @return
	 */
	public static ByteArrayOutputStream transformModel(ReportObject queryObject) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder domBuilder = domFactory.newDocumentBuilder();

			Document doc = domBuilder.newDocument();

			// Insert the root element node
			Element root = doc.createElement("query");
			doc.appendChild(root);

			// Insert a comment in front of the element node
			Comment comment = doc
					.createComment("Imixs IX XML QueyObject  -  www.imixs.com");
			doc.insertBefore(comment, root);

			Element configurationElement = doc.createElement("itemcollection");
			root.appendChild(configurationElement);

			transformItemCollection(doc, configurationElement, queryObject
					.getItemCollection());

			// Prepare the DOM document for writing
			Source source = new DOMSource(doc);

			// Write the DOM document to the file
			TransformerFactory factory = TransformerFactory.newInstance();
			factory.setAttribute("indent-number", new Integer(2));
			Transformer transformer = factory.newTransformer();

			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// (3)wrap the otuputstream with a writer (or bufferedwriter)
			transformer.transform(source, new StreamResult(
					new OutputStreamWriter(out, "utf-8")));

		} catch (Exception e) {

		}
		return out;

	}

	/**
	 * Diese Methode parsed einen Prozessbaum
	 * 
	 * @param nodeProcessTree
	 */
	private static void transformItemCollection(Document doc,
			Element rootelement, ItemCollection itemCollection) {

		Element itemcollectionElement = doc.createElement("itemcollection");

		// Map map = itemCollection.getAllItems();
		Iterator it = itemCollection.getAllItems().entrySet().iterator();
		// int i = 0;
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String sName = (String) entry.getKey();
			// ItemValue ermitteln
			Vector v = (Vector) entry.getValue();
			if (v.size() > 0) {
				Element itemElement = doc.createElement("item");
				itemElement.setAttribute("name", sName);

				// check type...
				Object o = v.elementAt(0);
				if (o instanceof Integer || o instanceof Double)
					itemElement.setAttribute("type", "number");

				Enumeration enumvalues = v.elements();
				while (enumvalues.hasMoreElements()) {
					String sValue = enumvalues.nextElement().toString();
					Element valueElement = doc.createElement("value");
					// changed for JDK 1.4
					// text.setTextContent(sValue);
					valueElement.appendChild((doc.createTextNode(sValue)));
					itemElement.appendChild(valueElement);
				}
				itemcollectionElement.appendChild(itemElement);
			}
		}
		rootelement.appendChild(itemcollectionElement);

	}

}
