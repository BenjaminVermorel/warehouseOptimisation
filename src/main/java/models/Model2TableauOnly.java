package models;

import data.Data;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import static org.chocosolver.solver.search.strategy.Search.*;

public class Model2TableauOnly {

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
        IntVar[] coutFixe = model.intVarArray("coutFixe", 1,   0, constructionCost * warehouseNumber, true);
        IntVar[] coutAppro = model.intVarArray("coutAppro", storeNumber,  0, data.getMaxTotalCost(), true);
        IntVar[] coutTotal = model.intVarArray("coutTotal", 1, 0, data.getMaxTotalCost(), true);

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
        IntVar[] closedWarehouseCount = model.intVarArray("openWarehouseCount", 1, 0, warehouseNumber, true);
        model.count(0, usedCapacity, closedWarehouseCount[0]).post();
        IntVar[] openWarehouseCount = model.intVarArray("openWarehouseCount", 1, 0, warehouseNumber, true);
        IntVar[] warehouseNumberVar = model.intVarArray("openWarehouseCount", 1, warehouseNumber,warehouseNumber);
        model.arithm(warehouseNumberVar[0], "-", closedWarehouseCount[0], "=", openWarehouseCount[0]).post();

        //calculating
        model.times(openWarehouseCount[0], constructionCost, coutFixe[0]).post();

        //Only one supplier per store
        IntVar[] storeNumberVar = model.intVarArray("openWarehouseCount", 1,storeNumber, storeNumber);
        model.sum(usedCapacity, "=", storeNumberVar[0]).post();

        //sum to get the total cost
        IntVar[] finalSupplyCost = model.intVarArray("supplyCost", 1, 0, data.getMaxTotalCost());
        model.sum(coutAppro, "=", finalSupplyCost[0]).post();
        model.arithm(coutFixe[0], "+", finalSupplyCost[0], "=", coutTotal[0]).post();

        model.setObjective(false, coutTotal[0]);

        Solver solver = model.getSolver();

        //solver.setSearch(minDomLBSearch(coutAppro));      ok 2 solutions instant 877408 au bout de 300s

        //solver.setSearch(minDomLBSearch(usedCapacity));   <= ca ca marche pas je sais pas pk

        //solver.setSearch(minDomLBSearch(fournisseur));    ok 5783 solution 880842 au bout de 300s

        //solver.setSearch(minDomLBSearch(coutTotal));        //ok 1 solution optimal en 10s 856165

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

        solver.setSearch(minDomLBSearch(ArrayUtils.append(coutFixe,
                                                            closedWarehouseCount,
                                                            coutAppro,
                                                            usedCapacity,
                                                            fournisseur,
                                                            openWarehouseCount,
                                                            warehouseNumberVar,
                                                            storeNumberVar,
                                                            finalSupplyCost,
                                                            coutTotal)));
        /*solver.setSearch(activityBasedSearch(ArrayUtils.append(coutFixe,
                closedWarehouseCount,
                coutAppro,
                usedCapacity,
                fournisseur,
                openWarehouseCount,
                warehouseNumberVar,
                storeNumberVar,
                finalSupplyCost,
                coutTotal)));*/


        // comme minDomLBSearch mais en plus développé
        /*solver.setSearch(intVarSearch(
                // selects the variable of smallest domain size
                new FirstFail(model),
                // selects the smallest domain value (lower bound)
                new IntDomainMin(),
                // variables to branch on
                ArrayUtils.append(coutTotal,
                        coutFixe,
                        coutAppro,
                        closedWarehouseCount,
                        openWarehouseCount,
                        finalSupplyCost,
                        warehouseNumberVar,
                        storeNumberVar,
                        usedCapacity,
                        fournisseur
                        )
        ));*/    // ca a l'air de fonctionner'

        //sélection aléatoire des variables
        /*solver.setSearch(intVarSearch(
                // selects the variable of smallest domain size
                new Random(System.currentTimeMillis()),
                // selects the smallest domain value (lower bound)
                new IntDomainMin(),
                // variables to branch on
                ArrayUtils.append(coutTotal,
                        coutFixe,
                        coutAppro,
                        closedWarehouseCount,
                        openWarehouseCount,
                        finalSupplyCost,
                        warehouseNumberVar,
                        storeNumberVar,
                        usedCapacity,
                        fournisseur
                )
        ));*/

        /*solver.setSearch(intVarSearch(
                // selects the variable of smallest domain size
                new Cyclic<>(),
                // selects the smallest domain value (lower bound)
                new IntDomainMin(),
                // variables to branch on
                ArrayUtils.append(coutTotal,
                        coutFixe,
                        coutAppro,
                        closedWarehouseCount,
                        openWarehouseCount,
                        finalSupplyCost,
                        warehouseNumberVar,
                        storeNumberVar,
                        usedCapacity,
                        fournisseur
                )
        ));*/

        /*solver.setSearch(intVarSearch(
                // selects the variable of smallest domain size
                new Smallest(),
                // selects the smallest domain value (lower bound)
                new IntDomainMin(),
                // variables to branch on
                ArrayUtils.append(coutTotal,
                        coutFixe,
                        coutAppro,
                        closedWarehouseCount,
                        openWarehouseCount,
                        finalSupplyCost,
                        warehouseNumberVar,
                        storeNumberVar,
                        usedCapacity,
                        fournisseur
                )
        ));*/

        /*solver.setSearch(intVarSearch(
                // selects the variable of smallest domain size
                new FirstFail(model),
                // selects the smallest domain value (lower bound)
                new IntDomainMax(),
                // variables to branch on
                ArrayUtils.append(coutTotal,
                        coutFixe,
                        coutAppro,
                        closedWarehouseCount,
                        openWarehouseCount,
                        finalSupplyCost,
                        warehouseNumberVar,
                        storeNumberVar,
                        usedCapacity,
                        fournisseur
                )
        ));*/
        /*solver.setSearch(minDomLBSearch(ArrayUtils.append(coutFixe,
                                                            closedWarehouseCount,
                                                            coutAppro,
                                                            usedCapacity,
                                                            fournisseur,
                                                            openWarehouseCount,
                                                            warehouseNumberVar,
                                                            storeNumberVar,
                                                            finalSupplyCost,
                                                            coutTotal)));*/

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
            prettyPrint(model, usedCapacity, warehouseNumber, fournisseur, storeNumber, coutFixe[0], finalSupplyCost[0], coutTotal[0]);
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