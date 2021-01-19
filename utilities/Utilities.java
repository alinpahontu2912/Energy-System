package utilities;

import entities.Consumer;
import entities.Distributor;
import entities.Producer;
import strategies.EnergyChoiceFactory;
import strategies.EnergyStrategy;

import java.util.ArrayList;

public final class Utilities {

    /**
     * @param distributors the distributor list
     * @return false if all distributors are bankrupt, true otherwise
     */
    public boolean verifyDistributors(final ArrayList<Distributor> distributors) {
        int i = 0;
        int bankrupted = 0;
        while (i < distributors.size()) {
            if (!distributors.get(i).isBankrupt()) {
                bankrupted++;
            }
            i++;
        }
        return (bankrupted > distributors.size());
    }

    /**
     * @param distributors the distributor list
     * @return the distributor with the cheapest contract
     */
    public Distributor getCheapest(final ArrayList<Distributor> distributors) {
        long minid = 0;
        while (distributors.get((int) minid).isBankrupt()) {
            minid++;
        }
        Distributor wanted = distributors.get((int) minid);
        for (Distributor distributor : distributors) {
            if (!distributor.isBankrupt()
                    && distributor.contractPrice() < distributors
                    .get((int) minid).contractPrice()) {
                minid = distributor.getId();
                wanted = distributor;
            }
        }
        return wanted;
    }

    /**
     * @param consumers the consumers salary
     *                  give the monthly income to every non-bankrupt consumer
     */
    public void payconsMonthly(final ArrayList<Consumer> consumers) {
        for (Consumer consumer : consumers) {
            if (!consumer.isBankrupt()) {
                consumer.setBudget(consumer.getBudget() + consumer.getIncome());
                consumer.setNumMonths(consumer.getNumMonths() - 1);
            }
        }
    }

    /**
     * @param distributors the distributors list
     * @param consumers    the consumers list
     *                     removes bankrupted consumers from distributor
     */
    public void removeBankrupt(final ArrayList<Distributor> distributors,
                               final ArrayList<Consumer> consumers) {
        for (Consumer consumer : consumers) {
            if (consumer.isBankrupt() && !consumer.isOk()) {
                consumer.setOk(true);
                distributors.get((int) consumer.getCurrentDist()).setNumberOfConsumers(
                        distributors.get((int) consumer.getCurrentDist())
                                .getNumberOfConsumers() - 1);
            }
        }
    }

    /**
     * @param distributor the cheapest distributor
     * @param consumers   the list of cconsumers
     *                    gives every consumer the cheapest
     */
    public void assignDistributor(final Distributor distributor,
                                  final ArrayList<Consumer> consumers) {
        for (Consumer consumer : consumers) {
            if (!consumer.isBankrupt()) {
                if (consumer.getCurrentDist() == -1 || consumer.getNumMonths() == 0) {
                    consumer.setCurrentDist(distributor.getId());
                    consumer.setPriceToPay(distributor.contractPrice());
                    consumer.setNumMonths(distributor.getContractLength());

                }
            }
        }
    }

    /**
     * @param consumers    the consumer list
     * @param distributors the distributor list
     *                     resets the distributors'  contract number
     *                     by counting the number of valid contraccts
     */
    public void countcontracts(final ArrayList<Consumer> consumers,
                               final ArrayList<Distributor> distributors) {
        for (Distributor distributor : distributors) {
            if (!distributor.isBankrupt()) {
                distributor.setNumberOfConsumers(0);
                for (Consumer consumer : consumers) {
                    if (!consumer.isBankrupt()
                            && consumer.getCurrentDist() == distributor.getId()) {
                        distributor.setNumberOfConsumers(distributor.getNumberOfConsumers() + 1);
                    }
                }
            }
        }
    }

