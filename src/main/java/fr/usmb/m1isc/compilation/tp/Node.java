package fr.usmb.m1isc.compilation.tp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Node {

    protected NodeType type;
	protected String nom;
    protected Object valeur;
	protected ArrayList<Node> fils = new ArrayList<Node>();

	/**
	 * Constructeur Node
     * @exception IllegalArgumentException NodeTypes valides:
     *  ERROR, NIL, POINT, INPUT
	 * @param t type de Node
	 */
	public Node(NodeType t) {
        switch (t) {
            case ERROR:
                this.nom = "ERROR";
                break;
            case NIL:
                this.nom = "NIL";
                break;
            case POINT:
                this.nom = ".";
                break;
            case INPUT:
                this.nom = "INPUT";
                break;
            default:
                throw new IllegalArgumentException("Invalid 0 child Node " + t);
        }
        this.type = t;
		this.fils = new ArrayList<Node>();
	}

	/**
	 * Constructeur Unary Node 
     * @exception IllegalArgumentException NodeTypes valides:
     *  NOT, OUTPUT, THEN, ELSE, DO, MOINS_UNAIRE
	 * @param t type de Node
	 * @param fils1
	 */
	public Node(NodeType t, Node fils1) {
        switch (t) {
            case NOT:
                this.nom = "NOT";
                break;
            case OUTPUT:
                this.nom = "OUTPUT";
                break;
            case THEN:
                this.nom = "THEN";
                break;
            case ELSE:
                this.nom = "ELSE";
                break;
            case DO:
                this.nom = "DO";
                break;
            case MOINS_UNAIRE:
                this.nom = "MOINS_UNAIRE";
                break;
            default:
                throw new IllegalArgumentException("Invalid 1 child Node " + t);
        }
        this.type = t;
		this.fils = new ArrayList<Node>();
		this.fils.add(fils1);
	}

    /**
     * Constructeur Leaf Node
     * @exception IllegalArgumentException NodeTypes valides:
     *  ENTIER, IDENT
     * @exception IllegalArgumentException 
     *  ENTIER Node doit avoir une valeur Integer
     *  IDENT Node doit avoir une valeur String
     * @param t type de Node
     * @param valeur valeur de la Node (Integer: ENTIER ou String: IDENT)
     */
    public Node(NodeType t, Object valeur) {
        switch (t) {
            case ENTIER:
                if(!(valeur instanceof Integer)){
                    throw new IllegalArgumentException("ENTIER Node must have an Integer value");
                }
                this.nom = "ENTIER";
                break;
            case IDENT:
                if(!(valeur instanceof String)){
                    throw new IllegalArgumentException("IDENT Node must have a String value");
                }
                this.nom = "IDENT";
                break;
            default:
                throw new IllegalArgumentException("Invalid leaf Node " + t);
        }
        this.type = t;
        this.valeur = valeur;
        this.fils = new ArrayList<Node>();
    }

	/**
	 * Constructeur Binary Node
     * @exception IllegalArgumentException NodeTypes valides:
     *  PLUS, MOINS, MUL, DIV, MOD, OR, AND, EGAL, GT, GTE, LET, WHILE, SEMI, IF_T
	 * @param t type de Node
	 * @param fils1
	 * @param fils2
	 */
	public Node(NodeType t, Node fils1, Node fils2) {
        switch (t) {
            case PLUS:
                nom = "+";
                break;
            case MOINS:
                nom = "-";
                break;
            case MUL:
                nom = "*";
                break;
            case DIV:
                nom = "/";
                break;
            case MOD:
                nom = "%";
                break;
            case OR:
                nom = "OR";
                break;
            case AND:
                nom = "AND";
                break;
            case EGAL:
                nom = "==";
                break;
            case GT:
                nom = ">";
                break;
            case GTE:
                nom = ">=";
                break;
            case LET:
                nom = "LET";
                break;
            case WHILE:
                nom = "WHILE";
                break;
            case SEMI:
                nom = "SEMI";
                break;
            case IF_T:
                nom = "IF_THEN";
                break;
            default:
                throw new IllegalArgumentException("Invalid 2 child Node " + t);
        }
		this.type = t;
		this.fils = new ArrayList<Node>();
		this.fils.add(fils1);
		this.fils.add(fils2);
	}

	/**
	 * Constructeur Ternary Node
     * @exception IllegalArgumentException NodeTypes valides:
     *  IF_TE
	 * @param t type de Node
	 * @param fils1
	 * @param fils2
	 * @param fils3
	 */
	public Node(NodeType t, Node fils1, Node fils2, Node fils3) {
        switch (t) {
            case IF_TE:
                nom = "IF_THEN_ELSE";
                break;
            default:
                throw new IllegalArgumentException("Invalid 3 child Node " + t);
        }
		this.type = t;
		this.fils = new ArrayList<Node>();
		this.fils.add(fils1);
		this.fils.add(fils2);
		this.fils.add(fils3);
	}

	/** addFils
	 * Ajoute un fils à la liste des fils
	 * @param fils
	 */
	public void addFils(Node fils) {
        if(fils == null){
            throw new IllegalArgumentException("Cannot add null Node");
        }
		this.fils.add(fils);
	}

    @Override
	public String toString(){
        if(this.type == NodeType.ENTIER || this.type == NodeType.IDENT){
            return this.nom + "_" + this.valeur.toString();
        }
        String res = fils.isEmpty() ? nom : "("+ nom;
		for (Node fils : fils) {
			res += " " + fils.toString();
		}
		if (!fils.isEmpty()) {
			res += ")";
		}
		return res;
    }

    protected static HashMap<NodeType, Integer> createcpt() {
        HashMap<NodeType, Integer> cpt = new HashMap<NodeType, Integer>();

        cpt.clear();
        cpt.put(NodeType.AND,0);
        cpt.put(NodeType.OR,0);
        cpt.put(NodeType.NOT,0);
        cpt.put(NodeType.EGAL,0);
        cpt.put(NodeType.GT,0);
        cpt.put(NodeType.GTE,0);
        cpt.put(NodeType.WHILE, 0);
        cpt.put(NodeType.IF_T, 0);
        cpt.put(NodeType.IF_TE, 0);

        return cpt;
    }

    /** toInstruction
     * Convertie la Node et ses fils en code assembleur lambdada
     * Le résultat de chaque instruction est stocké dans le registre eax (si applicable)
     * @param cpt compteur pour les instructions conditionnelles pour créer des étiquettes uniques
     * @return code assembleur lambdada
     */
    protected String toInstruction(HashMap<NodeType, Integer> cpt) {
        switch (this.type) {
            case POINT:
                return this.fils.get(0).toInstruction(cpt); // On ne fait rien avec POINT (on continue d'explorer l'arbre)
            case SEMI:
                return this.fils.get(0).toInstruction(cpt) + // On exécute la première instruction
                     this.fils.get(1).toInstruction(cpt); // On exécute la deuxième instruction
            case NIL:
                return "\tnop\n"; // No operation
            case IDENT:
                return "\tmov eax, " + this.valeur.toString() + "\n"; // On charge la valeur de la variable dans eax
            case ENTIER:
                return "\tmov eax, " + this.valeur.toString() + "\n"; // On charge la valeur de l'entier dans eax
            case INPUT:
                return "\tin eax\n"; // On charge la valeur de l'entrée standard dans eax
            case OUTPUT:
                return this.fils.get(0).toInstruction(cpt) + // On exécute l'instruction à afficher (qui doit mettre le résultat dans eax)
                 "\tout eax\n"; // On affiche la valeur de eax
            case THEN:
                return this.fils.get(0).toInstruction(cpt); // On ne fait rien avec THEN (on continue d'explorer l'arbre)
            case ELSE:
                return this.fils.get(0).toInstruction(cpt); // On ne fait rien avec ELSE (on continue d'explorer l'arbre)
            case DO:
                return this.fils.get(0).toInstruction(cpt); // On ne fait rien avec DO (on continue d'explorer l'arbre)
            case LET:
                return this.fils.get(1).toInstruction(cpt) + // On exécute l'instruction d'initialisation (qui doit mettre le résultat dans eax)
                    "\tmov " + this.fils.get(0).valeur.toString() + ", eax\n"; // On stocke le résultat dans la variable
            case DIV:
                return this.fils.get(1).toInstruction(cpt) + // On charge le deuxième opérande dans eax
                    "\tpush eax\n" + // On sauvegarde le deuxième opérande
                    this.fils.get(0).toInstruction(cpt) +  // On charge le premier opérande dans eax
                    "\tpop ebx\n" + // On restaure le deuxième opérande dans ebx
                    "\tdiv eax, ebx\n"; // On divise le premier opérande par le deuxième
            case MUL:
                return this.fils.get(1).toInstruction(cpt) + // On charge le deuxième opérande dans eax
                    "\tpush eax\n" + // On sauvegarde le deuxième opérande
                    this.fils.get(0).toInstruction(cpt) + // On charge le premier opérande dans eax
                    "\tpop ebx\n" + // On restaure le deuxième opérande dans ebx
                    "\tmul eax, ebx\n"; // On multiplie le premier opérande par le deuxième
            case MOINS:
                return this.fils.get(1).toInstruction(cpt) + // On charge le deuxième opérande dans eax
                    "\tpush eax\n" + // On sauvegarde le deuxième opérande
                    this.fils.get(0).toInstruction(cpt) + // On charge le premier opérande dans eax
                    "\tpop ebx\n" + // On restaure le deuxième opérande dans ebx
                    "\tsub eax, ebx\n"; // On soustrait le deuxième opérande du premier
            case PLUS:
                return this.fils.get(1).toInstruction(cpt) + // On charge le deuxième opérande dans eax
                    "\tpush eax\n" + // On sauvegarde le deuxième opérande
                    this.fils.get(0).toInstruction(cpt) + // On charge le premier opérande dans eax
                    "\tpop ebx\n" + // On restaure le deuxième opérande dans ebx
                    "\tadd eax, ebx\n";// On ajoute le deuxième opérande au premier
            case MOINS_UNAIRE:
                return this.fils.get(0).toInstruction(cpt) + // On charge l'opérande dans eax
                    "\tmov ebx, eax\n" + // On copie l'opérande dans ebx
                    "\tmov eax, 0\n" + // On met 0 dans eax
                    "\tsub eax, ebx\n"; // On soustrait l'opérande à 0
            case MOD:
                return this.fils.get(1).toInstruction(cpt) + // On charge le deuxième opérande dans eax
                    "\tpush eax\n" + // On sauvegarde le deuxième opérande
                    this.fils.get(0).toInstruction(cpt) + // On charge le premier opérande dans eax
                    "\tpop ebx\n" + // On restaure le deuxième opérande dans ebx
                    "\tmov ecx, eax\n" + // On copie le premier opérande dans ecx
                    "\tdiv ecx, ebx\n" + // On divise le premier opérande par le deuxième
                    "\tmul ecx, ebx\n" + // On multiplie le quotient par le deuxième opérande
                    "\tsub eax, ecx\n"; // On soustrait le produit du quotient par le deuxième opérande au premier opérande
            case GT:
                cpt.put(NodeType.GT, cpt.get(NodeType.GT) + 1);
                String eId = cpt.get(NodeType.GT).toString(); // Créer un identifiant unique pour l'instruction
                return this.fils.get(0).toInstruction(cpt) + // On charge le premier opérande dans eax
                    "\tpush eax\n" + // On sauvegarde le premier opérande
                    this.fils.get(1).toInstruction(cpt) + // On charge le deuxième opérande dans eax
                    "\tpop ebx\n" + // On restaure le premier opérande dans ebx
                    "\tsub eax, ebx\n" + // On soustrait le deuxième opérande du premier
                    "\tjle faux_gt_"+eId+"\n" + // Si le résultat est inférieur ou égal à 0, on saute à la fausse condition
                    "\tmov eax, 1\n" + // Sinon, on met 1 dans eax
                    "\tjmp fin_gt_"+eId+"\n" + // On saute à la fin de la condition
                    "faux_gt_"+eId+":\n" + // Étiquette pour la fausse condition
                    "\tmov eax, 0\n" + // On met 0 dans eax
                    "fin_gt_"+eId+":\n"; // Étiquette pour la fin de la condition
            case GTE:
                cpt.put(NodeType.GTE, cpt.get(NodeType.GTE) + 1);
                eId = cpt.get(NodeType.GTE).toString(); // Créer un identifiant unique pour l'instruction
                return this.fils.get(0).toInstruction(cpt) + // On charge le premier opérande dans eax
                    "\tpush eax\n" + // On sauvegarde le premier opérande
                    this.fils.get(1).toInstruction(cpt) + // On charge le deuxième opérande dans eax
                    "\tpop ebx\n" + // On restaure le premier opérande dans ebx
                    "\tsub eax, ebx\n" + // On soustrait le deuxième opérande du premier
                    "\tjl faux_gte_"+eId+"\n" + // Si le résultat est inférieur à 0, on saute à la fausse condition
                    "\tmov eax, 1\n" + // Sinon, on met 1 dans eax
                    "\tjmp fin_gte_"+eId+"\n" + // On saute à la fin de la condition
                    "faux_gte_"+eId+":\n" + // Étiquette pour la fausse condition
                    "\tmov eax, 0\n" + // On met 0 dans eax
                    "fin_gte_"+eId+":\n"; // Étiquette pour la fin de la condition
            case EGAL:
                cpt.put(NodeType.EGAL, cpt.get(NodeType.EGAL) + 1);
                eId = cpt.get(NodeType.EGAL).toString(); // Créer un identifiant unique pour l'instruction
                return this.fils.get(0).toInstruction(cpt) + // On charge le premier opérande dans eax
                    "\tpush eax\n" + // On sauvegarde le premier opérande
                    this.fils.get(1).toInstruction(cpt) + // On charge le deuxième opérande dans eax
                    "\tpop ebx\n" + // On restaure le premier opérande dans ebx
                    "\tsub eax, ebx\n" + // On soustrait le deuxième opérande du premier
                    "\tjz vrai_egal_"+eId+"\n" + // Si le résultat est égal à 0, on saute à la vrai condition
                    "\tmov eax, 0\n" + // Sinon, on met 1 dans eax
                    "\tjmp fin_egal_"+eId+"\n" + // On saute à la fin de la condition
                    "vrai_egal_"+eId+":\n" + // Étiquette pour la fausse condition
                    "\tmov eax, 1\n" + // On met 0 dans eax
                    "fin_egal_"+eId+":\n"; // Étiquette pour la fin de la condition
            case NOT:
                cpt.put(NodeType.NOT, cpt.get(NodeType.NOT) + 1);
                eId = cpt.get(NodeType.NOT).toString(); // Créer un identifiant unique pour l'instruction
                return this.fils.get(0).toInstruction(cpt) + // On charge l'opérande dans eax
                    "\tmov ebx, 0\n" + // On met 0 dans ebx
                    "\tsub ebx, eax\n" + // On soustrait l'opérande à 0
                    "\tjl faux_not_"+eId+"\n" + // Si le résultat est inférieur à 0, on saute à la fausse condition
                    "\tmov eax, 1\n" + // Sinon, on met 1 dans eax
                    "\tjmp fin_not_"+eId+"\n" + // On saute à la fin de la condition
                    "faux_not_"+eId+":\n" + // Étiquette pour la fausse condition
                    "\tmov eax, 0\n" + // On met 0 dans eax
                    "fin_not_"+eId+":\n"; // Étiquette pour la fin de la condition
            case OR:
                cpt.put(NodeType.OR, cpt.get(NodeType.OR) + 1);
                eId = cpt.get(NodeType.OR).toString(); // Créer un identifiant unique pour l'instruction
                return this.fils.get(0).toInstruction(cpt) + // On charge le premier opérande dans eax
                    "\tmov ebx, 0\n" + // On met 0 dans ebx
                    "\tsub ebx, eax\n" + // On soustrait le premier opérande à 0
                    "\tjge vrai_or_"+eId+"\n" + // Si le premier opérande est supérieur ou égal à 0, on saute à la vraie condition
                    this.fils.get(1).toInstruction(cpt) + // On charge le deuxième opérande dans eax
                    "\tmov ebx, 0\n" + // On met 0 dans ebx
                    "\tsub ebx, eax\n" + // On soustrait le deuxième opérande à 0
                    "\tjge vrai_or_"+eId+"\n" + // Si le deuxième opérande est supérieur ou égal à 0, on saute à la vraie condition
                    "\tmov eax, 0\n" + // Sinon, on met 0 dans eax
                    "\tjmp fin_or_"+eId+"\n" + // On saute à la fin de la condition
                    "vrai_or_"+eId+":\n" + // Étiquette pour la vraie condition
                    "\tmov eax, 1\n" + // On met 1 dans eax
                    "fin_or_"+eId+":\n"; // Étiquette pour la fin de la condition
            case AND:
                cpt.put(NodeType.AND, cpt.get(NodeType.AND) + 1);
                eId = cpt.get(NodeType.AND).toString(); // Créer un identifiant unique pour l'instruction
                return this.fils.get(0).toInstruction(cpt) + // On charge le premier opérande dans eax
                    "\tmov ebx, 0\n" + // On met 0 dans ebx
                    "\tsub ebx, eax\n" + // On soustrait le premier opérande à 0
                    "\tjl faux_and_"+eId+"\n" + // Si le premier opérande est inférieur à 0, on saute à la fausse condition
                    this.fils.get(1).toInstruction(cpt) + // On charge le deuxième opérande dans eax
                    "\tmov ebx, 0\n" + // On met 0 dans ebx
                    "\tsub ebx, eax\n" + // On soustrait le deuxième opérande à 0
                    "\tjl faux_and_"+eId+"\n" + // Si le deuxième opérande est inférieur à 0, on saute à la fausse condition
                    "\tmov eax, 1\n" + // Sinon, on met 1 dans eax
                    "\tjmp fin_and_"+eId+"\n" + // On saute à la fin de la condition
                    "faux_and_"+eId+":\n" + // Étiquette pour la fausse condition
                    "\tmov eax, 0\n" + // On met 0 dans eax
                    "fin_and_"+eId+":\n"; // Étiquette pour la fin de la condition
            case IF_T:
                cpt.put(NodeType.IF_T, cpt.get(NodeType.IF_T) + 1);
                eId = cpt.get(NodeType.IF_T).toString(); // Créer un identifiant unique pour l'instruction
                return this.fils.get(0).toInstruction(cpt) + // On charge l'opérande condition dans eax
                    "\tmov ebx, 0\n" + // On met 0 dans ebx
                    "\tsub ebx, eax\n" + // On soustrait l'opérande condition à 0
                    "\tjl vrai_if_"+eId+"\n" + // Si l'opérande condition est inférieur à 0, on saute à la vraie condition
                    "\tjmp fin_if_"+eId+"\n" + // Sinon, on saute à la fin de la condition
                    "vrai_if_"+eId+":\n" + // Étiquette pour la vraie condition
                    this.fils.get(1).toInstruction(cpt) + // Instruction à exécuter si la condition est vraie
                    "fin_if_"+eId+":\n"; // Étiquette pour la fin de la condition
            case IF_TE:
                cpt.put(NodeType.IF_TE, cpt.get(NodeType.IF_TE) + 1);
                eId = cpt.get(NodeType.IF_TE).toString(); // Créer un identifiant unique pour l'instruction
                return this.fils.get(0).toInstruction(cpt) + // On charge l'opérande condition dans eax
                    "\tmov ebx, 0\n" + // On met 0 dans ebx
                    "\tsub ebx, eax\n" + // On soustrait l'opérande condition à 0
                    "\tjl vrai_if_"+eId+"\n" + // Si l'opérande condition est inférieur à 0, on saute à la vraie condition
                    this.fils.get(2).toInstruction(cpt) + // Instruction à exécuter si la condition est fausse
                    "\tjmp fin_if_"+eId+"\n" + // On saute à la fin de la condition
                    "vrai_if_"+eId+":\n" + // Étiquette pour la vraie condition
                    this.fils.get(1).toInstruction(cpt) + // Instruction à exécuter si la condition est vraie
                    "fin_if_"+eId+":\n"; // Étiquette pour la fin de la condition
            case WHILE:
                cpt.put(NodeType.WHILE, cpt.get(NodeType.WHILE) + 1);
                eId = cpt.get(NodeType.WHILE).toString(); // Créer un identifiant unique pour l'instruction
                return "debut_while_"+eId+":\n" + // Étiquette pour le début de la boucle
                    this.fils.get(0).toInstruction(cpt) + // On charge l'opérande condition dans eax
                    "\tjz fin_while_"+eId+"\n" + // Si l'opérande condition est égal à 0, on saute à la fin de la boucle
                    this.fils.get(1).toInstruction(cpt) + // Instruction à exécuter tant que la condition est vraie
                    "\tjmp debut_while_"+eId+"\n" + // On saute au début de la boucle
                    "fin_while_"+eId+":\n"; // Étiquette pour la fin de la boucle
            default:
                throw new IllegalArgumentException("Invalid instruction Node " + this.type);
        }
    }

    /** toProgram
     * Convertie un Arbre abstrait en un programme lambdada
     * @exception IllegalArgumentException Un programme commence par une Node POINT
     * @param name nom du fichier .lambdada à créer
     * @param env environnement de variables à déclarer
     */
    public void toProgram(String name, HashMap<String, Integer> env) {
        if(this.type != NodeType.POINT){
            throw new IllegalArgumentException("Program Node must be a POINT Node");
        }
        try (FileWriter w = new FileWriter(name + ".lambdada", false)) {
            w.write("\n");
            w.write(this.toString() + "\n");
            w.write("\n");
            w.write("DATA SEGMENT\n");
            for (String key : env.keySet()) {
                w.write("\t" + key + " DD \n");
            }
            w.write("DATA ENDS\n");
            w.write("CODE SEGMENT\n");
            w.write(this.toInstruction(createcpt()));
            w.write("CODE ENDS\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public void afficher() {
		System.out.println(this.toString());
	}
}