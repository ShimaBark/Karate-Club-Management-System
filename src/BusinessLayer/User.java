package BusinessLayer;

import DataAccessLayer.UserData;

import java.util.ArrayList;

public class User extends Person {
   private enum enMode{AddNewMode , UpdateMode};
    enum enPermission {

        // Member permissions
        ShowMembers(1),
        AddNewMember(2),
        UpdateMember(4),
        DeleteMember(8),

        // Instructor permissions
        ShowInstructors(16),
        AddNewInstructor(32),
        UpdateInstructor(64),
        DeleteInstructor(128),

        // User permissions
        ShowUsers(256),
        AddNewUser(512),
        UpdateUser(1024),
        DeleteUser(2048);

        private final int value;

        enPermission(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    private String username;
    private String password;
    private int userPermission;
    private enMode mode;
    private boolean markForDelete = false;

    public User(String username, String password, int userPermission) {
        this.username = username;
        this.password = password;
        this.userPermission = userPermission;
        mode= enMode.UpdateMode;

    }

    public User() {
        this.username = "";
        this.password = "";
        this.userPermission = 0;
        mode=enMode.AddNewMode;

    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public void setUserPermission(int userPermission) {
        this.userPermission = userPermission;
    }
    public int getUserPermission() {
        return userPermission;
    }

    // Checks whether the user has a specific permission.
    private boolean hasPermission(enPermission permission) {
        return (userPermission & permission.getValue())
                == permission.getValue();
    }

    public boolean canShowMembers() {
        return hasPermission(enPermission.ShowMembers);
    }

    public boolean canAddMember() {
        return hasPermission(enPermission.AddNewMember);
    }

    public boolean canUpdateMember() {
        return hasPermission(enPermission.UpdateMember);
    }

    public boolean canDeleteMember() {
        return hasPermission(enPermission.DeleteMember);
    }
    public boolean canShowInstructors() {
        return hasPermission(enPermission.ShowInstructors);
    }
    public boolean canAddInstructor() {
        return hasPermission(enPermission.AddNewInstructor);
    }
    public boolean canUpdateInstructor() {
        return hasPermission(enPermission.UpdateInstructor);
    }
    public boolean canDeleteInstructor() {
        return hasPermission(enPermission.DeleteInstructor);
    }
    public boolean canShowUsers() {
        return hasPermission(enPermission.ShowUsers);
    }

    public boolean canAddUser() {
        return hasPermission(enPermission.AddNewUser);
    }

    public boolean canUpdateUser() {
        return hasPermission(enPermission.UpdateUser);
    }

    public boolean canDeleteUser() {
        return hasPermission(enPermission.DeleteUser);
    }
    // add permission
    public void allowShowMembers() {
        userPermission |= enPermission.ShowMembers.getValue();
    }

    public void allowAddMember() {
        userPermission |= enPermission.AddNewMember.getValue();
    }

    public void allowUpdateMember() {
        userPermission |= enPermission.UpdateMember.getValue();
    }

    public void allowDeleteMember() {
        userPermission |= enPermission.DeleteMember.getValue();
    }
    public void allowShowInstructors() {
        userPermission |= enPermission.ShowInstructors.getValue();
    }

    public void allowAddInstructor() {
        userPermission |= enPermission.AddNewInstructor.getValue();
    }
    public void allowUpdateInstructor() {
        userPermission |= enPermission.UpdateInstructor.getValue();
    }

    public void allowDeleteInstructor() {
        userPermission |= enPermission.DeleteInstructor.getValue();
    }
    public void allowShowUsers() {
        userPermission |= enPermission.ShowUsers.getValue();
    }

    public void allowAddUser() {
        userPermission |= enPermission.AddNewUser.getValue();
    }
    public void allowUpdateUser() {
        userPermission |= enPermission.UpdateUser.getValue();
    }

    public void allowDeleteUser() {
        userPermission |= enPermission.DeleteUser.getValue();
    }
    // Add / Update / Delete user

    private boolean addNewUser() {
        return UserData.saveNewUser(this);
    }
    private boolean updateUser() {

        ArrayList<User> users = UserData.getAllUsers();

        for (User user : users) {

            if (user.getUsername().equals(this.username)) {

                user.setPassword(this.password);
                user.setUserPermission(this.userPermission);

                return UserData.resaveAllUsers(users);
            }
        }

        return false;
    }
    public boolean save() {

        switch (mode) {

            case AddNewMode -> {

                if (addNewUser()) {

                    this.mode = enMode.UpdateMode;
                    return true;
                }
            }

            case UpdateMode -> {
                return updateUser();
            }
        }

        return false;
    }
    public boolean deleteUser() {

        ArrayList<User> users = UserData.getAllUsers();

        for (User user : users) {

            if (user.getUsername().equals(this.username)) {

                user.markForDelete = true;

                return UserData.resaveAllUsers(users);
            }
        }

        return false;
    }
    public boolean isMarkForDelete() {
        return markForDelete;
    }
    public static User find(String username) {

        ArrayList<User> users = UserData.getAllUsers();

        for (User user : users) {

            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    public static ArrayList<User> getAllUsers() {
        return UserData.getAllUsers();
    }

    public static boolean isExists(String username) {

        ArrayList<User> users = getAllUsers();

        for (User user : users) {

            if (user.getUsername()
                    .trim()
                    .equalsIgnoreCase(username.trim())) {

                return true;
            }
        }

        return false;
    }

    public void clearPermissions() {
        userPermission = 0;
    }

    public static User login(String username, String password) {

        User user = find(username);

        if (user == null) {
            return null;
        }

        if (user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }
}
