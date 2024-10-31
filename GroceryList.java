import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroceryList {

    private String name;
    private String weight;
    private String price;
    private List<GroceryList> items = new ArrayList<>();
    private Map<String, List<GroceryList>> categorizedItems = new HashMap<>();

    public GroceryList(String name, String weight, String price) {
        this.name = name;
        this.weight = weight;
        this.price = price;
    }

    public GroceryList() {}

    public void loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String category = parts[0].trim();
                    String[] itemParts = parts[1].trim().split(",");
                    if (itemParts.length == 3) {
                        String itemName = itemParts[0].trim();
                        String itemWeight = itemParts[1].trim();
                        String itemPrice = itemParts[2].trim();
                        GroceryList item = new GroceryList(itemName, itemWeight, itemPrice);
                        items.add(item);
                        categorizedItems.putIfAbsent(category, new ArrayList<>());
                        categorizedItems.get(category).add(item);
                    }
                }
            }
        } catch (IOException e) {
            LoggerUtility.severe("IO Exception, CODE: 00015X in Class: " + GroceryList.class.getName());
        }
    }

    public void printItemsByCategory(String category) {
        if (categorizedItems.containsKey(category)) {
            System.out.println(category + ":");
            for (GroceryList item : categorizedItems.get(category)) {
                System.out.println(" - " + item.name + ", " + item.weight + ", " + item.price);
            }
        } else {
            System.out.println("Category " + category + " not found.");
        }
    }

    public void printByPrice() {
        items.sort(Comparator.comparing(GroceryList::getPriceInDollars));
        printAllItems();
    }

    public void printByWeight() {
        items.sort(Comparator.comparing(GroceryList::getWeightInGrams));
        printAllItems();
    }

    private void printAllItems() {
        for (GroceryList item : items) {
            System.out.println(item.name + ", " + item.weight + ", " + item.price);
        }
    }

    private double getPriceInDollars() {
        return Double.parseDouble(this.price.replace("$", "").trim());
    }

    private double getWeightInGrams() {
        return Double.parseDouble(this.weight.replace("g", "").trim());
    }

    public String getName() {
        return name;
    }

    public String getWeight() {
        return weight;
    }

    public String getPrice() {
        return price;
    }
}