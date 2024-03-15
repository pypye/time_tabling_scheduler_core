package utils.parser;

import core.solver.Factory;
import entities.Distribution;
import entities.Problem;
import entities.Time;
import entities.rooms.Room;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import utils.MergeDistribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        problem.setRooms(parseRoom(doc.getElementsByTagName("rooms").item(0).getChildNodes()));
        problem.setCourses(parseCourse(doc.getElementsByTagName("courses").item(0).getChildNodes()));
        problem.setStudents(parseStudent(doc.getElementsByTagName("students").item(0).getChildNodes()));
        problem.setDistributions(parseDistribution(doc.getElementsByTagName("distributions").item(0).getChildNodes()));
        mergeUnavailableRoom();
        mergeDistributions();
    }

    public static void mergeUnavailableRoom() {
        for (Room y : Factory.getProblem().getRooms().values()) {
            List<Time> unavailableList = mergeWithDays(y.getUnavailableList());
            unavailableList = mergeWithWeeks(unavailableList);
            y.setUnavailableList(unavailableList);
        }
    }

    public static void mergeDistributions() {
        List<Distribution> newDistributions = new ArrayList<>();
        Map<Distribution, MergeDistribution> distributionMap = new HashMap<>();
        for (Distribution d : Factory.getProblem().getDistributions()) {
            Distribution key = new Distribution();
            key.setType(d.getType());
            key.setRequired(d.isRequired());
            key.setPenalty(d.getPenalty());
            if (!distributionMap.containsKey(key)) {
                distributionMap.put(key, new MergeDistribution(Factory.getProblem().getClasses().size()));
            }
            MergeDistribution mergeDistribution = distributionMap.get(key);
            for (int i = 1; i < d.getClassList().size() - 1; i++) {
                mergeDistribution.merge(Integer.parseInt(d.getClassList().get(i)), Integer.parseInt(d.getClassList().get(i + 1)));
            }
        }

        for (Map.Entry<Distribution, MergeDistribution> entry : distributionMap.entrySet()) {
            Distribution d = entry.getKey();
            MergeDistribution mergeDistribution = entry.getValue();
            Map<Integer, ArrayList<String>> classMap = new HashMap<>();
            for (int i = 1; i < mergeDistribution.getRootList().length; i++) {
                int root = mergeDistribution.getRoot(i);
                if (!classMap.containsKey(root)) {
                    classMap.put(root, new ArrayList<>());
                }
                classMap.get(root).add(String.valueOf(i));
            }
            for (ArrayList<String> x : classMap.values()) {
                if (x.size() > 1) {
                    Distribution newDistribution = new Distribution();
                    newDistribution.setType(d.getType());
                    newDistribution.setRequired(d.isRequired());
                    newDistribution.setPenalty(d.getPenalty());
                    newDistribution.setClassList(x);
                    newDistributions.add(newDistribution);
                }
            }
        }
        Factory.getProblem().setDistributions(newDistributions);
    }
}
