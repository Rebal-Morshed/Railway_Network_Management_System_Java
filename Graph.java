import java.util.*;
import java.io.*;

public class Graph {
    private HashMap<String, Station> stations;

    public Graph() {
        this.stations = new HashMap<>();
    }

    public void addStation(Station station) {
        stations.put(station.getName(), station);
    }

    public Station getStation(String name) {
        return stations.get(name);
    }
    public boolean hasStation(String name){
        return stations.containsKey(name);
    }
    public Collection<Station> getStations() {
        return stations.values();
    }

    //تصدير المخطط البياني إلى نص منسق لسهولة التعامل مع الملفات
    public String exportGraph() {
    StringBuilder sb = new StringBuilder();
    for (Station station : stations.values()) {
        sb.append(station.getName()).append(" -> ");
        for (int i = 0; i < station.getRoutes().size(); i++) {
            Route route = station.getRoutes().get(i);
            sb.append(route.getDestination().getName()).append("(").append(route.getDistance()).append(")");
            if (i < station.getRoutes().size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("\n");
    }
    return sb.toString();
}

  //استيراد المخطط البياني من نص منسق للتعامل مع الملفات
  public void importGraph(String text){
    String[] lines = text.split("\n");
    for (String line : lines) {
        line = line.trim();
        if (line.isEmpty()) continue;
        String[] parts = line.split("->");
        String sourceName = parts[0].trim();
        Station source = stations.get(sourceName);
        if (source == null) {
            source = new Station(sourceName);
            addStation(source);
        }
        if(parts.length<2) continue;
        String[] routes = parts[1].split(",");
        for(String routeText : routes){
            routeText = routeText.trim();
            if(routeText.isEmpty()) continue;
            int open = routeText.indexOf('(');
            int close = routeText.indexOf(')');
            String destinationName = routeText.substring(0, open).trim();
            int distance = Integer.parseInt(routeText.substring(open + 1, close).trim());
            Station destination = stations.get(destinationName);
            if(destination == null){
                destination = new Station(destinationName);
                addStation(destination);
            }
            source.addRoute(new Route(source, destination, distance));
        }
    }
} 
//معالجة الملفات لإستيراد المخطط البياني من ملف نصي و تصديره إلى ملف آخر
 public void processFile(String inputFile, String outputFile){
    try{
        String text = "";
        Scanner sc = new Scanner(new File(inputFile));
        while(sc.hasNextLine()){
            text += sc.nextLine() + "\n";
        }
        sc.close();
        importGraph(text);
        PrintWriter pw = new PrintWriter(new File(outputFile));
        pw.print(exportGraph());
        pw.close();
    }
    catch(IOException e){
        System.out.println("Error processing file: " + e.getMessage());
  }
 }
  
 //ايجاد أقصر مسار بين محطتين باستخدام خوارزمية Dijkstra
 public int shortestPath(String startName, String endName){
    HashMap <String, Integer> distances = new HashMap<>();
    for(Station station : stations.values()){
        distances.put(station.getName(), Integer.MAX_VALUE);
    }
    distances.put(startName, 0);
    //استخدام PriorityQueue لتحديد المحطة الأقرب في كل خطوة
    PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getDistance));
    pq.add(new Node(getStation(startName),0));
     while(!pq.isEmpty()){
        Node current = pq.poll();
        Station currentStation = current.getStation();
        int currentDistance = current.getDistance();
        for(Route route : currentStation.getRoutes()){
            Station neighbor = route.getDestination();
            int newDistance = currentDistance + route.getDistance();
            if(newDistance < distances.get(neighbor.getName())){
                distances.put(neighbor.getName(), newDistance);
                pq.add(new Node(neighbor, newDistance));
        }
     }
     }
    return distances.get(endName);
 }
//فحص وجود دورات في المخطط البياني باستخدام خوارزمية DFS
 private boolean dfsCycle(Station station, HashSet<String> visited, HashSet<String> recStack) {
    String name = station.getName();
    if (recStack.contains(name)) {
        return true;
    }
    if (visited.contains(name)) {
        return false;
    }
    visited.add(name);
    recStack.add(name);
    for (Route route : station.getRoutes()) {
        if (dfsCycle(route.getDestination(), visited, recStack)) {
            return true;
        }
    }
    recStack.remove(name);
    return false;
 }
//فحص وجود دورات في المخطط البياني
 public boolean hasCycle() {
    HashSet<String> visited = new HashSet<>();
    HashSet<String> recStack = new HashSet<>();
    for (Station station : stations.values()) {
        if (dfsCycle(station, visited, recStack)) {
            return true;
        }
    }
    return false;
 }
//ترتيب المحطات حسب عدد الطرق المتصلة بها (المحطات الأكثر اتصالاً تظهر أولاً)
 public ArrayList<Station> sortStationsByConnectoins(){
    ArrayList<Station> sortedStations = new ArrayList<>(stations.values());
    sortedStations.sort((s1, s2) -> Integer.compare(s2.getRoutes().size(), s1.getRoutes().size()));
    return sortedStations;
 }
}