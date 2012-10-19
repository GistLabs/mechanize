/**
 * Modification from original by Gist Labs, LLC (http://gistlabs.com)
 * 
 * Copyright (c) 2009-2012, Christer Sandberg
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.gistlabs.mechanize.util.css_query;

import java.util.Collection;
import java.util.List;

public interface NodeHelper<Node> {

	public class Index {
		public final int index;
		public final int size;
		public Index(final int index, final int size) {
			this.index = index;
			this.size = size;
		}
	}

	public String getValue(Node element);

	public boolean hasAttribute(Node element, String name);

	public String getAttribute(Node element, String name);

	public Index getIndexInParent(Node node, boolean byType);

	public Node getRoot();

	public boolean isEmpty(Node node);

	public Collection<? extends Node> getDescendentNodes(Node node);

	public List<? extends Node> getChildNodes(Node node);

	public String getName(Node n);

	public Node getNextSibling(Node node);

	/**
	 * Returns a case appropriate equality check for the name of the node.
	 * Also returns true if name argument is Selector.UNIVERSAL_TAG
	 * @param n
	 * @param name
	 * @return
	 */
	public boolean nameMatches(Node n, String name);
}
