package utils.parser;

import entities.courses.Class;
import entities.courses.Config;
import entities.courses.Course;
import entities.courses.Subpart;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class CoursesParser {
    public CoursesParser() {
    }


    public static List<Course> parseCourse(NodeList courseList) {
        ArrayList<Course> courseArr = new ArrayList<>();

        for (int i = 0; i < courseList.getLength(); ++i) {
            Node course = courseList.item(i);
            if (course.getNodeType() == Node.ELEMENT_NODE) {
                Element courseElement = (Element) course;
                Course courseObj = new Course();
                courseObj.setConfigList(parseConfig(courseElement.getElementsByTagName("config")));
                courseArr.add(courseObj);
            }
        }
        return courseArr;
    }

    public static List<Config> parseConfig(NodeList configList) {
        ArrayList<Config> configArr = new ArrayList<>();

        for (int i = 0; i < configList.getLength(); ++i) {
            Node config = configList.item(i);
            if (config.getNodeType() == Node.ELEMENT_NODE) {
                Element configElement = (Element) config;
                Config configObj = new Config();
                configObj.setSubpartList(parseSubpart(configElement.getElementsByTagName("subpart")));
                configArr.add(configObj);
            }
        }
        return configArr;
    }

    public static List<Subpart> parseSubpart(NodeList subpartList) {
        ArrayList<Subpart> subpartArr = new ArrayList<>();

        for (int i = 0; i < subpartList.getLength(); ++i) {
            Node subpart = subpartList.item(i);
            if (subpart.getNodeType() == Node.ELEMENT_NODE) {
                Element subpartElement = (Element) subpart;
                Subpart subpartObj = new Subpart();
                subpartObj.setClassList(parseClass(subpartElement.getElementsByTagName("class")));
                subpartArr.add(subpartObj);
            }
        }
        return subpartArr;
    }

    public static List<Class> parseClass(NodeList classList) {
        ArrayList<Class> classArr = new ArrayList<>();

        for (int i = 0; i < classList.getLength(); ++i) {
            Node classNode = classList.item(i);
            if (classNode.getNodeType() == Node.ELEMENT_NODE) {
                Element classElement = (Element) classNode;
                Class classObj = new Class();
                classObj.setId(classElement.getAttribute("id"));
                classObj.setLimit(Integer.parseInt(classElement.getAttribute("limit")));
                classArr.add(classObj);
            }
        }
        return classArr;
    }
}
