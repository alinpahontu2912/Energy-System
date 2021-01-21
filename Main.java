import entities.Consumer;
import entities.Distributor;
import entities.EntityFactory;
import entities.Producer;
import ioutils.InputReader;
import ioutils.Output;
import org.json.simple.JSONObject;
import strategies.EnergyChoiceFactory;
import utilities.Utilities;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Entry point to the simulation
 */
public final class Main {

    private Main() {
    }

    /**
     * Main function which reads the input file and starts simulation
     *
     * @param args input and output files
     * @throws Exception might error when reading/writing/opening files, parsing JSON
     */
    public static void main(final String[] args) throws Exception {
        InputReader inputReader = new InputReader();
        inputReader.doParse(new FileReader(args[0]));
        EntityFactory entityFactory = EntityFactory.getInstance();
        EnergyChoiceFactory energyChoiceFactory = EnergyChoiceFactory.getInstance();
        ArrayList<Consumer> consumerList =
                inputReader.createConsumerList(entityFactory);
        ArrayList<Distributor> distributorList =
                inputReader.createDistributorList(entityFactory);
        ArrayList<Producer> producerList =
                inputReader.createProducerList();
        // initial round
        Utilities utilities = new Utilities();
        long turns = inputReader.getTurns();
        utilities.assignAllProducers(distributorList, producerList, energyChoiceFactory);
        utilities.setProductionCosts(distributorList, producerList);
        Distributor wantedDistributor = utilities.getCheapest(distributorList);
        utilities.assignDistributor(wantedDistributor, consumerList);
        utilities.makePricesHistory(distributorList);
        utilities.countcontracts(consumerList, distributorList);
        utilities.payconsMonthly(consumerList);
        utilities.payDistributors(distributorList, consumerList);
        utilities.payExpenses(distributorList);
        // the next rounds
        for (long i = 1; i <= turns; i++) {
            if (utilities.verifyDistributors(distributorList)) {
                i = turns + 2;
            } else {
                inputReader.getConsumerMonthlyUpdate(
                        (int) (i - 1), consumerList, entityFactory);
                inputReader.getDistributorMonthlyUpdate((int) (i - 1), distributorList);
                inputReader.getProducerMonthlyUpdate((int) (i - 1), producerList);
                wantedDistributor = utilities.getCheapest(distributorList);
                utilities.makePricesHistory(distributorList);
                utilities.assignDistributor(wantedDistributor, consumerList);
                utilities.countcontracts(consumerList, distributorList);
                utilities.payconsMonthly(consumerList);
                utilities.payDistributors(distributorList, consumerList);
                utilities.payExpenses(distributorList);
                utilities.resetDistList(producerList, distributorList);
                utilities.resetObservers(producerList, distributorList);
                utilities.assignAllProducers(distributorList, producerList, energyChoiceFactory);
                utilities.setProductionCosts(distributorList, producerList);
                utilities.removeBankrupt(distributorList, consumerList);
                utilities.getMonthlyStats(producerList, distributorList, i);
            }
        }
        // writing the output
        Output output = new Output();
        JSONObject test = output.createOutobj(distributorList, consumerList, producerList);
        FileWriter o = new FileWriter(args[1]);
        output.writeOut(o, test);

    }
}
