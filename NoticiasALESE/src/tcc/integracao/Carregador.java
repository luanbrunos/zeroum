package tcc.integracao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import tcc.arquitetura.TCCUtils;
import tcc.dominio.Noticia;
import tcc.negocio.NoticiaMBean;


//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;



public class Carregador {

	private Socket socketProxy;
	private boolean useProxy;
	private String usuarioProxy;
	private String senhaProxy;
	
	private Collection<Noticia> noticias;
	
	private static final String LINK_BASE_NOTICIA = "/agenciaalese/interna.wsp?tmp_page=";
	
	
	private NoticiaMBean noticiaMBean;
	
	public Carregador() throws ClassNotFoundException, SQLException{
		useProxy = false;
		noticiaMBean = new NoticiaMBean(new Noticia());
	}

	
	public static void main(String args[]) throws Exception{
	
		Carregador carregador = new Carregador();
		carregador.setUseProxy(false);
		
		Collection<Noticia> n = carregador.getNoticias(new Date("2013/04/10"),new Date("2013/04/15"), false);
		
	}
	
	private Date toDate(String data) throws ParseException{
		
		String soData = data.substring(0,data.indexOf(' '));
		String soHora = data.substring(data.indexOf(' ')+1,data.length());

		String dataCorrigida = soData.substring(3,6)+soData.substring(0,3)+soData.substring(6);
		
		Date dataAuxiliar = new Date(dataCorrigida);
		dataAuxiliar.setHours(Integer.parseInt(soHora.substring(0, 2)));
		dataAuxiliar.setMinutes(Integer.parseInt(soHora.substring(3)));
		
		return dataAuxiliar;
		
	}
	
	private String buscarHTML() throws UnknownHostException, IOException{
		
		Socket s = null;
		
		if(useProxy)
			s = socketProxy;
		else
			s = new Socket("www.agenciaalese.se.gov.br",80);

		OutputStream out = s.getOutputStream();
		DataOutputStream dout = new DataOutputStream(out);	
				
		String msg;
		
		if(useProxy){
			
			//Proxy proxy = new Proxy( Proxy.Type.HTTP, new InetSocketAddress("aruana.ufs.br",3128));
			
			//s = new Socket(proxy);
			String login = usuarioProxy+":"+senhaProxy;
			String encoded = Base64.encode(login);
						
			msg = "GET /agenciaalese/interna.wsp?tmp_page=maisnoticias&tmp_topico=Not%EDcias http/1.0\n"+
				"Host: www.agenciaalese.se.gov.br\n"+
				"Proxy-Authorization: Basic "+ encoded + "\n" +
				"\r\n";
		}
		else
			msg = "GET /agenciaalese/interna.wsp?tmp_page=maisnoticias&tmp_topico=Not%EDcias http/1.0\n"+
				"Host: www.agenciaalese.se.gov.br\n"+
				"\r\n";
			
			
		dout.writeBytes(msg);
		
		InputStream in = s.getInputStream();
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);
		
		String msgResp = null;
		String html = null;
		
	    while ((msgResp = br.readLine())!=null){
	    	html += msgResp+"\n";
	    }
	    
		s.close();
			
