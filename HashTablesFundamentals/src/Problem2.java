import java.util.*;

class InventoryManager {

    // productId -> stock count
    private HashMap<String, Integer> stockMap = new HashMap<>();

    // productId -> waiting queue (FIFO)
    private HashMap<String, Queue<Integer>> waitingList = new HashMap<>();

    // Add product
    public void addProduct(String productId, int stock) {
        stockMap.put(productId, stock);
        waitingList.put(productId, new LinkedList<>());
    }

    // Check stock (O(1))
    public int checkStock(String productId) {
        return stockMap.getOrDefault(productId, 0);
    }

    // Purchase item (thread-safe)
    public synchronized String purchaseItem(String productId, int userId) {
        int stock = stockMap.getOrDefault(productId, 0);

        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "Success: User " + userId + " purchased. Remaining: " + (stock - 1);
        } else {
            Queue<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            return "Out of stock. User " + userId + " added to waiting list. Position: " + queue.size();
        }
    }

    // Restock product
    public synchronized void restock(String productId, int quantity) {
        int stock = stockMap.getOrDefault(productId, 0);
        stockMap.put(productId, stock + quantity);

        Queue<Integer> queue = waitingList.get(productId);

        // Serve waiting list
        while (stockMap.get(productId) > 0 && !queue.isEmpty()) {
            int user = queue.poll();
            stockMap.put(productId, stockMap.get(productId) - 1);
            System.out.println("Waiting user " + user + " fulfilled!");
        }
    }

    // Get waiting list size
    public int getWaitingListSize(String productId) {
        return waitingList.get(productId).size();
    }
}

public class Problem2 {
    public static void main(String[] args) {

        InventoryManager manager = new InventoryManager();

        // Add product with limited stock
        manager.addProduct("IPHONE15_256GB", 3);

        // Simulate purchases
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 101));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 102));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 103));

        // Stock finished → waiting list
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 104));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 105));

        // Check stock
        System.out.println("\nStock left: " + manager.checkStock("IPHONE15_256GB"));

        // Restock
        System.out.println("\nRestocking...");
        manager.restock("IPHONE15_256GB", 2);

        // Final state
        System.out.println("\nFinal Stock: " + manager.checkStock("IPHONE15_256GB"));
        System.out.println("Waiting List Size: " + manager.getWaitingListSize("IPHONE15_256GB"));
    }
}
