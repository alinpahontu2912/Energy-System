package ioutils;

import entities.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utilities.Constants;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class InputReader {

    private final ArrayList<Distributor> distributorList = new ArrayList<>();
    private final ArrayList<Consumer> consumerList = new ArrayList<>();
    private final ArrayList<Producer> producerList = new ArrayList<>();
    private JSONObject jsonObject = null;
    private JSONObject database = null;
    private JSONArray updates = null;

    public InputReader() {
    }

    /**
     * @param fileReader the input file
     * @throws IOException    in case file not found
     * @throws ParseException in case file cannot be parse
     *                        exceptions are throwed in cse
     *                        the file is not found, badly formatted, etc
     */
    public void doParse(final FileReader fileReader) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        jsonObject = (JSONObject) jsonParser.parse(fileReader);
        database = (JSONObject) jsonObject.get(Constants.INITIALDATA);
        updates = (JSONArray) jsonObject.get(Constants.MONTHLYUPDATES);
    }

    /**
     * @return the number of months for the simulation
     */
    public long getTurns() {
        return (long) jsonObject.get(Constants.NUMBEROFTURNS);
    }

    /**
     * @param entityFactory the consumer creator
     * @return the initial list of consumers
     */
    public ArrayList<Consumer> createConsumerList(final EntityFactory entityFactory) {
        JSONArray array = (JSONArray) database.get(Constants.CONSUMERS);
        for (Object o : array) {
            JSONObject buffer = (JSONObject) o;
            long readId = (long) buffer.get(Constants.ID);
            long initialBudget = (long) buffer.get(Constants.INITIALBUDGET);
            long monthlyIncome = (long) buffer.get(Constants.MONTHLYINCOME);
            Entity newConsumer = entityFactory
                    .createEntity(Constants.CONSUMERS, readId,
                            initialBudget, monthlyIncome, 0, 0, 0, "");
            consumerList.add((Consumer) newConsumer);
        }
        return consumerList;
    }

    /**
     * @param entityFactory the distributor creator
     * @return a list of distributors
     * creates the list of distributors
     */
    public ArrayList<Distributor> createDistributorList(
            final EntityFactory entityFactory) {
        JSONArray array = (JSONArray) database.get(Constants.DISTRIBUTORS);
        for (Object o : array) {
            JSONObject buffer = (JSONObject) o;
            long readId = (long) buffer.get(Constants.ID);
            long contractLength = (long) buffer.get(Constants.CONTRACTLENGTH);
            long initialBudget = (long) buffer.get(Constants.INITIALBUDGET);
            long initialInfrastructureCost = (long) buffer.get(Constants.INITIALINFRASTRUCTURECOST);
            long energyNeeded = (long) buffer.get(Constants.ENERGYNEEDEDKW);
            String strategy = (String) buffer.get(Constants.PRODUCERSTRATEGY);
            Entity newDistributor = entityFactory
                    .createEntity(Constants.DISTRIBUTORS, readId, initialBudget,
                            0, contractLength, initialInfrastructureCost, energyNeeded, strategy);
            distributorList.add((Distributor) newDistributor);

        }
        return distributorList;
    }

    /**
     * @param monthNumber a specific month
     * @param distList    the distributor list
     *                    updates the production and infrastructure cost
     *                    of the distributors
     */
    public void getDistributorMonthlyUpdate(final int monthNumber,
                                            final ArrayList<Distributor> distList) {
        JSONObject thisMonth = (JSONObject) updates.get(monthNumber);
        JSONArray distChanges = (JSONArray) thisMonth.get("distributorChanges");
        if (distChanges.size() != 0) {
            for (Object object : distChanges) {
                long distId = (long) ((JSONObject) object).get(Constants.ID);
                long distInfrastructurecost = (long) ((JSONObject) object)
                        .get(Constants.INFRASTRUCTURECOST);
                distList.get((int) distId).setInfrastructureCost(distInfrastructurecost);
            }
        }
    }

    /**
     * @param monthNumber     a specific month
     * @param consList        the consumer list
     * @param consumerFactory the consumer creator
     */
    public void getConsumerMonthlyUpdate(final int monthNumber,
                                         final ArrayList<Consumer> consList,
                                         final EntityFactory consumerFactory) {
        JSONObject thisMonth = (JSONObject) updates.get(monthNumber);
        JSONArray distChanges = (JSONArray) thisMonth.get(Constants.NEWCONSUMERS);
        if (distChanges.size() != 0) {
            for (Object object : distChanges) {
                long consId = (long) ((JSONObject) object).get(Constants.ID);
                long consBudget = (long) ((JSONObject) object).get(Constants.INITIALBUDGET);
                long consIncome = (long) ((JSONObject) object).get(Constants.MONTHLYINCOME);
                Consumer wantedConsumer = (Consumer) consumerFactory
                        .createEntity(Constants.CONSUMERS, consId, consBudget, consIncome,
                                0, 0, 0, "");
                consList.add(wantedConsumer);
            }
        }
    }

    public ArrayList<Producer> createProducerList() {
        JSONArray array = (JSONArray) database.get(Constants.PRODUCERS);
        for (Object o : array) {
            JSONObject buffer = (JSONObject) o;
            long readId = (long) buffer.get(Constants.ID);
            String energyType = (String) buffer.get(Constants.ENERGYTYPE);
            EnergyType energyType1 = null;
            switch (energyType) {
                case "WIND":
                    energyType1 = EnergyType.WIND;
                    break;
                case "SOLAR":
                    energyType1 = EnergyType.SOLAR;
                    break;
                case "HYDRO":
                    energyType1 = EnergyType.HYDRO;
                    break;
                case "COAL":
                    energyType1 = EnergyType.COAL;
                    break;
                case "NUCLEAR":
                    energyType1 = EnergyType.NUCLEAR;
                    break;
            }
            long maxDistributors = (long) buffer.get(Constants.MAXDISTRIBUTORS);
            double priceKW = (double) buffer.get(Constants.PRICEKW);
            long energyPerDistributor = (long) buffer.get(Constants.ENERGYPERDISTRIBUTOR);
            Producer newProducer = new Producer(readId, energyType1, maxDistributors, priceKW, energyPerDistributor);
            producerList.add(newProducer);
        }
        return producerList;
    }


    public void getProducerMonthlyUpdate(final int monthNumber,
                                         final ArrayList<Producer> prodList) {
        JSONObject thisMonth = (JSONObject) updates.get(monthNumber);
        JSONArray prodChanges = (JSONArray) thisMonth.get(Constants.PRODUCERCHANGES);
        prodList.sort(Comparator.comparing(Producer::getId));
        if (prodChanges.size() != 0) {
            for (Object object : prodChanges) {
                long prodId = (long) ((JSONObject) object).get(Constants.ID);
                long energyPerDistributor = (long) ((JSONObject) object)
                        .get(Constants.ENERGYPERDISTRIBUTOR);
                prodList.get((int) prodId).setEnergyPerDistributor(energyPerDistributor);
                prodList.get((int) prodId).setHasChanged(true);
                prodList.get((int) prodId).warnDistributors(prodList.get((int) prodId).isHasChanged());
                prodList.get((int) prodId).setHasChanged(false);
            }
        }
    }

}
