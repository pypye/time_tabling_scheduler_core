package core.solver;

import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import core.constraints.defaults.SpecificRoom;
import core.constraints.defaults.SpecificTime;
import core.constraints.defaults.TwoClassNotOverlap;
import core.constraints.defaults.UnavailableRoom;
import entities.Time;
import entities.courses.Class;
import entities.rooms.Room;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import utils.TimeConvert;

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


public class Solver {
    private final CpSolver solver = new CpSolver();

    public Solver() {
    }

    public void buildModel() {
        // init solver constraint
        for (Class x : Factory.getProblem().getClassList()) {
            x.initSolverConstraint();
        }
        // class can meet in only specific several room and time
        for (Class x : Factory.getProblem().getClassList()) {
            SpecificRoom.add(x);
            SpecificTime.add(x);
        }
        // 2 class can not overlap in time and room
        for (Class x : Factory.getProblem().getClassList()) {
            for (Class y : Factory.getProblem().getClassList()) {
                if (x.getId().equals(y.getId())) continue;
                TwoClassNotOverlap.add(x, y);
            }
        }
        // class can not meet in unavailable room
        for (Class x : Factory.getProblem().getClassList()) {
            for (Room y : x.getRoomList()) {
                for (Time z : y.getUnavailableList()) {
                    UnavailableRoom.add(x, y, z);
                }
            }
        }
    }

    public void solve() {
        System.out.println("Solving...");
        CpSolverStatus status = solver.solve(Factory.getModel());
        System.out.println("Solve time: " + solver.wallTime());
        if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
            writeConsole(solver);
            new File("./output").mkdir();
            writeXML(solver, "./output/solution.xml");
        } else {
            System.out.println("No solution found!");
        }
    }

    public void writeConsole(CpSolver solver) {
        for (Class x : Factory.getProblem().getClassList()) {
            if (x.room != null) {
                System.out.println(
                    "Class " + x.getId() +
                        " in room " + solver.value(x.room) +
                        " from " + solver.value(x.start) +
                        " to " + solver.value(x.end) +
                        " on day " + TimeConvert.convert(x.day, solver) +
                        " on week " + TimeConvert.convert(x.week, solver)
                );
            } else {
                System.out.println(
                    "Class " + x.getId() +
                        " from " + solver.value(x.start) +
                        " to " + solver.value(x.end) +
                        " on day " + TimeConvert.convert(x.day, solver) +
                        " on week " + TimeConvert.convert(x.week, solver)
                );
            }
        }
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
                if (x.room != null) {
                    e.setAttribute("room", Long.toString(solver.value(x.room)));
                }
                e.setAttribute("start", Long.toString(solver.value(x.start)));
                e.setAttribute("days", TimeConvert.convert(x.day, solver));
                e.setAttribute("weeks", TimeConvert.convert(x.week, solver));
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

}
