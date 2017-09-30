package tcc.negocio;

import java.io.IOException;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import tcc.arquitetura.ConstantesNavegacao;
import tcc.arquitetura.MapeadorClasses;
import tcc.arquitetura.MapeadorDeputado;
import tcc.arquitetura.TCCUtils;
import tcc.dados.NoticiaDAO;
import tcc.dados.UsuarioDAO;
import tcc.dominio.Noticia;
import tcc.dominio.Usuario;
import tcc.integracao.Carregador;
import tcc.textmining.Classificador;
import tcc.textmining.SeletorDeputado;

public class NoticiaMBean extends GeneralBean{

	private Noticia noticia;
	
	private Collection<Noticia> noticias;
	
	private NoticiaDAO noticiaDao;
	
	private Classificador classificador;
	private SeletorDeputado seletorDeputado;
	
	//campos usados para ferramenta de busca
	private Integer relevanciaInicio = 0;
	private Integer relevanciaFim = 5;
	private Date dataInicio;
	private Date dataFim;
	
	private boolean veioDaPaginaInicial;
		
	public NoticiaMBean() throws ClassNotFoundException, SQLException{
		
		noticiaDao = new NoticiaDAO();
		
		UsuarioDAO usuarioDao = new UsuarioDAO();
		usuario = usuarioDao.findByPrimaryKey((Integer)(getSession().getAttribute("user")));
		
		noticias = noticiaDao.findAllNaoLida(usuario.getId());
		
		veioDaPaginaInicial = true;
	}
	
    public NoticiaMBean(Noticia n) throws ClassNotFoundException, SQLException{
    	noticia = n;
    	noticiaDao = new NoticiaDAO();
    }
    
    public NoticiaMBean(Noticia n, Collection<Noticia> ns) throws ClassNotFoundException, SQLException{
    	noticia = n;
    	noticias = ns;
    	noticiaDao = new NoticiaDAO();
    }
    
    public NoticiaMBean(Collection<Noticia> ns) throws ClassNotFoundException, SQLException{
    	noticias = ns;
    	noticiaDao = new NoticiaDAO();
    }
	

	public boolean isVeioDaPaginaInicial() {
		return veioDaPaginaInicial;
	}

	public boolean definirRelevancia(int relevancia) throws ClassNotFoundException, SQLException, IOException{
		
		noticia.setRelevancia(relevancia);
		
		noticiaDao.persistirNoticia(noticia,usuario.getId());
		
		return true;
		
	}
	
	public boolean buscarDoDeputado(int idDeputado) throws SQLException{
		
		noticias = noticiaDao.findByDeputado(idDeputado,usuario.getId());
		
		return true;
		
	}
	
	public boolean buscarNaoLidas() throws SQLException, ClassNotFoundException, IOException{
				
		noticias = noticiaDao.findAllNaoLida(usuario.getId());
		
		return true;
		
	}

	
	public String entrarNoticia() throws ClassNotFoundException, SQLException, IOException{
		
		if (!checkRole())
			return null;
		
		NoticiaDAO noticiaDao = new NoticiaDAO();
		
		FacesContext fc = FacesContext.getCurrentInstance(); 
		Map<String,String> params = fc.getExternalContext().getRequestParameterMap();    
        Integer idNoticia = Integer.parseInt(params.get("idNoticia"));   
		
		selecionarNoticia(noticiaDao.findByPrimaryKey(idNoticia, usuario.getId()));
		
		//Se a notícia está marcada como não lida, marcar como lida
		if (!noticia.isLida()){
			noticia.setLida(true);
			noticiaDao.persistirNoticia(noticia, usuario.getId());
		}
		
		if (veioDaPaginaInicial)
		 buscarNaoLidas();
		
		return TCCUtils.redirecionar(ConstantesNavegacao.PAGINA_DETALHE_NOTICIA);
		
	} 
	
	public String entrarBuscaNoticia() throws IOException, ClassNotFoundException, SQLException{
		
		if (veioDaPaginaInicial)
			noticias = new ArrayList<Noticia>();
		
		veioDaPaginaInicial = false;
		
		if (!checkRole())
			return null;
		
		noticia = new Noticia();
		
		noticia.setIdDeputado(-1);
		noticia.setClasse(-1);
		
		return TCCUtils.redirecionar(ConstantesNavegacao.PAGINA_BUSCA_NOTICIA);
	}
	
	
	public String entrarQoP() throws IOException, ClassNotFoundException, SQLException{
		
		if (!checkRole())
			return null;
		
		return TCCUtils.redirecionar(ConstantesNavegacao.PAGINA_QOP);
	}
	
