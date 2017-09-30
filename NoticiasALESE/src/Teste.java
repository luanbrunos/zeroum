import java.io.File;
import java.util.Scanner;

import ptstemmer.support.stopwords.StopWordsFromFile;
import tcc.arquitetura.MapeadorClasses;
import tcc.arquitetura.TCCUtils;
import tcc.ptstemmer.OrengoStemmer;
import tcc.textmining.Classificador;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Teste {

	static Classifier classif = new NaiveBayesMultinomial();

	static int MIN_TERM_FREQ = 0;
	static boolean SET_IDF_TRANSFORM = true;

	// static Tokenizer token = new NGramTokenizer();

	// teste
	// static int NUM_FOLDS = 5;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// testeInicial();

		textDirectoryLoaderTeste();

		// Classificador.main(null);

		// Persistence.

	}

	public static int textDirectoryLoaderTeste() throws Exception {

		MapeadorClasses mapClass = new MapeadorClasses();

		// Criar a classe TextDirectoryLoader e setar o diretório de conjunto de
		// treinamento
		TextDirectoryLoader tdl = new TextDirectoryLoader();
		tdl.setDirectory(new File(TCCUtils.PASTA_TREINAMENTO));

		// Buscar Instâncias
		Instances treinamento = tdl.getDataSet();
		treinamento.setClass(treinamento.attribute(1));
		
		// Filtrar instâncias
		StringToWordVector filter = montarFiltro(treinamento);
		Instances treinamentoFiltrado = Filter.useFilter(treinamento, filter);
		
				
		
		Instances treinamento2 = carregarDadosTreinamento();
		StringToWordVector filter2 = montarFiltro(treinamento2);
		Instances treinamentoFiltrado2 = Filter.useFilter(treinamento2, filter2);
		

		// CONJUNTO DE TREINAMENTO PRONTO

		// Instanciar e criar classificador

		// Classifier classificador = classif;
		// classificador.buildClassifier(treinamentoFiltrado);

		Classificador c = new Classificador();
		Classifier classificador = c.getClassificador();

		// CLASSIFICADOR PRONTO!!!!

		// I-n-i-c-i-a-r t-e-s-t-e

		TextDirectoryLoader td2 = new TextDirectoryLoader();
		td2.setDirectory(new File(TCCUtils.DIRETORIO_TEMPORARIO));
		Instances teste = td2.getDataSet();

		teste.setClass(teste.attribute(1));
	
		Instances testeFiltrado = Filter.useFilter(teste, c.getFiltro());

		System.out.println("Número de atributos do conjunto de teste: "
				+ testeFiltrado.numAttributes());
		System.out.println("Número de instâncias: "
				+ testeFiltrado.numInstances());

		Instance instanciaTeste;

		double numClasse;

		instanciaTeste = testeFiltrado.get(0);

		numClasse = classificador.classifyInstance(instanciaTeste);

		System.out.println("Para documento " + (0) + ", Classe: "
				+ mapClass.getClasse(numClasse));
		
		return (int)numClasse;

	}

	private static Instances carregarDadosTreinamento() throws Exception{	
		//Buscar Instâncias
		ArffLoader arffLoader = new ArffLoader();
		File arquivoDados = new File(TCCUtils.ARQUIVO_TREINAMENTO);
		arffLoader.setFile(arquivoDados);
	
		Instances treinamento = arffLoader.getDataSet();
		treinamento.setClass(treinamento.attribute(0));
		
		
		return treinamento;
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

	private static StringToWordVector montarFiltro(Instances treinamento)
			throws Exception {
		// Criar Stemmer em português e ignorar StopWords
		OrengoStemmer stemmer = new OrengoStemmer();
		StopWordsFromFile stopWords = new StopWordsFromFile(
				TCCUtils.ARQUIVO_STOP_WORDS);
		stemmer.ignoreStopWords(stopWords);
		stemmer.setIgnoreNumeros(true);

		// Criar filtro e seta-lo Stemmer e lista de StopWords
		StringToWordVector filter = new StringToWordVector();
		filter.setLowerCaseTokens(true);
		filter.setStemmer(stemmer);
		filter.setStopwords(new File(TCCUtils.ARQUIVO_STOP_WORDS));
		filter.setIDFTransform(SET_IDF_TRANSFORM);
		filter.setOutputWordCounts(true);
		filter.setMinTermFreq(MIN_TERM_FREQ);

		// filter.setOutputWordCounts(true);

		// Filtrar instâncias
		filter.setInputFormat(treinamento);

		return filter;
	}

	public static void testeInicial() {

		Scanner entradaScanner = new Scanner(System.in);

		StopWordsFromFile stopWords = new StopWordsFromFile(
				TCCUtils.ARQUIVO_STOP_WORDS);

		OrengoStemmer os = new OrengoStemmer();

		// os.ignoreStopWords(stopWords);

		while (true) {
			String aux = entradaScanner.next();
			System.out.println(os.stemming(aux));
			if (aux.equals("exit"))
				break;
		}

		StringToWordVector stwv = new StringToWordVector();
		stwv.setStemmer(os);
		stwv.setStopwords(new File(TCCUtils.ARQUIVO_STOP_WORDS));

		// até aqui tá rodando

		stwv.setSelectedRange("Amar é fogo, amor é fogaréu");

		stwv.setOutputWordCounts(true);
		stwv.setUseStoplist(true);
		stwv.setIDFTransform(true);

		Instances inst = stwv.getOutputFormat();

		Instance i0 = inst.get(0);
		Instance i1 = inst.get(1);

		System.out.println(i0.stringValue(0) + " " + i0.classValue());
		System.out.println(i1.stringValue(0) + " " + i1.classValue());

	}

}
