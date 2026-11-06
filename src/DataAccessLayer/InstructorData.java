package DataAccessLayer;

import BusinessLayer.Instructor;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class InstructorData {

    private static final File file = new File("InstructorsFile.txt");

    private static String joinInstructorInfo(
            Instructor instructor, String delimiter) {

        return instructor.getInstructorID() + delimiter
                + instructor.getName() + delimiter
                + instructor.getAddress() + delimiter
                + instructor.getContactInfo() + delimiter
                + instructor.getQualification() + delimiter
                + instructor.getTeachingBelt();
    }

    private static Instructor split(
            String instructorInfo, String separator) {

        String[] infoToken = instructorInfo.split(separator);

        Instructor instructor = new Instructor();

        instructor.setInstructorID(Integer.parseInt(infoToken[0]));
        instructor.setName(infoToken[1]);
        instructor.setAddress(infoToken[2]);
        instructor.setContactInfo(infoToken[3]);
        instructor.setQualification(infoToken[4]);
        instructor.setTeachingBelt(infoToken[5]);

        return instructor;
    }

    public static boolean saveNewInstructor(Instructor instructor) {
        try (PrintWriter writer =
                     new PrintWriter(new FileWriter(file, true))) {

            writer.println(
                    joinInstructorInfo(instructor, "##//##")
            );

            return true;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static boolean resaveAllInstructors(
            ArrayList<Instructor> instructors) {

        try (PrintWriter writer = new PrintWriter(file)) {

            for (Instructor instructor : instructors) {
                if (!instructor.isMarkForDelete()) {
                    writer.println(
                            joinInstructorInfo(instructor, "##//##")
                    );
                }
            }

            return true;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static ArrayList<Instructor> getAllInstructors() {

        ArrayList<Instructor> instructors = new ArrayList<>();

        try (Scanner input = new Scanner(file)) {

            while (input.hasNextLine()) {
                String line = input.nextLine();

                instructors.add(
                        split(line, "##//##")
                );
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return instructors;
    }
}