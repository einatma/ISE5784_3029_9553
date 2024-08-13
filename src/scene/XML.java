package scene;

import geometries.*;
import lighting.AmbientLight;
import org.xml.sax.SAXException;
import primitives.*;

import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.io.*;

/**
 * The XML class is responsible for parsing XML scene files and constructing the
 * scene objects based on the parsed data.
 */
public class XML {

    /**
     * Parses the XML scene file and constructs the scene objects.
     *
     * @param scene    The Scene object to populate with the parsed data.
     * @param fileName The path of the XML scene file to parse.
     * @throws ParserConfigurationException If a DocumentBuilder cannot be created.
     * @throws IOException                  If an I/O error occurs while parsing the
     *                                      XML file.
     * @throws SAXException                 If any parsing errors occur.
     */
    public static void sceneParser(Scene scene, String fileName)
            throws ParserConfigurationException, IOException, SAXException {
        // Create a new instance of DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // Create a new instance of DocumentBuilder
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Parse the XML file using the DocumentBuilder and obtain the Document object
        Document document = builder.parse(new File(fileName));
        // Normalize the structure of the XML document
        document.getDocumentElement().normalize();

        // Get the root element of the XML document
        Element root = document.getDocumentElement();

        // Set the background color of the scene by parsing the value of the
        // "background-color" attribute
        scene.setBackground(parseColor(root.getAttribute("background-color")));

        // Get the ambient element from the root element and set the ambient light of
        // the scene
        Element ambient = (Element) root.getElementsByTagName("ambient-light").item(0);
        scene.setAmbientLight(new AmbientLight(parseColor(ambient.getAttribute("color")), new Double3(1d, 1d, 1d)));

        // Get the list of geometry elements from the root element
        NodeList geometryNodes = root.getElementsByTagName("geometries");

        // Create a new Geometries object to store the parsed geometries
        Geometries geometries = new Geometries();

        // Iterate over the geometry elements and parse each geometry based on its type
        for (int i = 0; i < geometryNodes.getLength(); i++) {
            Element geometryElement = (Element) geometryNodes.item(i);
            Node attribute = geometryNodes.item(i);
            String geometryType = attribute.getNodeName();

            switch (geometryType) {
                case "triangle":
                    // Parse the points of the triangle and add it to the geometries
                    Point p0 = parsePoint(geometryElement.getAttribute("p0"));
                    Point p1 = parsePoint(geometryElement.getAttribute("p1"));
                    Point p2 = parsePoint(geometryElement.getAttribute("p2"));
                    geometries.add(new Triangle(p0, p1, p2));
                    break;
                case "sphere":
                    // Parse the center and radius of the sphere and add it to the geometries
                    Point center = parsePoint(geometryElement.getAttribute("center"));
                    double radius = Double.parseDouble(geometryElement.getAttribute("radius"));
                    geometries.add(new Sphere( center , radius));
                    break;
                case "plane":
                    // Parse the point and vector of the plane and add it to the geometries
                    Point point = parsePoint(geometryElement.getAttribute("point"));
                    Vector vector = parseVector(geometryElement.getAttribute("vector"));
                    geometries.add(new Plane(point, vector));
                    break;
            }
        }

        // Set the parsed geometries to the scene
        scene.setGeometries(geometries);
    }

    /**
     * Parses a vector from a string.
     *
     * @param toParse The string to parse.
     * @return The parsed vector.
     */
    private static Vector parseVector(String toParse) {
        String[] parsed = toParse.split(" ");
        return new Vector(Double.parseDouble(parsed[0]), Double.parseDouble(parsed[1]), Double.parseDouble(parsed[2]));
    }

    /**
     * Parses a point from a string.
     *
     * @param toParse The string to parse.
     * @return The parsed point.
     */
    private static Point parsePoint(String toParse) {
        String[] parsed = toParse.split(" ");
        return new Point(Double.parseDouble(parsed[0]), Double.parseDouble(parsed[1]), Double.parseDouble(parsed[2]));
    }

    /**
     * Parses a color from a string.
     *
     * @param toParse The string to parse.
     * @return The parsed color.
     */
    private static Color parseColor(String toParse) {
        String[] parsed = toParse.split(" ");
        return new Color(Integer.parseInt(parsed[0]), Integer.parseInt(parsed[1]), Integer.parseInt(parsed[2]));
    }
}