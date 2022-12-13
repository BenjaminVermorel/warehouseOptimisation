int: n_entrepot;
int: n_magasin;
int: cout_construction;

set of int: magasins  = 1..n_magasin;
set of int: entrepots  = 1..n_entrepot;
array[1..n_entrepot] of int: capacite;
array[1..n_magasin,1..n_entrepot] of int: cout_matrice;

int: capacite_max = max(i in 1..n_entrepot)(capacite[i]);
array[1..n_entrepot] of var 0..capacite_max: used_capacity;
array[1..n_magasin] of var 1..n_entrepot: fournisseur;
var int: cout_fixe;
array[1..n_magasin] of var int: cout_appro;
var int: cout_total;

constraint forall(e in entrepots)(
    %Règles sur la capacité utilisée
    used_capacity[e]<=capacite[e]
    /\ used_capacity[e] == sum(m in magasins where fournisseur[m] = e)(1)
    );

constraint forall(m in magasins)(
    cout_appro[m] = cout_matrice[m,fournisseur[m]]
    );

constraint sum(e in entrepots)(used_capacity[e]) = n_magasin;
constraint cout_fixe = sum(e in entrepots where used_capacity[e] != 0)(1) * cout_construction;

constraint cout_total == cout_fixe + sum(m in magasins)(cout_appro[m]);

solve ::seq_search([
             int_search(cout_appro, max_regret, indomain_min)%,
             %int_search(cout_fixe, largest, indomain_min)])
             ])
             minimize cout_total;