    /**
     * @param distributors the distributor list
     * @param consumers    the consumer list
     *                     each non bankrupt consumer pays its distributor
     */
    public void payDistributors(final ArrayList<Distributor> distributors,
                                final ArrayList<Consumer> consumers) {
        for (Consumer consumer : consumers) {
            if (!consumer.isBankrupt) {
                if (consumer.isPostpone()) {
                    if (consumer.getNumMonths() == 0) {
                        if (consumer.getOldDist() == consumer.getCurrentDist()) {
                            Distributor curDist = distributors.get((int) consumer.getCurrentDist());
                            if (consumer.getBudget() >= consumer.getPriceToPay()
                                    + consumer.getRestanta()) {
                                consumer.setBudget(consumer.getBudget()
                                        - consumer.getPriceToPay() - consumer.getRestanta());
                                consumer.setPostpone(false);
                                consumer.setRestanta(0);
                                consumer.setOldDist(-1);
                                curDist.setBudget(curDist.getBudget()
                                        + consumer.getPriceToPay() + consumer.getRestanta());
                            } else {
                                consumer.setBankrupt(true);
                            }
                        } else {
                            Distributor oldDist = distributors.get((int) consumer.getOldDist());
                            if (consumer.getBudget() > consumer.getRestanta()) {
                                oldDist.setBudget(oldDist.getBudget() + consumer.getRestanta());
                                final double x = 1.2;
                                consumer.setRestanta(Math.round(Math.floor(
                                        (double) consumer.getPriceToPay() * x)));
                                consumer.setOldDist(consumer.getCurrentDist());
                            } else {
                                consumer.setBankrupt(true);
                            }
                        }
                    } else {
                        if (consumer.getBudget() >= consumer.getPriceToPay()
                                + consumer.getRestanta()) {
                            consumer.setBudget(consumer.getBudget() - consumer.getPriceToPay()
                                    - consumer.getRestanta());
                            Distributor curDist = distributors.get((int) consumer.getCurrentDist());
                            curDist.setBudget(curDist.getBudget() + consumer.getPriceToPay());
                            Distributor oldDist = distributors.get((int) consumer.getOldDist());
                            oldDist.setBudget(oldDist.getBudget() + consumer.getRestanta());
                            consumer.setPostpone(false);
                            consumer.setRestanta(0);
                            consumer.setOldDist(-1);
                        } else {
                            consumer.setBankrupt(true);
                        }
                    }
                } else {
                    if (consumer.getBudget() >= consumer.getPriceToPay()) {
                        consumer.setBudget(consumer.getBudget() - consumer.getPriceToPay());
                        Distributor curDist = distributors.get((int) consumer.getCurrentDist());
                        curDist.setBudget(curDist.getBudget() + consumer.getPriceToPay());
                    } else {
                        consumer.setPostpone(true);
                        final double x = 1.2;
                        consumer.setRestanta(Math.round(Math.floor(
                                (double) consumer.getPriceToPay() * x)));
                        consumer.setOldDist(consumer.getCurrentDist());
                    }
                }
            }
        }
    }

    /**
     * @param distributors - the distributors list
     *                     if the distributor is not bankrupt,
     *                     he pays hist monthly expenses
     */
    public void payExpenses(final ArrayList<Distributor> distributors) {
        for (Distributor distributor : distributors) {
            if (!distributor.isBankrupt()) {
                distributor.setBudget(distributor.getBudget() - distributor.monthlyExpenses());
            }
            if (distributor.getBudget() < 0) {
                distributor.setBankrupt(true);
            }
        }
    }

    /**
     * @param distributor a specific distributor
     * @param producers   the list of all producers
     *                    calculates the production cost of the wanted Distributor
     */
    public void distributorProductionCost(final Distributor distributor,
                                          final ArrayList<Producer> producers) {
        long productionCost = 0;
        for (Long x : distributor.getProducerIds()) {
            Producer wantedProducer = findProducer(producers, x);
            productionCost += wantedProducer.getEnergyPerDistributor()
                    * wantedProducer.getPriceKW();
        }
        final int ten = 10;
        productionCost = Math.round(Math.floor(productionCost / ten));
        distributor.setProductionCost(productionCost);
    }

    /**
     * @param distributors a list of all Distributors
     * @param producers    a list of all producers
     */
    public void setProductionCosts(final ArrayList<Distributor> distributors,
                                   final ArrayList<Producer> producers) {
        for (Distributor distributor : distributors) {
            distributorProductionCost(distributor, producers);
        }
    }

