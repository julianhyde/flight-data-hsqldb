/*
 * Licensed to Julian Hyde under one or more contributor license
 * agreements.  See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership. Julian Hyde
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hydromatic.flight.data.hsqldb;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Kick the tires!
 */
public class FlightHsqldbTest {
  @Test public void test() throws SQLException {
    final Connection connection = DriverManager.getConnection(FlightHsqldb.URI,
        FlightHsqldb.USER, FlightHsqldb.PASSWORD);
    final Statement statement = connection.createStatement();
    checkThatTableRowCount(statement, "accidents", is(70_777));
    checkThatTableRowCount(statement, "aircraft", is(359_928));
    checkThatTableRowCount(statement, "aircraft_engine_types", is(8));
    checkThatTableRowCount(statement, "aircraft_engines", is(761));
    checkThatTableRowCount(statement, "aircraft_models", is(60_461));
    checkThatTableRowCount(statement, "aircraft_types", is(6));
    checkThatTableRowCount(statement, "airport_remarks", is(69_708));
    checkThatTableRowCount(statement, "airports", is(19_795));
    checkThatTableRowCount(statement, "carriers", is(21));
    checkThatTableRowCount(statement, "exceptions", is(702));
    // "ontime" is partially disabled because too large; it should have
    // 38,483,559 rows but we only load the first segment of 51 segments.
    checkThatTableRowCount(statement, "ontime", is(399_999));
    checkThatTableRowCount(statement, "states", is(58));
    checkThatTableRowCount(statement, "zipcodes", is(29_467));
    statement.close();
    connection.close();
  }

  private void checkThatTableRowCount(Statement statement, String tableName,
      Matcher<Integer> rowCountMatcher) throws SQLException {
    final String sql = "select * from \"" + tableName + "\"";
    final ResultSet resultSet = statement.executeQuery(sql);
    final ResultSetMetaData metaData = resultSet.getMetaData();
    final int columnCount = metaData.getColumnCount();
    int rowCount = 0;
    System.out.println(tableName + ":");
    while (resultSet.next()) {
      if (rowCount++ < 5) {
        for (int i = 0; i < columnCount; i++) {
          if (i > 0) {
            System.out.print(", ");
          }
          System.out.print(metaData.getColumnLabel(i + 1));
          System.out.print(": ");
          System.out.print(resultSet.getObject(i + 1));
        }
        System.out.println();
      }
    }
    assertThat("rows in " + tableName, rowCount, rowCountMatcher);
    resultSet.close();
  }
}

// End FlightHsqldbTest.java
