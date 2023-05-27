package drakin.model;

import drakin.dao.IdentityInterface;

import java.util.ArrayList;
import java.util.List;

public class Road implements IdentityInterface<Long> {
    private List<Lane> lanes;
    private Long identity;

    public Road() {
        lanes = new ArrayList<>();
    }

    public List<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(List<Lane> lanes) {
        this.lanes = lanes;
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
        return "Road{" +
                "lanes=" + lanes +
                ", identity=" + identity +
                '}';
    }
}
