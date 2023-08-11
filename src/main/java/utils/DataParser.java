package utils;

import entities.Time;
import entities.rooms.Room;
import entities.rooms.Travel;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DataParser {
    public DataParser() {
    }

    public static List<Room> parseRoom(NodeList roomList) {
        ArrayList<Room> roomArr = new ArrayList();

        for(int i = 0; i < roomList.getLength(); ++i) {
            Node room = roomList.item(i);
            if (room.getNodeType() == 1) {
                Element roomElement = (Element)room;
                Room roomObj = new Room();
                roomObj.setId(roomElement.getAttribute("id"));
                roomObj.setCapacity(Integer.parseInt(roomElement.getAttribute("capacity")));
                roomObj.setTravelList(parseTravel(roomElement.getElementsByTagName("travel")));
                roomObj.setUnavailableList(parseUnavailable(roomElement.getElementsByTagName("unavailable")));
                roomArr.add(roomObj);
            }
        }

        return roomArr;
    }

    public static List<Travel> parseTravel(NodeList travelList) {
        ArrayList<Travel> travelArr = new ArrayList();

        for(int j = 0; j < travelList.getLength(); ++j) {
            Node travel = travelList.item(j);
            if (travel.getNodeType() == 1) {
                Element travelElement = (Element)travel;
                Travel travelObj = new Travel();
                travelObj.setRoom(travelElement.getAttribute("room"));
                travelObj.setValue(Integer.parseInt(travelElement.getAttribute("value")));
                travelArr.add(travelObj);
            }
        }

        return travelArr;
    }

    public static List<Time> parseUnavailable(NodeList unavailableList) {
        ArrayList<Time> unavailableArr = new ArrayList();

        for(int j = 0; j < unavailableList.getLength(); ++j) {
            Node unavailable = unavailableList.item(j);
            if (unavailable.getNodeType() == 1) {
                Element unavailableElement = (Element)unavailable;
                Time unavailableObj = new Time();
                unavailableObj.setStart(Integer.parseInt(unavailableElement.getAttribute("start")));
                unavailableObj.setLength(Integer.parseInt(unavailableElement.getAttribute("length")));
                unavailableObj.setWeek(unavailableElement.getAttribute("weeks"));
                unavailableObj.setDays(unavailableElement.getAttribute("days"));
                unavailableArr.add(unavailableObj);
            }
        }

        return unavailableArr;
    }
}
