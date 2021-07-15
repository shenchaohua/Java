package com.lagou.math;

import java.util.ArrayList;
import java.util.LinkedList;

class TreeNode<T>{
    T val;
    TreeNode<T> left;
    TreeNode<T> right;

    public TreeNode(T val) {
        this.val = val;
    }
}

public class FindNode {

    public static <T> boolean getPathToTarget(TreeNode<T> node, TreeNode<T> target,
                                          LinkedList<TreeNode<T>> path) {
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
    public static void main(String[] args) {


        // create tree
        TreeNode<Integer> root = new TreeNode<>(0);
        TreeNode<Integer> node1 = new TreeNode<>(1);
        TreeNode<Integer> node2 = new TreeNode<>(2);
        TreeNode<Integer> node3 = new TreeNode<>(3);
        TreeNode<Integer> node4 = new TreeNode<>(4);
        TreeNode<Integer> node5 = new TreeNode<>(5);
        TreeNode<Integer> node6 = new TreeNode<>(6);
        TreeNode<Integer> node7 = new TreeNode<>(7);
        TreeNode<Integer> node8 = new TreeNode<>(8);
        root.left = node1;
        root.right = node2;
        node1.left = node3;
        node1.right = node4;
        node3.left = node5;
        node3.right = node6;
        node6.left = node7;
        node6.right = node8;

        // test
        LinkedList<TreeNode<Integer>> path = new LinkedList<>();
        // get the path to node4
        boolean hasPath = getPathToTarget(root, node4, path);
        if (hasPath) {

            for (TreeNode<Integer> node : path) {

                System.out.print(node.val + " ");
            }
        }
        System.out.println();
        System.out.println("============");
        path.clear();
        // get the path to nonexistent node
        hasPath = getPathToTarget(root, new TreeNode<Integer>(9), path);
        if (hasPath) {

            for (TreeNode<Integer> node : path) {

                System.out.print(node.val + " ");
            }
        }

    }
}
