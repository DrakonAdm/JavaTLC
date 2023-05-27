package drakin.dao;

import drakin.model.Lane;
import drakin.model.Road;
import drakin.reflection.Component;
import drakin.reflection.DependencyInjection;
import drakin.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RoadDaoCSV extends AbstractCSVFileDAO<Long, Road> {

    @DependencyInjection
    private LaneDaoCSV laneDaoCSV;
    public RoadDaoCSV() throws IOException {
        super("roadTable");
    }

    @Override
    public void put(Road object) {
        var key = object.getIdentity();
        var fields = new Object[] {
                Utils.objectsToList(object.getLanes())
        };

        try {
            this.putInCSVFile(key, fields);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<Road> get(Long key) {
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
        List<Long> idLanes = Utils.strToList((String) fields[1]);
        List<Lane> lanes = new ArrayList<>();
        for (Long id: idLanes) {
            lanes.add(laneDaoCSV.get(id).get());
        }
        var road = new Road();
        road.setLanes(lanes);
        road.setIdentity(key);
        return Optional.of(road);
    }
}
