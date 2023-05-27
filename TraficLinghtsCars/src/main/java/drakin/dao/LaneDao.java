package drakin.dao;

import drakin.model.Car;
import drakin.model.Lane;
import drakin.model.Road;
import drakin.reflection.Component;
import drakin.reflection.DependencyInjection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class LaneDao implements DAO<Long, Lane> {

    private Connection connection;

    @DependencyInjection
    private CarDao carDao;


    public void setConnection(Connection connection) {
        this.connection = connection;
        carDao.setConnection(connection);
    }

    @Override
    public void put(Lane object) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO \"lane\" (road_id) VALUES (null) RETURNING id");

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long id = resultSet.getLong("id");


            for (Car car : object.getCars()) {
                carDao.update(car.getIdentity(), id);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Long id, Long road) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE \"lane\" SET road_id = ?  WHERE id = ?");

            statement.setInt(1, road.intValue());
            statement.setInt(2, id.intValue());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Lane> get(Long key) {
        Lane lane = new Lane();
        lane.setIdentity(key);
        List<Optional<Car>> cars = carDao.getByLane(key);

        for (Optional<Car> car : cars) {
            lane.getCars().add(car.get());
        }

        return Optional.of(lane);

    }

    public List<Optional<Lane>> getByLane(Long key) {
        List<Optional<Lane>> lanes = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"lane\" WHERE lane_id = ?");
            statement.setInt(1, key.intValue());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Lane lane = get(id).get();

                lanes.add(Optional.of(lane));
            }


            return lanes;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
