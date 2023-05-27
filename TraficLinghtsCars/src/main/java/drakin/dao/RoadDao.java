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
import java.util.List;
import java.util.Optional;

@Component
public class RoadDao implements DAO<Long, Road> {

    private Connection connection;

    @DependencyInjection
    private LaneDao laneDao;


    public void setConnection(Connection connection) {
        this.connection = connection;
        laneDao.setConnection(connection);
    }

    @Override
    public void put(Road object) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO \"road\" DEFAULT VALUES RETURNING id");

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long id = resultSet.getLong("id");


            for (Lane lane : object.getLanes()) {
                laneDao.update(lane.getIdentity(), id);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Road> get(Long key) {
        Road road = new Road();
        road.setIdentity(key);
        List<Optional<Lane>> cars = laneDao.getByLane(key);

        for (Optional<Lane> lane : cars) {
            road.getLanes().add(lane.get());
        }

        return Optional.of(road);
    }


}
