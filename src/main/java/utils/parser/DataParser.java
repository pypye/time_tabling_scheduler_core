package utils.parser;

import entities.Student;
import entities.Time;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static utils.parser.CoursesParser.parseCourse;

public class DataParser {
    public DataParser() {
    }


    public static List<Student> parseStudent(NodeList studentList) {
        ArrayList<Student> studentArr = new ArrayList<>();

        for (int i = 0; i < studentList.getLength(); ++i) {
            Node student = studentList.item(i);
            if (student.getNodeType() == Node.ELEMENT_NODE) {
                Element studentElement = (Element) student;
                Student studentObj = new Student();
                studentObj.setId(studentElement.getAttribute("id"));
                studentObj.setCourseList(parseCourse(studentElement.getElementsByTagName("course")));
                studentArr.add(studentObj);
            }
        }
        return studentArr;
    }

    public static List<Time> parseTime(NodeList timeList) {
        ArrayList<Time> timeArr = new ArrayList<>();

        for (int j = 0; j < timeList.getLength(); ++j) {
            Node time = timeList.item(j);
            if (time.getNodeType() == Node.ELEMENT_NODE) {
                Element timeElement = (Element) time;
                Time timeObj = new Time();
                timeObj.setStart(Integer.parseInt(timeElement.getAttribute("start")));
                timeObj.setLength(Integer.parseInt(timeElement.getAttribute("length")));
                timeObj.setWeek(timeElement.getAttribute("weeks"));
                timeObj.setDays(timeElement.getAttribute("days"));
                timeArr.add(timeObj);
            }
        }
        return timeArr;
    }
}
