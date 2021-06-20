package com.dawii.trabfinal.pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class PageWrapper<T> {

	private static final Logger logger = LoggerFactory.getLogger(PageWrapper.class);
	
	private Page<T> pagina;
	private UriComponentsBuilder uriBuilder;
	private int maximoPaginasMostrar = 5;
	private int inicio;
	private int fim;
	
	public PageWrapper(Page<T> pagina, HttpServletRequest request) {
		this.pagina = pagina;
		StringBuffer requestURL = request.getRequestURL();
		String queryString = request.getQueryString();
		String httpURL = requestURL.append(queryString != null ? "?" + queryString : "").toString().replaceAll("\\+", "%20");
		logger.debug("PageWrapper criado para a pagina {}, requestURL {}, queryString {} e URL final {}", pagina, requestURL, queryString, httpURL);
		uriBuilder  = UriComponentsBuilder.fromHttpUrl(httpURL);
		definirInicioFimPaginacao();
	}
	
	public List<T> getConteudo() {
		return pagina.getContent();
	}

	public boolean isVazia() {
		return pagina.getContent().isEmpty();
	}
	
	public int getAtual() {
		return pagina.getNumber();
	}
	
	public boolean isPrimeira() {
		return pagina.isFirst();
	}
	
	public boolean isUltima() {
		return pagina.isLast();
	}
	
	public int getNumeroPaginas() {
		return pagina.getTotalPages();
	}
	
	public int getInicio() {
		return inicio;
	}
	
	public int getFim() {
		return fim;
	}
	
	public String urlParaPagina(int pagina) {
		logger.debug("Gerando URL para a pagina {}", pagina);
		String url = uriBuilder.replaceQueryParam("page", pagina).build(true).encode().toUriString();
		logger.debug("URL gerada: {}", url);
		return url;
	}
	
	//tenho a impressao que isso aqui so funciona para uma propriedade ordenada e nao mais de uma.
	public String urlInvertendoDirecaoOrdem(String propriedade) {
		logger.debug("Gerando URL invertendo a direcao da ordem para a propriedade {}", propriedade);
		//Nao podemos usar o mesmo uriBuilder ou então ao carregar a pagina o sort ja vai ser
		// inserido no objeto e aparecer na paginacao mesmo que nao tenhamos clicado para ordenar
		UriComponentsBuilder uriBuilderOrder = UriComponentsBuilder.fromUriString(uriBuilder.build(true).encode().toUriString());
		
		String valorSort = String.format("%s,%s", propriedade, inverterDirecaoOrdem(propriedade));
		
		String url = uriBuilderOrder.replaceQueryParam("sort", valorSort).build(true).encode().toUriString();
		logger.debug("URL gerada: {}", url);
		return url;
	}
	
	public String inverterDirecaoOrdem(String propriedade) {
		String direcao = "asc";
		
		Sort.Order order = (pagina.getSort() != null) ? pagina.getSort().getOrderFor(propriedade) : null;
		if (order != null) {
			direcao = Sort.Direction.ASC.equals(order.getDirection()) ? "desc" : "asc";
		}
		
		return direcao;
	}
	
	public boolean isDescendente(String propriedade) {
		Sort.Order order = (pagina.getSort() != null) ? pagina.getSort().getOrderFor(propriedade) : null;
		if (order != null) {
			return Sort.Direction.DESC.equals(order.getDirection());
		}
		return false;
	}
	
	public boolean ordenada(String propriedade) {
		Sort.Order order = (pagina.getSort() != null) ? pagina.getSort().getOrderFor(propriedade) : null;
		return order != null;
	}

	public int getMaximoPaginasMostrar() {
		return maximoPaginasMostrar;
	}

	public void setMaximoPaginasMostrar(int maximoPaginasMostrar) {
		this.maximoPaginasMostrar = maximoPaginasMostrar;
	}
	
	private void definirInicioFimPaginacao() {
		logger.debug("Gerando a numeração da paginação para a Página {} e o máximo de páginas na numeração {}", pagina, maximoPaginasMostrar);
		int metadeMaximoPaginasMostrar = maximoPaginasMostrar / 2;
		int totalDePaginas = pagina.getTotalPages();
		int paginaAtual = pagina.getNumber() + 1;

		logger.debug("totalDePaginas {}, paginaAtual {}", totalDePaginas, paginaAtual);
		
		inicio = 1;
		if (totalDePaginas == 0) {
			fim = 1;
		} else if (totalDePaginas <= maximoPaginasMostrar) {
			fim = totalDePaginas;
		} else {
			if (paginaAtual <= metadeMaximoPaginasMostrar + 1) {
				fim = maximoPaginasMostrar;
			} else {
				inicio = paginaAtual - metadeMaximoPaginasMostrar;
				if (paginaAtual + metadeMaximoPaginasMostrar <= totalDePaginas) {
					fim = paginaAtual + metadeMaximoPaginasMostrar;
				} else {
					fim = totalDePaginas;
					inicio = totalDePaginas - maximoPaginasMostrar + 1;
				}
			}
		}
		logger.debug("Números das páginas serão gerados entre {} e {}", inicio, fim);
	}
	
}
