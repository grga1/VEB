package mk.ukim.finki.wp.lab.model;

public class Dish {
    private String dishId;
    private String name;
    private String cuisine;
    private int preparationTime;

    public Dish(String dishId, String name, String cuisine, int preparationTime) {
        this.dishId = dishId;
        this.name = name;
        this.cuisine = cuisine;
        this.preparationTime = preparationTime;
    }

    public String getDishId() { return dishId; }
    public String getName() { return name; }
    public String getCuisine() { return cuisine; }
    public int getPreparationTime() { return preparationTime; }
}
