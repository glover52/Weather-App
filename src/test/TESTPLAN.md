# Acceptance testing plan

test status file [link]()  <!-- TODO -->

## Acceptance Criteria

- Forecast information is displayed in a chart and table.
- Any data that can be retrieved, may be graphed.
    - Graphs are created dynamically, and can be adjusted in real time.
- Forecast and Observation information are displayed separately.
- User can zoom in on a part of charts.
- Weather stations can be saved as favorites.
    - favorite stations appear on the main window.
- The locations and dimensions of windows are preserved are persist when re-opened.
- Weather stations can be filtered through text searches.

## Test cases

###TC01:

**Scenario**: User can favorite channels from details window.

**Set Up**: Start program.

**Procedure**

1. Select an arbitrary station from the weather station list
2. Click any one of the "BOM observations", "Forecast.io observations", "Forecast" to open details window.
3. Click on the favorite button
4. Close the details window

**Expected Outcome**

- Favorite button with change to unfavorite button.
- Favorited weather station will now appear on the favorites list

###TC02:

**Scenario**: User can favorite channels in town panel.

**Set Up**: Start program.

**Procedure**

1. Select an arbitrary station from the weather station list
2. Click on the favorite button.

**Expected Outcome**

- Favorite button with change to unfavorite button.
- Favorited weather station will now appear on the favorites list

###TC03:

**Scenario**: User opens detail window to show BOM weather observations history

**Set Up**: Start program, select arbitrary weather station.

**Procedure**

1. Click on "BOM Observations button"

**Expected Outcome**

- Details Window will open.
- Latest Observations will be visible.
- Observation history chart will be visible.

###TC04:

**Scenario**: User opens detail window to show Forecast.io weather observations history

**Set Up**: Start program, select arbitrary weather station.

**Procedure**

1. Click on "Forecast.io Observations button"

**Expected Outcome**

- Details Window will open.
- Latest Observations will be visible.
- Observation history chart will be visible.

###TC05:

**Scenario**: User opens detail window to show Forecast.io weather forecast

**Set Up**: Start program, select arbitrary weather station.

**Procedure**

1. Click on "Forecast.io Observations button"

**Expected Outcome**

- Details Window will open.
- Latest Observations will be visible.
- Forecast chart will be visible.

###TC06:

**Scenario**: User can toggle data sets displayed on the chart.

**Set Up**: Start program, select arbitrary weather station.

**Procedure**

1. Click on "Forecast.io Observations button"
2. Click any one of the "BOM observations", "Forecast.io observations", "Forecast" to open details window.
3. Click and drag a rectangle from top left to bottom right. While dragging a rectangle will be displayed on the portion of the chart to be magnified.

**Expected Outcome**

- The chart will zoom into the selected portion of the chart

###TC07:

**Scenario**: User can zoom out of a chart that has been magnified.

**Set Up**: Start program, select arbitrary weather station, and open details window.

**Procedure**

1. Click on "Forecast.io Observations button"
2. Click any one of the "BOM observations", "Forecast.io observations", "Forecast" to open details window.
3. Click and drag a rectangle from top left to bottom right. While dragging a rectangle will be displayed on the portion of the chart to be magnified.
4. Click and drag from right to left.

**Expected Outcome**

- The chart will zoom out to the standard level of magnification