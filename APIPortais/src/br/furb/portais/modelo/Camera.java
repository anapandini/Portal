package br.furb.portais.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Trabalho de Conclusão de Curso II
 * Fundação Universidade Regional de Blumenau - FURB
 * Orientador: Dalton Solano dos Reis
 * Biblioteca de algoritmos de portais para a plataforma Android
 * 
 * @author Ana Paula Pandini
 */
public class Camera extends Ponto {

	private List<PontoInteresse> pontosVistos;

	/**
	 * Inicializa o observador com as coordenadas (x,y) e também a sala em que ele está localizado.
	 * 
	 * @param x
	 * @param y
	 * @param gl
	 * @param sala
	 */
	public Camera(float x, float y, Sala sala) {
		super(x, y, sala);
		this.pontosVistos = new ArrayList<PontoInteresse>();
	}

	/**
	 * Remove da memória do observador os pontos de interesse que estavam no campo de visão dele.
	 */
	public void limpaPontosVistos() {
		this.pontosVistos.clear();
	}

	/**
	 * Armazena um ponto de interesse que foi visto pelo campo de visão do observador.
	 * 
	 * @param wp
	 */
	public void adicionaPontoVisto(PontoInteresse wp) {
		this.pontosVistos.add(wp);
	}

	/**
	 * Retorna uma lista dos pontos de interesse que foram
	 * 
	 * @return
	 */
	public List<PontoInteresse> getPontosVistos() {
		return this.pontosVistos;
	}

	/**
	 * Grava a sala em que o observador está localizado.
	 * 
	 * @param novaSala
	 */
	public void setSala(Sala novaSala) {
		this.sala = novaSala;
	}

}