package drakin.dao;

import drakin.model.Car;
import drakin.reflection.Component;
import drakin.reflection.DependencyInjection;

import java.io.IOException;
import java.util.Optional;

@Component
public class CarDaoCSV extends AbstractCSVFileDAO<Long, Car> {


    @DependencyInjection
    private RoadDaoCSV roadDaoCSV;
    @DependencyInjection
    private LaneDaoCSV laneDaoCSV;
    public CarDaoCSV() throws IOException {
        super("carTable");
    }

    @Override
    public void put(Car object) {
        var key = object.getIdentity();
        var fields = new Object[] {
                object.getName(),
                object.getSpeed()
        };

        try {
            this.putInCSVFile(key, fields);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<Car> get(Long key) {
        Optional<Object[]> fieldsInternal;
        try {
            fieldsInternal = this.getFromCSV(key);
        } catch (IOException e) {
            throw  new IllegalStateException(e);
        }

        if (fieldsInternal.isEmpty()) {
            return  Optional.empty();
        }

        var fields = fieldsInternal.get();
        var car = new Car();
        car.setName((String) fields[1]);
        car.setSpeed(Integer.parseInt((String) fields[2]));

        car.setIdentity(key);
        return Optional.of(car);
    }
}
