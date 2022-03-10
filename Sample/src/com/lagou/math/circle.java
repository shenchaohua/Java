import javax.swing.text.ParagraphView;
import java.util.*;

/*
循环依赖问题
 */
public class circle {

    public static boolean findCircle (List<String[]> list) {
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indo = new HashMap<>();

        for (String[] strings : list) {
            String ai = strings[0];
            String bi = strings[1];
            List<String> dependencyList = graph.getOrDefault(bi, new ArrayList<>());
            dependencyList.add(ai);
            graph.put(bi, dependencyList);
            indo.put(ai, indo.getOrDefault(ai, 0)+1);
            indo.put(bi, 0);
        }

        List<String> queue = new ArrayList<>();
        indo.forEach( (k, v) -> {
            if (v ==0 ) {
                queue.add(k);
            }
        });

        for (int i=0; i< queue.size();i++) {
            List<String> dependencyList = graph.getOrDefault(queue.get(i), new ArrayList<>());
            for (String par : dependencyList) {
                int cnt = indo.get(par) - 1;
                indo.put(par, cnt);
                if (cnt==0) {
                    queue.add(par);
                }
            }
        }
        for (Integer value : indo.values()) {
            if (value != 0 ) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        List<String[]> strings = Arrays.asList(new String[]{"A", "B"}, new String[]{"B", "C"}, new String[]{"C", "A"});
        System.out.println(findCircle(strings));
    }
}
