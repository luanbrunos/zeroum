package tcc.ptstemmer;

/**
 * PTStemmer - Java Stemming toolkit for the Portuguese language (C) 2008 Pedro Oliveira
 * 
 * This file is part of PTStemmer.
 * PTStemmer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * PTStemmer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with PTStemmer. If not, see <http://www.gnu.org/licenses/>.
 * 
 */


import ptstemmer.support.datastructures.LRUCache;
import ptstemmer.support.namedentities.NamedEntitiesList;
import ptstemmer.support.stopwords.StopWordList;
import tcc.ptstemmer.OrengoStemmer;
import tcc.ptstemmer.PorterStemmer;
import weka.core.stemmers.*;

/**
 * Abstract class that provides the main features to all the stemmers
 * @author Pedro Oliveira
 *
 */
public abstract class StemmerProprio implements Stemmer{

	private boolean cacheStems;
	private LRUCache lruCache;

	private boolean ignoreNamedEntities;
	private NamedEntitiesList namedEntities;

	private boolean ignoreStopWords;
	private StopWordList stopWords;
	
	protected boolean ignoreNumeros;

	public enum StemmerType{ORENGO, PORTER};

	/**
	 * Factory to stemmer construction
	 * @param stype
	 * @return
	 */
	public static Stemmer StemmerFactory(StemmerType stype)
	{
		if(stype == StemmerType.ORENGO)
			return new OrengoStemmer();
		else
			return new PorterStemmer();
	}

	/**
	 * Create a LRU Cache, caching the last <code>size</code> stems
	 * @param size
	 */
	public void enableCaching(int size)
	{
		cacheStems = true;
		lruCache = new LRUCache(size);
	}

	/**
	 * Disable and deletes the LRU Cache
	 */
	public void disableCaching()
	{
		cacheStems = false;
		lruCache = null;
	}

	/**
	 * Check if LRU Cache is enabled
	 * @return
	 */
	public boolean isCachingEnabled()
	{
		return cacheStems;
	}

	/**
	 * Ignore all the named entities present in <code>list</code>
	 * @param list
	 */
	public void ignoreNamedEntities(NamedEntitiesList list)
	{
		ignoreNamedEntities = true;
		namedEntities = list;
	}

	/**
	 * Disable named entity recognition
	 */
	public void allowNamedEntities()
	{
		ignoreNamedEntities = false;
		namedEntities = null;
	}

	/**
	 * Check if named entity recognition is enabled
	 * @return
	 */
	public boolean isNamedEntitiesIgnored()
	{
		return ignoreNamedEntities;
	}

	/**
	 * Ignore all the stopwords present in <code>list</code>
	 * @param list
	 */
	public void ignoreStopWords(StopWordList list)
	{
		ignoreStopWords = true;
		stopWords = list;
	}

	/**
	 * Disable stopword recognition
	 */
	public void allowStopWords()
	{
		ignoreStopWords = false;
		stopWords = null;
	}

	/**
	 * Check if stopword recognition is enabled
	 * @return
	 */
	public boolean isStopWordsIgnored()
	{
		return ignoreStopWords;
	}

	/**
	 * Performs stemming on the <code>phrase</code>, using a simple space tokenizer
	 * @param phrase
	 * @return
	 */
	public String[] phraseStemming(String phrase)
	{
		String[] res = phrase.split(" ");
		for(int i=0; i<res.length; i++)
			res[i] = wordStemming(res[i]);
		return res;
	}

	/**
	 * Performs stemming on the <code>word</code>
	 * @param word
	 * @return
	 */
	public String wordStemming(String word)
	{
		String res;
		if(word.contains(" "))
			return word;
		else
		{
			word = word.trim().toLowerCase();
			
			if(cacheStems)
				if(lruCache.containsKey(word))
					return lruCache.get(word);

			if(ignoreNamedEntities)
				if(namedEntities.isNamedEntity(word))
					return word;

			if(ignoreStopWords)
				if(stopWords.isStopWord(word))
					return word;

			res = stemming(word);

			if(cacheStems)
				lruCache.put(word, res);
			return res;
		}
	}


	public void setIgnoreNumeros(boolean ig){
		ignoreNumeros = ig;
	}
	
	protected boolean isNumero(char c){
		if ((c=='0')||(c=='1')||(c=='2')||(c=='3')||(c=='4')||(c=='5')||(c=='6')||(c=='7')||(c=='8')||(c=='9'))
			return true;
		return false;
	}
	
	protected abstract String stemming(String word);
}
