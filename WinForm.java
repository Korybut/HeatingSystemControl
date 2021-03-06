import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class WinForm extends JFrame {

    private Chart outdoorChart;
    private Chart interiorChart;
    private Chart powerChart;
    private TemperaturesArea temperArea;
    private SimulationParametersArea simParamArea;
    private JButton buttonStart;

    public WinForm() throws HeadlessException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(1200,700);
        setResizable(false);
        setVisible(true);

        double[] doubles = new double[23];
        Arrays.fill(doubles, 0.0);

        /* Trzy wykresy */
        outdoorChart = new Chart("Zewnętrzna temperatura", doubles, Color.BLUE);
        outdoorChart.setLocation(350,40);
        add(outdoorChart);

        interiorChart = new Chart("Temperatura pomieszczenia", doubles, 5.0, 26.0,  Color.red);
        interiorChart.setLocation(350,240);
        add(interiorChart);

        powerChart = new Chart("Moc pieca grzewczego", doubles, 0.0, 4.0, Color.GREEN);
        powerChart.setLocation(350,440);
        add(powerChart);

        /* Panel z parametrami symulacji */
        simParamArea = new SimulationParametersArea();
        simParamArea.setLocation(20,42);
        add(simParamArea);

        /* Pole temperatur */
        temperArea = new TemperaturesArea();
        temperArea.setLocation(240,42);
        add(temperArea);

        /* Przycisk rozpoczynający symulacje */
        buttonStart = new JButton("Start Simulation");
        buttonStart.setBounds(170,595,160,30);
        buttonStart.addActionListener(e -> {
            /* Wyświetlenie wykresu temperatury zewnętrznej */
            setVisible(false);
            remove(outdoorChart);
            outdoorChart = new Chart("Zewnętrzna temperatura", temperArea.get(), Color.BLUE);
            outdoorChart.setLocation(350,40);
            add(outdoorChart);
            setVisible(true);

            /* Rozpoczęcie obliczania logiki rozmytej z pobranych danych */
            FuzzyLogic fuzzyLogic = new FuzzyLogic(simParamArea.getStartTemperature(),
                    temperArea.get(),
                    simParamArea.getInsulation(),
                    simParamArea.getFactor(),
                    simParamArea.getMinTemp(),
                    simParamArea.getLowOpt(),
                    simParamArea.getHighOpt());

            /* Wyświetlenie wykresu temperatury pomiesczenia */
            setVisible(false);
            remove(interiorChart);
            interiorChart = new Chart("Temperatura pomieszczenia", fuzzyLogic.getTemp(), Color.RED);
            interiorChart.setLocation(350,240);
            add(interiorChart);
            setVisible(true);

            /* Wyświetlenie wykresu pracy pieca */
            setVisible(false);
            remove(powerChart);
            String[] valuesLabel = {"ZERO", "LOW", "MEDIUM", "HIGH", "MAX"};
            powerChart = new Chart("Moc pieca grzewczego", fuzzyLogic.getPower(), valuesLabel, Color.GREEN);
            powerChart.setLocation(350,440);
            add(powerChart);
            setVisible(true);

        });
        add(buttonStart);
    }
}
