package tcc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import tcc.arquitetura.ConstantesNavegacao;
import tcc.arquitetura.MapeadorDeputado;
import tcc.arquitetura.TCCUtils;
import tcc.dados.QopDAO;
import tcc.dados.UsuarioDAO;
import tcc.dominio.ComparatorQoP;
import tcc.dominio.Usuario;
import tcc.dominio.QoP;

import java.text.DecimalFormat;  

public class QopMBean extends GeneralBean{
	
	QopDAO qopDao;
	
	Collection<QoP> qopsPessoais;
	Collection<QoP> qopsGerais;
		
	MapeadorDeputado mapDeputados;
	
	public QopMBean() throws ClassNotFoundException, SQLException{
		
		qopDao = new QopDAO();
		
		UsuarioDAO usuarioDao = new UsuarioDAO();
		usuario = usuarioDao.findByPrimaryKey((Integer)(getSession().getAttribute("user")));
		
		qopsPessoais = new ArrayList<QoP>();
		qopsGerais = new ArrayList<QoP>();
		
		mapDeputados = new MapeadorDeputado();
		
		popularQopsPessoais();
		popularQopsGerais();
		
	}
	
	public void popularQopsPessoais() throws SQLException{
		
		float qopClasse1;
		float qopClasse2;
		float qopClasse3;
		float qopFinal;
		
		int qtMaxClasse1 = qopDao.getQtNoticiaMax(1);
		int qtMaxClasse2 = qopDao.getQtNoticiaMax(2);
		int qtMaxClasse3 = qopDao.getQtNoticiaMax(3);
		
		DecimalFormat aproximador = new DecimalFormat("00.00"); 
		
		for (String nome : mapDeputados.getDeputados()){
				
			if (nome != null){
			
				int idDeputado = mapDeputados.getIndiceDeputado(nome);
			
				//Inicializar valores caso não haja nenhuma notícia cadastrada para evitar divisão por 0
				if (qtMaxClasse1 == 0 ) qtMaxClasse1++;
				if (qtMaxClasse2 == 0 ) qtMaxClasse2++;
				if (qtMaxClasse3 == 0 ) qtMaxClasse3++;
				
				qopClasse1 =  20 * qopDao.getMediaRelevanciaPessoal(idDeputado, 1, usuario.getId()) * qopDao.getQtNoticia(idDeputado, 1) / qtMaxClasse1;
				qopClasse2 =  20 * qopDao.getMediaRelevanciaPessoal(idDeputado, 2, usuario.getId()) * qopDao.getQtNoticia(idDeputado, 2) / qtMaxClasse2;
				qopClasse3 =  20 * qopDao.getMediaRelevanciaPessoal(idDeputado, 3, usuario.getId()) * qopDao.getQtNoticia(idDeputado, 3) / qtMaxClasse3;
			
				qopFinal = qopClasse1*usuario.getPesoClasse1()/10 + qopClasse2*usuario.getPesoClasse2()/10 + qopClasse3*usuario.getPesoClasse3()/10;
			
				qopClasse1 = Float.parseFloat(aproximador.format(qopClasse1).replace(',', '.'));
				qopClasse2 = Float.parseFloat(aproximador.format(qopClasse2).replace(',', '.'));
				qopClasse3 = Float.parseFloat(aproximador.format(qopClasse3).replace(',', '.'));
				qopFinal = Float.parseFloat(aproximador.format(qopFinal).replace(',', '.'));
			
				qopsPessoais.add(new QoP(nome, qopClasse1, qopClasse2, qopClasse3, qopFinal));
			}
		}
		
		//ordena por qopgeral
		ordenarQopPor('p',0);
		
	}
	
	public void popularQopsGerais() throws SQLException{
		
		float qopClasse1;
		float qopClasse2;
		float qopClasse3;
		float qopFinal;
		
		int qtMaxClasse1 = qopDao.getQtNoticiaMax(1);
		int qtMaxClasse2 = qopDao.getQtNoticiaMax(2);
		int qtMaxClasse3 = qopDao.getQtNoticiaMax(3);
		
		DecimalFormat aproximador = new DecimalFormat( " 0.00 " ); 
		
		for (String nome : mapDeputados.getDeputados()){
			
			if (nome != null){
			
				int idDeputado = mapDeputados.getIndiceDeputado(nome);
			
				//Inicializar valores caso não haja nenhuma notícia cadastrada para evitar divisão por 0
				if (qtMaxClasse1 == 0 ) qtMaxClasse1++;
				if (qtMaxClasse2 == 0 ) qtMaxClasse2++;
				if (qtMaxClasse3 == 0 ) qtMaxClasse3++;
				
				
				qopClasse1 = 20 * qopDao.getMediaRelevanciaGeral(idDeputado, 1) * qopDao.getQtNoticia(idDeputado, 1) / qtMaxClasse1;
				qopClasse2 = 20 * qopDao.getMediaRelevanciaGeral(idDeputado, 2) * qopDao.getQtNoticia(idDeputado, 2) / qtMaxClasse2;
				qopClasse3 = 20 * qopDao.getMediaRelevanciaGeral(idDeputado, 3) * qopDao.getQtNoticia(idDeputado, 3) / qtMaxClasse3;
			
				//Média harmônica
				qopFinal = 3/(1/(qopClasse1+1) + 1/(qopClasse2+1) + 1/(qopClasse3+1));

				qopClasse1 = Float.parseFloat(aproximador.format(qopClasse1).replace(',', '.'));
				qopClasse2 = Float.parseFloat(aproximador.format(qopClasse2).replace(',', '.'));
				qopClasse3 = Float.parseFloat(aproximador.format(qopClasse3).replace(',', '.'));
				qopFinal = Float.parseFloat(aproximador.format(qopFinal-1).replace(',', '.'));
			
				qopsGerais.add(new QoP(nome, qopClasse1, qopClasse2, qopClasse3, qopFinal));
			
			}
		}
		
		//ordena por qopgeral
		ordenarQopPor('g',0);
		
	}
		
	public String ordenarQopPor(){
		
		FacesContext fc = FacesContext.getCurrentInstance(); 
		Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
		char tipo = params.get("tipo").charAt(0);
		int campo = Integer.parseInt(params.get("campo"));
		
		ordenarQopPor(tipo,campo);
		
		return null;
		
	}
		
	private void ordenarQopPor(char tipo, int campo){		
		
		Collection<QoP> colecaoAOrdenar = null;
		
		if (tipo == 'g')
			colecaoAOrdenar = qopsGerais;
		else if (tipo == 'p')
			colecaoAOrdenar = qopsPessoais;
		
		List<QoP> lista = new ArrayList<QoP>();
		
		for (QoP qop : colecaoAOrdenar)
			lista.add(qop);
		
		Collections.sort(lista, new ComparatorQoP(campo));
		
		if (tipo == 'g')
			qopsGerais = lista;
		else if (tipo == 'p')
			qopsPessoais = lista;
			
	}
		
	public Collection<QoP> getQopsPessoais() {
		return qopsPessoais;
	}

	public Collection<QoP> getQopsGerais() {
		return qopsGerais;
	}

	public String redirecionarPaginaInicio() throws IOException, SQLException{
		return TCCUtils.redirecionar(ConstantesNavegacao.PAGINA_NOTICIAS);
	}
		

}
