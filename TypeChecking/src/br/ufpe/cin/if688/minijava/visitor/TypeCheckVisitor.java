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
	private boolean fromVar = false;

	public TypeCheckVisitor(SymbolTable st) {
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
		currClass = symbolTable.getClass(n.i1.s);
		n.i1.accept(this);

		this.currMethod = symbolTable.getMethod("main", this.currClass.getId());
		this.fromVar = true;
		n.i2.accept(this);
		this.fromVar = false;
		currMethod = null;

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
		this.fromVar = true;
		n.i.accept(this);
		this.fromVar = false;
		return n.t;
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public Type visit(MethodDecl n) {
		currMethod = symbolTable.getMethod(n.i.s, currClass.getId());
		Type t1 = n.t.accept(this);
		n.i.accept(this);

		Type t = symbolTable.getMethodType(n.i.s, currClass.getId());

		for (int i = 0; i < n.fl.size(); i++) {
			n.fl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		Type expType = n.e.accept(this);
		if (!symbolTable.compareTypes(t1, expType)) {
			System.out.print("Retorno da expressão incompatível com o tipo definido, ");
			System.out.print("recibido ");
			expType.accept(new PrettyPrintVisitor());
			System.out.print(", mas esperado ");
			t1.accept(new PrettyPrintVisitor());
			System.out.println();
		}
		currMethod = null;
		return t;
	}

	// Type t;
	// Identifier i;
	public Type visit(Formal n) {
		this.fromVar = true;
		n.t.accept(this);
		n.i.accept(this);
		this.fromVar = false;
		return null;
	}

	public Type visit(IntArrayType n) {
		return new IntArrayType();
	}

	public Type visit(BooleanType n) {
		return new BooleanType();
	}

	public Type visit(IntegerType n) {
		return new IntegerType();
	}

	// String s;
	public Type visit(IdentifierType n) {
		if (symbolTable.containsClass(n.s))
			return new IdentifierType(n.s);
		else {
			System.out.println("Não pude encontrar classe do tipo " + n.s);
			return null;
		}
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
		if (expType == null) {
			return null;
		}
		if (!(expType instanceof BooleanType)) {
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
		if (expType == null)
			return null;
		if (!(expType instanceof BooleanType)) {
			System.out.println("A expressão não é um tipo booleano");
		}
		n.s.accept(this);
		return null;
	}

	// Exp e;
	public Type visit(Print n) {
		Type expType = n.e.accept(this);
		if (expType == null)
			return null;
		if (!(expType instanceof Type)) {
			System.out.print("No PRINT: ");
			n.e.accept(new PrettyPrintVisitor());
			System.out.println(" não é um tipo");

		}
		return null;
	}

	// Identifier i;
	// Exp e;
	public Type visit(Assign n) {
		this.fromVar = true;
		Type idType = n.i.accept(this);
		this.fromVar = false;
		Type expType = n.e.accept(this);
		if (idType == null) {
			return null;
		}
		if (expType == null) {
			return null;
		}
		if (!symbolTable.compareTypes(idType, expType)) {
			System.out.print("Em assign, o tipo do identificador " + n.i.toString() + " e da expressão ");
			n.e.accept(new PrettyPrintVisitor());
			System.out.println(" não são do mesmo tipo");
		}

		return null;
	}

	// Identifier i;
	// Exp e1,e2;
	public Type visit(ArrayAssign n) {
		this.fromVar = true;
		Type idType = n.i.accept(this);
		this.fromVar = false;
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		if (idType == null | expType1 == null | expType2 == null) {
			return null;
		}
		if (!(idType instanceof IntArrayType)) {
			System.out.println("O identificador " + n.i.toString() + " não é um array");
		}
		if (!(expType1 instanceof IntegerType)) {
			System.out.print(" Em ArrayAssign ");
			n.e1.accept(new PrettyPrintVisitor());
			System.out.println(" não é do tipo INT");
		}
		if (!(expType2 instanceof IntegerType)) {
			System.out.print(" Em ArrayAssign ");
			n.e2.accept(new PrettyPrintVisitor());
			System.out.println(" não é do tipo INT");
		}
		return null;
	}

	// Exp e1,e2;
	public Type visit(And n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		if (expType1 == null | expType2 == null)
			return null;
		if (!(expType1 instanceof BooleanType)) {
			System.out.print("Em AND a expressão: ");
			n.e1.accept(new PrettyPrintVisitor());
			System.out.println(" não é booleana");
		}
		if (!(expType2 instanceof BooleanType)) {
			System.out.print("Em AND a expressão: ");
			n.e2.accept(new PrettyPrintVisitor());
			System.out.println(" não é booleana");
		}
		return new BooleanType();
	}

	// Exp e1,e2;
	public Type visit(LessThan n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		if (expType1 == null | expType2 == null)
			return null;
		if (!(expType1 instanceof IntegerType)) {
			System.out.print("Em LessThan a expressão: ");
			n.e1.accept(new PrettyPrintVisitor());
			System.out.println(" não é do tipo INT");
		}
		if (!(expType2 instanceof IntegerType)) {
			System.out.print("Em LessThan a expressão: ");
			n.e2.accept(new PrettyPrintVisitor());
			System.out.println(" não é do tipo INT");
		}
		return new BooleanType();
	}

	// Exp e1,e2;
	public Type visit(Plus n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		if (expType1 == null | expType2 == null)
			return null;
		if (!(expType1 instanceof IntegerType)) {
			System.out.print("Em Plus: ");
			n.e1.accept(new PrettyPrintVisitor());
			System.out.println(" não é do tipo INT");
		}
		if (!(expType2 instanceof IntegerType)) {
			System.out.print("Em Plus a expressão: ");
			n.e2.accept(new PrettyPrintVisitor());
			System.out.println(" não é do tipo INT");
		}
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(Minus n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		if (expType1 == null | expType2 == null)
			return null;
		if (!(expType1 instanceof IntegerType)) {
			System.out.print("Em Minus : ");
			n.e1.accept(new PrettyPrintVisitor());
			System.out.println(" não é do tipo INT");
		}
		if (!(expType2 instanceof IntegerType)) {
			System.out.print("Em Minus a expressão: ");
			n.e2.accept(new PrettyPrintVisitor());
			System.out.println(" não é do tipo INT");
		}
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(Times n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		if (expType1 == null | expType2 == null)
			return null;
		if (!(expType1 instanceof IntegerType)) {
			System.out.print("Em Times : ");
			n.e1.accept(new PrettyPrintVisitor());
			System.out.println(" não é do tipo INT");
		}
		if (!(expType2 instanceof IntegerType)) {
			System.out.print("Em Times a expressão: ");
			n.e2.accept(new PrettyPrintVisitor());
			System.out.println(" não é do tipo INT");
		}
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(ArrayLookup n) {
		Type exp1Type = n.e1.accept(this);
		Type exp2Type = n.e2.accept(this);

		if (exp1Type != null && exp2Type != null) {
			if (!(exp1Type instanceof IntArrayType)) {
				System.out.print("A expressão ");
				n.e1.accept(new PrettyPrintVisitor());
				System.out.println(" em ArrayLookup não é um Int []");
			}

			if (!(exp2Type instanceof IntegerType)) {
				System.out.print("A expressão ");
				n.e2.accept(new PrettyPrintVisitor());
				System.out.println(" em ArrayLookup não é um int");
			}

			return new IntegerType();
		}
		return null;
	}

	// Exp e;
	public Type visit(ArrayLength n) {
		Type expType = n.e.accept(this);
		if (expType != null) {
			if (!(expType instanceof IntArrayType)) {
				System.out.print("A expressão ");
				n.e.accept(new PrettyPrintVisitor());
				System.out.println(" em ArrayLength não é do tipo IntArray");
			} else
				return new IntegerType();
		}
		return null;
	}

	// this.m().g()
	// Exp e;
	// Identifier i;
	// ExpList el;
	public Type visit(Call n) {
		Type expType = n.e.accept(this);
		String className = "";
		if (expType instanceof IdentifierType) {
			className = ((IdentifierType) expType).s;
		} else {
			System.out.println("");
		}

		if (symbolTable.getMethod(n.i.toString(), className) == null) {
			System.out.print("Erro na expressão: ");
			n.e.accept(new PrettyPrintVisitor());
			System.out.println(
					", que é do tipo da classe" + className + " a qual não contém o método: " + n.i.toString());
		}else {
			Type methodType = symbolTable.getMethod(n.i.toString(), className).type();
			Method method = symbolTable.getMethod(n.i.toString(), className);
			
			int sizeMethodParams = 0;
			
			while (true) {
				if (method.getParamAt(sizeMethodParams) != null)
					sizeMethodParams++;
				else
					break;
			}
			
			if (n.el.size() != sizeMethodParams) {
				System.out.println("Quantidade de parâmetros não são equivalentes");
			} else {
				for (int i = 0; i < n.el.size(); i++) {
					Type paramType = n.el.elementAt(i).accept(this);
					Type methodParamType = method.getParamAt(i).type();
					
					if (!(symbolTable.compareTypes(paramType, methodParamType))) {
						System.out.println("No parâmetro " + (i + 1) + " o tipo esperado era "
								+ getTypeName(methodParamType) + " e o recebido foi " + getTypeName(paramType));
					}
				}
			}
			return methodType;
		}


		return null;
	}

	// int i;
	public Type visit(IntegerLiteral n) {
		return new IntegerType();
	}

	public Type visit(True n) {
		return new BooleanType();
	}

	public Type visit(False n) {
		return new BooleanType();
	}

	// String s;
	public Type visit(IdentifierExp n) {
		Type t = symbolTable.getVarType(currMethod, currClass, n.s);
		if (t == null) {
			System.out.println("Expressão " + n.s + " não pode");
		}
		return t;
	}

	public Type visit(This n) {
		return currClass.type();
	}

	// Exp e;
	public Type visit(NewArray n) {
		Type expType = n.e.accept(this);
		if (expType != null) {
			if (!(expType instanceof IntegerType)) {
				System.out.print("Em NewArray a expressão que define o tamanho do array: ");
				n.e.accept(new PrettyPrintVisitor());
				System.out.println(" não é do tipo Int");
			} else
				return new IntArrayType();
		}
		return null;
	}

	// Identifier i;
	public Type visit(NewObject n) {
		Method aux = currMethod;
		currMethod = null;
		Type idType = n.i.accept(this);
		currMethod = aux;

		return idType;
	}

	// Exp e;
	public Type visit(Not n) {
		Type expType = n.e.accept(this);

		if (expType != null) {
			if (!(expType instanceof BooleanType)) {
				System.out.print("Em Not a expressão: ");
				n.e.accept(new PrettyPrintVisitor());
				System.out.println(" não é do tipo Boolean");
			} else
				return new BooleanType();
		}

		return null;
	}

	// String s;
	public Type visit(Identifier n) {
		if (this.fromVar) {
			return symbolTable.getVarType(currMethod, currClass, n.toString());
		} else {
			if (currClass != null) {
				if (currMethod == null) {
					if (!symbolTable.containsClass(n.toString())) {
						System.out.println("Símbolo " + n.toString() + " não pode ser encontrado");
						return null;
					}
				} else {
					if (symbolTable.getClass(currClass.getId()).parent() != null) {
						if (!symbolTable.getClass(symbolTable.getClass(currClass.getId()).parent()).containsMethod(n.toString()) &&
								!symbolTable.getClass(currClass.getId()).containsMethod(n.toString())) {
							System.out.println("Símbolo " + n.toString() + " não pode ser "
									+ "encontrado nem na classe filha " + currClass.getId() + ", nem na classe pai "
									+ symbolTable.getClass(currClass.getId()).parent());
							return null;
						}
					}
					if (!symbolTable.getClass(currClass.getId()).containsMethod(n.toString())) {
						System.out.println("Símbolo " + n.toString() + " não pode ser encontrado");
						return null;
					}
				}
			}
		}
		return new IdentifierType(n.toString());
	}

	private String getTypeName(Type t) {
		if (t != null) {
			if (t instanceof BooleanType)
				return "Boolean";
			else if (t instanceof IdentifierType)
				return ((IdentifierType) t).toString();
			else if (t instanceof IntArrayType)
				return "int []";
			else
				return "int";
		}
		return "null";
	}
}
