% Use this editor as a MiniZinc scratch book

int: n_entrepot;
int: n_magasin;
int: cout_construction;
array[1..n_entrepot] of int: capacite;
array[1..n_magasin,1..n_entrepot] of int: cout_matrice;


int: MaxCost = max(i in 1..n_magasin, j in 1..n_entrepot)(cout_matrice[i,j]);
int: MaxTotal =   (n_entrepot * cout_construction)
                + sum(i in 1..n_magasin, j in 1..n_entrepot)(cout_matrice[i,j]);
 
array[1..n_magasin] of var 1..n_entrepot: supplier;
array[1..n_entrepot] of var bool: open;
array[1..n_magasin] of var 1..MaxCost: cost;
var 1..MaxTotal: tot;
 
constraint
  sum (i in 1..n_entrepot) (cout_construction * bool2int(open[i])) +
  sum (i in 1..n_magasin) (cost[i])
  = tot;
 
constraint
  forall (i in 1..n_magasin) (
    cout_matrice[i,supplier[i]] = cost[i]
  );
 
constraint
  forall (i in 1..n_entrepot) (
    let {
      var int: use
     } in
    count(supplier,i,use) /\ use <= capacite[i]
  );
 
constraint
  forall (i in 1..n_entrepot) (
    (exists (j in 1..n_magasin) (supplier[j] == i)) == open[i]
  );
 
solve
  :: int_search(
    supplier ++ cost ++ [bool2int(open[i]) | i in 1..n_entrepot],
    first_fail,
    indomain_split,
    complete
  )minimize tot;
 
output
  [ "warehouses:" ]
  ++
  [ "\ntot = ", show(tot) ]
  ++
  [ "\nsupplier = [\n" ]
  ++
  [ "\t" ++ show(supplier[i]) ++
    if i = n_entrepot then "\n]"
    elseif i mod 5 = 0 then ",\n"
    else ","
    endif
  | i in 1..n_magasin
  ]
  ++
  [ "\ncost = [\n" ]
  ++
  [ "\t" ++ show(cost[i]) ++
    if i = n_entrepot then "\n]"
    elseif i mod 5 = 0 then ",\n"
    else ","
    endif
  | i in 1..n_magasin
  ]
  ++
  [ "\nopen = [\n" ]
  ++
  [ "\t" ++ show(open[i]) ++
    if i = n_entrepot then "\n]\n"
    elseif i mod 5 = 0 then ",\n"
    else ","
    endif
  | i in 1..n_entrepot
  ]