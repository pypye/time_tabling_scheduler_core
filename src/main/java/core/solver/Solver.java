package core.solver;

import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.Literal;
import entities.Placement;
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
import java.util.Map;


public class Solver {
    private final CpSolver solver = new CpSolver();

    public Solver() {
        solver.getParameters().setNumSearchWorkers(16);
        solver.getParameters().setMaxMemoryInMb(16384);
        solver.getParameters().setMaxTimeInSeconds(60);
//        solver.getParameters().setLogSearchProgress(true);
    }

    public void buildModel() {
        for (Class x : Factory.getProblem().getClassList()) {
            x.init();
        }
//        for (Distribution d : Factory.getProblem().getDistributionList()) {
//            if (d.isRequired()) {
//                for (Class x : d.getClassList()) {
//                    for (Class y : d.getClassList()) {
//                        if (d.getClassList().indexOf(y) <= d.getClassList().indexOf(x)) {
//                            continue;
//                        }
//                        Class a = Factory.getProblem().getClassList()
//                            .stream().filter(c -> c.getId().equals(x.getId())).findFirst().orElse(null);
//                        Class b = Factory.getProblem().getClassList()
//                            .stream().filter(c -> c.getId().equals(y.getId())).findFirst().orElse(null);
//                        if (a != null && b != null) {
//                            a.removeDistributionConstraint(d.getType(), b);
//                            b.removeDistributionConstraint(d.getType(), a);
//                        }
//                    }
//                }
//            }
//
//        }
        Factory.makePlacementLiterals();
        for (Class x : Factory.getProblem().getClassList()) {
            x.makeSolverConstraints();
        }
        Factory.resolvePlacementLiteralsConflict();


    }


    public void solve() {
        System.out.println("Solving...");
        System.out.println(Factory.getModel().modelStats());
        CpSolverStatus status;
        status = solver.solve(Factory.getModel());
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
            Placement placement = getSolvePlacement(x);
            Room room = placement.getRoom();
            Time time = placement.getTime();
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
                Placement placement = getSolvePlacement(x);
                Room room = placement.getRoom();
                Time time = placement.getTime();
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

    private Placement getSolvePlacement(Class x) {
        Placement placement = null;
        for (Map.Entry<Placement, Literal> entry : x.placementLiterals.entrySet()) {
            if (solver.value(entry.getValue()) == 1) {
                placement = entry.getKey();
                break;
            }
        }
        return placement;
    }
}
