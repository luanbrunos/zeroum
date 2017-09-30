package tcc.integracao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;

import tcc.arquitetura.Parametros;
import tcc.dados.NoticiaDAO;
import tcc.dados.ParametroDAO;
import tcc.dominio.Noticia;

public class Timer extends Thread {

	@Override
	public void run(){
		try {
			iniciarCarregamento();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void iniciarCarregamento() throws SQLException, InterruptedException{
		
		//Carregar parâmetro que define os horários de execução do Timer
		ParametroDAO parametroDAO = new ParametroDAO();
		String horas = parametroDAO.getParametro(Parametros.HORARIOS_BAIXA_NOTICIAS);
		StringTokenizer token = new StringTokenizer(horas, ",");
		Collection<Integer> horasInt = new ArrayList<Integer>();
		while (token.hasMoreTokens())
			horasInt.add(Integer.parseInt(token.nextToken()));
		
		do{
			
			if (inCollectionInt(getHoraAtual(), horasInt))
				try {
					buscarESalvarNoticiasMaisRecentes();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			//Durma por uma hora
			Thread.sleep(1000*60*60);
			
		} while (true);
		
	}
	
	/**
	 * Busca no site da ALESE e salva as novas notícias
	 * @return
	 * @throws Exception
	 */
	private void buscarESalvarNoticiasMaisRecentes() throws Exception{
		
		NoticiaDAO noticiaDao = new NoticiaDAO();
		Date dataMaisRecente = noticiaDao.findDataMaisRecente();
		
		Carregador carregador = new Carregador();
		
		Collection<Noticia> noticias = carregador.getNoticias(dataMaisRecente, new Date(), false);

	}
	
	@SuppressWarnings("deprecation")
	private Integer getHoraAtual(){
		return new Date().getHours();
	}
	
	/**
	 * Retorna se um inteiro está presente em uma coleção
	 * @param x
	 * @param col
	 * @return
	 */
	private boolean inCollectionInt(Integer x, Collection<Integer> col){
		
		for (Integer i : col)
			if (i == x) 
				return true;
		
		return false;
		
	}
	
}
