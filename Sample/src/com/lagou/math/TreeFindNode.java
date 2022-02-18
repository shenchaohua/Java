package com.lagou.math;

import java.util.LinkedList;

public class TreeFindNode {
    public static void main(String[] args) {
        // create tree
        TreeNode root = new TreeNode(0);
        TreeNode node1 = new TreeNode(1);
        TreeNode node2 = new TreeNode(2);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(4);
        TreeNode node5 = new TreeNode(5);
        TreeNode node6 = new TreeNode(6);
        TreeNode node7 = new TreeNode(7);
        TreeNode node8 = new TreeNode(8);
        root.left = node1;
        root.right = node2;
        node1.left = node3;
        node1.right = node4;
        node3.left = node5;
        node3.right = node6;
        node6.left = node7;
        node6.right = node8;

        // test
        LinkedList<TreeNode> path = new LinkedList<>();
        // get the path to node4
        boolean hasPath = getPathToTarget(root, node4, path);
        if (hasPath) {
            for (TreeNode node : path) {
                System.out.print(node.val + " ");
            }
        }
        System.out.println();
        System.out.println("============");
        path.clear();
        // get the path to nonexistent node
        hasPath = getPathToTarget(root, new TreeNode(9), path);
        if (hasPath) {
            for (TreeNode node : path) {
                System.out.print(node.val + " ");
            }
        }
    }

    public static boolean getPathToTarget(TreeNode node, TreeNode target,
                                          LinkedList<TreeNode> path) {
        if (node == null)
            return false;
        path.push(node);
        if (node == target)
            return true;
        // find in left tree
        if (getPathToTarget(node.left, target, path))
            return true;
        // find in right tree
        if (getPathToTarget(node.right, target, path))
            return true;
        // this node is not in the path of target
        // cause leftchild rightchild and itself do not have target node
        path.pop();
        return false;
    }
}

//class TreeNode {
//
//    int val;
//    TreeNode left;
//    TreeNode right;
//    TreeNode(int x) {
//        val = x; }
//}