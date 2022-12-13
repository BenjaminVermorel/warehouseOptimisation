package data;

public abstract class Data {
    private int warehouseNumber;
    private int storeNumber;
    private int constructionCost;
    int[] warehouseCapacity;
    int[][] supplyCost;


    public void setWarehouseNumber(int warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    public void setStoreNumber(int storeNumber) {
        this.storeNumber = storeNumber;
    }

    public void setConstructionCost(int constructionCost) {
        this.constructionCost = constructionCost;
    }

    public void setWarehouseCapacity(int[] warehouseCapacity) {
        this.warehouseCapacity = warehouseCapacity;
    }

    public void setSupplyCost(int[][] supplyCost) {
        this.supplyCost = supplyCost;
    }

    public int getWarehouseNumber() {
        return this.warehouseNumber;
    }

    public int getStoreNumber() {
        return this.storeNumber;
    }

    public int getConstructionCost() {
        return this.constructionCost;
    }

    public int[] getWarehouseCapacity() {
        return this.warehouseCapacity;
    }

    public int[][] getSupplyCost() {
        return this.supplyCost;
    }

    public int getMaxSupplyCost() {
        int max = -1000000000;
        for(int x = 0; x<storeNumber; x++) {
            for(int y = 0; y<warehouseNumber; y++) {
                if(supplyCost[x][y] > max)
                    max = supplyCost[x][y];
            }
        }
        return max;
    }

    public int getMaxCapacity() {
        int max = 0;
        for(int x = 0; x<warehouseCapacity.length; x++) {
                if(warehouseCapacity[x] > max)
                    max = warehouseCapacity[x];
        }
        return max;
    }

    public int getMaxTotalCost() {
        int totalCost = warehouseNumber * constructionCost;
        for(int x = 0; x<storeNumber; x++) {
            for(int y = 0; y<warehouseNumber; y++) {
                totalCost = totalCost + supplyCost[x][y];
            }
        }
        return totalCost;
    }
 }
