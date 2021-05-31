package Metropolises;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.List;

public class MetropolisAbstractTableModel extends AbstractTableModel {
    List<Metropolis> metropolises;
    MetropolisDAO metropolisDAO;

    public MetropolisAbstractTableModel(MetropolisDAO metropolisDAO) throws SQLException {
        this.metropolisDAO = metropolisDAO;
        metropolises = metropolisDAO.getAll();
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Metropolis";
            case 1: return "Continent";
            case 2: return "Population";
        }
        throw new RuntimeException("Out of bounds");
    }

    @Override
    public int getRowCount() {
        return metropolises.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Metropolis m = metropolises.get(rowIndex);
        switch (columnIndex) {
            case 0: return m.getName();
            case 1: return m.getContinent();
            case 2: return m.getPopulation();
        }
       throw new RuntimeException("Out of bounds");
    }

    public void add(Metropolis m) throws SQLException {
        metropolisDAO.add(m);
        metropolises = metropolisDAO.getAll();
        fireTableDataChanged();
    }

    public void search(boolean populationGreater, boolean exactMatch, String metropolis, String continent, long population) throws SQLException {
        metropolises = metropolisDAO.search(populationGreater, exactMatch, metropolis, continent, population);
        fireTableDataChanged();
    }
}
