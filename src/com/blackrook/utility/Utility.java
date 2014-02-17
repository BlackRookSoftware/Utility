/*******************************************************************************
 * Copyright (c) 2013-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.utility;

import java.io.PrintStream;

/**
 * Utility base for all utilities.
 * This class operates on {@link Context}s rather than change its internal state. 
 * @author Matthew Tropiano
 */
public abstract class Utility<C extends Context>
{
	/** The Print Stream to use for output. */
	protected PrintStream out;
	/** The Print Stream to use for error output. */
	protected PrintStream err;
	
	/**
	 * Creates the Utility and sets its printstream.
	 * @param out the {@link PrintStream} to output to.
	 * @param err the {@link PrintStream} to output error output to.
	 */
	protected Utility(PrintStream out, PrintStream err)
	{
		this.out = out;
		this.err = err;
	}
	
	/**
	 * Creates the Utility and sets its printstreams to {@link System#out} and {@link System#err}.
	 */
	protected Utility()
	{
		this(System.out, System.err);
	}
	
	/**
	 * Returns the current version of this utility.
	 * @return the {@link Version} of this utility class.
	 */
	public abstract Version getVersion(); 
	
	/**
	 * Parses the command line input and returns a {@link Settings} object.
	 * @param args the same args from a <i>p.s.v.Main</i> invocation.
	 * @return the Settings to use for this utility's execution.
	 */
	public abstract Settings getSettingsFromCMDLINE(String ... args); 

	/**
	 * Creates a new context for this utility to operate on.
	 */
	public abstract C createNewContext();

	/**
	 * Called to execute this utility on a specific context with a set of settings.
	 * @param context the context to use for execution.
	 * @param settings the {@link Settings} to use for this utility's execution.
	 * @return an errorlevel-type of return. 0 for no error, nonzero for error.
	 */
	public abstract int execute(C context, Settings settings);
	
	/**
	 * An automating method that does the following:
	 * <ul>
	 * <li>Calls {@link #getSettingsFromCMDLINE(String...)} with <code>args</code>.
	 * <li>Calls {@link #createNewContext()} to get a new context.
	 * <li>Calls {@link #execute(Context, Settings)} with the new context and settings, and return its value.
	 * </ul>
	 * @return an errorlevel-type of return. 0 for no error, nonzero for error.
	 */
	public int go(String ... args)
	{
		Settings settings = getSettingsFromCMDLINE(args);
		C context = createNewContext();
		
		return execute(context, settings);
	}
	
}
