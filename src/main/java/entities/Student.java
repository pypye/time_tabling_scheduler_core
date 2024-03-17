package entities;

import com.google.ortools.sat.Literal;
import core.solver.Factory;
import entities.courses.Class;
import entities.courses.Config;
import entities.courses.Course;
import entities.courses.Subpart;
import utils.StringFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student {
    private String id;
    private ArrayList<String> courseList;

    private final Map<Class, Literal> classes = new HashMap<>();

    public Student() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<String> courseList) {
        this.courseList = courseList;
    }


    public Map<Class, Literal> getClasses() {
        return classes;
    }

    @Override
    public String toString() {
        return StringFormatter.printObject(this);
    }

    public void init() {
        for (String courseId : this.courseList) {
            Course course = Factory.getProblem().getCourses().get(courseId);
            ArrayList<Literal> configLit = new ArrayList<>();
            for (Config config : course.getConfigList().values()) {
                Literal scf = Factory.getModel().newBoolVar("student_" + this.id + "_config_" + config.getId());
                configLit.add(scf);

                for (Subpart subpart : config.getSubpartList().values()) {
                    ArrayList<Literal> classLit = new ArrayList<>();
                    for (Class clazz : subpart.getClassList().values()) {
                        Literal scl = Factory.getModel().newBoolVar("student_" + this.id + "_class_" + clazz.getId());
                        classLit.add(scl);
                        this.classes.put(clazz, scl);
                        Factory.getModel().addImplication(scl, scf); // scl => scf

                        if (clazz.getParentId() != null) {
                            Class parent = Factory.getProblem().getClasses().get(clazz.getParentId());
                            Factory.getModel().addImplication(scl, this.classes.get(parent)); // scl => parent_scl
                        }
                    }
                    Factory.getModel().addExactlyOne(classLit.toArray(new Literal[0])); // one class per subpart
                }

            }
            Factory.getModel().addExactlyOne(configLit.toArray(new Literal[0])); // one config per course
        }
    }
}
