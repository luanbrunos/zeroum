package tcc.dados;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import tcc.dominio.Usuario;
import tcc.dados.Conexao;

public class UsuarioDAO {

private Connection conexao;
	
	public UsuarioDAO() throws ClassNotFoundException, SQLException{
		
		conexao = Conexao.getConexao();
		
	}
	
	public Usuario findByPrimaryKey(int idUsuario) throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select * from usuario where id_usuario = "+idUsuario;
		
		ResultSet rs = st.executeQuery(sql);
		
		return encarnarUsuario(rs);
		
	}
	
	public Usuario findByLoginSenha(String login, String senha) throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select * from usuario where email = '"+login+"' and senha = '"+senha+"'";
		
		ResultSet rs = st.executeQuery(sql);
		
		return encarnarUsuario(rs);
	}
	
	private Usuario encarnarUsuario(ResultSet rs) throws SQLException{
	
		Usuario usuario = null;
		
		while (rs.next()){
			
			usuario = new Usuario();
			
			usuario.setEmail(rs.getString("email"));
			usuario.setId(rs.getInt("id_usuario"));
			usuario.setNome(rs.getString("nome"));
			usuario.setPesoClasse1(rs.getFloat("pesoclasse1"));
			usuario.setPesoClasse2(rs.getFloat("pesoclasse2"));
			usuario.setPesoClasse3(rs.getFloat("pesoclasse3"));
			usuario.setReceberEmails(rs.getBoolean("receber_emails"));
			break;
		}
		
		return usuario;
	}
	
	public int findMaxIdUsuario() throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select max(id_usuario) as max from usuario";
		
		ResultSet rs = st.executeQuery(sql);
		
		rs.next();
		return rs.getInt("max");
		
	}
	
	public boolean persistirUsuario(Usuario usuario) throws SQLException{
		
		Statement st = conexao.createStatement();
		
		if (usuario.getId() == 0){
			
			String sql = "insert into usuario(id_usuario";
			if (usuario.getNome()!=null)
				sql += ", nome";
			if (usuario.getEmail()!=null)
				sql += ", email";
			if (usuario.getSenha()!=null)
				sql += ", senha";
			//if (usuario.getPesoClasse1()!=0)
				sql += ", pesoclasse1";
			//if (usuario.getPesoClasse2()!=0)
				sql += ", pesoclasse2";
			//if (usuario.getPesoClasse3()!=0)
				sql += ", pesoclasse3";
				sql += ", receber_emails";
			
			sql += ") values (";
			
			int idUsuario = findMaxIdUsuario();
			usuario.setId(idUsuario+1);
			sql += (idUsuario + 1)+" ";
			
			if (usuario.getNome()!=null)
				sql += ", '"+usuario.getNome()+"' ";
			if (usuario.getEmail()!=null)
				sql += ", '"+usuario.getEmail()+"' ";
			if (usuario.getSenha()!=null)
				sql += ", '"+usuario.getSenha()+"' ";
			//if (usuario.getPesoClasse1()!=0)
				sql += ", "+usuario.getPesoClasse1();
			//if (usuario.getPesoClasse2()!=0)
				sql += ", "+usuario.getPesoClasse2();
			//if (usuario.getPesoClasse3()!=0)
				sql += ", "+usuario.getPesoClasse3();
				sql += ", "+usuario.isReceberEmails();
			
			sql += ")";
			
			st.executeUpdate(sql);
						
		}else{
			
			String sql = "update usuario set ";
			if (usuario.getEmail()!=null)
				sql += " email = '"+usuario.getEmail()+"', ";
			if (usuario.getNome()!=null)
				sql += " nome = '"+usuario.getNome()+"', ";
			if (usuario.getSenha()!=null)
				sql += " senha = '"+usuario.getSenha()+"', ";
			//if (usuario.getPesoClasse1()!=0)
				sql += " pesoclasse1 = "+usuario.getPesoClasse1()+", ";
			//if (usuario.getPesoClasse2()!=0)
				sql += " pesoclasse2 = "+usuario.getPesoClasse2()+", ";
			//if (usuario.getPesoClasse3()!=0)
				sql += " pesoclasse3 = "+usuario.getPesoClasse3()+", ";
				sql += " receber_emails = "+usuario.isReceberEmails()+", ";
			
			sql += " id_usuario = "+usuario.getId()+" where id_usuario = "+usuario.getId();
			
			st.executeUpdate(sql);
			
		}		
		
		return true;
	}
	
}
