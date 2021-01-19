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
     * @param type type of entity to be created
     * @param args variable arguments, depending on the element to be created
     * @return a new Entity of type 'type'
     */
    public final Entity createEntity(final String type, Object... args) {
        Entity entity;
        switch (type) {
            case "consumers":
                entity = new Consumer((long) args[0], (long) args[1], (long) args[2]);
                break;
            case "distributors":
                entity = new Distributor((long) args[0], (long) args[1], (long) args[2],
                        (long) args[3], (long) args[4], (String) args[5]);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return entity;
    }

}