		return html;
	}
		
	public Collection<Noticia> getNoticias(Date dataInicio, Date dataFim, boolean salvar) throws Exception{
		
		Collection<Noticia> noticias = new ArrayList<Noticia>();
		
		String html = buscarHTML();
		//System.out.println(html);
		String data;
		String link;
		String titulo;
		int indexI;
		int indexF;
		
		
		Noticia aux; 
		
		while (html.indexOf("<span class=\"dataultimanoticias\">")>-1){
		
			aux = new Noticia();
			
			//pegar data
			indexI = html.indexOf("<span class=\"dataultimanoticias\">");
			indexF = html.indexOf("</span>");
			data = html.substring(indexI+33, indexF);
			//System.out.println("Data: " + data);
			html = html.substring(indexF,html.length());
			
			//pegar link
			indexI = html.indexOf("<a href=\"interna");
			indexF = html.indexOf("\">");
			link = html.substring(indexI+30, indexF);
			//System.out.println("Link: " + link);
			html = html.substring(indexF,html.length());
			
			//pegar título
			indexI = html.indexOf("\">");
			indexF = html.indexOf("</a>");
			titulo = html.substring(indexI+2, indexF);
			//System.out.println("Título: " + titulo);
			html = html.substring(indexF,html.length());
				
		
			Date dataAuxiliar = toDate(data);
			
			//Sair se todas as novas notícias foram carregadas
			if (dataAuxiliar.compareTo(dataInicio)<0)
				continue;
			
			if (dataAuxiliar.compareTo(dataFim)>0)
				continue;
			
			aux.setData(dataAuxiliar);
			aux.setTitulo(titulo);
			aux.setLink(link);
			
			aux.setTexto(buscarTextoNoticia(link));
			
			System.out.println();
			System.out.println("Nova notícia: ");
			System.out.println("Título: "+aux.getTitulo());
			System.out.println("Data: "+aux.getData());
			System.out.println("Texto: "+aux.getTexto());
			
			aux.setTexto(processarTexto(aux.getTexto()));
			aux.setTitulo(processarTexto(aux.getTitulo()));
			
			noticias.add(aux);
			
			noticiaMBean.persistirNoticiaPrimeiraVez(aux);
			
			if (salvar)
				salvarNoticia(aux);
		}
		return noticias;
	}
		
	private String processarTexto(String texto){
		
		texto = texto.replaceAll("\n\n\n\n\n", "");
		texto = texto.replaceAll("\n\n\n\n", "");
		texto = texto.replaceAll("\n\n\n", "");
		texto = texto.replaceAll("\n\n", "");
		return texto.replaceAll("'", "´");
		
	}
	
	private String buscarTextoNoticia(String link) throws UnknownHostException, IOException{
		
		Socket s = null;
		
		if(useProxy)
			s = socketProxy;
		else
			s = new Socket("www.agenciaalese.se.gov.br",80);

		OutputStream out = s.getOutputStream();
		DataOutputStream dout = new DataOutputStream(out);	
				
		String msg;
		
		System.out.println("Link: "+ LINK_BASE_NOTICIA+link);
		
		if(useProxy){
			
			if (useProxy){
				String login = usuarioProxy+":"+senhaProxy;
				String encoded = Base64.encode(login);
			}
			
			 msg = "GET / http/1.1\n"+
				"Host: "+LINK_BASE_NOTICIA+link+
				"\r\n";
		}
		else
			msg = "GET "+LINK_BASE_NOTICIA+link+" http/1.0\n"+
				"Host: www.agenciaalese.se.gov.br\n"+
				"\r\n";
			
			
		dout.writeBytes(msg);
		
		InputStream in = s.getInputStream();
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);
		
		String msgResp = null;
		String html2 = null;
		
	    while ((msgResp = br.readLine())!=null){
	    	html2 += msgResp+"\n";
	    }
	    
	    s.close();
			
		//separar somente o texto
		
		//System.out.println(html2);
		
		int indexI = html2.indexOf("<p class=\"subtitulo\">");
		int indexF = html2.indexOf("<div id=\"maisnoticias\">");
		html2 = html2.substring(indexI+21, indexF);
		
		
		//html2 = html2.substring(html2.indexOf("<P>"));
		
		
		html2 = limparHTMLTexto(html2);
		
		return html2;
		
		
	}
		
	public String limparHTMLTexto(String html){
		
		html = html.replaceAll("<.*?>","");
		
		String aux = "";
				
		//retirar "&nbsp;"
		while (html.indexOf("&nbsp;")>-1){
			
			aux = "";
			
			int index3 = html.indexOf("&nbsp;");
			aux += html.substring(0, index3);
			aux += html.substring(index3+7);
			
			html = aux;
		}
		
		
		//excluir barra
		html.replaceAll("/", " ");
		
		return aux.trim();
	}
	
	public void salvarNoticia(Noticia n) throws Exception{
		
		String diretorio = TCCUtils.PASTA_TREINAMENTO;
		
		PrintStream ps = null;
		try{
			FileOutputStream out = new FileOutputStream(diretorio+"/"+n.getTitulo()+".txt");
			ps = new PrintStream(out);
			ps.print(n.getTexto());
			ps.close();
		} catch (Exception e){
		}
		
	}
	
	public void setSocketProxy(String host, int porta) throws UnknownHostException, IOException{
		socketProxy = new Socket(host,porta);
		//useProxy = true;
	}
		
	public void setUseProxy(boolean useProxy){
		this.useProxy = useProxy; 
	}

	
	public boolean getUseProxy(){
		return useProxy;
	}

	
	public String getUsuarioProxy() {
		return usuarioProxy;
	}

	
	public void setUsuarioProxy(String usuarioProxy) {
		this.usuarioProxy = usuarioProxy;
	}

	
	public String getSenhaProxy() {
		return senhaProxy;
	}

	
	public void setSenhaProxy(String senhaProxy) {
		this.senhaProxy = senhaProxy;
	}
}
