package drakin.model;

import drakin.dao.IdentityInterface;

import java.util.ArrayList;
import java.util.List;

public class Lane implements IdentityInterface<Long> {
    private List<Car> cars;
    private Long identity;


    public List<Car> getCars() {
        return cars;
    }

    public Lane() {
        cars = new ArrayList<>();
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public Long getIdentity() {
        return identity;
    }

    @Override
    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "Lane{" +
                "cars=" + cars +
                ", identity=" + identity +
                '}';
    }
}
