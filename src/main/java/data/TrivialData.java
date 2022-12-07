package data;

public class TrivialData extends Data {

    public TrivialData() {
        this.setWarehouseNumber(5);
        this.setStoreNumber(10);
        this.setConstructionCost(30);
        this.setWarehouseCapacity(warehouseCapacity());
        this.setSupplyCost(supplyCost());
    }

    private int[] warehouseCapacity() {
        int[] capacity = {1,4,2,1,3};
        return capacity;
    }

    private int[][]  supplyCost() {
        int[][] supplyCost =
                {
                        {20, 24, 11, 25, 30},
                        {28, 27, 82, 83, 74},
                        {74, 97, 71, 96, 70},
                        {2, 55, 73, 69, 61},
                        {46, 96, 59, 83, 4},
                        {42, 22, 29, 67, 59},
                        {1, 5, 73, 59, 56},
                        {10, 73, 13, 43, 96},
                        {93, 35, 63, 85, 46},
                        {47, 65, 55, 71, 95},
                };
        return supplyCost;
    }
}
