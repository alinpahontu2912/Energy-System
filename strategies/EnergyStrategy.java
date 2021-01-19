package strategies;

import entities.Producer;

import java.util.ArrayList;

public interface EnergyStrategy {

    /**
     * @param producers the producers list
     */
    void sortProducer(ArrayList<Producer> producers);

}
