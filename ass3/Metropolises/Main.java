package Metropolises;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends JFrame {

    private static final int TEXT_FIELDS_SIZE = 10;

    private static final String[] POPULATION_DROP_DOWN_MENU = {"Population Larger Then", "Population Smaller Then"};
    private static final String[] MATCH_DROP_DOWN_MENU = {"Exact Match", "Partial Match"};


    private final JTextField metropolisTextField;
    private final JTextField continentTextField;
    private final JTextField populationTextField;
    private JComboBox populationDropDown;
    private JComboBox matchTypeDropDown;
    private JButton addButton;
    private JButton searchButton;
    private final MetropolisAbstractTableModel model;


    public Main(MetropolisDAO dao) throws SQLException {
        super("Metropolis Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(4,4));
        JPanel topPanel = new JPanel();

        metropolisTextField = new JTextField(TEXT_FIELDS_SIZE);
        continentTextField = new JTextField(TEXT_FIELDS_SIZE);
        populationTextField = new JTextField(TEXT_FIELDS_SIZE);

        createTopPanel(topPanel);

        model = new MetropolisAbstractTableModel(dao);

        JTable table = new JTable(model);
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setPreferredSize(new Dimension(300,200));

        Box sideMenu = Box.createVerticalBox();
        createSideMenu(sideMenu);

        add(topPanel, BorderLayout.NORTH);
        add(scrollTable, BorderLayout.CENTER);
        add(sideMenu,BorderLayout.EAST);

        addActionListeners();

        pack();
        setVisible(true);
    }

    private void addActionListeners() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Metropolis metropolis = new Metropolis(metropolisTextField.getText(),
                        continentTextField.getText(),
                        Integer.parseInt(populationTextField.getText()));
                try {
                    model.add(metropolis);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        searchButton.addActionListener(e -> {
            boolean greater = Objects.equals(populationDropDown.getSelectedItem(), POPULATION_DROP_DOWN_MENU[0]);
            boolean exact = Objects.equals(matchTypeDropDown.getSelectedItem(), MATCH_DROP_DOWN_MENU[0]);
            String population = populationTextField.getText();
            if (population.isEmpty()) {
                try {
                    model.search(greater, exact, metropolisTextField.getText(), continentTextField.getText(), -1);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                try {
                    model.search(greater, exact, metropolisTextField.getText(), continentTextField.getText(), Integer.parseInt(population));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    private void createTopPanel(JPanel topPanel) {
        topPanel.add(new JLabel(" Metropolis: "));
        topPanel.add(metropolisTextField);
        topPanel.add(new JLabel(" Continent: "));
        topPanel.add(continentTextField);
        topPanel.add(new JLabel(" Population: "));
        topPanel.add(populationTextField);
    }

    private void createSideMenu(Box sideMenu) {
        addButton = new JButton("Add");
        searchButton = new JButton("Search");

        addButton.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        searchButton.setAlignmentX(JComponent.LEFT_ALIGNMENT);


        Box innerSideMenu = Box.createVerticalBox();
        innerSideMenu.setBorder(new TitledBorder("Search options"));

        populationDropDown = new JComboBox(POPULATION_DROP_DOWN_MENU);
        matchTypeDropDown = new JComboBox(MATCH_DROP_DOWN_MENU);

        populationDropDown.setAlignmentX(Component.LEFT_ALIGNMENT);
        matchTypeDropDown.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        populationDropDown.setMaximumSize(new Dimension(200, 25));
        matchTypeDropDown.setMaximumSize(new Dimension(200, 25));

        sideMenu.add(addButton);
        sideMenu.add(searchButton);
        innerSideMenu.add(populationDropDown);
        innerSideMenu.add(matchTypeDropDown);
        sideMenu.add(innerSideMenu);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/metro_db", "root", "root");
        MetropolisDAO metropolisDAO = MetropolisDAO.getInstance(connection);
        Main m = new Main(metropolisDAO);
    }
}
