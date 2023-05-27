package drakin.dao;

import drakin.model.Car;
import drakin.model.Lane;
import drakin.reflection.DependencyInjection;
import org.jetbrains.annotations.NotNull;
import drakin.reflection.Component;
import drakin.utils.Utils;

import java.io.IOException;
import java.util.*;


@Component
public class LaneDaoCSV extends AbstractCSVFileDAO<Long, Lane> {

    @DependencyInjection
    private CarDaoCSV ac;

    public LaneDaoCSV() throws IOException {
        super("LaneTable");
    }

    @Override
    public void put(@NotNull Lane object) {
        var key = object.getIdentity();
        var fields = new Object[] {
                Utils.objectsToList(object.getCars())
        };

        try {
            this.putInCSVFile(key, fields);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<Lane> get(Long key) {
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
        List<Long> idCars = Utils.strToList((String) fields[1]);
        List<Car> cars = new ArrayList<>();
        for (Long id: idCars) {
            cars.add(ac.get(id).get());
        }
        Lane lane = new Lane();
        lane.setCars(cars);
        lane.setIdentity(key);
        return Optional.of(lane);
    }

}
