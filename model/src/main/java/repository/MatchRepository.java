package repository;

import model.Client;
import model.Match;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MatchRepository implements IRepository<Integer, Match> {
    private DatabaseConnection connectionClass;
    private static final Logger logger = LogManager.getLogger();

    public MatchRepository(Properties properties) {
        logger.info("Initializing SortingTaskRepository with properties: {} ", properties);
        connectionClass = new DatabaseConnection(properties);
    }

    @Override
    public Match findOne(Integer id) {
        logger.traceEntry("finding match with id {} ", id);
        Connection connection = connectionClass.getConnection();

        try(PreparedStatement statement = connection.prepareStatement("select * from Match where id=?")){
            statement.setInt(1, id);
            try(ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    String name = result.getString("name");
                    Integer price = result.getInt("price");
                    Integer places = result.getInt("places");
                    Match match = new Match(name, price, places);
                    match.setId(id);
                    logger.traceExit(match);
                    return match;
                }
            }
        } catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ ex);
        }
        logger.traceExit("No task found with id {}", id);
        return null;
    }

    @Override
    public Iterable<Match> findAll() {
        logger.traceEntry("finding all matches");
        Connection connection = connectionClass.getConnection();
        List<Match> matches = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("select * from Match")) {
            try(ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    Integer price = result.getInt("price");
                    Integer places = result.getInt("places");
                    Match match = new Match(name, price, places);
                    match.setId(id);
                    matches.add(match);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e );
        }
        logger.traceExit(matches);
        return matches;
    }

    @Override
    public void save(Match match) {
        logger.traceEntry("saving match {} ", match);
        Connection connection = connectionClass.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("insert into Match(name, price, places) values (?,?,?)")){
            statement.setString(1, match.getName());
            statement.setInt(2, match.getPrice());
            statement.setInt(3, match.getPlacesRemaining());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0)
                throw new SQLException("Creating match failed, no rows affected.");

        } catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleting match with {}", id);
        Connection connection = connectionClass.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("delete from Match where id=?")){
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0)
                throw new SQLException("Deleting match failed, no rows affected.");

        } catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Match match) {
        logger.traceEntry("Updating match with {}", match.getId());
        Connection connection = connectionClass.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("UPDATE MATCH Set name=?, price=?, places=? where id=?")) {
            statement.setString(1, match.getName());
            statement.setInt(2, match.getPrice());
            statement.setInt(3, match.getPlacesRemaining());
            statement.setInt(4, match.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0)
                throw new SQLException("Updating client failed, no rows affected.");

        } catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }
}
