package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import database.DatabaseAccess;

/**
 * Created by hungdo on 12/3/16.
 */

public class Report {
    private DatabaseAccess data;
    private String projectName;
    private int numApplicant;
    private double rate;
    private List<String> topMajors;
    private List<Apply> applies;
    private Map<String, Integer> treeMapMajors;

    public Report(String projectName) {
        data = DatabaseAccess.getDatabaseAccess();
        applies = data.getListApplyByProjectname(projectName);
        this.projectName = projectName;
        numApplicant = applies.size();
        HashMap<String, Integer> mapMajors = new HashMap<>();
        topMajors = new ArrayList<>();

        int accepted = 0;
        for (Apply apply: applies) {
            if (apply.getStatus().equals("accepted")) {
                accepted += 1;
            }
            User u = data.getUserByEmail(apply.getEmail());
            String major = u.getMajor();
            if (!mapMajors.containsKey(major)) {
                mapMajors.put(major, 1);
            } else {
                mapMajors.put(major, mapMajors.get(major) + 1);
            }
        }

        treeMapMajors = data.sortMapByValue(mapMajors);
        int i = 0;
        for (Map.Entry<String, Integer> m: treeMapMajors.entrySet()) {
            if (i < 3) {
                topMajors.add(m.getKey());
            } else {
                break;
            }
            i++;
        }
        this.rate = ((double) accepted) * 100 / numApplicant;


    }

    public String getProjectName() {
        return projectName;
    }
    public int getNumApplicant() {
        return numApplicant;
    }
    public double getRate() {
        return rate;
    }
    public List<String> getMajors() {
        return topMajors;
    }
}
