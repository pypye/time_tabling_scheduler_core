package utils;

import entities.Time;

import java.util.ArrayList;
import java.util.List;

public class MergeUnavailableRoom {
    public static List<Time> mergeWithDays(List<Time> currentList) {
        List<Time> unavailableList = new ArrayList<>();
        boolean[] isVisited = new boolean[currentList.size()];
        for (int i = 0; i < currentList.size(); i++) {
            if (!isVisited[i]) {
                Time current = new Time();
                current.setStart(currentList.get(i).getStart());
                current.setLength(currentList.get(i).getLength());
                current.setWeek(currentList.get(i).getWeek());
                current.setDays(currentList.get(i).getDays());
                for (int j = i + 1; j < currentList.size(); j++) {
                    if (!isVisited[j] && currentList.get(i).getStart() == currentList.get(j).getStart() && currentList.get(i).getLength() == currentList.get(j).getLength() && currentList.get(i).getDays().equals(currentList.get(j).getDays())) {
                        current = merge(current, currentList.get(j));
                        isVisited[j] = true;
                    }
                }
                unavailableList.add(current);
                isVisited[i] = true;
            }
        }
        return unavailableList;
    }

    public static List<Time> mergeWithWeeks(List<Time> currentList) {
        List<Time> unavailableList = new ArrayList<>();
        boolean[] isVisited = new boolean[currentList.size()];
        for (int i = 0; i < currentList.size(); i++) {
            if (!isVisited[i]) {
                Time current = new Time();
                current.setStart(currentList.get(i).getStart());
                current.setLength(currentList.get(i).getLength());
                current.setWeek(currentList.get(i).getWeek());
                current.setDays(currentList.get(i).getDays());
                for (int j = i + 1; j < currentList.size(); j++) {
                    if (currentList.get(i).getStart() == currentList.get(j).getStart() && currentList.get(i).getLength() == currentList.get(j).getLength() && currentList.get(i).getWeek().equals(currentList.get(j).getWeek())) {
                        current = merge(current, currentList.get(j));
                        isVisited[j] = true;
                    }
                }
                unavailableList.add(current);
                isVisited[i] = true;
            }
        }
        return unavailableList;
    }

    private static Time merge(Time a, Time b) {
        Time result = new Time();
        result.setStart(a.getStart());
        result.setLength(a.getLength());
        StringBuilder newWk = new StringBuilder();
        for (int i = 0; i < a.getWeek().length(); i++) {
            if (a.getWeek().charAt(i) == '1' || b.getWeek().charAt(i) == '1') {
                newWk.append('1');
            } else {
                newWk.append('0');
            }
        }
        result.setWeek(newWk.toString());
        StringBuilder newDay = new StringBuilder();
        for (int i = 0; i < a.getDays().length(); i++) {
            if (a.getDays().charAt(i) == '1' || b.getDays().charAt(i) == '1') {
                newDay.append('1');
            } else {
                newDay.append('0');
            }
        }
        result.setDays(newDay.toString());
        return result;
    }
}
