package br.ufpe.cin.if688.minijava.antlr;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import br.ufpe.cin.if688.minijava.antlr.impParser.ClassDeclExtendsContext;
import br.ufpe.cin.if688.minijava.antlr.impParser.ClassDeclSimpleContext;
import br.ufpe.cin.if688.minijava.antlr.impParser.ClassDeclarationContext;
import br.ufpe.cin.if688.minijava.antlr.impParser.ExpressionContext;
import br.ufpe.cin.if688.minijava.antlr.impParser.GoalContext;
import br.ufpe.cin.if688.minijava.antlr.impParser.IdentifierContext;
import br.ufpe.cin.if688.minijava.antlr.impParser.MainClassContext;
import br.ufpe.cin.if688.minijava.antlr.impParser.MethodDeclarationContext;
import br.ufpe.cin.if688.minijava.antlr.impParser.StatementContext;
import br.ufpe.cin.if688.minijava.antlr.impParser.TypeContext;
import br.ufpe.cin.if688.minijava.antlr.impParser.VarDeclarationContext;
import br.ufpe.cin.if688.minijava.ast.And;
import br.ufpe.cin.if688.minijava.ast.ArrayAssign;
import br.ufpe.cin.if688.minijava.ast.ArrayLength;
import br.ufpe.cin.if688.minijava.ast.ArrayLookup;
import br.ufpe.cin.if688.minijava.ast.Assign;
import br.ufpe.cin.if688.minijava.ast.Block;
import br.ufpe.cin.if688.minijava.ast.BooleanType;
import br.ufpe.cin.if688.minijava.ast.Call;
import br.ufpe.cin.if688.minijava.ast.ClassDecl;
import br.ufpe.cin.if688.minijava.ast.ClassDeclExtends;
import br.ufpe.cin.if688.minijava.ast.ClassDeclList;
import br.ufpe.cin.if688.minijava.ast.ClassDeclSimple;
import br.ufpe.cin.if688.minijava.ast.Exp;
import br.ufpe.cin.if688.minijava.ast.ExpList;
import br.ufpe.cin.if688.minijava.ast.False;
import br.ufpe.cin.if688.minijava.ast.Formal;
import br.ufpe.cin.if688.minijava.ast.FormalList;
import br.ufpe.cin.if688.minijava.ast.Identifier;
import br.ufpe.cin.if688.minijava.ast.IdentifierType;
import br.ufpe.cin.if688.minijava.ast.If;
import br.ufpe.cin.if688.minijava.ast.IntArrayType;
import br.ufpe.cin.if688.minijava.ast.IntegerLiteral;
import br.ufpe.cin.if688.minijava.ast.IntegerType;
import br.ufpe.cin.if688.minijava.ast.LessThan;
import br.ufpe.cin.if688.minijava.ast.MainClass;
import br.ufpe.cin.if688.minijava.ast.MethodDecl;
import br.ufpe.cin.if688.minijava.ast.MethodDeclList;
import br.ufpe.cin.if688.minijava.ast.Minus;
import br.ufpe.cin.if688.minijava.ast.NewArray;
import br.ufpe.cin.if688.minijava.ast.NewObject;
import br.ufpe.cin.if688.minijava.ast.Not;
import br.ufpe.cin.if688.minijava.ast.Plus;
import br.ufpe.cin.if688.minijava.ast.Print;
import br.ufpe.cin.if688.minijava.ast.Program;
import br.ufpe.cin.if688.minijava.ast.Statement;
import br.ufpe.cin.if688.minijava.ast.StatementList;
import br.ufpe.cin.if688.minijava.ast.This;
import br.ufpe.cin.if688.minijava.ast.Times;
import br.ufpe.cin.if688.minijava.ast.True;
import br.ufpe.cin.if688.minijava.ast.Type;
import br.ufpe.cin.if688.minijava.ast.VarDecl;
import br.ufpe.cin.if688.minijava.ast.VarDeclList;
import br.ufpe.cin.if688.minijava.ast.While;

