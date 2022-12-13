package models;

import data.Data;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
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

        for(int x = 0; x < storeNumber; x++) {
            model.element(coutAppro[x], supplyCost[x], fournisseur[x]).post();
        }

        //used capacity is being determined by looking in fournisseur
        IntVar closedWarehouseCount = model.intVar("openWarehouseCount", 0, warehouseNumber, true);
        model.count(0, usedCapacity, closedWarehouseCount).post();
        IntVar openWarehouseCount = model.intVar("openWarehouseCount", 0, warehouseNumber, true);
        IntVar warehouseNumberVar = model.intVar("openWarehouseCount", warehouseNumber);
        model.arithm(warehouseNumberVar, "-", closedWarehouseCount, "=", openWarehouseCount).post();
        model.times(openWarehouseCount, constructionCost, coutFixe).post();

        //Only one supplier per store
        IntVar storeNumberVar = model.intVar("openWarehouseCount",storeNumber);
        model.sum(usedCapacity, "=", storeNumberVar).post();

        //sum to get the total cost
        IntVar finalSupplyCost = model.intVar("supplyCost", 0, data.getMaxTotalCost());
        model.sum(coutAppro, "=", finalSupplyCost).post();
        model.arithm(coutFixe, "+", finalSupplyCost, "=", coutTotal).post();

        model.setObjective(false, coutTotal);
        Solver solver = model.getSolver();
        while(solver.solve()){
            prettyPrint(model, usedCapacity, warehouseNumber, fournisseur, storeNumber, coutTotal);
        }

    }

    private void prettyPrint(Model model, IntVar[] open, int W, IntVar[] supplier, int S, IntVar coutTotal) {
        StringBuilder st = new StringBuilder();
        st.append("Solution #").append(model.getSolver().getSolutionCount()).append("\n");
        for (int i = 0; i < W; i++) {
            if (open[i].getValue() > 0) {
                st.append(String.format("\tWarehouse %d supplies stores: ", (i + 1)));
                for (int j = 0; j < S; j++) {
                    if (supplier[j].getValue() == (i + 1)) {
                        st.append(String.format("%d ", (j + 1)));
                    }
                }
                st.append("\n");
            }
        }
        st.append("\tTotal C: ").append(coutTotal.getValue());
        System.out.println(st.toString());
    }
}