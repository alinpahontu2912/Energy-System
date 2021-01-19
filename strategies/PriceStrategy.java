package strategies;

import entities.Producer;

import java.util.ArrayList;
import java.util.Comparator;

public final class PriceStrategy implements EnergyStrategy {

    Comparator<Producer> comparator = Comparator.comparing(Producer::getPriceKW)
            .thenComparing(Producer::getEnergyPerDistributor, Comparator.reverseOrder())
            .thenComparing(Producer::getId);

    @Override
    public void sortProducer(ArrayList<Producer> producers) {
        producers.sort(Comparator.comparing(Producer::getId));
        producers.sort(comparator);
    }
}
