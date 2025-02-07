package fr.usmb.m1isc.compilation.tp;

import java.util.ArrayList;

public class Node {
	protected String nom;
	protected ArrayList<Node> fils = new ArrayList<Node>();

	/**
	 * Constructeur Node
	 * @param nom
	 */
	public Node(String nom) {
		this.nom = nom;
		this.fils = new ArrayList<Node>();
	}

	/**
	 * Constructeur Unary Node 
	 * @param nom
	 * @param fils1
	 */
	public Node(String nom, Node fils1) {
		this.nom = nom;
		this.fils = new ArrayList<Node>();
		this.fils.add(fils1);
	}

	/**
	 * Constructeur Binary Node
	 * @param nom
	 * @param fils1
	 * @param fils2
	 */
	public Node(String nom, Node fils1, Node fils2) {
		this.nom = nom;
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
	public Node(String nom, Node fils1, Node fils2, Node fils3) {
		this.nom = nom;
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
	public String toString() {
		String res = fils.isEmpty() ? nom : "("+ nom;
		for (Node fils : fils) {
			res += " " + fils.toString();
		}
		if (!fils.isEmpty()) {
			res += ")";
		}
		return res;
	}

	public String toString(int indent){
		String res = "  ".repeat(indent);
		res += fils.isEmpty() ? nom : "("+ nom;
		for (Node fils : fils) {
			res += "\n" + fils.toString(indent+1);
		}
		if (!fils.isEmpty()) {
			res += "\n" + "  ".repeat(indent) + ")";
		}
		return res;
	}

	public void afficher() {
		System.out.println(this.toString());
	}

	public void afficher(int indent) {
		System.out.println(this.toString(indent));
	}
}