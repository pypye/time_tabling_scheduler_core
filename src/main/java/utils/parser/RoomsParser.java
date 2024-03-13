package utils.parser;

import core.solver.Factory;
import entities.Penalty;
import entities.rooms.Room;
import entities.rooms.Travel;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.parser.TimesParser.parseUnavailableTime;

public class RoomsParser {
    public static Map<String, Room> parseRoom(NodeList roomList) {
        Map<String, Room> roomMap = new HashMap<>();
        for (int i = 0; i < roomList.getLength(); ++i) {
            Node room = roomList.item(i);
            if (room.getNodeType() == Node.ELEMENT_NODE) {
                Element roomElement = (Element) room;
                Room roomObj = new Room();
                roomObj.setId(roomElement.getAttribute("id"));
                if (roomElement.getAttribute("capacity").isEmpty()) {
                    roomObj.setCapacity(0);
                } else {
                    roomObj.setCapacity(Integer.parseInt(roomElement.getAttribute("capacity")));
                }
                roomObj.setTravelList(parseTravel(roomElement.getElementsByTagName("travel")));
                roomObj.setUnavailableList(parseUnavailableTime(roomElement.getElementsByTagName("unavailable")));
                roomMap.put(roomObj.getId(), roomObj);
            }
        }
        return roomMap;
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


    public static ArrayList<Penalty<Room>> parseRoomClass(NodeList roomClassList) {
        ArrayList<Penalty<Room>> roomClassArr = new ArrayList<>();
        for (int i = 0; i < roomClassList.getLength(); ++i) {
            Node room = roomClassList.item(i);
            if (room.getNodeType() == Node.ELEMENT_NODE) {
                Element roomElement = (Element) room;
                Penalty<Room> obj = new Penalty<>();
                String index = roomElement.getAttribute("id");
                Room roomObj = Factory.getProblem().getRooms().get(index);
                obj.setEntity(roomObj);
                obj.setPenalty(Integer.parseInt(roomElement.getAttribute("penalty")));
                roomClassArr.add(obj);
            }
        }
        return roomClassArr;
    }
}
