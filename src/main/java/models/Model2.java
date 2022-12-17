package models;

import data.Data;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import static org.chocosolver.solver.search.strategy.Search.*;

public class Model2 {

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
        IntVar[] fournisseur = model.intVarArray("fournisseur", storeNumber,  0, warehouseNumber - 1, true);
        IntVar coutFixe = model.intVar("coutFixe",   0, constructionCost * warehouseNumber, true);
        IntVar[] coutAppro = model.intVarArray("coutAppro", storeNumber,  0, data.getMaxTotalCost(), true);
        IntVar coutTotal = model.intVar("coutTotal", 0, data.getMaxTotalCost(), true);

        for(int x = 0; x < warehouseNumber; x++) {
            //used capacity can't exceed warehouseCapacity
            model.arithm(usedCapacity[x], "<=", warehouseCapacity[x]).post();

            //used capacity is being determined by looking in fournisseur
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

        //calculating
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

        //solver.setSearch(minDomLBSearch(coutAppro));      ok 2 solutions instant 877408 au bout de 300s

        //solver.setSearch(minDomLBSearch(usedCapacity));   <= ca ca marche pas je sais pas pk

        //solver.setSearch(minDomLBSearch(fournisseur));    ok 5783 solution 880842 au bout de 300s

        solver.setSearch(minDomLBSearch(coutTotal));        //ok 1 solution optimal en 10s 856165

        //solver.setSearch(minDomLBSearch(coutFixe,warehouseNumberVar,coutTotal,closedWarehouseCount,openWarehouseCount,warehouseNumberVar,storeNumberVar,finalSupplyCost));

        //solver.setSearch(minDomLBSearch(ArrayUtils.append(usedCapacity, fournisseur, coutAppro))); // 1 269 512


        /*solver.setSearch(Search.intVarSearch(
                new VariableSelectorWithTies<>(
                        new FirstFail(model),
                        new Smallest()),
                new IntDomainMiddle(false),
                ArrayUtils.append(fournisseur, coutAppro, usedCapacity))
        );*/

        /*solver.setSearch(
                Search.intVarSearch(
                        new MaxRegret(),                    //877408
                        new IntDomainMin(),
                        coutAppro)
        );*/

        /* liste des variables du modele
        IntVar[] usedCapacity = model.intVarArray("usedCapacity", warehouseNumber,  0, maxCapacity, true);
        IntVar[] fournisseur = model.intVarArray("fournisseur", storeNumber,  0, warehouseNumber - 1, true);
        IntVar[] coutAppro = model.intVarArray("coutAppro", storeNumber,  0, data.getMaxTotalCost(), true);

        IntVar[] coutFixe = model.intVarArray("coutFixe",   0, constructionCost * warehouseNumber, true);
        IntVar[] closedWarehouseCount = model.intVarArray("openWarehouseCount", 0, warehouseNumber, true);
        IntVar[] openWarehouseCount = model.intVarArray("openWarehouseCount", 0, warehouseNumber, true);
        IntVar[] warehouseNumberVar = model.intVarArray("openWarehouseCount", warehouseNumber);
        IntVar[] storeNumberVar = model.intVarArray("openWarehouseCount",storeNumber);
        IntVar[] finalSupplyCost = model.intVarArray("supplyCost", 0, data.getMaxTotalCost());
        IntVar[] coutTotal = model.intVarArray("coutTotal", 1, 0, data.getMaxTotalCost(), true);
        */

        solver.limitTime("300s");

        solver.showShortStatistics();

        while(solver.solve()){
            prettyPrint(model, usedCapacity, warehouseNumber, fournisseur, storeNumber, coutFixe, finalSupplyCost, coutTotal);
        }

    }

    private void prettyPrint(Model model, IntVar[] open, int W, IntVar[] supplier, int S, IntVar coutFixe, IntVar supplyCost, IntVar coutTotal) {
        StringBuilder st = new StringBuilder();
        st.append("Solution #").append(model.getSolver().getSolutionCount()).append("\n");
        for (int i = 0; i < W; i++) {
            if (open[i].getValue() > 0) {
                st.append(String.format("\tWarehouse %d supplies stores: ", i + 1 ));
                for (int j = 0; j < S; j++) {
                    if (supplier[j].getValue() == i ) {
                        st.append(String.format("%d ", j + 1));
                    }
                }
                st.append("\n");
            }
        }
        st.append("coutFixe: ").append(coutFixe.getValue());
        //st.append("\tcoutAppro: ").append(coutAppro.getValue());
        st.append("\nsupplyCost: ").append(supplyCost.getValue());
        st.append("\nTotal: ").append(coutTotal.getValue());
        st.append("\n");
        System.out.println(st);
    }
}