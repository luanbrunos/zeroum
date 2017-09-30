package tcc.arquitetura;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;

import tcc.dados.Conexao;


public class MapeadorClasses {
	
	//Cria o Map das classes e seus índices
	public static final HashMap<Integer, String> mapClasses = new HashMap<Integer, String>();
	
	public MapeadorClasses() throws SQLException, ClassNotFoundException{
		inicializarIndicesClasses();
	}
	
	public void inicializarIndicesClasses() throws SQLException, ClassNotFoundException{
		
		Connection conn = Conexao.getConexao();
		Statement st = conn.createStatement();
		String sql = "select * from classe";
		ResultSet rs = st.executeQuery(sql);
		
		while (rs.next()){
			
			int id = rs.getInt("id_classe");
			String nome = rs.getString("titulo");
			mapClasses.put(id, nome);
			
		}
		
	}
	
	public String getClasse(int i){
		return mapClasses.get(i);
	}
	
	public String getClasse(double i){
		return mapClasses.get(new Integer((int) i));
	}
	
	public Collection<String> getClasses(){
		return mapClasses.values();
	}
	
	public int size(){
		
		return mapClasses.size();
		
	}

}
