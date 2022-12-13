
import data.Bench1;
import data.Data;
import models.Model4;

public class main {

    public static void main(String[] args) throws Exception {
        Data data = new Bench1();
        Model4 model = new Model4();
        model.solve(data);

        /*
        System.out.println("rien a faire ici ");
        ChocoFZN2 chocoFZN2 = new ChocoFZN2();
        chocoFZN2.main(args);
        */
    }
}
