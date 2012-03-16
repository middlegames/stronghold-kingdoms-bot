package com.middlegames.shkbot.gui;

import java.awt.Dimension;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.middlegames.shkbot.xml.ParishDescriptor;
import com.middlegames.shkbot.xml.PersistenceEngine;
import com.middlegames.shkbot.xml.VillageDescriptor;

public class BotGovernorTree extends JTree implements TreeSelectionListener {

	private static final long serialVersionUID = 1L;

	private static class RootNode extends DefaultMutableTreeNode {

		private static final long serialVersionUID = 1L;

		public RootNode() {
			super("Governor");
			add(new VillagesNode());
			add(new ParishesNode());
		}
	}
	
	private static class VillagesNode extends DefaultMutableTreeNode {
		
		private static final long serialVersionUID = 1L;

		public VillagesNode() {
			super("Villages");
			for(VillageDescriptor vd : engine.getVillageDescriptors()) {
				add(new DefaultMutableTreeNode(vd));
			}
		}
	}
	
	private static class ParishesNode extends DefaultMutableTreeNode {
		
		private static final long serialVersionUID = 1L;

		public ParishesNode() {
			super("Parishes");
			for(ParishDescriptor pd : engine.getParishDescriptors()) {
				add(new DefaultMutableTreeNode(pd));
			}
		}
	} 

	private static PersistenceEngine engine = PersistenceEngine.getInstance();
	private DefaultMutableTreeNode root = new RootNode();
	
	public BotGovernorTree() {
		super();
		
		setModel(new DefaultTreeModel(root));
		setBorder(BorderFactory.createEmptyBorder(2,4,2,4));
		
		expandAll(this, new TreePath(root), true);
		
		
		addTreeSelectionListener(this);
		setRootVisible(true);
		setPreferredSize(new Dimension(200, 300));
		setSize(new Dimension(200, 300));
	} 
	
	@Override
	public void valueChanged(TreeSelectionEvent tse) {
		TreePath tp = tse.getNewLeadSelectionPath();
		if (tp != null) {
			Object selection = tp.getLastPathComponent();
			System.out.println(selection);
		}
	}
	
	private void expandAll(JTree tree, TreePath parent, boolean expand) {

	    TreeNode node = (TreeNode)parent.getLastPathComponent();
	    if (node.getChildCount() >= 0) {
	        for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
	            TreeNode n = (TreeNode) e.nextElement();
	            TreePath path = parent.pathByAddingChild(n);
	            expandAll(tree, path, expand);
	        }
	    }

	    if (expand) {
	        tree.expandPath(parent);
	    } else {
	        tree.collapsePath(parent);
	    }
	}
}
