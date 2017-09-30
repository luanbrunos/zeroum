package tcc.dados;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class QopDAO {
	
	private Connection conexao;
	
	public QopDAO() throws ClassNotFoundException, SQLException{
		
		conexao = Conexao.getConexao();
		
	}
	
	public float getRelevanciaMediaNoticia(int idNoticia) throws SQLException{
		
		Statement st = conexao.createStatement();
		String sql = "select avg(relevancia) as media from usuario_noticia  where ativa = true and id_noticia = "+idNoticia;
		ResultSet rs = st.executeQuery(sql);
		rs.next();
		
		return rs.getFloat("media");
		
	}
	
	public int getQtNoticia(int idDeputado, int idClasse) throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select count(*) as soma from noticia where id_deputado = " + idDeputado + " and id_classe = " + idClasse;
		
		ResultSet rs = st.executeQuery(sql);
		
		rs.next();
		
		return rs.getInt("soma");
				
	}
	
	public int getQtNoticiaMax(int idClasse) throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select max(quant) as maxima from ( "+
				"select id_deputado, count(id_noticia) as quant from noticia "+
				"where id_deputado > 0 and id_classe = "+idClasse+
				" group by id_deputado ) s ";
		
		ResultSet rs = st.executeQuery(sql);
		
		rs.next();
		
		return rs.getInt("maxima");
		
	}
	
	public float getMediaRelevanciaPessoal(int idDeputado, int idClasse, int idUsuario) throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select avg(un.relevancia) as media from usuario_noticia un " +
				"join noticia n using(id_noticia)" +
				" where n.id_deputado = " + idDeputado +
						" and n.id_classe = " + idClasse +
						" and un.id_usuario = " + idUsuario +
						" and un.ativa = true";
		
		ResultSet rs = st.executeQuery(sql);
		
		rs.next();
		
		return rs.getFloat("media");
		
	}
	
	public float getMediaRelevanciaGeral(int idDeputado, int idClasse) throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select avg(un.relevancia) as media from usuario_noticia un " +
				"join noticia n using(id_noticia)" +
				" where n.id_deputado = " + idDeputado +
						" and n.id_classe = " + idClasse +
						" and un.ativa = true";
		
		ResultSet rs = st.executeQuery(sql);
		
		rs.next();
		
		return rs.getFloat("media");
		
	}

}
