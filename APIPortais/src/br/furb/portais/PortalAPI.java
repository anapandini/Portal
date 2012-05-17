package br.furb.portais;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.furb.portais.modelo.Camera;
import br.furb.portais.modelo.Divisao;
import br.furb.portais.modelo.Frustum;
import br.furb.portais.modelo.Ponto;
import br.furb.portais.modelo.PontoInteresse;
import br.furb.portais.modelo.Sala;
import br.furb.portais.modelo.TipoDivisao;
import br.furb.portais.util.PortalAPI_Utils;

/**
 * Trabalho de Conclus�o de Curso II
 * Funda��o Universidade Regional de Blumenau - FURB
 * Orientador: Dalton Solano dos Reis
 * Biblioteca de algoritmos de portais para a plataforma Android
 * 
 * @author Ana Paula Pandini
 */
public class PortalAPI {

	/**
	 * Identifica os pontos de interesse que est�o no campo de vis�o do observador. <br>
	 * Inicia testando apenas os pontos que est�o na mesma sala em que o observador est�. Ap�s isso,
	 * detecta se dentro do campo de vis�o h� algum portal. Se houver, explora o trecho sala que pode ser visto por esse portal.
	 * E faz essas verifica��es recursivamente por todos os portais de todas as salas que s�o alcan�adas pelo campo de vis�o original do observador.
	 * 
	 * @param fo
	 * @param fd
	 * @param fe
	 * @param salaCorrente
	 * @param pontosInteresse
	 * @param camera
	 * @param idSalasVisitadas
	 * @param frustumsAuxiliares
	 */
	private void visao(Ponto fo, Ponto fd, Ponto fe, Sala salaCorrente, List<PontoInteresse> pontosInteresse, Camera camera, List<Integer> idSalasVisitadas, List<Frustum> frustumsAuxiliares) {

		// Verifica se j� visitou a sala atual do observador
		if (!idSalasVisitadas.contains(salaCorrente.getIdentificadorSala())) {

			// Grava a sala que est� sendo visitada
			idSalasVisitadas.add(salaCorrente.getIdentificadorSala());

			// Para todos os pontos de interesse verifica os que est�o no campo de vis�o
			for (PontoInteresse wp : pontosInteresse) {
				if (wp.getSala().getIdentificadorSala() == salaCorrente.getIdentificadorSala()) {
					if (PortalAPI_Utils.pontoNoTrianguloMatrizDalton(fo, fd, fe, wp)) {
						// Salva no observador todos os pontos de interesse que ele v�
						camera.adicionaPontoVisto(wp);
					}
				}
			}

			// Trecho do c�digo que � respons�vel por definir as coordenadas de um frustum auxiliar caso o campo de vis�o principal esteja vendo um portal
			Ponto novoFd = null;
			Ponto novoFe = null;
			// Para todos os portais que a sala do observador possui...
			for (Divisao div : salaCorrente.getPortais()) {
				// ... � verificado se algum dos lados do frustum intercepta
				// significando que ent�o o campo de vis�o v� at� a outra sala
				if (PortalAPI_Utils.intercepta(fo, fe, div.getDestino(), div.getOrigem())) {
					novoFe = fe;
					if (PortalAPI_Utils.pontoNoTrianguloMatrizDalton(fo, fe, fd, div.getDestino())) {
						novoFd = PortalAPI_Utils.getInterseccaoRetas(fo, div.getDestino(), fe, fd);
					} else if (PortalAPI_Utils.pontoNoTrianguloMatrizDalton(fo, fe, fd, div.getOrigem())) {
						novoFd = PortalAPI_Utils.getInterseccaoRetas(fo, div.getOrigem(), fe, fd);
					} else {
						novoFd = fd;
					}
					// Esta verifica��o � feita para os dois lados do frustum
					// e dessa forma � poss�vel descobrir as novas coordenadas de um
					// frustum auxiliar
				} else if (PortalAPI_Utils.intercepta(fo, fd, div.getDestino(), div.getOrigem())) {
					novoFd = fd;
					if (PortalAPI_Utils.pontoNoTrianguloMatrizDalton(fo, fe, fd, div.getDestino())) {
						novoFe = PortalAPI_Utils.getInterseccaoRetas(fo, div.getDestino(), fe, fd);
					} else if (PortalAPI_Utils.pontoNoTrianguloMatrizDalton(fo, fe, fd, div.getOrigem())) {
						novoFe = PortalAPI_Utils.getInterseccaoRetas(fo, div.getOrigem(), fe, fd);
					} else {
						novoFe = fe;
					}
				}

				// Se tiver as coordenadas preenchidas, significa que tem um novo frustum para a pr�xima sala
				if (novoFe != null && novoFd != null) {
					frustumsAuxiliares.add(new Frustum(fo, novoFe, novoFd));
					visao(fo, novoFd, novoFe, retornaProximaSala(salaCorrente, div), pontosInteresse, camera, idSalasVisitadas, frustumsAuxiliares);
				}
			}
		}
	}

