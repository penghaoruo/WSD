package cs446;
// code borrowed from cogcomp
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import edu.illinois.cs.cogcomp.core.datastructures.Pair;
import edu.cmu.lti.lexical_db.*;
import edu.cmu.lti.lexical_db.data.Concept;
import edu.cmu.lti.ws4j.*;
import edu.cmu.lti.ws4j.impl.*;
import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IPointer;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import edu.mit.jwi.item.SenseEntry;
import edu.mit.jwi.item.Synset;
import edu.mit.jwi.morph.WordnetStemmer;

public class WNWrapper {

	public IRAMDictionary dict = null;
	public WordnetStemmer wstem = null;
	public static ILexicalDatabase db =null;
	
	private LeacockChodorow leacockchodorow;
	private Lesk lesk;
	private WuPalmer wupalmer;
	private Resnik resnik;
	private Lin lin;
	private JiangConrath jiangconrath;
	private HirstStOnge hirststonge;
	private Path path;
	
	public static final int maxSense = 0;
	public static final int maxStem = 0;
	public static final double pTHRESH = 0.2;
	
	public static HashMap<Pair<String,String>, String> mapsense = new HashMap<Pair<String,String>, String>();
	//public static HashMap<String , String> mapsensereverse = new HashMap<String , String>();
	public static HashMap<String , ArrayList<ArrayList<String>>> mapcluster = new HashMap<String , ArrayList<ArrayList<String>>>();
	
	public static void main(String[] args){
	    
	    WNWrapper rap = new WNWrapper("data/WordNet-3.0/dict");
	    /*
	    System.out.println(rap.getStemsList("computation"));
	    System.out.println(rap.getStemsList("computer",POS.NOUN));
	    System.out.println(rap.getStemsList("computing",POS.VERB));
	    System.out.println(rap.getStemsList("mice",POS.NOUN));
	    System.out.println(rap.getStemsList("countries",POS.NOUN));
	    System.out.println(rap.getStemsList("killed",POS.VERB));
	    System.out.println(rap.getStemsList("released",POS.VERB));
	    rap.getSynsetId("editorial");
	    rap.printSynsets("editorial");
	    
	    ArrayList<ISynset> s=rap.getAllSynset("large", POS.ADJECTIVE);
	    ISynset ss=s.get(0);
	    for (int i=0;i<1;i++) {
	    	System.out.println(s.get(i).getID());
	    	System.out.println(s.get(i).getGloss());
	    	System.out.println(s.get(i).getLexicalFile().getDescription());
	    }
	   
	    Map<IPointer,List<ISynsetID>> smap=ss.getRelatedMap();
	    Iterator it = smap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	    */
	    
	    /*
	    List<Concept> s=(List<Concept>) db.getAllConcepts("good", "n");
	    for (int i=0;i<s.size();i++) {
	    	Concept c=s.get(i);
	    	System.out.println(c.getSynset());
	    }
	    */
	    
	    /*
	    System.out.println(rap.getClusterRange("gray"));
	    System.out.println(mapcluster.get("gray"));
	    
	    int ss=rap.getClusterRange("good");
	    System.out.println(ss);
	    for (int i=0;i<ss;i++) {
	    	ArrayList<Concept> cs=rap.getAllSynsetsFromCluster("good", "a", i);
	    	if (cs.size()==0) System.out.print("Null");
	    	for (int j=0;j<cs.size();j++) {
	    		System.out.print(cs.get(j).getSynset()+" ");
	    	}
	    	System.out.println();
	    }
	    */
	}
	
	Concept getConcept(AmbWord w, int index) {
		List<Concept> s=(List<Concept>) db.getAllConcepts(w.getLemma(), w.getPos());
		return s.get(index);
	}
	
