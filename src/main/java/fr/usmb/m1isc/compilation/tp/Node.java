package fr.usmb.m1isc.compilation.tp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Node {

    protected NodeType type;
	protected String nom;
    protected Object valeur;
	protected ArrayList<Node> fils = new ArrayList<Node>();

	/**
	 * Constructeur Node
	 * @param nom
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
	 * @param nom
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
	 * @param nom
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
	 * @param nom
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

	/**
	 * Ajoute un fils à la liste des fils
	 * @param fils
	 */
	public void addFils(Node fils) {
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

    protected String toInstruction() {
        switch (this.type) {
            case POINT:
                return this.fils.get(0).toInstruction();
            case SEMI:
                return this.fils.get(0).toInstruction() + this.fils.get(1).toInstruction();
            case IDENT:
                return "\tmov eax, " + this.valeur.toString() + "\n";
            case ENTIER:
                return "\tmov eax, " + this.valeur.toString() + "\n";
            case LET:
                return this.fils.get(1).toInstruction() + 
                    "\tmov " + this.fils.get(0).valeur.toString() + ", eax\n";
            case DIV:
                return this.fils.get(0).toInstruction() + 
                    "\tpush eax\n" + 
                    this.fils.get(1).toInstruction() + 
                    "\tpop ebx\n" + 
                    "\tdiv eax, ebx\n";
            case MUL:
                return this.fils.get(0).toInstruction() + 
                    "\tpush eax\n" + 
                    this.fils.get(1).toInstruction() + 
                    "\tpop ebx\n" + 
                    "\tmul eax, ebx\n";
            case MOINS:
                return this.fils.get(0).toInstruction() + 
                    "\tpush eax\n" + 
                    this.fils.get(1).toInstruction() + 
                    "\tpop ebx\n" + 
                    "\tsub eax, ebx\n";
            case PLUS:
                return this.fils.get(0).toInstruction() + 
                    "\tpush eax\n" + 
                    this.fils.get(1).toInstruction() + 
                    "\tpop ebx\n" + 
                    "\tadd eax, ebx\n";
            default:
                throw new IllegalArgumentException("Invalid instruction Node " + this.type);
        }
    }

    public void toProgram(String name, HashMap<String, Integer> env) {
        try (FileWriter w = new FileWriter(name + ".lambdada", true)) {
            w.write("\n");
            w.write(this.toString() + "\n");
            w.write("\n");
            w.write("DATA SEGMENT\n");
            for (String key : env.keySet()) {
                w.write("\t" + key + " DD " + env.get(key) + "\n");
            }
            w.write("DATA ENDS\n");
            w.write("CODE SEGMENT\n");
            w.write(this.toInstruction());
            w.write("CODE ENDS\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public void afficher() {
		System.out.println(this.toString());
	}
}