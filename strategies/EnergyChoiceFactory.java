package strategies;

public class EnergyChoiceFactory {

    private static EnergyChoiceFactory energyChoiceFactory = null;

    /**
     *
     * @return
     */
    public static EnergyChoiceFactory getInstance() {
        if (energyChoiceFactory == null) {
            energyChoiceFactory = new EnergyChoiceFactory();
        }
        return energyChoiceFactory;
    }

    /**
     *
     * @param type
     * @return
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
