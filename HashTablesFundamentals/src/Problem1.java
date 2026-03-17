import java.util.*;

class UsernameChecker {

    // username -> userId
    private HashMap<String, Integer> userDB = new HashMap<>();

    // username -> attempt frequency
    private HashMap<String, Integer> attemptCount = new HashMap<>();

    private int userIdCounter = 1;

    // Add existing users
    public void addUser(String username) {
        userDB.put(username, userIdCounter++);
    }

    // Check availability in O(1)
    public boolean checkAvailability(String username) {
        attemptCount.put(username, attemptCount.getOrDefault(username, 0) + 1);
        return !userDB.containsKey(username);
    }

    // Register user
    public boolean registerUser(String username) {
        if (checkAvailability(username)) {
            userDB.put(username, userIdCounter++);
            return true;
        }
        return false;
    }

    // Suggest alternatives
    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        // Append numbers
        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;
            if (!userDB.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        // Replace '_' with '.'
        String modified = username.replace('_', '.');
        if (!userDB.containsKey(modified)) {
            suggestions.add(modified);
        }

        // Add prefix
        String prefix = "the_" + username;
        if (!userDB.containsKey(prefix)) {
            suggestions.add(prefix);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {
        String result = "";
        int maxFreq = 0;

        for (Map.Entry<String, Integer> entry : attemptCount.entrySet()) {
            if (entry.getValue() > maxFreq) {
                maxFreq = entry.getValue();
                result = entry.getKey();
            }
        }

        return result + " (" + maxFreq + " attempts)";
    }
}

public class Problem1{
    public static void main(String[] args) {

        UsernameChecker system = new UsernameChecker();

        // Preload users
        system.addUser("john_doe");
        system.addUser("admin");
        system.addUser("user123");

        // Availability check
        System.out.println("john_doe → " +
                (system.checkAvailability("john_doe") ? "Available" : "Taken"));

        System.out.println("jane_smith → " +
                (system.checkAvailability("jane_smith") ? "Available" : "Taken"));

        // Suggestions
        List<String> suggestions = system.suggestAlternatives("john_doe");
        System.out.println("\nSuggestions for john_doe:");
        for (String s : suggestions) {
            System.out.println(s);
        }

        // Simulate attempts
        for (int i = 0; i < 10000; i++) system.checkAvailability("admin");
        for (int i = 0; i < 5000; i++) system.checkAvailability("john_doe");

        // Most attempted
        System.out.println("\nMost attempted: " + system.getMostAttempted());
    }
}
