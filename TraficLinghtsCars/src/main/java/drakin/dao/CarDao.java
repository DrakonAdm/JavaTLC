package drakin.dao;
import drakin.model.Car;
import drakin.reflection.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CarDao implements DAO<Long, Car> {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void put(Car object) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO \"car\" (name, speed) VALUES (?, ?)");

            statement.setString(1, object.getName());
            statement.setInt(2, object.getSpeed());


            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(Long id, Long lane) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE \"car\" SET lane_id = ?  WHERE id = ?");

            statement.setInt(1, lane.intValue());
            statement.setInt(2, id.intValue());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Car> get(Long key) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"car\" WHERE id = ?");
            statement.setInt(1, key.intValue());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            Integer speed = resultSet.getInt("speed");
            Car car = new Car();
            car.setName(name);
            car.setSpeed(speed);
            car.setIdentity(id);

            return Optional.of(car);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Optional<Car>> getByLane(Long key) {
        List<Optional<Car>> cars = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"car\" WHERE lane_id = ?");
            statement.setInt(1, key.intValue());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                Integer speed = resultSet.getInt("speed");
                Car car = new Car();
                car.setName(name);
                car.setSpeed(speed);
                car.setIdentity(id);

                cars.add(Optional.of(car));
            }


            return cars;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
