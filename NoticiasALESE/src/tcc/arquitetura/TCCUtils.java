package tcc.arquitetura;
import java.io.IOException;
import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;


public class TCCUtils {

	//Para Notebook
	public static final String DIRETORIO_WORKSPACE = "C:/Users/Luan e Daiana/Dropbox/Coisas do TCC não-compartilhadas/workspace-tcc2/";
	
	//Para PC do CPD
	//public static final String DIRETORIO_WORKSPACE = "C:/Users/sistema01/Dropbox/Coisas do TCC não-compartilhadas/workspace-tcc/";
	
	public static final String ARQUIVO_STOP_WORDS = DIRETORIO_WORKSPACE + "lista de stopwords Portugues.txt";
	
	public static final String ARQUIVO_TREINAMENTO = DIRETORIO_WORKSPACE + "classes.arff";
	
	public static final String PASTA_TREINAMENTO = DIRETORIO_WORKSPACE + "Dados/Treinamento";
	
	public static final String PASTA_TESTE = DIRETORIO_WORKSPACE + "Dados/Teste";
	
	public static final String ARQUIVO_TESTE = PASTA_TESTE + "/unclassfier/not3.txt";
	
	public static final String DIRETORIO_TEMPORARIO = DIRETORIO_WORKSPACE + "Temp/";
	
	public static String redirecionar(String pagina) throws IOException{
		
		FacesContext.getCurrentInstance().getExternalContext().redirect(pagina); 
		
		return pagina;
	}
	
	public static boolean isEmpty(Object o){
		if (o == null)
			return true;
		if (o instanceof String)
			return ((String)o).length()==0 ;
		if (o instanceof Number) {
			Number i = (Number) o;
			return (i.intValue() == 0);
		}
		return false;
	}
		
}
