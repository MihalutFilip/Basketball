package repository;

import model.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ClientRepository implements IRepository<Integer, Client>{
    private DatabaseConnection connectionClass;
    private static final Logger logger = LogManager.getLogger();

    public ClientRepository(Properties properties) {
        logger.info("Initializing SortingTaskRepository with properties: {} ", properties);
        connectionClass = new DatabaseConnection(properties);
    }

    @Override
    public Client findOne(Integer id) {
        logger.traceEntry("finding client with id {} ", id);
        Connection connection = connectionClass.getConnection();

        try(PreparedStatement statement = connection.prepareStatement("select * from Client where id=?")){
            statement.setInt(1, id);
            try(ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    String name = result.getString("name");
                    Client client = new Client(name);
                    client.setId(id);
                    logger.traceExit(client);
                    return client;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ ex);
        }
        logger.traceExit("No task found with id {}", id);
        return null;
    }

    @Override
    public Iterable<Client> findAll() {
        logger.traceEntry("finding all clients");
        Connection connection = connectionClass.getConnection();
        List<Client> clients = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("select * from Client")) {
            try(ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    Client client = new Client(name);
                    client.setId(id);
                    clients.add(client);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e );
        }
        logger.traceExit(clients);
        return clients;
    }

    @Override
    public void save(Client client) {
        logger.traceEntry("saving client {} ", client);
        Connection connection = connectionClass.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("insert into Client(name) values (?)")){
            statement.setString(1, client.getName());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0)
                throw new SQLException("Creating client failed, no rows affected.");

        } catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleting client with {}", id);
        Connection connection = connectionClass.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("delete from Client where id=?")){
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0)
                throw new SQLException("Deleting client failed, no rows affected.");
        } catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Client client) {
        logger.traceEntry("Updating client with {}", client.getId());
        Connection connection = connectionClass.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("UPDATE Client Set name=? where id=?")){
            statement.setInt(2, client.getId());
            statement.setString(1, client.getName());
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
