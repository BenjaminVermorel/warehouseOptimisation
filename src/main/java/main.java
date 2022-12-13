
import data.Data;
import data.TrivialData;
import models.Model1;
import models.Model4;

public class main {

    public static void main(String[] args) throws Exception {
        Data data = new TrivialData();
        Model4 model = new Model4();
        model.solve(data);

        /*
        System.out.println("rien a faire ici ");
        ChocoFZN2 chocoFZN2 = new ChocoFZN2();
        chocoFZN2.main(args);
        */
    }
}
