package entities;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public final class Distributor extends Entity implements Observer {

    private long contractLength;
    private long infrastructureCost;
    private long productionCost;
    private long numberOfConsumers;
    private long energyNeededKW;
    private String producerStrategy;
    private ArrayList<Long> producerIds;
    private boolean resetProducers;
    private long totalEnergy;
    private ArrayList<Long> pricesHistory;

    public Distributor(final long id, final long contractLength, final long budget,
                       final long infrastructureCost, final long energyNeededKW, final String producerStrategy) {
        this.id = id;
        this.contractLength = contractLength;
        this.budget = budget;
        this.infrastructureCost = infrastructureCost;
        this.isBankrupt = false;
        this.energyNeededKW = energyNeededKW;
        this.producerStrategy = producerStrategy;
        this.numberOfConsumers = 0;
        this.productionCost = 0;
        this.producerIds = new ArrayList<>();
        this.resetProducers = true;
        this.totalEnergy = 0;
        this.pricesHistory = new ArrayList<>();
    }

    public ArrayList<Long> getPricesHistory() {
        return pricesHistory;
    }

    public long getEnergyNeededKW() {
        return energyNeededKW;
    }

    public void setEnergyNeededKW(final long energyNeededKW) {
        this.energyNeededKW = energyNeededKW;
    }

    public String getProducerStrategy() {
        return producerStrategy;
    }

    public void setProducerStrategy(final String producerStrategy) {
        this.producerStrategy = producerStrategy;
    }

    public long getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setInfrastructureCost(final long infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public void setProductionCost(final long productionCost) {
        this.productionCost = productionCost;
    }

    public long getNumberOfConsumers() {
        return numberOfConsumers;
    }

    public void setNumberOfConsumers(final long numberOfConsumers) {
        this.numberOfConsumers = numberOfConsumers;
    }

    public ArrayList<Long> getProducerIds() {
        return producerIds;
    }

    public boolean isResetProducers() {
        return resetProducers;
    }

    public void setResetProducers(final boolean resetProducers) {
        this.resetProducers = resetProducers;
    }

    public long getTotalEnergy() {
        return totalEnergy;
    }

    public void setTotalEnergy(final long totalEnergy) {
        this.totalEnergy = totalEnergy;
    }

    /**
     * @return the profit wanted by the distributor
     */
    public long profit() {
        final double profitMargin = 0.2d;
        return Math.round(Math.floor(profitMargin * productionCost));

    }

    /**
     * @return contract price of the distributor
     */
    public long contractPrice() {
        long price;
        if (numberOfConsumers == 0) {
            price = infrastructureCost + productionCost + profit();
        } else {
            price = Math.round(Math.floor(infrastructureCost / numberOfConsumers
                    + productionCost + profit()));
        }
        return price;
    }

    /**
     * @return the expenses of every disttributor
     */
    public long monthlyExpenses() {
        return infrastructureCost + productionCost * numberOfConsumers;
    }

    public long getContractLength() {
        return contractLength;
    }

    public void setContractLength(final long contractLength) {
        this.contractLength = contractLength;
    }


    @Override
    public void update(Observable o, Object arg) {
        this.resetProducers = (boolean) arg;
    }
}