public class impASTvVisitor implements impVisitor<Object> {

	@Override
	public Object visit(ParseTree arg0) {
		return arg0.accept(this);
	}

	@Override
	public Object visitChildren(RuleNode arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitErrorNode(ErrorNode arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitTerminal(TerminalNode arg0) {
		
		return null;
	}

	@Override
	public Object visitGoal(GoalContext ctx) {
		MainClass mainClass = (MainClass) ctx.mainClass().accept(this);
		ClassDeclList dc = new ClassDeclList();
		for (ClassDeclarationContext ct : ctx.classDeclaration()) {
			dc.addElement((ClassDecl) ct.accept(this));
		}
		return new Program(mainClass, dc);
	}

	@Override
	public Object visitMainClass(MainClassContext ctx) {
		Identifier id1 = (Identifier) ctx.identifier(0).accept(this);
		Identifier id2 = (Identifier) ctx.identifier(1).accept(this);
		Statement stm = (Statement) ctx.statement().accept(this);
		
		return new MainClass(id1, id2, stm);
	}

	@Override
	public Object visitClassDeclaration(ClassDeclarationContext ctx) {
		
		if(ctx.classDeclSimple() == null) {
			 return ctx.classDeclExtends().accept(this);
		}else {
			return ctx.classDeclSimple().accept(this);
		}
		
	}

	@Override
	public Object visitClassDeclSimple(ClassDeclSimpleContext ctx) {
		Identifier id = (Identifier) ctx.identifier().accept(this);
		VarDeclList dc = new VarDeclList();
		for (VarDeclarationContext v : ctx.varDeclaration()) {
			dc.addElement((VarDecl) v.accept(this));
		}	
		MethodDeclList ml = new MethodDeclList();
		for(MethodDeclarationContext d : ctx.methodDeclaration()) {
			ml.addElement((MethodDecl) d.accept(this)) ;
		}	
		return new ClassDeclSimple(id, dc, ml);
	}

	@Override
	public Object visitClassDeclExtends(ClassDeclExtendsContext ctx) {
		Identifier id =  (Identifier) ctx.identifier(0).accept(this);
		Identifier id2 =  (Identifier) ctx.identifier(1).accept(this);
		VarDeclList dc = new VarDeclList();
		for (VarDeclarationContext v : ctx.varDeclaration()) {
			dc.addElement((VarDecl) v.accept(this));
		}	
		MethodDeclList ml = new MethodDeclList();
		for(MethodDeclarationContext d : ctx.methodDeclaration()) {
			ml.addElement((MethodDecl) d.accept(this)) ;
		}	
		return new ClassDeclExtends(id,id2, dc, ml);
		
	}

	@Override
	public Object visitVarDeclaration(VarDeclarationContext ctx) {
		
		return new VarDecl((Type) ctx.type().accept(this), (Identifier) ctx.identifier().accept(this));
	}

	@Override
	public Object visitMethodDeclaration(MethodDeclarationContext ctx) {
		Identifier id = (Identifier) ctx.identifier(0).accept(this);
		Type tp = (Type) ctx.type(0).accept(this);
		
		FormalList fl = new FormalList();
		for(int i = 1;i < ctx.type().size(); i++) {
			Formal f = new Formal(((Type) ctx.type(i).accept(this)), (Identifier) ctx.identifier(i).accept(this));
			fl.addElement(f);
		}
		
		VarDeclList vl = new VarDeclList();
		for(VarDeclarationContext vd : ctx.varDeclaration()) {
			vl.addElement((VarDecl) vd.accept(this));
		}
		
		StatementList sl = new StatementList();
		for(StatementContext st : ctx.statement()) {
			sl.addElement((Statement) st.accept(this));
		}
		
		Exp ex = (Exp) ctx.expression().accept(this);
		
		return new MethodDecl(tp, id, fl, vl, sl, ex);
	}

	@Override
	public Object visitType(TypeContext ctx) {
		String type = ctx.getText();
		switch (type) {
		
		case "int":
			return new IntegerType();
		case "int[]":
			return  new IntArrayType();
		case "boolean":
			return  new BooleanType();
		default :
			return new IdentifierType(type);
		}
		
	}

	@Override
	public Object visitStatement(StatementContext ctx) {
		String start = ctx.getStart().getText();
		
		switch (start) {
		case "{":
			StatementList stl = new StatementList();
			for (StatementContext st : ctx.statement()) {
				stl.addElement((Statement)st.accept(this));
			}
			return new Block(stl);
		case "if":
			Exp e_if = (Exp)ctx.expression(0).accept(this);
			Statement stm1_if = (Statement) ctx.statement(0).accept(this);
			Statement stm2_if = (Statement) ctx.statement(1).accept(this);

			return new If(e_if,stm1_if,stm2_if);
		
		case "while":
			Exp e_w = (Exp)ctx.expression(0).accept(this);
			Statement stm_w = (Statement) ctx.statement(0).accept(this);
			
			return new While(e_w, stm_w);
			
		case "System.out.println":
			Exp e_syso = (Exp)ctx.expression(0).accept(this);
			
			return new Print(e_syso);
		//para identifier
		default:
			Statement stm;
			if(ctx.expression().size() == 1) {
				Identifier id = (Identifier) ctx.identifier().accept(this);
				Exp exp = (Exp) ctx.expression(0).accept(this);
				stm = new Assign(id,exp);
			}else {
				Identifier id = (Identifier) ctx.identifier().accept(this);
				Exp exp1 = (Exp) ctx.expression(0).accept(this);
				Exp exp2 = (Exp) ctx.expression(1).accept(this);
				
				stm = new ArrayAssign(id, exp1, exp2);
			}
			return stm;
		}
	}

	@Override
	public Object visitExpression(ExpressionContext ctx) {
		
		int expSize = ctx.expression().size();
		int childSize = ctx.getChildCount();
		String start = ctx.getStart().getText();
		
		
		if(childSize >= 5 && ctx.getChild(1).getText().equals(".")) {
			
			Exp exp = (Exp) ctx.expression(0).accept(this);
			Identifier id = (Identifier) ctx.identifier().accept(this);
			
			ExpList el = new ExpList();
			
			for(int i = 1; i < ctx.expression().size(); i++) {
				el.addElement((Exp) ctx.expression(i).accept(this));
			}

			return new Call(exp, id, el);
			
		}else if(expSize == 2) {
			
			Exp exp1 = (Exp) ctx.expression(0).accept(this);
			Exp exp2 = (Exp) ctx.expression(1).accept(this);
			
			String op = ctx.getChild(1).getText();
			
			if(childSize == 3) {
				switch(op) {
				case "&&":
					return new And(exp1, exp2);
				case "<":
					return new LessThan(exp1, exp2);
				case "+":
					return new Plus(exp1, exp2);
				case "-":
					return new Minus(exp1, exp2);
				default:
					return new Times(exp1, exp2);
				}
			} else {
				return new ArrayLookup(exp1, exp2);
			}
		}else if(expSize == 1) {
			Exp exp = (Exp) ctx.expression(0).accept(this);
			
			if(start.equals("!")) {
				return new Not(exp);
			} else if(start.equals("(")) {
				return (Exp) ctx.expression(0).accept(this);
			} else if(start.equals("new")) {
				return new NewArray(exp);
			} else {
				return new ArrayLength(exp);
			}
			
		}else if(start.equals("new")) {
			return new NewObject((Identifier) ctx.identifier().accept(this));
		} else if(start.equals("this")) {
			return new This();
		} else if(start.equals("true")) {
			return new True();
		} else if(start.equals("false")) {
			return new False();
		} else if(start.matches("\\d+")) {
			return new IntegerLiteral (Integer.parseInt(ctx.start.getText()));
		} else {
			return (Identifier) ctx.identifier().accept(this);
		}
	}

	@Override
	public Object visitIdentifier(IdentifierContext ctx) {
		return new Identifier(ctx.getText());
	}

}
