package br.furb.portais.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.furb.portais.modelo.Ponto;

/**
 * Trabalho de Conclus�o de Curso II
 * Funda��o Universidade Regional de Blumenau - FURB
 * Orientador: Dalton Solano dos Reis
 * Biblioteca de algoritmos de portais para a plataforma Android
 * 
 * Muitos dos conceitos desta classe foram estudados em: http://www2.inatel.br/docentes/rosanna/cursos/C421-C_20072/AG2.pdf
 * 
 * @author Ana Paula Pandini
 */
public class PortalAPI_Utils {

	private static boolean logTempos = false;

	private static FileWriter fw;

	/**
	 * Verifica se os dois segmentos de reta se intersectam.
	 * O segmento de reta 1 � definido pelos pontos <code>a</code> e <code>b</code>,
	 * e o segmento de reta 2 � definido pelos pontos <code>c</code> e <code>d</code>
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return Retorna verdadeiro quando os dois segmentos de reta se intersectam. Caso contr�rio, retorna falso.
	 */
	public static boolean intercepta(Ponto a, Ponto b, Ponto c, Ponto d) {
		long inicio = System.currentTimeMillis();
		boolean intercepta = se_encontram(a, b, c, d) || se_tocam(a, b, c, d) || se_intercepta(a, b, c, d);
		long duracao = System.currentTimeMillis() - inicio;
		gravarLog("intercepta", duracao);
		return intercepta;
	}

	/**
	 * Determina se dois segmentos de retas (segmentos AB e CD) se tocam.
	 * Isto acontece quando um ponto extremo de um dos segmentos � imediatamente na sequ�ncia de outro ponto extremo do outro segmento.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private static boolean se_tocam(Ponto a, Ponto b, Ponto c, Ponto d) {
		if (alinhados(a, b, c, d) || superpostos(a, b, c, d)) {
			return false;
		}
		return (em(c, a, b) || em(d, a, b) || em(a, c, d) || em(b, c, d));
	}

	/**
	 * Indica se os segmentos AB e CD est�o alinhados e possuem um trecho em comum.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private static boolean superpostos(Ponto a, Ponto b, Ponto c, Ponto d) {
		if ((lado(a, b, c) == 0) && (lado(a, b, d) == 0)) {
			return (em(c, a, b) || em(d, a, b) || em(a, c, d) || em(b, c, d));
		}
		return false;
	}

	/**
	 * Indica se o ponto P � coincide com um dos pontos extremos do segmento AB
	 * 
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	private static boolean extremo(Ponto p, Ponto a, Ponto b) {
		return (sobre(p, a) || sobre(p, b));
	}

	/**
	 * Indica se o ponto A � coincidente ao ponto B
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private static boolean sobre(Ponto a, Ponto b) {
		return ((b.getX() == a.getX()) || (b.getY() == a.getY()));
	}

	/**
	 * Indica se o ponto P est� compreendido no segmento AB, mas n�o coincide com os pontos A e B
	 * 
	 * @param p
	 * @param a
	 * @param b
	 * @return
	 */
	private static boolean em(Ponto p, Ponto a, Ponto b) {
		if ((lado(a, b, p) == 0) && (!extremo(p, a, b))) {
			if (a.getX() != b.getX()) {
				return (((a.getX() < p.getX()) && (p.getX() < b.getX())) || ((a.getX() > p.getX()) && (p.getX() > b.getX())));
			}
			return (((a.getY() < p.getY()) && (p.getY() < b.getY())) || ((a.getY() > p.getY()) && (p.getY() > b.getY())));
		}
		return false;
	}

