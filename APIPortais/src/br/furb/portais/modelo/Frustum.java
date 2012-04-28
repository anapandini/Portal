package br.furb.portais.modelo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import br.furb.portais.constantes.PortalAPI_Enums;
import br.furb.portais.util.PortalAPI_Utils;

/**
 * Trabalho de Conclusão de Curso II
 * Fundação Universidade Regional de Blumenau - FURB
 * Orientador: Dalton Solano dos Reis
 * Biblioteca de algoritmos de portais para a plataforma Android
 * 
 * @author Ana Paula Pandini
 */
public class Frustum {

	private Camera cameraFrustum;
	private float angulo;
	private float abertura;
	private float far;
	private Ponto frustumOrigin;
	private Ponto frustumRight;
	private Ponto frustumLeft;
	private FloatBuffer frustumCoords;
	private boolean frustumAuxiliar;

	/**
	 * Inicializa um frustum auxiliar, passando diretamente os pontos que definem o frustum
	 * 
	 * @param fo
	 * @param fe
	 * @param fd
	 */
	public Frustum(Ponto fo, Ponto fe, Ponto fd) {
		this.frustumOrigin = fo;
		this.frustumLeft = fe;
		this.frustumRight = fd;
		this.frustumAuxiliar = true;
		atualizarCoordenadas();
	}

	/**
	 * Inicializa o frustum principal e calcula as coordenadas dos pontos que definem o frutum com base no ângulo, far e abertura.
	 * 
	 * @param camera
	 * @param anguloInicial
	 * @param aberturaInicial
	 * @param farInicial
	 */
	public Frustum(Camera camera, float anguloInicial, float aberturaInicial, float farInicial) {
		this.cameraFrustum = camera;
		this.angulo = anguloInicial;
		this.abertura = aberturaInicial;
		this.far = farInicial;
		this.frustumAuxiliar = false;
		atualizarCoordenadas();
	}

	/**
	 * Atualiza as coordenadas do buffer. <br>
	 * Deve ser utilizado após alterar a posição do observador ou após alterar o ângulo do campo de visão.
	 */
	public void atualizarCoordenadas() {
		float tempCoords[] = null;
		if (frustumAuxiliar) {
			tempCoords = new float[] { frustumOrigin.getX(), frustumOrigin.getY(), frustumRight.getX(), frustumRight.getY(), frustumLeft.getX(), frustumLeft.getY() };
		} else {
			float x1 = PortalAPI_Utils.retornaX(cameraFrustum.getX(), angulo + abertura, far);
			float y1 = PortalAPI_Utils.retornaY(cameraFrustum.getY(), angulo + abertura, far);
			float x2 = PortalAPI_Utils.retornaX(cameraFrustum.getX(), angulo - abertura, far);
			float y2 = PortalAPI_Utils.retornaY(cameraFrustum.getY(), angulo - abertura, far);
			// Para os pontos
			frustumOrigin = new Ponto(cameraFrustum.getX(), cameraFrustum.getY(), null);
			frustumRight = new Ponto(x1, y1, null);
			frustumLeft = new Ponto(x2, y2, null);
			tempCoords = new float[] { cameraFrustum.getX(), cameraFrustum.getY(), x1, y1, x2, y2 };
		}

		ByteBuffer vbb = ByteBuffer.allocateDirect(tempCoords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		frustumCoords = vbb.asFloatBuffer();
		frustumCoords.put(tempCoords);
		frustumCoords.position(0);
	}

	/**
	 * Retorna o buffer de coordenadas do frustum.
	 * 
	 * @return
	 */
	public FloatBuffer getCoordenadas() {
		return frustumCoords;
	}

	/**
	 * Retorna o ponto de origem do frustum
	 * 
	 * @return
	 */
	public Ponto getFrustumOrigin() {
		return frustumOrigin;
	}

	/**
	 * Retorna o ponto direito do frustum
	 * 
	 * @return
	 */
	public Ponto getFrustumRight() {
		return frustumRight;
	}

	/**
	 * Retorna o ponto esquerdo do frustum
	 * 
	 * @return
	 */
	public Ponto getFrustumLeft() {
		return frustumLeft;
	}

	/**
	 * Retorna o ângulo do campo de visão do frustum
	 * 
	 * @return
	 */
	public float getAngulo() {
		return angulo;
	}

	/**
	 * Retorna a distância entre o observador e o ponto mais distante que ele consegue ver do seu campo de visão
	 * 
	 * @return
	 */
	public float getFar() {
		return far;
	}

	/**
	 * Altera o ângulo de visão do frustum conforme a direção passada e já atualiza as coordenadas do buffer após isso.
	 * 
	 * @param direcao
	 */
	public void mover(PortalAPI_Enums direcao) {
		if (direcao == PortalAPI_Enums.ROTACIONAR_FRUSTUM_HORARIO) {
			if (this.angulo > 0) {
				this.angulo -= 4;
			} else {
				this.angulo = 360;
			}
		} else {
			if (this.angulo <= 356) {
				this.angulo += 4;
			} else {
				this.angulo = 0;
			}
		}
		atualizarCoordenadas();
	}

}
