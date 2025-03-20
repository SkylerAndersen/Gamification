package WindowStates;

import ApplicationDefaults.WindowState;
import ApplicationDefaults.WindowStateEvent;
import DataStructures.FileHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest; // Used for computing message digests (a fixed length hash value)
import java.security.NoSuchAlgorithmException;

import java.io.*;

// NOTES: Will move the user_database.bin file to another location and may look into encryption options

public class Login extends WindowState {

    public Login () {
        super(WindowStateName.LOGIN);
        super.setStartupScreen(false); // Initialize the app to this screen

        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000); // Set initial size when opened

        frame.setLayout(new GridBagLayout()); // GridBagLayout in order to have more precision when placing components
        GridBagConstraints gbc = new GridBagConstraints();

        // LOGIN SCREEN

        Font titleFont = new Font("Helvetica", Font.BOLD, 30);

        JLabel title = new JLabel("Welcome!"); // Title label
        title.setFont(titleFont);
        title.setBorder(new EmptyBorder(0, 0, 100, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        title.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(title, gbc);

        Font labelFont = new Font("Helvetica", Font.ITALIC, 15);

        JLabel usernameLabel = new JLabel("Username:"); // Login username label
        usernameLabel.setFont(labelFont);
        usernameLabel.setBorder(new EmptyBorder(0, 0, 5, 0));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(); // Login username text field
        usernameField.setColumns(30);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(usernameField, gbc);

        JLabel empty = new JLabel(" "); // Spacing between the username section and password section
        empty.setBorder(new EmptyBorder(5, 0, 5, 0));
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(empty, gbc);

        JLabel passwordLabel = new JLabel("Password:"); // Login password label
        passwordLabel.setFont(labelFont);
        passwordLabel.setBorder(new EmptyBorder(0, 0, 5, 0));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(30); // Login password field
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        passwordField.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(passwordField, gbc);

        // Setup behavior telling the app how to switch between screens
        setCloseEvent(WindowStateEvent.SWITCH_STATE);
        setNextWindow(WindowStateName.MISSION_SELECT);

        JButton loginButton = new JButton("Enter"); // Login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = new String(usernameField.getText());
                String password = new String(passwordField.getPassword());
                // Ensure that something is entered for both the username and password
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Both fields are required.");
                    return;
                }
                // Authenticate using the UserDatabase class
                boolean isAuthenticated = UserDatabase.authenticate(username, password);
                if (isAuthenticated) {
                    close(); // If the username and password are both valid, then the next screen is opened
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password."); // Error message if the username and/or password are invalid
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 6;
        frame.add(loginButton, gbc);

        // SIGN UP SCREEN

        JTextField signUpUsernameField = new JTextField(); // Signup username text field
        signUpUsernameField.setColumns(30);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        signUpUsernameField.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(signUpUsernameField, gbc);
        signUpUsernameField.setVisible(false); // Initially invisible until the to-sign-up button is pressed

        JTextField signUpPasswordField = new JTextField(); // Signup password text field
        signUpPasswordField.setColumns(30);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        signUpPasswordField.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(signUpPasswordField, gbc);
        signUpPasswordField.setVisible(false); // Initially invisible until the to-sign-up button is pressed

        JButton signUpButton = new JButton("Sign Up"); // Signup button
        gbc.gridx = 0;
        gbc.gridy = 6;
        frame.add(signUpButton, gbc);
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newUsername = new String(signUpUsernameField.getText());
                String newPassword = new String(signUpPasswordField.getText());
                UserDatabase.addEntry(newUsername, newPassword); // Adds an entry to the hash map in UserDatabase used to store username and passwords
                // Modifies visibility to switch from sign up screen back to log in screen
                usernameField.setVisible(true);
                passwordField.setVisible(true);
                loginButton.setVisible(true);
                signUpUsernameField.setVisible(false);
                signUpPasswordField.setVisible(false);
                signUpButton.setVisible(false);
            }
        });
        signUpButton.setVisible(false); // Initially invisible until the to-sign-up button is pressed

        JButton toSignupButton = new JButton("Don't have an account? Sign up now."); // Sign up trigger button
        gbc.gridx = 0;
        gbc.gridy = 7;
        frame.add(toSignupButton, gbc);
        toSignupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Modifies visibility to switch from  log in screen to sign up screen
                toSignupButton.setVisible(false);
                usernameField.setVisible(false);
                passwordField.setVisible(false);
                loginButton.setVisible(false);
                signUpUsernameField.setVisible(true);
                signUpPasswordField.setVisible(true);
                signUpButton.setVisible(true);
            }
        });
        frame.setVisible(true);
    }

    @Override
    public void save(FileHandler fileHandler) {
        // saving some data
        fileHandler.save("4",10);
    }

    @Override
    public void load(FileHandler fileHandler) {}

    public class PasswordUtils { // Class used to hash password using SHA-256
        public static String hashPassword(String password) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256"); // Initializes a MessageDigest object with the SHA-256 hashing algorithm
                byte[] hashedBytes = md.digest(password.getBytes()); // Produces a byte array representing the hashed result of the password using SHA-256
                // Converting byte[] to hex format
                StringBuilder hexString = new StringBuilder();
                for (byte b : hashedBytes) {
                    hexString.append(String.format("%02x", b));
                }
                return hexString.toString();
            } catch (NoSuchAlgorithmException e) { // Thrown if the SHA-256 algorithm is not available on the system
                throw new RuntimeException("Error hashing password", e);
            }
        }
    }

    public class UserDatabase {
        private static final String DATABASE_FILE = "/Users/mirandabecker/Desktop/SDSU Documents/Spring 2025/CS 250/Gamification/Project Files/Gamification/src/WindowStates/user_database.bin";  // The binary file to store user data
        public static void addEntry(String newUsername, String newPassword) { // Add a new entry to the binary file
            // Check if the user already exists by reading the existing database
            if (containsUser(newUsername)) {
                throw new RuntimeException("User already exists.");
            }
            String hashedPassword = PasswordUtils.hashPassword(newPassword); // Hash the password before storing it
            try (RandomAccessFile raf = new RandomAccessFile(DATABASE_FILE, "rw")) {
                raf.seek(raf.length()); // Move the file pointer to the end of the file to add a new entry
                // Write username length and username bytes
                raf.writeInt(newUsername.length());
                raf.writeBytes(newUsername);
                // Write password length and hashed password bytes
                raf.writeInt(hashedPassword.length());
                raf.writeBytes(hashedPassword);
                System.out.println("User added successfully."); // Update for development
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public static boolean authenticate(String username, String password) { // Authenticate user by reading the binary file
            try (RandomAccessFile raf = new RandomAccessFile(DATABASE_FILE, "r")) {
                long fileLength = raf.length();
                while (raf.getFilePointer() < fileLength) { // Iterates through the file
                    // Read username length and username bytes
                    int usernameLength = raf.readInt();
                    byte[] usernameBytes = new byte[usernameLength]; // Creates a new byte list for reading the username
                    raf.read(usernameBytes); // Reads the username bytes from the file and stores them in the usernameBytes array
                    String storedUsername = new String(usernameBytes); // Converts raw byte data back into a String representation
                    // Read password length and hashed password bytes
                    int passwordLength = raf.readInt();
                    byte[] passwordBytes = new byte[passwordLength];
                    raf.read(passwordBytes);
                    String storedHash = new String(passwordBytes);
                    // Check if the username matches, and if so, verify the password
                    if (storedUsername.equals(username)) {
                        String inputHash = PasswordUtils.hashPassword(password);
                        return storedHash.equals(inputHash); // Verify password hash match
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false; // User not found
        }

        private static boolean containsUser(String username) { // Helper method to check if a user exists in the binary file
            try (RandomAccessFile raf = new RandomAccessFile(DATABASE_FILE, "r")) {
                long fileLength = raf.length();
                while (raf.getFilePointer() < fileLength) {
                    // Read username length and username bytes
                    int usernameLength = raf.readInt();
                    byte[] usernameBytes = new byte[usernameLength];
                    raf.read(usernameBytes);
                    String storedUsername = new String(usernameBytes);
                    // Skip the password part (we don't need it here)
                    int passwordLength = raf.readInt();
                    raf.skipBytes(passwordLength);
                    // Check if the username matches
                    if (storedUsername.equals(username)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false; // User not found
        }
    }

    // Discarded code: (ignore)
    /*
    public class UserDatabase {
        private static Map<String, String> userDatabase = new HashMap<>(); // A static map that maps a String to another String
        public static void addEntry(String newUsername, String newPassword) { // Used to add username and password entries to the UserDatabase map
            if (userDatabase.containsKey(newUsername)) {
                throw new RuntimeException("User already exists.");
            } else {
                userDatabase.put(newUsername, PasswordUtils.hashPassword(newPassword));
            }
        }
        public static boolean authenticate(String username, String password) { // Verification Logic
            if (userDatabase.containsKey(username)) { // Checks if the user exists
                String storedHash = userDatabase.get(username);
                String inputHash = PasswordUtils.hashPassword(password);
                return storedHash.equals(inputHash); // Checks if the password matches
            }
            return false;  // Username not found
        }
    }
    */
}