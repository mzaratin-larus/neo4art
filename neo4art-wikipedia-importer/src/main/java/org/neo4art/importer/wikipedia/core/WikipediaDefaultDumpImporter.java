/**
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neo4art.importer.wikipedia.core;

import info.bliki.wiki.dump.WikiXMLParser;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.neo4art.importer.wikipedia.service.WikipediaDumpParserListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

/**
 * 
 * @author Lorenzo Speranzoni
 * @since 25.02.2015
 */
@Service
public class WikipediaDefaultDumpImporter implements WikipediaDumpImporter {

	@Autowired
	@Qualifier("wikipediaAggregatedDumpParserListener")
	private WikipediaDumpParserListener wikipediaPageHandler;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public long importOrUpdateDump(File dumpFile) throws IOException, SAXException, ParserConfigurationException {
		WikiXMLParser parser = new WikiXMLParser(dumpFile.getAbsolutePath(), wikipediaPageHandler);
		parser.parse();
		this.wikipediaPageHandler.flush();
		return this.wikipediaPageHandler.getPageCount();
	}

}
