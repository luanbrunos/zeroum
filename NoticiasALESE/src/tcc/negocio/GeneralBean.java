package tcc.negocio;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import tcc.arquitetura.ConstantesNavegacao;
import tcc.arquitetura.TCCUtils;
import tcc.dados.UsuarioDAO;
import tcc.dominio.Usuario;

public class GeneralBean {

	protected Collection<String> listaMensagens; 
	
	protected Usuario usuario;
	
	public GeneralBean(){
		
	}
	
	public boolean checkRole() throws ClassNotFoundException, SQLException, IOException{
		
		UsuarioDAO usuarioDao = new UsuarioDAO();
		usuario = usuarioDao.findByPrimaryKey((Integer)(getSession().getAttribute("user")));
		
		if (usuario == null){
			TCCUtils.redirecionar(ConstantesNavegacao.PAGINA_LOGIN);
			return false;
		}
		
		return true;
		
	}
	
	public String entrarAjuda() throws IOException, ClassNotFoundException, SQLException{
		return TCCUtils.redirecionar(ConstantesNavegacao.PAGINA_AJUDA);
	}
	
	protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

	protected HttpSession getSession() {
        return (HttpSession) getFacesContext().getExternalContext().getSession(false);
    }
	
}
