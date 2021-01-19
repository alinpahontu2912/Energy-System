package entities;

public abstract class Entity {
    public long id;
    public long budget;
    public boolean isBankrupt;

    /**
     * @return true if entity is banrupt, false otherwise
     */
    public boolean isBankrupt() {
        return isBankrupt;
    }

    /**
     * @param bankrupt change the bankruptcy status
     *                 of the entity
     */
    public void setBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    /**
     * @return the entity's id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id set the entity's id
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @return the entity's current budget
     */
    public long getBudget() {
        return budget;
    }

    /**
     * @param budget the new budget of the entity
     */
    public void setBudget(final long budget) {
        this.budget = budget;
    }

}
