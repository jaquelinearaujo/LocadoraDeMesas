package com.dawii.trabfinal.pagination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class PageWrapper<T> {
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
		String url = uriBuilder.replaceQueryParam("page", pagina).build(true).encode().toUriString();
		return url;
	}
	
	public String urlInvertendoDirecaoOrdem(String propriedade) {
		UriComponentsBuilder uriBuilderOrder = UriComponentsBuilder.fromUriString(uriBuilder.build(true).encode().toUriString());
		
		String valorSort = String.format("%s,%s", propriedade, inverterDirecaoOrdem(propriedade));
		
		String url = uriBuilderOrder.replaceQueryParam("sort", valorSort).build(true).encode().toUriString();
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
		int metadeMaximoPaginasMostrar = maximoPaginasMostrar / 2;
		int totalDePaginas = pagina.getTotalPages();
		int paginaAtual = pagina.getNumber() + 1;

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
	}
	
}
