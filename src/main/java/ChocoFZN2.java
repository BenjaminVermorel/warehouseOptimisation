import org.chocosolver.parser.flatzinc.Flatzinc;

public class ChocoFZN2 {

    public ChocoFZN2() {
    }

    public static void main(String[] args) throws Exception {
        Flatzinc fzn = new Flatzinc();
        if (fzn.setUp(args)) {
            fzn.createSettings();
            fzn.createSolver();
            fzn.buildModel();
            fzn.configureSearch();
            fzn.solve();
        }
    }
}