	public double dependency(AmbWord w1, AmbWord w2, int index1, int index2, int flag) {
		RelatednessCalculator rc=null;
		switch(flag) {
			case 1: {rc=leacockchodorow; break;}
			case 2: {rc=lesk; break;}
			case 3: {rc=wupalmer; break;}
			case 4: {rc=resnik; break;}
			case 5: {rc=lin; break;}
			case 6: {rc=jiangconrath; break;}
			case 7: {rc=hirststonge; break;}
			case 8: {rc=path; break;}
			default: rc=null;
		}
		/*
		List<Concept> synset1 = getAllSynsetsFromCluster(w1.getLemma(),w1.getPos(),index1);
		List<Concept> synset2 = getAllSynsetsFromCluster(w2.getLemma(),w2.getPos(),index2);
		double avg = 0.0;
		int count = 0;
		if(synset1.isEmpty() || synset2.isEmpty())
			return Double.POSITIVE_INFINITY;
		for(Concept s1: synset1)
			for(Concept s2: synset2)
			{
				Relatedness res=rc.calcRelatednessOfSynset(s1, s2);
				avg+=res.getScore();
				count++;
			}
        return avg/count;
        */
		ArrayList<Concept> sl1=(ArrayList<Concept>) db.getAllConcepts(w1.getLemma(), w1.getPos());
		Concept s1=sl1.get(index1);
		ArrayList<Concept> sl2=(ArrayList<Concept>) db.getAllConcepts(w2.getLemma(), w2.getPos());
		Concept s2=sl2.get(index2);
		Relatedness res=rc.calcRelatednessOfSynset(s1, s2);
        return res.getScore();
	}

	// Constructor
    public WNWrapper(String wordnetPath) {
    	db = new NictWordNet();
    	leacockchodorow =new LeacockChodorow(db);
    	lesk=new Lesk(db);
    	wupalmer=new WuPalmer(db);
    	resnik=new Resnik(db);
    	lin=new Lin(db);
    	jiangconrath=new JiangConrath(db);
    	hirststonge=new HirstStOnge(db);
    	path=new Path(db);
    	/*
        String wnhome = System.getenv("WNHOME");
        if (wnhome == null)
            wnhome = wordnetPath;
        String wnpath = wnhome;
        try {
            System.out.println("Creating wordnet dictionary from "+wnpath+"...");
            dict = new RAMDictionary(new File(wnpath), ILoadPolicy.BACKGROUND_LOAD);
            int tryCount = 10;
            while (--tryCount > 0 && !dict.open());
            System.out.println("Dictionary opened.");
            wstem = new WordnetStemmer(dict);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        createMapSense(wordnetPath);
        createMapCluster(wordnetPath);
    }
   
    // Create Corase-grained map for sense clusters: mapcluster<word, sc>
    // sc is in the format of ArrayList<ArrayList<String>>
    // outer ArrayList represents different clusters
    // inner ArrayList represents different senseIds
    public void createMapCluster(String path) {
    	mapcluster.clear();
    	ArrayList<String> lines=IOManager.readLines(path+"/sense_clusters-21.senses");
    	String prevword=lines.get(0).substring(0, lines.get(0).indexOf('%'));;
    	ArrayList<ArrayList<String>> sc=new ArrayList<ArrayList<String>>();
    	sc.clear();
    	for (int i=0;i<lines.size();i++) {
    		String line=lines.get(i);
    		String word=line.substring(0, line.indexOf('%'));
    		
    		String[] strs=line.split(" ");
    		ArrayList<String> s=new ArrayList<String>();
    		for (int j=0;j<strs.length;j++) {
    			s.add(strs[j]);
    		}
 
    		if (word.equals(prevword)) {
    			sc.add(s);
    		}
    		else {
    			mapcluster.put(prevword, sc);
    			prevword=word;
    			sc=new ArrayList<ArrayList<String>>();
    			sc.clear();
    			sc.add(s);
    		}	
    	}
    }
    
    // Create map for sensekeys: mapsense<senseID, sensekey>
    // senseID is the representative ID for wordnet 3.0
    // sensekey is the internal ID for wordnet 3.0
    public void createMapSense(String path) {
    	mapsense.clear();
    	//mapsensereverse.clear();
    	ArrayList<String> lines=IOManager.readLines(path+"/index.sense");
    	for (int i=0;i<lines.size();i++) {
    		String[] strs=lines.get(i).split(" ");
    		String word=strs[0].substring(0, strs[0].indexOf('%'));
    		String synset=strs[1];
    		mapsense.put(new Pair<String, String>(word,synset), strs[0]);
    		//mapsensereverse.put(strs[0], strs[1]);
    	}
    }
    
    // Check if a word with a certain sensekey is in the constrcted mapcluster
    public int checkCluster(String lemma, int index, String sensekey) {
    	if(!mapcluster.containsKey(lemma))
    		return -1;
    	ArrayList<String> senses=mapcluster.get(lemma).get(index);
    	if (senses.indexOf(sensekey)!=-1) return 1;
    	return 0;
    }
    
    // Get all the Synsets of a given word with given pos and given sensecluster ID
    // Please use this to get Concepts!!!
    public ArrayList<Concept> getAllSynsetsFromCluster(String lemma, String pos, int index) {
    	List<Concept> syns=(List<Concept>) db.getAllConcepts(lemma, pos);
    	ArrayList<Concept> synsets=new ArrayList<Concept>();
    	int rv;
    	for (int i=0;i<syns.size();i++) {
    		String key=getSenseKey(syns.get(i).getSynset());
    		String value=mapsense.get(new Pair<String, String>(lemma, key));
    		rv = checkCluster(lemma,index,value);
    		if(rv==1) 
    			synsets.add(syns.get(i));
    		if(rv==-1)
    			synsets.add(db.getAllConcepts(lemma, pos).iterator().next());
    	}
    	return synsets;
    }
    
    // Return the number of sense clusters w.r.t a given word
    public int getClusterRange(String lemma,String pos){
    	//System.out.println(lemma+" "+mapcluster.containsKey(lemma));
    	/*
    	if(mapcluster.containsKey(lemma))
    		return mapcluster.get(lemma).size();
    	else
    	{
    		return 1;
    	}
    	*/
    	return db.getAllConcepts(lemma, pos).size();
    }

