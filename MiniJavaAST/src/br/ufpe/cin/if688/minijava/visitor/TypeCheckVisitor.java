package br.ufpe.cin.if688.minijava.visitor;

import br.ufpe.cin.if688.minijava.ast.And;
import br.ufpe.cin.if688.minijava.symboltable.Class;
import br.ufpe.cin.if688.minijava.ast.ArrayAssign;
import br.ufpe.cin.if688.minijava.ast.ArrayLength;
import br.ufpe.cin.if688.minijava.ast.ArrayLookup;
import br.ufpe.cin.if688.minijava.ast.Assign;
import br.ufpe.cin.if688.minijava.ast.Block;
import br.ufpe.cin.if688.minijava.ast.BooleanType;
import br.ufpe.cin.if688.minijava.ast.Call;
import br.ufpe.cin.if688.minijava.ast.ClassDeclExtends;
import br.ufpe.cin.if688.minijava.ast.ClassDeclSimple;
import br.ufpe.cin.if688.minijava.ast.False;
import br.ufpe.cin.if688.minijava.ast.Formal;
import br.ufpe.cin.if688.minijava.ast.Identifier;
import br.ufpe.cin.if688.minijava.ast.IdentifierExp;
import br.ufpe.cin.if688.minijava.ast.IdentifierType;
import br.ufpe.cin.if688.minijava.ast.If;
import br.ufpe.cin.if688.minijava.ast.IntArrayType;
import br.ufpe.cin.if688.minijava.ast.IntegerLiteral;
import br.ufpe.cin.if688.minijava.ast.IntegerType;
import br.ufpe.cin.if688.minijava.ast.LessThan;
import br.ufpe.cin.if688.minijava.ast.MainClass;
import br.ufpe.cin.if688.minijava.ast.MethodDecl;
import br.ufpe.cin.if688.minijava.ast.Minus;
import br.ufpe.cin.if688.minijava.ast.NewArray;
import br.ufpe.cin.if688.minijava.ast.NewObject;
import br.ufpe.cin.if688.minijava.ast.Not;
import br.ufpe.cin.if688.minijava.ast.Plus;
import br.ufpe.cin.if688.minijava.ast.Print;
import br.ufpe.cin.if688.minijava.ast.Program;
import br.ufpe.cin.if688.minijava.ast.This;
import br.ufpe.cin.if688.minijava.ast.Times;
import br.ufpe.cin.if688.minijava.ast.True;
import br.ufpe.cin.if688.minijava.ast.Type;
import br.ufpe.cin.if688.minijava.ast.VarDecl;
import br.ufpe.cin.if688.minijava.ast.While;
import br.ufpe.cin.if688.minijava.symboltable.Method;
import br.ufpe.cin.if688.minijava.symboltable.SymbolTable;

public class TypeCheckVisitor implements IVisitor<Type> {

	private SymbolTable symbolTable;
	private Class currClass;
	private Method currMethod;
	

	TypeCheckVisitor(SymbolTable st) {
		symbolTable = st;
	}

