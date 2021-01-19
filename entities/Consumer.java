package entities;

import java.util.Comparator;

public final class Consumer extends Entity implements Comparable<Consumer> {

    private long income;
    private boolean postpone;
    private long numMonths;
    private long currentDist;
    private long oldDist;
    private long restanta;
    private long priceToPay;
    private boolean ok;

    public Consumer(final long budget, final long income, final long id) {
        this.budget = budget;
        this.income = income;
        this.id = id;
        this.postpone = false;
        this.numMonths = -1;
        this.isBankrupt = false;
        this.currentDist = -1;
        this.oldDist = -1;
        this.restanta = 0;
        this.priceToPay = 0;
        this.ok = false;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(final boolean ok) {
        this.ok = ok;
    }

    public long getPriceToPay() {
        return priceToPay;
    }

    public void setPriceToPay(final long priceToPay) {
        this.priceToPay = priceToPay;
    }

    public long getRestanta() {
        return restanta;
    }

    public void setRestanta(final long restanta) {
        this.restanta = restanta;
    }

    public long getIncome() {
        return income;
    }

    public long getCurrentDist() {
        return currentDist;
    }

    public void setCurrentDist(final long currentDist) {
        this.currentDist = currentDist;
    }

    public long getOldDist() {
        return oldDist;
    }

    public void setOldDist(final long oldDist) {
        this.oldDist = oldDist;
    }

    public long getMonthlyIncome() {
        return income;
    }

    public void setMonthlyIncome(final long income1) {
        this.income = income1;
    }

    public boolean isPostpone() {
        return postpone;
    }

    public void setPostpone(final boolean postpone) {
        this.postpone = postpone;
    }

    public long getNumMonths() {
        return numMonths;
    }

    public void setNumMonths(final long numMonths) {
        this.numMonths = numMonths;
    }

    @Override
    public int compareTo(final Consumer o) {
        return Comparator.comparing(Consumer::getNumMonths)
                .thenComparing(Consumer::getCurrentDist)
                .compare(this, o);
    }


}