    /**
     * @param distributor         a certain distributor
     * @param producers           a list of all producers
     * @param energyChoiceFactory a factory for the Energy Choice
     *                            assigns the distributor its producers,
     *                            based on its the Energy Strategy
     */
    public void assignProducers(final Distributor distributor,
                                final ArrayList<Producer> producers,
                                final EnergyChoiceFactory energyChoiceFactory) {
        EnergyStrategy strategy = energyChoiceFactory
                .createStrategy(distributor.getProducerStrategy());
        strategy.sortProducer(producers);
        distributor.getProducerIds().clear();
        distributor.setResetProducers(false);
        distributor.setTotalEnergy(0);
        for (Producer producer : producers) {
            if (distributor.getTotalEnergy() < distributor.getEnergyNeededKW()
                    && producer.getActualDistributors() < producer.getMaxDistributors()) {
                distributor.setTotalEnergy(distributor.getTotalEnergy()
                        + producer.getEnergyPerDistributor());
                producer.addObserver(distributor);
                producer.setActualDistributors(producer.getActualDistributors() + 1);
                distributor.getProducerIds().add(producer.getId());
            }
        }
    }

    /**
     * @param producers    a list of all producers
     * @param distributors a list of all distributors
     *                     remakes the connections between observer and observable
     */
    public void resetObservers(final ArrayList<Producer> producers,
                               final ArrayList<Distributor> distributors) {
        for (Producer producer : producers) {
            producer.setActualDistributors(0);
            producer.deleteObservers();
            for (Distributor distributor : distributors) {
                if (distributor.getProducerIds().contains(producer.getId())
                        && !distributor.isResetProducers()) {
                    producer.setActualDistributors(producer.getActualDistributors() + 1);
                    producer.addObserver(distributor);
                }
            }
        }
    }

    /**
     * @param distributors        a list of all distributors
     * @param producers           a list of all producers
     * @param energyChoiceFactory a factory for the energy strategy
     *                            assigns to every distributor in the list its producers
     */
    public void assignAllProducers(final ArrayList<Distributor> distributors,
                                   final ArrayList<Producer> producers,
                                   final EnergyChoiceFactory energyChoiceFactory) {
        for (Distributor distributor : distributors) {
            if (distributor.isResetProducers() && !distributor.isBankrupt()) {
                assignProducers(distributor, producers, energyChoiceFactory);
            }
        }
    }

    /**
     * @param producers    a list of all producers
     * @param distributors a list of all distributors
     * @param monthNumber  the month we are in
     *                     memorizes the connections between distributor
     *                     and producer in the specified month
     */
    public void getMonthlyStats(final ArrayList<Producer> producers,
                                final ArrayList<Distributor> distributors, long monthNumber) {
        for (Producer producer : producers) {
            producer.getMonthlyDistributors().put(monthNumber, new ArrayList<>());
            for (Distributor distributor : distributors) {
                if (distributor.getProducerIds().contains(producer.getId())) {
                    producer.getMonthlyDistributors().get(monthNumber).add(distributor.getId());
                }
            }
        }
    }

    /**
     * @param producers    a list of all producers
     * @param distributors a list of all distributors
     *                     resets both the distributors' producers and
     *                     the producers' distributors, if needed
     */
    public void resetDistList(final ArrayList<Producer> producers,
                              final ArrayList<Distributor> distributors) {
        for (Producer producer : producers) {
            if (producer.isHasChanged()) {
                for (Distributor distributor : distributors) {
                    if (distributor.getProducerIds().contains(producer.getId())) {
                        distributor.getProducerIds().clear();
                    }
                }
                producer.setActualDistributors(0);
                producer.deleteObservers();
            }
        }
    }

    /**
     * @param producers a list of all producers
     * @param id        the producer's id
     * @return the wanted producer
     */
    public Producer findProducer(final ArrayList<Producer> producers, final long id) {
        Producer wantedProducer = null;
        for (Producer producer : producers) {
            if (producer.getId() == id) {
                wantedProducer = producer;
            }
        }
        return wantedProducer;
    }

    /**
     * @param distributors a list of all distributors
     *                     makes a list of the distributor's contract price every month
     */
    public void makePricesHistory(final ArrayList<Distributor> distributors) {
        for (Distributor distributor : distributors) {
            distributor.getPricesHistory().add(distributor.contractPrice());
        }
    }
}




