package utils.parser;

import core.solver.Factory;
import entities.Optimization;
import entities.Problem;
import entities.Time;
import entities.rooms.Room;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

import static utils.MergeUnavailableRoom.mergeWithDays;
import static utils.MergeUnavailableRoom.mergeWithWeeks;
import static utils.parser.CoursesParser.parseCourse;
import static utils.parser.DistributionsParser.parseDistribution;
import static utils.parser.RoomsParser.parseRoom;
import static utils.parser.StudentsParser.parseStudent;

public class DataParser {
    public DataParser() {
    }

    public static void parseProblem(Document doc) {
        Problem problem = Factory.getProblem();
        Element problemElement = (Element) doc.getElementsByTagName("problem").item(0);
        problem.setName(problemElement.getAttribute("name"));
        problem.setNrDays(Integer.parseInt(problemElement.getAttribute("nrDays")));
        problem.setSlotsPerDay(Integer.parseInt(problemElement.getAttribute("slotsPerDay")));
        problem.setNrWeeks(Integer.parseInt(problemElement.getAttribute("nrWeeks")));

        problem.setOptimization(parseOptimization(doc.getElementsByTagName("optimization").item(0)));
        problem.setRooms(parseRoom(doc.getElementsByTagName("rooms").item(0).getChildNodes()));
        problem.setCourses(parseCourse(doc.getElementsByTagName("courses").item(0).getChildNodes()));
        problem.setStudents(parseStudent(doc.getElementsByTagName("students").item(0).getChildNodes()));
        problem.setDistributions(parseDistribution(doc.getElementsByTagName("distributions").item(0).getChildNodes()));
        mergeUnavailableRoom();
    }

    public static void mergeUnavailableRoom() {
        for (Room y : Factory.getProblem().getRooms().values()) {
            List<Time> unavailableList = mergeWithDays(y.getUnavailableList());
            unavailableList = mergeWithWeeks(unavailableList);
            y.setUnavailableList(unavailableList);
        }
    }

    public static Optimization parseOptimization(Node node) {
        Optimization optimization = new Optimization();
        Element optimizationElement = (Element) node;
        optimization.setRoom(Integer.parseInt(optimizationElement.getAttribute("room")));
        optimization.setTime(Integer.parseInt(optimizationElement.getAttribute("time")));
        optimization.setDistribution(Integer.parseInt(optimizationElement.getAttribute("distribution")));
        optimization.setStudent(Integer.parseInt(optimizationElement.getAttribute("student")));
        return optimization;
    }
}
