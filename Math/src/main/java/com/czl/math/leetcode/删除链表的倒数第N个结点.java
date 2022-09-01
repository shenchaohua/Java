package com.czl.math.leetcode;



public class 删除链表的倒数第N个结点 {

    public static Node deleteN(Node head, int n) {

        Node newHead = new Node(0, head);

        Node slow = newHead;
        Node fast = head;

        for (int i = 0; i < n; i++) {
            if (fast == null) {
                return head;
            }
            fast = fast.next;
        }

        while (fast!=null) {
            fast = fast.next;
            slow = slow.next;
        }
        slow.next = slow.next.next;
        return newHead.next;
    }

    public static void main(String[] args) {
        Node a = new Node(1, new Node(2, new Node(3, null)));
        Node node = deleteN(a, 1);
        while (node != null) {
            System.out.println(node.val);
            node = node.next;
        }
    }

    static class Node {

        public int val;
        public Node next;

        public Node(int val, Node next) {
            this.val = val;
            this.next = next;
        }
    }
}
