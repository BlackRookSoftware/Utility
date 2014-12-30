/*******************************************************************************
 * Copyright (c) 2013-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.blackrook.commons.Common;
import com.blackrook.commons.CommonTokenizer;
import com.blackrook.commons.hash.CaseInsensitiveHashMap;
import com.blackrook.commons.list.List;

/**
 * Command script executor.
 * @author Matthew Tropiano
 * @param <C> a Context type.
 * @param <E> an enum type that are also commands.
 */
public class Executor<C extends Context, E extends Enum<E>>
{
	public static final String DEFAULT_PREFIX = "#";
	
	/** If a line is prefixed with this string, ignore it. */
	private String commentPrefix;
	/** Hash of commands (for case-insensitivity). */
	private CaseInsensitiveHashMap<E> commandHash;
	
	/**
	 * Creates a new CommandScript Module for a command type.
	 * Default commentPrefix is "#".
	 */
	public Executor(Class<E> clazz)
	{
		commentPrefix = DEFAULT_PREFIX;
		commandHash = new CaseInsensitiveHashMap<E>();
		for (E econst : clazz.getEnumConstants())
			commandHash.put(econst.name(), econst);
	}
	
	/** If a line is prefixed with this string, ignore it. */
	public String getCommentPrefix()
	{
		return commentPrefix;
	}

	/** Sets comment prefix: if a line is prefixed with this string, ignore it. */
	public void setCommentPrefix(String commentPrefix)
	{
		this.commentPrefix = commentPrefix;
	}

	/**
	 * Executes a script line by line until it reaches the end.
	 * @param f the file to read from.
	 * @param c the context.
	 * @throws ScriptException if something goes wrong.
	 * @throws FileNotFoundException if the file can't be found.
	 * @throws NullPointerException if f is null.
	 */
	public void execute(File f, C c) throws ScriptException, FileNotFoundException
	{
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			execute(f.getName(), fis, c);
		} catch (ScriptException ex) {
			throw ex;
		} finally {
			Common.close(fis);
		}
	}
	
	/**
	 * Executes a script line by line until it reaches the end.
	 * @param in the input stream to read from.
	 * @param c the context.
	 * @throws ScriptException if something goes wrong.
	 */
	public void execute(InputStream in, C c) throws ScriptException
	{
		execute(null, in, c);
	}
	
	/**
	 * Executes a script line by line until it reaches the end.
	 * @param streamName the name of the stream.
	 * @param in the input stream to read from.
	 * @param c the context.
	 * @throws ScriptException if something goes wrong.
	 */
	@SuppressWarnings("unchecked")
	public void execute(String streamName, InputStream in, C c) throws ScriptException
	{
		BufferedReader br = null;
		String line = null;
		int lineCount = 0;
		try {
			br = new BufferedReader(Common.openTextStream(in));
			boolean good = true;
			while (good && (line = br.readLine()) != null)
			{
				lineCount++;
				String tline = line.trim();
				if (tline.length() == 0)
					continue;
				else if (tline.startsWith(commentPrefix))
					continue;
				
				CommonTokenizer ct = new CommonTokenizer(tline);
				E cmd = commandHash.get(ct.nextToken());
				if (cmd == null)
					throw new ScriptException(streamName, lineCount, "Expected valid command.");
				
				List<String> list = new List<String>();
				while (ct.hasMoreTokens()) 
					list.add(ct.nextToken());
				String[] out = new String[list.size()];
				list.toArray(out);
				good = ((Command<C>)cmd).execute(c, out);
			}
		} catch (ScriptException e) {
			throw e;
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ScriptException(streamName, lineCount, "Expected missing argument.");
		} catch (Exception e) {
			throw new ScriptException(streamName, lineCount, e);
		}
	}
	
	/**
	 * Exception thrown if something goes completely wrong during a script execution.
	 */
	public static class ScriptException extends Exception
	{
		private static final long serialVersionUID = 8904944049568990748L;
		
		private String streamName;
		private int line;
		
		public ScriptException(String streamName, int line, String message)
		{
			this(streamName, line, message, null);
		}
		
		public ScriptException(String streamName, int line, Throwable t)
		{
			super(t.getLocalizedMessage(), t);
		}
		
		public ScriptException(String streamName, int line, String message, Throwable t)
		{
			super(message, t);
			this.streamName = streamName;
			this.line = line;
		}
		
		public String getStreamName()
		{
			return streamName;
		}

		public int getLine()
		{
			return line;
		}
		
		public String asErrorString()
		{
			StringBuilder sb = new StringBuilder();
			if (streamName != null)
				sb.append('(').append(streamName).append(')').append(' ');
			sb.append("Line ").append(line).append(':').append(' ');
			sb.append(getLocalizedMessage());
			return sb.toString();
		}
		
	}
	
}
