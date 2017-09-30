package tcc.dados;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

import tcc.arquitetura.Formatador;
import tcc.dominio.Noticia;

public class NoticiaDAO {

	private Connection conexao;
		
	public NoticiaDAO() throws ClassNotFoundException, SQLException{
		
		conexao = Conexao.getConexao();
		
	}
	
	public Noticia findByPrimaryKey(int key, int idUsuario) throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select n.titulo, n.texto, n.link, n.id_classe, n.data, n.id_deputado, n.id_noticia, un.relevancia, un.lida from noticia n" +
				" join usuario_noticia un using(id_noticia) " +
				"where n.id_noticia = "+key+" and un.id_usuario = "+idUsuario;
		
		ResultSet rs = st.executeQuery(sql);
		
		if (rs.next()){
			return encarnarNoticia(st.executeQuery(sql));
		}
		
		//Se não houver resultados, estão a notícia ainda não foi lida. Pegar sem fazer join com a tabela
		sql = "select n.titulo, n.texto, n.link, n.id_classe, n.data, n.id_deputado, n.id_noticia, 3 as relevancia, false as lida from noticia n" +
				" where n.id_noticia = "+key;
		
		rs = st.executeQuery(sql);
		
		return encarnarNoticia(rs);
		
		
	}
	
	public Collection<Noticia> findByDeputado(int idDeputado, int idUsuario) throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select n.titulo, n.texto, n.link, n.id_classe, n.data, n.id_deputado, n.id_noticia, un.relevancia, un.lida" +
				" from noticia n " +
				"join usuario_noticia un using(id_usuario) where n.id_deputado = "+idDeputado+" and un.id_usuario ="+idUsuario;
		
		ResultSet rs = st.executeQuery(sql);
		
		return encarnarNoticias(rs);
		
	}

	public Collection<Noticia> findAllNaoLida(int idUsuario) throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select n.titulo, n.texto, n.link, n.id_classe, n.data, n.id_deputado, n.id_noticia, 3 as relevancia, false as lida" +
				" from noticia n where id_noticia not in (select id_noticia from usuario_noticia where ativa = true and lida = true and id_usuario = "+
				idUsuario + ") order by n.data desc";
		
		ResultSet rs = st.executeQuery(sql);
		
		return encarnarNoticias(rs);
		
	}
	
	public Collection<Noticia> findByClasse(int idClasse, int idUsuario) throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select n.titulo, n.texto, n.link, n.id_classe, n.data, n.id_deputado, n.id_noticia, un.relevancia, un.lida" +
				" from noticia n join usuario_noticia un using(id_usuario) where id_classe = "+idClasse+" and un.id_usuario = "+idUsuario;
		
		ResultSet rs = st.executeQuery(sql);
		
		return encarnarNoticias(rs);
		
	}

	public Noticia findMaisRecente(int idUsuario) throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select n.titulo, n.texto, n.link, n.id_classe, n.data, n.id_deputado, n.id_noticia, un.relevancia, un.lida" +
				" from noticia n " +
				"join usuario_noticia un using(id_usuario) " +
				"where data in (select max(data) as data from noticia) and un.id_usuario = "+idUsuario;
		
		ResultSet rs = st.executeQuery(sql);
		
		return encarnarNoticia(rs);
		
	}
	
	/**
	 * Retorna a data da notícia mais recente
	 * @return
	 * @throws SQLException 
	 */
	public Date findDataMaisRecente() throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select n.data " +
				" from noticia n " +
				" order by n.data desc limit 1";
		
		ResultSet rs = st.executeQuery(sql);
		
		Date dataUltima = new Date();
		while (rs.next()){
			dataUltima = rs.getDate("data");
			break;
		}
		
		return dataUltima;
		
	}
	
	public Collection<Noticia> buscar(Integer idDeputado, String titulo, String texto, Integer idClasse, Boolean lida, Date dataInicio, Date dataFim, Integer relevanciaInicio, Integer relevanciaFim, int idUsuario) throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select n.*, un.lida, un.relevancia from noticia n " +
					"join deputado d using(id_deputado) " +
					"join classe c using(id_classe) " +
					"join usuario_noticia un using(id_noticia) where ";
		
		if ((idDeputado != null ) && ( idDeputado >= 0 ))
			sql += " d.id_deputado = "+idDeputado+" and ";
		
		if (titulo != null)
			sql += " n.titulo ilike('%"+titulo+"%') and ";
		
		if (texto != null)
			sql += " n.texto ilike('%"+texto+"%') and ";
		
		if ((idClasse != null ) && ( idClasse >= 0 ))
			sql += " n.id_classe = "+idClasse+" and ";
		
		if (lida != null)
			sql += " un.lida = "+lida.booleanValue()+" and ";
		
		String dataInicioString;
		if (dataInicio == null) 
			dataInicioString = "01/01/1900";
		else
			dataInicioString = Formatador.formatarData(dataInicio);
		
		if (dataFim == null) 
			dataFim = new Date();
		
		sql += " n.data between '"+dataInicioString+"' and '"+Formatador.formatarData(dataFim)+"' and ";
		
		if ((relevanciaInicio != null) && (relevanciaFim != null))
			sql += " un.relevancia between "+relevanciaInicio+" and "+relevanciaFim+" and ";
		
		sql += " un.id_usuario = "+idUsuario+" order by data";
		
		
		ResultSet rs = st.executeQuery(sql);
		
		return encarnarNoticias(rs);
		
		
	}
	
	public boolean persistirNoticia(Noticia noticia, int idUsuario) throws SQLException{
		
		Statement st = conexao.createStatement();
		String sql;
		
		if(noticia.getIdNoticia() == 0){
			
			sql = "insert into noticia(id_noticia";
			if (noticia.getClasse() != 0)
				sql += ", id_classe";
			if (noticia.getData() != null)
				sql += ", data";
			if (noticia.getIdDeputado() != 0)
				sql += ", id_deputado";
			if (noticia.getLink() != null)
				sql += ", link";
			//if (noticia.getRelevancia() != 0)
			//	sql += ", relevancia";
			if (noticia.getTexto() != null)
				sql += ", texto";
			if (noticia.getTitulo() != null)
				sql += ", titulo";
			
			sql += ") values (";
			
			int maxIdNoticia = findMaxIdNoticia();
			noticia.setIdNoticia(maxIdNoticia+1);
			sql += (maxIdNoticia + 1)+" ";
			
			if (noticia.getClasse() != 0)
				sql += ", "+noticia.getClasse();
			if (noticia.getData() != null)
				sql += ", '"+noticia.getData()+"' ";
			if (noticia.getIdDeputado() != 0)
				sql += ", "+noticia.getIdDeputado();
			if (noticia.getLink() != null)
				sql += ", '"+noticia.getLink()+"' ";
			if (noticia.getTexto() != null)
				sql += ", '"+noticia.getTexto()+"' ";
			if (noticia.getTitulo() != null)
				sql += ", '"+noticia.getTitulo()+"' ";
			
			
			sql += ")";
			
			

			
			st.executeUpdate(sql);
						
					
			
		}
		else{
			
			sql = "update noticia set ";
			if (noticia.getClasse() != 0)
				sql += " id_classe = "+noticia.getClasse()+", ";
			if (noticia.getData() != null)
				sql += " data = '"+noticia.getData()+"', ";
			if (noticia.getIdDeputado() != 0)
				sql += "id_deputado = "+noticia.getIdDeputado()+", ";
			if (noticia.getLink() != null)
				sql += "link =  '"+noticia.getLink()+"', ";
			if (noticia.getTexto() != null)
				sql += "texto = '"+noticia.getTexto()+"', ";
			if (noticia.getTitulo() != null)
				sql += "titulo = '"+noticia.getTitulo()+"', ";
			
			if (noticia.isLida()==true)
				sql+= "id_noticia = "+noticia.getIdNoticia()+" where id_noticia = "+noticia.getIdNoticia();
			else
				sql+= "id_noticia = "+noticia.getIdNoticia()+" where id_noticia = "+noticia.getIdNoticia();
			
			
			
			st.executeUpdate(sql);
			
		}
		
		
		//inserir na tabela usuario_noticia
		
		sql = "select * from usuario_noticia where id_noticia = "+noticia.getIdNoticia()+" and id_usuario = "+idUsuario;
		ResultSet rs2 = st.executeQuery(sql);
		
		if (rs2.next()){
			
			sql = "update usuario_noticia set ";
			sql += " relevancia = "+noticia.getRelevancia();
			sql += ", lida = "+noticia.isLida()+", ativa = true ";
			sql += " where id_usuario = "+idUsuario+" and id_noticia = "+noticia.getIdNoticia();
			
		}else{
			
			sql = "insert into usuario_noticia( id_usuario_noticia, id_noticia, id_usuario, lida, ativa, relevancia ";
			sql += ") values ("+(findMaxIdUsuarioNoticia()+1)+", "+noticia.getIdNoticia()+", "+idUsuario+", false, true, 1 ";

			
			sql += ")";
			
		}
		
		st.executeUpdate(sql);
		
		return true;
				
	}

	private int findMaxIdNoticia() throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select max(id_noticia) as max from noticia";
		
		ResultSet rs = st.executeQuery(sql);
		
		rs.next();
		return rs.getInt("max");
		
	} 
	
	private int findMaxIdUsuarioNoticia() throws SQLException{
		
		Statement st = conexao.createStatement();
		
		String sql = "select max(id_usuario_noticia) as max from usuario_noticia";
		
		ResultSet rs = st.executeQuery(sql);
		
		rs.next();
		return rs.getInt("max");
		
	} 
	
	
	private Noticia encarnarNoticia(ResultSet rs) throws SQLException{
		
		Noticia n = new Noticia();
		
		while (rs.next()){
			
			n.setClasse(rs.getInt("id_classe"));
			n.setData(rs.getDate("data"));
			n.setIdDeputado(rs.getInt("id_deputado"));
			n.setIdNoticia(rs.getInt("id_noticia"));
			n.setLink(rs.getString("link"));
			n.setRelevancia(rs.getInt("relevancia"));
			n.setTexto(rs.getString("texto"));
			n.setTitulo(rs.getString("titulo"));
			n.setLida(rs.getBoolean("lida"));
			break;
			
		}
		
		return n;
		
	}
	
	private Collection<Noticia> encarnarNoticias(ResultSet rs) throws SQLException{
		
		Collection<Noticia> noticias = new ArrayList<Noticia>();
		
		while (rs.next()){
			
			Noticia n = new Noticia();
			n.setClasse(rs.getInt("id_classe"));
			n.setData(rs.getDate("data"));
			n.setIdDeputado(rs.getInt("id_deputado"));
			n.setIdNoticia(rs.getInt("id_noticia"));
			n.setLink(rs.getString("link"));
			n.setRelevancia(rs.getInt("relevancia"));
			n.setTexto(rs.getString("texto"));
			n.setTitulo(rs.getString("titulo"));
			n.setLida(rs.getBoolean("lida"));
			
			noticias.add(n);
			
		}
		
		return noticias;
		
	}
	
	
	//método para teste
	public void gerarRelevanciasAleatorias(int idUsuario) throws SQLException{
		
		Statement st = conexao.createStatement();
		
		Collection<Noticia> noticias = findAllNaoLida(idUsuario);
		int relevancia;
		Random entrada = new Random();
		
		for (Noticia n : noticias){
			
		    relevancia = entrada.nextInt(6);
			String sql = "insert into usuario_noticia values ("+(findMaxIdUsuarioNoticia()+1)+", "+n.getIdNoticia()+", "+idUsuario+", "+relevancia+", true, true)";
			
			st.executeUpdate(sql);
			
		}
				
	}
	
	//método para teste
	public static void main(String args[]) throws ClassNotFoundException, SQLException{
		
		NoticiaDAO noticiaDao = new NoticiaDAO();
		noticiaDao.gerarRelevanciasAleatorias(12);
		
	}
	
	
}
