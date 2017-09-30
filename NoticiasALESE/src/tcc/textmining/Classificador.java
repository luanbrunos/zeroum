package tcc.textmining;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;

import org.hibernate.engine.jdbc.ReaderInputStream;

import ptstemmer.support.stopwords.StopWordsFromFile;
import tcc.arquitetura.MapeadorClasses;
import tcc.arquitetura.TCCUtils;
import tcc.ptstemmer.OrengoStemmer;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Classificador {

	static int MIN_TERM_FREQ = 0;
	static boolean SET_IDF_TRANSFORM = true;
	
	static Classifier classificador = new NaiveBayesMultinomial();
	
	static Instances treinamento;
	
	private StringToWordVector filter;
	
	public static void main(String args[]) throws Exception{
		
		gerarArquivoDeTreinamento();
		
		////Classificador c = new Classificador();
		//MapeadorClasses map = new MapeadorClasses();
		//System.out.println(map.getClasse(c.classificar("")));
		
	}
	
	public Classificador() throws Exception{
		
		treinamento = carregarDadosTreinamento();
		
		montarFiltro(treinamento);
		
		Instances treinamentoFiltrado = Filter.useFilter(treinamento, filter);
		
		classificador.buildClassifier(treinamentoFiltrado);
		
	}
	
	
	public int classificar(String texto) throws Exception{
		
		//escrever texto no arquivo
		FileOutputStream out = new FileOutputStream(new File(TCCUtils.DIRETORIO_TEMPORARIO + "/Temp/temp.txt"));
		out.write(texto.getBytes());
		
		//Criar a classe TextDirectoryLoader e setar o diretório de conjunto de treinamento
		TextDirectoryLoader tdl = new TextDirectoryLoader(); 
		tdl.setDirectory(new File(TCCUtils.DIRETORIO_TEMPORARIO));
		
		//Buscar instância a partir do arquivo
		Instances instances = tdl.getDataSet();
		instances.setClass(instances.attribute(1));
		
		//Montar e aplicar o filtro
		//montarFiltro(treinamento);
		Instances testeFiltrado = Filter.useFilter(instances, filter);

		//Buscar primeira instância
		Instance i = testeFiltrado.get(0);
		
		double numClasse = classificar(i);
		
		return (int)numClasse;
		
	}
	
	public double classificar(Instance i) throws Exception{
		return classificador.classifyInstance(i);
	}
	
	private Instances carregarDadosTreinamento() throws Exception{	
		//Buscar Instâncias
		ArffLoader arffLoader = new ArffLoader();
		File arquivoDados = new File(TCCUtils.ARQUIVO_TREINAMENTO);
		arffLoader.setFile(arquivoDados);
	
		Instances treinamento = arffLoader.getDataSet();
		treinamento.setClass(treinamento.attribute(1));
		
		
		return treinamento;
	}

	private void montarFiltro(Instances treinamento) throws Exception{
		//Criar Stemmer em português e ignorar StopWords
		OrengoStemmer stemmer = new OrengoStemmer();
		StopWordsFromFile stopWords = new StopWordsFromFile(TCCUtils.ARQUIVO_STOP_WORDS);
		stemmer.ignoreStopWords(stopWords);
		stemmer.setIgnoreNumeros(true);
				
		//Criar filtro e seta-lo Stemmer e lista de StopWords
		filter = new StringToWordVector();
		filter.setLowerCaseTokens(true);
		filter.setStemmer(stemmer);
		filter.setStopwords(new File(TCCUtils.ARQUIVO_STOP_WORDS));
		filter.setIDFTransform(SET_IDF_TRANSFORM);
		filter.setOutputWordCounts(true);
		filter.setMinTermFreq(MIN_TERM_FREQ); 
		
		//filter.setOutputWordCounts(true);
							
		//Filtrar instâncias
	    filter.setInputFormat(treinamento);
	    
	}

	private static void gerarArquivoDeTreinamento() throws Exception {

		// Criar a classe TextDirectoryLoader e setar o diretório de conjunto de
		// treinamento
		TextDirectoryLoader tdl = new TextDirectoryLoader();
		tdl.setDirectory(new File(TCCUtils.PASTA_TREINAMENTO));

		// Buscar Instâncias
		Instances treinamento = tdl.getDataSet();
		treinamento.setClass(treinamento.attribute(1));

		// Filtrar instâncias
	//	StringToWordVector filter = montarFiltro(treinamento);

	//	Instances treinamentoFiltrado = Filter.useFilter(treinamento, filter);

		// salvar arff
		ArffSaver saverArff = new ArffSaver();
		File arquivoDados = new File(TCCUtils.ARQUIVO_TREINAMENTO);
		saverArff.setFile(arquivoDados);
		saverArff.setInstances(treinamento);
		saverArff.writeBatch();

	}
	
	public Classifier getClassificador(){
		return classificador;
	}
	
	public StringToWordVector getFiltro(){
		return filter;
	}
	
}
