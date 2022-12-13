
import data.Data;
import data.TrivialData;
import models.Model1;
import org.chocosolver.parser.flatzinc.ChocoFZN;

public class main {

    public static void main(String[] args) throws Exception {
        Data data = new TrivialData();
        Model1 model = new Model1();
        model.solve(data);

        /*
        System.out.println("rien a faire ici ");
        ChocoFZN2 chocoFZN2 = new ChocoFZN2();
        chocoFZN2.main(args);
        */
    }
}
