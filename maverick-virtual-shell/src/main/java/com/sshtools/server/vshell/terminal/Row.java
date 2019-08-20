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
package com.sshtools.server.vshell.terminal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.sshtools.server.vshell.terminal.Table.Alignment;

public class Row extends ArrayList<Cell<?>> {
	private static final long serialVersionUID = 1L;

	private Alignment defaultAlignment = Alignment.LEFT;
	private boolean strong;

	public Row(Cell<?>... cells) {
		this(Arrays.asList(cells));
	}

	public Row(int cells) {
		for (int i = 0; i < cells; i++) {
			add(new Cell<String>(""));
		}
	}

	public Row(Collection<Cell<?>> cells) {
		addAll(cells);
	}
	

	public boolean isStrong() {
		return strong;
	}

	public void setStrong(boolean strong) {
		this.strong = strong;
	}

	public Alignment getDefaultAlignment() {
		return defaultAlignment;
	}

	public void setDefaultAlignment(Alignment defaultAlignment) {
		this.defaultAlignment = defaultAlignment;
	}
}