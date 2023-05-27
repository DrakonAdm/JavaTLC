package drakin;

import drakin.dao.CarDaoCSV;
import drakin.dao.LaneDaoCSV;
import drakin.dao.RoadDaoCSV;
import drakin.model.Car;
import drakin.model.Lane;
import drakin.model.Road;
import drakin.reflection.ApplicationContext;
import drakin.reflection.DependencyInjection;

public class MainCSV {

    @DependencyInjection
    private static LaneDaoCSV laneDaoCSV;

    @DependencyInjection
    private static CarDaoCSV carDaoCSV;

    @DependencyInjection
    private static RoadDaoCSV roadDaoCSV;

    static {
        try {
            ApplicationContext.injectDependencies();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Car car = new Car();
        car.setIdentity(1L);
        Lane lane1 = new Lane();
        lane1.getCars().add(car);
        lane1.setIdentity(1L);
        Lane lane2 = new Lane();
        lane2.setIdentity(2L);
        Lane lane3 = new Lane();
        lane3.setIdentity(3L);
        Lane lane4 = new Lane();
        lane4.setIdentity(4L);
        Road road1 = new Road();
        road1.setIdentity(1L);
        Road road2 = new Road();
        road2.setIdentity(2L);
        road1.getLanes().add(lane1);
        road1.getLanes().add(lane2);
        road2.getLanes().add(lane3);
        road2.getLanes().add(lane4);

        car.setSpeed(11);
        car.setName("BMW");

        laneDaoCSV.put(lane1);
        laneDaoCSV.put(lane2);
        laneDaoCSV.put(lane3);
        laneDaoCSV.put(lane4);

        roadDaoCSV.put(road1);
        roadDaoCSV.put(road2);

        carDaoCSV.put(car);

        System.out.println(laneDaoCSV.get(1L));
        System.out.println(laneDaoCSV.get(2L));
        System.out.println(laneDaoCSV.get(3L));
        System.out.println(laneDaoCSV.get(4L));

        System.out.println(roadDaoCSV.get(1L));
        System.out.println(roadDaoCSV.get(2L));

        System.out.println(carDaoCSV.get(1L));
    }
}
