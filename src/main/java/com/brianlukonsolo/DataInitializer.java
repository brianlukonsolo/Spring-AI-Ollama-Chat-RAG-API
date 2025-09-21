package com.brianlukonsolo;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer {

	@Autowired
	private VectorStore vectorStore;
	
	@PostConstruct
	public void init() {
		TextReader jobListReader = new TextReader(new ClassPathResource("jobs_knowledgebase.txt"));
		TokenTextSplitter tokenTextSplitter = new TokenTextSplitter(100,100,5,1000,true);
		List<Document> documents = tokenTextSplitter.split(jobListReader.get());
		vectorStore.add(documents);
	}

}
