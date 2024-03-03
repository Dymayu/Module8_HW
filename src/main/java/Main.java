import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        Connection connection = Database.getInstance().getConnection();
        ClientService clientService = new ClientService(connection);
        // add client with NAME and return ID
        System.out.println("Client with ID added => " + clientService.create("AUDI"));

        // get client NAME by ID
        String clientById = clientService.getById(4);
        System.out.println("Found Client with NAME => " + clientById);

        // set new NAME for the client with ID
        clientService.setName(3, "TOYOTA Corp"); // old name: TOYOTA

        // delete client with ID
        clientService.deleteById(4); // name: VOLVO

        // return all clients
        for (Client client : clientService.listAll()) {
            System.out.println("=====>>>>> " + client);
        }

    }
}
