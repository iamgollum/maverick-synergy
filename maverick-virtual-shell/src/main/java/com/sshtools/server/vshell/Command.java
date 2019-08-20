/**
 * (c) 2002-2019 JADAPTIVE Limited. All Rights Reserved.
 *
 * This file is part of the Maverick Synergy Java SSH API.
 *
 * Maverick Synergy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Maverick Synergy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Maverick Synergy.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.sshtools.server.vshell;

import java.io.IOException;

import jline.Completor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.sshtools.common.permissions.PermissionDeniedException;

public interface Command extends Completor {

	public static final int STILL_ACTIVE = Integer.MIN_VALUE;
	
	public abstract Options getOptions();

	public abstract void run(CommandLine args, VirtualProcess process)
			throws IOException, PermissionDeniedException;

	public abstract String getDescription();

	public abstract String getSubsystem();

	public abstract String getCommandName();

	public abstract String getSignature();

	public abstract boolean isBuiltIn();

	public abstract int getExitCode();

	public abstract void init(VirtualProcess process);

	public abstract boolean isHidden();


}