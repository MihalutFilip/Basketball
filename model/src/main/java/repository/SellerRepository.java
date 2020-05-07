package repository;

import model.Client;
import model.Seller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SellerRepository implements IRepository<Integer, Seller>{
    private DatabaseConnection connectionClass;
    private static final Logger logger = LogManager.getLogger();

    public SellerRepository(Properties properties) {
        logger.info("Initializing SortingTaskRepository with properties: {} ", properties);
        connectionClass = new DatabaseConnection(properties);
    }

    @Override
    public Seller findOne(Integer id) {
        logger.traceEntry("finding seller with id {} ", id);
        Connection connection = connectionClass.getConnection();

        try(PreparedStatement statement = connection.prepareStatement("select * from Seller where id=?")){
            statement.setInt(1, id);
            try(ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    String name = result.getString("name");
                    String password = result.getString("password");
                    Seller seller = new Seller(name, password);
                    seller.setId(id);
                    logger.traceExit(seller);
                    return seller;
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
    public Iterable<Seller> findAll() {
        logger.traceEntry("finding all sellers");
        Connection connection = connectionClass.getConnection();
        List<Seller> sellers = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("select * from Seller")) {
            try(ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    String password = result.getString("password");
                    Seller seller = new Seller(name, password);
                    seller.setId(id);
                    sellers.add(seller);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e );
        }
        logger.traceExit(sellers);
        return sellers;
    }

    @Override
    public void save(Seller seller) {
        logger.traceEntry("saving seller {} ", seller);
        Connection connection = connectionClass.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("insert into Seller(name, password) values (?,?)")){
            statement.setString(1, seller.getName());
            statement.setString(2, seller.getPassword());
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
        logger.traceEntry("deleting seller with {}", id);
        Connection connection = connectionClass.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("delete from Seller where id=?")){
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0)
                throw new SQLException("Deleting seller failed, no rows affected.");

        } catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Seller seller) {
        logger.traceEntry("Updating seller with {}", seller.getId());
        Connection connection = connectionClass.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("UPDATE Seller Set name=?, password=? where id=?")) {
            statement.setString(1, seller.getName());
            statement.setString(2, seller.getPassword());
            statement.setInt(3, seller.getId());

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
