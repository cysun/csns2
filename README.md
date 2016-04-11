# CSNetwork Services (CSNS)

CSNS is an open-source, web-based software system that combines the functions of learning management, program assessment, student administration and advisement into one system; and by doing so, CSNS achieves a level of efficiency that is not possible by loosely coupling several special-purposed systems.

## Software Requirements

Java and Maven are required to build the code, and PostgreSQL and a Java EE application server (e.g. Tomcat) are required to run it.

## Installation

1. Create two empty databases, one for operational use and one for testing.
2. Populate the operatinal database using the following two SQL scripts:
  * `src/main/scripts/csns-create.sql` - create all the schema elements.
  * `src/main/scripts/csns-test-insert.sql` - create some sample data to play with. Plese read the script to see what users,         departments, and classes are created.
3. Copy `build.properties.sample` to `build.properties`.
4. Change the values in `build.properties` according to your environment.
5. Run `mvn package` to build the code.
6. Deploy the WAR file `target/csns2.war` to your application server.

For a development setup, please check out this [YouTube video](https://www.youtube.com/watch?v=_DQz69YIKHg).

## Test Drive

You can test drive CSNS at http://cs3.calstatela.edu:4046/csns2/. Please contact Chengyu Sun at csun@calstatela.edu to request an account.

