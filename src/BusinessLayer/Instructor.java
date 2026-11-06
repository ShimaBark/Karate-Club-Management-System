package BusinessLayer;

import DataAccessLayer.InstructorData;
import java.util.ArrayList;

public class Instructor extends Person {

    private enum enMode {
        UpdateMode,
        AddNewMode
    }
    private int instructorID;
    private String qualification;
    private static final int MAX_STUDENTS = 15;
    private String teachingBelt;
    private enMode mode;
    private boolean markForDelete = false;

    public Instructor() {
        super("", "", "");
        this.instructorID = -1;
        this.qualification = "";
        this.mode = enMode.AddNewMode;
        this.teachingBelt="";
    }

   /* public Instructor(String name, String address,
                      String contactInfo, String qualification) {
        super(name, address, contactInfo);


        this.instructorID = counter;
        this.qualification = qualification;
        this.mode = enMode.AddNewMode;
    }*/

    public int getInstructorID() {
        return instructorID;
    }

    public String getQualification() {
        return qualification;
    }

    public void setInstructorID(int instructorID) {
        this.instructorID = instructorID;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getTeachingBelt() {
        return teachingBelt;
    }

    public void setTeachingBelt(String teachingBelt) {
        this.teachingBelt = teachingBelt;
    }

    public boolean isMarkForDelete() {
        return markForDelete;
    }

    private boolean addNewInstructor() {
        return InstructorData.saveNewInstructor(this);
    }

    private boolean updateInstructor() {
        ArrayList<Instructor> instructors =
                InstructorData.getAllInstructors();

        for (Instructor instructor : instructors) {
            if (instructor.instructorID == this.instructorID) {
                instructor.setName(this.getName());
                instructor.setAddress(this.getAddress());
                instructor.setContactInfo(this.getContactInfo());
                instructor.qualification = this.qualification;
                instructor.teachingBelt= this.teachingBelt;


                return InstructorData.resaveAllInstructors(instructors);
            }
        }

        return false;
    }

    public boolean deleteInstructor() {

        if (getNumberOfStudents() > 0) {
            return false;
        }
        ArrayList<Instructor> instructors =
                InstructorData.getAllInstructors();

        for (Instructor current : instructors) {
            if (current.instructorID == this.instructorID) {
                current.markForDelete = true;
                return InstructorData.resaveAllInstructors(instructors);
            }
        }

        return false;
    }

    public boolean save() {
        switch (mode) {
            case AddNewMode -> {
                if (addNewInstructor()) {
                    mode = enMode.UpdateMode;
                    return true;
                }
            }

            case UpdateMode -> {
                return updateInstructor();
            }
        }

        return false;
    }

    public static Instructor find(int instructorID) {
        ArrayList<Instructor> instructors =
                InstructorData.getAllInstructors();

        for (Instructor instructor : instructors) {
            if (instructor.instructorID == instructorID) {
                return instructor;
            }
        }

        return null;
    }
    public static Instructor find(String instructorName) {
        ArrayList<Instructor> instructors =
                InstructorData.getAllInstructors();

        for (Instructor instructor : instructors) {
            if (instructor.getName().equalsIgnoreCase(instructorName)) {
                return instructor;
            }
        }

        return null;
    }

    public static ArrayList<Instructor> getAllInstructors() {

        return InstructorData.getAllInstructors();
    }

    public static boolean isExists(int instructorID) {
        return find(instructorID) != null;
    }
    public int getNumberOfStudents() {

        int count = 0;

        for (Member member : Member.getAllMembers()) {

            if (member.getInstructorID() == this.instructorID) {
                count++;
            }
        }

        return count;
    }

    public boolean hasAvailableSpace() {

        return getNumberOfStudents() < MAX_STUDENTS;
    }

    public static ArrayList<Instructor> getInstructorsByBelt(String belt) {

        ArrayList<Instructor> matchedInstructors =
                new ArrayList<>();

        ArrayList<Instructor> instructors =
                getAllInstructors();

        for (Instructor instructor : instructors) {

            if (instructor.getTeachingBelt()
                    .equalsIgnoreCase(belt)) {

                matchedInstructors.add(instructor);
            }
        }

        return matchedInstructors;
    }

}