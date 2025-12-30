import java.util.List;

public class Output {
    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = null;

        for(ListNode curr = node1; curr != null; curr = curr.next) {
            System.out.println(curr.val);
        }


    }

}
        class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
