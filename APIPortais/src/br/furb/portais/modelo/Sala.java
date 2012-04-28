package br.furb.portais.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Trabalho de Conclus�o de Curso II
 * Funda��o Universidade Regional de Blumenau - FURB
 * Orientador: Dalton Solano dos Reis
 * Biblioteca de algoritmos de portais para a plataforma Android
 * 
 * @author Ana Paula Pandini
 */
public class Sala {

	private List<Divisao> divisoes;
	private int identificadorSala;
	private List<Divisao> portaisDaSala;

	public Sala(int identificadorSala) {
		this.divisoes = new ArrayList<Divisao>();
		this.identificadorSala = identificadorSala;
	}

	/**
	 * Retorna as divis�es da sala
	 * 
	 * @return
	 */
	public List<Divisao> getDivisoes() {
		return divisoes;
	}

	/**
	 * Retorna os identificadores da sala
	 * 
	 * @return
	 */
	public int getIdentificadorSala() {
		return identificadorSala;
	}

	/**
	 * Adiciona uma divis�o para esta sala
	 * 
	 * @param divisao
	 */
	public void addDivisao(Divisao divisao) {
		this.divisoes.add(divisao);
	}

	/**
	 * Retorna os portais que esta sala possui
	 * 
	 * @return Retorna as divis�es que s�o do tipo <code>TipoDivisao.PORTAL</code>
	 */
	public List<Divisao> getPortais() {
		if (portaisDaSala == null) {
			portaisDaSala = new ArrayList<Divisao>();
			for (Divisao div : divisoes) {
				if (div.getTipo() == TipoDivisao.PORTAL) {
					portaisDaSala.add(div);
				}
			}
		}
		return portaisDaSala;
	}

}
