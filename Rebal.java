public class Rebal {
    public static void main(String[] args) {

Graph graph = new Graph();
//محطات مسبقة التعريف للتعامل مع الملفات
Station damascus = new Station("Damascus");
Station homs = new Station("Homs");

graph.addStation(damascus);
graph.addStation(homs);

damascus.addRoute(new Route(damascus, homs, 120));
//اختبار للتعامل مع الملف
graph.processFile("input.txt", "output.txt");

  new graphGUI();
 }
}