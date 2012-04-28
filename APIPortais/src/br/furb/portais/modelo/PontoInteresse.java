package br.furb.portais.modelo;


/**
 * Trabalho de Conclusão de Curso II
 * Fundação Universidade Regional de Blumenau - FURB
 * Orientador: Dalton Solano dos Reis
 * Biblioteca de algoritmos de portais para a plataforma Android
 * 
 * @author Ana Paula Pandini
 */
public class PontoInteresse extends Ponto {

	/**
	 * Inicializa o ponto de interesse com as coordenadas (x,y) e a sala que ele está localizado
	 * 
	 * @param x
	 * @param y
	 * @param gl
	 * @param sala
	 */
	public PontoInteresse(float x, float y, Sala sala) {
		super(x, y, sala);
	}

}