    public String getSenseKey(String synsetID) {
    	String id=synsetID.split("-")[0];
		return id;
	}
    
    public String getSenseString(String lemma, int index, String pos) {
    	String str="";
    	/*
		if(!mapcluster.containsKey(lemma)) {
			//System.out.println(lemma+" "+pos);
			if (!db.getAllConcepts(lemma,pos).iterator().hasNext()) return str;
			Concept c=db.getAllConcepts(lemma,pos).iterator().next();
			String value=mapsense.get(Pair.of(lemma,c.getSynset()));
			str=str+value+" ";
		}
    	else
    	{
    		ArrayList<String> strs=mapcluster.get(lemma).get(index);
    		for (int i=0;i<strs.size();i++)
    			str=str+strs.get(i)+" ";
    	}
    	*/
    	ArrayList<Concept> sl=(ArrayList<Concept>) db.getAllConcepts(lemma, pos);
    	if (sl.size()==0) str="null";
    	else str=mapsense.get(new Pair<String, String>(lemma,getSenseKey(sl.get(index).getSynset())));
		return str;
	}	
    
    public int[] getClusterIDs(ArrayList<String> tags,String lemma, String pos) {
		int[] v=new int[tags.size()];
    	for (int i=0;i<tags.size();i++) {
    		String tag=tags.get(i);
    		/*
    		v[i]=1;
    		if (!mapcluster.containsKey(lemma)) continue;
    		ArrayList<ArrayList<String>> senses=mapcluster.get(lemma);
    		boolean flag=false;
    		
    		for (int j=0;j<senses.size();j++) {
    			for (int k=0;k<senses.get(j).size();k++)
    				if (senses.get(j).get(k).equals(tag)) {
    					v[i]=j;
    					flag=true;
    					break;
    				}
    			if (flag) break;
    		}
    		*/
    		v[i]=0;
    		ArrayList<Concept> sl=(ArrayList<Concept>) db.getAllConcepts(lemma, pos);
    		for (int j=0;j<sl.size();j++)
    			if (mapsense.get(new Pair<String, String>(lemma,getSenseKey(sl.get(j).getSynset()))).equals(tag)) {
    				v[i]=j;
    				break;
    			}
		}
		return v;
	}

//---------------------------------------------------------------------
    public ArrayList<String> getAllSenseKey(String word,POS pos) {
    	ArrayList<ISynset> synsets=getAllSynset(word,pos);
    	ArrayList<String> sensekeys=new ArrayList<String>();
    	sensekeys.clear();
    	for (int i=0;i<synsets.size();i++) {
    		sensekeys.add(getSenseKey(synsets.get(i).getID().toString()));
    	}
		return sensekeys;
	}

