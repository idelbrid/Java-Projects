import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.*;
public class MyTreeNode implements TreeNode {
	Cluster data;
	MyTreeNode parent;
	ArrayList<MyTreeNode> children;
	
	public MyTreeNode(Cluster data){
		
	}
	public Enumeration children() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getAllowsChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	public TreeNode getChildAt(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getChildCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getIndex(TreeNode arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public TreeNode getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

}
