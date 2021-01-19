package entities;


public class EntityFactory {
    private static EntityFactory entityFactory = null;

    /**
     * @return instance of the factory
     */
    public static EntityFactory getInstance() {
        if (entityFactory == null) {
            entityFactory = new EntityFactory();
        }
        return entityFactory;
    }

    /**
     * @param type               type of entity to be created
     * @param id                 id of the entity to be created
     * @param budget             budget of the entity to be created
     * @param income             income of the entity to be created
     * @param contractLength     only usable if entity is distributor
     * @param infrastructurecost only usable if entity is distributor
     * @param energyNeeded       only usable if entity is distributor
     * @param strategy           only usable if entity is distributor
     * @return a new entity
     */
    public Entity createEntity(final String type, final long id, final long budget,
                               final long income, final long contractLength,
                               final long infrastructurecost, final long energyNeeded, final String strategy) {
        Entity entity;
        switch (type) {
            case "consumers":
                entity = new Consumer(budget, income, id);
                break;
            case "distributors":
                entity = new Distributor(id, contractLength, budget,
                        infrastructurecost, energyNeeded, strategy);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return entity;
    }

}
