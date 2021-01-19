package strategies;

public class EnergyChoiceFactory {

    private static EnergyChoiceFactory energyChoiceFactory = null;

    /**
     * @return an instance of the energy factory
     */
    public static EnergyChoiceFactory getInstance() {
        if (energyChoiceFactory == null) {
            energyChoiceFactory = new EnergyChoiceFactory();
        }
        return energyChoiceFactory;
    }

    /**
     * @param type the type of strategy to be made
     * @return the type of strategy wanted
     */
    public EnergyStrategy createStrategy(String type) {
        EnergyStrategy strategy;
        switch (type) {
            case "GREEN":
                strategy = new GreenStrategy();
                break;
            case "PRICE":
                strategy = new PriceStrategy();
                break;
            case "QUANTITY":
                strategy = new QuantityStrategy();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return strategy;
    }

}
