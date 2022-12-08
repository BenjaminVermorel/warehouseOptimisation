package models;

import data.Data;
import org.chocosolver.parser.flatzinc.Flatzinc4Parser;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.SetVar;

public class Model1 {

    public void solve(Data data) {
        // 1. Create a Model
        Model model = new Model("my first problem");
        int warehouseNumber = data.getWarehouseNumber();
        int storeNumber = data.getStoreNumber();
        int constructionCost = data.getConstructionCost();
        int[][] supplyCost= data.getSupplyCost();

        IntVar magasins = model.intVar("magasins", 1,warehouseNumber, true);
        IntVar entrepots = model.intVar("magasins", 1,warehouseNumber, true);

        IntVar[] coutFixe = model.intVarArray("coutFixe", warehouseNumber, 0, constructionCost, true);
        IntVar[][] assign = model.intVarMatrix("assign", storeNumber, warehouseNumber, 0, 1);
        IntVar[][] coutMaintenance = model.intVarMatrix("coutMaintenance", storeNumber, warehouseNumber, 0, data.MaxTotalCost(), true);

        for(int x = 0; x < storeNumber; x++) {
            //One store can only get supplied by one warehouse
            model.sum(assign[x], "=", 1);
            //Calculation of the final supply cost
            for(int y = 0;y < warehouseNumber; y++) {
                model.times(assign[x][y], supplyCost[x][y], coutMaintenance[x][y]);
            }
        }

        for(int y = 0; y < warehouseNumber; y++) {
            model.sum(assign[y], "<=", 1);
        }
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