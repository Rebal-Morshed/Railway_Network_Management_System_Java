public class Node {
    private Station station;
    private int distance;
    public Node(Station station, int distance){
        this.station = station;
        this.distance = distance;
    }
    public Station getStation(){
        return station;
    }
    public int getDistance(){
        return distance;
    }
}
