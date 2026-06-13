import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

//فئة مخصصة لرسم المخطط البياني للمحطات و الطرق بينهما
public class GraphPanel extends JPanel {

    private Graph graph;
    private Map<String, Point> positions = new HashMap<>(); //للتعامل مع مواقع المحطات و تنسيق الرسم

    public GraphPanel(Graph graph) {
        this.graph = graph;
    }
       //حساب مواقع المحطات بشكل بسيط لتنسيق الرسم
       private void computePositions() {

        positions.clear();

        int cols = 3;   //عدد الأعمدة في التخطيط
        int i = 0;  //عداد لتحديد موقع كل محطة في التخطيط

        for (Station s : graph.getStations()) {

            int col = i % cols;
            int row = i / cols;

            int x = 100 + col * 180;
            int y = 100 + row * 180;

            positions.put(s.getName(), new Point(x, y));

            i++;
        }
    }

    //رسم المخطط البياني للمحطات و الطرق بينهما
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        computePositions();

        for (Station s : graph.getStations()) {

            Point from = positions.get(s.getName());

            for (Route r : s.getRoutes()) {

                String destName = r.getDestination().getName().trim();
                Point to = positions.get(destName);

                if (from != null && to != null) {

                    g.drawLine(from.x, from.y, to.x, to.y);

                    g.drawString(
                            String.valueOf(r.getDistance()),(from.x + to.x) / 2,(from.y + to.y) / 2);
                }
            }
        }

        for (Station s : graph.getStations()) {

            Point p = positions.get(s.getName());

            if (p != null) {

                g.drawOval(p.x - 20, p.y - 20, 40, 40);

                g.drawString(
                        s.getName(),
                        p.x - 25,
                        p.y - 30
                );
            }
        }
    }
}