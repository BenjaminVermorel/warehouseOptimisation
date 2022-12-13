package models;

import data.Data;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Model4 {

    public void solve(Data data) {
        // 1. Create a Model
        Model model = new Model("my first problem");
        int warehouseNumber = data.getWarehouseNumber();
        int storeNumber = data.getStoreNumber();
        int constructionCost = data.getConstructionCost();
        int[][] supplyCost = data.getSupplyCost();
        int[] warehouseCapacity = data.getWarehouseCapacity();
        int maxCapacity = data.getMaxCapacity();

        IntVar[] usedCapacity = model.intVarArray("usedCapacity", warehouseNumber,  0, maxCapacity, true);
        IntVar[] fournisseur = model.intVarArray("fournisseur", storeNumber,  1, warehouseNumber, true);
        IntVar coutFixe = model.intVar("coutFixe",   0, constructionCost * warehouseNumber, true);
        IntVar[] coutAppro = model.intVarArray("coutAppro", storeNumber,  0, data.getMaxTotalCost(), true);
        IntVar coutTotal = model.intVar("coutTotal", 0, data.getMaxTotalCost(), true);

        for(int x = 0; x < warehouseNumber; x++) {
            //used capacity can't exceed warehouseCapacity
            model.arithm(usedCapacity[x], "<=", warehouseCapacity[x]).post();

            //used capacity is being determined by looking in fournisseur
            IntVar fournisseurCount = model.intVar("fCount" + x, 0, warehouseCapacity[x], true);
            model.count(x, fournisseur, usedCapacity[x]).post();
        }

        //used capacity is being determined by looking in fournisseur
        IntVar closedWarehouseCount = model.intVar("openWarehouseCount", 0, warehouseNumber, true);
        model.count(0, usedCapacity, closedWarehouseCount).post();
        IntVar openWarehouseCount = model.intVar("openWarehouseCount", 0, warehouseNumber, true);
        model.arithm(warehouseNumber, "-", closedWarehouseCount, "=", openWarehouseCount);
        model.

        model.arithm(coutFixe, "=", )
        /*
        IntVar magasins = model.intVar("magasins", 1,warehouseNumber, true);
        IntVar entrepots = model.intVar("magasins", 1,warehouseNumber, true);


        IntVar[][] assign = model.intVarMatrix("assign", storeNumber, warehouseNumber, 0, 1);
        IntVar[][] coutMaintenance = model.intVarMatrix("coutMaintenance", storeNumber, warehouseNumber, 0, data.getMaxTotalCost(), true);
        */

    }
}