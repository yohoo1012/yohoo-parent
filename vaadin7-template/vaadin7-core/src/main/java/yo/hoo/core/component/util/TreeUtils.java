package yo.hoo.core.component.util;

import java.util.Iterator;

import com.vaadin.ui.Tree;

public class TreeUtils {

	public static void removeChilds(Tree tree, Object itemId) {
		if(tree.hasChildren(itemId)){
			for (Iterator<?> ite = tree.getChildren(itemId).iterator(); ite.hasNext();) {
				removeChilds(tree, ite.next());
			}
		}
	}
}
