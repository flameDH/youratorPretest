package util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtil {

	public Document createXml(String xml) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbFactory.newDocumentBuilder();
		// Document doc = dBuilder.parse(f);

		InputSource is = new InputSource(new StringReader(xml));
		return builder.parse(is);

	}

	public Document createXml(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbFactory.newDocumentBuilder();
		return builder.parse(xmlFile);

	}

	public String findTextContent(Document doc, String eleName) {
		NodeList lst = doc.getElementsByTagName(eleName);
		for (int i = 0; i < lst.getLength(); i++) {
			Node node = lst.item(i);
			if (node instanceof Element)
				return node.getTextContent();
		}
		return null;
	}
}
