package org.windom.generator.postprocessor.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.windom.generator.engine.NodeInstance;
import org.windom.generator.engine.TreeInstance;
import org.windom.generator.postprocessor.PostProcessor;
import org.windom.generator.util.traversal.Evaluator;
import org.windom.generator.util.traversal.Traversal;
import org.windom.generator.util.traversal.Visitor;

public class ExpansionPostProcessor implements PostProcessor {

	@Override
	public String process(TreeInstance treeInstance) {
		NodeInstance startInstance = treeInstance.getStartInstance();
		ExpansionWalker walker = new ExpansionWalker(startInstance);
		Traversal.depthFirst(startInstance, walker, walker);
		return walker.getResult();
	}	
	
	private class ExpansionWalker implements Visitor<NodeInstance>, Evaluator<NodeInstance> {
		
		private final List<NodeInstance> done = new ArrayList<NodeInstance>();
		private final List<NodeInstance> todo = new LinkedList<NodeInstance>();
		
		private final StringBuilder result = new StringBuilder();
		
		public ExpansionWalker(NodeInstance startInstance) {
			todo.add(startInstance);
			processExpansion(null);
		}
		
		private void processExpansion(NodeInstance nodeInstance) {
			if (nodeInstance != null) {
				result.append("\n[");
				result.append(nodeInstance.getRuleInstance().getRule());
				result.append("]");
			} else {
				result.append("[initial]");
			}
			result.append("\t\t");
			for (NodeInstance doneInstance : done) {
				result.append(' ');
				result.append(doneInstance.getNode());
			}
			if (!todo.isEmpty()) {
				result.append(" *");
				for (NodeInstance todoInstance : todo) {
					result.append(' ');
					result.append(todoInstance.getNode());
				}
			}
		}
		
		public String getResult() {
			return result.toString();
		}
		
		public boolean prune(NodeInstance nodeInstance) {
			return nodeInstance.isOnLimit();
		}
		
		public void visit(NodeInstance nodeInstance) {
			if (todo.remove(0) != nodeInstance) {
				throw new RuntimeException("Inconsistent generation");
			}
			List<NodeInstance> childInstances = nodeInstance.getChildren();
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
