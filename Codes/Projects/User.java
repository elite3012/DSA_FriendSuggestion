package Codes.Projects;

import java.util.HashSet;
import java.util.Set;

public class User {
    String name;
    int age;
    String location;
    Set<User> friends;

    User(String name, int age, String location) {
        this.name = name;
        this.age = age;
        this.location = location;
        this.friends = new HashSet<>();
    }

    void addFriend(User friend) {
        if (friend == null || friend == this || friends.contains(friend)) return;
        friends.add(friend);
    }
}

