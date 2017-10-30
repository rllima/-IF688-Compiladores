package br.ufpe.cin.if688.visitor;

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
import br.ufpe.cin.if688.symboltable.Table;

public class Interpreter implements IVisitor<Table> {
	
	//a=8;b=80;a=7;
	// a->7 ==> b->80 ==> a->8 ==> NIL
	private Table t;
	
	public Interpreter(Table t) {
		this.t = t;
	}

	@Override
	public Table visit(Stm s) {
		
		return s.accept(this);
	}

	@Override
	public Table visit(AssignStm s) {
		
		Table table = s.getExp().accept(this);
		table.id = s.getId();
		this.t = table;
		return this.t;
	}

	@Override
	public Table visit(CompoundStm s) {
		s.getStm1().accept(this);
		s.getStm2().accept(this);
		return this.t;
	}

	@Override
	public Table visit(PrintStm s) {
		s.getExps().accept(this);
		return this.t;
	}

	@Override
	public Table visit(Exp e) {
		
		return e.accept(this);
	}

	@Override
	public Table visit(EseqExp e) {
		IntAndTableVisitor aux = new IntAndTableVisitor(this.t);
		int aux2 = aux.visit(e).result;
		Table aux3 = new Table("Rodrigo", aux2, this.t);
		return aux3;
	}

	@Override
	public Table visit(IdExp e) {
		IntAndTableVisitor aux = new IntAndTableVisitor(this.t);
		int aux2 = aux.visit(e).result;
		Table aux3 = new Table("Rodrigo", aux2, this.t);
		return aux3;
	}

	@Override
	public Table visit(NumExp e) {
		IntAndTableVisitor aux = new IntAndTableVisitor(this.t);
		int aux2 = aux.visit(e).result;
		Table aux3 = new Table("Rodrigo", aux2, this.t);
		return aux3;
	}

	@Override
	public Table visit(OpExp e) {
		IntAndTableVisitor aux = new IntAndTableVisitor(this.t);
		int aux2 = aux.visit(e).result;
		Table aux3  = new Table("Rodrigo", aux2, this.t);	
		return aux3;
	}

	@Override
	public Table visit(ExpList el) {
		
		return el.accept(this);
	}

	@Override
	public Table visit(PairExpList el) {
		int aux = el.getHead().accept(this).value;
		System.out.print(aux + " " );
		return el.getTail().accept(this);
	}

	@Override
	public Table visit(LastExpList el) {
		Table  aux = el.getHead().accept(this);
		System.out.print(aux.value + " ");
		return aux ;
	}


}
