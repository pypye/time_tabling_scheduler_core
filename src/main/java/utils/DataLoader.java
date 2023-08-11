//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package utils;

import entities.rooms.Room;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;

public class DataLoader {
    public DataLoader(String path) {
        try {
            parse(path);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    private static void parse(String path) throws Exception {
        File inputFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        List<Room> roomArr = DataParser.parseRoom(doc.getElementsByTagName("rooms").item(0).getChildNodes());
        System.out.println(((Room)roomArr.get(0)).getUnavailableList());
    }
}
