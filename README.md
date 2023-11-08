<!--
{% comment %}
Licensed to Julian Hyde under one or more contributor license
agreements.  See the NOTICE file distributed with this work
for additional information regarding copyright ownership.
Julian Hyde licenses this file to you under the Apache
License, Version 2.0 (the "License"); you may not use this
file except in compliance with the License.  You may obtain a
copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
either express or implied.  See the License for the specific
language governing permissions and limitations under the
License.
{% endcomment %}
-->
[![Build Status](https://github.com/julianhyde/flight-data-hsqldb/actions/workflows/main.yml/badge.svg?branch=main)](https://github.com/julianhyde/flight-data-hsqldb/actions?query=branch%3Amain)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.hydromatic/flight-data-hsqldb/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.hydromatic/flight-data-hsqldb)

# flight-data-hsqldb
FAA flight data set in hsqldb format

This project contains the FAA flight data set as an
HSQLDB database.

It contains the following tables, each backed by a data set in a `.csv` file.

| table                 | row count 
| :-------------------- | ----------:
| accidents             | 70,777 
| aircraft              | 359,928
| aircraft_engine_types | 8
| aircraft_engines      | 761
| aircraft_models       | 60,461
| aircraft_types        | 6
| airport_remarks       | 69,708
| airports              | 19,795
| carriers              | 21
| exceptions            | 702
| ontime                | 38,483,559
| states                | 58
| zipcodes              | 29,467

The `ontime` table is split into 51 files, each with approximately 400,000
rows, and because of time/memory considerations only the first is loaded.
Therefore `ontime` has only 399,999 rows when queried via SQL.

## To connect and read data

Add the following to your Maven pom.xml:
```xml
<dependencies>
  <dependency>
    <groupId>org.hsqldb</groupId>
    <artifactId>hsqldb</artifactId>
    <version>2.3.1</version>
  </dependency>
  <dependency>
    <groupId>net.hydromatic</groupId>
    <artifactId>flight-data-hsqldb</artifactId>
    <version>0.2</version>
  </dependency>
</dependencies>
```

Connect to the database via the URL, user name and password in the
`FlightHsqldb` class:

```java
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.hydromatic.flight.data.hsqldb;

Connection connection =
  DriverManager.getConnection(FlightHsqldb.URI, FlightHsqldb.USER, FlightHsqldb.PASSWORD);
Statement statement = connection.createStatement();
ResultSet resultSet = statement.executeQuery("select * from \"airports\"");
```

## Using SQLLine

Connect from the command line using the [SQLLine](https://github.com/julianhyde/sqlline) shell:

```
$ ./sqlline
sqlline version 1.12.0
sqlline> !connect jdbc:hsqldb:res:flight FLIGHT TIGER
0: jdbc:hsqldb:res:flight> select * from "aircraft_types";
+------------------+-----------------------------+
| aircraft_type_id | description                 |
+------------------+-----------------------------+
| 1                | Glider                      |
| 2                | Balloon                     |
| 3                | Blimp/Dirigible             |
| 4                | Fixed wing, single engine   |
| 5                | Fixed wing, multiple engine |
| 6                | Rotorcraft                  |
+------------------+-----------------------------+
6 rows selected (0.001 seconds)

0: jdbc:hsqldb:res:flight> select count(*) from "airports";
+--------+
| C1     |
+--------+
| 19795  |
+--------+
1 row selected (0.002 seconds)
```

You may need to edit the `sqlline` or `sqlline.bat` launcher script,
adding `flight-data-hsqldb.jar` to your class path.

## Get flight-data-hsqldb

### From Maven

Get flight-data-hsqldb from
<a href="https://search.maven.org/#search%7Cga%7C1%7Cg%3Anet.hydromatic%20a%3Aflight-data-hsqldb">Maven Central</a>:

```xml
<dependency>
  <groupId>net.hydromatic</groupId>
  <artifactId>flight-data-hsqldb</artifactId>
  <version>0.2</version>
</dependency>
```

### Download and build

Java version 8 or higher.

Check out and build:

```bash
$ git clone git://github.com/julianhyde/flight-data-hsqldb.git
$ cd flight-data-hsqldb
$ ./mvnw install
```

On Windows, the last line is

```bash
> mvnw install
```

## See also

Similar data sets:

* [chinook-data-hsqldb](https://github.com/julianhyde/chinook-data-hsqldb)
* [foodmart-data-hsqldb](https://github.com/julianhyde/foodmart-data-hsqldb)
* [scott-data-hsqldb](https://github.com/julianhyde/scott-data-hsqldb)
* [steelwheels-data-hsqldb](https://github.com/julianhyde/steelwheels-data-hsqldb)

## More information

* License: <a href="LICENSE">Apache License, Version 2.0</a>
* Author: Julian Hyde (<a href="https://twitter.com/julianhyde">@julianhyde</a>)
* Project page: https://www.hydromatic.net/flight-data-hsqldb
* API: https://www.hydromatic.net/flight-data-hsqldb/apidocs
* Source code: https://github.com/julianhyde/flight-data-hsqldb
* Distribution: <a href="https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22flight-data-hsqldb%22">Maven Central</a>
* Developers list:
  <a href="mailto:dev@calcite.apache.org">dev at calcite.apache.org</a>
  (<a href="https://mail-archives.apache.org/mod_mbox/calcite-dev/">archive</a>,
  <a href="mailto:dev-subscribe@calcite.apache.org">subscribe</a>)
* Issues: https://github.com/julianhyde/flight-data-hsqldb/issues
* <a href="HISTORY.md">Release notes and history</a>
