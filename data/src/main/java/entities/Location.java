package entities;

public class Location implements Comparable<Location> {
    public String name;

    public Location(String name) {
        this.name = name;
    }

   @Override
   public int compareTo(Location o) {
        return name.equals(o.name) ? 1 : 0;
   }

}