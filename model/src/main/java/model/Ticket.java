package model;

import javafx.util.Pair;

public class Ticket extends Entity<Pair> {
    private int placesTaken;

    public Ticket(int placesTaken) {
        this.placesTaken = placesTaken;
    }

    public int getPlacesTaken() {
        return placesTaken;
    }

    public void setPlacesTaken(int placesTaken) {
        this.placesTaken = placesTaken;
    }

    @Override
    public String toString() {
        return "Ticket{" + "client id =" + this.getId().getKey() +
                ", match id=" + this.getId().getValue() +
                ", placesTaken=" + placesTaken +
                '}';
    }
}
