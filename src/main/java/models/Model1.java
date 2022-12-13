package models;

import data.Data;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;
import java.util.ArrayList;
import java.util.List;

public class Model1 {

    public void solve(Data data) {
        // 1. Create a Model
        Model model = new Model("my first problem");
        int warehouseNumber = data.getWarehouseNumber();
        int storeNumber = data.getStoreNumber();
        int constructionCost = data.getConstructionCost();
        int[][] supplyCost = data.getSupplyCost();
        int[] warehouseCapacity = data.getWarehouseCapacity();

        IntVar magasins = model.intVar("magasins", 1,warehouseNumber, true);
        IntVar entrepots = model.intVar("magasins", 1,warehouseNumber, true);

        IntVar[] coutFixe = model.intVarArray("coutFixe", warehouseNumber, 0, constructionCost, true);
        IntVar[][] assign = model.intVarMatrix("assign", storeNumber, warehouseNumber, 0, 1);
        IntVar[][] coutMaintenance = model.intVarMatrix("coutMaintenance", storeNumber, warehouseNumber, 0, data.getMaxTotalCost(), true);

        IntVar ONE = model.intVar(1,1);
        for(int x = 0; x < storeNumber; x++) {
            //One store can only get supplied by one warehouse
            model.count(1, assign[x], ONE).post();
            //Calculation of the final supply cost
            for(int y = 0;y < warehouseNumber; y++) {
                model.times(assign[x][y], supplyCost[x][y], coutMaintenance[x][y]).post();
            }
        }

        IntVar[] column = new IntVar[warehouseNumber];
        List<IntVar> list = new ArrayList<>();
        for(int x = 0; x < storeNumber; x++)
        {
            for (int y = 0; y < warehouseNumber; y++) {
                list.add(assign[x][y]);
            }
            model.sum(column, "<=", warehouseCapacity[x]).post();
        }

        IntVar var =
        model.



        /*
        // 2. Create variables
        IntVar x = model.intVar("X", 0, 5);                 // x in [0,5]
        IntVar y = model.intVar("Y", new int[]{2, 3, 8});   // y in {2, 3, 8}
        // 3. Post constraints
        model.arithm(x, "+", y, "<", 5).post(); // x + y < 5
        model.times(x,y,4).post();              // x * y = 4
        // 4. Solve the problem
        model.getSolver().solve();
        // 5. Print the solution
        System.out.println(x); // Prints X = 2
        System.out.println(y); // Prints Y = 2
        */
    }
}