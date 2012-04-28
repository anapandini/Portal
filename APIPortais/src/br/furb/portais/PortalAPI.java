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
 * Trabalho de Conclusão de Curso II
 * Fundação Universidade Regional de Blumenau - FURB
 * Orientador: Dalton Solano dos Reis
 * Biblioteca de algoritmos de portais para a plataforma Android
 * 
 * @author Ana Paula Pandini
 */
public class PortalAPI {

	/**
	 * Identifica os pontos de interesse que estão no campo de visão do observador. <br>
	 * Inicia testando apenas os pontos que estão na mesma sala em que o observador está. Após isso,
	 * detecta se dentro do campo de visão há algum portal. Se houver, explora o trecho sala que pode ser visto por esse portal.
	 * E faz essas verificações recursivamente por todos os portais de todas as salas que são alcançadas pelo campo de visão original do observador.
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
		if (!idSalasVisitadas.contains(salaCorrente.getIdentificadorSala())) {

			idSalasVisitadas.add(salaCorrente.getIdentificadorSala());

			for (PontoInteresse wp : pontosInteresse) {
				if (wp.getSala().getIdentificadorSala() == salaCorrente.getIdentificadorSala()) {
					if (PortalAPI_Utils.pontoNoTrianguloMatrizDalton(fo, fd, fe, wp)) {
						camera.adicionaPontoVisto(wp);
					}
				}
			}

			Ponto novoFd = null;
			Ponto novoFe = null;
			for (Divisao div : salaCorrente.getPortais()) {
				if (PortalAPI_Utils.intercepta(fo, fe, div.getDestino(), div.getOrigem())) {
					novoFe = fe;
					if (PortalAPI_Utils.pontoNoTrianguloMatrizDalton(fo, fe, fd, div.getDestino())) {
						novoFd = PortalAPI_Utils.getInterseccaoRetas(fo, div.getDestino(), fe, fd);
					} else if (PortalAPI_Utils.pontoNoTrianguloMatrizDalton(fo, fe, fd, div.getOrigem())) {
						novoFd = PortalAPI_Utils.getInterseccaoRetas(fo, div.getOrigem(), fe, fd);
					} else {
						novoFd = fd;
					}
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

				// Se tiver as coordenadas preenchidas, significa que tem um novo frustum para a próxima sala
				if (novoFe != null && novoFd != null) {
					frustumsAuxiliares.add(new Frustum(fo, novoFe, novoFd));
					visao(fo, novoFd, novoFe, retornaProximaSala(salaCorrente, div), pontosInteresse, camera, idSalasVisitadas, frustumsAuxiliares);
				}
			}
		}
	}

	/**
	 * Retorna a sala que pode ser vista pelo portal passado por parâmetro.
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
		List<Frustum> frustumsAuxiliares = new ArrayList<Frustum>();
		visao(frustum.getFrustumOrigin(), frustum.getFrustumRight(), frustum.getFrustumLeft(), camera.getSala(), pontosInteresse, camera, new ArrayList<Integer>(), frustumsAuxiliares);
		return frustumsAuxiliares;
	}

	/**
	 * /**
	 * Calcula as novas coordenadas do observador, verificando se ele pode movimentar-se ou não. <br>
	 * Quando houver uma parede no caminho, este método irá detectar e não irá atualizar as coordenadas do observador, para que ele não se mova. <br>
	 * Caso contrário, irá movimentar o observador, verificar se ele trocou de sala e também identificar os pontos de interesse que estão
	 * no campo de visão da nova posição.
	 * 
	 * @param camera
	 * @param novoXCamera
	 * @param novoYCamera
	 * @param pontosInteresse
	 * @param salas
	 * @param frustum
	 */
	public void moverCamera(Camera camera, float novoXCamera, float novoYCamera, List<PontoInteresse> pontosInteresse, Map<Integer, Sala> salas, Frustum frustum) {
		boolean podeMover = true;
		for (Divisao div : camera.getSala().getDivisoes()) {

			if (PortalAPI_Utils.intercepta(camera, new Ponto(novoXCamera, novoYCamera, null), div.getOrigem(), div.getDestino())) {
				if (div.getTipo() == TipoDivisao.PORTAL) {
					if (div.getSalaOrigem().getIdentificadorSala() == camera.getSala().getIdentificadorSala()) {
						camera.setSala(div.getSalaDestino());
					} else {
						camera.setSala(div.getSalaOrigem());
					}
					// Log.d("tcc", String.valueOf(camera.getSala().getIdentificadorSala())); // TODO será que removo essa linha?
				} else {
					podeMover = false;
				}
				break;
			}
		}
		if (podeMover) {
			camera.setX(novoXCamera);
			camera.setY(novoYCamera);
			frustum.atualizarCoordenadas();
		}
	}

}
