# Acceptance testing plan

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

#####Status: PASS

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

#####Status: PASS

**Scenario**: User can favorite channels in town panel.

**Set Up**: Start program.

**Procedure**

1. Select an arbitrary station from the weather station list
2. Click on the favorite button.

**Expected Outcome**

- Favorite button with change to unfavorite button.
- Favorited weather station will now appear on the favorites list

###TC03:

#####Status: PASS

**Scenario**: User opens detail window to show BOM weather observations history

**Set Up**: Start program, select arbitrary weather station.

**Procedure**

1. Click on "BOM Observations button"

**Expected Outcome**

- Details Window will open.
- Latest Observations will be visible.
- Observation history chart will be visible.

###TC04:

#####Status: PASS

**Scenario**: User opens detail window to show Forecast.io weather observations history

**Set Up**: Start program, select arbitrary weather station.

**Procedure**

1. Click on "Forecast.io Observations button"

**Expected Outcome**

- Details Window will open.
- Latest Observations will be visible.
- Observation history chart will be visible.

###TC05:

#####Status: PASS

**Scenario**: User opens detail window to show Forecast.io weather forecast

**Set Up**: Start program, select arbitrary weather station.

**Procedure**

1. Click on "Forecast.io Observations button"

**Expected Outcome**

- Details Window will open.
- Latest Observations will be visible.
- Forecast chart will be visible.

###TC06:

#####Status: PASS

**Scenario**: User can toggle data sets displayed on the chart.

**Set Up**: Start program, select arbitrary weather station.

**Procedure**

1. Click on "Forecast.io Observations button"
2. Click any one of the "BOM observations", "Forecast.io observations", "Forecast" to open details window.
3. Click and drag a rectangle from top left to bottom right. While dragging a rectangle will be displayed on the portion of the chart to be magnified.

**Expected Outcome**

- The chart will zoom into the selected portion of the chart

###TC07:

#####Status: PASS

**Scenario**: User can zoom out of a chart that has been magnified.

**Set Up**: Start program, select arbitrary weather station, and open details window.

**Procedure**

1. Click on "Forecast.io Observations button"
2. Click any one of the "BOM observations", "Forecast.io observations", "Forecast" to open details window.
3. Click and drag a rectangle from top left to bottom right. While dragging a rectangle will be displayed on the portion of the chart to be magnified.
4. Click and drag from right to left.

**Expected Outcome**

- The chart will zoom out to the standard level of magnification

###TC08:

#####Status: PASS

**Scenario**: Main window location and size are preserved from previous execution

**Set Up**: None

**Procedure**

1. Start the application
2. Maximize the main window
3. Close the window

**Expected Outcome**

- The application will open maximized.

###TC09:

#####Status: PASS

**Scenario**: Detail window location and size are preserved from previous execution

**Set Up**: None

**Procedure**

1. Start the application.
2. Select an arbitrary weather station.
3. Click any one of the "BOM observations", "Forecast.io observations", "Forecast" to open details window.
4. Maximize details window.
5. Close details window.

**Expected Outcome**

- The details window will open maximized.

###TC10:

#####Status: PASS

**Scenario**: Weather stations list can be filtered through a search

**Set Up**: None

**Procedure**

1. Start the application.
2. Type the word "Melbourne" into the search bar.

**Expected Outcome**

- Only weather stations with "Melbourne" will appear on the list.

###TC11:

#####Status: PASS

**Scenario**: Weather stations will display latest retrievable data when selected in town panel.

**Set Up**: Start the application.

**Procedure**

1. Select an arbitrary weather station.

**Expected Outcome**

- Town panel will display latest observations.
- Observation, Forecast, and Favorite buttons will be visible.