	public void selecionarNoticia(Noticia n) throws SQLException{
			
		noticia = noticiaDao.findByPrimaryKey(n.getIdNoticia(), usuario.getId());

	}
	
	public void selecionarNoticia(int i){
		
		int count = -1;
		for (Noticia n : noticias){
			noticia = n;
			count++;
			if (count == i)
				break;
		}
		
	}
	
	public void persistirNoticiaPrimeiraVez(Noticia n) throws Exception{
		
		if (classificador == null)
			classificador = new Classificador();
		
		if (seletorDeputado == null)
			seletorDeputado = new SeletorDeputado();		
		
		//Classificar notícia
		n.setClasse(classificador.classificar(n.getTexto())+1);
		
		//Definir autoria da notícia
		n.setIdDeputado(seletorDeputado.buscarCodigoDeputado(n.getTitulo()));
		
		noticiaDao.persistirNoticia(n,0);
		
	}
	
	public String gravarNoticia() throws Exception{
		persistirNoticia(noticia);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Relevância definida com sucesso", ""));
		return null;
	}
	
	public void persistirNoticia(Noticia n) throws Exception{
		
		if (seletorDeputado == null)
			seletorDeputado = new SeletorDeputado();
		
		n.setIdDeputado(seletorDeputado.buscarCodigoDeputado(n.getTitulo()));
		
		noticiaDao.persistirNoticia(n,usuario.getId());
		
	}
	
	
	public String buscarNoticia() throws SQLException, IOException{
	
		if ((dataInicio!=null)&&(dataFim!=null)&&(dataInicio.after(dataFim))){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Data de início não pode ser depois da Data de Fim", ""));
			return null;
		}
		
		noticias = noticiaDao.buscar(noticia.getIdDeputado(), noticia.getTitulo(), noticia.getTexto(), noticia.getClasse(), null, dataInicio, dataFim, relevanciaInicio, relevanciaFim, usuario.getId());
		
		if (noticias.isEmpty()){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Não foram encontradas notícias com os parâmetros informados", ""));
			return null;
		}
		
		return TCCUtils.redirecionar(ConstantesNavegacao.PAGINA_BUSCA_NOTICIA);
		
	}
	
	public Collection<SelectItem> getRelevancias(){
		
		Collection<SelectItem> relevancias = new ArrayList<SelectItem>();
				
		for (int i = 0; i <= 5; i++)
			relevancias.add(new SelectItem(i,Integer.toString(i)));
		
		return relevancias;
		
	}
	
	public Collection<SelectItem> getDeputados() throws ClassNotFoundException, SQLException{
		
		Collection<SelectItem> deputados = new ArrayList<SelectItem>();
		MapeadorDeputado mapDep = new MapeadorDeputado();
		
		deputados.add(new SelectItem(-1, "--TODOS--"));
		
		for (int i = 0; i < mapDep.size()-1; i++){
			deputados.add(new SelectItem(i,mapDep.getDeputado(i)));
		}
				
		return deputados;
		
	} 
	
	public Collection<SelectItem> getClasses() throws ClassNotFoundException, SQLException{
		
		Collection<SelectItem> classes = new ArrayList<SelectItem>();
		MapeadorClasses mapCla = new MapeadorClasses();
		
		classes.add(new SelectItem(-1, "--TODAS--"));
		
		for (int i = 1; i < mapCla.size()-1; i++){
			classes.add(new SelectItem(i,mapCla.getClasse(i)));
		}
				
		return classes;
		
	} 
	
	public Noticia getNoticia() {
		return noticia;
	}
	
	public Collection<Noticia> getNoticias() {
		return noticias;
	}
	
	public Usuario getUsuario(){
		return usuario;
	}
	
	public Integer getRelevanciaInicio() {
		return relevanciaInicio;
	}

	public void setRelevanciaInicio(Integer relevanciaInicio) {
		this.relevanciaInicio = relevanciaInicio;
	}

	public Integer getRelevanciaFim() {
		return relevanciaFim;
	}

	public void setRelevanciaFim(Integer relevanciaFim) {
		this.relevanciaFim = relevanciaFim;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String redirecionarPaginaInicio() throws IOException, SQLException{
		veioDaPaginaInicial = true;
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("qop");
		noticias = noticiaDao.findAllNaoLida(usuario.getId());
		return TCCUtils.redirecionar(ConstantesNavegacao.PAGINA_NOTICIAS);
	}


	public String atualizar() throws IOException, ClassNotFoundException, SQLException{
		return TCCUtils.redirecionar(ConstantesNavegacao.PAGINA_NOTICIAS);
	}
	
	
	
		
}
