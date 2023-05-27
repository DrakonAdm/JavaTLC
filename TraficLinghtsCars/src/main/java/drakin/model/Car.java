package drakin.model;
import drakin.dao.IdentityInterface;

public class Car implements IdentityInterface<Long> {

    private String name;
    private int speed;
    private Long identity;

    public Car() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
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
        return "Car{" +
                "name='" + name + '\'' +
                ", speed=" + speed +
                ", identity=" + identity +
                '}';
    }
}
