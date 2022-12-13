int: n_entrepot;
int: n_magasin;
int: cout_construction;
array[1..n_entrepot] of int: capacite;
array[1..n_magasin,1..n_entrepot] of int: cout_matrice;

int: coutMax = max(i in 1..n_magasin, j in 1..n_entrepot)(cout_matrice[i,j]);
int: MaxTotal =   (n_entrepot * cout_construction)
                + sum(i in 1..n_magasin, j in 1..n_entrepot)(cout_matrice[i,j]);

set of int: magasins  = 1..n_magasin;
set of int: entrepots  = 1..n_entrepot;
array[1..n_entrepot] of var 0..cout_construction: coutFixe;
array[1..n_magasin,1..n_entrepot] of var 0..1: assign;
array[1..n_magasin,1..n_entrepot] of var 0..coutMax: cout_maintenance;
var 0..MaxTotal: total;



constraint forall(i in magasins)(
              %Un magasin ne peut être fourni que par un entrepot 
              sum(j in entrepots)(assign[i,j] == 1) == 1
           /\ forall(j in entrepots)(
                 cout_maintenance[i,j] = cout_matrice[i,j] * assign[i,j]
              )
           );

constraint forall(j in entrepots)(
              %Un entrepot ne peut pas fournir plus de magasin qu'il a de capacite
              sum(i in magasins)(assign[i,j] == 1) <= capacite[j]
           );
           
constraint forall(j in entrepots where sum(i in magasins)(assign[i,j] == 1) > 0)(
              coutFixe[j] =  cout_construction
           );

constraint forall(j in entrepots where sum(i in magasins)(assign[i,j] == 1) == 0)(
              coutFixe[j] =  0
           );

constraint total =
           sum(i in 1..n_magasin, j in 1..n_entrepot)(cout_maintenance[i,j]) 
         + sum(j in 1..n_entrepot)(coutFixe[j]);

%recherche par minimisation du cout total  
   
solve :: seq_search([
             int_search(cout_maintenance, smallest, indomain_min, complete),
             int_search(coutFixe, input_order, indomain_min, complete)])
minimize total;

output["cout: ", show(total), "\n"];
output["coutFixe: ", show(coutFixe), "\n"];