    // Convert pos-tagger in dataset to the internal POS 
    public POS POSConvert(String p) {
    	if (p.equals("n")) return POS.NOUN;
    	if (p.equals("a")) return POS.ADJECTIVE;
    	if (p.equals("v")) return POS.VERB;
    	if (p.equals("r")) return POS.ADVERB;
    	return null;
    }
    
	public String getSynsetId(String word) {
		List<String> stems = wstem.findStems(word, null);
		if(stems.size() > 0)
			word = stems.get(0);
		IIndexWord idxWord = dict.getIndexWord(word, POS.NOUN) ;
		IWordID wordID = idxWord.getWordIDs().get(0); // 1 st meaning
		IWord iword = dict.getWord (wordID);
		ISynset synset = iword.getSynset();
		return synset.getID().toString();
	}
	
	public void getStems(String word) {
		List<String> stems = wstem.findStems(word,null);
		for(String s: stems){
			System.out.println(s);
		}
	}
	
	public ArrayList<String> getStemsList(String word) { //experimental change!!!!!!!!!!!!!!!!!!!!!!!
		return getStemsList(word, null);
	}
	
	public ArrayList<String> getStemsList(final String word, POS pos) { //experimental change!!!!!!!!!!!!!!!!!!!!!!!
		List<String> stems = new ArrayList<String>();
		if(pos!=null) {
			List<String> stems1 = wstem.findStems(word, pos);
			stems=fix(stems1, word.substring(0,1), word);
		}
		else {
			for (POS p : POS.values()) {
				List<String> stems1=wstem.findStems(word, p);
				stems.addAll(fix(stems1, word.substring(0,1), word));
			}
		}
		stems = new ArrayList<String>((new HashSet<String>(stems)));
		if(stems.size() == 0)
			return new ArrayList<String>(Arrays.asList(word));
		return (ArrayList<String>) stems;
	}
	
	private List<String> fix(List<String> stems, String substring, String word) {
		ArrayList<String> stms = new ArrayList<String>();
		
		for(String s : stems) {
			if(s.equals("ha") && word.equals("has"))
				s="has";
			if(s.equals("doe") && word.equals("does"))
				s="does";
			//if(s.substring(0,1).equals(substring)) {
				stms.add(s);
				break;
			//}
		}

		
		return stms;	
	}

	public void printSynsets(String word) {
		IIndexWord idxWord = dict.getIndexWord(word, POS.NOUN) ;
		IWordID wordID = idxWord.getWordIDs().get(0); // 1 st meaning
		IWord iword = dict . getWord (wordID);
		ISynset synset = iword.getSynset();
		List<ISynsetID> sets = synset.getRelatedSynsets(Pointer.HYPERNYM);
		for(ISynsetID is: sets) {
			System.out.println(synset);
			recurseSynset(dict.getSynset(is));
		}
	}
	
