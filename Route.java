public class Route {
    private Station source;
    private Station destination;
    private int distance;

    public Route(Station source, Station destination, int distance) {
        this.source = source;   
        this.destination = destination;
        this.distance = distance;
    }
    public Station getSource() {
        return source;
    }
    public Station getDestination() {
        return destination;
    }
    public int getDistance() {
        return distance;
    }
    
} 
