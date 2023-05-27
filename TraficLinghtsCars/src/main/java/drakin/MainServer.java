package drakin;
import drakin.controller.CarController;
import drakin.controller.LaneController;
import drakin.controller.RoadController;
import drakin.reflection.ApplicationContext;
import drakin.reflection.DependencyInjection;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainServer {


    @DependencyInjection
    private static CarController carController;

    @DependencyInjection
    private static LaneController laneController;

    @DependencyInjection
    private static RoadController roadController;

    private static final String URL = "jdbc:postgresql://localhost:9100/cars";
    private static final String USER_NAME = "admin";
    private static final String PASSWORD = "root";

    static {
        try {
            ApplicationContext.injectDependencies();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection;
        try {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        carController.getDao().setConnection(connection);
        laneController.getDao().setConnection(connection);
        roadController.getDao().setConnection(connection);
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/api");

        servletContextHandler.addServlet(new ServletHolder(carController), "/car");
        servletContextHandler.addServlet(new ServletHolder(laneController), "/lane");
        servletContextHandler.addServlet(new ServletHolder(roadController), "/road");

        server.setHandler(servletContextHandler);

        server.start();
        server.join();
    }
}