	public void getAllSemanticRelations(String lemma) {
		//for each stem, for each sense in stemn\,print lemma, sensem, probability, sr and lr
		List<String> stems = wstem.findStems(lemma, null);
		for(String s: stems){
			for (POS pos : POS.values()) {
				IIndexWord idxWord = dict.getIndexWord(lemma, pos) ;
				if(idxWord==null)
					continue;
				List<IWordID> senses = idxWord.getWordIDs(); // 1 st meaning
				for(IWordID iw : senses) {
					ISynset is = dict.getSynset(iw.getSynsetID());
					System.out.println(is.getGloss());
					getAllSemanticRelations(is);
				}
			}
		}
	}
	
	public ArrayList<IWord> getAntonyms(String st) {
		return getAntonyms(st,null);
	}
	
	public ArrayList<IWord> getAntonyms(String st, POS p) {
		ArrayList<IWord> iwds = getIWords(st,p);
		ArrayList<IWord> antonyms = new ArrayList<IWord>();
		for(IWord iw: iwds) {
			List<IWordID> ants = iw.getRelatedMap().get(Pointer.ANTONYM);
			if(ants==null)
				continue;
			for(IWordID id: ants) {
				//System.out.println(dict.getWord(id));
				antonyms.add(dict.getWord(id));
			}
		}
		
		return antonyms;
	}
	
	public boolean isAntonym(String w1, POS p1, String w2, POS p2) {
		ArrayList<IWord> ants = getAntonyms(w1,p1);
		ArrayList<IWord> wds = getIWords(w2,p2);
		
		for(IWord ant: ants) {
			for(IWord iw: wds) {
				if(ant.equals(iw))
					return true;
			}
		}
		
		return false;
	}
	
	public boolean isAntonym(String w1, String w2) {
		return isAntonym(w1,null,w2,null);
	}
	
	/*uncomment to let capital letterered words be converted to lowercase*/
	public ArrayList<ISynset> getAllSynset(String word) {
		ArrayList<ISynset> syns = getAllSynset(word, false, null);
		if(syns.size()==0)
			return getAllSynset(word, true,  null);
		return syns;
	}
	
	public ArrayList<ISynset> getAllSynset(String word, POS pos) {
		ArrayList<ISynset> syns = getAllSynset(word, false, pos);
		if(syns.size()==0)
			return getAllSynset(word, true, pos);
		return syns;
	}
	
	//if word starts with cap, only let those synsets through that also start with cap.
	public ArrayList<ISynset> getAllSynset(String word, boolean flag, POS pos_) {
		ArrayList<ISynset> syns = new ArrayList<ISynset>();
		List<String> stems = getStemsList(word,pos_);
		for(String s: stems){
			//System.out.println(s);
			for (POS pos : POS.values()) {
				if(pos_ !=null && !pos.equals(pos_))
					continue;
				//System.out.println(pos);
				IIndexWord idxWord = dict.getIndexWord(s, pos) ;
				if(idxWord==null)
					continue;
				List<IWordID> senses = idxWord.getWordIDs(); // 1 st meaning
			//	HashMap<IWordID,Double> probs = getProbs(senses);
				for(IWordID iw : senses) {
			//		if(probs.get(iw) > pTHRESH) {
			//			System.out.println(probs.get(iw));
					ISynset is = dict.getSynset(iw.getSynsetID());
					List<IWord> words = is.getWords();
					if(isAcceptable(words, word) || flag) {
					//	System.out.println(is);
					//if(true) {
						syns.add(is);
//						for(IWord iword: words) {
//						//	System.out.println(iword);
//						}
					}
				}
			}
		}
		
		return syns;
	}
	
	//only acceptable if word is not capitalized or is capitalized and a capitaled word in synset.
	private boolean isAcceptable(List<IWord> words, String word) {
		if(containsCapital(word)) {
			for(IWord iword : words) {
				if(containsCapital(iword.getLemma()))
					return true;
			}
			return false;
		}
		return true;
	}
	
