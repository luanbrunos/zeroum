package tcc.textmining;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.StringTokenizer;

import tcc.arquitetura.MapeadorDeputado;
import tcc.arquitetura.TCCUtils;

public class SeletorDeputado {

	private Collection<String> deputados;
	private MapeadorDeputado mapDeputado;

	public static void main(String args[]) throws ClassNotFoundException,
			SQLException {

		SeletorDeputado seletor = new SeletorDeputado();
		String base = TCCUtils.DIRETORIO_WORKSPACE + "Dados/Teste";
		base = "C:/Users/Luan e Daiana/Dropbox/Coisas do TCC não-compartilhadas/workspace-tcc2/Dados/Teste";
		String titulo;
		File fileBase = new File(base);
		File lista[] = fileBase.listFiles();

		for (int i = 0; i < lista.length; i++) {
			titulo = lista[i].getName();
			System.out.println(seletor.buscarNomeDeputado(titulo) + " -> "
					+ titulo);
		}

	}

	public SeletorDeputado() throws ClassNotFoundException, SQLException {

		mapDeputado = new MapeadorDeputado();
		this.deputados = mapDeputado.getDeputados();

	}

	public String buscarNomeDeputado(String titulo) {

		return mapDeputado.getDeputado(buscarCodigoDeputado(titulo));

	}

	public int buscarCodigoDeputado(String titulo) {

		String deputado = null;

		try {
			// filtrar
			titulo = retirarAspas(titulo);
			titulo = retirarPalavrasMinusculas(titulo);
			titulo = retirarCaracteresEspeciais(titulo);

			// buscar deputado
			StringTokenizer tokenizer;

			tokenizer = new StringTokenizer(titulo);

			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				
				for (String d : deputados) {
					
					if (d == null)
						continue;
					if (d.indexOf(token) >= 0) {
						deputado = d;
						break;
					}
				}

				if (deputado != null)
					break;

			}
		} catch (Exception e) {
			return -1;
		}

		return mapDeputado.getIndiceDeputado(deputado);

	}

	private String retirarCaracteresEspeciais(String titulo){
		
		titulo = titulo.replace(',', ' ');
		titulo = titulo.replace('.', ' ');
		titulo = titulo.replace(':', ' ');
		titulo = titulo.replace(';', ' ');
		
		return titulo;
		
	}
	
	private String retirarAspas(String string) {

		int indice1, indice2;

		indice1 = string.indexOf("“");
		if (indice1 > 0)
			string = string.substring(0, indice1);

		indice2 = string.indexOf("”");
		if (indice2 < string.length())
			string = string.substring(indice2 + 1);

		return string;

	}

	private String retirarPalavrasMinusculas(String string) {

		StringTokenizer tokenizer = new StringTokenizer(string);

		String aux = "";
		string = "";

		while (tokenizer.hasMoreTokens()) {
			aux = tokenizer.nextToken();
			if (Character.isUpperCase(aux.charAt(0)))
				string += aux + " ";
		}

		return string;
	}

}
