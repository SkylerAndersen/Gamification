package WindowStates;

import ApplicationDefaults.BevelPanel;
import ApplicationDefaults.WindowState;
import ApplicationDefaults.WindowStateEvent;
import DataStructures.FileHandler;
import DataStructures.PlayerData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;
import java.security.MessageDigest; // Used for computing message digests (a fixed length hash value)
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate; // for login rewards

import java.io.*;

public class Login extends WindowState {
    private FileHandler fileHandler;

    public Login () {
        super(WindowStateName.LOGIN);
        super.setStartupScreen(true); // Initialize the app to this screen

        setTitle("Login");

        getContentPane().setLayout(new GridBagLayout()); // GridBagLayout in order to have more precision when placing components

        // LOGIN SCREEN

        getContentPane().setBackground(new Color(223, 223, 223));
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create the styled panel container
        BevelPanel loginBox = new BevelPanel();
        loginBox.setLayout(new GridBagLayout());
        loginBox.setRoundTop(true);
        loginBox.setRoundBottom(true);
        loginBox.setRounding(30);
        loginBox.setBackground(new Color(201, 201, 201));
        loginBox.setOpaque(true);
        loginBox.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Wrap the loginBox inside a container to center it
        JPanel outerWrapper = new JPanel(new GridBagLayout());
        outerWrapper.setBackground(new Color(223, 223, 223));
        outerWrapper.add(loginBox);

        // Set the main window's content
        getContentPane().removeAll(); // Just in case
        getContentPane().add(outerWrapper);

        Font labelFont = new Font("Helvetica", Font.PLAIN, 18);
        Color textColor = new Color(124, 124, 124);
        Color inputBg = new Color(220, 220, 220);

        JLabel title = new JLabel("Welcome to Questify!");
        title.setFont(new Font("Helvetica", Font.PLAIN, 48));
        title.setForeground(textColor);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginBox.add(title, gbc);

        // Username Label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont);
        usernameLabel.setForeground(textColor);
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy++;
        loginBox.add(usernameLabel, gbc);

        // Username Field
        JTextField usernameField = new JTextField();
        usernameField.setFont(labelFont);
        usernameField.setBackground(inputBg);
        usernameField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gbc.gridy++;
        loginBox.add(usernameField, gbc);

        // Spacer
        gbc.gridy++;
        loginBox.add(Box.createVerticalStrut(20), gbc);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(textColor);
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy++;
        loginBox.add(passwordLabel, gbc);

        // Password Field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(labelFont);
        passwordField.setBackground(inputBg);
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gbc.gridy++;
        loginBox.add(passwordField, gbc);

        // Login Button
        JButton loginButton = new JButton("Enter");
        loginButton.setFont(labelFont);
        loginButton.setBackground(new Color(168, 168, 168));
        loginButton.setForeground(textColor);
        loginButton.setFocusPainted(false);
        gbc.gridy++;
        loginBox.add(loginButton, gbc);

        // Setup behavior telling the app how to switch between screens
        setCloseEvent(WindowStateEvent.SWITCH_STATE);
        setNextWindow(WindowStateName.MISSION_SELECT);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = new String(usernameField.getText());
                String password = new String(passwordField.getPassword());
                // Ensure that something is entered for both the username and password
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(getContentPane(), "Both fields are required.");
                    return;
                }
                // Authenticate using the UserDatabase class
                boolean isAuthenticated = UserDatabase.authenticate(username, password);
                if (isAuthenticated) {
                    PlayerData.currentUser = username; // when authenticated, tells player data what data to call
                    PlayerData.load(fileHandler);

                    String today = LocalDate.now().toString();

                    if (!today.equals(PlayerData.lastLoginDate)){ // Check if the player already received today's bonus
                        PlayerData.xp += 50; // Give 50 XP
                        if (PlayerData.xp >= 1000) {
                            PlayerData.level++;
                            PlayerData.xp -= 1000;
                        }
                        // Pop up notification for the login reward
                        JOptionPane.showMessageDialog(
                                getContentPane(),
                                "Daily Login Bonus!\nYou have gained 50 XP!",
                                "Login Reward",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }

                    PlayerData.lastLoginDate = today; // Update the last login date

                    close(); // If the username and password are both valid, then the next screen is opened
                } else {
                    JOptionPane.showMessageDialog(getContentPane(), "Invalid username or password."); // Error message if the username and/or password are invalid
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 6;
        loginBox.add(loginButton, gbc);

        // SIGN UP SCREEN

        labelFont = new Font("Helvetica", Font.PLAIN, 18);
        textColor = new Color(124, 124, 124);
        inputBg = new Color(220, 220, 220);

        // Signup Username Field
        JTextField signUpUsernameField = new JTextField();
        signUpUsernameField.setFont(labelFont);
        signUpUsernameField.setBackground(inputBg);
        signUpUsernameField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        signUpUsernameField.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginBox.add(signUpUsernameField, gbc);
        signUpUsernameField.setVisible(false);

        // Signup Password Field
        JTextField signUpPasswordField = new JTextField();
        signUpPasswordField.setFont(labelFont);
        signUpPasswordField.setBackground(inputBg);
        signUpPasswordField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        signUpPasswordField.setForeground(textColor);
        gbc.gridy = 5;
        loginBox.add(signUpPasswordField, gbc);
        signUpPasswordField.setVisible(false);

        // Signup Button
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFont(labelFont);
        signUpButton.setBackground(new Color(168, 168, 168));
        signUpButton.setForeground(textColor);
        signUpButton.setFocusPainted(false);
        gbc.gridy = 6;
        loginBox.add(signUpButton, gbc);
        signUpButton.setVisible(false);

        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newUsername = new String(signUpUsernameField.getText());
                String newPassword = new String(signUpPasswordField.getText());
                UserDatabase.addEntry(newUsername, newPassword); // Adds an entry to the hash map in UserDatabase used to store username and passwords
                // Modifies visibility to switch from sign up screen back to log in screen

                PlayerData.currentUser = newUsername;
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
        loginBox.add(toSignupButton, gbc);
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
    }

    @Override
    public void onEscapePressed () {
        setCloseEvent(WindowStateEvent.CLOSE_APP);
        close();
    }

    @Override
    public void save(FileHandler fileHandler) {
        PlayerData.save(fileHandler);
    }

    @Override
    public void load(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

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
        private static final String DATABASE_FILE = Paths.get(System.getProperty("user.dir"),"resources",
                "user_database.bin").toString();
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

}
