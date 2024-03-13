package utils.parser;

import core.solver.Factory;
import entities.Penalty;
import entities.Time;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utils.TimeConvert;

import java.util.ArrayList;

public class TimesParser {
    public static ArrayList<Time> parseUnavailableTime(NodeList unavailableList) {
        ArrayList<Time> timeArr = new ArrayList<>();
        for (int j = 0; j < unavailableList.getLength(); ++j) {
            Node time = unavailableList.item(j);
            if (time.getNodeType() == Node.ELEMENT_NODE) {
                Element timeElement = (Element) time;
                Time timeObj = new Time();
                timeObj.setStart(Integer.parseInt(timeElement.getAttribute("start")));
                timeObj.setLength(Integer.parseInt(timeElement.getAttribute("length")));
                timeObj.setWeek(timeElement.getAttribute("weeks"));
                timeObj.setDays(timeElement.getAttribute("days"));
                timeArr.add(timeObj);
            }
        }
        return timeArr;
    }

    public static ArrayList<Penalty<Time>> parseTimeClass(NodeList timeList) {
        ArrayList<Penalty<Time>> timeArr = new ArrayList<>();
        for (int j = 0; j < timeList.getLength(); ++j) {
            Node time = timeList.item(j);
            if (time.getNodeType() == Node.ELEMENT_NODE) {
                Element timeElement = (Element) time;
                Penalty<Time> obj = new Penalty<>();
                String encodeTime = TimeConvert.encode(
                    timeElement.getAttribute("start"),
                    timeElement.getAttribute("length"),
                    timeElement.getAttribute("weeks"),
                    timeElement.getAttribute("days")
                );
                Time timeObj;
                if (!Factory.getProblem().getTimes().containsKey(encodeTime)) {
                    timeObj = new Time();
                    timeObj.setStart(Integer.parseInt(timeElement.getAttribute("start")));
                    timeObj.setLength(Integer.parseInt(timeElement.getAttribute("length")));
                    timeObj.setWeek(timeElement.getAttribute("weeks"));
                    timeObj.setDays(timeElement.getAttribute("days"));
                    Factory.getProblem().getTimes().put(encodeTime, timeObj);
                } else {
                    timeObj = Factory.getProblem().getTimes().get(encodeTime);
                }
                obj.setEntity(timeObj);
                obj.setPenalty(Integer.parseInt(timeElement.getAttribute("penalty")));
                timeArr.add(obj);
            }
        }
        return timeArr;
    }
}
