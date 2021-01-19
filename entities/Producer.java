package entities;

import java.util.ArrayList;
import java.util.Observable;
import java.util.TreeMap;


public final class Producer extends Observable {

    private long id;
    private EnergyType energyType;
    private long maxDistributors;
    private double priceKW;
    private long energyPerDistributor;
    private final TreeMap<Long, ArrayList<Long>> monthlyDistributors;
    private boolean hasChanged;
    private long actualDistributors;

    public Producer(final long id, final EnergyType energyType, final long maxDistributors,
                    final double priceKW, final long energyPerDistributor) {
        this.id = id;
        this.energyType = energyType;
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyPerDistributor = energyPerDistributor;
        this.monthlyDistributors = new TreeMap<>();
        this.hasChanged = false;
        this.actualDistributors = 0;
    }

    public boolean getEnergyType() {
        return energyType.isRenewable();
    }

    public void setEnergyType(EnergyType energyType) {
        this.energyType = energyType;
    }

    public String getType() {
        return energyType.getLabel();
    }

    public long getMaxDistributors() {
        return maxDistributors;
    }

    public void setMaxDistributors(final long maxDistributors) {
        this.maxDistributors = maxDistributors;
    }

    public double getPriceKW() {
        return priceKW;
    }

    public void setPriceKW(final double priceKW) {
        this.priceKW = priceKW;
    }

    public long getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(final long energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public TreeMap<Long, ArrayList<Long>> getMonthlyDistributors() {
        return monthlyDistributors;
    }

    public boolean isHasChanged() {
        return hasChanged;
    }

    public void setHasChanged(final boolean hasChanged) {
        this.hasChanged = hasChanged;
    }

    /**
     *
     * @param needsChange
     */
    public void warnDistributors(final boolean needsChange) {
        setChanged();
        notifyObservers(needsChange);
    }


    public long getActualDistributors() {
        return actualDistributors;
    }

    public void setActualDistributors(long actualDistributors) {
        this.actualDistributors = actualDistributors;
    }
}
