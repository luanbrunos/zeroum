package tcc.arquitetura;
import tcc.dados.Conexao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MapeadorDeputado {

public static final HashMap<Integer, String> mapDeputado = new HashMap<Integer, String>();
	
	public MapeadorDeputado() throws ClassNotFoundException, SQLException{
		inicializarIndicesDeputados();
	}
	
	public void inicializarIndicesDeputados() throws ClassNotFoundException, SQLException{
		
		Connection conn = Conexao.getConexao();
		Statement st = conn.createStatement();
		String sql = "select * from deputado order by id_deputado";
		ResultSet rs = st.executeQuery(sql);
		
		while (rs.next()){
			
			int id = rs.getInt("id_deputado");
			String nome = rs.getString("nome");
			mapDeputado.put(id, nome);
			
		}
			
	}
	
	public String getDeputado(int i){
		return mapDeputado.get(i);
	}
	
	public String getDeputado(double i){
		return mapDeputado.get(new Integer((int) i));
	}
	
	public int getIndiceDeputado(String nome){
		
		if (!mapDeputado.containsValue(nome))
			return -1;
		
		Collection<String> deps = new ArrayList<String>();
		
		for (int i = 0; i < mapDeputado.size(); i++)
			deps.add(mapDeputado.get(i));
		
		int key = 0;
		for (String s: deps){
			if (s!= null && s.equals(nome))
				break;
			key++;
		}
		return key;
		
	}
	
	public Collection<String> getDeputados(){
		//mapDeputado.values() estava dado erro
		Collection<String> aux = new ArrayList<String>();
		for (int i = 0; i < mapDeputado.size(); i++)
			aux.add(mapDeputado.get(i));
		return aux;
	}
	
	public int size(){
		
		return mapDeputado.size();
		
	}
	
}
