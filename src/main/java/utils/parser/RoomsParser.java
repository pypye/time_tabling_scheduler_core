package utils.parser;

import entities.Time;
import entities.rooms.Room;
import entities.rooms.Travel;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static utils.parser.DataParser.parseTime;

public class RoomsParser {
    public RoomsParser() {
    }

    public static List<Room> parseRoom(NodeList roomList) {
        ArrayList<Room> roomArr = new ArrayList<>();

        for (int i = 0; i < roomList.getLength(); ++i) {
            Node room = roomList.item(i);
            if (room.getNodeType() == Node.ELEMENT_NODE) {
                Element roomElement = (Element) room;
                Room roomObj = new Room();
                roomObj.setId(roomElement.getAttribute("id"));
//                roomObj.setCapacity(Integer.parseInt(roomElement.getAttribute("capacity")));
//                roomObj.setTravelList(parseTravel(roomElement.getElementsByTagName("travel")));
//                roomObj.setUnavailableList(parseTime(roomElement.getElementsByTagName("unavailable")));
                roomArr.add(roomObj);
            }
        }
        return roomArr;
    }

    public static List<Travel> parseTravel(NodeList travelList) {
        ArrayList<Travel> travelArr = new ArrayList<>();

        for (int j = 0; j < travelList.getLength(); ++j) {
            Node travel = travelList.item(j);
            if (travel.getNodeType() == Node.ELEMENT_NODE) {
                Element travelElement = (Element) travel;
                Travel travelObj = new Travel();
                travelObj.setRoom(travelElement.getAttribute("room"));
                travelObj.setValue(Integer.parseInt(travelElement.getAttribute("value")));
                travelArr.add(travelObj);
            }
        }
        return travelArr;
    }
}
