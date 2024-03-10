package utils.parser;

import entities.Distribution;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static utils.parser.CoursesParser.parseClass;

public class DistributionsParser {
    public static List<Distribution> parseDistribution(NodeList distributionList) {
        ArrayList<Distribution> distributionArr = new ArrayList<>();

        for (int i = 0; i < distributionList.getLength(); ++i) {
            Node student = distributionList.item(i);
            if (student.getNodeType() == Node.ELEMENT_NODE) {
                Element distributionElement = (Element) student;
                Distribution distributionObj = new Distribution();
                distributionObj.setType(distributionElement.getAttribute("type"));
                distributionObj.setClassList(parseClass(distributionElement.getElementsByTagName("class")));
                if (distributionElement.getAttribute("required").equals("true")) {
                    distributionObj.setRequired(true);
                } else {
                    distributionObj.setRequired(false);
                }
                distributionArr.add(distributionObj);
            }
        }
        return distributionArr;
    }
}