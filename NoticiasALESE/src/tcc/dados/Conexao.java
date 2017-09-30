package tcc.dados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgresql.Driver;

import tcc.dominio.Noticia;

public class Conexao {
	
	private static final String DRIVER = "org.postgresql.Driver";
	
	private static final String BANCO = "jdbc:postgresql://localhost:5432/zeroum";
		
	
	public static Connection getConexao() throws ClassNotFoundException, SQLException{
		
	    Class.forName(DRIVER);
	         
		Connection c = DriverManager.getConnection(BANCO,"postgres","123456");
		
		return c;
		
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		
		/*Connection c = getConexao();
		
		Statement st = c.createStatement();
		
		String sql = "select * from deputado";
		
		ResultSet rs = st.executeQuery(sql);
		
		while (rs.next()){
			
			System.out.println(rs.getString("nome"));
			
		}*/
		
        Noticia n = new Noticia();
		
	    n.setTitulo("Titulo teste");
	    n.setRelevancia(7);
	    
	    NoticiaDAO noticiaDao = new NoticiaDAO();
	    
	    noticiaDao.persistirNoticia(n,1);
	    
	    n.setLink("link");
	    
	    noticiaDao.persistirNoticia(n,1);
		
		
	}

}