	// MainClass m;
	// ClassDeclList cl;
	public Type visit(Program n) {
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.elementAt(i).accept(this);
		}
		return null;
	}

	// Identifier i1,i2;
	// Statement s;
	public Type visit(MainClass n) {
		currClass = symbolTable.getClass(n.i1.toString());
		n.i1.accept(this);
		n.i2.accept(this);
		n.s.accept(this);
		currClass = null;
		return null;
	}

	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclSimple n) {
		currClass = symbolTable.getClass(n.i.toString());
		n.i.accept(this);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		currClass = null;
		return null;
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclExtends n) {
		currClass = symbolTable.getClass(n.i.toString());
		n.i.accept(this);
		n.j.accept(this);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		currClass = null;
		return null;
	}

	// Type t;
	// Identifier i;
	public Type visit(VarDecl n) {
		n.t.accept(this);
		n.i.accept(this);
		return n.t;
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public Type visit(MethodDecl n) {
		Type t1 = n.t.accept(this);
		n.i.accept(this);
		currMethod = symbolTable.getMethod(n.i.s, currClass.getId());
		Type t = symbolTable.getMethodType(n.i.s, currClass.getId());
		Type [] formalListTypes = new Type[n.fl.size()];
		Type [] varDeclList = new Type[n.vl.size()];
		Type [] statementList = new Type[n.sl.size()];
		
		for (int i = 0; i < n.fl.size(); i++) {
			formalListTypes[i] = n.fl.elementAt(i).accept(this);
			
		}
		for (int i = 0; i < n.vl.size(); i++) {
			varDeclList[i] = n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.sl.size(); i++) {
			 statementList[i] = n.sl.elementAt(i).accept(this);
		}
		Type exp = n.e.accept(this);
		if(!symbolTable.compareTypes(t1, exp)) {
			System.out.println( "Retorno da expressão incompatível com o tipo definido");
			

		}
		currMethod = null;
		return t;
	}

	// Type t;
	// Identifier i;
	public Type visit(Formal n) {
		n.t.accept(this);
		n.i.accept(this);
		return null;
	}

	public Type visit(IntArrayType n) {
		return null;
	}

	public Type visit(BooleanType n) {
		return null;
	}

	public Type visit(IntegerType n) {
		return null;
	}

	// String s;
	public Type visit(IdentifierType n) {
		return null;
	}

	// StatementList sl;
	public Type visit(Block n) {
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		return null;
	}

	// Exp e;
	// Statement s1,s2;
	public Type visit(If n) {
		Type expType = n.e.accept(this);
		if(expType == null) {
			return null;
		}
		if(!(expType instanceof BooleanType)) {
			System.out.println("A expressão não é um tipo booleano");
		}
	
		n.s1.accept(this);
		n.s2.accept(this);
		return null;
	}

	// Exp e;
	// Statement s;
	public Type visit(While n) {
		Type expType = n.e.accept(this);
		if(expType == null) return null;
		if(!(expType instanceof BooleanType)) {
			System.out.println("A expressão não é um tipo booleano");
		}
		n.s.accept(this);
		return null;
	}

	// Exp e;
	public Type visit(Print n) {
		Type expType = n.e.accept(this);
		if(expType == null) return null;
		if(expType instanceof Type) {
			System.out.println("No PRINT: " + n.e.toString() + " não é um tipo");
			
		}
		return null;
	}

	// Identifier i;
	// Exp e;
	public Type visit(Assign n) {
		Type idType = n.i.accept(this);
		Type expType = n.e.accept(this);
		if(idType == null) {
			return null;
		}
		if(expType == null) {
			return null;
		}
		if(!symbolTable.compareTypes(idType, expType)) {
			System.out.println("Em assign, o tipo do identificador " + n.i.toString() + " e da expressão " + n.e.toString());
			n.accept(new PrettyPrintVisitor());
			System.out.println("não são do mesmo tipo");
		}
		
		return null;
	}

	// Identifier i;
	// Exp e1,e2;
	public Type visit(ArrayAssign n) {
		Type idType = n.i.accept(this);
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		if(idType == null | expType1 == null | expType2 == null) {
			return null;
		}
		if(!(expType1 instanceof IntegerType)) {
			System.out.println(" Em ArrayAssign ");
			n.accept(new PrettyPrintVisitor());
			System.out.println(n.e1.toString() + " não é do tipo INT");
		}
		return null;
	}

	// Exp e1,e2;
	public Type visit(And n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		if(expType1 == null | expType2 == null) return null;
		if(!(expType1 instanceof BooleanType) | !(expType2 instanceof BooleanType)) {
			System.out.println("Em AND a expressão :");
			n.accept(new PrettyPrintVisitor());
		}
		return  new BooleanType();
	}

	// Exp e1,e2;
	public Type visit(LessThan n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Type visit(Plus n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Type visit(Minus n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Type visit(Times n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e1,e2;
	public Type visit(ArrayLookup n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	// Exp e;
	public Type visit(ArrayLength n) {
		n.e.accept(this);
		return null;
	}

	// Exp e;
	// Identifier i;
	// ExpList el;
	public Type visit(Call n) {
		n.e.accept(this);
		n.i.accept(this);
		for (int i = 0; i < n.el.size(); i++) {
			n.el.elementAt(i).accept(this);
		}
		return null;
	}

	// int i;
	public Type visit(IntegerLiteral n) {
		return null;
	}

	public Type visit(True n) {
		return null;
	}

	public Type visit(False n) {
		return null;
	}

	// String s;
	public Type visit(IdentifierExp n) {
		return null;
	}

	public Type visit(This n) {
		return null;
	}

	// Exp e;
	public Type visit(NewArray n) {
		n.e.accept(this);
		return null;
	}

	// Identifier i;
	public Type visit(NewObject n) {
		return null;
	}

	// Exp e;
	public Type visit(Not n) {
		n.e.accept(this);
		return null;
	}

	// String s;
	public Type visit(Identifier n) {
		return null;
	}
}
