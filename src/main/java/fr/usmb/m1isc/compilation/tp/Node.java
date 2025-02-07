package fr.usmb.m1isc.compilation.tp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Node {
    protected static HashMap<NodeType, Integer> etiquetteCpt = new HashMap<NodeType, Integer>();

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

    protected static void initEtiquetteCpt() {
        etiquetteCpt.clear();
        etiquetteCpt.put(NodeType.AND,0);
        etiquetteCpt.put(NodeType.OR,0);
        etiquetteCpt.put(NodeType.NOT,0);
        etiquetteCpt.put(NodeType.EGAL,0);
        etiquetteCpt.put(NodeType.GT,0);
        etiquetteCpt.put(NodeType.GTE,0);
        etiquetteCpt.put(NodeType.WHILE, 0);
        etiquetteCpt.put(NodeType.IF_T, 0);
        etiquetteCpt.put(NodeType.IF_TE, 0);
    }

    protected String toInstruction() {
        switch (this.type) {
            case POINT:
                return this.fils.get(0).toInstruction();
            case SEMI:
                return this.fils.get(0).toInstruction() +
                     this.fils.get(1).toInstruction();
            case NIL:
                return "\tnop\n";
            case IDENT:
                return "\tmov eax, " + this.valeur.toString() + "\n";
            case ENTIER:
                return "\tmov eax, " + this.valeur.toString() + "\n";
            case INPUT:
                return "\tin eax\n";
            case OUTPUT:
                return this.fils.get(0).toInstruction() +
                 "\tout eax\n";
            case THEN:
                return this.fils.get(0).toInstruction();
            case ELSE:
                return this.fils.get(0).toInstruction();
            case DO:
                return this.fils.get(0).toInstruction();
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
            case MOINS_UNAIRE:
                return this.fils.get(0).toInstruction() + 
                    "\tmov ebx, eax\n" +
                    "\tmov eax, 0\n" +
                    "\tsub eax, ebx\n";
            case MOD:
                return this.fils.get(1).toInstruction() + 
                    "\tpush eax\n" + 
                    this.fils.get(0).toInstruction() + 
                    "\tpop ebx\n" +
                    "\tmov ecx, eax\n" +
                    "\tdiv ecx, ebx\n" +
                    "\tmul ecx, ebx\n" +
                    "\tsub eax, ecx\n";
            case GT:
                etiquetteCpt.put(NodeType.GT, etiquetteCpt.get(NodeType.GT) + 1);
                String eId = etiquetteCpt.get(NodeType.GT).toString();
                return this.fils.get(0).toInstruction() + 
                    "\tpush eax\n" + 
                    this.fils.get(1).toInstruction() + 
                    "\tpop ebx\n" +
                    "\tsub eax, ebx\n" +
                    "\tjle faux_gt_"+eId+"\n" +
                    "\tmov eax, 1\n" +
                    "\tjmp fin_gt_"+eId+"\n" +
                    "faux_gt_"+eId+":\n" +
                    "\tmov eax, 0\n" +
                    "fin_gt_"+eId+":\n";
            case GTE:
                etiquetteCpt.put(NodeType.GTE, etiquetteCpt.get(NodeType.GTE) + 1);
                eId = etiquetteCpt.get(NodeType.GTE).toString();
                return this.fils.get(0).toInstruction() + 
                    "\tpush eax\n" + 
                    this.fils.get(1).toInstruction() + 
                    "\tpop ebx\n" +
                    "\tsub eax, ebx\n" +
                    "\tjl faux_gte_"+eId+"\n" +
                    "\tmov eax, 1\n" +
                    "\tjmp fin_gte_"+eId+"\n" +
                    "faux_gte_"+eId+":\n" +
                    "\tmov eax, 0\n" +
                    "fin_gte_"+eId+":\n";
            case EGAL:
                etiquetteCpt.put(NodeType.EGAL, etiquetteCpt.get(NodeType.EGAL) + 1);
                eId = etiquetteCpt.get(NodeType.EGAL).toString();
                return this.fils.get(0).toInstruction() + 
                    "\tpush eax\n" + 
                    this.fils.get(1).toInstruction() + 
                    "\tpop ebx\n" +
                    "\tsub eax, ebx\n" +
                    "\tjz faux_egal_"+eId+"\n" +
                    "\tmov eax, 1\n" +
                    "\tjmp fin_egal_"+eId+"\n" +
                    "faux_egal_"+eId+":\n" +
                    "\tmov eax, 0\n" +
                    "fin_egal_"+eId+":\n";
            case NOT:
                etiquetteCpt.put(NodeType.NOT, etiquetteCpt.get(NodeType.NOT) + 1);
                eId = etiquetteCpt.get(NodeType.NOT).toString();
                return this.fils.get(0).toInstruction() + 
                    "\tmov ebx, 0" +
                    "\tsub ebx, eax\n" +
                    "\tjl faux_not_"+eId+"\n" +
                    "\tmov eax, 1\n" +
                    "\tjmp fin_not_"+eId+"\n" +
                    "faux_not_"+eId+":\n" +
                    "\tmov eax, 0\n" +
                    "fin_not_"+eId+":\n";
            case OR:
                etiquetteCpt.put(NodeType.OR, etiquetteCpt.get(NodeType.OR) + 1);
                eId = etiquetteCpt.get(NodeType.OR).toString();
                return this.fils.get(0).toInstruction() + 
                    "\tpush eax\n" + 
                    this.fils.get(1).toInstruction() + 
                    "\tpop ebx\n" +
                    "\tmov ecx, 0" +
                    "\tcmp ecx, eax\n" + // Compare second operand with 0
                    "\tjl vrai_or_"+eId+"\n" + // If second operand is 1, jump to true case
                    "\tmov ecx, 0\n" +
                    "\tsub ecx, ebx\n" + // Compare first operand with 0
                    "\tjl vrai_or_"+eId+"\n" + // If first operand is 1, jump to true case
                    "\tmov eax, 0\n" + // Both operands are non-zero, set eax to 1 (false)
                    "\tjmp fin_and_"+eId+"\n" + // Jump to end
                    "vrai_or_"+eId+":\n" +
                    "\tmov eax, 1\n" + // One or both operands are zero, set eax to 0 (true)
                    "fin_and_"+eId+":\n";
            case AND:
                etiquetteCpt.put(NodeType.AND, etiquetteCpt.get(NodeType.AND) + 1);
                eId = etiquetteCpt.get(NodeType.AND).toString();
                return this.fils.get(0).toInstruction() + 
                    "\tpush eax\n" + 
                    this.fils.get(1).toInstruction() + 
                    "\tpop ebx\n" +
                    "\tcmp eax, 0\n" + // Compare second operand with 0
                    "\tje faux_and_"+eId+"\n" + // If second operand is 0, jump to false case
                    "\tcmp ebx, 0\n" + // Compare first operand with 0
                    "\tje faux_and_"+eId+"\n" + // If first operand is 0, jump to false case
                    "\tmov eax, 1\n" + // Both operands are non-zero, set eax to 1 (true)
                    "\tjmp fin_and_"+eId+"\n" + // Jump to end
                    "faux_and_"+eId+":\n" +
                    "\tmov eax, 0\n" + // One or both operands are zero, set eax to 0 (false)
                    "fin_and_"+eId+":\n";
            case IF_T:
                etiquetteCpt.put(NodeType.IF_T, etiquetteCpt.get(NodeType.IF_T) + 1);
                eId = etiquetteCpt.get(NodeType.IF_T).toString();
                return this.fils.get(0).toInstruction() + 
                    "\tmov ebx, 0\n" +
                    "\tsub ebx, eax\n" +
                    "\tjl vrai_if_"+eId+"\n" +
                    "\tjmp fin_if_"+eId+"\n" +
                    "vrai_if_"+eId+":\n" +
                    this.fils.get(1).toInstruction() +
                    "fin_if_"+eId+":\n";
            case IF_TE:
                etiquetteCpt.put(NodeType.IF_TE, etiquetteCpt.get(NodeType.IF_TE) + 1);
                eId = etiquetteCpt.get(NodeType.IF_TE).toString();
                return this.fils.get(0).toInstruction() + 
                    "\tmov ebx, 0\n" +
                    "\tsub ebx, eax\n" +
                    "\tjl vrai_if_"+eId+"\n" +
                    this.fils.get(1).toInstruction() +
                    "\tjmp fin_if_"+eId+"\n" +
                    "vrai_if_"+eId+":\n" +
                    this.fils.get(2).toInstruction() +
                    "fin_if_"+eId+":\n";
            case WHILE:
                etiquetteCpt.put(NodeType.WHILE, etiquetteCpt.get(NodeType.WHILE) + 1);
                eId = etiquetteCpt.get(NodeType.WHILE).toString();
                return "debut_while_"+eId+":\n" +
                    this.fils.get(0).toInstruction() + 
                    "\tjz fin_while_"+eId+"\n" +
                    this.fils.get(1).toInstruction() +
                    "\tjmp debut_while_"+eId+"\n" +
                    "fin_while_"+eId+":\n";
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
                w.write("\t" + key + " DD \n");
            }
            w.write("DATA ENDS\n");
            initEtiquetteCpt();
            w.write("CODE SEGMENT\n");
            w.write(this.toInstruction());
            w.write("CODE ENDS\n");
            etiquetteCpt.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public void afficher() {
		System.out.println(this.toString());
	}
}