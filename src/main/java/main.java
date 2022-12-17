
import data.Bench1;
import data.Data;
import models.Model1;
import models.Model2;
import models.Model2TableauOnly;

public class main {

    public static void main(String[] args) throws Exception {

        //Instantiation des données d'entrées(bench)
        //Va de bench0 à bench 6
        Data data = new Bench1();

        //Instantiation du model
        //Model1 model = new Model1();
        //Model2 model = new Model2();
        Model2TableauOnly model = new Model2TableauOnly();

        //instruction de resoudre le model
        model.solve(data);


        //Code permettant d'utiliser Choco-parser.
        //Paramètres d'entrée: $PROJECT_DIR$/warehouseOptimisation\src\main\java\FlatZinc\model1Bench1.fzn -lvl INFO
        /*
        ChocoFZN2 chocoFZN2 = new ChocoFZN2();
        chocoFZN2.main(args);
        */
    }
}
