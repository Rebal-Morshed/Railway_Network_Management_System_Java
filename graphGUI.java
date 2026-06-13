import javax.swing.*;
import java.awt.*;
import java.util.*;

//فئة الواجهة الرسومية للتعامل مع المخطط البياني للمحطات و الطرق بينهما
public class graphGUI extends JFrame {
 private Graph graph;
 private GraphPanel graphPanel;
 private JButton fileProcessButton;
 private JTextField stationField;
 private JTextField sourceField;
 private JTextField destinationField;
 private JTextField distanceField;
 private JTextField startField;
 private JButton findPathButton;
 private JTextField endField;
 private JLabel resultLabel;
 private JButton addStationButton;
 private JButton addRouteButton;
 private JTextArea displayArea;

    public graphGUI() {

    graph = new Graph();

    setTitle("Railway Network");
    setSize(1200, 700);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    //لوحة لإضافة المحطات
    JPanel addStationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    stationField = new JTextField(33);
    addStationButton = new JButton("Add Station");
    addStationButton.addActionListener(e -> {
        String stationName = stationField.getText();
        if (!stationName.isEmpty()) {
            Station station = new Station(stationName);
            graph.addStation(station);
            displayArea.setText(graph.exportGraph());
            graphPanel.repaint();
            stationField.setText("");
        }
    });
    addStationPanel.add(new JLabel("Station : "));
    addStationPanel.add(stationField);
    addStationPanel.add(addStationButton);
    
    //لوحة لإضافة الطرق بين المحطات
    JPanel addRoutePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    sourceField = new JTextField(8);
    destinationField = new JTextField(8);
    distanceField = new JTextField(7);
    addRouteButton = new JButton("Add Route");
    addRouteButton.addActionListener(e -> {
        String sourceName = sourceField.getText();
        String destinationName = destinationField.getText();
        String distanceText = distanceField.getText();
        if (!sourceName.isEmpty() && !destinationName.isEmpty() && !distanceText.isEmpty()) {
            try {
                int distance = Integer.parseInt(distanceText);
                Station source = graph.getStation(sourceName);
                Station destination = graph.getStation(destinationName);
                if (source != null && destination != null) {
                    Route route = new Route(source, destination, distance);
                    source.addRoute(route);
                    displayArea.setText(graph.exportGraph());
                    sourceField.setText("");
                    destinationField.setText("");
                    distanceField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Both stations must exist.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Distance must be a number.");
            }
        }
    });
    addRoutePanel.add(new JLabel("From : "));
    addRoutePanel.add(sourceField);
    addRoutePanel.add(new JLabel("To : "));
    addRoutePanel.add(destinationField);
    addRoutePanel.add(new JLabel("Distance : "));
    addRoutePanel.add(distanceField);
    addRoutePanel.add(addRouteButton);
    
    //لوحة لإيجاد أقصر مسار بين محطتين
    JPanel pathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    startField = new JTextField(15);
    endField = new JTextField(15);
    findPathButton = new JButton("Find Path");
    resultLabel = new JLabel("Distance: ");
    pathPanel.add(new JLabel("Start"));
    pathPanel.add(startField);
    pathPanel.add(new JLabel("End"));
    pathPanel.add(endField);
    pathPanel.add(findPathButton);
    pathPanel.add(resultLabel);
    
    findPathButton.addActionListener(e -> {
        String startName = startField.getText();
        String endName = endField.getText();
        int distance = graph.shortestPath(startName, endName);
            resultLabel.setText("Distance: " + distance);
    });

    //لوحة للتعامل مع الملفات
    JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    fileProcessButton = new JButton("Process File");
    fileProcessButton.addActionListener(e -> {
        graph.processFile("input.txt", "output.txt");
        displayArea.setText(graph.exportGraph());
        graphPanel.repaint();
    });
    filePanel.add(fileProcessButton);

    //لوحة لفرز المحطات حسب عدد الاتصالات
    JPanel sortByConnectionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton sortByConnectionsButton = new JButton("Sort by Connections");
    sortByConnectionsButton.addActionListener(e -> {
        ArrayList<Station> sortedStations = graph.sortStationsByConnectoins();
        displayArea.setText("");
        for (Station station : sortedStations) {
            displayArea.append(station.getName() + " has " + station.getRoutes().size() + " connections.\n");
        }
    });
    sortByConnectionsPanel.add(sortByConnectionsButton);

    //تجميع كل اللوحات الجانبية في لوحة واحدة بجهة اليسار
    JPanel controlPanel = new JPanel(new GridLayout(6, 1));
    controlPanel.add(addStationPanel);
    controlPanel.add(addRoutePanel);
    controlPanel.add(pathPanel);
    controlPanel.add(filePanel);
    controlPanel.add(sortByConnectionsPanel);
    add(controlPanel, BorderLayout.WEST);

    //منطقة لعرض المخطط الكتابي و النصوص
    displayArea = new JTextArea(10,4);
    displayArea.setEditable(false);
    add(new JScrollPane(displayArea), BorderLayout.SOUTH);
    setVisible(true);
    
    //إضافة المخطط البياني في جهة اليمين
    graphPanel = new GraphPanel(graph);
    graphPanel.setPreferredSize(new Dimension(550, 550));
    add(graphPanel,BorderLayout.EAST);
 }
}
