package org.windom.generator.postprocessor.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.windom.generator.engine.NodeInstance;
import org.windom.generator.engine.TreeInstance;
import org.windom.generator.postprocessor.PostProcessor;
import org.windom.util.traversal.Evaluator;
import org.windom.util.traversal.Traversal;
import org.windom.util.traversal.Visitor;

public class ExpansionPostProcessor<N extends NodeInstance<N>> implements PostProcessor<N> {

	@Override
	public String process(TreeInstance<N> treeInstance) {
		N startInstance = treeInstance.getStartInstance();
		ExpansionWalker walker = new ExpansionWalker(startInstance);
		Traversal.depthFirst(startInstance, walker, walker);
		return walker.getResult();
	}	
	
	private class ExpansionWalker implements Visitor<N>, Evaluator<N> {
		
		private final List<N> done = new ArrayList<N>();
		private final List<N> todo = new LinkedList<N>();
		
		private final StringBuilder result = new StringBuilder();
		
		public ExpansionWalker(N startInstance) {
			todo.add(startInstance);
			processExpansion(null);
		}
		
		private void processExpansion(N nodeInstance) {
			if (nodeInstance != null) {
				result.append("\n[");
				result.append(nodeInstance.getRuleInstance().getRule());
				result.append("]");
			} else {
				result.append("[initial]");
			}
			result.append("\t\t");
			for (N doneInstance : done) {
				result.append(' ');
				result.append(doneInstance.getNode());
			}
			if (!todo.isEmpty()) {
				result.append(" *");
				for (N todoInstance : todo) {
					result.append(' ');
					result.append(todoInstance.getNode());
				}
			}
		}
		
		public String getResult() {
			return result.toString();
		}
		
		public boolean prune(N nodeInstance) {
			return nodeInstance.isOnLimit();
		}
		
		public void visit(N nodeInstance) {
			if (todo.remove(0) != nodeInstance) {
				throw new RuntimeException("Inconsistent generation");
			}
			List<N> childInstances = nodeInstance.getChildren();
			for (int childIdx = childInstances.size()-1; childIdx >= 0; childIdx--) {
				todo.add(0, childInstances.get(childIdx));
			}
			while (todo.size() > 0 && todo.get(0).isOnLimit()) {
				done.add(todo.remove(0));
			}
			processExpansion(nodeInstance);
		}
		
	}
	
}
