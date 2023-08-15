package utils;

import entities.Problem;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import static utils.parser.DataParser.parseProblem;

public class DataLoader {
    private final Problem problem;
    public DataLoader(String path) {
        try {
            this.problem = parse(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static Problem parse(String path) throws Exception {
        File inputFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        return parseProblem(doc);
    }

    public Problem getProblem() {
        return problem;
    }
}
