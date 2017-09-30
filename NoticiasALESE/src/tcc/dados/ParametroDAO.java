package tcc.dados;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class ParametroDAO {

	private Connection conexao;
	
	public ParametroDAO() {
		
		try {
			conexao = Conexao.getConexao();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Retorna um parâmetro pelo seu id
	 * @param idParametro
	 * @return
	 * @throws SQLException 
	 */
	public String getParametro(Integer idParametro) throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select valor " +
				" from parametro " +
				" where id_parametro =  "+idParametro;
		
		ResultSet rs = st.executeQuery(sql);
		
		String valor = null;
		while (rs.next()){
			valor = rs.getString("valor");
			break;
		}
		
		return valor;
		
	}
	
}
