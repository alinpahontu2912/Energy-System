package strategies;

import entities.Producer;

import java.util.ArrayList;

public interface EnergyStrategy {

     /**
      *
      * @param producers
      */
    void sortProducer(ArrayList<Producer> producers);

}
