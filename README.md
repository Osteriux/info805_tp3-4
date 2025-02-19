###### Xavier Mazière

# INFO805 TP3-4

## Présentation

### Node

La classe Node représente un arbre n-aire utilisé pour construire l'arbre abstrait du programme pour ensuit le convertire en lambdada.

### NodeType

NodeType est un enum des différents types de nodes (aka d'opérations) supportés par Node. Il permet de créer et d'interpréter les nodes sans ambiguïté.

## Examples

> Calculatrice
```
let op = input;
let a = input;
let b = input;
while (not ( op = 0 ) )
do (
    if (op = 1) 
    then (
        output a + b
    );
    if (op = 2)
    then (
        output a - b
    );
    if (op = 3)
    then (
        output a * b
    );
    if (op = 4)
    then (
        output a / b
    );
    let op = input;
    let a = input;
    let b = input
)
.
```
> Calculatrice
```
DATA SEGMENT
	op DD 
	a DD 
	b DD 
DATA ENDS
CODE SEGMENT
	in eax
	mov op, eax
	in eax
	mov a, eax
	in eax
	mov b, eax
	mov eax, op
	push eax
	mov eax, 1
	pop ebx
	sub eax, ebx
	jz vrai_egal_1
	mov eax, 0
	jmp fin_egal_1
vrai_egal_1:
	mov eax, 1
fin_egal_1:
	mov ebx, 0
	sub ebx, eax
	jl vrai_if_1
	jmp fin_if_1
vrai_if_1:
	mov eax, b
	push eax
	mov eax, a
	pop ebx
	add eax, ebx
	out eax
fin_if_1:
	mov eax, op
	push eax
	mov eax, 2
	pop ebx
	sub eax, ebx
	jz vrai_egal_2
	mov eax, 0
	jmp fin_egal_2
vrai_egal_2:
	mov eax, 1
fin_egal_2:
	mov ebx, 0
	sub ebx, eax
	jl vrai_if_2
	jmp fin_if_2
vrai_if_2:
	mov eax, b
	push eax
	mov eax, a
	pop ebx
	sub eax, ebx
	out eax
fin_if_2:
	mov eax, op
	push eax
	mov eax, 3
	pop ebx
	sub eax, ebx
	jz vrai_egal_3
	mov eax, 0
	jmp fin_egal_3
vrai_egal_3:
	mov eax, 1
fin_egal_3:
	mov ebx, 0
	sub ebx, eax
	jl vrai_if_3
	jmp fin_if_3
vrai_if_3:
	mov eax, b
	push eax
	mov eax, a
	pop ebx
	mul eax, ebx
	out eax
fin_if_3:
	mov eax, op
	push eax
	mov eax, 4
	pop ebx
	sub eax, ebx
	jz vrai_egal_4
	mov eax, 0
	jmp fin_egal_4
vrai_egal_4:
	mov eax, 1
fin_egal_4:
	mov ebx, 0
	sub ebx, eax
	jl vrai_if_4
	jmp fin_if_4
vrai_if_4:
	mov eax, b
	push eax
	mov eax, a
	pop ebx
	div eax, ebx
	out eax
fin_if_4:
CODE ENDS
```

> PGCD
```
let a = input;
let b = input;
while ( 0 < b )
do (
    let aux = ( a mod b );
    let a = b;
    let b = aux
);
output a
.
```
> PGCD
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