	/**
	 * Retorna a sala que pode ser vista pelo portal passado por par�metro.
	 * 
	 * @param salaCorrente
	 * @param div
	 * @return
	 */
	private Sala retornaProximaSala(Sala salaCorrente, Divisao div) {
		if (salaCorrente == div.getSalaDestino()) {
			return div.getSalaOrigem();
		}
		return div.getSalaDestino();
	}

	/**
	 * @see br.furb.portal.api.PortalAPI#visao(br.furb.portal.api.model.Ponto, br.furb.portal.api.model.Ponto, br.furb.portal.api.model.Ponto, br.furb.portal.api.model.Sala, java.util.List, br.furb.portal.api.model.Camera, java.util.List, java.util.List)
	 */
	public List<Frustum> visaoCamera(List<PontoInteresse> pontosInteresse, Map<Integer, Sala> salas, Camera camera, Frustum frustum) {
		long inicio = System.currentTimeMillis();

		// Cria uma lista para guardar campos de vis�o auxiliares, que podem ser utilizados
		// para representar graficamente o que est� sendo visto por cada portal a partir do observador
		List<Frustum> frustumsAuxiliares = new ArrayList<Frustum>();

		// Chama m�todo privado respons�vel por executar a atividade
		visao(frustum.getFrustumOrigin(), frustum.getFrustumRight(), frustum.getFrustumLeft(), camera.getSala(), pontosInteresse, camera, new ArrayList<Integer>(), frustumsAuxiliares);

		long duracao = System.currentTimeMillis() - inicio;
		PortalAPI_Utils.gravarLog("visaoCamera", duracao);
		return frustumsAuxiliares;
	}

	/**
	 * /**
	 * Calcula as novas coordenadas do observador, verificando se ele pode movimentar-se ou n�o. <br>
	 * Quando houver uma parede no caminho, este m�todo ir� detectar e n�o ir� atualizar as coordenadas do observador, para que ele n�o se mova. <br>
	 * Caso contr�rio, ir� movimentar o observador, verificar se ele trocou de sala e tamb�m identificar os pontos de interesse que est�o
	 * no campo de vis�o da nova posi��o.
	 * 
	 * @param camera
	 * @param novoXCamera
	 * @param novoYCamera
	 * @param pontosInteresse
	 * @param salas
	 * @param frustum
	 */
	public void moverCamera(Camera camera, float novoXCamera, float novoYCamera, List<PontoInteresse> pontosInteresse, Map<Integer, Sala> salas, Frustum frustum) {
		long inicio = System.currentTimeMillis();

		boolean podeMover = true;
		// Recupera as divis�es que formam a sala do observador
		for (Divisao div : camera.getSala().getDivisoes()) {

			if (PortalAPI_Utils.intercepta(camera, new Ponto(novoXCamera, novoYCamera, null), div.getOrigem(), div.getDestino())) {
				// Verifica se o observador est� trocando de sala
				if (div.getTipo() == TipoDivisao.PORTAL) {
					if (div.getSalaOrigem().getIdentificadorSala() == camera.getSala().getIdentificadorSala()) {
						camera.setSala(div.getSalaDestino());
					} else {
						camera.setSala(div.getSalaOrigem());
					}
					// Ou se ele est� de frente para uma parede
				} else {
					podeMover = false;
				}
				break;
			}
		}
		if (podeMover) {
			// Grava novas coordenadas
			camera.setX(novoXCamera);
			camera.setY(novoYCamera);
			frustum.atualizarCoordenadas();
		}

		long duracao = System.currentTimeMillis() - inicio;
		PortalAPI_Utils.gravarLog("moverCamera", duracao);
	}

	public void desligaLogTempos() {
		PortalAPI_Utils.setLogTempos(false);
	}

	public void ligaLogTempos() {
		PortalAPI_Utils.setLogTempos(true);
	}

}
