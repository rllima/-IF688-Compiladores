package br.ufpe.cin.if688.visitor;

import java.lang.invoke.SwitchPoint;

import br.ufpe.cin.if688.ast.AssignStm;
import br.ufpe.cin.if688.ast.CompoundStm;
import br.ufpe.cin.if688.ast.EseqExp;
import br.ufpe.cin.if688.ast.Exp;
import br.ufpe.cin.if688.ast.ExpList;
import br.ufpe.cin.if688.ast.IdExp;
import br.ufpe.cin.if688.ast.LastExpList;
import br.ufpe.cin.if688.ast.NumExp;
import br.ufpe.cin.if688.ast.OpExp;
import br.ufpe.cin.if688.ast.PairExpList;
import br.ufpe.cin.if688.ast.PrintStm;
import br.ufpe.cin.if688.ast.Stm;
import br.ufpe.cin.if688.symboltable.IntAndTable;
import br.ufpe.cin.if688.symboltable.Table;

public class IntAndTableVisitor implements IVisitor<IntAndTable> {
	private Table t;

	public IntAndTableVisitor(Table t) {
		this.t = t;
	}

	@Override
	public IntAndTable visit(Stm s) {
		
		return null;
	}

	@Override
	public IntAndTable visit(AssignStm s) {
	
		return null;
	}

	@Override
	public IntAndTable visit(CompoundStm s) {
		
		return null;
	}

	@Override
	public IntAndTable visit(PrintStm s) {
		
		return null;
	}

	@Override
	public IntAndTable visit(Exp e) {
		
		return e.accept(this);
	}

	@Override
	public IntAndTable visit(EseqExp e) {
		Interpreter aux1 = new Interpreter(this.t);
		Table aux = aux1.visit(e.getStm());
		this.t= aux;
		return e.getExp().accept(this);
	}
	
	public IntAndTable seachId(Table t, String aux) {
		while(t.tail != null && t.id != aux) {
			t = t.tail;
		}
		if(t.id == aux) return new IntAndTable(t.value, this.t);
		return null;
	}

	@Override
	public IntAndTable visit(IdExp e) {
		IntAndTable result = seachId(this.t, e.getId());
		return result;
	}
	

	@Override
	public IntAndTable visit(NumExp e) {
		return new IntAndTable(e.getNum(), this.t);
	}

	@Override
	public IntAndTable visit(OpExp e) {
		int result = 9;
		IntAndTable left = e.getLeft().accept(this);
		IntAndTable right = e.getRight().accept(this);
		switch (e.getOper()) {
			case OpExp.Plus: 
				result = left.result + right.result;
				break;
			case OpExp.Minus:
				result = left.result - right.result;
				break;
			case OpExp.Times:
				result = left.result * right.result;
				break;
			case OpExp.Div:
				result = left.result / right.result;
				break;
		}
		return new IntAndTable(result, right.table);
	}

	@Override
	public IntAndTable visit(ExpList el) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntAndTable visit(PairExpList el) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntAndTable visit(LastExpList el) {
		// TODO Auto-generated method stub
		return null;
	}


}
