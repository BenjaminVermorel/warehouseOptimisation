import data.Data;
import data.TrivialData;
import models.Model1;

public class main {

    public static void main(String[] args) {
        Model1 model = new Model1();

        Data data = new TrivialData();

        model.solve(data);
    }
}
