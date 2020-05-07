package repository;

import javafx.util.Pair;
import model.Client;
import model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TicketRepository implements IRepository<Pair, Ticket> {
    private DatabaseConnection connectionClass;
    private static final Logger logger = LogManager.getLogger();

    public TicketRepository(Properties properties) {
        logger.info("Initializing SortingTaskRepository with properties: {} ", properties);
        connectionClass = new DatabaseConnection(properties);
    }

    @Override
    public Ticket findOne(Pair id) {
        logger.traceEntry("finding ticket with id {} ", id);
        Connection connection = connectionClass.getConnection();

        try(PreparedStatement statement = connection.prepareStatement("select * from Ticket where client_id=? and match_id=?")){
            statement.setInt(1, (Integer) id.getKey());
            statement.setInt(2, (Integer) id.getValue());
            try(ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    Integer placesTaken = result.getInt("places");
                    Ticket ticket = new Ticket(placesTaken);
                    ticket.setId(id);
                    logger.traceExit(ticket);
                    return ticket;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ ex);
        }
        logger.traceExit("No ticket found with id {}", id);
        return null;
    }

    @Override
    public Iterable<Ticket> findAll() {
        logger.traceEntry("finding all tickets");
        Connection connection = connectionClass.getConnection();
        List<Ticket> tickets = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("select * from Ticket")) {
            try(ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    int clientId = result.getInt("client_id");
                    int matchId = result.getInt("match_id");
                    int placesTaken = result.getInt("places");
                    Ticket ticket = new Ticket(placesTaken);
                    ticket.setId(new Pair(clientId, matchId));
                    tickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e );
        }
        logger.traceExit(tickets);
        return tickets;
    }

    @Override
    public void save(Ticket ticket) {
        logger.traceEntry("saving ticket {} ", ticket);
        Connection connection = connectionClass.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("insert into Ticket values (?,?,?)")){
            statement.setInt(1, (Integer) ticket.getId().getKey());
            statement.setInt(2, (Integer) ticket.getId().getValue());
            statement.setInt(3, ticket.getPlacesTaken());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0)
                throw new SQLException("Creating ticket failed, no rows affected.");

        } catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Pair id) {
        logger.traceEntry("deleting ticket with {}", id);
        Connection connection = connectionClass.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("delete from Ticket where client_id=? and match_id=?")){
            statement.setInt(1, (Integer) id.getKey());
            statement.setInt(2, (Integer) id.getValue());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0)
                throw new SQLException("Deleting ticket failed, no rows affected.");
        } catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Ticket ticket) {
        logger.traceEntry("Updating ticket with {}", ticket.getId());
        Connection connection = connectionClass.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("UPDATE Client Set places=? where client_id=? and match_id=?")) {
            statement.setInt(1, ticket.getPlacesTaken());
            statement.setInt(2, (Integer) ticket.getId().getKey());
            statement.setInt(3, (Integer) ticket.getId().getValue());
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
