package Codes.Projects;

import java.util.*;

public class Graph {
    private Map<String, User> users;

    public Graph() {
        users = new HashMap<>();
    }

    public boolean addUser(String name, int age, String location) {
        if (users.containsKey(name)) return false;
        User newUser = new User(name, age, location);
        users.put(name, newUser);
        return true;
    }

    public boolean addFriendship(String name1, String name2) {
        User user1 = getUserIgnoreCase(name1);
        User user2 = getUserIgnoreCase(name2);
        if (user1 == null || user2 == null || user1 == user2 || user1.friends.contains(user2)) return false;
        user1.addFriend(user2);
        user2.addFriend(user1);
        return true;
    }

    public User getUser(String name) {
        return users.get(name);
    }

    public User getUserIgnoreCase(String name) {
        for (String key : users.keySet()) {
            if (key.equalsIgnoreCase(name)) return users.get(key);
        }
        return null;
    }

    public int getMutualFriendCount(User u1, User u2) {
        if (u1 == null || u2 == null) return 0;
        int count = 0;
        for (User friend : u1.friends) {
            if (u2.friends.contains(friend)) count++;
        }
        return count;
    }

    public List<User> suggestFriends(String name, boolean prioritizeAge, boolean prioritizeLocation) {
        User user = getUserIgnoreCase(name);
        if (user == null) return new ArrayList<>();

        Map<User, Integer> scoreMap = new HashMap<>();
        for (User other : users.values()) {
            if (other == user || user.friends.contains(other)) continue;

            int mutual = 0;
            for (User f : user.friends) {
                if (other.friends.contains(f)) mutual++;
            }

            int ageDiff = Math.abs(user.age - other.age);
            int ageScore = prioritizeAge ? (100 - ageDiff) : 0;
            int locScore = prioritizeLocation && user.location.equalsIgnoreCase(other.location) ? 100 : 0;

            int score = mutual * 5 + ageScore + locScore;
            if (score > 0) scoreMap.put(other, score);
        }

        List<User> candidates = new ArrayList<>(scoreMap.keySet());
        candidates.sort((u1, u2) -> Integer.compare(scoreMap.get(u2), scoreMap.get(u1)));

        return candidates;
    }

    public boolean removeUser(String name) {
        User user = getUserIgnoreCase(name);
        if (user == null) return false;
        for (User u : users.values()) {
            u.friends.remove(user);
        }
        users.remove(user.name);
        return true;
    }

    public boolean removeFriendship(String name1, String name2) {
        User user1 = getUserIgnoreCase(name1);
        User user2 = getUserIgnoreCase(name2);
        if (user1 == null || user2 == null || !user1.friends.contains(user2)) return false;
        user1.friends.remove(user2);
        user2.friends.remove(user1);
        return true;
    }

    public List<String> getAllUsers() {
        return new ArrayList<>(users.keySet());
    }

    public List<String> getFriendsOfUser(String name) {
        User user = getUserIgnoreCase(name);
        List<String> friendNames = new ArrayList<>();
        if (user != null) {
            for (User f : user.friends) {
                friendNames.add(f.name);
            }
        }
        return friendNames;
    }
}
