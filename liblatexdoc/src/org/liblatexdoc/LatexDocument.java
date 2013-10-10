package org.liblatexdoc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.libteidoc.OutputDocument;

public class LatexDocument implements OutputDocument {
	
	protected static Logger logger = LogManager.getLogger(LatexDocument.class.getName());
	
	protected File latexFile;
	
	protected final int MAX_LINE_LENGTH = 1024;
	protected final String lineSeparator = System.getProperty("line.separator") ;
	
	protected int currentLineLength = 0;

	protected LinkedList<String> preamble;
	protected BufferedWriter writer = null;
	
	public LatexDocument(String f) {
		latexFile = new File(f);
		preamble = new LinkedList<String>();
		//content = "";
	}
	
	public void usePackage(String pack, String options) {
		String preambleString = "\\usepackage["+options+"]{"+pack+"}\n";
		preamble.add(preambleString);
	}
	
	public void usePackage(String pack) {
		String preambleString = "\\usepackage{"+pack+"}\n";
		preamble.add(preambleString);
	}
	
	public void addPreambleLine(String s) {
		preamble.add(s+"\n");
	}
	
	public void begin(String env) {
		write("\\begin{"+env+"}");
	}
	
	public void end(String env) {
		write("\\end{"+env+"}");
	}	
	
	public void beginDocument() {
		try {
			writer = new BufferedWriter(new FileWriter(latexFile));
			
			writePreamble();
			writer.write("\\begin{document}\n");
		} catch (IOException e) {
			logger.error("IOException: "+e.getMessage());
		}		
	}
	
	public void endDocument() {
		try {

			writer.write("\\end{document}\n");
		} catch (IOException e) {
			logger.error("IOException: " + e.getMessage());

		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * Carefully use this method. You might exceed line length limit!
	 * @param s
	 */
	public void write(String s) {
		
		s = replaceCharacters(s);
		try {
			writer.write(s);
		} catch (IOException e) {
			logger.error("IOException: "+e.getMessage());
		}
	}
	
	protected String replaceCharacters(String s) {
	
			// Replace '&' by '\&'
			s = StringUtils.replace(s, "&", "\\&");
			
			// Replace 'ē' by '\~{e}'
			s = StringUtils.replace(s, "ē", "\\~{e}");
			
			// Replace 'ā' by '\~{a}'
			s = StringUtils.replace(s, "ā", "\\~{a}");
			
			// Replace 'ū' by '\~{u}'
			s = StringUtils.replace(s, "ū", "\\~{u}");
			
			// Replace 'ę' by '\c{e}'
			s = StringUtils.replace(s, "ę", "\\c{e}");
			
			// Replace 'ō' by '\~{o}'
			s = StringUtils.replace(s, "ō", "\\~{o}");
			
			// Replace '\n' by system line separator
			s = StringUtils.replace(s,  "\n", lineSeparator);
		
		return s;
	}
	
	protected void writePreamble() {
		try {
			writer.write("\\documentclass[12pt,a4paper]{article}\n");
			writer.write("\\usepackage[utf8]{inputenc}\n");
			writer.write("\\usepackage[top=3cm, bottom=3cm, left=3cm, right=6cm, marginparwidth=4cm]{geometry}\n");
			//writer.write("\\usepackge{setspace}\n");
			
			for (String s: preamble) {
				writer.write(s);
			}
			
			writer.write("\\renewcommand{\\baselinestretch}{1.50}\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
