package org.windom.generator.input.plain.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.windom.generator.input.InputException;
import org.windom.generator.input.plain.Scanner;
import org.windom.generator.input.plain.symbol.Identifier;
import org.windom.generator.input.plain.symbol.Literal;
import org.windom.generator.input.plain.symbol.Sigil;
import org.windom.generator.input.plain.symbol.Tag;
import org.windom.generator.input.plain.symbol.Token;
import org.windom.generator.input.plain.symbol.Word;

public class ScannerImpl implements Scanner {

	private final Reader reader;
	private int peek;
	private int line;
	private int col;
	
	public ScannerImpl(InputStream is) {
		this(new InputStreamReader(is));
	}
	
	public ScannerImpl(Reader reader) {
		this.reader = reader;
		this.peek = ' ';
		this.line = 1;
		this.col = 0;
	}
	
	@Override
	public Token token() throws InputException {
		try {
			skipWhite();
			int line = this.line;
			int col = this.col;
			Token token = tokenInner();
			token.setLine(line);
			token.setCol(col);
			return token;
		} catch (IOException e) {
			throw new InputException("Error reading input",e);
		}
	}
	
	public Token tokenInner() throws IOException, InputException {		
		if (Character.isLetter(peek) || peek == '_') {
			StringBuilder sb = new StringBuilder();
			do {
				sb.append((char)peek);
				readChar();
			} while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_');
			return new Identifier(sb.toString());
		}
		
		if (peek == '\'') {
			StringBuilder sb = new StringBuilder();
			for (readChar(); peek >= 0 && peek != '\''; readChar()) {
				if (peek == '\\') {
					readChar();
					if (peek < 0) break;
				}
				sb.append((char)peek);
			}
			if (peek < 0) {
				throw new InputException(String.format(
						"Unterminated literal near line %d col %d",
						line,
						col));
			}
			peek = ' ';
			return new Literal(sb.toString());
		}
		
		if (peek == '-') {
			if (readChar('>')) 
				return new Word(Tag.EXPAND,"->");
			else
				return new Sigil('-');
		}
		
		if (peek < 0) {
			return new Token(Tag.END);
		}
		
		Token token = new Sigil((char)peek);
		peek = ' ';
		return token;
	}
	
	private void readChar() throws IOException {
		peek = reader.read();
		if (peek == '\n') {
			line++;
			col = 0;
		} else if (peek >= 0 && peek != '\r') {
			col++;
		}
	}
	
	private boolean readChar(int ch) throws IOException {
		readChar();
		if (peek != ch) return false;
		peek = ' ';
		return true;
	}
	
	private void skipWhite() throws IOException {
		for ( ; ; readChar()) {
			if (Character.isWhitespace(peek)) {
				continue;
			} else if (peek == '#') {
				while (peek >= 0 && peek != '\n') {
					readChar();
				}
			} else { 
				break;
			}
		}
	}
	
}
