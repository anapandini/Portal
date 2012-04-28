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
public class Ponto {

	protected Sala sala;
	private FloatBuffer coordedanasPonto;
	private float x;
	private float y;

	/**
	 * Inicializa o ponto com as coordenadas (x,y) e a sala em que ele está localizado
	 * 
	 * @param x
	 * @param y
	 * @param sala
	 */
	public Ponto(float x, float y, Sala sala) {
		this.x = x;
		this.y = y;
		this.sala = sala;
		atualizarCoordenadas();
	}

	/**
	 * Atualiza o buffer de coordenadas do ponto, para usar na renderização
	 */
	public void atualizarCoordenadas() {
		float[] pointCoords = new float[] { x, y, 0 };

		ByteBuffer vbb = ByteBuffer.allocateDirect(pointCoords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		coordedanasPonto = vbb.asFloatBuffer();
		coordedanasPonto.mark();
		coordedanasPonto.put(pointCoords);
		coordedanasPonto.position(0);
	}

	/**
	 * Grava a coordenada do eixo X do ponto e atualiza o buffer de coordenadas.
	 * 
	 * @param x
	 */
	public void setX(float x) {
		this.x = x;
		atualizarCoordenadas();
	}

	/**
	 * Retorna a coordenada do eixo X do ponto.
	 * 
	 * @return
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * Grava a coordenada do eixo Y do ponto e atualiza o buffer de coordenadas.
	 * 
	 * @param y
	 */
	public void setY(float y) {
		this.y = y;
		atualizarCoordenadas();
	}

	/**
	 * Retorna a coordenada do eixo Y do ponto.
	 * 
	 * @return
	 */
	public float getY() {
		return this.y;
	}

	/**
	 * Retorna a sala em que o ponto está localizado
	 * 
	 * @return
	 */
	public Sala getSala() {
		return this.sala;
	}

	/**
	 * Retorna o buffer de coordenadas do ponto para renderização
	 * 
	 * @return
	 */
	public FloatBuffer getCoords() {
		return this.coordedanasPonto;
	}
}
