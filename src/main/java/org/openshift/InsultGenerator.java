package org.openshift;

import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class InsultGenerator {
	public String generateInsult()
	{

		String vowels = "AEIOU";
		String article = "an";
		String theInsult = "";
		try {
			String databaseUrl = "jdbc:postgreswl://";
			databaseUrl += System.getenv("POSTGRESQL_SERVICE_HOST");
			databaseUrl += System.getenv("POSTGRESQL_DATABASE");
			String username = System.getenv("POSTGRESQL_USER");
			String password = System.getenv("PGPASSWORD");

			Connection connection = DriverManager.getConnection(databaseUrl, username, password);

			if (connection != null) {
				String sql = "select a.string AS first , b.string AS second, c.string AS noun from short_adjective a, long_adjective b, noun c ORDER BY random() limit 1";
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql);
				while (resultSet.next()) {
					if (vowels.indexOf(resultSet.getString("first").charAt(0)) == -1) {
						article = "a";
					}
					theInsult = String.format("Thou art %s %s %s %s!", article, resultSet.getString("first"), resultSet.getString("second"), resultSet.getString("noun"));
				}
				resultSet.close();
				connection.close();
			}

		} catch (Exception e) {
			System.out.println("error");
		}
		return theInsult;
	}
}
