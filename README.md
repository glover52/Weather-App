# Software Engineering: Process & Tools

[WeatherApp on Bitbucket](https://bitbucket.org/chocvanilla/weather-app)

This repository was private to help prevent plagiarism, it is now under the [**GNU AGPLv3**](http://choosealicense.com/licenses/agpl-3.0/) license.

## Team Members

 - Adam Thalhammer (s3544305)
 - Utsav 'Raj' Adhikari (s3542303)
 - Rhys Van Orsouw (s3542173)
 - Kyle Glover (s3539870)

### Project Changes
The project underwent a restructure and code refactor, into a layered architecture. Charting was done in a more dynamic
 way to allow for addition and removal of components. The retrieval of data was refactored to a more Object Orientated
 approach, allowing the extension of existing frameworks to new data formats. A User Interface was also done, creating a
 more intuitive design, allowing users to see Forecasts and past Observations.

### Source
Application source code can be found in [src/main](src/main).

### Tests
Unit tests can be found in [src/test](src/test).

Reports can be found in [src/test/reports](src/test/reports).

Test plan is located at [src/test/TESTPLAN.txt](src/test/TESTPLAN.md).

### Documentation
Comprehensive documentation can be found in [docs](/docs).

### Logs
Logs can be found in [logs](logs/).

### Build Automation
Gradle was used in both the first and second build releases as a Build Automation tool. Many different aspects became
increasingly valuable, such as Automated Testing, Build Dependencies, JAR building and Compilation.

### Jira Server
A virtual server was used to host the JIRA application. JIRA was used throughout the project to assign tasks, track
issues, and organise sprints. JIRA was also integrated with the Bamboo server, allowing it to display whether a build
was successful or not. JIRA was also integrated into the HipChat server, broadcasting when commits and merges
are made.

The server may be connected to using the following details:

Web Address: #REMOVED#

User name: guest

Password: guest

This server may be connected to on the RMIT intranet.

### Contiguous Integration
Bamboo was an invaluable tool for finding bugs not instantly obvious under visual inspection. Bamboo was also integrated
into the HipChat server, so that the status of a build was displayed upon completion of testing.

The server may be connected to using the following details:

Web Address: #REMOVED#

User Name: guest

password: guest

Please note that the server cannot be accessed from the RMIT intranet, as port 8085 is blocked.

## Contribution
##### Adam Thalhammer (25%)
Worked on:

- Project Restructure
- Extending data sources to Forecast.io
- Exception Handling
- Project Logging
- Debugging
- OO design emphasis
- Unit Testing


As a team member, Adam worked together with Raj and completed a vast majority of the backend of the project. Adam also
conducted most of the major testing and was the first person to suggest applications/software to use for the project.
The use of Gradle for build automation and dependency management was Adam's idea, and he provided assistance consistently in the use and
maintenance of this useful tool. Furthermore, Adam worked well in tandem with Kyle to organise a virtual server, using student credit to pay for it,
and set up the Centos server to run JIRA. Adam continued to implement strong ideas, and enforced good Software Engineering
principles throughout the project.

##### Utsav Adhikari (25%)
Worked on:

- Retrieving data from the Bureau of Meteorology website
- Weather observation table
- Weather station search
- Unit testing
- Stress Testing
- Acceptance Testing

Raj worked mainly on the back end of the project with some aspects of design in the station GUI. Along with Adam, he
was the main driving force behind the back end programming of the assignment and worked until the very end of the
project whilst also balancing other assignments. Raj also made great contributions in regards to the UML and
organisation of code structure. Raj provided invaluable test cases and testing framework to properly test the
application in many ways.

##### Rhys Van Orsouw (25%)
Worked on:

- Created graph
- Favourites
- Favourites button (For addition and removal)
- GUI layout
- GUI restructure
- Managed HipChat

Rhys worked solo on the presentation of the graph, not only that, he constructed many aspects of the GUI and had a large
stake in how it is presented to the user. Also as the manager of the group chat, Rhys also made sure that the group members
were behaving themselves in order to create a very streamlined collaborative process for the assignment.

##### Kyle Glover (25%)
Worked on:

- Sketched out the initial GUI layout
- JIRA setup
- Managed JIRA
- Managed group Git repositories (branches, .gitignore, etc.)
- User Documentation
- Bug Tracking and assignment.

Kyle worked in conjunction with Adam in setting up the dedicated JIRA server, and continued to maintain the server,
while also keeping the group organised, and managing the JIRA tasks and sprints. Kyle also helped in assigning tasks to
other members, and assisted in fixing many mistakes made in the GIT version control.
