package DataAccessLayer;

import BusinessLayer.User;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class UserData {

    private static final File file =
            new File("UsersFile.txt");


    // Joins the user's information into one line to save it in the file.
    private static String joinUserInfo(User user, String delimiter) {

        return user.getUsername()
                + delimiter
                + user.getPassword()
                + delimiter
                + user.getUserPermission();
    }


    // Converts a line from the file into a User object.
    private static User split(String userInfo, String separator) {

        String[] infoToken = userInfo.split(separator);

        return new User(
                infoToken[0],
                infoToken[1],
                Integer.parseInt(infoToken[2])
        );
    }


    // Saves a new user at the end of the file.
    public static boolean saveNewUser(User user) {

        try (PrintWriter writer =
                     new PrintWriter(new FileWriter(file, true))) {

            writer.println(
                    joinUserInfo(user, "##//##")
            );

            return true;

        } catch (Exception ex) {

            System.out.println(ex.getMessage());
            return false;
        }
    }


    // Rewrites all users in the file.
    public static boolean resaveAllUsers(ArrayList<User> users) {

        try (PrintWriter writer =
                     new PrintWriter(file)) {

            for (User user : users) {


                    if (!user.isMarkForDelete()) {

                        writer.println(
                                joinUserInfo(user, "##//##")
                        );
                    }

            }

            return true;

        } catch (Exception ex) {

            System.out.println(ex.getMessage());
            return false;
        }
    }


    // Reads all users from the file.
    public static ArrayList<User> getAllUsers() {

        ArrayList<User> users = new ArrayList<>();

        try (Scanner input = new Scanner(file)) {

            while (input.hasNextLine()) {

                String line = input.nextLine();

                users.add(
                        split(line, "##//##")
                );
            }

        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }

        return users;
    }
}