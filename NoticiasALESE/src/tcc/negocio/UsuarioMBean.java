package tcc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tcc.arquitetura.ConstantesNavegacao;
import tcc.arquitetura.TCCUtils;
import tcc.dados.UsuarioDAO;
import tcc.dominio.Usuario;

public class UsuarioMBean extends GeneralBean{
	
	private boolean cadastrar;
	
	//Indica se houve falha no login
	private boolean falha = false;
	
	//Usado para alterar senha
	private String senhaAtual = "";
	private String senhaNova = "";
	private String senhaNova2 = "";
	
	public UsuarioMBean(){
		usuario = new Usuario();
		falha = false;
		listaMensagens = new ArrayList<String>();
	}
	
	public String sair() throws IOException{
		usuario = new Usuario();
		getSession().setAttribute("user", 0);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("noticia"); 
		return TCCUtils.redirecionar(ConstantesNavegacao.PAGINA_LOGIN);
	}
	
	public String entrarCadastraUsuario() throws IOException{
		usuario = new Usuario();
		cadastrar = true;
		return TCCUtils.redirecionar(ConstantesNavegacao.PAGINA_CADASTRA_ALTERA_USUARIO);
	}
	
	public String entrarAlteraUsuario() throws IOException, ClassNotFoundException, SQLException{
		UsuarioDAO usuarioDao = new UsuarioDAO();
		usuario = usuarioDao.findByPrimaryKey((Integer)(getSession().getAttribute("user")));
		cadastrar = false;
		return TCCUtils.redirecionar(ConstantesNavegacao.PAGINA_CADASTRA_ALTERA_USUARIO);
	}
	
	public String entrar() throws ClassNotFoundException, SQLException, IOException{
		
		falha = false;
		
		UsuarioDAO usuarioDao = new UsuarioDAO();
		
		Usuario u = usuarioDao.findByLoginSenha(usuario.getEmail(), usuario.getSenha());
				
		if (u == null){
			falha = true;
			return TCCUtils.redirecionar(ConstantesNavegacao.PAGINA_LOGIN);
		} else {
			usuario = u;
			getSession().setAttribute("user", usuario.getId());			
			return TCCUtils.redirecionar(ConstantesNavegacao.PAGINA_NOTICIAS);
		} 
		
		
	}
	
	public String persistirUsuario() throws ClassNotFoundException, SQLException, IOException{
		
		UsuarioDAO usuarioDao = new UsuarioDAO();
		
		//FacesContext.getCurrentInstance()
		
		String mensagem = usuario.validarCampos();
		if (!TCCUtils.isEmpty(mensagem))
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,mensagem+":", "Campo não pode ser vazio"));
		
		//verifica se há alteração de senha
		if ((cadastrar)||(senhaAtual.length()>0)){
			Usuario temp = usuarioDao.findByLoginSenha(usuario.getEmail(), senhaAtual);
			if((temp==null)&&(!cadastrar))
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Senha atual incorreta",""));
			else if (senhaNova.length()<5)
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"A senha deve conter pelo menos 5 dígitos",""));
			else if(!senhaNova.equals(senhaNova2)){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Novas senhas não conferem",""));
			} else {
					usuario.setSenha(senhaNova);
			}
		}
		
		if (!FacesContext.getCurrentInstance().getMessageList().isEmpty())
			return null;
		
		
		usuarioDao.persistirUsuario(usuario);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Usuário "+(cadastrar?"cadastrado":"alterado")+" com sucesso",""));
			
		return null;
	}
	
	public boolean getFalha(){
		return falha;
	}
	
	public Usuario getUsuario(){
		return usuario;
	}
	
	public boolean isCadastrar() {
		return cadastrar;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getSenhaNova() {
		return senhaNova;
	}

	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}

	public String getSenhaNova2() {
		return senhaNova2;
	}

	public void setSenhaNova2(String senhaNova2) {
		this.senhaNova2 = senhaNova2;
	}

	public Collection<String> getListaMensagens() {
		return listaMensagens;
	}

	public void setCadastrar(boolean cadastrar) {
		this.cadastrar = cadastrar;
	}

	
}
