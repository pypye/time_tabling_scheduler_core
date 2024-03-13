package utils.parser;

import core.solver.Factory;
import entities.courses.Class;
import entities.courses.Config;
import entities.courses.Course;
import entities.courses.Subpart;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static utils.parser.RoomsParser.parseRoomClass;
import static utils.parser.TimesParser.parseTimeClass;

public class CoursesParser {
    public CoursesParser() {
    }

    public static Map<String, Course> parseCourse(NodeList courseList) {
        Map<String, Course> courseMap = new HashMap<>();
        for (int i = 0; i < courseList.getLength(); ++i) {
            Node course = courseList.item(i);
            if (course.getNodeType() == Node.ELEMENT_NODE) {
                Element courseElement = (Element) course;
                Course courseObj = new Course();
                courseObj.setId(courseElement.getAttribute("id"));
                courseObj.setConfigList(parseConfig(courseElement.getElementsByTagName("config")));
                courseMap.put(courseObj.getId(), courseObj);
            }
        }
        return courseMap;
    }

    public static Map<String, Config> parseConfig(NodeList configList) {
        Map<String, Config> configMap = new HashMap<>();
        for (int i = 0; i < configList.getLength(); ++i) {
            Node config = configList.item(i);
            if (config.getNodeType() == Node.ELEMENT_NODE) {
                Element configElement = (Element) config;
                Config configObj = new Config();
                configObj.setId(configElement.getAttribute("id"));
                configObj.setSubpartList(parseSubpart(configElement.getElementsByTagName("subpart")));
                configMap.put(configObj.getId(), configObj);
            }
        }
        return configMap;
    }

    public static Map<String, Subpart> parseSubpart(NodeList subpartList) {
        Map<String, Subpart> subpartMap = new HashMap<>();
        for (int i = 0; i < subpartList.getLength(); ++i) {
            Node subpart = subpartList.item(i);
            if (subpart.getNodeType() == Node.ELEMENT_NODE) {
                Element subpartElement = (Element) subpart;
                Subpart subpartObj = new Subpart();
                subpartObj.setId(subpartElement.getAttribute("id"));
                subpartObj.setClassList(parseClass(subpartElement.getElementsByTagName("class")));
                subpartMap.put(subpartObj.getId(), subpartObj);
            }
        }
        return subpartMap;
    }

    public static Map<String, Class> parseClass(NodeList classList) {
        Map<String, Class> classMap = new HashMap<>();
        for (int i = 0; i < classList.getLength(); ++i) {
            Node classNode = classList.item(i);
            if (classNode.getNodeType() == Node.ELEMENT_NODE) {
                Element classElement = (Element) classNode;
                Class classObj = new Class();
                classObj.setId(classElement.getAttribute("id"));
                if (classElement.hasAttribute("limit")) {
                    classObj.setLimit(Integer.parseInt(classElement.getAttribute("limit")));
                }
                classObj.setRoomList(parseRoomClass(classElement.getElementsByTagName("room")));
                classObj.setTimeList(parseTimeClass(classElement.getElementsByTagName("time")));
                classMap.put(classObj.getId(), classObj);
                Factory.getProblem().getClasses().put(classObj.getId(), classObj);
            }
        }
        return classMap;
    }

    public static ArrayList<String> parseId(NodeList list) {
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < list.getLength(); ++i) {
            Node item = list.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                Element ele = (Element) item;
                arr.add(ele.getAttribute("id"));
            }
        }
        return arr;
    }
}
