package T1753A1;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.CompositeReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;

public class App 
{
	public static void printQuery(IndexSearcher is, String query) {
		try {
		QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		TopDocs results;
		ScoreDoc[] hits;
		
		/* Perform the first query */
		results = is.search(parser.parse(query), 10);
		hits = results.scoreDocs;
		System.out.println("Begin: '" + query + "'");
		for (ScoreDoc hit: hits) {
			Document doc = is.doc(hit.doc);
			System.out.println(doc.get("id"));
			System.out.println(doc.get("text"));
		}
		System.out.println("End '" + query +"'");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args)
	{
		try {
			String dataFile = args[0];
			
			/* Setup the indexer */
			Directory indexDir = FSDirectory.open(new File("index").toPath());
			IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
			IndexWriter iwriter = new IndexWriter(indexDir, config);
			
	
			/* Deserialize the data and add documents to the index */
			FileInputStream fp = new FileInputStream(dataFile);
			for (Data.Paragraph para : DeserializeData.iterableParagraphs(fp)) {
				Document doc = new Document();
				doc.add(new StringField("id", para.getParaId(), Field.Store.YES));
				doc.add(new TextField("text", para.getTextOnly(), Field.Store.YES));
				iwriter.addDocument(doc);
			}
			iwriter.close();
			/* Use the index */
			IndexSearcher is = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("index").toPath())));
			
			printQuery(is, "text: power text: nap text: benefits");
			printQuery(is, "text: whale text: vocalization text: production text: of text: sound");
			printQuery(is, "text: pokemon text: puzzle text: league");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
