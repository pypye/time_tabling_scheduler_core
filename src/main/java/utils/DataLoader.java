package utils;

import entities.Student;
import entities.courses.Course;
import entities.rooms.Room;
import org.w3c.dom.Document;
import utils.parser.CoursesParser;
import utils.parser.DataParser;
import utils.parser.RoomsParser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;

public class DataLoader {
    public DataLoader(String path) {
        try {
            parse(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void parse(String path) throws Exception {
        File inputFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        List<Room> roomArr = RoomsParser.parseRoom(doc.getElementsByTagName("rooms").item(0).getChildNodes());
        List<Course> courseArr = CoursesParser.parseCourse(doc.getElementsByTagName("courses").item(0).getChildNodes());
        List<Student> studentArr = DataParser.parseStudent(doc.getElementsByTagName("students").item(0).getChildNodes());
        System.out.println(studentArr);
    }
}
