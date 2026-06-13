import java.util.*;

public class Station {
    private String name;
    private ArrayList<Route> routes;

    public Station(String name) {
        this.name = name;
        this.routes = new ArrayList<>();
    }

    public void addRoute(Route route) {
        routes.add(route);
    }
    public String getName() {
        return name;
    }   
    public ArrayList<Route> getRoutes() {
        return routes;
    }
}

