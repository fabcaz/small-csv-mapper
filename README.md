## Goal:
To convert a csv format from one software product to a different csv format recognized by another product. I was initially going to use an awk script but the complexity of the conversion ultimately made java a more attractive option.

## Description:
Some types of entries mapped directly to the desired format, but others first needed to be merged into an intermediary format.

i.e. (a,b,c,d) -> (1,2) where:
- map({a,b}) -> {1} 
- merge({c,d}) -> {"two"} then map({"two"}) -> {2}

The steps were 1) substitute label names, 2) substitute headers, 3) rearrange columns, 4) merge types that require merging.

For convenience, (1) was done directly with a text editor.

Since (2) and (3) could be achieved simply by constructing a different object, instead of being consistent and constructing an intermediary object for all types in the source csv, some types directly converted to the target type; only the types that needed to be merged in (4) were merged into an intermediary object.

