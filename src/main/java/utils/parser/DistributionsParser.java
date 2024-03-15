package utils.parser;

import entities.Distribution;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static utils.parser.CoursesParser.parseId;

public class DistributionsParser {
    public static List<Distribution> parseDistribution(NodeList distributionList) {
        ArrayList<Distribution> distributionArr = new ArrayList<>();
        for (int i = 0; i < distributionList.getLength(); ++i) {
            Node student = distributionList.item(i);
            if (student.getNodeType() == Node.ELEMENT_NODE) {
                Element distributionElement = (Element) student;
                Distribution distributionObj = new Distribution();
                distributionObj.setType(distributionElement.getAttribute("type"));
                distributionObj.setClassList(parseId(distributionElement.getElementsByTagName("class")));
                distributionObj.setRequired(distributionElement.getAttribute("required").equals("true"));
                if (distributionElement.hasAttribute("penalty")) {
                    distributionObj.setPenalty(Integer.parseInt(distributionElement.getAttribute("penalty")));
                }
                distributionArr.add(distributionObj);
            }
        }
        return distributionArr;
    }
}
