package br.ufpe.cin.if688.parsing.analysis;

import java.util.*;

import org.hamcrest.core.Is;

import com.sun.javafx.image.impl.General;

import br.ufpe.cin.if688.parsing.grammar.*;


public final class SetGenerator {

	public static Map<Nonterminal, Set<GeneralSymbol>> getFirst(Grammar g) {

		if (g == null) throw new NullPointerException("g nao pode ser nula.");

		Map<Nonterminal, Set<GeneralSymbol>> first = initializeNonterminalMapping(g);
		/*
		 * Implemente aqui o m√©todo para retornar o conjunto first
		 */

		Collection <Production> productions = g.getProductions();
		Iterator<Production> it = productions.iterator();
		Production aux = null;
		Nonterminal nonTerminal = null;
		Set<GeneralSymbol> f_set = null;


		while(it.hasNext()) {
			aux = (Production) it.next();
			nonTerminal = aux.getNonterminal();
			f_set = getFirstSymbol (null, productions, nonTerminal);     // Lista do first do N„o Terminal
			first.get(nonTerminal).addAll(f_set);


		}
		return first;

	}

	public static Set <GeneralSymbol> getFirstSymbol (Set <GeneralSymbol> aux, Collection <Production> productions, GeneralSymbol symbol){
		Iterator<Production> it = productions.iterator(); 
		List <GeneralSymbol> prod_current = null;
		List<Production> prods_symbol = new ArrayList<Production>();
		while(it.hasNext()) {
			Production s = it.next();	
			if(s.getNonterminal() == symbol) {        //Pegando a lista de produÁıes para o Terminal
				prods_symbol.add(s);
			}
		}

		for (Production p : prods_symbol) {
			prod_current = p.getProduction();
			if (aux == null) { 
				aux = new HashSet<GeneralSymbol>();
			}
			for(int i = 0; i < prod_current.size(); i++) {

				if (prod_current.get(i) instanceof Terminal) {          //X È um terminal / First(x) = {x}
					aux.add(prod_current.get(i));
					break;
				}
				if(prod_current.contains(SpecialSymbol.EPSILON)) {    // X -> Vazio
					aux.add(SpecialSymbol.EPSILON);
					break;
				}
				else if (prod_current.get(i) instanceof Nonterminal) {           //Se X È um n„o-terminal
					Set<GeneralSymbol> aux1 = getFirstSymbol(aux, productions, prod_current.get(i));
					if(i == prod_current.size() -1 && aux1.contains(SpecialSymbol.EPSILON) ) {   
						aux1.add(SpecialSymbol.EPSILON);
					}
					if(!aux1.remove(SpecialSymbol.EPSILON)) {    
						break;
					}
					aux.addAll(aux1);
				}

			}
		}


		return aux; 
		
	}
	

	public static Map<Nonterminal, Set<GeneralSymbol>> getFollow(Grammar g, Map<Nonterminal, Set<GeneralSymbol>> first) {

		if (g == null || first == null)
			throw new NullPointerException();

		Map<Nonterminal, Set<GeneralSymbol>> follow = initializeNonterminalMapping(g);
		Map<Nonterminal, Set<GeneralSymbol>> follow_aux;

		/*
		 * implemente aqui o m√©todo para retornar o conjunto follow
		 */
		
		
		Collection <Production> productions = g.getProductions();
		GeneralSymbol start = g.getStartSymbol();
		GeneralSymbol current = null;
		GeneralSymbol current_next = null;
		Set<GeneralSymbol> set_symb;
		Set<GeneralSymbol> bi;
		follow.get(start).add(SpecialSymbol.EOF);
		boolean change = true;
		
		while(change) {
			follow_aux = new HashMap<Nonterminal, Set<GeneralSymbol>>();
			for(Nonterminal nt:follow.keySet()) {
				set_symb = new HashSet<GeneralSymbol>();
				set_symb.addAll(follow.get(nt));
				follow_aux.put(nt, set_symb);
			}
			//baseado no algoritmo do livro  ENGINEERING A COMPILER 
			for (Production p : productions)  {
				set_symb = new HashSet<GeneralSymbol>();
				set_symb.addAll(follow.get(p.getNonterminal()));
				for(int i = p.getProduction().size()-1; i>=0;i-- ) {
					if(p.getProduction().get(i) instanceof Nonterminal) {
						follow.get(p.getProduction().get(i)).addAll(set_symb);
						if(first.get(p.getProduction().get(i)).contains(SpecialSymbol.EPSILON)) {
							bi = new HashSet<GeneralSymbol>();
							bi.addAll(first.get(p.getProduction().get(i)));
							bi.remove(SpecialSymbol.EPSILON);
							set_symb.addAll(bi);
						}else {
							set_symb = new HashSet<GeneralSymbol>();
							set_symb.addAll(first.get(p.getProduction().get(i)));
						}
					}else{
						set_symb = new HashSet<GeneralSymbol>();
						set_symb.add(p.getProduction().get(i));
						set_symb.addAll(set_symb);
					}
				
				}
				
				change = !follow.equals(follow_aux);
			
			}
			
		}
	
		

		return follow;
	}
	

	//m√©todo para inicializar mapeamento n√£oterminais -> conjunto de s√≠mbolos
	private static Map<Nonterminal, Set<GeneralSymbol>>
	initializeNonterminalMapping(Grammar g) {
		Map<Nonterminal, Set<GeneralSymbol>> result = 
				new HashMap<Nonterminal, Set<GeneralSymbol>>();

		for (Nonterminal nt: g.getNonterminals())
			result.put(nt, new HashSet<GeneralSymbol>());

		return result;
	}

} 
