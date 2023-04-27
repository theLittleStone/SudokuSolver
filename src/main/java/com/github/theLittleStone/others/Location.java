package main.java.com.github.theLittleStone.others;

/**
 * Created by theLittleStone on 2023/4/26.
 */
public class Location {
    public int row;
    public int column;

    public Location(int row, int column) {
        this.row = row;
        this.column = column;
    }
    public Location(Location location){
        this.row = location.row;
        this.column = location.column;
    }
}
