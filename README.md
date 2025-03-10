###### Xavier Mazière

# INFO805 TP3-4

## Présentation

### NodeType

NodeType est un enum des différents types de nodes (aka d'opérations) supportés par Node. Il permet de créer et d'interpréter les nodes sans ambiguïté.

### Node

La classe Node représente un arbre n-aire utilisé pour construire l'arbre abstrait du programme pour ensuit le convertire en lambdada.
J'ai choisit de construire la methode `toInstruction` avec un switch case plutôt qu'en faisant des sous classes pour chaque type d'instruction car cela autait demandé de créer beaucoup de calsses différentes pour override une seul méthode.

J'ai choisit de stoquer le résultat de toutes les instructions dans le registre eax.

### CUP

J'ai ajouté un symbole non terminal afin de pouvoir gérer des IF THEN en plus des IF THEN ELSE. Il fallait le gérer avec un symbole supplémantaire afin de le désambïgué.

## Examples

#### Exercice 1

> Pseudo code :
```
let prixHt = 200;
let prixTtc =  prixHt * 119 / 100 .
```

> Instruction générés :
```
DATA SEGMENT
	prixTtc DD 
	prixHt DD 
DATA ENDS
CODE SEGMENT
	mov eax, 200
	mov prixHt, eax
	mov eax, 100
	push eax
	mov eax, 119
	push eax
	mov eax, prixHt
	pop ebx
	mul eax, ebx
	pop ebx
	div eax, ebx
	mov prixTtc, eax
CODE ENDS
```

#### Exercice 2

> Pseudo code :
```
let a = input;
let b = input;
while (0 < b)
do (let aux=(a mod b); let a=b; let b=aux );
output a
.
```

> Instruction générés :
```
DATA SEGMENT
	a DD 
	b DD 
	aux DD 
DATA ENDS
CODE SEGMENT
	in eax
	mov a, eax
	in eax
	mov b, eax
debut_while_1:
	mov eax, 0
	push eax
	mov eax, b
	pop ebx
	sub eax, ebx
	jle faux_gt_1
	mov eax, 1
	jmp fin_gt_1
faux_gt_1:
	mov eax, 0
fin_gt_1:
	jz fin_while_1
	mov eax, b
	push eax
	mov eax, a
	pop ebx
	mov ecx, eax
	div ecx, ebx
	mul ecx, ebx
	sub eax, ecx
	mov aux, eax
	mov eax, b
	mov a, eax
	mov eax, aux
	mov b, eax
	jmp debut_while_1
fin_while_1:
	mov eax, a
	out eax
CODE ENDS
```

#### PGCD

> Pseudo code :
```
let a = input;
let b = input;
while (0 < b)
do (let aux=(a mod b); let a=b; let b=aux );
output a .
```

> Instruction générés :
```
DATA SEGMENT
	a DD 
	b DD 
	aux DD 
DATA ENDS
CODE SEGMENT
	in eax
	mov a, eax
	in eax
	mov b, eax
debut_while_1:
	mov eax, 0
	push eax
	mov eax, b
	pop ebx
	sub eax, ebx
	jle faux_gt_1
	mov eax, 1
	jmp fin_gt_1
faux_gt_1:
	mov eax, 0
fin_gt_1:
	jz fin_while_1
	mov eax, b
	push eax
	mov eax, a
	pop ebx
	mov ecx, eax
	div ecx, ebx
	mul ecx, ebx
	sub eax, ecx
	mov aux, eax
	mov eax, b
	mov a, eax
	mov eax, aux
	mov b, eax
	jmp debut_while_1
fin_while_1:
	mov eax, a
	out eax
CODE ENDS
```

