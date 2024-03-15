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
import utils.Log;

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
        solver.getParameters().setMaxTimeInSeconds(120);
        solver.getParameters().setLogSearchProgress(true);
    }

    public void buildModel() {
        for (Class x : Factory.getProblem().getClasses().values()) {
            x.init();
        }
        Factory.getProblem().prepareDistribution();
        Factory.getProblem().makePlacementLiterals();
        for (Class x : Factory.getProblem().getClasses().values()) {
            x.mapClassPlacementToGlobalPlacement();
        }
        Factory.getProblem().resolvePlacementLiteralsConflict();
    }


    public void solve() {
        Log.info("Solving problem");
        Log.info("Model info: " + Factory.getModel().modelStats());
        CpSolverStatus status;
        status = solver.solve(Factory.getModel());
        if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
            writeConsole(solver);
            Log.info("Solving status: " + status);
            Log.info("Objective value: " + solver.objectiveValue());
            Log.info("Wall time: " + solver.wallTime());
            new File("./output").mkdir();
            writeXML(solver, "./output/solution.xml");
        } else {
            Log.error("No solution found");
            Log.info("Solving status: " + status);
            Log.info("Wall time: " + solver.wallTime());
        }
    }

    public void writeConsole(CpSolver solver) {
        for (Class x : Factory.getProblem().getClasses().values()) {
            Placement placement = getSolvePlacement(x);
            Room room = placement.getRoom();
            Time time = placement.getTime();
            if (room != null && time != null) {
                System.out.println("Class " + x.getId() + " in room " + room.getId() + " from " + time.getStart() + " to " + time.getEnd() + " on day " + time.getDays() + " on week " + time.getWeek());
            } else {
                if (time != null) {
                    System.out.println("Class " + x.getId() + " from " + time.getStart() + " to " + time.getEnd() + " on day " + time.getDays() + " on week " + time.getWeek());
                }
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
            for (Class x : Factory.getProblem().getClasses().values()) {
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
                Log.error("Error while trying to write XML " + te);
            }
        } catch (ParserConfigurationException pce) {
            Log.error("Error while trying to instantiate DocumentBuilder " + pce);
        }
    }

    private Placement getSolvePlacement(Class x) {
        Placement placement = null;
        for (Map.Entry<Placement, Literal> entry : x.getPlacements().entrySet()) {
            if (solver.value(entry.getValue()) == 1) {
                placement = entry.getKey();
                break;
            }
        }
        return placement;
    }
}
