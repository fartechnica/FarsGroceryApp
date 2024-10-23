import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GroceryList {

    private String name;
    private String weight;
    private String price;
    private List<GroceryList> items = new ArrayList<>();

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
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String itemName = parts[0].trim();
                    String itemWeight = parts[1].trim();
                    String itemPrice = parts[2].trim();
                    items.add(new GroceryList(itemName, itemWeight, itemPrice));
                }
            }
        } catch (IOException e) {
            LoggerUtility.severe("IO Exception, CODE: 00015X in Class: " + GroceryList.class.getName());
        }
    }

    public void printByCategory() {
        for (GroceryList item : items) {
            System.out.println(item.name + ", " + item.weight + ", " + item.price);
        }
    }

    public void printByPrice() {
        items.sort(Comparator.comparing(GroceryList::getPriceInDollars));
        printByCategory();
    }

    public void printByWeight() {
        items.sort(Comparator.comparing(GroceryList::getWeightInGrams));
        printByCategory();
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