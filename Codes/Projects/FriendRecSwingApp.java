package Codes.Projects;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class FriendRecSwingApp {
    private Graph graph = new Graph();
    private DefaultListModel<String> outputListModel = new DefaultListModel<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FriendRecSwingApp::new);
    }

    public FriendRecSwingApp() {
        JFrame frame = new JFrame("Friend Recommendation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(760, 580);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(255, 255, 255));

        Font uiFont = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel userPanel = new JPanel(new FlowLayout());
        userPanel.setBackground(new Color(255, 255, 255));
        JTextField nameField = new JTextField(10);
        JTextField ageField = new JTextField(5);
        JTextField cityField = new JTextField(10);
        JButton addUserBtn = new JButton("Add User");
        userPanel.add(new JLabel("Name:"));
        userPanel.add(nameField);
        userPanel.add(new JLabel("Age:"));
        userPanel.add(ageField);
        userPanel.add(new JLabel("Location:"));
        userPanel.add(cityField);
        userPanel.add(addUserBtn);

        JPanel friendPanel = new JPanel(new FlowLayout());
        friendPanel.setBackground(new Color(255, 255, 255));
        JTextField user1Field = new JTextField(10);
        JTextField user2Field = new JTextField(10);
        JButton addFriendBtn = new JButton("Add Friendship");
        friendPanel.add(new JLabel("User 1:"));
        friendPanel.add(user1Field);
        friendPanel.add(new JLabel("User 2:"));
        friendPanel.add(user2Field);
        friendPanel.add(addFriendBtn);

        JPanel suggestPanel = new JPanel(new FlowLayout());
        suggestPanel.setBackground(new Color(255, 255, 255));
        JTextField suggestField = new JTextField(10);
        JCheckBox agePriority = new JCheckBox("Prioritize Age");
        JCheckBox locPriority = new JCheckBox("Prioritize Location");
        JButton suggestBtn = new JButton("Suggest Friends");
        suggestPanel.add(new JLabel("Suggest for:"));
        suggestPanel.add(suggestField);
        suggestPanel.add(agePriority);
        suggestPanel.add(locPriority);
        suggestPanel.add(suggestBtn);

        JList<String> outputList = new JList<>(outputListModel);
        JScrollPane scrollPane = new JScrollPane(outputList);
        scrollPane.setPreferredSize(new Dimension(720, 200));
        outputList.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputList.setBackground(new Color(248, 249, 250));

        JPanel extraPanel = new JPanel(new FlowLayout());
        extraPanel.setBackground(new Color(248, 249, 250));

        JButton listUsersBtn = new JButton("Show All Users");
        JButton listFriendsBtn = new JButton("Show Friends");
        JTextField friendLookupField = new JTextField(10);

        JButton deleteUserBtn = new JButton("Delete User");
        JButton deleteFriendshipBtn = new JButton("Delete Friendship");
        JTextField delUserField = new JTextField(10);
        JTextField delFriend1 = new JTextField(10);
        JTextField delFriend2 = new JTextField(10);

        for (JButton btn : new JButton[]{addUserBtn, addFriendBtn, suggestBtn, listUsersBtn, listFriendsBtn, deleteUserBtn, deleteFriendshipBtn}) {
            btn.setBackground(new Color(204, 229, 255));
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }

        nameField.setFont(uiFont);
        ageField.setFont(uiFont);
        cityField.setFont(uiFont);
        user1Field.setFont(uiFont);
        user2Field.setFont(uiFont);
        suggestField.setFont(uiFont);
        friendLookupField.setFont(uiFont);
        delUserField.setFont(uiFont);
        delFriend1.setFont(uiFont);
        delFriend2.setFont(uiFont);

        extraPanel.add(listUsersBtn);
        extraPanel.add(new JLabel("User:"));
        extraPanel.add(friendLookupField);
        extraPanel.add(listFriendsBtn);
        extraPanel.add(new JLabel("Del User:"));
        extraPanel.add(delUserField);
        extraPanel.add(deleteUserBtn);
        extraPanel.add(new JLabel("Unfriend:"));
        extraPanel.add(delFriend1);
        extraPanel.add(delFriend2);
        extraPanel.add(deleteFriendshipBtn);

        mainPanel.add(userPanel);
        mainPanel.add(friendPanel);
        mainPanel.add(suggestPanel);
        mainPanel.add(extraPanel);
        mainPanel.add(scrollPane);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);

        addUserBtn.addActionListener(e -> {
            String name = nameField.getText();
            String ageText = ageField.getText();
            String city = cityField.getText();
            try {
                int age = Integer.parseInt(ageText);
                if (graph.addUser(name, age, city)) {
                    outputListModel.addElement("User added: " + name);
                } else {
                    outputListModel.addElement("User already exists: " + name);
                }
            } catch (NumberFormatException ex) {
                outputListModel.addElement("Invalid age input");
            }
            nameField.setText("");
            ageField.setText("");
            cityField.setText("");
        });

        addFriendBtn.addActionListener(e -> {
            String user1 = user1Field.getText();
            String user2 = user2Field.getText();
            if (graph.addFriendship(user1, user2)) {
                outputListModel.addElement("Friendship added: " + user1 + " <-> " + user2);
            } else {
                outputListModel.addElement("Cannot add friendship: " + user1 + ", " + user2);
            }
            user1Field.setText("");
            user2Field.setText("");
        });

        suggestBtn.addActionListener(e -> {
            String name = suggestField.getText();
            boolean byAge = agePriority.isSelected();
            boolean byLoc = locPriority.isSelected();
            User user = graph.getUserIgnoreCase(name);
            if (user == null) {
                outputListModel.addElement("User not found: " + name);
                return;
            }
            List<User> suggestions = graph.suggestFriends(name, byAge, byLoc);
            outputListModel.addElement("Suggestions for " + name + ":");
            for (User suggested : suggestions) {
                int mutual = graph.getMutualFriendCount(user, suggested);
                outputListModel.addElement("- " + suggested.name + " | Age: " + suggested.age + " | Location: " + suggested.location + " | Mutual: " + mutual);
            }
            suggestField.setText("");
        });

        listUsersBtn.addActionListener(e -> {
            outputListModel.addElement("All users:");
            for (String user : graph.getAllUsers()) {
                outputListModel.addElement("- " + user);
            }
        });

        listFriendsBtn.addActionListener(e -> {
            String target = friendLookupField.getText().trim();
            List<String> friends = graph.getFriendsOfUser(target);
            if (friends.isEmpty()) {
                outputListModel.addElement("No friends or user not found: " + target);
            } else {
                outputListModel.addElement("Friends of " + target + ":");
                for (String friend : friends) {
                    outputListModel.addElement("• " + friend);
                }
            }
            friendLookupField.setText("");
        });

        deleteUserBtn.addActionListener(e -> {
            String name = delUserField.getText().trim();
            if (graph.removeUser(name)) {
                outputListModel.addElement("User deleted: " + name);
            } else {
                outputListModel.addElement("User not found: " + name);
            }
            delUserField.setText("");
        });

        deleteFriendshipBtn.addActionListener(e -> {
            String name1 = delFriend1.getText().trim();
            String name2 = delFriend2.getText().trim();
            if (graph.removeFriendship(name1, name2)) {
                outputListModel.addElement("Friendship removed: " + name1 + " <-> " + name2);
            } else {
                outputListModel.addElement("Unable to remove friendship: " + name1 + ", " + name2);
            }
            delFriend1.setText("");
            delFriend2.setText("");
        });
    }
}