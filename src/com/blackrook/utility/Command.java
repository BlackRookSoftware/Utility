/*******************************************************************************
 * Copyright (c) 2013-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.utility;

/**
 * A command interface for executable commands.
 * @author Matthew Tropiano
 */
public interface Command<C extends Context>
{
	/**
	 * Executes this command.
	 * @param context the context to pass to the command.
	 * @param args the arguments to pass.
	 * @return true to continue execution in an Executor, false to terminate. 
	 */
	public boolean execute(C context, String ... args);
}
