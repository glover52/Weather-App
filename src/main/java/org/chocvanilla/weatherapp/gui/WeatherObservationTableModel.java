package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.data.observations.*;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class WeatherObservationTableModel extends AbstractTableModel {
    private final List<List<Field>> rows = new ArrayList<>();
    
    @Override
    public String getColumnName(int column) {
        return rows.get(0).get(column).getLabel();
    }

    public WeatherObservationTableModel(WeatherObservations observations){
        observations.stream().map(WeatherObservation::getFields).forEach(rows::add);
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return rows.get(0).size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex).get(columnIndex).getFormattedValue();
    }
}
