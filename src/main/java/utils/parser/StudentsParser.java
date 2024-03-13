package utils.parser;

import core.solver.Factory;
import entities.Student;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Map;

import static utils.parser.CoursesParser.parseId;

public class StudentsParser {
    public static Map<String, Student> parseStudent(NodeList studentList) {
        Map<String, Student> studentMap = Factory.getProblem().getStudents();
        for (int i = 0; i < studentList.getLength(); ++i) {
            Node student = studentList.item(i);
            if (student.getNodeType() == Node.ELEMENT_NODE) {
                Element studentElement = (Element) student;
                Student studentObj = new Student();
                studentObj.setId(studentElement.getAttribute("id"));
                studentObj.setCourseList(parseId(studentElement.getElementsByTagName("course")));
                studentMap.put(studentObj.getId(), studentObj);
            }
        }
        return studentMap;
    }
}