	private boolean containsCapital(String word) {
		
		for(int i = 0; i < word.length(); i++) {	
			if (Character.isUpperCase(word.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	HashMap<IWordID, Double> getProbs(List<IWordID> senses) {
		HashMap<IWordID,Double> probs = new HashMap<IWordID, Double>();
		double total = 0;
		for(IWordID iw: senses) {
			total += dict.getSenseEntry(dict.getWord(iw).getSenseKey()).getTagCount();
		}
		for(IWordID iw: senses) {
			double val = dict.getSenseEntry(dict.getWord(iw).getSenseKey()).getTagCount() / total;
			if(Double.isNaN(val)) {
				val = 0.;
			}
			probs.put(iw, val);
		}
		
		return probs;
	}

	public void getAllSemanticRelations(ISynset s) {
		Map<IPointer, List<ISynsetID>> map = s.getRelatedMap();
		Set<Entry<IPointer, List<ISynsetID>>> x = map.entrySet();
		Iterator<Entry<IPointer, List<ISynsetID>>> it = x.iterator();
		while(it.hasNext()) {
			Entry y = it.next();
			System.out.println(y.getKey());
			List<ISynsetID> lis = (List<ISynsetID>) y.getValue();
			for(ISynsetID id: lis) {
				ISynset syn = dict.getSynset(id);
				System.out.println("\t" + syn);
			}
			
		}
	}
	
	public void getAllLexicalRelations(String lemma) {
		//for each stem, for each sense in stemn\,print lemma, sensem, probability, sr and lr
		List<String> stems = wstem.findStems(lemma, null);
		for(String s: stems){
			for (POS pos : POS.values()) {
				IIndexWord idxWord = dict.getIndexWord(lemma, pos) ;
				if(idxWord==null)
					continue;
				List<IWordID> senses = idxWord.getWordIDs(); // 1 st meaning
				for(IWordID iw : senses) {
					ISynset is = dict.getSynset(iw.getSynsetID());
					System.out.println(is.getGloss());
					getAllLexicalRelations(dict.getWord(iw));
				}
			}
		}
	}
	
	public ArrayList<IWord> getIWords(String st) {
		return getIWords(st,null);
	}
	
	public ArrayList<IWord> getIWords(String st, POS pos_) {
		List<String> stems = wstem.findStems(st, pos_);
		ArrayList<IWord> iwds = new ArrayList<IWord>();
		for(String s: stems){
			for (POS pos : POS.values()) {
				if(pos_!=null && !pos.equals(pos_))
					continue;
				IIndexWord idxWord = dict.getIndexWord(st, pos) ;
				if(idxWord==null)
					continue;
				List<IWordID> senses = idxWord.getWordIDs(); // 1 st meaning
				for(IWordID iw : senses) {
					iwds.add(dict.getWord(iw));
				}
			}
		}
		
		return iwds;
	}
	
	public void getAllLexicalRelations(IWord s) {
		Map<IPointer, List<IWordID>> map = s.getRelatedMap();
		Set<Entry<IPointer, List<IWordID>>> x = map.entrySet();
		Iterator<Entry<IPointer, List<IWordID>>> it = x.iterator();
		while(it.hasNext()) {
			Entry y = it.next();
			System.out.println(y.getKey());
			List<IWordID> lis = (List<IWordID>) y.getValue();
			for(IWordID id: lis) {
				IWord syn = dict.getWord(id);
				System.out.println("\t" + syn);
			}
			
		}
	}
	
	public void recurseSynset(ISynset s) {
		System.out.println(s);
		List<ISynsetID> lis = s.getRelatedSynsets(Pointer.HYPERNYM);
		for(ISynsetID id: lis) {
			recurseSynset(dict.getSynset(id));
			dict.getSynset(id);
		}
	}
}