	/**
	 * Indica se os segmentos AB e CD est�o alinhados, mas sem verificar se possuem um trecho em comum.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private static boolean alinhados(Ponto a, Ponto b, Ponto c, Ponto d) {
		if ((lado(a, b, c) == 0) && (lado(a, b, d) == 0)) {
			return (!em(c, a, b) && !em(d, a, b) && !em(a, c, d) && !em(b, c, d));
		}
		return false;
	}

	/**
	 * Indica se os segmentos de retas AB e CD se encontram, ou seja, se possuem um ponto extremo coincidente.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private static boolean se_encontram(Ponto a, Ponto b, Ponto c, Ponto d) {
		if (iguais(a, b, c, d)) {
			return false;
		}
		return ((sobre(a, c) && !em(d, a, b) && !em(b, c, d)) || /**/
		(sobre(a, d) && !em(c, a, b) && !em(b, c, d)) || /**/
		(sobre(b, c) && !em(d, a, b) && !em(a, c, d)) || /**/
		(sobre(b, d) && !em(c, a, b) && !em(a, c, d)));
	}

	/**
	 * Retorna verdadeiro quando os segmentos de reta AB e CD s�o iguais (coincidentes).
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private static boolean iguais(Ponto a, Ponto b, Ponto c, Ponto d) {
		return ((sobre(a, c) && sobre(b, d)) || (sobre(a, d) && sobre(b, c)));
	}

	/**
	 * Indica se as retas AB e CD se interceptam.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private static boolean intercerpta_reta(Ponto a, Ponto b, Ponto c, Ponto d) {
		// Fonte: http://www2.inatel.br/docentes/rosanna/cursos/C421-C_20072/AG2.pdf
		double x1, x2, x3, x4, y1, y2, y3, y4;
		x1 = Math.min(a.getX(), b.getX());
		x2 = Math.max(a.getX(), b.getX());
		y1 = Math.min(a.getY(), b.getY());
		y2 = Math.max(a.getY(), b.getY());

		x3 = Math.min(c.getX(), d.getX());
		x4 = Math.max(c.getX(), d.getX());
		y3 = Math.min(c.getY(), d.getY());
		y4 = Math.max(c.getY(), d.getY());

		return ((x2 >= x3) && (x4 >= x1) && (y2 >= y3) && (y4 >= y1));
	}

	/**
	 * Verifica se os segmentos AB e CD se intersectam.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private static boolean se_intercepta(Ponto a, Ponto b, Ponto c, Ponto d) {
		if (!intercerpta_reta(a, b, c, d)) {
			return false;
		}
		double abc = lado(a, b, c);
		double abd = lado(a, b, d);
		double cda = lado(c, d, a);
		double cdb = lado(c, d, b);
		return ((abc * abd) < 0) && ((cda * cdb) < 0);
	}

	/**
	 * Retorna a posi��o do ponto C em rela��o ao segmento AB.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return Retorna -1 quando ponto C est� a direita <br>
	 *         Retorna 1 quando est� a esquerda <br>
	 *         E retorna 0 quando est� alinhado.
	 */
	private static double lado(Ponto a, Ponto b, Ponto c) {
		float s = a.getX() * b.getY() - a.getY() * b.getX() + a.getY() * c.getX() - a.getX() * c.getY() + b.getX() * c.getY() - b.getY() * c.getX();
		if (s < 0) {
			return -1;
		} else if (s > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	// TODO este javadoc precisa ser melhorado
	/**
	 * Retorna a nova coordenada x de um ponto baseando-se em um �ngulo e taxa de deslocamento.
	 * 
	 * @param xAtual
	 * @param angulo
	 * @param raio
	 * @return
	 */
	public static float retornaX(float xAtual, float angulo, float raio) {
		return (float) (xAtual + (raio * Math.cos(Math.PI * angulo / 180.0)));
	}

	// TODO este javadoc precisa ser melhorado
	/**
	 * Retorna a nova coordenada y de um ponto baseando-se em um �ngulo e taxa de deslocamento.
	 * 
	 * @param yAtual
	 * @param angulo
	 * @param raio
	 * @return
	 */
	public static float retornaY(float yAtual, float angulo, float raio) {
		return (float) (yAtual + (raio * Math.sin(Math.PI * angulo / 180.0)));
	}

	/**
	 * Subtrai um vetor 2D
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	private static Ponto SubVetor2D(Ponto p1, Ponto p2) {
		float x = (p1.getX() - p2.getX());
		float y = (p1.getY() - p2.getY());

		Ponto c = new Ponto(x, y, null);
		return c;
	}

	/**
	 * Determina se um ponto pertence a um tri�ngulo utilizando a compara��o da �rea do tri�ngulo com a soma da �rea de tr�s tri�ngulos formados
	 * com os pontos do tri�ngulo e o ponto de interesse.
	 * 
	 * @param of
	 * @param rf
	 * @param lf
	 * @param ponto
	 * @return
	 */
	public static boolean pontoNoTrianguloUsandoSomaAreas(Ponto of, Ponto rf, Ponto lf, Ponto ponto) {
		long inicio = System.currentTimeMillis();
		// Fonte: ???
		// TODO Fazer m�todo que gere tri�ngulos com os pontos do frustum + o waypoint e ver se a �rea deles � igual a �rea do frustum
		double areaOLR = areaTriangulo(of, lf, rf);
		double areaOLW = areaTriangulo(of, lf, ponto);
		double areaORW = areaTriangulo(of, rf, ponto);
		double areaLRW = areaTriangulo(lf, rf, ponto);

		boolean noTriangulo = (areaOLW + areaORW + areaLRW) <= areaOLR; // TODO alterei na reuni�o, antes estava usando == no lugar de <=

		long duracao = System.currentTimeMillis() - inicio;
		gravarLog("pontoNoTrianguloUsandoSomaAreas", duracao);
		return noTriangulo;
	}

	// TODO Procurar o livro para documentar este m�todo de forma decente
	/**
	 * Determina se um ponto pertence a um tri�ngulo utilizando a t�cnica passada pelo Dalton.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	public static boolean pontoNoTrianguloMatrizDalton(Ponto a, Ponto b, Ponto c, Ponto d) {
		long inicio = System.currentTimeMillis();

		Ponto ab = null;
		Ponto ac = null;
		boolean retorno = false;
		float ter;
		float[][] matrizAuxiliar = new float[2][2];
		float[][] matrizI = new float[2][2]; // TODO que nome � esse??

		ab = SubVetor2D(b, a);
		ac = SubVetor2D(c, a);

		matrizAuxiliar[0][0] = ab.getX();
		matrizAuxiliar[0][1] = ab.getY();

		matrizAuxiliar[1][0] = ac.getX();
		matrizAuxiliar[1][1] = ac.getY();

		ter = 1 / ((matrizAuxiliar[0][0] * matrizAuxiliar[1][1]) - (matrizAuxiliar[1][0] * matrizAuxiliar[0][1]));
		matrizI[0][0] = ter * matrizAuxiliar[1][1];
		matrizI[0][1] = ter * -matrizAuxiliar[0][1];
		matrizI[1][0] = ter * -matrizAuxiliar[1][0];
		matrizI[1][1] = ter * matrizAuxiliar[0][0];

		float s, t;

		Ponto at = SubVetor2D(d, a);
		s = (at.getX() * matrizI[0][0]) + (at.getY() * matrizI[1][0]);
		t = (at.getX() * matrizI[0][1]) + (at.getY() * matrizI[1][1]);
		if (!((s < 0) || (t < 0) || ((s + t) > 1))) {
			retorno = true;
		}

		long duracao = System.currentTimeMillis() - inicio;
		gravarLog("pontoNoTrianguloMatrizDalton", duracao);
		return retorno;
	}

	/**
	 * Tentativa de scanline para determinar se um ponto pertence a um tri�ngulo.
	 * N�o funciona completamente. Est� sendo mantido no c�digo para fazer uma an�lise de desempenho depois.
	 * 
	 * @param fo
	 * @param fe
	 * @param fd
	 * @param pontoInteresse
	 * @return
	 */
	public static boolean pontoNoTrianguloScanline(Ponto fo, Ponto fe, Ponto fd, Ponto pontoInteresse) {
		long inicio = System.currentTimeMillis();

		boolean retorno = false;
		List<Ponto> points = new ArrayList<Ponto>();
		points.add(fo);
		points.add(fd);
		points.add(fe);
		int n = 0;
		for (int i = 0; i < points.size() - 1; i++) {
			if (points.get(i).getY() != points.get(i + 1).getY()) {
				// minha aresta � meu ponto atual e o proximo ponto
				float ti = (pontoInteresse.getY() - points.get(i).getY()) / (points.get(i + 1).getY() - points.get(i).getY());
				// x ponto interseccao
				float xInt = points.get(i).getX() + (points.get(i + 1).getX() - points.get(i).getX()) * ti;
				// y ponto interseccao
				float yInt = pontoInteresse.getY();
				Ponto pInt = new Ponto(xInt, yInt, null);
				if (pInt.getX() == pontoInteresse.getX()) {
					break;
				} else if ((pInt.getX() > pontoInteresse.getX()) && (pInt.getY() > Math.min(points.get(i).getY(), points.get(i + 1).getY())) && (pInt.getY() <= Math.max(points.get(i).getY(), points.get(i + 1).getY()))) {
					n++;
				}
			} else if ((pontoInteresse.getY() == points.get(i).getY()) && (pontoInteresse.getX() >= Math.min(points.get(i).getX(), points.get(i + 1).getX())) && pontoInteresse.getX() <= Math.max(points.get(i).getX(), points.get(i + 1).getX())) {
				break;
			}
		}
		if (n % 2 != 0) {
			retorno = true;
		}

		long duracao = System.currentTimeMillis() - inicio;
		gravarLog("pontoNoTrianguloScanline", duracao);
		return retorno;
	}

	/**
	 * Realiza o c�lculo da �rea do tri�ngulo definido pelos pontos passados por par�metro
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	private static double areaTriangulo(Ponto a, Ponto b, Ponto c) {
		// Fonte: http://www.inf.unioeste.br/~rogerio/Geometria-Triangulos.pdf
		double area = 0.5 * (((a.getX() * b.getY()) - (a.getY() * b.getX())) + ((a.getY() * c.getX()) - (a.getX() * c.getY())) + ((b.getX() * c.getY()) - (b.getY() * c.getX())));
		return Math.abs(area);
	}

	/**
	 * Retorna o ponto de intersec��o entre as retas KL e MN.
	 * 
	 * @param k
	 * @param l
	 * @param m
	 * @param n
	 * @return
	 */
	public static Ponto getInterseccaoRetas(Ponto k, Ponto l, Ponto m, Ponto n) {
		long inicio = System.currentTimeMillis();
		float det = (n.getX() - m.getX()) * (l.getY() - k.getY()) - (n.getY() - m.getY()) * (l.getX() - k.getX());
		float s = ((n.getX() - m.getX()) * (m.getY() - k.getY()) - (n.getY() - m.getY()) * (m.getX() - k.getX())) / det;
		Ponto ponto = new Ponto(k.getX() + (l.getX() - k.getX()) * s, k.getY() + (l.getY() - k.getY()) * s, null);
		long duracao = System.currentTimeMillis() - inicio;
		gravarLog("getInterseccaoRetas", duracao);
		return ponto;
	}

	public static void setLogTempos(boolean logTempos) {
		PortalAPI_Utils.logTempos = logTempos;
		if (!PortalAPI_Utils.logTempos) {
			finalizaGravacaoLog();
			fw = null;
		}
	}

	public static void gravarLog(String metodo, long duracao) {
		if (PortalAPI_Utils.logTempos) {
			if (fw == null) {
				try {
					fw = new FileWriter(new File("/sdcard/TemposTCC.txt"));
				} catch (IOException e) {
					System.out.println("Erro ao criar arquivo de log dos tempos." + e);
				}
			}
			try {
				fw.write("M�todo: " + metodo + "\tDura��o: " + duracao + "\r\n");
			} catch (IOException e) {
				System.out.println("Erro ao escrever no arquivo de log dos tempos" + e);
			}
		}
	}

	public static void finalizaGravacaoLog() {
		if (fw != null) {
			try {
				fw.flush();
				fw.close();
			} catch (IOException e) {
				System.out.println("Erros ao fechar o arquivo." + e);
			}
		}
	}

}
