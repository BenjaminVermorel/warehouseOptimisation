int: n_entrepot;
int: n_magasin;
int: cout_construction;

set of int: magasins  = 1..n_magasin;
set of int: entrepots  = 1..n_entrepot;
array[1..n_entrepot] of int: capacite;
array[1..n_magasin,1..n_entrepot] of int: cout_matrice;

int: capacite_max = max(i in 1..n_entrepot)(capacite[i]);
array[1..n_entrepot] of var 0..capacite_max: used_capacity;
array[1..n_magasin,1..n_entrepot] of var 0..1: association;
var int: cout_fixe;
array[1..n_magasin,1..n_entrepot] of var int: cout_appro;
var int: cout_total;

constraint forall(e in entrepots)(
    %Règles sur la capacité utilisée
    used_capacity[e]<=capacite[e]
    /\ used_capacity[e] >=0
    /\ used_capacity[e] == sum(m in magasins)(association[m,e])
    /\ forall(m in magasins)(
        cout_appro[m,e] == cout_matrice[m,e]*association[m,e]
        )
    );

constraint forall(m in magasins)(
    %Un magasin n'est fourni que par un entrepôt
    sum(e in entrepots)(association[m,e]) == 1
    );

constraint sum(e in entrepots)(used_capacity[e]) = n_magasin;
constraint (sum(e in entrepots)(if used_capacity[e]>0 then cout_construction else 0 endif) = cout_fixe);

constraint cout_total == cout_fixe + sum(m in magasins, e in entrepots)(cout_appro[m,e]);

solve ::int_search(association, input_order, indomain) minimize cout_total; 