package br.furb.portais.modelo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Trabalho de Conclusão de Curso II
 * Fundação Universidade Regional de Blumenau - FURB
 * Orientador: Dalton Solano dos Reis
 * Biblioteca de algoritmos de portais para a plataforma Android
 * 
 * @author Ana Paula Pandini
 */
public class Divisao {

	private Ponto origem;
	private Ponto destino;
	private TipoDivisao tipo;
	private FloatBuffer coordenadasDivisao;

	/**
	 * Atributo <code>salaOrigem</code> utilizado apenas quando a Divisão é do tipo <code>TipoDivisao.PORTAL</code>.
	 */
	private Sala salaOrigem;
	private Sala salaDestino;

	/**
	 * Inicializa a divisão, informando já o seu tipo.
	 * 
	 * @param origem
	 * @param destino
	 * @param tipo
	 */
	public Divisao(Ponto origem, Ponto destino, TipoDivisao tipo) {
		this.origem = origem;
		this.destino = destino;
		this.tipo = tipo;
	}

	/**
	 * Retorna as coordenadas para desenho da divisão.
	 * 
	 * @return
	 */
	public FloatBuffer getBufferCoordenadas() {
		if (coordenadasDivisao == null) {
			float divCoords[] = new float[] { getOrigem().getX(), getOrigem().getY(), 0, getDestino().getX(), getDestino().getY(), 0 };
			ByteBuffer vbb = ByteBuffer.allocateDirect(divCoords.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			coordenadasDivisao = vbb.asFloatBuffer();
			coordenadasDivisao.put(divCoords);
			coordenadasDivisao.position(0);
		}
		return coordenadasDivisao;
	}

	/**
	 * Retorna um dos pontos da divisão.
	 * 
	 * @return
	 */
	public Ponto getOrigem() {
		return origem;
	}

	/**
	 * Retorna o ponto de destino da divisão.
	 * 
	 * @return
	 */
	public Ponto getDestino() {
		return destino;
	}

	/**
	 * Retorna o tipo da divisão: portal / parede.
	 * 
	 * @return
	 */
	public TipoDivisao getTipo() {
		return tipo;
	}

	/**
	 * Retorna a sala de origem da divisão caso ela for do tipo <code>TipoDivisao.PORTAL</code>.
	 * 
	 * @return
	 */
	public Sala getSalaOrigem() {
		return salaOrigem;
	}

	/**
	 * Grava a sala de origem da divisão quando ela é do tipo <code>TipoDivisao.PORTAL</code>.
	 * 
	 * @param salaOrigem
	 */
	public void setSalaOrigem(Sala salaOrigem) {
		this.salaOrigem = salaOrigem;
	}

	/**
	 * Retorna a sala de destino da divisão caso ela for do tipo <code>TipoDivisao.PORTAL</code>.
	 * 
	 * @return
	 */
	public Sala getSalaDestino() {
		return salaDestino;
	}

	/**
	 * Grava a sala de destino da divisão quando ela é do tipo <code>TipoDivisao.PORTAL</code>.
	 * 
	 * @param salaDestino
	 */
	public void setSalaDestino(Sala salaDestino) {
		this.salaDestino = salaDestino;
	}

}
