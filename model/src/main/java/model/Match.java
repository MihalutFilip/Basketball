package model;

public class Match extends Entity<Integer> {
    private String name;
    private int price;
    private int placesRemaining;
    private String status;

    public Match(String name, int price, int placesRemaining) {
        this.name = name;
        this.price = price;
        this.placesRemaining = placesRemaining;
        this.status = getStatus();
    }

    public String getStatus() {
        return placesRemaining == 0 ? "Sold out" : "Avaible";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPlacesRemaining() {
        return placesRemaining;
    }

    public void setPlacesRemaining(int placesRemaining) {
        this.placesRemaining = placesRemaining;
    }

    @Override
    public String toString() {
        return "Match{" + this.getId() +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", placesRemaining=" + placesRemaining +
                '}';
    }
}
