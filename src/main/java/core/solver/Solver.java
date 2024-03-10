package core.solver;

import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.LinearExpr;
import core.constraints.defaults.TwoClassNotOverlap;
import core.constraints.defaults.UnavailableRoom;
import core.constraints.distributions.*;
import entities.Distribution;
import entities.Time;
import entities.courses.Class;
import entities.rooms.Room;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class Solver {
    private final CpSolver solver = new CpSolver();

    public Solver() {
        solver.getParameters().setNumSearchWorkers(16);
        solver.getParameters().setMaxMemoryInMb(16384);
        solver.getParameters().setMaxTimeInSeconds(60);
        solver.getParameters().setLogSearchProgress(true);
    }

    public void buildModel() {
        // init solver constraint
        for (Class x : Factory.getProblem().getClassList()) {
            x.initSolverConstraint();
        }
        System.out.println("Model built");

        // minimize penalty
        ArrayList<IntVar> penaltyList = new ArrayList<>();
        for (Class x : Factory.getProblem().getClassList()) {
            if (!x.getRoomList().isEmpty()) {
                penaltyList.addAll(Arrays.asList(x.penaltyRoom).subList(0, x.getRoomList().size()));
            }
            if (!x.getAvailableTimeList().isEmpty()) {
                penaltyList.addAll(Arrays.asList(x.penaltyTime).subList(0, x.getAvailableTimeList().size()));
            }
        }
        LinearExpr penalty = LinearExpr.sum(penaltyList.toArray(new IntVar[0]));
        Factory.getModel().minimize(penalty);


        // class can not meet in unavailable room
        for (Class x : Factory.getProblem().getClassList()) {
            UnavailableRoom.add(x);
        }
        System.out.println("Unavailable room constraints added");

        int count = 0;
        // 2 class can not overlap in time and room
        for (int i = 0; i < Factory.getProblem().getClassList().size(); i++) {
            for (int j = i + 1; j < Factory.getProblem().getClassList().size(); j++) {
                Class x = Factory.getProblem().getClassList().get(i);
                Class y = Factory.getProblem().getClassList().get(j);
                count += TwoClassNotOverlap.add(x, y);
            }
        }
        System.out.println("Added " + count + " constraints for 2 class not overlap");
        System.out.println("Default constraints added");

        // add distribution constraint
        int classCount = Factory.getProblem().getClassList().size();
        ArrayList<ArrayList<ArrayList<String>>> classMatrix = new ArrayList<>();
        for (int i = 0; i < classCount; i++) {
            classMatrix.add(new ArrayList<>());
            for (int j = 0; j < classCount; j++) {
                classMatrix.get(i).add(new ArrayList<>());
            }
        }
        int cnt = 0;
        for (Distribution d : Factory.getProblem().getDistributionList()) {
            if (d.isRequired()) {
                for (int i = 0; i < d.getClassList().size(); i++) {
                    for (int j = i + 1; j < d.getClassList().size(); j++) {
                        Class x = d.getClassList().get(i);
                        Class y = d.getClassList().get(j);
                        classMatrix
                            .get(Integer.parseInt(x.getId()) - 1)
                            .get(Integer.parseInt(y.getId()) - 1)
                            .add(d.getType());
                        classMatrix
                            .get(Integer.parseInt(y.getId()) - 1)
                            .get(Integer.parseInt(x.getId()) - 1)
                            .add(d.getType());
                    }
                }
                cnt++;
            }
        }
        System.out.println("Added " + cnt + " distribution constraints");

        for (int i = 0; i < Factory.getProblem().getClassList().size(); i++) {
            for (int j = i + 1; j < Factory.getProblem().getClassList().size(); j++) {
                Class x = Factory.getProblem().getClassList().get(i);
                Class y = Factory.getProblem().getClassList().get(j);
                for (String type : classMatrix.get(Integer.parseInt(x.getId()) - 1).get(Integer.parseInt(y.getId()) - 1)) {
                    addDistributionConstraint(type, x, y);
                }
            }
            System.out.println("Added " + (classCount - i - 1) + " distribution constraints for class " + (i + 1) + " out of " + classCount);
        }
    }

    public void addDistributionConstraint(String type, Class x, Class y) {
        switch (type) {
            case "SameStart":
                SameStart.add(x, y);
                break;
            case "SameTime":
                SameTime.add(x, y);
                break;
            case "DifferentTime":
                DifferentTime.add(x, y);
                break;
            case "SameDays":
                SameDays.add(x, y);
                break;
            case "DifferentDays":
                DifferentDays.add(x, y);
                break;
            case "SameWeeks":
                SameWeeks.add(x, y);
                break;
            case "DifferentWeeks":
                DifferentWeeks.add(x, y);
                break;
            case "Overlap":
                Overlap.add(x, y);
                break;
            case "NotOverlap":
                NotOverlap.add(x, y);
                break;
            case "SameRoom":
                SameRoom.add(x, y);
                break;
            case "DifferentRoom":
                DifferentRoom.add(x, y);
                break;
            case "SameAttendees":
                SameAttendees.add(x, y);
                break;
            default:
                System.out.println("Distribution type not implemented: " + type);
        }
    }


    public void solve() {
        System.out.println("Solving...");
        System.out.println(Factory.getModel().modelStats());
        CpSolverStatus status = solver.solve(Factory.getModel());
        System.out.println("Solve time: " + solver.wallTime());
        if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
            System.out.println(status);
            writeConsole(solver);
            new File("./output").mkdir();
            writeXML(solver, "./output/solution.xml");
        } else {
            System.out.println("No solution found!");
        }
    }

    public void writeConsole(CpSolver solver) {
        for (Class x : Factory.getProblem().getClassList()) {
            Room room = getSolverRoom(x);
            Time time = getSolverTime(x);
            if (room != null && time != null) {
                System.out.println(
                    "Class " + x.getId() +
                        " in room " + room.getId() +
                        " from " + time.getStart() +
                        " to " + time.getEnd() +
                        " on day " + time.getDays() +
                        " on week " + time.getWeek()
                );
            } else {
                if (time != null)
                    System.out.println(
                        "Class " + x.getId() +
                            " from " + time.getStart() +
                            " to " + time.getEnd() +
                            " on day " + time.getDays() +
                            " on week " + time.getWeek()
                    );
            }
        }
        System.out.println("Total penalty: " + solver.objectiveValue());
    }

    public void writeXML(CpSolver solver, String path) {
        Document dom;
        Element e;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.newDocument();
            Element rootEle = dom.createElement("solution");
            rootEle.setAttribute("name", Factory.getProblem().getName());
            rootEle.setAttribute("runtime", String.valueOf(solver.wallTime()));
            rootEle.setAttribute("technique", "CP-SAT");
            rootEle.setAttribute("author", "Pye");
            rootEle.setAttribute("institution", "UET");
            rootEle.setAttribute("country", "Vietnam");
            dom.appendChild(rootEle);
            for (Class x : Factory.getProblem().getClassList()) {
                e = dom.createElement("class");
                e.setAttribute("id", x.getId());
                Room room = getSolverRoom(x);
                Time time = getSolverTime(x);
                if (room != null) {
                    e.setAttribute("room", room.getId());
                }
                if (time != null) {
                    e.setAttribute("start", String.valueOf(time.getStart()));
                    e.setAttribute("days", time.getDays());
                    e.setAttribute("weeks", time.getWeek());
                }
                rootEle.appendChild(e);
            }
            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "http://www.itc2019.org/competition-format.dtd");
                tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(path)));
            } catch (TransformerException | IOException te) {
                System.out.println(te.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("Error while trying to instantiate DocumentBuilder " + pce);
        }
    }

    private Room getSolverRoom(Class x) {
        Room room = null;
        for (int i = 0; i < x.getRoomList().size(); i++) {
            if (solver.value(x.room[i]) == 1) {
                room = x.getRoomList().get(i);
            }
        }
        return room;
    }

    private Time getSolverTime(Class x) {
        Time time = null;
        for (int i = 0; i < x.getAvailableTimeList().size(); i++) {
            if (solver.value(x.time[i]) == 1) {
                time = x.getAvailableTimeList().get(i);
                break;
            }
        }
        return time;
    }
}
