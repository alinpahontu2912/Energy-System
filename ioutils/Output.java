package ioutils;

import entities.Consumer;
import entities.Distributor;
import entities.Producer;
import org.json.simple.JSONObject;
import utilities.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


public class Output {

    public Output() {
    }

    /**
     * @param distributors the distributors list
     * @param consumers    the consumers list
     * @return a JSONObject to be written in output file
     */
    @SuppressWarnings("unchecked")
    public JSONObject createOutobj(final ArrayList<Distributor> distributors,
                                   final ArrayList<Consumer> consumers,
                                   final ArrayList<Producer> producers) {
        ArrayList<JSONObject> consArray = new ArrayList<>();
        ArrayList<JSONObject> distArray = new ArrayList<>();
        ArrayList<JSONObject> prodArray = new ArrayList<>();
        for (Consumer consumer : consumers) {
            JSONObject object1 = new JSONObject();
            object1.put(Constants.ID, consumer.getId());
            object1.put(Constants.ISBANKRUPT, consumer.isBankrupt());
            object1.put(Constants.BUDGET, consumer.getBudget());
            consArray.add(object1);
        }
        for (Distributor distributor : distributors) {
            JSONObject object2 = new JSONObject();
            object2.put(Constants.ID, distributor.getId());
            object2.put(Constants.ENERGYNEEDEDKW, distributor.getEnergyNeededKW());
            object2.put(Constants.BUDGET, distributor.getBudget());
            object2.put(Constants.PRODUCERSTRATEGY, distributor.getProducerStrategy());
            object2.put(Constants.ISBANKRUPT, distributor.isBankrupt());
            object2.put(Constants.CONTRACTCOST,
                    distributor.getPricesHistory().get(distributor.getPricesHistory().size() - 1));
            ArrayList<JSONObject> array = new ArrayList<>();
            Collections.sort(consumers);
            for (Consumer consumer : consumers) {
                if (consumer.getCurrentDist() == distributor.getId()) {
                    if (!consumer.isBankrupt()) {
                        JSONObject object3 = new JSONObject();
                        object3.put(Constants.CONSUMERID, consumer.getId());
                        object3.put(Constants.PRICE, consumer.getPriceToPay());
                        object3.put(Constants.REMAINEDMONTHS, consumer.getNumMonths());
                        array.add(object3);
                    }
                }
            }
            object2.put(Constants.CONTRACTS, array);
            distArray.add(object2);
        }
        producers.sort(Comparator.comparing(Producer::getId));
        for (Producer producer : producers) {
            JSONObject object3 = new JSONObject();
            object3.put(Constants.ID, producer.getId());
            object3.put(Constants.MAXDISTRIBUTORS, producer.getMaxDistributors());
            object3.put(Constants.PRICEKW, producer.getPriceKW());
            object3.put(Constants.ENERGYTYPE, producer.getType());
            object3.put(Constants.ENERGYPERDISTRIBUTOR, producer.getEnergyPerDistributor());
            ArrayList<JSONObject> array2 = new ArrayList<>();
            for (Map.Entry<Long, ArrayList<Long>>
                    entry : producer.getMonthlyDistributors().entrySet()) {
                JSONObject monthDistributor = new JSONObject();
                Long monthNumber = entry.getKey();
                ArrayList<Long> distIDs = entry.getValue();
                monthDistributor.put(Constants.MONTH, monthNumber);
                monthDistributor.put(Constants.DISTRIBUTORIDS, distIDs);
                array2.add(monthDistributor);
            }
            object3.put(Constants.MONTHLYSTATS, array2);
            prodArray.add(object3);
        }
        JSONObject wantedoutput = new JSONObject();
        wantedoutput.put(Constants.CONSUMERS, consArray);
        wantedoutput.put(Constants.DISTRIBUTORS, distArray);
        wantedoutput.put(Constants.ENERGYPRODUCERS, prodArray);
        return wantedoutput;
    }

    /**
     * @param fileWriter   the file to be written in
     * @param wantedOutput the object to be written
     * @throws IOException in case file does not exist, cannot be opened, etc
     */
    public void writeOut(final FileWriter fileWriter,
                         final JSONObject wantedOutput) throws IOException {
        fileWriter.write(wantedOutput.toJSONString());
        fileWriter.flush();
        fileWriter.close();
    }
}
