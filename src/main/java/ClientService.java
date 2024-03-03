import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientService {

    private static final String SELECT_BY_NAME_STRING = "SELECT ID FROM client WHERE NAME = ? ";
    private static final String INSERT_NEW_CLIENT = "INSERT INTO client (NAME) VALUES (?)";
    private static final String SELECT_MAX_ID = "SELECT MAX(ID) AS maxId FROM client";
    private static final String SELECT_BY_ID = "SELECT NAME FROM client WHERE ID = ? ";
    private static final String UPDATE_NAME_BY_ID = "UPDATE client SET NAME = ? WHERE ID = ? ";
    private static final String DELETE_BY_ID = "DELETE FROM client WHERE ID = ? ";
    private static final String SELECT_ALL_CLIENTS = "SELECT ID, NAME FROM client";
    private static final String FOREIGN_KEY_CHECKS = "SET FOREIGN_KEY_CHECKS = ? ";

    private Connection connection;

    private PreparedStatement selectIdSt;
    private PreparedStatement insertSt;
    private PreparedStatement selectMaxIdSt;
    private PreparedStatement getNameSt;
    private PreparedStatement updateClnSt;
    private PreparedStatement deleteSt;
    private PreparedStatement selectAllSt;
    private PreparedStatement foreignKeyCheckSt;

    public ClientService(Connection connection) {
        this.connection = connection;
    }

    public long create(String name) throws SQLException {

        long id = -1;
        try {
            // "INSERT INTO client (NAME) VALUES (?)"
            insertSt = connection.prepareStatement(INSERT_NEW_CLIENT);
            insertSt.setString(1, name);
            insertSt.executeUpdate();

            // "SELECT MAX(ID) AS maxId FROM client"
            selectMaxIdSt = connection.prepareStatement(SELECT_MAX_ID);
            ResultSet resultSet = selectMaxIdSt.executeQuery();
            resultSet.next();
            id = resultSet.getLong("maxId");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return id;
    }
    public String getById(long id){

        String name = null;
        try{
            // "SELECT NAME FROM client WHERE ID = ? "
            getNameSt = connection.prepareStatement(SELECT_BY_ID);
            getNameSt.setLong(1, id);
            ResultSet resultSet = getNameSt.executeQuery();
            resultSet.next();
            name = resultSet.getString("NAME");


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return name;
    }
    public void setName(long id, String name){

        // UPDATE_NAME_BY_ID = "UPDATE client SET NAME = ? WHERE ID = ? "
        try{
            updateClnSt = connection.prepareStatement(UPDATE_NAME_BY_ID);
            updateClnSt.setString(1, name); // new NAME
            updateClnSt.setLong(2, id);
            updateClnSt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public void deleteById(long id){

        // DELETE_BY_ID = "DELETE FROM client WHERE ID = ? ";
        try{
            // "SET FOREIGN_KEY_CHECKS = ? "
            foreignKeyCheckSt = connection.prepareStatement(FOREIGN_KEY_CHECKS);
            foreignKeyCheckSt.setInt(1, 0); // to disable
            boolean executedToDisable = foreignKeyCheckSt.execute();
            System.out.println("FOREIGN KEY CHECK Constraint is Disabled");

            deleteSt = connection.prepareStatement(DELETE_BY_ID);
            deleteSt.setLong(1, id);
            int deleteExecuted = deleteSt.executeUpdate();
            System.out.println("Deleted records = " + deleteExecuted);

            // "SET FOREIGN_KEY_CHECKS = ? "
            foreignKeyCheckSt = connection.prepareStatement(FOREIGN_KEY_CHECKS);
            foreignKeyCheckSt.setInt(1, 1); // to enable
            boolean executedToEnable = foreignKeyCheckSt.execute();
            System.out.println("FOREIGN KEY CHECK Constraint is Enabled");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public List<Client> listAll() {
        // SELECT_ALL_CLIENTS = "SELECT ID, NAME FROM client";
        List<Client> clientList = new ArrayList<>();
        try{
            selectAllSt = connection.prepareStatement(SELECT_ALL_CLIENTS);
            ResultSet resultSet = selectAllSt.executeQuery();

            while (resultSet.next()){
                Client client = new Client();
                client.setId(resultSet.getInt("ID"));
                client.setName(resultSet.getString("NAME"));
                clientList.add(client);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return clientList;
    }

}
