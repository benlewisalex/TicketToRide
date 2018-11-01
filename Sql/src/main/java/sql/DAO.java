package sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DAO {
    private static final String db_prefix = "jdbc:sqlite:";
    private String dbname = db_prefix + "t2r.db";

    static
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected boolean tableExists(String tableName) throws SQLException {
        Connection c = connect();
        try {
            DatabaseMetaData dbm = c.getMetaData();
            ResultSet rs = dbm.getTables(null, null, tableName, null);
            return rs.next();
        } finally {
            c.close();
        }
    }

    private Connection connect() throws SQLException
    {
        return DriverManager.getConnection(dbname);
    }

    private void addArgs(PreparedStatement stmt, String... raw_args) throws SQLException
    {
        List<String> args = Arrays.asList(raw_args);

        for(int i = 0; i < args.size(); )
        {
            String s = args.get(i);
            stmt.setString(++i, s);
        }
    }

    protected int execute(String command, String... args) throws SQLException
    {
        Connection connection = connect();
        try
        {
            PreparedStatement stmt = connection.prepareStatement(command);
            try
            {
                addArgs(stmt, args);
                return stmt.executeUpdate();
            }
            finally
            {
                stmt.close();
            }
        }
        finally
        {
            connection.close();
        }
    }

    protected List<List<String>> runQuery(String query, String... args) throws SQLException
    {
        List<List<String>> rows = new LinkedList<List<String>>();

        Connection connection = connect();
        try
        {
            PreparedStatement stmt = connection.prepareStatement(query);
            try
            {
                addArgs(stmt, args);
                ResultSet results = stmt.executeQuery();
                try
                {
                    while(results.next())
                    {
                        List<String> current_row = new LinkedList<String>();
                        for(int i = 0; i < results.getMetaData().getColumnCount(); )
                        {
                            current_row.add(results.getString(++i));
                        }
                        rows.add(current_row);
                    }
                }
                finally
                {
                    results.close();
                }
            }
            finally
            {
                stmt.close();
            }
        }
        finally
        {
            connection.close();
        }

        return rows;
    }
}
