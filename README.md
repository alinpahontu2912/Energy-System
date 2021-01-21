# Energy-System - 2-part project for OOP course

## Packages:

### entities:

- contains Producer, Consumer, Distributor and abstract class Entity, 
which has the common fields of both consumers and distributors 
(id, budget, isBankrupt)
- both Consumer and Distributor classes extend Entity
- the package contains a singleton entities factory that creates
consumers or distributors
- Consumer implements Comparable interface, to allow for easy sorting
after specified criteria(first after remaining contract months, and then by id)
- Distributor implements Observer interface, to receive updates concerning
the need to change its producers 
- Producer extends Observable and notifies its Distributors every time
a change in production prices happen (this is done using the warnDistributors
method)
- Distributor has the following specific functions:
    - profit () -> used to calculate the wanted profit 
    - contractPrice() -> used to compute the price of the contract
    - monthlyExpenses() -> calculates the expenses of the distributor
- Distributors also have a list of their contract prices history for every
month of the game simulation
- Producers hold their monthly stats about distributors in a map

### ioutils:

- contains InputReader and Output classes
- those classes contain specific methods for reading and writing data
to files
!Used the @supresswarnings("unchecked") because the org.json.simple
library uses raw HashMap types!

### utilities:

- contains Constants class, which has strings to be used while reading 
and writing data to files
- contains the Utilities class, a class whose methods are used to solve
the simulations
- this class contains methods for:
    - assigning each distributor producers
    - calculating distributor contract prices
    - checking if there are valid distributors
    - finding the cheapest non-bankrupt distributor
    - assign non-Bankrupt consumers with no contract to cheapest distributor
    - counting the remaining contracts of the distributors
    - paying the consumers (giving them their monthly income)
    - making distributors pay their monthly expenses
    - paying the distributors(consumers paying their contracts)
    - removing bankrupt consumers from the distributor's list
    - resetting the relations between distributors and producers
    - creating the monthly distributor stats of a producer
    - creating a list of distributor contract prices history
    - finding a producer by its id

### strategies:

- contains the interface EnergyStrategy, which has a method for sorting
a list of producers
- GreenStrategy class implements EnergyStrategy and sorts producers based on
whether they use renewable energy sources, then by price and then by quantity of
energy they can give
- PriceStrategy class implements EnergyStrategy and sorts producers based on price
and then by quantity of energy they can give 
- QuantityStrategy class implements EnergyStrategy and sorts producers based on 
quantity of energy they can give
- If the type of energy and the price are equal, producers will be sorted by
their id number

### The Main class and the logic behind the entire program:

- I read the initial data from the files, using my inputReader
- If the number of the simulation is greater than 0, I update consumers, 
distributors and producer data
- I calculate the contract prices and choose the cheapest distributor 
- I assign consumers with no contract to the cheapest distributor and then reset
the number of consumers of every distributor
- I pay my consumers, then they pay their distributors and then the distributors
pay their monthly expenses
- I reset the relations between observers and observables (producer - distributor)
- I assign distributors to their producers, if needed
- I calculate the production cost again
- Before a new round, I remove the Bankrupt consumers from the distributors' list:
- I create a map that shows me all the producers' distributors in a specific month
- I create a map that contains the contract prices of every distributor for every month


